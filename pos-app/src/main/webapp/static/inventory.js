


//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	if(!validateFormHTML($form)){return;}
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		
		$('#add-inventory-modal').modal('toggle');
		$("#inventory-table").DataTable().ajax.reload();
		Swal.fire({
			icon: "success",
			title: "Yay!!",
			text: "Inventory added successfully",
			timer: 1500,
		})
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateInventory(event){
	//Get the ID	
	var url = getInventoryUrl();

	//Set the values to update
	var $form = $("#inventory-edit-form");
	if(!validateFormHTML($form)){return;}
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		
		$('#edit-inventory-modal').modal('toggle');
		$("#inventory-table").DataTable().ajax.reload();
		Swal.fire({
			icon: "success",
			title: "Yay!!",
			text: "Inventory updated successfully",
			timer: 1500,
		});
	   },
	   error: handleAjaxError
	});

	return false;
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];


function processData(){
	var file = $('#inventoryFile')[0].files[0];

	if(!file){
		Swal.fire({
			icon: "error",
			title: "Oops...",
			text: "Please select a file to upload.",
		});
		return;
	}
	if(file.name.split('.').pop() != "tsv"){
		Swal.fire({
			icon: "error",
			title: "Oops...",
			text: "Invalid file format.Please upload a '.tsv' file.",
			
		});
		resetUploadDialog();
		return;
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	if(!checkJsonKeys(fileData[0], ["barcode", "quantity"])){
		Swal.fire({
			icon: "error",
			title: "Oops!!",
			text: "Invalid file format. Please download the sample file and upload again.",
		});
	}
	//Update progress
	var url = getInventoryUrl() + "/bulk-add";

	if(fileData.length > 5000){
		Swal.fire({
			icon: "error",
			title: "Oops...",
			text: "Too many rows. Please upload less than 5000 rows at a time.",
			
		});
		return;
	}

	$.ajax({
		url: url,
		type: 'POST',
		data: JSON.stringify(fileData),
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			
			updateUploadDialog();
			Swal.fire({
				icon: "success",
				title: "Yay!!",
				text: "Inventory uploaded successfully",
				timer: 1500,
			});
			return;

		},
		error: function(error){
			errorData = JSON.parse(JSON.parse(error.responseText).message);
			//enable download-errors button
			$('#download-errors').attr('disabled', false);

			//change order of json keys to order "barcode, quantity, error"
			for(var i = 0; i < errorData.length; i++){
				var temp = errorData[i];
				errorData[i] = {};
				errorData[i]["barcode"] = temp["barcode"];
				errorData[i]["quantity"] = temp["quantity"];
				errorData[i]["error"] = temp["error"];
			}

			updateUploadDialog();
			Swal.fire({
				icon: "error",
				title: "Oops...",
				text: "Error uploading inventory check error file.",
				timer: 1500,
			});
			return;
		}
	 });

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS


function displayEditInventory(barcode){
	var url = getInventoryUrl()+"/"+barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//disable download-errors button

	$('#download-errors').attr('disabled', true);

	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file[0].files[0].name;
	$('#inventoryFileName').html(fileName);
}

function displayAddInventory(){
	$("#inventory-form").trigger("reset");
	$('#add-inventory-modal').modal('toggle');
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);	
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);

	$('#inventory-edit-form').on('input change', function() {
		let quantity = $("#inventory-edit-form input[name=quantity]").val();

		if(quantity == data.quantity){
			$('#update-inventory').attr('disabled', true);
		}
		else{
			$('#update-inventory').attr('disabled', false);
		}
	});
	$('#update-inventory').attr('disabled', true);

	$('#edit-inventory-modal').modal('toggle');
}


function downloadReport(){
	var url = getReportsUrl() + "/inventory";

    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            const binary = atob(response);
            const array = new Uint8Array(binary.length);
            for (let i = 0; i < binary.length; i++) {
                array[i] = binary.charCodeAt(i);
            }
            const blob = new Blob([array], {
                type: 'application/pdf'
            });

            const a = document.createElement('a');
            //generate current datetime
			var date = new Date();
			a.href = URL.createObjectURL(blob);
            a.download = 'inventoryReport-'+date+'.pdf';
            a.click();
        },
        error: function (error) {
			Swal.fire({
				icon: "error",
				title: "Oops...",
				text: error.responseJSON.message,
				timer: 1500,
			});
        }
    });
}

function getConditionalColumns(){
	var columns = columns = [
		{ "data": "barcode" },
		{ "data": "quantity" },
	];

	if(user == 'SUPERVISOR'){
		columns.push({
				"data":null,
				"render":function(o){return '<button class="btn" title="edit" onclick="displayEditInventory(\'' + o.barcode + '\')"><img src='+editButton+ '></button>'}
			}
		);
	}

	return columns;
}

//INITIALIZATION CODE
function init(){
	$('.active').removeClass('active');
	$('#a-nav-inventory').addClass('active');

	$('#add-inventory-modal-toggle').html('<img src='+addNewButton+'></img>');
	$('#upload-data').html('<img src='+uploadFileButton+'></img>');
	$('#refresh-data').html('<img src='+refreshButton+'></img>');
	$('#download-report').html('<img src='+downloadReportButton+'></img>');

	$('#inventory-table').DataTable( {
		language: {
			searchPlaceholder: "Bacode"
		},
		"ordering": false,
		"processing": true,
		"serverSide": true,
		"lengthMenu": [2,5,10,20, 40, 60, 80, 100],
		"pageLength":10,
		"ajax": {url : getInventoryUrl()},
		"columns": getConditionalColumns()
	});

	$('#add-inventory-modal-toggle').click(displayAddInventory);
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(function(){$("#inventory-table").DataTable().ajax.reload()});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
	$('#download-report').click(downloadReport);

	
}

$(document).ready(init);


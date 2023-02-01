


//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
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
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID	
	var url = getInventoryUrl();

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
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
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	var url = getInventoryUrl() + "/bulk-add";

	$.ajax({
		url: url,
		type: 'POST',
		data: JSON.stringify(fileData),
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			processCount = fileData.length;
			
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
			console.log(error);
			errorData = JSON.parse(JSON.parse(error.responseText).message);
			console.log(errorData);
			processCount = fileData.length;	 
			
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
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
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
		{ "data": "quantity", className: "text-right" },
	];

	if(user == 'supervisor'){
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
	$('#add-inventory-modal-toggle').html('<img src='+addNewButton+'></img>');
	$('#upload-data').html('<img src='+uploadFileButton+'></img>');
	$('#refresh-data').html('<img src='+refreshButton+'></img>');
	$('#download-report').html('<img src='+downloadReportButton+'></img>');
	$('#inventory-table').DataTable( {
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


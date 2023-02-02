
//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	if(!validateFormHTML($form)){return;}
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
			$("#brand-table").DataTable().ajax.reload();
			Swal.fire({
				icon: "success",
				title: "Yay!!",
				text: "Brand added successfully",
				timer: 1500,
			})
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
	$('#update-brand').attr('disabled', true);
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
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
			$("#brand-table").DataTable().ajax.reload();
			Swal.fire({
				icon: "success",
				title: "Yay!!",
				text: "Brand updated successfully",
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
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function checkJsonKeys(json){
	
	var keys = ["name", "category"];
	for(var i in keys){
		if(!json.hasOwnProperty(keys[i])){
			return false;
		}
	}

	return true;
}

function uploadRows(){
	//Upload progress

	if(!checkJsonKeys(fileData[0])){
		Swal.fire({
			icon: "error",
			title: "Oops...",
			text: "Invalid file format. Please download the sample file and upload again.",
			
		});
		return;
	}

	var url = getBrandUrl() + "/bulk-add";

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
				text: "Brands uploaded successfully",
				timer: 1500,
			});
			return;	 
		},
		error: function(error){
			console.log(error);
			errorData = JSON.parse(JSON.parse(error.responseText).message);
			console.log(errorData); 
			
			updateUploadDialog();
			Swal.fire({
				icon: "error",
				title: "Oops...",
				text: "Error uploading brands check error file.",
			});
			return;
		}
	 });

}

function downloadErrors(){
	writeFileData(errorData);
}


function displayEditBrand(id){
	console.log("here1")
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		console.log("here2");
	   		displayBrand(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}

function displayAddBrand(){
	$("#brand-form").trigger("reset");
	$('#add-brand-modal').modal('toggle');
}

function displayBrand(data){
	console.log("here");
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}

function downloadReport(){
	var url = getReportsUrl() + "/brands";

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
            a.download = 'brandReport-'+date+'.pdf';
            a.click();
        },
        error: function (error) {
			Swal.fire({
				icon: "error",
				title: "Oops...",
				text: error.responseJSON.message,
			});
        }
    });
}


function getConditionalColumns(){
	var i = 1;
	var columns = [
		{
			render: function (data, type, row, meta) {
				return meta.row + meta.settings._iDisplayStart + 1;
			}
		},
		{ "data": "brand" },
		{ "data": "category" },
	];
	if(user == "supervisor"){
		columns.push(
			{
				"data":null,
				"render":function(o){return '<button class="btn" title="edit" onclick="displayEditBrand(' + o.id + ')" ><img src='+editButton+ '></button>'}
			}
		);
	}

	return columns;
};

//INITIALIZATION CODE
function init(){
	$('.active').removeClass('active');
	$('#a-nav-brand').addClass('active');


	$("#add-brand-modal-toggle").html('<img src='+addNewButton+ '>');
	$("#refresh-data").html('<img src='+refreshButton+ '>');
	$("#upload-data").html('<img src='+uploadFileButton+ '>');
	$("#download-report").html('<img src='+downloadReportButton+ '>');
	$('#brand-table').DataTable( {
        "ordering": false,
		"processing": true,
		"serverSide": true,
		"lengthMenu": [2,5,10,20, 40, 60, 80, 100],
		"pageLength":10,
		"ajax": {url : getBrandUrl()},
		"columns": getConditionalColumns()
	});

	$("#add-brand-modal-toggle").click(displayAddBrand);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(function(){$("#brand-table").DataTable().ajax.reload()});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
	$('#download-report').click(downloadReport);

	$('#brand-edit-form').on('input change', function() {
		$('#update-brand').attr('disabled', false);
	});
	
}

$(document).ready(init);


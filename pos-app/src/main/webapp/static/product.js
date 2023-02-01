

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		$("#product-table").DataTable().ajax.reload();
		Swal.fire({
			icon: "success",
			title: "Yay!!",
			text: "Product added successfully",
			timer: 1500,
		})
	   },
	   error: handleAjaxError
	});

	return false;
}

function getBrandList(){
	var url = getBrandUrl()+ "?draw=1&start=0&length=100";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		data = data.data;
	   		var brandSelect = $("#brand-select");
            var categorySelect  = $("#category-select");
            var brandEditSelect = $("#brand-edit-select");
            var categoryEditSelect  = $("#category-edit-select");
            
            for(var i in data){
                brandSelect.append("<option value='"+data[i].brand+"'>"+data[i].brand+"</option>");
                categorySelect.append("<option value='"+data[i].category+"'>"+data[i].category+"</option>");
                brandEditSelect.append("<option value='"+data[i].brand+"'>"+data[i].brand+"</option>");
                categoryEditSelect.append("<option value='"+data[i].category+"'>"+data[i].category+"</option>");
            }
	   },
	   error: handleAjaxError
	});
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		$("#product-table").DataTable().ajax.reload();
		Swal.fire({
			icon: "success",
			title: "Yay!!",
			text: "Product updated successfully",
			timer: 1500,
		})
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
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Upload progress
	var url = getProductUrl() + "/bulk-add";

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
				text: "Products uploaded successfully",
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
				title: "Oops!!",
				text: "Error uploading products. Please download the error file and correct the errors.",
			});
			return;
		}
	 });
}

function downloadErrors(){
	writeFileData(errorData);
}



function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayAddProduct(){
	$("#product-form").trigger("reset");	
	$('#add-product-modal').modal('toggle');
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);	
	$("#product-edit-form input[name=brand]").val(data.brand);	
	$("#product-edit-form input[name=category]").val(data.category);		
	$("#product-edit-form input[name=name]").val(data.name);	
	$("#product-edit-form input[name=mrp]").val(data.mrp);			
	$("#product-edit-form input[name=id]").val(data.id);	
	$('#edit-product-modal').modal('toggle');
}

function getConditionalColumns(){
	var columns = [
		{ "data": "id" },
		{ "data": "barcode" },
		{ "data": "brand" },
		{ "data": "category" },
		{ "data": "name" },
		{ 
			"data":null,
			"render":function(o){return parseFloat(o.mrp).toFixed(2)} , 
			className: "text-right" 
		},
	];
	if(user == 'supervisor'){
		columns.push(
			{
				"data":null,
				"render":function(o){return '<button class="btn" title="edit" onclick="displayEditProduct(' + o.id + ')"><img src='+editButton+ '></button>'}
			}
		);
	}
	return columns;
}

//INITIALIZATION CODE
function init(){
	$("#add-product-modal-toggle").html('<img src='+addNewButton+'></img>');
	$("#upload-data").html('<img src='+uploadFileButton+'></img>');
	$("#refresh-data").html('<img src='+refreshButton+'></img>');

	$('#product-table').DataTable( {
		"ordering": false,
		"processing": true,
		"serverSide": true,
		"lengthMenu": [2,5,10,20, 40, 60, 80, 100],
		"pageLength":10,
		"ajax": {url : getProductUrl()},
		"columns": getConditionalColumns()
	});
    getBrandList();

	$("#add-product-modal-toggle").click(displayAddProduct);
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(function(){$("#product-table").DataTable().ajax.reload()});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
	$('.select2').select2();
}

$(document).ready(init);


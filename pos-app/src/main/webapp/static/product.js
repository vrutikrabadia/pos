
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

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
		$("#product-table").DataTable().ajax.reload() 
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
		$("#product-table").DataTable().ajax.reload()   
	   },
	   error: handleAjaxError
	});

	return false;
}




function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
		$("#product-table").DataTable().ajax.reload()  
	   },
	   error: handleAjaxError
	});
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
			return;	 
		},
		error: function(error){
			console.log(error);
			errorData = JSON.parse(JSON.parse(error.responseText).message);
			console.log(errorData);
			processCount = fileData.length;	 
			
			updateUploadDialog();
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


//INITIALIZATION CODE
function init(){
	$('#product-table').DataTable( {
		"ordering": false,
		"processing": true,
		"serverSide": true,
		"lengthMenu": [2,5,10,20, 40, 60, 80, 100],
		"pageLength":10,
		"ajax": {url : getProductUrl()},
		"columns": [
            { "data": "id" },
            { "data": "barcode" },
            { "data": "brand" },
            { "data": "category" },
            { "data": "name" },
            { "data": "mrp" },
			{
				"data":null,
				"render":function(o){return '<button onclick="displayEditProduct(' + o.id + ')">edit</button>'}
			}
        ]
	});
    getBrandList();
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click($("#product-table").DataTable().ajax.reload());
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}

$(document).ready(init);



// create hashmap of key and array of values
var map = {};
//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	if(!validateFormHTML($form)){return;}
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
		
		$('#add-product-modal').modal('toggle');
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
	var url = getBrandUrl()+ "?draw=1&start=0&length=100&search[value]=";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		data = data.data;
	   		var brandSelect = $("#brand-select");
            var categorySelect  = $("#category-select");
            
            for(var i in data){
				if(map[data[i].brand] == undefined){
					map[data[i].brand] = [];
				}
				map[data[i].brand].push(data[i].category);

                brandSelect.append("<option value='"+data[i].brand+"'>"+data[i].brand+"</option>");
                categorySelect.append("<option value='"+data[i].category+"'>"+data[i].category+"</option>");
            }
	   },
	   error: handleAjaxError
	});
}

function populateAddCategoryList(){
	let brand = $("#brand-select").val();

	$("#category-select").empty();
	
	//populate category list using the map
	for(var i in map[brand]){
		$("#category-select").append("<option value='"+map[brand][i]+"'>"+map[brand][i]+"</option>");
	}
}

function updateProduct(event){
	
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl();

	//Set the values to update
	var $form = $("#product-edit-form");
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
		
		$('#edit-product-modal').modal('toggle');
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


function processData(){
	var file = $('#productFile')[0].files[0];

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

	if(!checkJsonKeys(fileData[0], ["name", "brand", "category", "price", "quantity"])){
		Swal.fire({
			icon: "error",
			title: "Oops!!",
			text: "Invalid file format. Please download the sample file and upload again.",
		});
	}
	//Upload progress
	var url = getProductUrl() + "/bulk-add";

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
				text: "Products uploaded successfully",
				timer: 1500,
			});
			return;	 
		},
		error: function(error){
			errorData = JSON.parse(JSON.parse(error.responseText).message);
			//enable download-errors button
			$('#download-error').attr("disabled", false);

			//change order of json keys in errorData to match the order in the sample file
			for(var i in errorData){
				var temp = errorData[i];
				errorData[i] = {};
				errorData[i].name = temp.name;
				errorData[i].brand = temp.brand;
				errorData[i].category = temp.category;
				errorData[i].price = temp.price;
				errorData[i].error = temp.error;
			}

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
	//disable download-error button
	$('#download-error').attr('disabled', true);

	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file[0].files[0].name;
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

	$('#product-edit-form').on('input change', function() {

		let nameVal = $("#product-edit-form input[name=name]").val();
		let mrpVal = $("#product-edit-form input[name=mrp]").val();

		if(nameVal == data.name && mrpVal == data.mrp){
			$('#update-product').attr('disabled', true);
		}
		else{
			$('#update-product').attr('disabled', false);
		}
	});


	$('#update-product').attr('disabled',true);	
	$('#edit-product-modal').modal('toggle');

	
}

function getConditionalColumns(){
	var columns = [
		{
			render: function (data, type, row, meta) {
				return meta.row + meta.settings._iDisplayStart + 1;
			}
		},
		{ "data": "barcode" },
		{ "data": "brand" },
		{ "data": "category" },
		{ "data": "name" },
		{ 
			"data":null,
			"render":function(o){return parseFloat(o.mrp).toFixed(2)} 
		},
	];
	if(user == 'SUPERVISOR'){
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
	$('.active').removeClass('active');
	$('#a-nav-product').addClass('active');

	$("#add-product-modal-toggle").html('<img src='+addNewButton+'></img>');
	$("#upload-data").html('<img src='+uploadFileButton+'></img>');
	$("#refresh-data").html('<img src='+refreshButton+'></img>');

	$('#product-table').DataTable( {
		"language": {
			"searchPlaceholder": "Name / Barcode / Brand / Category"
		},
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
	$("#brand-select").change(populateAddCategoryList);
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(function(){$("#product-table").DataTable().ajax.reload()});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
	$('.select2').select2({ width: '100%' });

	
}

$(document).ready(init);

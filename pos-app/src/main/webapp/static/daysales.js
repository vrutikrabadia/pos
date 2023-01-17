
function getDaySalesUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/day-sales";
}



function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}


function searchDateRange(){
    var startDate = $("#inputStartDate").val();
    var endDate = $("#inputEndDate").val();
    // Set dynamic parameters for the data table
    $('#day-sales-table').data('dt_params', { startDate: startDate, endDate: endDate });
    // Redraw data table, causes data to be reloaded
    $('#day-sales-table').DataTable().draw();

    
}

function refresh(){
     // Set dynamic parameters for the data table
     $('#day-sales-table').data('dt_params', { startDate: "", endDate: "" });
     // Redraw data table, causes data to be reloaded
     $('#day-sales-table').DataTable().draw();
}

//INITIALIZATION CODE
function init(){

	$('#day-sales-table').DataTable( {
        "ordering": false,
		"processing": true,
		"serverSide": true,
        "searching":false,
		"lengthMenu": [2,5,10,20, 40, 60, 80, 100],
		"pageLength":10,
		"ajax": {
            url: getDaySalesUrl(),
            dataType: 'json',
            cache:false,
            type: 'GET',
            data: function ( d ) {
               d.supersearch = $('.my-filter').val();
     
               // Retrieve dynamic parameters
               var dt_params = $('#day-sales-table').data('dt_params');
               // Add dynamic parameters to the data object sent to the server
               if(dt_params){ $.extend(d, dt_params); }
            }},
		"columns": [
            { "data": "date" },
            { "data": "itemsCount" },
            { "data": "orderCount" },
            { "data": "totalRevenue" },
            
        ]
	});

	$('#refresh-data').click(refresh);
    $('#search-date-range').click(searchDateRange);
	
}

$(document).ready(init);


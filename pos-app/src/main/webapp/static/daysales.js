

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}


function searchDateRange(){


    var startDate = new Date($("#inputStartDate").val()).toISOString();
    var endDate = new Date($("#inputEndDate").val()).toISOString();

    if(startDate>endDate){
        Swal.fire({
            icon: "error",
            title: "Oops!!",
            text: "Start date cannot be greater than end date",
        });
    }
    var table = $('#day-sales-table').DataTable();
    table.ajax.url(getDaySalesUrl()+"/dateRange?startDate="+startDate+"&endDate="+endDate).load();

    
}

function refresh(){
    var table = $('#day-sales-table').DataTable();
    table.ajax.url(getDaySalesUrl()).load();
}


function downloadReport(){
    console.log("downloadReport");
    //select input from form id
    var startDate = new Date($("#downlaodInputStartDate").val()).toISOString();
    var endDate = new Date($("#downloadInputEndDate").val()).toISOString();
    var brand = $("#inputBrand").val();
    var category = $("#inputCategory").val();

    var data = {
        startDate: startDate,
        endDate: endDate,
        brand: brand,
        category: category
    }

    var url = getReportsUrl() + "/sales";

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(data),
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
                title: 'Oops...',
                text: error.responseJSON.message,
                type: 'error',
            });
        }
    });
    //clear form using form id
    $('#sales-report-form').trigger("reset");
}

//INITIALIZATION CODE
function init(){
    $('.active').removeClass('active');
	$('#a-nav-sales').addClass('active');

    let now = new Date();
    let today = now.toISOString().slice(0,10);
    let firstDay = new Date(now.getFullYear(), now.getMonth(), 1).toISOString().slice(0,10);


    $('#inputStartDate').val(firstDay);
    $('#inputEndDate').val(today);

    $('#downlaodInputStartDate').val(firstDay);
    $('#downloadInputEndDate').val(today);

    $('#search-date-range').html('<img src='+filterButton+' />');
    $('#refresh-data').html('<img src='+clearFilterButton+' />');
    $('#download-report-button').html('<img src='+downloadReportButton+' />');

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
            { "data": null,
            "render": function (o) {
                return  new Date(o.date).toLocaleString();
            } 
        },
            { "data": "invoicedItemsCount" },
            { "data": "invoicedOrderCount" },
            { 
                "data":null,
                "render":function(o){return parseFloat(o.totalRevenue).toFixed(2)} , 
                className: "text-right" 
            },
            
        ]
	});

	$('#refresh-data').click(refresh);
    $('#search-date-range').click(searchDateRange);
	$('#download-report-button').click(function(){
        $('#download-report-modal').modal('toggle');
    });
    $('#download-report').click(downloadReport);
}

$(document).ready(init);


var wholeOrder = []
var currentInventory = new Map();


function resetForm() {
    var element = document.getElementById("order-item-form");
    element.reset()
}

function deleteOrderItem(id) {
    currentInventory.set(wholeOrder[id].barcode, currentInventory.get(wholeOrder[id].barcode) + parseInt(wholeOrder[id].quantity));
    wholeOrder.splice(id, 1);
    displayOrderItemList();
}

function editOrderItem(id){
    var currBarcode = $('#order-item-form input[name=barcode]').val();
    var currQuantity = $('#order-item-form input[name=quantity]').val();
    var currSellingPrice = $('#order-item-form input[name=sellingPrice]').val();

    if(currBarcode != "" && currQuantity != "" && currSellingPrice != ""){
        addOrderItem();
    }
    var barcode = wholeOrder[id].barcode;
    var quantity = wholeOrder[id].quantity;
    var sellingPrice = wholeOrder[id].sellingPrice;
    $('#order-item-form input[name=barcode]').val(barcode);
    $('#order-item-form input[name=quantity]').val(quantity);
    $('#order-item-form input[name=sellingPrice]').val(sellingPrice);
    deleteOrderItem(id);
}

function displayOrderItemList(data) {
    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();

    // logic is flawedshould be in a loop
    for (var i in wholeOrder) {
        var e = wholeOrder[i];
        var buttonHtml = '<button class="btn" title="edit" onclick="editOrderItem(' + i + ')" class="btn"><img src='+editButton+ '></button><button class="btn" title="remove" onclick="deleteOrderItem(' + i + ')" class="btn"><img src='+deleteButton+ '></button>';
        var row = '<tr>' +
            '<td>' + wholeOrder[i].barcode + '</td>' +
            '<td>' + wholeOrder[i].quantity + '</td>' +
            '<td>' + wholeOrder[i].sellingPrice + '</td>' +
            '<td>' + buttonHtml + '</td>' +
            '</tr>';

        $tbody.append(row);
    }

}

function checkOrderItemExist() {
    for (i in wholeOrder) {
        var barcode = wholeOrder[i].barcode;
        var temp_barcode = $("#order-item-form input[name=barcode]").val();
        if (temp_barcode == barcode) {
            return true;
        }
    }
    return false;
}


function changeQuantity(json) {

    for (i in wholeOrder) {
        if (wholeOrder[i].barcode == json.barcode) {
            var prev = parseInt(wholeOrder[i].quantity);

            currentInventory.set(json.barcode, parseInt(currentInventory.get(json.barcode)) + parseInt(prev));

            var new_quantity = parseInt(prev) + parseInt(json.quantity);

            if (new_quantity <= currentInventory.get(json.barcode)) {
                json.quantity = new_quantity;
                wholeOrder[i] = json;
            } else {
                currentInventory.set(json.barcode, currentInventory.get(json.barcode) -  parseInt(prev));
                Swal.fire({
                    title: 'Oops...',
                    text: 'quantity not sufficient for this item',
                    icon: 'error',
                    
                })
            }

        }
    }
    console.log(wholeOrder);
}

function checkSellingPrice(json) {
    console.log(json);
    for (i in wholeOrder) {
        if (wholeOrder[i].barcode == json.barcode) {
            if (parseInt(wholeOrder[i].sellingPrice) == parseInt(json.sellingPrice)) {
                return true;
            }
        }
    }
    return false;
}


function addOrderItem(event) {

    
    var $form = $("#order-item-form");
    var json = JSON.parse(toJson($form));


    if(json.barcode == "" || json.quantity == "" || json.sellingPrice == ""){
        Swal.fire({
            title: 'Oops...',
            text: 'Please enter the details',
            icon: 'error',
        });
        return;
    }

    if(json.quantity <= 0 || json.quantity > currentInventory.get(json.barcode)){
            Swal.fire({
                title: 'Oops...',
                text: 'Please enter valid quantity in range 1 to ' + currentInventory.get(json.barcode),
                icon: 'error',
            });
            return;
    }

    if(json.sellingPrice <= 0){
        Swal.fire({
            title: 'Oops...',
            text: 'Please enter valid selling price >= 0',
            icon: 'error',
        });
        return;
    }


    if(!validateFormHTML($form)){return;}

    

    if (checkOrderItemExist()) {

        if (checkSellingPrice(json) == false) {
            Swal.fire({
                title: 'Oops...',
                text: 'Selling price cannot be different',
                icon: 'error',
            });
        } else {
            changeQuantity(json);
        }
    } else {
        wholeOrder.push(json)
    }

    currentInventory.set(json.barcode, currentInventory.get(json.barcode) - json.quantity);
    resetForm();

    displayOrderItemList();

}

function displayCart() { 

    for(i in wholeOrder){
        currentInventory.set(wholeOrder[i].barcode, currentInventory.get(wholeOrder[i].barcode) + parseInt(wholeOrder[i].quantity));
    }
    wholeOrder = [];
    $('#add-order-item-modal').modal('toggle');
    // table should be empty
    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
}

function getOrderItemList() {
    var jsonObj = $.parseJSON('[' + wholeOrder + ']');
    console.log(jsonObj);
}

function insufficientQuantityAlert(input, inv){
    if(input.value >  inv|| input.value < 1){
        Swal.fire({
            title: 'Oops...',
            text: 'Insufficient inventory',
            icon: 'error',
        });
        input.value = null;
    }
}

function checkInventory() {
    var $form = $("#search-item-form");
    if(!validateFormHTML($form)){return;}
    var barcode = $("#search-item-form input[name=barcode]").val();

    if (currentInventory.has(barcode)) {
        console.log("here");
        var inv = currentInventory.get(barcode);
        $('#quantity-informer').text("Quantity Available: " + inv);
        $('#order-item-form input[name=barcode]').val(barcode);
        $('#order-item-form input[name=quantity]').attr({
            "max": inv
        });

        return;
    }

    var url = getInventoryUrl() + "/" + barcode;

    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            currentInventory.set(response.barcode, response.quantity);
            $('#quantity-informer').text("Quantity Available: " + response.quantity);
            $('#order-item-form input[name=barcode]').val(response.barcode);
            $('#order-item-form input[name=quantity]').attr({
                "max": response.quantity
            });
        },
        error: function (error) {
            Swal.fire({
                title: 'Oops...',
                text: error.responseJSON.message,
                icon: 'error',
            });
        }
    });
}

function displayOrder(id) {

    $('#view-order-item-modal').modal('toggle');

    var url = getOrderUrl() + "/" + id + "/items";

    var $tbody = $('#view-order-item-table').find('tbody');
    $tbody.empty();

    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            let count = 1;
            $("#view-order-id").text(id);
            response.forEach(element => {
                var row = '<tr>' +
                    '<td>' + count++ + '</td>' +
                    '<td>' + element.barcode + '</td>' +
                    '<td>' + element.quantity + '</td>' +
                    '<td>' + parseFloat(element.sellingPrice).toFixed(2) + '</td>' +
                    '</tr>';

                $tbody.append(row);
            });
        },
        error: function (error) {
            Swal.fire({
                title: 'Oops...',
                text: error.responseJSON.message,
                icon: 'error',
            });
        }
    });

}

function downloadInvoice(orderId) {

    var url = getOrderUrl() + "/" + orderId + "/invoice";

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
            a.href = URL.createObjectURL(blob);
            a.download = 'invoice'+orderId+'.pdf';
            a.click();
        },
        error: function (error) {
            Swal.fire({
                title: 'Oops...',
                text: error.responseJSON.message,
                icon: 'error',
            });
        }
    });
}


function placeOrder() {
    var url = getOrderUrl();

    if(wholeOrder.length == 0){
        Swal.fire({
            title: 'Oops...',
            text: 'Please add items to the order',
            icon: 'error',
        });
        return;
    }
    let data = {
        "orderItems": wholeOrder
    };
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $('#add-order-item-modal').modal('toggle');
            wholeOrder = []
            $("#Order-table").DataTable().ajax.reload();
            Swal.fire({
                title: 'Yay!!',
                text: 'Order Placed Successfully',
                icon: 'success',
                timer: 1500,
            });
        },
        error: handleAjaxError
    });

    return false;
}

function init() {

    $('.active').removeClass('active');
	$('#a-nav-order').addClass('active');

    $("#add-order").html('<img src='+addNewButton+ '>');
    $('#refresh-data').html('<img src='+refreshButton+ '>');

    $('#Order-table').DataTable({
        language: {
			searchPlaceholder: "Order Id"
		},
        "ordering": false,
        "processing": true,
        "serverSide": true,
        "lengthMenu": [2, 5, 10, 20, 40, 60, 80, 100],
        "pageLength": 10,
        "ajax": {
            url: getOrderUrl()
        },
        "columns": [{
                "data": "id"
            },
            {
                "data": null,
                "render": function (o) {
                    return new Date(o.updated).toLocaleString();
                }
            },
            {
                "data": null,
                "render": function (o) {
                    return '<button class="btn" title="view" onclick="displayOrder(' + o.id + ')"><img src='+viewButton+ '></button><button class="btn" title="download invoice" onclick="downloadInvoice(' + o.id + ')"><img src='+downloadButoon+ '></button>'
                }
            }
        ]
    });

    $("#add-order-item-modal").on('shown', function () {
        wholeOrder = [];
    });

    $('#add-order').click(displayCart);
    $('#add-order-item').click(addOrderItem);
    $('#place-order').click(placeOrder);
    $('#search-order-item').click(checkInventory);
    $('#refresh-data').click(function(){$("#Order-table").DataTable().ajax.reload()});
}

$(document).ready(init);
$(document).ready(getOrderItemList);
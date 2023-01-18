var wholeOrder = []
var currentInventory = new Map();

function getOrderItemUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/orders/items";
}

function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/orders";
}

function getProductUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/products";
}

function getInventoryUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/inventory";
}

function resetForm() {
    var element = document.getElementById("order-item-form");
    element.reset()
}

function deleteOrderItem(id) {
    wholeOrder.splice(id, 1);
    displayOrderItemList();
}

function displayOrderItemList(data) {
    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();

    // logic is flawedshould be in a loop
    for (var i in wholeOrder) {
        var e = wholeOrder[i];
        var buttonHtml = '<button onclick="deleteOrderItem(' + i + ')" class="btn">delete</button>';
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

            var new_quantity = parseInt(prev) + parseInt(json.quantity);

            console.log(currentInventory.get(json.barcode));

            if (new_quantity <= currentInventory.get(json.barcode)) {
                json.quantity = new_quantity;
                wholeOrder[i] = json;
            } else {
                alert("quantity not sufficient for this item");
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

    if (checkOrderItemExist()) {

        if (checkSellingPrice(json) == false) {
            alert("Selling price cannot be different");
        } else {
            changeQuantity(json);
        }
    } else {
        wholeOrder.push(json)
    }
    resetForm();

    displayOrderItemList();

}

function displayCart() {
    $('#add-order-item-modal').modal('toggle');
    // table should be empty
    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
}

function getOrderItemList() {
    var jsonObj = $.parseJSON('[' + wholeOrder + ']');
    console.log(jsonObj);
}


function checkInventory() {

    var barcode = $("#search-item-form input[name=barcode]").val();

    if (currentInventory.has(barcode)) {
        console.log("here");
        var inv = currentInventory.get(barcode);
        $('#quantity-informer').text("Quantity Available: " + inv);
        $('#order-item-form input[name=barcode]').val(barcode);
        $('#order-item-form input[name=quantity]').attr({
            "max": inv,
            "min": 1,
            "onkeyup": "if(this.value > " + inv + " || this.value < 1) this.value = null;"
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
                "max": response.quantity,
                "min": 1,
                "onkeyup": "if(this.value > " + response.quantity + " || this.value < 1) this.value = null;"
            });
        },
        error: function (error) {
            alert(error.responseJSON.message);
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
            $("#view-order-id").text(id);
            response.forEach(element => {
                var row = '<tr>' +
                    '<td>' + element.barcode + '</td>' +
                    '<td>' + element.quantity + '</td>' +
                    '<td>' + element.sellingPrice + '</td>' +
                    '</tr>';

                $tbody.append(row);
            });
        },
        error: function (error) {
            alert(error.responseJSON.message);
        }
    });

}


function placeOrder() {
    var url = getOrderUrl();

    // var jsonObj = arrayToJson();
    // console.log(jsonObj);
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(wholeOrder),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $('#add-order-item-modal').modal('toggle');
            wholeOrder = []
            $("#Order-table").DataTable().ajax.reload();
        },
        error: handleAjaxError
    });

    return false;
}

function init() {
    $('#Order-table').DataTable({
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
                "data": "updated"
            },
            {
                "data": null,
                "render": function (o) {
                    return '<button onclick="displayOrder(' + o.id + ')">view</button>'
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
    $('#refresh-data').click($("#Order-table").DataTable().ajax.reload());
}

$(document).ready(init);
$(document).ready(getOrderItemList);
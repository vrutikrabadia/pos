

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});

	return false;
}

function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: handleAjaxError
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
	console.log('Printing user data');
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn" onclick="deleteUser(' + e.id + ')"><img src='+deleteButton+ '></button>'
		buttonHtml += ' <button class="btn" onclick="displayEditUser(' + e.id + ')"><img src='+editButton+ '></button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#add-user').click(addUser);
	$('#refresh-data').click(getUserList);
}

$(document).ready(init);
$(document).ready(getUserList);


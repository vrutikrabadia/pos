
var editButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABa0lEQVR4nOVVO07DQBDNHfgIOqDlClyCFikEMxN7ZgNFqjQgUXIPfuegRVQYiRNg412DoHJFgjaOkbG99kYOBWKkkeXdmfdm3v46nb9gz667LEEM9Xfh4IFzvKKAHhTyRAI/SdddXxh4SLQqkf0UnD6nX2Rfj1cmvDi8pZBdhTwyeeSKnRI4sv+KYjv/X5IrAnYUcqIDah34rAieVTwlyToBMSxWnuhJiXQhgc9NroD2qsCLpD86mMky0eDWmqMZvLQGuu2s/Xl3S1AYj3pirZTcRBC0AW8i+OgOlnJb8dFaFlsCiXTSBB6jt5vfwnMRKKBrPRcj75tkaVzD2g6A7tIzQAN1SN0qWVoS8FvxsBU1b0WgkG4l8LtCvlfANxLoVC+8bb5dQIP9BwK0u4tMJoEv03zRrwz4vk2Rx7Pgka1L5Cudp/NDPNo0VhGhOLB6D7DSkxioZwTPTFeg26x7D0qOoh963kYj+G/ZF+ludktFQO8AAAAAAElFTkSuQmCC";
var deleteButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAApElEQVR4nGNgGAWjgAzwOikz4HVy5rPXKZn/kfGrlIynIDkGWoNXIIvQLEd2BM0d8BpqGbHiFFv0mkp46DmAGsFKcbS8HnVACmoQUsqnOApejzogZTQKMkcT4f/RbPh6RBdEpIKh74BX0Pbfy+R0a1L1vkzLsgG3E5Mzn1DggMx2iltDyZmtZDvgf2goG8gR+FrCOFvIyZlPQJaDzCDbAaNg2AMAj7xuMo3ALYMAAAAASUVORK5CYII=";
var downloadButoon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAw0lEQVR4nO2USwrCQAxA5xKKR/IWIplFUvCSLX6ornThvgtLk0NU0o9otQozHfAXCBMy8B5khhjzUyGASwFMQgoSAYqDCT4/GHDDllbBBGKp1PwLvmhEAhSLpV2OOO4KtKd3DLR2FjBgqkC2dGiAlUBr7VV3gKmz4AbUnNd1KzY+cZovRgK4b+EXCdCxmEUTL3ifxAn+ag3k9bi2ms/GUm/cByt9qC8ofZzgAraYdR/SNZV1Jyggmg4hYYuZsnwn8T5xBoOJIsh6gYtGAAAAAElFTkSuQmCC";
var viewButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABxUlEQVR4nO1UPUsDQRC1sFP8alUEBcU/IcG/oaBmxsvMhWBhZRHzW1IaAmlCBEEQY2OhhYWNioWFZvcSY4JaRsbdhLvckY8mWDgwsOzMvnn7ZnbHxv7tT1sdYE4jJTVSUQG9aORvcbOmosQkZ2jgViYz7gEfaaCmRm71dKCm5MqZgcA9TC0o5Os2gEI+U8ig47T2un04IS5r2ZOYL+9azvYEr8XdJY38ZJjxo4q7G/0ISY5GerCFngQjMrHiupMK6d6CX77vHMy0Y9VdXtTAeYXcME4FuUWHmONMa6ALcxO6F6wwE6CsZXHrT6ju8qJCrnZrr4BrEvMTlLMmRtkAuIZEzDbsq+I4K8EY5w0oFb1EYl501sAlWygXUMFxVgTDYCViPhAqW1bHoZshNyTmb+CvZEaOjwgl0pZsOaIApYcqAFwP51MmokBbIv582+PlrgMFGytJEQNOp3bvJJibXLWPMShRoMlANzLrneJxWpOGhpqM5Plv5W2lpjTQXWSTw2NKFzJ6XZLkRHNxYe4HrzPPauSrnmMaemhID4M8NI20qYGf+z60nl8FEHrA68JMXNYK3X0FfD7UVzGSz24k3/W/jdR+AAV3Bl24IjY3AAAAAElFTkSuQmCC";


function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}


function getReportsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports";
}


function getDaySalesUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/day-sales";
}


function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

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

function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/user";
}


//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
    try {
        
        response = JSON.parse(response.message);
        var error = "";

        if (Array.isArray(response)) {
            for (var i = 0; i < response.length; i++) {
                error += response[i].error + "\n";
            }
        }
        else{
            error = response.error;
        } 
        
        sweetAlert("Oops...", error, "error");
        return;
    }
    catch(e) {
        sweetAlert("Oops...", response.message, "error");
        return ;
    }
	
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

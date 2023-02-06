
var editButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABa0lEQVR4nOVVO07DQBDNHfgIOqDlClyCFikEMxN7ZgNFqjQgUXIPfuegRVQYiRNg412DoHJFgjaOkbG99kYOBWKkkeXdmfdm3v46nb9gz667LEEM9Xfh4IFzvKKAHhTyRAI/SdddXxh4SLQqkf0UnD6nX2Rfj1cmvDi8pZBdhTwyeeSKnRI4sv+KYjv/X5IrAnYUcqIDah34rAieVTwlyToBMSxWnuhJiXQhgc9NroD2qsCLpD86mMky0eDWmqMZvLQGuu2s/Xl3S1AYj3pirZTcRBC0AW8i+OgOlnJb8dFaFlsCiXTSBB6jt5vfwnMRKKBrPRcj75tkaVzD2g6A7tIzQAN1SN0qWVoS8FvxsBU1b0WgkG4l8LtCvlfANxLoVC+8bb5dQIP9BwK0u4tMJoEv03zRrwz4vk2Rx7Pgka1L5Cudp/NDPNo0VhGhOLB6D7DSkxioZwTPTFeg26x7D0qOoh963kYj+G/ZF+ludktFQO8AAAAAAElFTkSuQmCC";
var deleteButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAApElEQVR4nGNgGAWjgAzwOikz4HVy5rPXKZn/kfGrlIynIDkGWoNXIIvQLEd2BM0d8BpqGbHiFFv0mkp46DmAGsFKcbS8HnVACmoQUsqnOApejzogZTQKMkcT4f/RbPh6RBdEpIKh74BX0Pbfy+R0a1L1vkzLsgG3E5Mzn1DggMx2iltDyZmtZDvgf2goG8gR+FrCOFvIyZlPQJaDzCDbAaNg2AMAj7xuMo3ALYMAAAAASUVORK5CYII=";
var downloadButoon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAw0lEQVR4nO2USwrCQAxA5xKKR/IWIplFUvCSLX6ornThvgtLk0NU0o9otQozHfAXCBMy8B5khhjzUyGASwFMQgoSAYqDCT4/GHDDllbBBGKp1PwLvmhEAhSLpV2OOO4KtKd3DLR2FjBgqkC2dGiAlUBr7VV3gKmz4AbUnNd1KzY+cZovRgK4b+EXCdCxmEUTL3ifxAn+ag3k9bi2ms/GUm/cByt9qC8ofZzgAraYdR/SNZV1Jyggmg4hYYuZsnwn8T5xBoOJIsh6gYtGAAAAAElFTkSuQmCC";
var viewButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABxUlEQVR4nO1UPUsDQRC1sFP8alUEBcU/IcG/oaBmxsvMhWBhZRHzW1IaAmlCBEEQY2OhhYWNioWFZvcSY4JaRsbdhLvckY8mWDgwsOzMvnn7ZnbHxv7tT1sdYE4jJTVSUQG9aORvcbOmosQkZ2jgViYz7gEfaaCmRm71dKCm5MqZgcA9TC0o5Os2gEI+U8ig47T2un04IS5r2ZOYL+9azvYEr8XdJY38ZJjxo4q7G/0ISY5GerCFngQjMrHiupMK6d6CX77vHMy0Y9VdXtTAeYXcME4FuUWHmONMa6ALcxO6F6wwE6CsZXHrT6ju8qJCrnZrr4BrEvMTlLMmRtkAuIZEzDbsq+I4K8EY5w0oFb1EYl501sAlWygXUMFxVgTDYCViPhAqW1bHoZshNyTmb+CvZEaOjwgl0pZsOaIApYcqAFwP51MmokBbIv582+PlrgMFGytJEQNOp3bvJJibXLWPMShRoMlANzLrneJxWpOGhpqM5Plv5W2lpjTQXWSTw2NKFzJ6XZLkRHNxYe4HrzPPauSrnmMaemhID4M8NI20qYGf+z60nl8FEHrA68JMXNYK3X0FfD7UVzGSz24k3/W/jdR+AAV3Bl24IjY3AAAAAElFTkSuQmCC";
var addNewButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAA4klEQVR4nO2ZQQrDIBBFPUe67sXigA65WLLoxfwttL1AioFuAl00E5zQ/gcu1f+dr4iGQAghh6WkdC6SL0X0CdG5ZSt1zqjTNaWTQbzeWgvHukXFJhN15d3Fy7vlcYOB9rHBpzhFvX9tYD1IaAys89OAEbACYsugd//gLQA0IKzAzAgJN7H+7imEnS9sgQZWsAJy8AgdvX/wFgAaEFZgZoSEm1j/9xSyAhoQVsAErAtYJD/2vvO3fdyNOnkLh+V5vX4q1M8Fd/FRgX7ovjawVKEfuureI05lmTOPm8UTQkhowQuwhN/ia8eGmAAAAABJRU5ErkJggg=="; 
var refreshButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAADiUlEQVR4nO1ZX4hUZRT/DDHK/pii9tI/Igp6CRYXfHAlhSh9XnywWL3n7L3n3N1hWRJ6EVbxYV98FF9Di+ghfBAiENTd1tL8k9ASUWTEVjvNObPrLoFYqSPnrguzd2fkzp3Pxon5wbzMXM73+50/93znjHMddNBBB/8LFHHwpTJEgwr0iQJdFaSyAP0ryH8L8owifyvAHysy2bPuYYEEvFORzylyJetHkO8o8JcCvKsyMvJIS4hrEG8X5EuNEK8j5ntF2lbrjCLRBkW+rEAT3ohXRkZWCvJo4sUmyWtVRAT56E+FwqPV5AV5cvEZL+Tn3xtYVy9dBOi2IH8tSPsFaUspDF8uvvv+6kpv76o/ATbadxrwPkE+W0+8AJ+3M9LkvQgo7y48JUAXa3jvVlKcYfhaVlsSRa8o8Gd1ojGZJt+0gKnh4ccUaGzZgUC/aD9vymtXgrhHgX/OkmZNCRDkw8uN0hfX+4bWNGXYOTe9t7DeUueBCShD1G1psjTMdMai4jxhNohfUKCb3gXYO1qBvkulzY8aBE/6Il+sUbDeBAhEO5a9aYK4xxf56b2F9csc5FOAIn+eEnDsv/a85hVgeWkerzZSwvgNXwKSDotZGx1NNX4ARFEq97/yRX7BPk1kJV+C+O2GDxDk40sF8AHXTpDURS2XF1oJQT5blT437Crh2glliLoV+ZogzVs9tJpP20GRBhToL7tvmTNdO+F639AaG0erUnjMtROkn95JXbEvuXaCIh1M9aAPcw3rAvx7pm7pc2Z1dgejC6krDOYx8lv2Vu8vxIrclbJ9a2YPP5fHUFbyk3Yx8yVAFnZH1VeJk7kMZSIP/EOpL37WF3kN+M30wF8O+K0HIsC352fD8GlrnKnaulpxboV/AUA3i1H0oi/yf4Th47apSxWu9YGu3EYzROAbHxGYY35GkE/VsD/alOGMRXzN8jbvGTNBuFmQfq1RW6ert3ReBCTjX535VYBPaECvZiaO8euK9Gl64rtn63wpjp9oinxawGLBzgGstfXh/TbOivyB9PNWezuZF229aJs4+86GImtStYhr8qFxO6Np8tUC0m8b23UK8BGvy12g2wp8yBbHXsgnAoAmrMPWK1TzaCNbhfuQv2C2XCtgiy8B7rXQNxKRhWdp/KEaU5P1IHIoSB/ZyiT5i8n+XgL+R4FVga4kv0EUzYbh863m20EHHXTgvOAuk8y8fWKhhh8AAAAASUVORK5CYII=";
var uploadFileButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAAB4klEQVR4nO2YzUoDMRDH96p3i9bn0YMiPoK4E3YzLR56sSKeBMGHUhQLHvy4WETxKNjFTA+iXuqlkjbqumxr3WyyW8gfAj00mfkls5PJeJ6Tk3kRq60T8FNi/J0Y9nWGAPwQDO8F43tPQTBr3HkBeKjr9MgB/DrivGJ25+WuMewR4Db59areekPHuxAuEeCDWrttDGIYNgOjzVzWUwDyt3RaMGwbhSDgbwMDQbCQN4BUZ3NrjoDfqG/jLi87Iw2aWK9jEsIGgFEIWwDGIGwCGIHIHUAlhXHpOIplJ3lPPDYaM6UBEAxPJknLEeeVn3uC75boBHDt+2Jk2Bx3El0fl9V/b0sDMFgT8OBftRPDXqkApISPqwLwWDB8nQSidADW7JMD0BO5E2DuG5jeEIrkbcrwSjC8zPpYoaIA4kWZTmFGRQAkndeBINsAyXdurBzI9PYlmwDJWv55ozb/NT9rnU+2ANKcT87PAkG2AGS2SQuR5PxfIQb8ojwAwFtp6TJtfiy9nudl39gCRc/3inaAHABzJ9Cf7hAC3iLAs6Lse+49oClyJwB/9zJNqRuGi6puejHeyzQhAr6jeqNHVnqZeYn8elXaUjb7AsIVq73MnMd+Ib1MnSEYvsqw0d55JydvIn0Cnr1RJnihEfUAAAAASUVORK5CYII=";
var downloadReportButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAADGElEQVR4nO2ZS2sUQRDHx+ADvaln/QTiRTQ3EfGgeM5JBN2qzFat5JCToDELehXiSb3oR/Bx8Dt4MR6NYkBjEpKqTchGBQVxpDb7nOzsPHcmwhYM7Ka7t/+/ru6qSo/jjGxkIwu1Fdc9osj3FXlRkP8oshfnEeTfCjznVatjThHiBehtXNEBz7PcIXRn5bMQXwyEIi92TX7bm5g4GHN8sRDS3PMCvOk5zr644wv3hLYPIq2kGd8SnTuEZghgHhTkx70Q9HyoEFkC2PfcITRjgNwhdAgAuULokAByg9CUAN2lx6epqUP+dq9aHRtqdMrAA+1EKMCzuUNoeoDkpQjwXOEAqYpBoF+FA7QgGp4A/hy3HHf2AkDSOXUE4BTkAShfEOSHCvTxvwTI1IYFUL9+67gAoQC/FuQFRfphj30WoFeCDFsAx/YcwE5Y5VlB3g6LQNZHgO7ZmFwApMRXBXhZgL7JJF3xt2+67kkFep8gJ8xv3OATQwVoFGbAy11lw7Lnugd6xfNqoMhJPltzK+MDsvJqIogoAM2q8mmfSR+0E1nIyju++YI8EXs7hQEEiRfkv4p8xvrYno+acTX0XNBMZgD9xAvQEwV6ZKvVjjYRDqwTHaAeKzoFAdQm+ZIAf/WLb129bJToVGP1kSFOzaMR+q4D30wNYFkySHy3WZzPGkCQXmYKYGEz6NLLD5oNAC+kB0C6KEhfBGlpHSqXg8ZH2f8aH2A7NUBU6wcgpcr5RswH1kAAYK1B+Zz1LRSg3xYyYda2BnxakWQ3AIm12fd+yS2TLRTVrDDbtQ2AtSVwrQnRma8jvtHW5aXOmeMX+QEEhVEfhNO0MPGxw2j7eh2plgRgJ5FRPQyi2waJF+CtuIms84IDymWvWt3vxDQriQOjCvRCDBLfBLib5yumN123EvODIGpuZdwfmXb3o3dL09OHc37JV75mv2OlsJ2jpL8jSCuJ/ydIc6+jwD+t1m9BDPQEBq98YvFZW8OjSDOBBxt7Vr0uSHdib5s8zCKJlKhkhZkAf1Cg7/bYZ/ubhcot5qNF6xyZE2L/AGXcEZJdLzLSAAAAAElFTkSuQmCC";
var filterButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAACkUlEQVR4nO2Yu2/UQBDGJwSoQKSgIEI8JSoeERISNNCRjtKioLicZ7TeMUihQTRIRwctJfwDlEg0KFBBAQ0N0FCkQSS53I4vJx4iJCEYLcqRPUeB+HKxfcg/yZW938znHe/OGqCkpKSkkMwpddAQPzXEC0Ic53EZ4gVDeqI5xgdSGzDIz/JKXNYY0RPpDeT45iV5of6e2oArADkhm8mh7w0Y1MvtwXGttg0yJva8wT/fAOrl1AKG9HRbYM4PD0HG1OnakVUDPJVawH75q1Oob0DGCPFNJ/6T1AINCsecZexT09cnICNm1dWTNmY7foS6klokVmqHIX7nmIgin0dhi4l8HrWxnLf/xubSlVijGowY4qazM/4wpG/FAAO9TjwGGLDaNoYTrzmLfGpTwsbXpw3qj4md8XGrMj7Uq+RblfEhq9kRA3nKxu5JgGml9tq+KBHgg/jqzGa1G9VgRJAnO3df/bxRCfdBz9dl5Nvu/iDE84YYu9UUCq4I6a9OyfwU4ntd1/yGgiJfMqRbiX7lfux5OzeqEddq2w3y3UTn+dkge5AFJgiOCeq3CROv60Fw+F9joyDYb4hfJpJ/36TwOGRJIwx3CfLDzq6RJcLg4npjjB9eENL1xILwaE6pPZkm35EU6esGedHpW5aEWCWfEwyClXvtRWDRjoUiYEifF+QZt56TzwjqL85MzdgxUCSMUsN/a387ykapYSgiskEDUFSkNJAz5QzkTTkDefPfzoD4/u6+NRCRPpc4sMxDPxiIPW/w9xnXaeBW+qQ7UFSks7V+kWibWwb5MhQZWe/3OPKr2SofhaIjaxLXS/YcbcsJ+gFB/c0poclIhWehnxD718L+VUP9wC6deedTUlJSAlvOL87C/o23+8XoAAAAAElFTkSuQmCC";
var clearFilterButton = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAAAsTAAALEwEAmpwYAAADN0lEQVR4nO1YO2sUURS+8VEaEAVtVHwgFj6w0j+QwkKwCQpBkuw5mZyzMaVEUNiADyxEsxaKYvwJdiJRC7HQWhQLDTYmuHvvbEx8B02u3DExs5OZzdx9zKy4H0y1957zfTPnuUK00EILLTQlphxnq0R+KJFnFbJO45HIsxJprNTDW6wFSOBHaRFXy4TQmL2AFN+8Cj5AP6wF+A2IlKBq4fDPC5BAc4uXdS63SiQM3dm5+m8OAM1ZG5BIk4sGpjLZbSJhfMBT25cE8IS1AZP5S5+QTouEoZCHfP4fWBsoYrbHV8ZmShnaKxJCwRnYZ3wu+neBuq2NaMdZK5Ff+kS4LvR3iAbDhf4O48v39l8YLlUZK/b2H5DIJV9n/KWAzmgh2upNXAvRZmx7Ppb8lQrA+2syLDN00CRRWWcEvud2DbbXi7zbNdhubAZ8TBjfdXEw6TgbJdLj8u7Ib+qRFxIHdkugVwHbT4vd2c2intC53BoJfDnQ4j9L4M5qbSrgoxJ4OjA63Ko65uNA9tEJhfTFF6fzCjlv49RrUsDD/mapkL+byieSgHKcPRLpdeBrPCkAbFrp7szJgQ3lPYZNpXlrSqdIEuGJR+9dJ3so6o5JSgX0LhAy96eZ14s04JU+5KFAKMRdVuZNTqUxZy2D7KMjEnjKYkGZURk+JpoJRcjuivnmnxUdZ6doRqgm2CFqwn8hQCJdWugjeZsENmcV0HUF/FUBXxRpCfA3QYU8GkeERx551JdHn+pOPr4AzgcSe7SSiCD5haY5krgAhXTVIyREm0S+EejEd8NEeGeBbtoIbqAAj2hsETpp8vEEePF7JTI08A/BiLC50/CuHSVAZTLrwn7TESJSIR8lwEU6rIDH/WOz/44OF5E8+aAAM/NLpHMS6Gc5oeU1XEeLaGzMVxJg1sLAAPdRAh8Pu6ebUkDZnsDPC728I5S8CK02K5bYRASY8JHAZ004hZLPhVebqOrUeAFA33whNF5pO6tUKnWFEttgATzs/asGdNuUzmrIi0pnks6JUPIWHVZbjB2JwRuJLeq8Dv8SeZEWysZpiNekEhun40AhXfBEAF+rYqEZWbh7vrEsWxCx8Rsj09kHejf0eAAAAABJRU5ErkJggg==";

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

function validateFormHTML($form){
	return $form[0].reportValidity();
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
        
        Swal.fire({
            title: 'Oops...',
            text: error,
            icon: 'error',
        })
        return;
    }
    catch(e) {
        Swal.fire({
            title: 'Oops...',
            text: response.message,
            icon: 'error',
        })
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


function emphasizeNavbarCurrentLink(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = document.URL;
	path = url.substring(url.indexOf(baseUrl));
	var element = document.querySelectorAll('[href="' + path + '"][class="nav-link"]');
	element[0].style.fontWeight = "bold";	
}
emphasizeNavbarCurrentLink();
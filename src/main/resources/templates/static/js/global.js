// 获取cookies
function setCookie(cname, cvalue, exdays) {
	var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

// 获取cookies
function getCookie(cname) {
	var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return "";
}

//清除cookie  
function clearCookie(name) {  
    setCookie(name, "", -1);  
}  

function checkToken() {
    var userToken = getCookie("userToken");
    if (userToken != "") {
        console.log("Welcome again " + userToken);
    } else {
        location.href = "/zzj/login.html";
    }
}
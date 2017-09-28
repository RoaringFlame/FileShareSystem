var FSS = {};
FSS.getRootPath = function () {
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var href = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    //获取主机地址，如： http://localhost:8083
    var serverPath = href.substring(0, href.length - pathName.length);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName = "/";
    if (pathName.length > 1) {
        projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    }
    return (serverPath + projectName);
};
FSS.getContextPath = function () {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0, index + 1);
    return result;
};
FSS.sendAjax = function (type, url, data, successFn, errorFn) {
    var path = FSS.getRootPath();
    if (url.indexOf("/") == 0) {
        path += url;
    }
    else {
        path += "/" + url;
    }
    $.ajax({
        type: type,
        data: data,
        url: path,
        dataType: "json",
        success: function (d) {
            if (successFn) {
                successFn(d);
            }
        },
        error: function (e) {
            if (e.responseText.indexOf("<!DOCTYPE html>") >= 0) {
                window.location.href = FSS.getContextPath();
            }
            if (errorFn) {
                errorFn(e);
            }
            else {
                console.log(e);
            }
        }
    });
};

FSS.tipAlert = function (type, icon, title, message, timer) {
    $.niftyNoty({
        type: type,
        icon: icon,
        title: title,
        message: message,
        container: 'floating',
        timer: timer
    });
};

FSS.pageAlert = function (type, icon, title, message, timer) {
    $.niftyNoty({
        type: type,
        icon: icon,
        title: title,
        message: message,
        timer: timer //0会固定窗口
    });
};
//var dataAlert = [{ 提示参照类型
//    icon: 'fa fa-info fa-lg',
//    title: "Info",
//    type: "info"         蓝
//}, {
//    icon: 'fa fa-star fa-lg',
//    title: "Primary",
//    type: "primary"       深蓝
//}, {
//    icon: 'fa fa-thumbs-up fa-lg',
//    title: "Success",
//    type: "success"      绿
//}, {
//    icon: 'fa fa-bolt fa-lg',
//    title: "Warning",
//    type: "warning"     黄
//}, {
//    icon: 'fa fa-times fa-lg',
//    title: "Danger",
//    type: "danger"      红
//}, {
//    icon: 'fa fa-leaf fa-lg',
//    title: "Mint",
//    type: "mint"    深绿
//}, {
//    icon: 'fa fa-shopping-cart fa-lg',
//    title: "Purple",
//    type: "purple"    紫
//}, {
//    icon: 'fa fa-heart fa-lg',
//    title: "Pink",
//    type: "pink"  粉
//}, {
//    icon: 'fa fa-sun-o fa-lg',
//    title: "Dark",
//    type: "dark" 黑
//}
//];
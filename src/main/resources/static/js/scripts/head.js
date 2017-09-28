"use strict";
$(function () {
    var alertCount = $("#alertCount");
    var alertListBox = $("#alertList");
    var alertBox = $("#alertBox");
    var countNumber = $("#count");
    //
    //function writeAlertInfo(data) {
    //    var alertList = data.alertList;
    //    var count = data.count;
    //    if (count > 0) {
    //        alertCount.text(count);
    //        countNumber.text(count);
    //        alertCount.show();
    //        $(alertList).each(function (index, alert) {
    //            var newAlert = alertBox.clone();
    //            newAlert.find("a").attr("href", FSS.getContextPath()
    //                + "/file/download/" + alert.versionId).click(function () {
    //                var count = $("#alertCount").text();
    //                count = count - 1;
    //                alertCount.text(count);
    //                countNumber.text(count);
    //                var rCount = $("#receiveCount").text();
    //                rCount = rCount - 1;
    //                $("#receiveCount").text(rCount);
    //                this.remove();
    //            });
    //            newAlert.find("a>div:eq(0)>img").attr("src", FSS.getRootPath() + "/img/" + alert.pictureName);
    //            newAlert.find("a>div:eq(1)>div").text(alert.author + " 《" + alert.fileName + "》");
    //            newAlert.find("a>div:eq(1)>small").text(alert.delayTime);
    //            alertListBox.append(newAlert);
    //        });
    //    }
    //}
    //
    //function showAlert() {
    //    alertCount.hide();
    //    alertListBox.empty();
    //    countNumber.text("0");
    //    FSS.sendAjax("get", "user/alert", {}, function (data) {
    //        writeAlertInfo(data);
    //    });
    //}
    //
    //function bindSendMailEven() {
    //    $("#email").click(function () {
    //        sendEmail();
    //    });
    //}
    //
    //function sendEmail() {
    //    FSS.sendAjax("get", "file/initRevise/2", {}, function (data) {
    //        console.log(data);
    //    });
    //}
    //
    //function init() {
    //    bindSendMailEven();
    //    showAlert();
    //}
    //
    //init();
});
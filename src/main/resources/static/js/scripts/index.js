"use strict";
$(function () {
    var receiveCount = $("#receiveCount"); //未接收计数
    var receiveList = $("#receiveList");  //未接收列表
    var receiveBox = $("#receiveBox");    //未接收信息
    var receivedList = $("#receivedList"); //已接收列表
    var receivedBox = $("#receivedBox");    //已接收信息
    var uploadedList = $("#uploadedList"); //已上传列表
    var uploadedBox = $("#uploadedBox");  //已上传信息
    var reviseModel = $("#revise-content");//修改重传模态框
    var roleModel = $("#role-content");//修改重传模态框
    var select1 = $("#select1");
    var select2 = $("#select2");
    var select3 = $("#select3");
    var type = 0;
    var fileId = '';
    var versionId = '';

    //ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    function bindEvent() {
        $("#receiveButton").click(function () {
            initReceive();
        });

        $("#receivedButton").click(function () {
            initReceived();
        });

        $("#uploadedButton").click(function () {
            initUploaded();
        });

        $("#submit").click(function () {
            submitRevise(versionId, type);
        });

        $("#submitRole").click(function () {
            submitRole(fileId);
        });
    }

    function bindSelect() {
        $(function () {
            //移到右边
            $('#add').click(function () {
                //获取选中的选项，删除并追加给对方
                $('#select1 :selected').appendTo('#select2');
            });

            //移到左边
            $('#remove').click(function () {
                $('#select2 :selected').appendTo('#select1');
            });

            //全部移到右边
            $('#add_all').click(function () {
                //获取全部的选项,删除并追加给对方
                $('#select1 option').appendTo('#select2');
            });

            //全部移到左边
            $('#remove_all').click(function () {
                $('#select2 option').appendTo('#select1');
            });
        });

        $(function () {
            //移到右边
            $('#add2').click(function () {
                //获取选中的选项，删除并追加给对方
                $('#select2 :selected').appendTo('#select3');
            });

            //移到左边
            $('#remove2').click(function () {
                $('#select3 option:selected').appendTo('#select2');
            });

            //全部移到右边
            $('#add_all2').click(function () {
                //获取全部的选项,删除并追加给对方
                $('#select2 option').appendTo('#select3');
            });

            //全部移到左边
            $('#remove_all2').click(function () {
                $('#select3 option').appendTo('#select2');
            });
        });

        //双击选项
        $('#select1').dblclick(function () { //绑定双击事件
            //获取全部的选项,删除并追加给对方
            $("option:selected", this).appendTo('#select2'); //追加给对方
        });
        $('#select2').dblclick(function () { //绑定双击事件
            //获取全部的选项,删除并追加给对方
            $("option:selected", this).appendTo('#select3'); //追加给对方
        });

        $('#select3').dblclick(function () { //绑定双击事件
            //获取全部的选项,删除并追加给对方
            $("option:selected", this).appendTo('#select2'); //追加给对方
        });
    }


    function initReceive() {
        receiveList.empty();
        FSS.sendAjax("get", "home/file/needReceive", {}, function (data) {
            var fileInfoVoList = data.fileInfoVOList;
            var count = data.count;
            if (count > 0) {
                receiveCount.text(count);
                receiveCount.show();
                $(fileInfoVoList).each(function (index, info) {
                    var newInfo = receiveBox.clone();
                    newInfo.find("td:eq(0)").text(index + 1);
                    newInfo.find("td:eq(1)>a").text("《" + info.fileName + "》")
                        .attr("href", FSS.getContextPath() + "/file/download/"
                            + info.versionId).click(function () {
                        var rCount = $("#receiveCount").text();
                        rCount = rCount - 1;
                        $("#receiveCount").text(rCount);
                        indexUpdate(this);
                    });
                    newInfo.find("td:eq(2)").text(info.name);
                    newInfo.find("td:eq(3)").text(info.catalog);
                    newInfo.find("td:eq(4)").text(info.department);
                    newInfo.find("td:eq(5)>span").text(info.createTime);
                    newInfo.find("td:eq(6)").text(info.versionNumber);
                    var tip = newInfo.find("td:eq(7)>span");
                    if (info.alert) {
                        tip.text("紧急!")
                    } else {
                        tip.removeClass("label-danger");
                        tip.addClass("label-success");
                        tip.text("一般")
                    }
                    newInfo.find("td:eq(8)>div>a").attr("href", FSS.getContextPath()
                        + "/file/download/" + info.versionId).click(function () {
                        var rCount = $("#receiveCount").text();
                        rCount = rCount - 1;
                        $("#receiveCount").text(rCount);
                        indexUpdate(this);
                    });
                    newInfo.show();
                    receiveList.append(newInfo);
                });
            }
        });
    }

    function initReceived() {
        receivedList.empty();
        FSS.sendAjax("get", "home/file/received", {}, function (data) {
            if (data.length > 0) {
                $(data).each(function (index, info) {
                    var newInfo = receivedBox.clone();
                    newInfo.find("td:eq(0)").text(index + 1);
                    newInfo.find("td:eq(1)>a").text("《" + info.fileName + "》")
                        .attr("href", FSS.getContextPath()
                            + "/file/download/" + info.versionId);
                    newInfo.find("td:eq(2)").text(info.name);
                    newInfo.find("td:eq(3)").text(info.catalog);
                    newInfo.find("td:eq(4)").text(info.department);
                    newInfo.find("td:eq(5)>span").text(info.downloadTime);
                    newInfo.find("td:eq(6)").text(info.versionNumber);
                    var tip = newInfo.find("td:eq(7)>span");
                    if (info.alert) {
                        tip.text("紧急!")
                    } else {
                        tip.removeClass("label-danger");
                        tip.addClass("label-success");
                        tip.text("一般")
                    }
                    if (info.canRevise) {
                        newInfo.find("td:eq(8)>div>button").click(function () {
                            type = 1;
                            versionId = info.versionId;
                            $("#revise-modal").modal('show');
                            reviseModel.find("div:eq(0)>h4").text("《" + info.fileName + "》  " + "v" + info.versionNumber);
                        });
                    } else {
                        newInfo.find("td:eq(8)>div>button").hide();
                    }
                    newInfo.find("td:eq(8)>div>a").attr("href", FSS.getContextPath()
                        + "/file/download/" + info.versionId);
                    newInfo.show();
                    receivedList.append(newInfo);
                });
            }
        });
    }

    function initUploaded() {
        uploadedList.empty();
        FSS.sendAjax("get", "home/file/uploaded", {}, function (data) {
            if (data.length > 0) {
                $(data).each(function (index, info) {
                    var newInfo = uploadedBox.clone();
                    newInfo.find("td:eq(0)").text(index + 1);
                    newInfo.find("td:eq(1)>a").text("《" + info.fileName + "》")
                        .attr("href", FSS.getContextPath()
                            + "/file/download/" + info.versionId);
                    newInfo.find("td:eq(2)").text(info.catalog);
                    newInfo.find("td:eq(3)>span").text(info.createTime);
                    newInfo.find("td:eq(4)").text(info.versionNumber);
                    newInfo.find("td:eq(5)>div>button:eq(0)").click(function () {
                        fileId = info.fileId;
                        deleteFile(fileId);
                        fileId = '';
                        indexUpdate(this)
                    });
                    newInfo.find("td:eq(5)>div>button:eq(1)").click(function () {
                        fileId = info.fileId;
                        $("#role-modal").modal('show');
                        roleModel.find("div:eq(0)>h4").text("《" + info.fileName + "》  " + "v" + info.versionNumber);
                        initRole(fileId);
                    });
                    newInfo.find("td:eq(5)>div>button:eq(2)").click(function () {
                        type = 2;
                        versionId = info.versionId;
                        $("#revise-modal").modal('show');
                        reviseModel.find("div:eq(0)>h4").text("《" + info.fileName + "》  " + "v" + info.versionNumber);
                    });
                    newInfo.find("td:eq(5)>div>a").attr("href", FSS.getContextPath()
                        + "/file/download/" + info.versionId);
                    newInfo.show();
                    uploadedList.append(newInfo);
                });
            }
        });
    }

    function deleteFile(fileId) {
        FSS.sendAjax('post', 'file/delete/' + fileId, {}, function (data) {
            if (data.status == 'success') {
                success(data.message)
            }
            else if (data.status == 'failure') {
                failure(data.message);
            }
        });
    }

    function initRole(fileId) {
        select1.empty();
        select2.empty();
        select3.empty();
        FSS.sendAjax("get", "file/initRevise/" + fileId, {}, function (data) {
            var userSelector = data.userSelector;
            var canLoadSelector = data.canLoadSelector;
            var canReviseSelector = data.canReviseSelector;
            $(userSelector).each(function (index, user) {
                $("#select1").append($("<option></option>")
                    .val(user.key)
                    .text(user.value)
                )
            });
            $(canLoadSelector).each(function (index, user) {
                $("#select2").append($("<option></option>")
                    .val(user.key)
                    .text(user.value)
                )
            });
            $(canReviseSelector).each(function (index, user) {
                $("#select3").append($("<option></option>")
                    .val(user.key)
                    .text(user.value)
                )
            });
        });
    }

    function submitRole(fileId) {
        var canDownloadIds = '';
        $('#select2 option').each(function () {
            canDownloadIds += $(this).val() + ','
        });
        var canReviseIds = '';
        $('#select3 option').each(function () {
            canReviseIds += $(this).val() + ','
        });
        var params = {
            canLoadUserIds: canDownloadIds,
            canReviseUserIds: canReviseIds
        };
        FSS.sendAjax('post', 'file/reviseRole/' + fileId, params, function (data) {
            if (data.status == 'success') {
                success(data.message)
            }
            else if (data.status == 'failure') {
                failure(data.message);
            }
        });
        $("#role-modal").modal('hide');
    }

    function submitRevise(versionId, type) {
        var flag = false;
        var file = $('#file');
        var canCover = $('#canCover').is(':checked');

        if (file.val() == '' || versionId == '') {
            $("#warning").show();
        } else {
            $("#warning").hide();
            flag = true;
        }

        if (flag) {
            var formData = new FormData();
            var name = $("input").val();
            formData.append("file", $('#file')[0].files[0]);
            formData.append("canCover", canCover);
            $.ajax({
                url: FSS.getContextPath() + '/file/reviseFile/' + versionId,
                type: "post",
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                data: formData,
                success: function (data) {
                    //根据服务器返回的值判断
                    if (data.status == 'success') {
                        success('文件修改成功!')
                    }
                    else if (data.status == 'failure') {
                        failure(data.message);
                    }
                },
                error: function (data) {
                    if (data.responseText.indexOf("<!DOCTYPE html>") >= 0) {
                        window.location.href = FSS.getContextPath();
                    }
                }
            });
            $("#revise-modal").modal('hide');
            if (type == 1) {
                initReceived();
            }
            else if (type == 2) {
                initUploaded();
            }
            $("#file").val("");
        }
    }

    function success(message) {
        FSS.pageAlert('success', 'fa fa-check fa-lg',
            '成功', message, 3000)
    }

    function failure(message) {
        FSS.pageAlert('danger', 'fa fa-times fa-lg',
            '错误', message, 3000)
    }

    function indexUpdate(obj) {
        $(obj).parents("tr").siblings().each(function (i) {
            $(this).find("td").eq(0).html(i + 1)
        });
        $(obj).parents("tr").remove();
    }

    function init() {
        bindEvent();
        bindSelect();
        initReceive();
    }

    init();
});
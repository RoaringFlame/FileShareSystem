"use strict";
//下拉框交换JQuery
$(function () {

    //ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    function initSelector() {
        FSS.sendAjax('get', 'file/uploadInit', {}, function (data) {
            var userSelector = data.userSelector;
            var catalogSelector = data.catalogSelector;
            $(userSelector).each(function (index, user) {
                $("#select1").append($("<option></option>")
                    .val(user.key)
                    .text(user.value)
                )
            });
            $(catalogSelector).each(function (index, catalog) {
                $("#catalogId").append($("<option></option>")
                    .val(catalog.key)
                    .text(catalog.value)
                )
            });
        });
    }

    function bindEven() {
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

        $('#submitButton').click(function () {
            submitFileInfo();
        });
    }

    function submitFileInfo() {
        var flag = false;
        var file = $('#file');
        var catalogId = $('#catalogId').val();
        var canDownloadIds = '';
        $('#select2 option').each(function () {
            canDownloadIds += $(this).val() + ','
        });
        var canReviseIds = '';
        $('#select3 option').each(function () {
            canReviseIds += $(this).val() + ','
        });
        var canCover = $('#canCover').is(':checked');
        var emailTo = $('#emailTo').is(':checked');

        if (file.val() == '') {
            error("请选择分享的文件！")
        } else if (catalogId == '') {
            error("请选择文件目录！")
        } else if (canDownloadIds == ''&&canReviseIds == '') {
            error("请选择可接收人员！")
        } else {
            flag = true;
        }

        var params = {
            file: file[0],
            catalogId: catalogId,
            canDownloadIds: canDownloadIds,
            canReviseIds: canReviseIds,
            canCover: canCover,
            emailTo: emailTo
        };

        console.log(params);

        if (flag) {
            var formData = new FormData();
            var name = $("input").val();
            formData.append("file",$('#file')[0].files[0]);
            formData.append("catalogId",catalogId);
            formData.append("canLoadUserIds",canDownloadIds);
            formData.append("canReviseUserIds",canReviseIds);
            formData.append("canCover",canCover);
            formData.append("isMailTo",emailTo);
            $.ajax({
                url : FSS.getContextPath()+'/file/upload',
                type : "post",
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                data : formData,
                success : function(data) {
                    //根据服务器返回的值判断
                    if (data.status == 'success') {
                        success('文件上传成功!');
                        $("#file").val("");
                    }
                    else if (data.status == 'failure') {
                        failure(data.message);
                    }
                },
                error : function(data) {
                    if (data.responseText.indexOf("<!DOCTYPE html>") >= 0) {
                        window.location.href = FSS.getContextPath();
                    }
                }
            });
        }
    }

    function warning(message) {
        FSS.tipAlert('warning', 'fa fa-exclamation fa-lg',
            '提示', message, 2500)
    }

    function error(message) {
        FSS.tipAlert('danger', 'fa fa-exclamation fa-lg',
            '错误', message, 2500)
    }

    function success(message) {
        FSS.pageAlert('success', 'fa fa-check fa-lg',
            '成功', message, 3000)
    }

    function failure(message) {
        FSS.pageAlert('danger', 'fa fa-times fa-lg',
            '错误', message, 3000)
    }

    function init() {
        initSelector();
        bindEven();
    }

    init();

});
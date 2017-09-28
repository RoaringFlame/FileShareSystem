'use strict';

$(function () {
    var validateCode = $("#validateCode");

    function GetRandomNum(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();
        return (Min + Math.round(Rand * Range));
    }
    //更换背景图片为用户之前选择,没有则使用随机
    function changeBackground() {
        var index = getCookieValue("background");
        if (index == "-1") {
            index = GetRandomNum(1, 7);
        }

        var $imgHolder 	= $('#demo-bg-list');//图片列表
        var $bgBtn 		= $imgHolder.find('.demo-chg-bg');//缩略图按钮
        var $target 	= $('#bg-overlay'); //背景图片
        var $el = $('#demo-bg-list img').eq(index - 1);

        $imgHolder.addClass('disabled');
        var url = $el.attr('src').replace('/thumbs','');

        $('<img/>').attr('src' , url).load(function(){
            $target.css('background-image', 'url("' + url + '")');
            $imgHolder.removeClass('disabled');
            $bgBtn.removeClass('active');
            $el.addClass('active');

            $(this).remove();
        })
    }

    //初始化背景图片选择
    function initBackground(){
        var $imgHolder 	= $('#demo-bg-list');//图片列表
        var $bgBtn 		= $imgHolder.find('.demo-chg-bg');//缩略图按钮
        var $target 	= $('#bg-overlay'); //背景图片

        $bgBtn.on('click', function(e){
            e.preventDefault();
            e.stopPropagation();

            var $el = $(this); //当前缩略图
            if ($el.hasClass('active') || $imgHolder.hasClass('disabled'))return;
            if ($el.hasClass('bg-trans')) {
                $target.css('background-image','none');
                $imgHolder.removeClass('disabled');
                $bgBtn.removeClass('active');
                $el.addClass('active');
                return;
            }

            $imgHolder.addClass('disabled');
            var url = $el.attr('src').replace('/thumbs','');

            $('<img/>').attr('src' , url).load(function(){
                $target.css('background-image', 'url("' + url + '")');
                $imgHolder.removeClass('disabled');
                $bgBtn.removeClass('active');
                $el.addClass('active');

                $(this).remove();
            })

        });
    }

    //查看登录状态
    function checkStatue() {
        var url = document.URL;
        if (url.indexOf("#") != -1) {
            window.location.href = FSS.getContextPath();
        }
        FSS.sendAjax("get", "validate/statue", {}, function (data) {//返回成功输出
            //验证码正确
            if (data.message == "bad") {
                $("#loginError").show();
            }
        });
    }

    function bindEven() {

        $("#submit").attr("type", "button");

        $("#submit").click(function () {
            var code = validateCode.val();
            if (code.length == 0) {
                $("#validateEmpty").show();
            } else if (code.length != 4) {
                $("#validateEmpty").text("验证码错误！");
                $("#validateEmpty").show();
            }
        });

        $('#username').focus(function () {
            $("#loginError").hide();
        });

        $('#password').focus(function () {
            $("#loginError").hide();
        });

        //验证码绑定
        validateCode.bind('input propertychange', function () {
            checkValidate();
        });

        //背景图片点击事件绑定，变更图片信息到cookie
        $('#demo-bg-list img').bind('click', function () {
            addCookie("background", $(this).index(), 7 * 24);
        });
    }

    function checkValidate() {
        $("#validateEmpty").hide();
        var code = validateCode.val();
        if (code.length == 0) {
            $("#validateResult").hide();
            $("#validate").show();
            $("#validateChecked").hide();
        } else if (code.length == 4) {
            FSS.sendAjax("get", "validate/check", {code: code}, function (data) {//返回成功输出
                //验证码正确
                if (data.status == "success") {
                    $("#validate").hide();
                    $("#validateChecked").show();
                    $("#validateResult").hide();
                    $("#submit").attr("type", "submit");

                } else {
                    $("#validateResult").show();
                    $("#validate").show();
                    $("#validateChecked").hide();
                }
            }, function (data) {
                if (data.responseText.indexOf("<!DOCTYPE html>") >= 0) {
                    window.location.href = FSS.getContextPath();
                }
            });
        } else if (code.length > 4) {
            $("#validateResult").show();
            $("#validate").show();
            $("#validateChecked").hide();
        }
    }

    function addCookie(name, value, expiresHours) {
        var cookieString = name + "=" + value;
        //判断是否设置过期时间,0代表关闭浏览器时失效
        if (expiresHours > 0) {
            var date = new Date();
            date.setTime(date.getTime() + expiresHours * 1000);
            cookieString = cookieString + ";expires=" + date.toUTCString();
        }
        document.cookie = cookieString;
    }

    //修改cookie的值
    function editCookie(name, value, expiresHours) {
        var cookieString = name + "=" + value;
        if (expiresHours > 0) {
            var date = new Date();
            date.setTime(date.getTime() + expiresHours * 1000); //单位是毫秒
            cookieString = cookieString + ";expires=" + date.toGMTString();
        }
        document.cookie = cookieString;
    }

    //根据名字获取cookie的值
    function getCookieValue(name) {
        var strCookie = document.cookie;
        var arrCookie = strCookie.split("; ");
        for (var i = 0; i < arrCookie.length; i++) {
            var arr = arrCookie[i].split("=");
            if (arr[0] == name) {
                return arr[1];
                break;
            }
        }
        return '-1';
    }

    function init() {
        checkStatue();
        changeBackground();
        initBackground();
        bindEven();
    }

    init();

});
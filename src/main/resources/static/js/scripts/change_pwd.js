'use strict';

$(function () {
    var ok1 = false; //密码校验
    var ok2 = false; //确认密码校验

    //ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    //输入新密码后光标离开
    $('#new-password').focus(function () {
    }).blur(function () {
        pwdCheck();
    });

    //再次输入密码后光标离开
    $('#re-new-password').focus(function () {
    }).blur(function () {
        confirmCheck();
    });

    //点击提交按钮
    $('#up-pwd').click(function () {
        pwdCheck();
        confirmCheck();
        var oldPwd = $("#old-password").val();
        var newPwd = $("#new-password").val();
        if (ok1 && ok2) {//当以上判断全部成立，即执行后面的代码
            $.post(FSS.getContextPath() + '/user/changePwd', {oldPwd: oldPwd, newPwd: newPwd},
                function (data) {
                    //根据服务器返回的值判断
                    if (data.status == 'success') {
                        success('密码修改成功!')
                    }
                    else if (data.status == 'failure') {
                        failure(data.message);
                    }
                }, 'json')
        }
    });

    //密码校验
    function pwdCheck() {
        ok1 = false;
        if ($('#old-password').val() == '') {
            error('请输入原密码！');
        }
        else if ($('#new-password').val() == '') {
            error('请输入新密码！');
        } else {
            var password = $('#new-password').val();
            var num = 0; //密码验证通过数
            var number = 0;  //数字
            var letter = 0; //小写字符
            var bigLetter = 0; //大写字符
            var chars = 0;
            if (password.search(/[0-9]/) != -1) {  //判断密码中是否有数字
                num += 1;
                number = 1;
            }
            if (password.search(/[A-Z]/) != -1) {  //判断密码中是否有大写字母
                num += 1;
                bigLetter = 1;
            }
            if (password.search(/[a-z]/) != -1) {  //判断密码中是否有小写字母
                num += 1;
                letter = 1;
            }
            if (password.search(/[^A-Za-z0-9]/) != -1) {  //判断密码中是否全部是字符
                num += 1;
                chars = 1;
            }
            if (num >= 2 && password.length >= 6 && password.length <= 16 && password != '') {  //密码的长度在6~16位之间并且要有字符和数字
                ok1 = true;
            } else if (password.length < 6 || password.length > 16) {
                warning('新密码必须为6-16位之间');
            } else if (num == 1) {
                if (number == 1) {
                    warning('密码不能全为数字!');
                }
                if (letter == 1) {
                    warning('密码不能全为字母!');
                }
                if (bigLetter == 1) {
                    warning('密码不能全为字母!');
                }
                if (chars == 1) {
                    warning('密码不能全为字符!');
                }
            }
        }
    }

    //密码确认校验
    function confirmCheck() {
        ok2 = false;
        var confirmPwd = $('#re-new-password').val();
        if (confirmPwd == $('#new-password').val()) {//判断密码是否正确
            ok2 = true;
        } else {
            error('输入的密码不一致!');
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
            '成功', message, 0)
    }

    function failure(message) {
        FSS.pageAlert('danger', 'fa fa-times fa-lg',
            '错误', message, 0)
    }
});
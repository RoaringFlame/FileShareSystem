'use strict';
$(function () {
    var dataCount;
    var pageCount;
    var pageNum = 1;
    var hasNextPage;
    var nextPage;
    var hasPrePage;
    var prePage;
    var genderList;
    var departmentList;
    var roleList;
    var partroleList;

    var trline_0 = $("#trline_0");
    var trline_1 = $("#trline_1");
    var trline_2 = $("#trline_2");
    
    var editbtn = $("#editbtn");
    var deletebtn = $("#deletebtn");
    var resetbtn = $("#resetbtn");
    
    //搜索按钮绑定功能
    $("#selectbtn").click(function() {
		pageNum = 1;
    	initInfoTab();
	});
    
    //添加按钮绑定功能
	$("#addbtn").click(function() {
		addformclean();
        
		//添加模态框的提交按钮绑定功能
		$("#addBtn").click(function() {
			var newName = $("#addName").val();
			var newUserName = $("#addUserName").val();
			var newEmail = $("#addEmail").val();
	        var newGender = $("#addGender").val();
			var newDepartment = $("#addDepartment").val();
			var newRole = $("#addRole").val();

			check();
			if(check1 && check2){
				var addParams = {
						name : newName,
						genderId : newGender,
						username : newUserName,
						email : newEmail,
						departmentId : newDepartment,
						roleId : newRole
				};
				
				FSS.sendAjax("post", "user/addOrUpdate", addParams, function (data){
					if (data.status == 'success') {
		                success('添加用户信息成功!');
		                $("#add-modal").modal('hide');
		                initSelector();
		                addformclean();
		                initInfoTab();
		            }
		            else if (data.status == 'failure') {
		            	alert(data.message);
		            }
				});
			}
		})
		
		$("#addclose").click(function() {
			addformclean();
		})
	})
	
	var check1 = false;
	var check2 = false;
	function check() {
		var newName = $("#addName").val();
		var newUserName = $("#addUserName").val();
		if(newName == ""){
			check1 = false;
			alert('请输入用户姓名！');
		}
		if(newUserName == ""){
			check1 = false;
			alert('请输入用户账号！');
		}
		if (newName.length < 2 || newName.length > 15) { //姓名的长度在6~15位之间并且要有字符和数字
			check1 = false;
			alert('姓名必须为2-15位之间！');
		}
		else check1 = true;
		if (newUserName.length < 6 || newUserName.length > 15) { //帐号的长度在6~15位之间并且要有字符和数字
			check2 = false;
			alert('帐号必须为6-15位之间！');
		}
		else check2 = true;
	}
	
	//清空重载添加模态框表单
	function addformclean() {
        $("#addUserName").val("");
		$("#addName").val("");
		$("#addEmail").val("");
		$("#addGender").empty();
		$("#addDepartment").empty();
		$("#addRole").empty();
		
		var option1 = $("<option></option>").val("").text("请选择");
		$("#addGender").append(option1);
        $(genderList).each(function (index, gender) {
            option1 = $("<option></option>").val(gender.key).text(gender.value);
            $("#addGender").append(option1);
        });
        
        var option2 = $("<option></option>").val("").text("请选择");
        $("#addDepartment").append(option2);
        $(departmentList).each(function (index, department) {
            option2 = $("<option></option>").val(department.key).text(department.value);
            $("#addDepartment").append(option2);
        });
        
		var option3 = $("<option></option>").val("").text("请选择");
		$("#addRole").append(option3);
        $(partroleList).each(function (index, role) {
            option3 = $("<option></option>").val(role.key).text(role.value);
            $("#addRole").append(option3);
        });
	}
	
	//清空重载添加模态框表单
	function editformclean() {
        $("#editUserName").val("");
		$("#editName").val("");
		$("#editEmail").val("");
		$("#editGender").empty();
		$("#editDepartment").empty();
		$("#editRole").empty();
		
		var option1 = $("<option></option>").val("").text("请选择");
		$("#editGender").append(option1);
        $(genderList).each(function (index, gender) {
            option1 = $("<option></option>").val(gender.key).text(gender.value);
            $("#editGender").append(option1);
        });
        
        var option2 = $("<option></option>").val("").text("请选择");
        $("#editDepartment").append(option2);
        $(departmentList).each(function (index, department) {
            option2 = $("<option></option>").val(department.key).text(department.value);
            $("#editDepartment").append(option2);
        });
        
		var option3 = $("<option></option>").val("").text("请选择");
		$("#editRole").append(option3);
        $(partroleList).each(function (index, role) {
            option3 = $("<option></option>").val(role.key).text(role.value);
            $("#editRole").append(option3);
        });
	}
	
    //初始化表格
    function initInfoTab() {
    	
    	var departmentKey = $("#selectDepartment").val();
        var roleKey = $("#selectRole").val();
        var name = $("#selectName").val();
        if(name == null){
        	name = "";
    	}
        var userName = $("#selectUserName").val();
        if(userName == null){
        	userName = "";
    	}
        $("#userInfoTab tbody").empty();
        
        var params = {
        		departmentKey : departmentKey,
        		roleKey : roleKey,
        		name : name,
        		userName : userName,
        		pageNum : pageNum
        };
        
        FSS.sendAjax("get", "user/showInfoList", params, function (data) {
        	var dataList = data.dataList;
        	pageCount = data.pageCount;
			pageNum = data.pageNum;
        	
        	$(dataList).each(function (index, userdata) {
	            var newInfoTab = trline_2.clone();
	            var editBtn = editbtn.clone();
	            var deleteBtn = deletebtn.clone();
	            var resetBtn = resetbtn.clone();
	            var userId = userdata.id;
	            var operationList = userdata.operation;
	            
	            newInfoTab.find("td:eq(0)").text(index + 1);
	            newInfoTab.find("td:eq(1)").text(userdata.name);
	            newInfoTab.find("td:eq(2)").text(userdata.username);
	            newInfoTab.find("td:eq(3)").text(userdata.email);
	            newInfoTab.find("td:eq(4)>div").text(userdata.departmentname);
	            newInfoTab.find("td:eq(5)>div").text(userdata.rolename);
	            
	            $(operationList).each(function (i, operation) {
	            	
	            	//修改按钮绑定功能
	            	if (operation == "edit"){
	            		newInfoTab.find("td:eq(6)>div").append(editBtn.click(function() {
	            			editformclean();
	            			
	            			$("#editName").val(userdata.name);
	            			$("#editUserName").val(userdata.username);
	            			$("#editUserName").attr("readonly","readonly");
	            		    $("#editGender").val(userdata.gender);
	            			$("#editEmail").val(userdata.email);
	            			$("#editDepartment").val(userdata.department);
	            			$("#editRole").val(userdata.role);
	            			
	            			//修改模态框的提交按钮绑定功能
	            			$("#editBtn").click(function() {
	            				var editName = $("#editName").val();
	            				var editGender = $("#editGender").val();
	            				var editUserName = $("#editUserName").val();
	            				var editEmail = $("#editEmail").val();
	            				var editDepartment = $("#editDepartment").val();
	            				var editRole = $("#editRole").val();
	            				var editParams = {
	            						id : userId,
	            						name : editName,
	            						genderId : editGender,
	            						username : editUserName,
	            						email : editEmail,
	            						departmentId : editDepartment,
	            						roleId : editRole
	            				};
	            				FSS.sendAjax("post", "user/addOrUpdate", editParams, function (data){
	            					if (data.status == 'success') {
	                                    success('修改用户信息成功!');
	                                    $("#edit-modal").modal('hide');
	                                    editformclean();
	                	                initInfoTab();
	                                }
	                                else if (data.status == 'failure') {
	                                	alert(data.message);
	                                }
	            				});
							})
							$("#editclose").click(function() {
								editformclean();
							})
						}));
	            	}
	            	
	            	//删除按钮绑定功能
	            	if (operation == "delete"){
	            		newInfoTab.find("td:eq(6)>div").append(deleteBtn.click(function() {
	            			FSS.sendAjax("delete", "user/delete/" + userId, {}, function (data){
            					if (data.status == 'success') {
                                    success('删除用户信息成功!')
                                    initInfoTab();
                                }
                                else if (data.status == 'failure') {
                                    failure(data.message);
                                }
            				});
						}));
	            	}
	            	
	            	//重置密码按钮绑定功能
	            	if (operation == "reset"){
	            		newInfoTab.find("td:eq(6)>div").append(resetBtn.click(function() {
	            			FSS.sendAjax("post", "user/resetPwd/" + userId, {}, function (data){
            					if (data.status == 'success') {
                                    success('重置用户密码成功!');
                                    initInfoTab();
                                }
                                else if (data.status == 'failure') {
                                    failure(data.message);
                                }
            				});
						}));
	            	}
	            });
	            newInfoTab.show();
	            $("#userInfoTab tbody").append(newInfoTab);
	        });
        	$("#pageAmount").text(data.pageCount + "页");
        	$("#dataAmount").text(data.dataCount + "条");
        	$("#currentpage").text(data.pageNum);
        });
    }
    
  //页码初始化
	function initPage() {
		var firstpage = $("#pageList").find("ul>li:eq(0)>button");
		var prepage = $("#pageList").find("ul>li:eq(1)>button");
		var currentpage = $("#pageList").find("ul>li:eq(2)>button");
		var nextpage = $("#pageList").find("ul>li:eq(3)>button");
		var lastpage = $("#pageList").find("ul>li:eq(4)>button");
		
		currentpage.text(pageNum);
	    	
		firstpage.click(function() {
			pageNum = 1;
			initInfoTab();
			currentpage.text(pageNum);
		});
	    
    	prepage.click(function() {
    		if(pageNum == 1){
    			
    		}
    		else{
    			pageNum --;
    			initInfoTab();
        		currentpage.text(pageNum);
    		}
        });
	    	
    	nextpage.click(function() {
    		if((pageNum == pageCount)||(pageCount<=1)){
    			
    		}
    		else {
    			pageNum ++;
    			initInfoTab();
    			currentpage.text(pageNum);
			}
        });
	    	
    	lastpage.click(function() {
    		if(pageNum > 0){
    			pageNum = pageCount;
    			initInfoTab();
    			currentpage.text(pageNum);
    		}
        });
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
    
  //初始化搜索条件下拉框
    function initSelector() {
    	$("#selectDepartment").empty();
    	$("#selectRole").empty();
    	$("#selectGender").hide();
        resetSelector($("#selectGender"), $("#selectDepartment"), $("#selectRole"));
        $("#selectName").empty();
    	$("#selectUserName").empty();
    }
    
  //重载下拉框
    function resetSelector(genderSelector, departmentSelector, roleSelector) {
    	genderSelector.empty();
    	departmentSelector.empty();
        roleSelector.empty();
        
        //重新载入下拉框全部选项
        FSS.sendAjax("get", "user/selectAllOption", {}, function (data) {
        	genderList = data.genderList;
            departmentList = data.departmentList;
            roleList = data.roleList;
            
            var option1 = $("<option></option>").val("").text("请选择");
            genderSelector.append(option1);
            $(genderList).each(function (index, gender) {
                option1 = $("<option></option>").val(gender.key).text(gender.value);
                genderSelector.append(option1);
            });
            
            var option2 = $("<option></option>").val("").text("请选择");
            departmentSelector.append(option2);
            $(departmentList).each(function (index, department) {
                option2 = $("<option></option>").val(department.key).text(department.value);
                departmentSelector.append(option2);
            });

            var option3 = $("<option></option>").val("").text("请选择");
            roleSelector.append(option3);
            $(roleList).each(function (index, role) {
                option3 = $("<option></option>").val(role.key).text(role.value);
                roleSelector.append(option3);
            });
        });
        
        FSS.sendAjax("get", "user/selectOption", {}, function (data) {
        	partroleList = data.roleList;
        });
	}
    
    //ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    
    function init() {
    	initSelector();
        initInfoTab();
        initPage();
    }
    
    init();
    
});
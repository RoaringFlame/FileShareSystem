'use strict';

(function ($) {
	var nodeId;
	var parentnodeId;
	var sNodes;
	var zTreeObj;
	var setting;
	var parentFileName;
	var currentFileName;
	var newFileName;
    $("#warning-1").hide();
	$("#warning-2").hide();
	$("#warning-3").hide();
	
	function isnull() {
		var parentFileName = $("#addform").find("form>div:eq(0)>div>input").val();
	    var currentFileName = $("#addform").find("form>div:eq(1)>div>input").val();
	    var newFileName = $("#addform").find("form>div:eq(2)>div>input").val();
		if((parentFileName == null) || (parentFileName == ""))
			$("#addform").find("form>label:eq(0)").show();
		else
			$("#addform").find("form>label:eq(0)").hide();
		if((currentFileName == null) || (currentFileName == ""))
			$("#addform").find("form>label:eq(1)").show();
		else
			$("#addform").find("form>label:eq(1)").hide();
		if((newFileName == null) || (newFileName == ""))
			$("#addform").find("form>label:eq(2)").show();
		else
			$("#addform").find("form>label:eq(2)").hide();
	}
	
	$("#add").click(function() {
		$("#addform").find("form>div:eq(0)>div>input").val(parentFileName);
		$("#addform").find("form>div:eq(1)>div>input").val(currentFileName);
		$("#addform").find("form>div:eq(2)>div>input").val(newFileName);
	})
	
	$("#edit").click(function() {
		$("#editform").find("form>div:eq(0)>div>input").val(parentFileName);
		$("#editform").find("form>div:eq(1)>div>input").val(currentFileName);
	})
	
	$("#delete").click(function() {
		$("#deleteform").find("form>div:eq(0)>div>input").val(parentFileName);
		$("#deleteform").find("form>div:eq(1)>div>input").val(currentFileName);
	})
	
	$("#cancel1").click(function() {
		$("#addform").find("form>div:eq(0)>div>input").val("");
		$("#addform").find("form>div:eq(1)>div>input").val("");
		$("#addform").find("form>div:eq(2)>div>input").val("");
	})
	
	$("#cancel2").click(function() {
		$("#editform").find("form>div:eq(0)>div>input").val("");
		$("#editform").find("form>div:eq(1)>div>input").val("");
	})
	
	$("#cancel3").click(function() {
		$("#deleteform").find("form>div:eq(0)>div>input").val("");
		$("#deleteform").find("form>div:eq(1)>div>input").val("");
	})
	//添加目录
	$("#addbtn").click(function() {
		parentFileName = $("#addform").find("form>div:eq(0)>div>input").val();
	    currentFileName = $("#addform").find("form>div:eq(1)>div>input").val();
	    newFileName = $("#addform").find("form>div:eq(2)>div>input").val();
		if((parentFileName == null) || (parentFileName == ""))
			$("#addform").find("form>label:eq(0)").show();
		else
			$("#addform").find("form>label:eq(0)").hide();
		if((currentFileName == null) || (currentFileName == ""))
			$("#addform").find("form>label:eq(1)").show();
		else
			$("#addform").find("form>label:eq(1)").hide();
		if((newFileName == null) || (newFileName == ""))
			$("#addform").find("form>label:eq(2)").show();
		else
			$("#addform").find("form>label:eq(2)").hide();
		
		var newFileName = $("#newFileName").val();
		var addParams = {
				parentId : nodeId,
				name : newFileName
		};
		$.post(FSS.getContextPath() + '/catalog/addOrUpdate', addParams, function (data) {
			if (data.status == 'success') {
				alert('添加目录成功!');
                loadData();
            }
            else if (data.status == 'failure') {
            	alert(data.message);
            }
		}, 'json')
	});
	
	//编辑目录
    $("#editbtn").click(function() {
    	var parentFileName = $("#editform").find("form>div:eq(0)>div>input").val();
	    var currentFileName = $("#editform").find("form>div:eq(1)>div>input").val();
		if((parentFileName == null) || (parentFileName == ""))
			$("#editform").find("form>label:eq(0)").show();
		else
			$("#editform").find("form>label:eq(0)").hide();
		if((currentFileName == null) || (currentFileName == ""))
			$("#editform").find("form>label:eq(1)").show();
		else
			$("#editform").find("form>label:eq(1)").hide();

    	if (sNodes && sNodes.length > 0) {
    		var parent = sNodes[0].getParentNode(); //得到选中节点的父节点
    		if (parent) {
    			sNodes[0].pid = parent.id; //如果选中节点父节点存在，将当前结点的pid属性值设置为父节点的id
    		}
    		sNodes[0].name = currentFileName;
    	}
    	var editParams = {
				id : nodeId,
				parentId : parentnodeId,
				name : currentFileName
		};
    	$.post(FSS.getContextPath() + '/catalog/addOrUpdate', editParams, function (data) {
			if (data.status == 'success') {
				alert('修改目录成功!');
                loadData();
            }
            else if (data.status == 'failure') {
            	alert(data.message);
            }
		}, 'json')
	});
    
    //删除目录
    $("#deletebtn").click(function() {
    	var parentFileName = $("#deleteform").find("form>div:eq(0)>div>input").val();
	    var currentFileName = $("#deleteform").find("form>div:eq(1)>div>input").val();
		if((parentFileName == null) || (parentFileName == ""))
			$("#deleteform").find("form>label:eq(0)").show();
		else
			$("#deleteform").find("form>label:eq(0)").hide();
		if((currentFileName == null) || (currentFileName == ""))
			$("#deleteform").find("form>label:eq(1)").show();
		else
			$("#deleteform").find("form>label:eq(1)").hide();
		
    	var currentFileName = $("#currentFileName").val();
    	if (sNodes && sNodes.length > 0) {
    		if (sNodes[0].children && sNodes[0].children.length > 0) {
				if (confirm("删除此文件夹,将级联删除此文件夹下所有文件夹!") == true) {
					$.ajax({
						url: FSS.getContextPath() + "/catalog/delete/" + nodeId,
						type: 'DELETE',
						success: function (data) {
							if (data.status == 'success') {
								alert('删除目录成功!');
								loadData();
							}
							else if (data.status == 'failure') {
								alert(data.message);
							}
						}
					});
				}
			}
    		else{
    			if (confirm("是否确认删除？") == true){
    				$.ajax({
    				    url: FSS.getContextPath() + "/catalog/delete/" + sNodes[0].id,
    				    type: 'DELETE',
    				    success: function(data) {
    				    	if (data.status == 'success') {
    				    		alert('删除目录成功!');
        		                loadData();
        		            }
        		            else if (data.status == 'failure') {
        		            	alert(data.message);
        		            }
    				    }
    				});
    			}
    		}
    	}
	});
	
    function initZtree() {
    	setting = {
    		view: {
    			selectedMulti: false,
    			showLine: true
    			},
    		data: {
    			simpleData: {
    				enable: true
    			}
    			},
    		callback: {
    			onClick: onClick,
//    			onRightClick: OnRightClick
//    			beforeRename: beforeRename, //定义节点重新编辑成功前回调函数，一般用于节点编辑时判断输入的节点名称是否合法
//    			onDblClick: onDblClick, //定义节点双击事件回调函数
    			},
    		};
    	
    	$.ajax({
		type: "Get",
		url: FSS.getContextPath() +"/catalog/showCatalogList",
		success: function (dataList) {
			var str = "[";
			$(dataList).each(function(i,data){
				str = str + "{ id:\""+data.id+"\", pId:\""+data.parentId+ "\", name:\"" +data.name+ "\", isParent:true, open:true},";
				});
			str=str.substring(0,str.length-1);
			str = str + "]";
			var treeNodes = eval('(' + str + ')');
			zTreeObj = $.fn.zTree.init($("#fileTree"), setting, treeNodes);
			}
		});
    	
    	var zNodes = [
    		{ id:1, pId:0, name:"根目录1", open:true},
			{ id:11, pId:1, name:"人力资源1", isParent:true, open:true},
			{ id:111, pId:11, name:"员工管理1", isParent:true, open:true},
			{ id:112, pId:11, name:"社招管理1", isParent:true, open:true},
			{ id:113, pId:11, name:"校招管理1", isParent:true, open:true},
			{ id:1111, pId:111, name:"CRM组1", isParent:true, open:true},
			{ id:1112, pId:111, name:"计费组1", isParent:true, open:true},
			{ id:12, pId:1, name:"行政事务1", isParent:true, open:true},
			{ id:121, pId:12, name:"办公用具1", isParent:true, open:true},
			{ id:122, pId:12, name:"薪资发放1", isParent:true, open:true},
		];
    	
    	function onClick(event, treeId, treeNode) {
    		sNodes = zTreeObj.getSelectedNodes();
    		for(var i=0;i<sNodes.length;i++){
    			if(sNodes[i].getParentNode()!=null){	//选中非根目录
    				parentFileName = sNodes[i].getParentNode().name;
    				currentFileName = sNodes[i].name;
    			}
    			else{//选中根目录
    				parentFileName = sNodes[i].name;
    				currentFileName = sNodes[i].name;
    			}
    			$("#addform").find("form>div:eq(0)>div>input").val(parentFileName);
    			$("#addform").find("form>div:eq(1)>div>input").val(currentFileName);
    			$("#addform").find("form>div:eq(2)>div>input").val(newFileName);
    			$("#editform").find("form>div:eq(0)>div>input").val(parentFileName);
    			$("#editform").find("form>div:eq(1)>div>input").val(currentFileName);
    			$("#deleteform").find("form>div:eq(0)>div>input").val(parentFileName);
    			$("#deleteform").find("form>div:eq(1)>div>input").val(currentFileName);
            }
    		parentnodeId = sNodes[0].getParentNode().id
    		nodeId = sNodes[0].id;
    		$("#warning-1").hide();
    		$("#warning-2").hide();
    		$("#warning-3").hide();
		}
    }
    
    //重载ztree数据
    function loadData(){
    	FSS.sendAjax("get", "catalog/showCatalogList", {}, function (dataList){
    		var str = "[";
			$(dataList).each(function(i,data){
				str = str + "{ id:\""+data.id+"\", pId:\""+data.parentId+ "\", name:\"" +data.name+ "\", isParent:true, open:true},";
				});
			str=str.substring(0,str.length-1);
			str = str + "]";
			var treeNodes = eval('(' + str + ')');
			zTreeObj = $.fn.zTree.init($("#fileTree"), setting, treeNodes);
		});
    }
    
    //ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    
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
    
    function initweb() {
    	initZtree();
    }
    
    initweb();
    
})(jQuery);
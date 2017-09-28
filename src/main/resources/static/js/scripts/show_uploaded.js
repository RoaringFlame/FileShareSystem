"use strict";
$(function(){
	var dataCount;
	var flag = 0;
    var pagenum = new Array(1,1,1,1);
    var pagecount = new Array(flag);
    var fileId;
    var versionId;
    var versionReviseId;
    var modal;

	var trline_0 = $("#trline_0");
	var trline_1 = $("#trline_1");
	var trline_2 = $("#trline_2");
	var trline_3 = $("#trline_3");
	var MrecordBtn = $("#modify-record");
	var DrecordBtn = $("#download-record");
	var ModifyBtn = $("#modify");
	var DrecordBtn2 = $("#download-record2");
	var DownloadBtn = $("#download");
	var ReviseBtn = $("#revise");
	var DeleteBtn = $("#deletebtn");
	var reviseModel = $("#revise-content");//修改重传模态框
	var roleModel = $("#role-content");//修改重传模态框
	var select1 = $("#select1");
    var select2 = $("#select2");
    var select3 = $("#select3");

	//ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
	
	//搜索按钮绑定功能
	$("#select_userinfo").click(function() {
		initUploadTab();
	});
	
	$("#submitRole").click(function() {
		submitRole(fileId);
	});

	$("#submit").click(function () {
		submitRevise(versionReviseId);
	});
	
	//初始化修改记录模态框
	function initModifyModalTab(fileData) {
        flag = 1;
		$("#ModifyRecordTab tbody").empty();
		FSS.sendAjax("get","history/fileRevise/"+ fileData.fileId, {pageNum : pagenum[flag]}, function(data) {
			$("#ModifyRecordTab tbody").empty();
			var dataList = data.dataList;
			pagecount[flag] = data.pageCount;
            pagenum[flag] = data.pageNum;

			$(dataList).each(function(index,filedata){
				var newFileTab = trline_1.clone();
				var Drecordbtn2 = DrecordBtn2.clone();
				var Downloabtn = DownloadBtn.clone();
				
				newFileTab.find("td:eq(0)").text(index + 1);
	            newFileTab.find("td:eq(1)").text(filedata.versionNumber);
	            newFileTab.find("td:eq(2)").text(filedata.name);
	            newFileTab.find("td:eq(3)>div").text(filedata.role);
	            newFileTab.find("td:eq(4)>span").text(filedata.operateTime);
	            newFileTab.find("td:eq(5)").text(filedata.count);
	            
	            //修改记录模态框中的下载记录按钮绑定功能
	            newFileTab.find("td:eq(6)>div").append(Drecordbtn2
	            		.click(function(){
	            	$("#downofmodify-modal").find("div>div>div:eq(0)>h4").text("《" + fileData.fileName + "》  " + "v" + filedata.versionNumber);
	            	versionId = filedata.versionId;
	            	initDownOfModifyTab(filedata.versionId,fileData);
	            	$("#Modify-modal").modal('hide');
	            }));
	            
	            //下载按钮绑定功能
	            newFileTab.find("td:eq(6)>div").append(Downloabtn);
	            newFileTab.find("td:eq(6)>div>a")
	            .attr("href", FSS.getContextPath() + "/file/download/" + filedata.versionId);
	            
	            newFileTab.show();
	            $("#ModifyRecordTab tbody").append(newFileTab);
			});
            initPage($("#Modify-modal"),fileData);
		})
	}

    $("#close-modify-modal").click(function(){
        pagenum[flag] = 1;
        flag = 0;
    });

	//初始化下载记录模态框
	function initDownloadModalTab(fileData) {
		flag = 2;
		$("#DownloadRecordTab tbody").empty();
		FSS.sendAjax("get","history/fileDownload/"+ fileData.fileId, {pageNum : pagenum[flag]}, function(data) {
			var dataList = data.dataList;
			pagecount[flag] = data.pageCount;
			pagenum[flag] = data.pageNum;
			
			$(dataList).each(function(index,filedata){
				var newFileTab = trline_2.clone();
				
				newFileTab.find("td:eq(0)").text(index + 1);
				newFileTab.find("td:eq(1)").text(filedata.versionNumber);
	            newFileTab.find("td:eq(2)").text(filedata.name);
	            newFileTab.find("td:eq(3)>div").text(filedata.role);
	            newFileTab.find("td:eq(4)>span").text(filedata.operateTime);
	            
	            newFileTab.show();
	            $("#Download-modal tbody").append(newFileTab);
			});
            initPage($("#Download-modal"),fileData);
		});
	}

    $("#close-download-modal").click(function(){
        pagenum[flag] = 1;
        flag = 0;
    });

	//初始化修改记录模态框下的下载记录模态框
	function initDownOfModifyTab(versionId,fileData){
		flag = 3;
		$("#downofmodify-modal tbody").empty();
		FSS.sendAjax("get","history/versionDownload/"+ versionId, {pageNum : pagenum[flag]}, function(data) {
			var dataList = data.dataList;
			pagecount[flag] = data.pageCount;
			pagenum[flag] = data.pageNum;
			
			$(dataList).each(function (index, filedata) {
	            var newFileTab = trline_3.clone();
	            newFileTab.find("td:eq(0)").text(index + 1);
	            newFileTab.find("td:eq(1)").text(filedata.name);
	            newFileTab.find("td:eq(2)>div").text(filedata.role);
	            newFileTab.find("td:eq(3)>span").text(filedata.operateTime);
	           
	            newFileTab.show();
	            $("#downofmodify-modal tbody").append(newFileTab);
			});
            initPage($("#downofmodify-modal"),fileData);
		});
	}

    $("#close-downofmodify-modal").click(function(){
        flag = 1;
        $("#ModifyRecordTab tbody").empty();
        $("#Modify-modal").modal('show');
        initModifyModalTab(fileData);
    });

	//初始化已上传文件的表格
	function initUploadTab(){
		var catalogNameKey = $("#selectCatalog").val();
		if(catalogNameKey == null){
			catalogNameKey = "";
		}
		var fileNameKey = $("#selectFileName").val();
		if(fileNameKey == null){
			fileNameKey = "";
		}
		$("#showReceiveTab tbody").empty();
        flag = 0;
		var params = {
            catalogNameKey : catalogNameKey,
            fileNameKey : fileNameKey,
            pageNum : pagenum[flag]
		};
		FSS.sendAjax("get","file/pageUploaded", params, function(data) {
			var dataList = data.dataList;
			pagecount[flag] = data.pageCount;
			pagenum[flag] = data.pageNum;

			$(dataList).each(function(index, filedata){
				var newFileTab = trline_0.clone();
				var Mrecordbtn = MrecordBtn.clone();
				var Drecordbtn = DrecordBtn.clone();
				var Modifybtn = ModifyBtn.clone();
				var Downloabtn = DownloadBtn.clone();
				var Revisebtn = ReviseBtn.clone();
				var Deletebtn = DeleteBtn.clone();
				
				newFileTab.find("td:eq(0)").text(index + 1);
	            newFileTab.find("td:eq(1)>a").text("《" + filedata.fileName + "》")
	            .attr("href", FSS.getContextPath() + "/file/download/" + filedata.versionId);
	            newFileTab.find("td:eq(2)>div").text(filedata.catalog);
	            newFileTab.find("td:eq(3)>span").text(filedata.createTime);
	            newFileTab.find("td:eq(4)").text(filedata.versionNumber);

                fileId = filedata.fileId;
                versionReviseId = filedata.versionId;
				var fileData = filedata;

	          //修改记录按钮绑定功能
	            newFileTab.find("td:eq(5)>div").append(Mrecordbtn
	            		.click(function(){
	            	$("#Modify-modal").find("div>div>div:eq(0)>h4").text("《" + filedata.fileName + "》  " + "v" + filedata.versionNumber);
	            	initModifyModalTab(fileData);
	            }));
	            
	          //下载记录按钮绑定功能
	            newFileTab.find("td:eq(5)>div").append(Drecordbtn
	            		.click(function(){
	            	$("#Download-modal").find("div>div>div:eq(0)>h4").text("《" + filedata.fileName + "》  " + "v" + filedata.versionNumber);
	            	initDownloadModalTab(fileData);
	            }));
	            
	            //修改权限按钮绑定功能
	            newFileTab.find("td:eq(5)>div").append(Modifybtn
	            		.click(function () {
                    roleModel.find("div:eq(0)>h4").text("《" + filedata.fileName + "》  " + "v" + filedata.versionNumber);
                    bindSelect();
                    initRole(filedata.fileId);
                }));
	            
	            //下载按钮绑定功能
	            newFileTab.find("td:eq(5)>div").append(Downloabtn);
	            newFileTab.find("td:eq(5)>div>a")
	            .attr("href", FSS.getContextPath() + "/file/download/" + filedata.versionId);

				//修改重传功能按钮
				newFileTab.find("td:eq(5)>div").append(Revisebtn.click(function () {
					$("#revise-modal").modal('show');
					reviseModel.find("div:eq(0)>h4").text("《" + filedata.fileName + "》  " + "v" + filedata.versionNumber);
				}));

	            //删除按钮绑定功能
	            newFileTab.find("td:eq(5)>div").append(Deletebtn.click(function () {
                    deleteFile(filedata.fileId);
                    // fileId = '';
                }));
	            
	            newFileTab.show();
	            $("#showReceiveTab tbody").append(newFileTab);
			});
            initPage($("#pageblock"));
		})
	}
	
	//页码初始化
	function initPage(modal,fileData) {
        var pageList = $("#pageList").clone();
        if(flag == 0){
            modal.empty();
            modal.append(pageList);
        }
        else{
            modal.find("div>div>div:eq(1)").empty();
            modal.find("div>div>div:eq(1)").append(pageList);
        }
		var firstpage = pageList.find("ul>li:eq(0)>button");
		var prepage = pageList.find("ul>li:eq(1)>button");
		var currentpage = pageList.find("ul>li:eq(2)>button");
		var nextpage = pageList.find("ul>li:eq(3)>button");
		var lastpage = pageList.find("ul>li:eq(4)>button");

		currentpage.text(pagenum[flag]);

		firstpage.click(function() {
            pagenum[flag] = 1;
            switch(flag){
                case 0 : initUploadTab();break;
                case 1 : initModifyModalTab(fileData);break;
                case 2 : initDownloadModalTab(fileData);break;
                case 3 : initDownOfModifyTab(versionId,fileData);break;
            }
			currentpage.text(pagenum[flag]);
		});

    	prepage.click(function() {
    		if(pagenum[flag] == 1){

    		}
    		else{
                pagenum[flag] --;
                switch(flag){
                    case 0 : initUploadTab();break;
                    case 1 : initModifyModalTab(fileData);break;
                    case 2 : initDownloadModalTab(fileData);break;
                    case 3 : initDownOfModifyTab(versionId,fileData);break;
                }
        		currentpage.text(pagenum[flag]);
    		}
        });

    	nextpage.click(function() {
    		if((pagenum[flag] == pagecount[flag])||(pagecount[flag] <= 1)){

    		}
    		else {
                pagenum[flag] ++;
                switch(flag){
                    case 0 : initUploadTab();break;
					case 1 : initModifyModalTab(fileData);break;
					case 2 : initDownloadModalTab(fileData);break;
					case 3 : initDownOfModifyTab(versionId,fileData);break;
				}
    			currentpage.text(pagenum[flag]);
			}
        });

    	lastpage.click(function() {
    		if(pagenum[flag] > 0){
                pagenum[flag] = pagecount[flag];
                switch(flag){
                    case 0 : initUploadTab();break;
                    case 1 : initModifyModalTab(fileData);break;
                    case 2 : initDownloadModalTab(fileData);break;
                    case 3 : initDownOfModifyTab(versionId,fileData);break;
                }
    			currentpage.text(pagenum[flag]);
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

	function submitRevise(versionReviseId) {
		var flag = false;
		var file = $('#file');
		var canCover = $('#canCover').is(':checked');

		if (file.val() == '' || versionReviseId == '') {
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
				url: FSS.getContextPath() + '/file/reviseFile/' + versionReviseId,
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
						initReceiveTab();
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
			$("#file").val("");
		}
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
	 
	  function deleteFile(fileId) {
	        FSS.sendAjax('post', 'file/delete/' + fileId, {}, function (data) {
	            if (data.status == 'success') {
	                success(data.message);
	                initUploadTab();
	            }
	            else if (data.status == 'failure') {
	                failure(data.message);
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
	  
	 function init(){
		//清空搜索条件
		$("#selectFileName").val("");
		initUploadTab();
	}
	
	init();
});
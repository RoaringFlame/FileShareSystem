"use strict";
$(function(){
	var dataCount;
    var flag = 0;
    var pagecount = new Array(flag);
    var pagenum = new Array(1,1,1,1);
    var fileId;
    var versionId;
	
	var trline_0 = $("#trline_0");
	var trline_1 = $("#trline_1");
	var trline_2 = $("#trline_2");
	var trline_3 = $("#trline_3");
	var MrecordBtn = $("#modify-record");
	var DrecordBtn = $("#download-record");
	var DrecordBtn2 = $("#download-record2");
	var DownloadBtn = $("#download");
	var modal;

	//ajax请求post,delete方法的security令牌认证
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

	//搜索按钮绑定功能
	$("#select_userinfo").click(function() {
		initReceiveTab();
	})
	
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
	            	initDownOfModifyTab(filedata.versionId);
	            	$("#Modify-modal").modal('hide');
	            }));
	            
	            //修改记录模态框中的下载按钮绑定功能
	            newFileTab.find("td:eq(6)>div").append(Downloabtn);
	            newFileTab.find("td:eq(6)>div>a")
	            .attr("href", FSS.getContextPath() + "/file/download/" + filedata.versionId);
	            
	            newFileTab.show();
	            $("#ModifyRecordTab tbody").append(newFileTab);
			});
            initPage($("#Modify-modal"),fileData);
		});
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

	//初始化待接收文件的表格
	function initReceiveTab(){
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
			pageSize : "5",
			pageNum : pagenum[flag]
		};
		FSS.sendAjax("get","file/pageNeedReceive", params, function(data) {
			var dataList = data.dataList;
            pagecount[flag] = data.pageCount;
            pagenum[flag] = data.pageNum;

			$(dataList).each(function(index, filedata){
				var newFileTab = trline_0.clone();
				var Mrecordbtn = MrecordBtn.clone();
				var Drecordbtn = DrecordBtn.clone();
				var Downloabtn = DownloadBtn.clone();
				
				newFileTab.find("td:eq(0)").text(index + 1);
	            newFileTab.find("td:eq(1)>a").text("《" + filedata.fileName + "》")
	            .attr("href", FSS.getContextPath() + "/file/download/" + filedata.versionId)
	            .click(function () {
	            	initReceiveTab();
                        });
	            newFileTab.find("td:eq(2)").text(filedata.name);
	            newFileTab.find("td:eq(3)>div").text(filedata.department);
	            newFileTab.find("td:eq(4)>div").text(filedata.catalog);
	            newFileTab.find("td:eq(5)>span").text(filedata.createTime);
	            newFileTab.find("td:eq(6)").text(filedata.versionNumber);
	            
	            fileId = filedata.fileId;
                var fileData = filedata;
	            
				//修改记录按钮绑定功能
	            newFileTab.find("td:eq(7)>div").append(Mrecordbtn
	            		.click(function(){
	            	$("#Modify-modal").find("div>div>div:eq(0)>h4").text("《" + filedata.fileName + "》  " + "v" + filedata.versionNumber);
	            	initModifyModalTab(fileData);
	            }));
	            
	            //下载记录按钮绑定功能
	            newFileTab.find("td:eq(7)>div").append(Drecordbtn
	            		.click(function(){
	            	$("#Download-modal").find("div>div>div:eq(0)>h4").text("《" + filedata.fileName + "》  " + "v" + filedata.versionNumber);
	            	initDownloadModalTab(fileData);
	            }));
	            
	            //下载按钮绑定功能
	            newFileTab.find("td:eq(7)>div").append(Downloabtn);
	            newFileTab.find("td:eq(7)>div>a")
	            .attr("href", FSS.getContextPath() + "/file/download/" + filedata.versionId)
	            .click(function(){
	            	initReceiveTab();
	            	});
	            
	            newFileTab.show();
	            $("#showReceiveTab tbody").append(newFileTab);
			});
            initPage($("#pageblock"));
		});
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
                case 0 : initReceiveTab();break;
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
                    case 0 : initReceiveTab();break;
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
                    case 0 : initReceiveTab();break;
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
                    case 0 : initReceiveTab();break;
                    case 1 : initModifyModalTab(fileData);break;
                    case 2 : initDownloadModalTab(fileData);break;
                    case 3 : initDownOfModifyTab(versionId,fileData);break;
                }
                currentpage.text(pagenum[flag]);
            }
        });
    }
	 
	 function init(){
		//清空搜索条件
		$("#selectDepartment").val("");
		$("#selectCatalog").val("");
		$("#selectFileName").val("");
		initReceiveTab();
	}
	
	init();
});
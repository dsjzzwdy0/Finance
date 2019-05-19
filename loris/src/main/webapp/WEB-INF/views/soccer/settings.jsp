<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String sid = request.getParameter("sid");
%>

<style>
.toolbar
{
	width: 100%;
	height: 50px;
	margin-top: 2px;
	margin-right: 2px;
	border-top: 2px solid #c70010;
	border-bottom: 1px solid #e5e5e5;
	border-left: 1px solid #e5e5e5;
	border-right: 1px solid #e5e5e5;
	backgroud: 
}

.nameText
{
	float: left;
	height: 28px;
	margin: 10px 0 10px 10px;
	display: block;
}

.gridTable .btn-xs
{
	width: 20px;
}
.gridTable .green
{
	color:green;
}
.gridTable .red
{
	color: red;
}
.gridTable .blue
{
	color: blue;
}

</style>

<div class="container_wrapper">
	<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
		<div style="float: left;">
			<span class="location">当前位置：</span><a href="../" class="aco">东彩首页</a> &gt; <b>设置列表</b>
		</div>
	</div>
	<div id="toolbar" class="toolbar">
		<div class="nameText">
			<!-- 
			<label for="name">配置名称：<input type="text" id="name" style="margin-left:3px;"/></label>			
			<a id="btnSave" class="btn btn-info"><span class='glyphicon glyphicon-ok'></span>保存</a>
			<a id="btnShow" class="btn btn-info"><span class='glyphicon glyphicon-th-list'></span>显示</a> -->
			<a href="usercorp" class="btn btn-info"><span class='glyphicon glyphicon-plus'></span>新建</a>
		</div>
	</div>
	<table class="table table-striped gridTable" id="gridTable" data-pagination="false"
		data-show-refresh="false" data-show-toggle="false"
		data-showColumns="false">
	</table>
</div>

<script>
function initTable() 
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		method: "get",                        		//使用get请求到服务器获取数据 
		url:"../soccerdata/getCorpSettings",
		striped: false, 							//表格显示条纹 
		pagination: true, 							//启动分页 
		search: false, 								//是否启用查询 
		showColumns: false, 						//显示下拉框勾选要显示的列 
		showRefresh: false, 						//显示刷新按钮 
		sidePagination: "server", 					//表示服务端请求 
		//toolbar: "#toolbar",
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined",		
		columns: [
		{
			field: "id",
			sortable: true,
			title: "序号",
			formatter: function(value, row, index)
     		{
     			return index + 1;
     		}
		},
		{
        	field: 'name',
         	title: '配置名称',
         	sortable: true,
         	formatter: function(value, row, index)
     		{
     			return '<a href="usercorp?sid=' + row.id + '" class="red" title="查看">' + value + '</a>';
     		}
     	},
     	{
        	field: 'type',
        	sortable: true,
        	title: '公司名称'
    	}, 
    	{
        	field: 'user',
       		title: '设置用户'
     	},
     	{
     		field: 'source',
     		sortable: true,
     		title: '数据来源'
     	},
     	{
     		field: 'createtime',
     		title: '创建时间',
     	},
     	{
     		field: 'modifytime',
     		title: '修改时间',
     	},
     	{
        	field: 'id',
        	sortable: true,
       		title: '操作',
       		formatter: actionFormatter
     	}],
		onLoadSuccess: function(){ //加载成功时执行 
		    layer.msg("加载成功");
			//$("#cusTable").TabStyle();
		}, 
		onLoadError: function(){ //加载失败时执行 
			layer.msg("加载数据失败", {time : 1500, icon : 2}); 
		} 
	});	
}

function deleteSid(sid, name)
{
	layer.confirm('确认要删除吗？', 
		{btn : [ '确定', '取消' ]//按钮
    	}, 
    	function(index){
        	$.ajax({
        		url: '../soccerdata/deleteCorpSetting',
        		type: "GET",
        		contentType : "application/json;charset=utf-8",
        		dataType : "json",
        		data : {
        			"sid": sid
        		},
                success : function (msg)
                {
                	if(msg.status == '200')
                	{
                		layer.msg('成功删除配置:' + name);
                		refreshTable();
                	}
                	else
                	{
                		layer.msg("在删除配置信息时出现错误，请重试。");
                		refreshTable();
                	}
                }
        	});
    	}
    ); 
}

function actionFormatter(value, row, index) 
{
    var id = value;
    var result = "";
    result += '<a href="usercorp?sid=' + id + '" class="btn btn-xs green" title="查看"><span class="glyphicon glyphicon-search"></span></a>';
    result += '<a href="usercorp?sid=' + id + '" class="btn btn-xs blue" title="编辑"><span class="glyphicon glyphicon-pencil"></span></a>';
    result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteSid('" + id +"', '" + row.name + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}

function refreshTable()
{
	$('#gridTable').bootstrapTable('refresh');
}

$(document).ready(function() {
	//调用函数，初始化表格  
	initTable();
});

</script>
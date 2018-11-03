<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="../content/scripts/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="../content/css/soccer/soccer_public.css" />
<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<script type="text/javascript" src="../content/scripts/jquery/jquery.min.js"></script>
<script type="text/javascript" src="../content/scripts/layer/layer.js"></script>
<script type="text/javascript" src="../content/scripts/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="../content/scripts/bootstrap/bootstrap-table.js"></script>

<style type="text/css">
.container_wrapper
{
	width: 80%;
	min-width: 800px;
	margin: auto;
	min-height: 470px;
}
</style>

<div class="container_wrapper">
	<div class="main-wrapper">
		<table id="gridTable" class="gridTable table-hover">
		</table>
	</div>
</div>

<script type="text/javascript">
function updateData(request) 
{
	$.ajax({
		type: "GET",
		url: "userList",
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
		},
		jsonp:'callback',
        success : function (msg)
        {			
			request.success({
                row : msg.data
            });
            $('#gridTable').bootstrapTable('load', msg.data);
        },
		error:function(){
			layer.msg("错误");
		}
    });
}

function initTable() 
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		ajax: updateData,
		striped: false, //表格显示条纹 
		pagination: false, //启动分页 
		search: false, //是否启用查询 
		showColumns: false, //显示下拉框勾选要显示的列 
		showRefresh: false, //显示刷新按钮 
		sidePagination: "server", //表示服务端请求 
		//toolbar: "#toolbar",
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined",
		columns: [
		{
			field: 'operate',
			title: '序号',
			formatter: function (value, row, index) {  
                return index + 1;  
            }  
     	},
     	{
        	field: 'username',
        	title: '用户名'
    	},
     	{
        	field: 'email',
       		title: 'Email',
     	},
     	{
        	field: 'end',
       		title: '结束时间',
     	},
     	{
        	field: 'createtime',
       		title: '创建时间',
     	},
     	{
        	field: 'preparetime',
       		title: '准备时间',
     	},
     	{
        	field: 'total',
       		title: '总数据量',
     	},
     	{
        	field: 'left',
       		title: '未下载完成',
     	},
     	{
        	field: 'finishtime',
       		title: '完成时间',
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

$(document).ready(function(){
	initTable();
});
</script>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<style type="text/css">
.container-wrapper
{
	margin:auto;
	width: 80%;
	min-width: 1000px;
	min-height: 700px;
	font:13px/20px 宋体, arial,sans-serif;
}

.minibars
{
	width: 100%;
	display: block;
	text-align: left;
	border-bottom: none;
	background-color: #fafafa;
	margin-top: 20px;
}

.minibars a.aco {
    color: #014991;
}

.main-wrapper
{
	width: 100%;
	margin-top: 15px;
}
</style>

<div class="container-wrapper">
	<div class="minibars">
		<div class="nav-bar">
			<span class="location">当前位置：</span><a href="admin" class="aco">数据下载首页</a> &gt; <b>当前正在下载</b>
		</div>
	</div>
	<div class="main-wrapper">
		<table id="gridTable" class="gridTable table-hover">
		</table>
	</div>
</div>

<script type="text/javascript">

function initPage()
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		striped: true,                        	//表格显示条纹 
		pagination: false,                   	//启动分页 
		sortable: true,           				//是否启用排序 
	    sortName:"createtime", 
	    sortOrder: "desc",          			//排序方式 
		search: false,                         	//是否启用查询 
		showColumns: false,                    	//显示下拉框勾选要显示的列 
		showRefresh: false,                    	//显示刷新按钮 
		sidePagination: "server",             	//表示服务端请求 
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined", 
		silent: true,  //刷新事件必须设置 
		formatLoadingMessage: function () {  
			return "请稍等，正在加载中...";  
		},
		columns: [
		{
			field: 'operate',
			title: '序号',
			formatter: function (value, row, index) {  
                return index + 1;  
            }  
     	},
     	{
        	field: 'category',
        	title: '类别'
    	}, 
    	{
        	field: 'name',
       		title: '下载名称',
       		formatter: function (value, row, index) {  
                return '<a href="downpage?id=' + row.id + '" target="_blank">' + value + '</a>';  
            } 
     	},
     	{
        	field: 'start',
       		title: '开始时间',
       		sortable: true,
     	},
     	{
        	field: 'end',
       		title: '结束时间',
       		sortable: true
     	},
     	{
        	field: 'createtime',
       		title: '创建时间',
       		sortable: true
     	},
     	{
        	field: 'encoding',
       		title: '数据编码',
       		sortable: false
     	},
     	{
        	field: 'interval',
       		title: '间隔时间',
       		sortable: true
     	},
     	{
        	field: 'preparetime',
       		title: '准备时间',
       		sortable: true
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
        	field: 'status',
       		title: '当前状态',
       		formatter: function (value, row, index) {
       			var s = '';
       			switch(value)
       			{
       			case 0:
       				s = '初始化';
       				break;
       			case 1:
       				s = '初始化完毕';
       				break;
       			case 2:
       				s = '正在下载';
       				break;
       			case 3:
       				s = '暂停';
       				break;
       			case 4:
       				s = '下载完成';
       				break;
       			default:
       				s = '未知';
       			}
                return s;  
            } 
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

//页面初始化
$(document).ready(function(){
	initPage();
	
	$.ajax({
        type: "GET",
        url: "curlist", 
        dataType: "json",
        success: function (msg) {
            $("#gridTable").bootstrapTable('load', msg);
        },
        error: function () {
            alert("错误");
        }
    });
});
</script>

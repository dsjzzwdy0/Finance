<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<div id="content" class="container_wrapper">
	<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
		<div style="float: left;">
			<a href="/" class="aco">东彩首页</a> &gt; <b>数据比较分析</a></b>
		</div>
	</div>
	
	<div id="main" class="main_wrapper">
		<div id="toolbar">
			<div class=btn-group style="margin-right:5px;">
				<input type="text" id="text" class="form-control" style="height:26px;" placeholder="查询数据"/>					
			</div>
			<div class=btn-group style="margin-right:20px;">
				<button id="btnSearch" class="btn btn-default"><i class="glyphicon glyphicon-zoom-in">查询</i> </button>					
			</div>
			<div class="btn-group btn-group-sm"> 
				<button id="btnUpdate" class="btn btn-default"><i class="glyphicon glyphicon-plus">更新</i></button> 
			 	<button id="btnRefresh" class="btn btn-default"><i class="glyphicon glyphicon-heart"></i></button> 
			 	<button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button> 
			</div>				
		</div>
		
		<table id="gridTable" class="gridTable table-hover" 
			data-pagination="true"
			data-show-refresh="true">
		</table>
	</div>
</div>

<script type="text/javascript">
function downloadData(request)
{
	$.ajax({
        type : "GET",
        url : "../soccerdata/getCorps",
		contentType: "application/json;charset=utf-8",
		dataType:"json",
		data: '',
		jsonp:'callback',
        success : function (msg)
        {			
			request.success({
                row : msg.data
            });
            $('#gridTable').bootstrapTable('load', msg);
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
		ajax: downloadData,
		striped: false, //表格显示条纹 
		pagination: false, //启动分页 
		search: false, //是否启用查询 
		showColumns: false, //显示下拉框勾选要显示的列 
		showRefresh: false, //显示刷新按钮 
		sidePagination: "server", //表示服务端请求 
		toolbar: "#toolbar",
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined", 
		queryParams: function queryParams(params) { //设置查询参数 
			var param = { 
		    	pageNumber: params.pageNumber, 
		     	pageSize: params.pageSize,
		     	orderNum : $("#orderNum").val() 
		    }; 
		    return param;     
		},
		
		columns: [
		{
			field: 'operate',
			title: '序号',
			formatter: function(value, row, index) {
		    	return index + 1;
			},
		},
		{
			field: "id",
			title: "编号"
		},
		{
        	field: 'gid',
         	title: '公司编号',
         	formatter: formatterMid
     	},
     	{
        	field: 'name',
        	title: '公司名称'
    	}, 
    	{
        	field: 'ismain',
       		title: '是否主流公司'
     	},
     	{
     		field: 'source',
     		title: '数据来源'
     	},
     	{
        	field: 'userid',
       		title: '设置',
       		formatter: formaterCheck
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

function getCorporates()
{
	var rows = $("#gridTable tbody tr");
	var size = rows.length;
	var corps = [];
	for(var i = 0; i < size; i ++)
	{
		var corp = getCorporate(rows[i]);
		corps.push(corp);
	}
	var queryString = JSON.stringify(corps);
	alert(queryString);	
}

function getCorporate(row)
{
	var cols = $(row).find("td");

	var gid = $(cols[1]).text();
	var name = $(cols[2]).text();
	var ismain = $(cols[3]).text();
	var userid = $(cols[4]).find("input").prop('checked');
	var corp = {
		gid: gid,
		name: name,
		ismain: ismain,
		userid: userid
	}
	return corp;
}

function formatterMid(value, row, index)
{
	return '<a href="oddslist.html?mid=' + value + '">' + value + '</a>';
}

//赋予的参数
function operateFormatter(value, row, index) {
    return computeDelta(row.avgprobwin, row.avgwinodds);
}


function formaterCheck(value, row, index)
{
	if(value != null || value != undefined)
	{
		return '<input type="checkbox" id="check' + index + '" checked/>';
	}
	else
	{
		return '<input type="checkbox" id="check' + index + '" />';
	}
}

function btnOk()
{
	getCorporates();
}

$(document).ready(function() {
	//调用函数，初始化表格  
	initTable();
	//当点击查询按钮的时候执行  
	$("#search").bind("click", initTable);	
	$("#btnOk").bind("click", btnOk);
});
</script>
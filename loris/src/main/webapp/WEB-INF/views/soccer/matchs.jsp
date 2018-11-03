<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.JcMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%
    String issue = request.getParameter("issue");
	if(StringUtils.isEmpty(issue))
	{
		issue = JcMatchUtil.getCurrentIssue();
	}
%>

<style type="text/css">

.container_wrapper .gridTable td
{
	inline-height: 19px;
}

.container_wrapper .gridTable td .oddsvalue {
    display: inline;
    padding-right: 5px;
    padding-left: 2px;
}

.container_wrapper .gridTable td .handicap {
    display: inline-block;
    padding-right: 2px;
    padding-left: 2px;
    width: 48px;
    overflow: hidden;
    text-align: center;
    vertical-align: bottom;
}

.container_wrapper .gridTable td a.analysis {
    display: inline-block;
    /*float: left;*/
    overflow: hidden;
    padding-right: 5px;
}

.container_wrapper .gridTable td .vsclass {
    display: inline-block;
    /*float: left;*/
    width: 15px;
    overflow: hidden;
    padding: 0px;
    margin: 0 2px 0 2px;
    inline-height: 15px;
}
.container_wrapper .gridTable td a.teamInfo {
    display: inline-block;
    /*float: left;*/
    width: 72px;
    padding: 0px;
    inline-height: 15px;
    color: #1453ba;
    overflow: hidden;
	word-break: keep-all;
	white-space: nowrap;
	text-overflow: ellipsis;
}

.container_wrapper .gridTable td .team {
	inline-height: 20px;
}

.container_wrapper .gridTable td a.left {
    text-align: right;
}

.container_wrapper .gridTable td a.right {
    text-align: left;
}

.container_wrapper .gridTable .th-inner {
    height: 28px;
    padding: 1px;
}
.container_wrapper #sortBtn .sortElement
{
    background: #EBF6FF;
    width: 224px;
    position: absolute;
    top: 25px;
    left: 0px;
    z-index: 401;
    height: 190px;
    padding: 20px 10px 0 10px;
    display: none;
}

.container_wrapper #sortBtn:hover .sortElement
{
	display: block;
}
.container_wrapper #txtDate
{
	display: inline-block;
	width: 100px;
}
</style>

<div id="content" class="container_wrapper">
	<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
		<div style="float: left;">
			<span class="location">当前位置：</span><a href="/" class="aco">东彩首页</a> &gt; <b>数据比较分析</b>
		</div>
	</div>
	
	<div id="main" class="main_wrapper">
		<div id="toolbar">
			<div class=btn-group style="margin-right:5px;">
				<label for="txtDate">比赛日期：</label>
				<input type="text" id="txtDate" class="form-control" data-format="YYYY-MM-DD" style="height:26px;" placeholder="查询数据"/>					
			</div>
			<div class=btn-group style="margin-right:20px;">
				<button id="btnSearch" class="btn btn-default"><i class="glyphicon glyphicon-zoom-in">浏览</i> </button>					
			</div>
			<div class="btn-group btn-group-sm" style="margin-right:20px;"> 
				<button id="btnUpdate" class="btn btn-default"><i class="glyphicon glyphicon-plus">更新</i></button> 
			 	<button id="btnRefresh2" class="btn btn-default"><i class="glyphicon glyphicon-heart"></i></button> 
			 	<button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button> 
			 	<button id="btnRefresh" class="btn btn-default"><i class="glyphicon glyphicon-refresh">刷新</i></button>
			</div>
		</div>
		
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true"
			data-show-refresh="true">
		</table>
	</div>
</div>

<script type="text/javascript">
var url="../soccerdata/getMatches";
var refresh = true;

function initTable() 
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		url: url,
		striped: true,                        	//表格显示条纹 
		pagination: true,                   	//启动分页 
		sortable: true,           				//是否启用排序 
	    sortName:"matchtime", 
	    sortOrder: "desc",          			//排序方式 
		pageSize: 10,                       	//每页显示的记录数 
		pageNumber:1,                       	//当前第几页 
		pageList: [10, 20, 30, 50],       		//记录数可选列表 
		search: false,                         	//是否启用查询 
		showColumns: false,                    	//显示下拉框勾选要显示的列 
		showRefresh: false,                    	//显示刷新按钮 
		sidePagination: "server",             	//表示服务端请求
		toolbar: "#toolbar",
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined", 
		queryParams: queryParams,				//设置查询参数
		silent: false,  						//刷新事件必须设置 
		columns: [
			{
				title: '序号',
				valign: 'middle',
				formatter: function(value, row, index)
				{
					return (index + 1);
				}
			},
			{
				field: "leaguename",
				title: "比赛",
				valign: 'middle',
				formatter: function(value, row, index)
				{
					return value;
				}
			},
			{
	        	field: 'homename',
	         	title: '球队',
	         	valign: 'middle',
	         	formatter: function(value, row, index)
	         	{
	         		var str = '<div class="team"><a class="teamInfo left" tid="' + row.homeid 
	         		str	+= '" href="#" onclick="showTeamInfo(this, ' + row.homeid + ')" title="'; 
	         		str	+= row.homename + '">';
	         		//str += $.isNullOrEmpty(row.homerank) ? '' : '[' + row.homerank + ']';
	         		str += row.homename 
	         		str	+= '</a> <div class="vsclass" > vs </div> <a class="teamInfo right" tid="' 
	         		str += row.clientid +'" href="#" onclick="showTeamInfo(this, ' + row.homeid + ')" title="';
	         		str	+= row.clientname + '">' + row.clientname; 
	         		//str += $.isNullOrEmpty(row.clientrank) ? '' : '[' + row.clientrank + ']';
	         		str += '</a></div>';
	         		return str;
	         	},
	     	},
	     	{
	        	field: 'matchtime',
	        	title: '比赛时间',
	        	sortable: true,
	        	valign: 'middle',
	        	valign: 'middle',
	        	formatter: function(value, row, index)
				{       		
					return '<div class="teamInfo" title="' + value + '">' + value + "</div>";
				}
	    	},
	    	{
	        	field: 'score',
	        	title: '比赛结果',
	        	valign: 'middle',
	        	valign: 'middle',
	    	},
	    	{
	        	field: 'mid',
	        	title: '详细信息',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
	        		var mid = value;
			    	var html = '<a class="analysis" href="bjop?mid=' + mid + '" target="blank">欧</a>';
			    	html += '<a class="analysis" href="ypdb?mid=' + mid + '" target="blank">亚</a>';
			    	html += '<a class="analysis" href="bfyc?mid=' + mid + '" target="blank">析</a>';
	        		return html;
				},
	    	}    		
     	],
		onLoadSuccess: function(){ //加载成功时执行 
		    layer.msg("加载成功");
			//$("#cusTable").TabStyle(); 
		},
		formatLoadingMessage: function(){
		    return "请稍等，正在加载中......";
		},
		onLoadError: function(){ //加载失败时执行 
			layer.msg("加载数据失败", {time : 1500, icon : 2}); 
		} 
	});
}

//设置查询参数 
function queryParams(params) {
	var date = $("#txtDate").val();
	var param = { 
    	current: params.pageNumber, 
     	size: params.pageSize,
     	date: date,
    };
	
	param.asc = (params.sortOrder == 'asc') ? 1 : 0;	
	if(param.asc == 1)
	{
		param.ascs = params.sortName;	
	}
	else
	{
     	param.descs = params.sortName;
	}	
    return param;     
};

$(document).ready(function()
{
	$("#txtDate").datepicker({format: "yyyy-mm-dd"});
	//调用函数，初始化表格  
	initTable();
});
</script>
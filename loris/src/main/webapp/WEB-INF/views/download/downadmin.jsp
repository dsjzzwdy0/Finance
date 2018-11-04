<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style type="text/css">
.container-wrapper
{
	margin:auto;
	width: 83%;
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

.container-wrapper .container-left
{
	width: 16%;
	min-width: 160px;
	float: left;
	text-align: left;
	padding-right: 8px;
}

.container-right
{
	width: 84%;
	float: right;
}


.custom-nav {
    margin-bottom: 10px;
}

.custom-nav > li > a {
    color: #fff;
    padding: 10px 14px;
    border-radius: 0;
    -webkit-border-radius: 0;
    background-color: #2cbcec;
    font:bold 14px/20px 宋体,arial,sans-serif;
}

.custom-nav > li > a:hover,
.custom-nav > li > a:active {
    background-color: #ff00ff;
    color: #65cea7;
    border-radius: 0;
    -webkit-border-radius: 0;
}


.custom-nav > li.menu-list > a:hover,
.custom-nav > li.menu-list > a:focus,
.custom-nav > li.menu-list > a:active{
    background-color: #ff00ff;;
    background-image: url(../content/images/plus.png);
}

.custom-nav > li.nav-active > a {
    background-color: #353f4f;
    background-image: url(../content/images/minus.png);
    color: #65cea7;
}

.custom-nav > li.nav-active > ul{
    display: block;
}

.custom-nav > li.nav-active > a:hover {
    background-image: url(../content/images/minus.png);
}

.custom-nav > li.active > a,
.custom-nav > li.active > a:hover,
.custom-nav > li.active > a:focus {
    background-color: #353f4f;
    color: #65cea7;
}

.custom-nav > li.menu-list.active > a {
    background-image: url(../content/images/plus.png);
}

.custom-nav > li.nav-active.active > a {
    background-image: url(../content/images/minus.png);
}

.custom-nav > li.nav-active.active > a:hover {
    background-image: url(../content/images/minus.png);
}

.custom-nav li .fa {
    font-size: 16px;
    vertical-align: middle;
    margin-right: 10px;
    width: 16px;
    text-align: center;
}

.custom-nav .sub-menu-list {
    list-style: none;
    display: block;
    margin: 0;
    padding: 0;
    background: #efefef;
}

.custom-nav .sub-menu-list > li > a {
    color: #0f0f0f;
    font-size: 13px;
    display: block;
    padding: 10px 5px 10px 50px;
    -moz-transition: all 0.2s ease-out 0s;
    -webkit-transition: all 0.2s ease-out 0s;
    transition: all 0.2s ease-out 0s;
}

.custom-nav .sub-menu-list > li > a:hover,
.custom-nav .sub-menu-list > li > a:active,
.custom-nav .sub-menu-list > li > a:focus {
    text-decoration: none;
    color: #65cea7;
    background: #2a323f;
}

.custom-nav .sub-menu-list > li .fa {
    font-size: 12px;
    opacity: 0.5;
    margin-right: 5px;
    text-align: left;
    width: auto;
    vertical-align: baseline;
}

.custom-nav .sub-menu-list > li.active > a {
    color: #65CEA7;
    background-color: #2A323F;
}

.custom-nav .sub-menu-list ul {
    margin-left: 12px;
    border: 0;
}

.custom-nav .menu-list.active ul {
    display: block;
}
</style>

<div class="container-wrapper">
	<div class="minibars">
		<div class="nav-bar">
			<span class="location">当前位置：</span><a href="admin" class="aco">数据下载首页</a> &gt; <b>数据下载管理页面</b>
		</div>
	</div>
	<div class="main-wrapper">
		<div class="container-left">
			<c:forEach var="cates" items="${settings }">
				<ul class="nav nav-pills nav-stacked custom-nav">
					<li class="menu-list">
						<a href="#" id="${cates.key}" name="category"><i class="fa fa-file-text"></i><span>${cates.key}</span></a>
						<ul class="sub-menu-list">
							<c:forEach var="setting" items="${cates.value}">
								<li><a href="#" id="${setting.wid }" name="node">${setting.name }</a></li>
							</c:forEach>
						</ul>
					</li>
				</ul>
			</c:forEach>
			<ul class="nav nav-pills nav-stacked custom-nav">
				<li class="menu-list active">
					<a href="#" id="all" type="category"><i class="fa fa-file-text"></i><span>所有下载类型</span></a>
				</li>
			</ul>
		</div>
		<div class="container-right">
			<table id="gridTable" class="gridTable table-hover">
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">

var wid= "all";
var type= "category";

InitPage = function()
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		method: "get",                        	//使用get请求到服务器获取数据 
		url: "downlist",             			//获取数据的Servlet地址 
		striped: true,                        	//表格显示条纹 
		pagination: true,                   	//启动分页 
		sortable: true,           				//是否启用排序 
	    sortName:"createtime", 
	    sortOrder: "desc",          			//排序方式 
		pageSize: 15,                       	//每页显示的记录数 
		pageNumber:1,                       	//当前第几页 
		pageList: [15, 20, 30, 50],       		//记录数可选列表 
		search: false,                         	//是否启用查询 
		showColumns: false,                    	//显示下拉框勾选要显示的列 
		showRefresh: false,                    	//显示刷新按钮 
		sidePagination: "server",             	//表示服务端请求 
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined", 
		queryParams: queryParams,				//设置查询参数
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

//设置查询参数 
function queryParams(params) {
	var param = { 
    	pageNumber: params.pageNumber, 
     	pageSize: params.pageSize,
     	sortName: params.sortName,
     	sortOrder: params.sortOrder, //排序方式 
     	wid: wid,
     	type: type,
    }; 
    return param;     
};

function refreshGridTable()
{
	$("#gridTable").bootstrapTable('refresh');
}

//初始化左侧导航栏
function initLeftNav()
{
	$(".container-left li a").each(function(index)
	{
		$(this).on('click',function(){
			$(".container-left li a").each(function(index){
				$(this).parent().removeClass("active");
			});
			$(this).parent().addClass("active");
			type = $(this).attr("name");
			wid = $(this).attr("id");
			
			refreshGridTable();
			//layer.msg(id + ' ' + type);
		});
	});
}

//页面初始化
$(document).ready(function(){
	//初始化页面数据	
	InitPage();
	initLeftNav();	
});
</script>

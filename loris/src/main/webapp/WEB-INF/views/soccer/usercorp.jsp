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
}

.nameText
{
	float: left;
	height: 28px;
	margin: 10px 0 10px 10px;
	display: block;
}
</style>

<div class="container_wrapper">
	<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
		<div style="float: left;">
			<span class="location">当前位置：</span><a href="../" class="aco">东彩首页</a> &gt; <a href="settings" class="aco">公司配置</a> &gt; <b>博彩公司设置</b>
		</div>
	</div>
	<div id="toolbar" class="toolbar">
		<div class="nameText">
			<label for="name">配置名称：<input type="text" id="name" style="margin-left:3px;"/></label>
			<input type="button" class="btn btn-warning" value="保 存" id="btnSave"/>
			<input type="button" class="btn btn-info" value="显 示" id="btnShow"/>
		</div>
	</div>
	<div id="main" class="main_wrapper">
		<table class="gridTable table table-hover table-striped" id="gridTable" data-pagination="false"
			data-show-refresh="false" data-show-toggle="false"
			data-showColumns="false">
		</table>
	</div>
</div>

<script type="text/javascript">
var sid = '<%=sid%>'
var url = '../soccerdata/getCorpSettingData'
var setting;
var corps;

function queryParams(params) { //设置查询参数 
	var param = { 
		current: params.pageNumber, 
	    size: params.pageSize,
	    sid: sid,
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
}

function containsCorp(row)
{
	if($.isNullOrEmpty(setting) || $.isNullOrEmpty(setting.params))
	{
		return false;
	}
	var params = setting.params;
	var len = params.length;
	var gid = row.gid;
	var source = row.source;
	for(var i = 0; i < len; i ++)
	{
		var p = params[i];
		if(gid == p.value && source == p.value1)
		{
			return true;
		}
	}
	
	return false;
}

function getCorps(request) 
{
	$.ajax({
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data:{
			sid: sid,
		},
		jsonp:'callback',
        success : function (msg)
        {
			setting = msg.data.setting;
			if(!$.isNullOrEmpty(setting))
			{
				$('#name').val(setting.name);
			}
			corps = msg.data.corps;
			request.success({
                row : corps
            });
            $('#gridTable').bootstrapTable('load', corps);
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
		ajax: getCorps,
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
        	field: 'gid',
       		title: '选择',
       		formatter: formaterCheck
     	},
     	{
			field: "id",
			sortable: true,
			title: "编号",
			formatter: function(value, row, index)
     		{
     			return index + 1;
     		}
		},
		{
        	field: 'gid',
         	title: '公司编号',
         	sortable: true,
         	formatter: formatterMid
     	},
     	{
        	field: 'name',
        	sortable: true,
        	title: '公司名称'
    	}, 
    	{
        	field: 'ismain',
       		title: '是否主流公司'
     	},
     	{
     		field: 'type',
     		title: '数据类型',
     		formatter: function(value, row, index)
     		{
     			return value == 'yp' ? '亚盘' : '欧赔';
     		}
     	},
     	{
     		field: 'source',
     		title: '数据来源',
     		formatter: function(value, row, index)
     		{
     			if(value == 'zgzcw')
     				return '中国足彩网';
     			else if(value == 'okooo')
     				return '澳客网';
     			else
     				return '未知来源';
     		}
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
	var rows = $("#cusTable tbody tr");
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
	var gid = value;
	var html = '<input value="' + value + '" type="checkbox" ';
	if(containsCorp(row)) html += ' checked';
	html += ' />'
	return html;
}

function showData()
{
	var text = $('#btnShow').val();
	if(text == '显 示')
	{
		$('#btnShow').val('隐 藏 ');
		var oMatchList = $("#gridTable input");
		oMatchList.each(function(){
			if(!$(this).prop("checked"))
			{
				$(this).parent().parent().hide();
			}
	    });
	}
	else
	{
		$('#btnShow').val('显 示');
		var oMatchList = $("#gridTable tr");
		oMatchList.each(function(){
			$(this).show();
	    });
	}
}

function saveInfo()
{
	var name = $("#name").val();
	if($.isNullOrEmpty(name))
	{
		layer.alert('您没有输入配置的名称', {icon: 6});
		$("#name")[0].focus();
		return;
	}
	
	var gids ={};
	var oMatchList = $("#gridTable input");
	oMatchList.each(function(){
		gids[$(this).val()] = $(this).prop("checked");
    });
	
	$.ajax({
		url:'../soccerdata/saveCorpSetting',
	    type:'POST', //GET
	    async:true,    //或false,是否异步
	    data:{
	        sid: sid,
	    	name: name,
	        json: JSON.stringify(gids)
	    },
	    timeout:5000,    //超时时间
	    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
	    success: function(msg)
	    {
            if(msg.status != 200)
            {
            	layer.msg('保存数据失败，请检查');
            	return;
            }
            else
            {
            	setting = msg.data;
            	sid = setting.id;
            	layer.msg('保存成功: ' + sid);            	
            	$('#gridTable').bootstrapTable('load', corps);
            }
      	}
	});
	//layer.msg(gids.join(', '));
}

$(document).ready(function() {
	//调用函数，初始化表格  
	initTable();
	//当点击查询按钮的时候执行  
	$("#btnSave").bind("click", saveInfo);
	$("#btnShow").bind("click", showData);
});
</script>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>

.container-wrapper
{
	margin:auto;
	width: 1100px;
	min-width: 1100px;
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

.form-horizontal .control-label
{
	float: left;
	width: 15%;
	max-width: 120px;
	min-width: 70px;
}

.control-panel
{
	text-align: left;
	overflow: hidden;
	padding-left: 15px;
	
}

.panel
{
	margin-top: 20px;
	box-shadow: 0  0px  0px rgba(0,0,0,.05);
}

.form-actions
{
	text-align: right;
}

.progress
{
	height: 36px;
	vertical-align: middle;
}

.progress-bar
{
	font-size: 18px;
	line-height: 36px;
}

</style>

<div class="container-wrapper">
	<div class="minibars">
		<div class="nav-bar">
			<span class="location">当前位置：</span><a href="admin" class="aco">数据下载首页</a> &gt; <b>数据下载管理页面</b>
		</div>
	</div>

	<section class="panel">
		<div class="panel-body">
			<form id="webPageInfoForm" class="form-horizontal" action="#">
				<div class="form-group">
					<label class="control-label">
						当前唯一标识
					</label>
					<div class="11col-md-10 control-panel">
						<input id="id" size="16" type="text" value="" class="form-control" disabled=""/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						下载器名称
					</label>
					<div class="11col-md-10 control-panel">
						<input id="name" size="16" type="text" value="" class="form-control" disabled=""/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						下载器类型
					</label>
					<div class="11col-md-10 control-panel">
						<input id="classname" size="16" type="text" value="test" class="form-control" disabled="" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						类别
					</label>
					<div class="11col-md-10 control-panel">
						<input id="category" size="16" type="text" value="test" class="form-control" disabled="" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						英文标识
					</label>
					<div class="11col-md-10 control-panel">
						<input id="wid" size="16" type="text" value="test" class="form-control"	disabled="" />
					</div>
				</div>
				<!-- <div class="form-group">
				<label class="control-label" for="datesection">设置日期起止时间</label>
				<div class="col-md-1">
				<input id="datesection" type="checkbox" class="form-control">
				</div>
				-->
				<div class="form-group">
					<label class="control-label">
						开始日期
					</label>
					<div class="col-md-4">
						<input id="start" size="16" type="text" class="form_datetime form-control"/>
					</div>
					<label class="control-label">
						结束日期
					</label>
					<div class="col-md-4">
						<input id="end" size="16" type="text" class="form_datetime form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						数据编码
					</label>
					<div class="col-md-10" style="text-align: left;">
						<select id="encoding" class="selectpicker">
							<option>
								UTF-8
							</option>
							<option>
								GBK
							</option>
							<option>
								GB2312
							</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						当前数据
					</label>
					<div class="col-md-10" style="text-align: left;">
						<select id="interval" class="selectpicker">
							<option>
								2000
							</option>
							<option>
								2500
							</option>
							<option>
								3000
							</option>
							<option>
								3500
							</option>
							<option>
								4000
							</option>
							<option>
								4500
							</option>
							<option>
								5000
							</option>
							<option>
								5500
							</option>
							<option>
								6000
							</option>
							<option>
								6500
							</option>
							<option>
								7000
							</option>
							<option>
								7500
							</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">
						下载说明
					</label>
					<div class="11col-md-10 control-panel">
						<input id="description" size="16" type="text" value="股票数据下载" class="form-control"/>
					</div>
				</div>
				<div id="progressBarPanel" class="form-group form-group-progress" style="display: none;">
					<label class="control-label">
						下载进度
					</label>
					<div class="11col-md-10 control-panel" style="text-align: left;">
						<div class="progress progress-striped">
							<div id="curProgress" style="width: 10%" aria-valuemax="100" aria-valuemin="0"
							aria-valuenow="10" role="progressbar" class="progress-bar progress-bar-danger">
								<span class="">
									10%
								</span>
							</div>
						</div>
						<label id="lblProgressInfo" for="progress" style="margin-top:0px; width:100%">
						</label>
						<label id="lblInfo" style="margin-top:0px; width:100%">
						</label>
					</div>
				</div>
				<div class="form-actions">
					<div class="buttons">
						<button id="btnStart" type="button" class="btn btn-success">开始下载</button>
						<button id="btnRefresh" type="button" class="btn btn-info">刷新页面</button>
						<button id="btnStop" type="button" class="btn btn-danger">停止下载</button>
					</div>
				</div>
			</form>
		</div>
	</section>
</div>

<script type="text/javascript">

//定时器，定时更新动态信息
var timer = null;
var id = '';     //ID值
var wid = '';    //下载器的值

/**
* Ajax function:
* url: 调用数据的地址;
* metho: 方法,
* params: 参数
* successFunc: 回调函数
* failFunc: 回调函数

function ajaxFunction(url, method, params, successFunc, failFunc)
{
} */

/** 
* 判断是否null 
* @param data 

function $.isNullOrEmpty(data){ 
	return (data == "" || data == undefined || data == null || data == 'null'); 
}*/

//设置页面信息
SetPageValue = function(page)
{
	//无可用的更新的信息
	if($.isNullOrEmpty(page))
	{
		//清空信息
		$("#id").val('');
		$("#name").val('');
		$("#classname").val('');
		$("#description").val(''),
		$("#category").val('');
		$("#wid").val('');
		return;
	}
	else
	{
		$("#id").val(page.id);
		$("#name").val(page.name);
		$("#classname").val(page.classname);
		$("#description").val(page.description);
		$("#encoding").selectpicker("val", page.encoding);
		$("#interval").selectpicker("val", page.interval);
		$("#category").val(page.category);
		$("#wid").val(page.wid);
		
		var d = formatDate(new Date(), "yyyy-MM-dd");
		if(page.start == null || page.start === undefined)
		{
			$("#start").val(d);
		}
		else
		{
			$("#start").val(page.start);
		}
		if(page.end == null || page.end === undefined)
		{
			$("#end").val(d);
		}
		else
		{
			$("#end").val(page.end);
		}
	}
}

function formatDuring(mss) {
	var days = parseInt(mss / (1000 * 60 * 60 * 24));
	var hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
	var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
	var seconds = (mss % (1000 * 60)) / 1000;
	return (days > 0 ? days + " 天 " : '') + hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒 ";
}
	
function getTimeInfo(left, interval)
{
	var leftnum = parseInt(left);
	var pertime = parseInt(interval);
	if(leftnum != 'NaN' && leftnum > 0 && pertime != 'NaN' && pertime > 0)
	{
		var t = pertime * leftnum;
		var info = ', 预计还需要' + formatDuring(t);
	}
	return '';
}

//设置当前状态信息
SetCurrentStatus= function(page)
{
	if($.isNullOrEmpty(page))
	{
		return;
	}
	
	var cur = page.curindex;
	var total = page.total;
	var left = page.left;
	var info = page.info;
	var value = 0; 
	if(total == 0)
	{
		value = 100;
	}
	else
	{
		value = Math.round((total - left) * 100 / total);
	}
		
	var status = page.status;
	
	var statusInfo = '';
	switch(status)
	{
	case 0:	
		statusInfo = '已经创建下载器';
		break;
	case 1:
		statusInfo = '已经初始化下载管理器'
		break;
	case 2:
		statusInfo = '正在下载数据';
		info += getTimeInfo(left, page.interval);
		break;
	case 3:
		statusInfo = '已经停止数据下载'
		break;
	case 4:
		statusInfo = '数据已经下载完成'
		break;
	default:
		break;
	}
	
	$("#curProgress").css("width", value + "%").text(value + "%");
	$("#lblProgressInfo").html(page.description + " total = " + total + ", left = " + left 
			+ ", cur = " + cur);
	$("#lblInfo").html($.isNullOrEmpty(info) ?  " " : (statusInfo + ": " + info));
}

//
//获得当关的状态信息
//
GetWebPageStatus = function(id, setpage)
{
	if($.isNullOrEmpty(id))
	{
		layer.msg("没有指定ID值");
		return;
	}
	var params = {
		id: id
	};
	$.ajax({
		url: "status",
		type: "get",
		data: params,
		dataType: "json",
		success: function(json){
			$("#progressBarPanel").show();
			if(json == null || json.data == null)
			{
				//layer.msg('更新出现错误.')
				$("#lblProgressBar").html('更新出现错误.');
				return;
			}
			
			if(setpage)
			{
				SetPageValue(json.data);
			}
			
			SetCurrentStatus(json.data);
			if(json.data.status == 2 || json.data.status == 0 || json.data.status == 1)    //在当前下载的状态下，启动监听器
			{
				if(timer == null || timer === undefined)
				{
					StartStatusListener();
				}
			}
			else //if(json.data.status == 3)   //其它状态下，停止当前的监控器
			{
				StopStatusListener();
			}
		},
		error: function(json){
			layer.msg("更新信息时出现错误");
			StopStatusListener();
		}
	});
}

//
//创建新的空下载器
//
CreateNewDownloader = function(name)
{
	$.ajax({
		url: "basicpage",
		type: "get",
		data: {name: name},
		dataType: "json",
		success: function(json){
			layer.msg('初始化数据成功');
			id = '';
			SetPageValue(json.data);
		},
		error: function(json){
			layer.msg("在调用基本信息时失败");
		}
	})
}


//初始化左侧总体导航面版
InitNavLeftPanel = function(basiclist)
{
	var html = [];
	var categories = [];
	var len = basiclist.length;
	for(var i = 0; i < len; i ++)
	{
		var page = basiclist[i];
		var cate = page.category;
		var exist = false;
		
		for(var j = 0; j < categories.length; j ++)
		{
			if(cate == categories[j])
			{
				exist = true;
				break;	
			}
		}
		
		if(!exist)
		{
			categories.push(cate);	
		}
	}
	
	for(var i = 0; i < categories.length; i ++)
	{
		var cate = categories[i];
		html.push('<ul class="nav nav-pills nav-stacked custom-nav"><li class="menu-list"><a href="#">');
		html.push('<i class="fa fa-file-text"></i><span>');
		html.push(cate);
		html.push('</span></a> <ul class="sub-menu-list">')
		
		for(var j = 0; j < len; j ++)
		{
			var page = basiclist[j];
			if(cate != page.category)
			{
				continue;
			}
			
			html.push('<li><a id="' + page.wid + '" href="#">');
			html.push(page.name);
			html.push('</a></li>')
		}
		html.push('</ul></li></ul>');
	}	
	$("#navLeftPanel").html(html.join(''));
	
	//监听事件
	$("#navLeftPanel .sub-menu-list a").click(function(){
		var name = $(this).attr("id");
		CreateNewDownloader(name);
	});
}

//数据初始化
InitPage = function(keyValue, refresh)
{
	$.ajax({
		url: "basiclist",
		type: "get",
		data: "",
		dataType: "json",
		success: function(json){
			//layer.msg('初始化数据成功');			
			var basiclist = json.data.basic;
			//InitNavLeftPanel(basiclist);
			
			//值为空时
			if(refresh)
			{
				if( $.isNullOrEmpty(keyValue))
				{	
					if(basiclist != null && basiclist.length > 0)
					{
						SetPageValue(basiclist[0]);
					}
				}
				else
				{
					GetWebPageStatus(keyValue, true);
				}
			}
		},
		error: function(json){
			layer.msg("在调用基本信息时失败");
		}
	});
}


//更新当前标准
UpdateStatus = function()
{
	var pid = $("#id").val();
	if($.isNullOrEmpty(pid))
	{
		pid = id;
	}
	
	if($.isNullOrEmpty(pid))
	{
		StopStatusListener();
		return;
	}
	GetWebPageStatus(pid, false);
}

//停止状态监控器
StopStatusListener = function()
{
	if(timer != null && timer != undefined)
	{
		//$("#progressBarPanel").hide();
		//$("#curProgress").css("width", 0 + "%").text(0 + "%");
		//$("#lblProgressInfo").html("");
		clearInterval(timer);
		timer = null;
	}
}

//启动状态监控器
StartStatusListener = function()
{
	if(timer != null)
	{
		StopStatusListener();
	}
	timer = setInterval(UpdateStatus, 1000);
}

//
//开始下载数据
//
StartDownloader = function()
{
	var params = $('#webPageInfoForm').GetWebControls('');
	params['enable'] = true;
	params['daysection'] = true;
	//var refreshPage = false;
	
	//如果ID值为空时，则需要更新
	//if($.isNullOrEmpty(params.id))
	//{
	//	refreshPage = true;
	//}
	
	$.ajax({  
      url: "start",  
      type: "get",  
      data: params,  
      dataType: "json",  
      success: function(json) {
      	if(json.status === 200)
      	{
      		layer.msg(json.msg);
      		//if(refreshPage)
				
      		if(!$.isNullOrEmpty(json.data))
      		{
      			id = json.data.id;
      			SetPageValue(json.data);
      		}
      		
      		//StopStatusListener();
      		StartStatusListener();
      	}
      	else
      		layer.msg('失败：' + json.msg);
      }
	});
}

StopDownloader = function()
{
	var params = {
		id: $("#id").val()
	};
	$.ajax({  
  	url: "stop",  
      type: "get",  
      data: params,  
      dataType: "json",  
      success: function(json) {
       	if(json.status === 200)
         	{
       		layer.msg('已经停止' + json.data.name);
       		SetCurrentStatus(json.data);
       		StopStatusListener();
         	}
         	else
         		layer.msg('失败：' + json.msg);
      },
      error: function(json){
        	layer.msg('取消下载失败')	
      }
	});
}

$(document).ready(function () 
{
	id = request('id');
	wid = request('wid');
	if(!$.isNullOrEmpty(id))
	{
		//初始化页面数据	
		InitPage(id, true);
	}
	else
	{
		if($.isNullOrEmpty(wid))
		{
			wid = 'gpdate';
		}
		CreateNewDownloader(wid);
	}

	//日期选择函数
	$(".form_datetime").datepicker({
		format : 'yyyy-mm-dd'
	});
	$('.selectpicker').selectpicker({
		style: 'btn-info',
		size: 16,
	});
	
	$('#btnStart').click(function(){
		StartDownloader();
	});
	
	$('#btnRefresh').click(function(){
		layer.msg("刷新页面");
		InitPage(id, true);
	});
	
	//停止当前下载的数据
	$("#btnStop").click(function(){
		StopDownloader();
	});	
});

</script>
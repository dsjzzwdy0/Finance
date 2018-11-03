<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">

.container_wrapper
{
	width: 1200px;
	margin: auto;
	top: 10px;
	font-family: Arial;
}
.input-title
{
	height: 155px;
}

.user-input
{
	margin-top: 15px;
	float: left;
	height: 135px;
	width: 100%;
	text-align: left;
	border: 1px solid #141414;
}

.compute-info
{
	height: 110px;
	margin-top: 10px;
	line-height: 30px;
	text-align: left;
	font-size: 14px;
	/*background-color: #f4f4f4;*/
}

.main_wrapper
{
	margin-top: 10px;
}

.user-input .info
{
	width: 100%;
	text-align: left;
	height: 35px;
	line-height: 35px;
	font-size: 14px;
	background-color: #f4f4f4;
	border-bottom: 1px solid #141414;
	font-weight: bold;
	padding-left: 5px;
}

.user-input .content
{
	margin-top: 10px;
	margin-left: 8px;
	height: 40px;
}

.user-input .input-button
{
	text-align: center;
	height: 40px;
	padding-top: 5px;
}

.user-input label{
	margin-left: 5px;
}

.button {
  background: #007dc1;
  background-image: -webkit-linear-gradient(top, #007dc1, #0061a7);
  background-image: -moz-linear-gradient(top, #007dc1, #0061a7);
  background-image: -ms-linear-gradient(top, #007dc1, #0061a7);
  background-image: -o-linear-gradient(top, #007dc1, #0061a7);
  background-image: linear-gradient(to bottom, #007dc1, #0061a7);
  -webkit-border-radius: 3;
  -moz-border-radius: 3;
  border-radius: 3px;
  text-shadow: 0px 1px 0px #154682;
  -webkit-box-shadow: inset 0px 1px 0px 0px #54a3f7;
  -moz-box-shadow: inset 0px 1px 0px 0px #54a3f7;
  box-shadow: inset 0px 1px 0px 0px #54a3f7;
  font-family: Arial;
  color: #ffffff;
  font-size: 13px;
  padding: 6px 30px 6px 30px;
  border: solid #124d77 1px;
  text-decoration: none;
}

.button:hover {
  color: #ffffff;
  background: #0061a7;
  text-decoration: none;
}

.a-ca-ht-div {
	background-image: url(../content/images/slope.png);
	background-size: 100% 100%;
}

.a-ca-head-title {
	border-right: solid 1px #e5e5e5;
	background-color: white;
	padding: 0 !important;
}
.gridTable .ordinary
{
	width: 100px;
}

.gridTable table tr
{
	background-color: transparent;
}

.gridTable #tableHeader th
{
	color: white;
	background-color:  #008B8B;
}

.container_wrapper .gridTable #tableHeader .th
{
	height: 25px;
	border: 1px solid #ddd;
	line-height: 25px;
	vertical-align: middle;
}

.container_wrapper .compute-info .gridTable .th
{
	height: 25px;
	border: 1px solid #ddd;
	line-height: 25px;
	vertical-align: middle;
}

.compute-info .gridTable table tr
{
	background-color: transparent;
}

.compute-info .gridTable th
{
	color: white;
	background-color:  #008B8B;
}

.gridTable .content tr
{
	height: 34px;
}

.container_wrapper .gridTable td
{
	height: 35px;
	border: 1px solid #ddd;
	text-align: center;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden; 
}
.compute-info p
{
	margin-left: 6px;
	display: inline;
}
</style>
<div id="content" class="container_wrapper">
	<div class="input-title">
		<div class="user-input">
			<div class="info"><p>球队实力对比输入</p></div>
			<div class="content">
				<label for="homevalue">主队平均进球数:&nbsp;</label><input id="homevalue" type="text" placeholder="输入主队平均进球数"></input>
				<label for="clientvalue">客队平均进球数:&nbsp;</label><input id="clientvalue" type="text" placeholder="输入客队平均进球数"></input>
				<label for="lossratio">返还率:&nbsp;</label><input id="lossratio" type="text" placeholder="输入返还率" value="0.94"></input>
			</div>
			<div class="input-button">
				<input id="btnCompute" type="button" class="button" value="计算"></input>
			</div>
		</div>
	</div>
	
	<div class="compute-info" style="display: none;">
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true" data-show-refresh="true">
			<thead >
				<tr>
					<th>类别</th>
					<th>胜</th>
					<th>平</th>
					<th>负</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<!-- 
		<div class="prob-value">
			<p id="win">甲队胜概率: <em></em></p>
			<p id="draw">平概率: <em></em></p>
			<p id="lose">负概率: <em></em></p>
		</div>
		<div class="odds-value">
			<p id="win">甲队胜赔: <em></em></p>
			<p id="draw">平赔: <em></em></p>
			<p id="lose">负赔: <em></em></p>
		</div>
		 -->
	</div>

	<div id="main" class="main_wrapper">
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true" data-show-refresh="true">
			<thead id="tableHeader" style="display: none;">
				<th rowspan="2" class="ordinary a-ca-head-title th">
					<div class="a-ca-ht-div">  
			          <table style="width: 100%;">  
			            <thead>  
			              <tr class="a-ca-ht-day">
			              	<th></th>  
			                <th>乙队</th>  
			              </tr>  
			              <tr class="a-ca-ht-worker">  
			                <th>甲队</th>  
			                <th></th>  
			              </tr>  
			            </thead>  
			          </table>  
			        </div>
				</th>
				<th class="th">0</th>
				<th class="th">1</th>
				<th class="th">2</th>
				<th class="th">3</th>
				<th class="th">4</th>
				<th class="th">5</th>
				<th class="th">6</th>
				<th class="th">总计</th>
			</thead>
			<tbody id="tableContent" class="content">
			</tbody>
		</table>
	</div>
</div>

<script type="text/javascript">

var url = "../soccerdata/computeOdds";

function computeOdds(conf)
{
	var options = {
		type: "GET",
		url: url,
		contentType: "application/json;charset=utf-8",
		dataType: "json",
		data : {
			homevalue: conf.homevalue,
			clientvalue: conf.clientvalue,
			lossratio: conf.lossratio
		},
		jsonp: 'callback',
	    success: function (msg){
	    	if(msg.status == '200')
	    	{
	    		showTableContent(msg.data, parseFloat(conf.lossratio));
	    	}
	    	else
	    	{
	    		layer.msg('获取系统数据出错: ' + msg.msg)
	    	}
	    }
	};
	$.ajax(options);
}

function showTableContent(data, loss)
{
	$('#tableHeader').show();
	var goals = data.goals;
	var probs = data.prob;
	
	var k = 7;
	var html = [];
	for(var i = 0; i < k; i ++)
	{
		html.push('<tr>')
		html.push(formatColumn(i, '', ''));
		var t = 0.0;
		for(var j = 0; j < k; j ++)
		{
			html.push(formatColumn(goals[i][j], '', 'number'));
			t += goals[i][j];
		}
		html.push(formatColumn(t, '', 'number'));
		html.push('</tr>')
	}
	
	html.push('<tr>');
	html.push(formatColumn('总计', '', ''));
	for(var i = 0; i < k; i ++)
	{
		var t = 0.0;
		for(var j = 0; j < k; j ++)
		{
			t += goals[j][i];
		}
		html.push(formatColumn(t, '', 'number'));
	}
	html.push(formatColumn(1.0, '', 'number'));
	html.push('</tr>')
	
	$('#tableContent').html(html.join(' '));
	
	$('.compute-info').show();
	showProbValue(probs, loss);
	/*
	$('.prob-value #win em').text(getProbValue(probs[0]));
	$('.prob-value #draw em').text(getProbValue(probs[1]));
	$('.prob-value #lose em').text(getProbValue(probs[2]));
	
	$('.odds-value #win em').text(getOddsVlue(probs[0], loss));
	$('.odds-value #draw em').text(getOddsVlue(probs[1], loss));
	$('.odds-value #lose em').text(getOddsVlue(probs[2], loss));*/
}

function showProbValue(probs, loss)
{
	var html = [];
	
	html.push('<tr>');
	html.push(formatColumn('概率'));
	for(var i = 0; i < 3; i ++)
	{
		html.push(formatColumn(getProbValue(probs[i]), '', ''));
	}
	html.push('</tr>')
	html.push('<tr>');
	html.push(formatColumn('赔率'));
	for(var i = 0; i < 3; i ++)
	{
		html.push(formatColumn(getOddsVlue(probs[i], loss), '', ''));
	}
	html.push('</tr>')
	$('.compute-info .gridTable tbody').html(html.join(' '));
}

function getProbValue(val)
{
	return (100.0 * val).toFixed(2) + '%';
}

function getOddsVlue(prob, loss)
{
	var odds = loss / prob;
	return odds.toFixed(2);
}

function formatColumn(value, className, valueType)
{
	var text = '';
	text += '<td><div ';
	if($.isNotNullOrEmpty(className))
	{
		text += 'class="' + className + '"';
	}
	text += '>' + (valueType == 'number' ? value.toFixed(3) : value);
	text += '</div></td>';
	return text;
}

function getConfValue()
{
	var homevalue = $('#homevalue').val();
	var clientvalue = $('#clientvalue').val();
	var lossratio = $('#lossratio').val();
	
	return {
		homevalue: homevalue,
		clientvalue: clientvalue,
		lossratio: lossratio
	};
}

$(document).ready(function(){
	$('#btnCompute').on('click', function(){
		var conf = getConfValue();
		layer.msg('计算数据: ' + conf.homevalue + ', ' + conf.clientvalue + ', ' + conf.lossratio);
		computeOdds(conf);
	});
});

</script>

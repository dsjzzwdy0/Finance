<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" type="text/css" href="../content/css/soccer/bfyc.css" />
<script type="text/javascript" src="../content/scripts/echarts/dist/echarts.min.js"></script>
<script type="text/javascript" src="../content/scripts/echarts/theme/shine.js"></script>
   	
<%@include file="./analysis/matchheader.jsp"%>
<style type="text/css">
.header
{
	margin: auto;
	height: 40px;
}
.wrapper
{
	width: 80%;
	min-width: 1100px;
	height: 560px;
	margin: auto;
}
.gridTable
{
	width: 100%;
	height: 160px;
	margin-left: 4px;
}
.graphElement
{
	width: 100%;
	height: 400px;
}

.tableValue
{
	width: 33%;
	text-align: center;
	min-width: 330px;
	display: inline-block;
	font-size: 12px;
}

.tableTitle
{
	height: 30px;
	text-align: center;
	width: 90%;
	margin: auto;
	display: block;
}

.tableValue table{
	width: 100%;
}

.tableValue th{
    height: 30px;
    border: 1px solid #ddd;
    line-height: 30px;
    vertical-align: middle;
    width: 12.5%;
}

.tableValue td {
    height: 35px;
    border: 1px solid #ddd;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}

.tableValue .decrease
{
	color: red;
	font-weight: bold;
}

.tableValue .increase
{
	color: green;
	font-weight: bold;
}

</style>


<div class="wrapper">
	<div id="gridTable" class="gridTable">
		<div class="tableValue">
			<div class="tableTitle">胜赔率</div>
			<table id="varWinTable">
				<thead>
					<tr>
						<th style="width: 30px;">序号</th>
						<th>最小</th>
						<th>最大</th>
						<th>平均</th>
						<th>方差</th>
						<th>标差</th>
						<th>偏离</th>
						<th>总数</th>
					</tr>
					</tr>
				</thead>
				<tbody>		
				</tbody>
			</table>
		</div>
		<div class="tableValue">
			<div class="tableTitle">平赔率</div>
			<table id="varDrawTable">
				<thead>
					<tr>
						<th style="width: 30px;">序号</th>
						<th>最小</th>
						<th>最大</th>
						<th>平均</th>
						<th>方差</th>
						<th>标差</th>
						<th>偏离</th>
						<th>总数</th>
					</tr>
				</thead>
				<tbody>		
				</tbody>
			</table>
		</div>
		<div class="tableValue">
			<div class="tableTitle">负赔率</div>
			<table id="varLoseTable">
				<thead>
					<tr>
						<th style="width: 30px;">序号</th>
						<th>最小</th>
						<th>最大</th>
						<th>平均</th>
						<th>方差</th>
						<th>标差</th>
						<th>偏离</th>
						<th>总数</th>
					</tr>
				</thead>
				<tbody>		
				</tbody>
			</table>
		</div>
	</div>
	<div id="chartelement" class="graphElement"></div>
</div>

<script type="text/javascript">

var chart;
var mid = '<%=request.getParameter("mid")%>';
var types = ['初胜赔', '胜赔', '初平赔', '平赔', '初负赔', '负赔']
var colors = ['rgba(228,26,28,0.8)', 'rgba(55,126,184, 0.8)', 
    'rgba(77,175,74, 0.8)', 'rgba(152,78,163, 0.8)', 
    'rgba(0,0,139, 0.8)', 'rgba(166,86,40, 0.8)']

function getData(params)
{
	var $element = $("#chartelement");   //document.getElementById('main')
	chart = echarts.init($element[0]);
	chart.showLoading();
	try
	{
		$.ajax({ 
			url: "../soccerdata/getMatchOpVar",
			data: params,
			context: document.body, 
			success: function(json){
				layer.msg("下载数据成功", {time: 2000})
				initChart(json.data);
				initTable(json.data.vars);
			},
		});
	}
	catch(err)
	{
	}
	chart.hideLoading();
}

function initTable(vars)
{
	createTableValue(vars[0].win, vars[1].win, 'varWinTable');
	createTableValue(vars[0].draw, vars[1].draw, 'varDrawTable');
	createTableValue(vars[0].lose, vars[1].lose, 'varLoseTable');
}

//初始化数组
function getDataArray(counters)
{
	var len = counters.length;
	var datas = [];	
	for(var i = 0; i < len; i ++)
	{
		datas[i] = [];
		datas[i][0] = counters[i].key;
		datas[i][1] = counters[i].value;
	}
	return datas;
}

/**
 * 建立Table数据
 */
function createTableValue(firstvar, latestvar, type)
{
	var html = [];
	html.push('<tr>');
	html.push('<td>初值</td>');
	html.push('<td>' + formatValue(firstvar.min) + "</td>");
	html.push('<td>' + formatValue(firstvar.max) + "</td>");
	html.push('<td>' + formatValue(firstvar.avg) + "</td>");
	html.push('<td>' + formatValue(firstvar.totalerr) + "</td>");
	html.push('<td>' + formatValue(firstvar.stderr) + "</td>");
	html.push('<td>' + firstvar.overnum + "</td>");
	html.push('<td>' + firstvar.size + "</td>");
	html.push('</tr>');
	html.push('<tr>');
	html.push('<td>即时</td>');
	html.push('<td class="' + getTableGridClass(firstvar.min, latestvar.min) + '">' + formatValue(latestvar.min) + "</td>");
	html.push('<td class="' + getTableGridClass(firstvar.max, latestvar.max) + '">' + formatValue(latestvar.max) + "</td>");
	html.push('<td class="' + getTableGridClass(firstvar.avg, latestvar.avg) + '">' + formatValue(latestvar.avg) + "</td>");
	html.push('<td class="' + getTableGridClass(firstvar.totalerr, latestvar.totalerr) + '">' + formatValue(latestvar.totalerr) + "</td>");
	html.push('<td class="' + getTableGridClass(firstvar.stderr, latestvar.stderr) + '">' + formatValue(latestvar.stderr) + "</td>");
	html.push('<td>' + latestvar.overnum + "</td>");
	html.push('<td>' + latestvar.size + "</td>");
	html.push('</tr>');
	$('#' + type + " tbody").html(html.join(''));
}

function getTableGridClass(first, last)
{
	if(Math.abs(last - first) < 0.001) return '';
	else if(last > first) return 'increase';
	else return 'decrease';
}

function formatValue(value)
{
	return $.isNullOrEmpty(value) ? "" : value.toFixed(2) ; 	
}

/**
 * 创建数据系列
 */
function createSerie(variance, index)
{
	var datas = getDataArray(variance.counter.freqs);
	var serie = {
		name: types[index],
		type: 'bar',
		data: datas,
		barWidth : 5,//柱图宽度
		markPoint: {
			data: [{
		    	type: 'max',
		        name: '最大值'
		    }]
		},
		itemStyle: {
        	//这里修改了柱子的颜色以及透明度，注意如果要修改透明度，一定要配置在itemStyle里面，直接写在外面不起作用。
        	normal: {
        		color: colors[index],		        		
        	}
        }
	};
	return serie;
}

function initChart(data)
{	
	var series = [];
	series.push(createSerie(data.vars[0].win, 0));
	series.push(createSerie(data.vars[1].win, 1));
	series.push(createSerie(data.vars[0].draw, 2));
	series.push(createSerie(data.vars[1].draw, 3));
	series.push(createSerie(data.vars[0].lose, 4));
	series.push(createSerie(data.vars[1].lose, 5));
	
	var option = {
		    title: {
		        x: 'center',
		        y: 'top',
		    	text: '百家欧赔分布图'
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data: types,
		        y: 'bottom'
		    },
		    toolbox: {
		        show: true,
		        feature: {
		            mark: {
		                show: true
		            },
		            dataView: {
		                show: true,
		                readOnly: false
		            },
		            magicType: {
		                show: true,
		                type: ['line', 'bar']
		            },
		            restore: {
		                show: true
		            },
		            saveAsImage: {
		                show: true
		            }
		        }
		    },
		    calculable: true,
		    xAxis: [{
		        type: 'value',
		        min: 1.0
		    }],
		    yAxis: [{
		        type: 'value',
		        axisLabel: {
		        	show: false
		        }
		    }],
		    series: series,
		    grid: {
		        show: false,
		        borderColor: '#19507c',
		        x: 5,
		        x2: 5,
		        y: 30,
		        y2: 40
		    },
		};
	chart.setOption(option);
}

$(document).ready(function() 
{
	var params = {
		mid: mid
	};
	getData(params);
});
</script>
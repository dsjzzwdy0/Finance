<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<script type="text/javascript" src="../content/scripts/echarts/dist/echarts.min.js"></script>
<script type="text/javascript" src="../content/scripts/echarts/theme/shine.js"></script>
<%
    String gid = request.getParameter("gid");
%>

<<style>
<!--
.main_wrapper{
	width: 100%;
	height: 800px;
}
-->
</style>
<div id="content" class="container_wrapper">
	<%@include file="./analysis/toolbar.jsp"%>

	<div id="main" class="main_wrapper">
	</div>
</div>


<script type="text/javascript">
var gid = '<%=gid%>';
var start = '2018-06-11';
var end = '2018-11-25';
var chart = null;
var url = '../soccerdata/getCorpOdds';

/**
 * 初始化数据入口
 */
function initData()
{
	var params = {
		gid: gid,
		start: start,
		end: end
	};
	chart.showLoading();
	try
	{
		$.ajax({ 
			url: url,
			data: params,
			context: document.body, 
			success: function(json){
				layer.msg("下载数据成功： " + json.data.length, {time: 2000}) 
				initGraph(json.data);
			},
		});
	}
	catch(err)
	{
	}
	chart.hideLoading();
}


function initGraph(items)
{
	var name = '查看数据记录';
	var data1 = [];
	var data2 = [];
	var data3 = [];
	
	var len = items.length;
	for(var i = 0; i < len; i ++)
	{
		var rec = items[i].values;
		data1[i] = rec[0];
		data2[i] = rec[1];
		data3[i] = rec[2];
	}
	var option = {
		tooltip : {
		    trigger: 'axis',
		    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		    }
		},
		legend: {
		    data:['胜','平','负']
		},
		toolbox: {
		    show : true,
		    orient: 'vertical',
		    x: 'right',
		    y: 'center',
		    feature : {
		        mark : {show: true},
		        dataView : {show: true, readOnly: false},
		        magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
		        restore : {show: true},
		        saveAsImage : {show: true}
		    }
		},
		calculable : true,
		xAxis : [
		        {
		            type : 'category',
		            data : name
		        }
		],
		yAxis : [
		        {
		            type : 'value'
		        }
		],
		series : [
		        {
		            name:'胜',
		            type:'scatter',
		            data: data1
		        },
		        {
		            name:'平',
		            type:'bar',
		            //stack: '广告',
		            data:data2
		        },
		        {
		            name:'负',
		            type:'bar',
		            //stack: '广告',
		            data:data3
		        }
		        
	    ]
	};	
	chart.setOption(option);
}

/**
 * 初始化数据
 */
$(document).ready(function(){
	var $element = $("#main");   //document.getElementById('main')
	chart = echarts.init($element[0], 'shine');
	
	initData();
});

</script>
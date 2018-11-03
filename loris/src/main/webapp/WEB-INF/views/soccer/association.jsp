<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%
    String issue = request.getParameter("issue");
	String sid = request.getParameter("sid");		//配置编号
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
	if(StringUtils.isEmpty(sid))
	{
		sid = "fe30f285-6fef-4785-be16-0fac30235e33";
	}
%>

<style type="text/css">

.container_wrapper .gridTable
{
	min-width: 1000px;
}

.container_wrapper .gridTable td
{
	inline-height: 29px;
	min-height: 25px;
}

.container_wrapper .condition
{
	height: 40px;
	text-align: left;
	margin: 15px 0 10px 0;
}

.container_wrapper .gridTable th
{
	height: 25px;
	border: 1px solid #ddd;
	line-height: 25px;
	vertical-align: middle;
}

.container_wrapper input
{
	vertical-align: middle;
}

.container_wrapper .gridTable label
{
	vertical-align: middle;
	font-size: 12px;
	margin-left: 3px;
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

.container_wrapper .gridTable .oddsvalue
{
	width: 60px;
}

.container_wrapper .gridTable .baseinfo
{
	width: 50px;
}

.container_wrapper .gridTable .matchname
{
	width: 160px;
}

.container_wrapper .checkbox-position label::before
{
	vertical-align:middle;
	line-height: 40px;
	margin-top: 3px;
}

.container_wrapper .gridTable .description
{
	width: 220px;
	min-width: 160px;
}

.container_wrapper .gridTable .association
{
	margin: 0px auto;	
	width: 60px;
	height: 30px;
	line-height: 30px;
	vertical-align: middle;
}

.container_wrapper .gridTable .association_red
{
	background: url(../content/images/soccer/circle-1.png) no-repeat;
}

.container_wrapper .gridTable .association_thin
{
	background: url(../content/images/soccer/circle-2.png) no-repeat;
}

.container_wrapper .gridTable .association_cyan
{
	color: white;
	background: url(../content/images/soccer/circle-3.png) no-repeat;
}

.container_wrapper .gridTable .association_blue
{
	color: white;
	background: url(../content/images/soccer/circle-4.png) no-repeat;
}

.container_wrapper .gridTable .association_orange
{
	color: white;
	background: url(../content/images/soccer/circle-5.png) no-repeat;
}

</style>

<div id="content" class="container_wrapper">
	<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
		<div style="float: left;">
			<span class="location">当前位置：</span><a href="/" class="aco">东彩首页</a> &gt; <b>数据关联分析</b>
		</div>
	</div>
	<div class="condition">
		<div class="checkbox checkbox-info checkbox-circle checkbox-position" style="display: none;">
			<input id="showordinary" class="styled" type="checkbox"/><label for="showordinary">显示序号</label>
		</div>
		<div style="width: 20px;display: inline-block;"></div>
		<div class="checkbox checkbox-info checkbox-circle checkbox-position" style="display: inline;">
			<input id="sameleague" class="styled" type="checkbox" checked/><label for="sameleague">同联赛内比较</label>
		</div>
		<select id="type" class="selectpicker" data-style="btn-warning">
			<option value="bd">北单</option>
			<option value="jc">竞彩</option>
		</select>
		<select id="sort" class="selectpicker" data-style="btn-warning">
			<option value="asc">升序</option>
			<option value="desc">降序</option>
		</select>
		<select id="threshold" class="selectpicker" data-style="btn-warning">
			<option value="0.01">0.01</option>
			<option value="0.02">0.02</option>
			<option value="0.03">0.03</option>
			<option value="0.04">0.04</option>
			<option value="0.05" selected>0.05</option>
			<option value="0.08">0.08</option>
			<option value="0.10">0.10</option>
			<option value="0.15">0.15</option>
			<option value="0.20">0.20</option>
			<option value="0.25">0.25</option>
			<option value="0.30">0.30</option>
		</select>
		<input type="button" class="btn btn-info" id="btnConfigure" value="排 序"/>
		<input type="button" class="btn btn-info" id="btnRefresh" value="重 置"/>		
	</div>
	
	<div id="main" class="main_wrapper">
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true" data-show-refresh="true">
			<thead id="tableHeader">
			</thead>
			<tbody id="tableContent">
			</tbody>
		</table>
	</div>
</div>

<script type="text/javascript" src="../content/scripts/soccer/association.js"></script>
<script type="text/javascript">

var matchURL = "../soccerdata/getMatchItems";
var settingURL = "../soccerdata/getCorpSetting";
var splashChar = '_';

var assClasses = ['association_red', 'association_thin', 'association_cyan', 'association_blue', 'association_orange'];

//系统参数
var issue = '<%=issue%>';
var sid = '<%=sid%>';

//系统配置
var refresh = false;
//var threshold = 0.05;
//var showOrdinary = true;
//var sameLeague = true;

//基础数据
var matchDoc = null;
var corpSetting = null;

//用于获得配置数据
function getSetting(sid)
{
	$.ajax({
		type: "GET",
		url: settingURL,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"sid" : sid
		},
		jsonp:'callback',
        success : function (msg)
        {
        	if(msg.status == '200')
        	{
        		//setting = msg.data;
        		corpSetting = new CorpSetting(msg.data);
        		getData(issue, refresh);
        	}
        	else
        	{
        		layer.msg("获取配置时出现错误");
        	}
        },
        error:function(){
			layer.msg("错误");
		}
    });
}

//数据下载
function getData(issue, refresh)
{
	$.ajax({
		type: "GET",
		url: matchURL,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"issue" : issue,
			"refresh" : refresh,
			"type": "bd"
		},
		jsonp:'callback',
        success : function (msg)
        {
        	if(msg.status != '200')
        	{
        		layer.msg('获取数据时出现错误');
        		return;
        	}
        	if(!$.isNullOrEmpty(msg.data))
        	{
				matchDoc = new MatchDoc(msg.data);
				//initTableHeader(corpSetting, showOrdinary);
				//setTableData(matchDoc, corpSetting, showOrdinary);
				showMatchData(true);
        	}
        },
        error:function(){
			layer.msg("错误");
		}
    });		
};

//显示数据
function showMatchData(initHeader)
{
	if($.isNullOrEmpty(matchDoc) || matchDoc.size <= 0 )
	{
		layer.msg('比赛数据为空');
		return;
	}
	
	if($.isNullOrEmpty(corpSetting) || $.isNullOrEmpty(corpSetting.getCorps()))
	{
		layer.msg('没有设置博彩公司');
		return;
	}
	
	//获得页面的配置数据
	var conf = getConfValue();
	if($.isNullOrEmpty(initHeader))
	{
		initHeader = false;
	}
	if(initHeader)
	{
		initTableHeader(corpSetting, conf.showOrdinary);
	}
	
	showTableContent(matchDoc, corpSetting, conf);
}

//初始化表格头部
function initTableHeader(setting, showOrdinary)
{
	var corpnum = setting.getSize();
	var header = [];
	//var corps = setting.getCorps();
	var opcorps = setting.getOpCorps();
	var opcorplen = opcorps.length;
	var ypcorps = setting.getYpCorps();
	var ypcorplen = ypcorps.length;
	
	
	//表格头部第一行、各列的定义、博彩公司的名称等
	header.push('<tr>');	
	if(showOrdinary)
	{
		header.push('<th rowspan="3" class="baseinfo">序号</th>')
	}
	
	header.push('<th rowspan="2" class="baseinfo">编号</th>');
	header.push('<th rowspan="2" class="baseinfo">联赛</th>');
	header.push('<th rowspan="3" class="matchname">比赛</th>');
	header.push('<th rowspan="3" class="baseinfo">时间</th>');
	
	//初始化欧赔表格头部数据
	for(var i = 0; i < opcorplen; i ++)
	{
		var corp = opcorps[i];
		header.push('<th colspan="3">' + corp.name + '</th>');
	}
	
	//初始化表格头部数据
	for(var i = 0; i < ypcorplen; i ++)
	{
		var corp = ypcorps[i];
		header.push('<th colspan="3">' + corp.name + '</th>');
	}
	header.push('<th rowspan="3">备注说明</th>')
	header.push('</tr>');
	
	// 表格头部第二行，赔率数据"胜、平、负"等
	header.push('<tr>')	
	if(!$.isNullOrEmpty(opcorps))
	{		
		for(var i = 0; i < opcorplen; i ++)
		{
			var corp = opcorps[i];
			header.push('<th class="oddsvalue">胜</th>');
			header.push('<th class="oddsvalue">平</th>');
			header.push('<th class="oddsvalue">负</th>');
		}		
	}

	if(!$.isNullOrEmpty(ypcorps))
	{
		for(var i = 0; i < ypcorplen; i ++)
		{
			var corp = ypcorps[i];
			header.push('<th rowspan="2" class="oddsvalue">胜</th>');
			header.push('<th rowspan="2" class="oddsvalue">让球</th>');
			header.push('<th rowspan="2" class="oddsvalue">负</th>');
		}
	}
	header.push('</tr>')
	
	//表格头部第三行，主要用于排序等操作
	header.push('<tr>')
	header.push('<th class="inputlabel"><input id="ordinary" value="ordinary" class="radio-primary inputdata" type="radio" name="field" checked/><label for="ordinary">排序</label></th>')
	header.push('<th class="inputlabel"><input id="league" value="league" class="radio-primary inputdata" type="radio" name="field"/><label for="league">排序</label></th>')
	for(var i = 0; i < opcorplen; i ++)
	{
		var corp = opcorps[i];
		header.push(getSortColumn(corp.gid, 0));
		header.push(getSortColumn(corp.gid, 1));
		header.push(getSortColumn(corp.gid, 2));
	}
	header.push('</tr>')
	
	$('#tableHeader').html(header.join(''));
}

function showTableContent(matchDoc, setting, conf)
{
	matchDoc.createMatchRelates(setting, conf.threshold, conf.sameLeague);
	var dataList = matchDoc.getSortList(conf.field, conf.sort, conf.gid, conf.index);
	if($.isNullOrEmpty(dataList))
	{
		return;
	}
	
	var opcorps = setting.getOpCorps();
	var opcorplen = opcorps.length;
	var ypcorps = setting.getYpCorps();
	var ypcorplen = ypcorps.length;
	
	var text = [];
	var rownum = dataList.length;
	for(var i = 0; i < rownum; i ++)
	{
		var match = dataList[i];
		
		text.push('<tr>');		
		if(conf.showOrdinary)
		{
			text.push('<td>' + (i + 1) + '</td>');
		}
		
		text.push('<td>' + match.ordinary + '</td>');
		text.push('<td>' + match.leaguename + '</td>');
		text.push('<td>' + match.homename + ' vs ' + match.clientname + '</td>');
		
		var t = formatDate(match.matchtime, 'hh:mm');
		text.push('<td>' + t + '</td>');
		
		for(var j = 0; j < opcorplen; j ++)
		{
			var corp = opcorps[j];			
			var odds = getOpOdds(match, corp.gid);
			if($.isNullOrEmpty(odds))
			{
				//设置空的赔率数据
				text.push(getEmptyOddsColumn());
			}
			else
			{
				var vals = odds.values;
				
				for(var k = 0; k < 3; k ++)
				{
					var relateIndex = matchDoc.getMatchRelationIndex(match, corp, k);
					var relateClass = getRelationClass(relateIndex);
					text.push('<td align="center"><div class="association ' + relateClass + '">' + formatValue(vals[k]) + '</div></td>');
				}
			}
		}
		
		for(var j = 0; j < ypcorplen; j ++)
		{
			var corp = ypcorps[j];			
			var odds = getYpOdds(match, corp.gid);
			if($.isNullOrEmpty(odds))
			{
				//设置空的赔率数据
				text.push(getEmptyOddsColumn());
			}
			else
			{
				var vals = odds.values;
				text.push('<td>' + formatValue(vals[0]) + '</td>');
				text.push('<td>' + odds.handicap + '</td>');
				text.push('<td>' + formatValue(vals[1]) + '</td>');
			}
		}
		
		text.push('<td class="description"> </td>')
		text.push('</tr>');
	}
	
	$('#tableContent').html(text.join(''));
}

// 按照数据和设置生成数据表格
// matchDoc： 数据
// setting: 设置
function setTableData(matchDoc, setting, showOrdinary)
{
	if($.isNullOrEmpty(matchDoc) || matchDoc.size <= 0 )
	{
		layer.msg('比赛数据为空');
		return;
	}
	
	if($.isNullOrEmpty(setting) || $.isNullOrEmpty(setting.getCorps()))
	{
		layer.msg('没有设置博彩公司');
		return;
	}
	
	//建立关联关系
	//matchDoc.createRelates(setting, threshold, sameLeague);
		
	var opcorps = setting.getOpCorps();
	var opcorplen = opcorps.length;
	var ypcorps = setting.getYpCorps();
	var ypcorplen = ypcorps.length;
	
	var text = [];
	var rownum = matchDoc.size;
	for(var i = 0; i < rownum; i ++)
	{
		var match = matchDoc.getMatchData(i);
		
		text.push('<tr>');		
		if(showOrdinary)
		{
			text.push('<td>' + (i + 1) + '</td>');
		}
		
		text.push('<td>' + match.ordinary + '</td>');
		text.push('<td>' + match.leaguename + '</td>');
		text.push('<td>' + match.homename + ' vs ' + match.clientname + '</td>');
		
		var t = formatDate(match.matchtime, 'hh:mm');
		text.push('<td>' + t + '</td>');
		
		for(var j = 0; j < opcorplen; j ++)
		{
			var corp = opcorps[j];			
			var odds = getOpOdds(match, corp.gid);
			if($.isNullOrEmpty(odds))
			{
				//设置空的赔率数据
				text.push(getEmptyOddsColumn());
			}
			else
			{
				var vals = odds.values;
				
				for(var k = 0; k < 3; k ++)
				{
					if((k != 1) && matchDoc.hasRelation(match, vals[k], k, corp.gid, threshold, sameLeague))
					{
						text.push('<td align="center"><div class="association association_blue">' + formatValue(vals[k]) + '</div></td>');
					}
					else
					{
						text.push('<td>' + formatValue(vals[k]) + '</td>');
					}
				}
			}
		}
		
		for(var j = 0; j < ypcorplen; j ++)
		{
			var corp = ypcorps[j];			
			var odds = getYpOdds(match, corp.gid);
			if($.isNullOrEmpty(odds))
			{
				//设置空的赔率数据
				text.push(getEmptyOddsColumn());
			}
			else
			{
				var vals = odds.values;
				text.push('<td>' + formatValue(vals[0]) + '</td>');
				text.push('<td>' + odds.handicap + '</td>');
				text.push('<td>' + formatValue(vals[1]) + '</td>');
			}
		}
		
		text.push('<td class="description"> </td>')
		text.push('</tr>');
	}
	
	$('#tableContent').html(text.join(''));
};

//数据格式化
function formatValue(value)
{
	return value.toFixed(2);
}

//取得关系的序数
function getRelationClass(index)
{
	if(index < 0)
	{
		return '';
	}
	if(index >= 5)
	{
		index = index % 5;
	}
	return assClasses[index];
}

//设置欧赔赔率的排序配置表
function getSortColumn(value, index)
{
	var id = value + splashChar + index;
	return '<th class="inputlabel"><input id="' + id + '" type="radio" name="odds" value="'
		+ id + '"/><label for="' + id + '">排序</label>'
}

//空表
function getEmptyOddsColumn()
{
	return '<td class="oddsvalue">无</td><td class="oddsvalue">无</td><td class="oddsvalue">无</td>';
}

//获得页面的配置信息
function getConfValue()
{
	var gid = null, index = null;
	var field = $('input[name="field"]:checked').val();
	var odds = $('input[name="odds"]:checked').val();
	if(!$.isNullOrEmpty(odds))
	{
		var w = odds.split(splashChar);
		gid = w[0];
		index = w[1];
	}
	var sort = $('#sort').val();
	var threshold = $('#threshold').val();
	var sameleague = $('#sameleague').prop('checked');
	var showOrdinary = $('#showordinary').prop('checked');
	
	return {
		field: field,
		gid: gid, 
		index: index,
		sort: "desc" == sort ? false : true,
		threshold: threshold,
		sameLeague: sameleague,
		showOrdinary: showOrdinary 
	};
}

function initConfValue()
{
	$('input[name="field"]').prop('checked', false);
	$('input[value="ordinary"]').prop('checked', true);
	$('input[name="odds"]').prop('checked', false);
	showMatchData(false);
}

//页面初始化
$(document).ready(function()
{
	getSetting(sid);
	$('#sort').selectpicker({
		width: 120
	});
	$('#type').selectpicker({
		width: 120
	});
	$('#threshold').selectpicker({
		width: 120
	});
	
	//刷新数据
	$('#btnRefresh').bind('click', function(){
		initConfValue();
		/*var size = matchDoc.size;
		var match = matchDoc.getMatchData(0);
		
		var corpid = '80';
		var matches = matchDoc.getRelateMatchData(match, corpid, threshold, true);		
		layer.msg('比赛： ' + match.mid + '有' + matches.length + '场相关联的比赛');	*/
	});
	
	$('#btnConfigure').bind('click', function(){
		showMatchData(false);
	});
});
</script>
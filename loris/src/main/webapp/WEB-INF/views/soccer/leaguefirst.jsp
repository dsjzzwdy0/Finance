<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.bean.data.table.league.League"%>
<%@page import="com.loris.soccer.bean.data.table.league.Round"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%
	League league = (League)request.getAttribute("league");
	Round round = (Round)request.getAttribute("round");

	String sid = request.getParameter("sid");		//配置编号
	String lid = (league == null) ? "" : league.getLid();
	String season = round.getSeason();
	String rid = round.getRid();

	if(StringUtils.isEmpty(sid))
	{
		sid = "0cb50f8e-9118-4358-bf2c-2ae44fa4a902";
	}
%>
<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<<<<<<< HEAD
<link rel="stylesheet" type="text/css" href="../content/scripts/soccer/soccer-table.css" />
<script type="text/javascript" src="../content/scripts/soccer/soccer-table.js"></script>
=======
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
<style>
.main_wrapper .gridTable th
{
	background-color: #008B8B;
	font-weight: 700;
	color: #fff;
	white-space: nowrap;
}
</style>

<div id="content" class="container_wrapper">
	<%@include file="./league/leaguetoolbar.jsp"%>
	
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

<<<<<<< HEAD
<script type="text/javascript">

var url = "../soccerdata/getRoundMatchRanks";
=======
<script type="text/javascript" src="../content/scripts/soccer/anarel.js"></script>
<script type="text/javascript">

var url = "../soccerdata/getRoundMatchRanks";
var splashChar = '_';

var assClasses = ['association_red', 'association_thin', 'association_cyan', 'association_blue', 'association_orange'];
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855

//系统参数
var sid = '<%=sid%>';
var lid = '<%=lid%>';
var season = '<%=season%>'
var round = '<%=rid%>';
<<<<<<< HEAD

//基础数据
var table = null;

var options = { 
	refresh: false,	
	sorter: null,
	relator: null,
	rows: null,
	columns: null,
	setting: null,
	first: true,
	clear: function()
	{
		this.columns = null;
		this.rows = null;
		this.setting = null;
	}
};

//用于获得配置数据
function createLeagueMatchOddsTable(conf)
{
	sid = conf.sid;
	var relator = new Relator(conf.threshold, conf.sameLeague, false);
	var sorter = new MatchOddsFieldSorter('ordinary', true);
	var source = {
=======
//var type = '';

//基础数据
var matchDoc = null;
var corpSetting = null;

//用于获得配置数据
function getIssueMatchOdds()
{	
	sid = $("#settingSel").val();
	$.ajax({
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"sid": sid,
			"lid": lid,
			"season": season,
			"round": round,
		},
		jsonp:'callback',
<<<<<<< HEAD
		success: null,
		error: null,
		presuccess: function(json, soccerTable)
		{
			if ($.isNotNullOrEmpty(json.data.setting)) {
				var corpSetting = new CorpSetting(json.data.setting);
				soccerTable.options.setting = corpSetting;
				soccerTable.options.columns = new SoccerTableColumns().createCorpSettingColumns(corpSetting);
			}
			if ($.isNotNullOrEmpty(json.data.matches)) {
				soccerTable.options.rows = json.data.matches;
			}
		}
	}	
	options.source = source;
	options.relator = relator;
	options.sorter = sorter;
	table = new SoccerTable(options);
	$('#gridTable').soccerTable(table);
}

function stateChange(state, source, conf)
{
	options.first = conf.first;
	if($.isNotNullOrEmpty(options.relator))
	{
		options.relator.threshold = conf.threshold;
		options.relator.sameLeague = conf.sameLeague;
	}
	
	if($(source).attr('id') == 'settingSel')
	{
		options.clear();
		createLeagueMatchOddsTable(conf);
	}
	else
	{
		table.update();
	}
}

$(document).ready(function() {
	showNewToolBar();
	showSettingSel();
	
=======
        success : function (msg)
        {
        	if(msg.status == '200')
        	{
        		corpSetting = new CorpSetting(msg.data.setting);
        		corpSetting.addTheoryCorp();
        		matchDoc = new MatchDoc(msg.data.matches);
        		showMatchData(true);
        	}
        	else
        	{
        		layer.msg("获取数据时出现错误,请重新试用");
        	}
        },
        error:function(){
			layer.msg("错误");
		}
    });
}

//显示数据
//initHeader: 是否重新显示头部信息
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
	if(initHeader == true)
	{
		//初始化控制面板
		initLeaguePanel(matchDoc.list);
		initTableHeader(corpSetting, conf.showOrdinary);
	}
	showTableContent(matchDoc, corpSetting, conf);
}

//初始化表格头部
function initTableHeader(setting, showOrdinary)
{
	var corpnum = setting.getSize();
	var header = [];
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
			header.push('<th rowspan="2" class="oddsvalue">盘</th>');
			header.push('<th rowspan="2" class="oddsvalue">负</th>');
		}
	}
	header.push('</tr>')
	
	//表格头部第三行，主要用于排序等操作
	header.push('<tr>')
	header.push('<th class="inputlabel"><input id="ordinary" value="ordinary" class="radio-primary inputdata" type="radio" name="field" checked/><label for="ordinary"></label></th>')
	header.push('<th class="inputlabel"><input id="league" value="league" class="radio-primary inputdata" type="radio" name="field"/><label for="league"></label></th>')
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

function filterDataList(dataList)
{
	var oLeagueList = $('#leagueList input');
	var lids = [];
	oLeagueList.each(function(){
        if($(this).prop("checked"))
        {
        	lids.push($(this).val());
        }
    });
	

	var list = [];
	if(lids.length == 0)
	{
		return list;
	}
	var lidsize = lids.length;
	var len = dataList.length;
	for(var i = 0; i < len; i ++)
	{
		var lid = dataList[i].lid;
		for(var j = 0; j < lidsize; j ++)
		{
			if(lid == lids[j])
			{
				list.push(dataList[i]);
				break;
			}
		}
	}
	return list;
}

function showTableContent(matchDoc, setting, conf)
{	
	var doc = matchDoc;
	doc.createMatchRelates(setting, conf.threshold, conf.sameLeague);
	var dataList = doc.getSortList(conf.field, conf.sort, conf.gid, conf.index);
	
	var total = matchDoc.size;
	var shownum = dataList.length;
	
	$('#matchNumAll').text(total);
	$('#matchNumHide').text(total - shownum);
	
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
		text.push('<td>' + formatLeagueInfo(match) + '</td>');
		text.push('<td>' + formatMatchTeamInfo(match) + '</td>');
		
		var t = formatDate(match.matchtime, 'hh:mm');
		var title = '比赛时间：' + match.matchtime + ',投注结束时间：' + match.closetime;
		text.push('<td title="' + title + '">' + t + '</td>');
		
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
				text.push(formatOpValueColumn(doc, match, odds, corp, conf.first));
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
				text.push(formatYpValueColumn(match, odds, corp, conf.first))
			}
		}
		
		text.push('<td class="description"> </td>')
		text.push('</tr>');
	}
	
	$('#tableContent').html(text.join(''));
}

//格式化欧赔数据栏目
function formatOpValueColumn(doc, match, op, corp, first)
{
	var st = first ? 0 : 3;
	var idx = getOpMaxProbIndex(op, first);
	var r = doc.getMatchRelation(match, corp, first);
	var relateIndex = doc.getMatchRelationIndex(match, corp, first);
	var relateClass = relateIndex >= 0 ? getRelationClass(relateIndex) : '';
	
	var text = [];
	var vals = op.values;
	for(var k = st; k < st + 3; k ++)
	{
		text.push('<td align="center"><div class="association ' + ((idx == k) ? relateClass : '')
			+ '">' + formatValue(vals[k]) + '</div></td>');
	}
	return text.join('');
}

//格式化亚盘数据
function formatYpValueColumn(match, yp, corp, first)
{
	var st = first ? 0 : 3;
	var text = [];
	var vals = yp.values;
	text.push('<td>' + formatValue(vals[st]) + '</td>');
	text.push('<td>' + getHandicapName(vals[st + 1]) + '</td>');
	text.push('<td>' + formatValue(vals[st + 2]) + '</td>');
	return text.join('');
}

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
		+ id + '"/><label for="' + id + '"></label>'
}

//获得页面的配置信息
function getConfValue()
{
	var gid = null, index = null;
	var field = $('input[name="field"]:checked').val();
	var odds = $('input[name="odds"]:checked').val();
	var type = $('#oddsType').val();

	if(!$.isNullOrEmpty(odds))
	{
		var w = odds.split(splashChar);
		gid = w[0];
		index = w[1];
	}
	var sort = $('#sort').val();
	var threshold = Number($('#threshold').val());
	var sameleague = $('#sameLeague').prop('checked');
	var showOrdinary = $('#showOrdinary').prop('checked');
	
	if($.isNullOrEmpty(threshold))
	{
		threshold = 0.05;
	}
	
	return {
		field: field,
		gid: gid, 
		index: index,
		first: type == 'now' ? false : true,
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

function openPage(type)
{
	var destUrl = 'analeague?type=' + type+ '&lid=' + lid + '&season=' + season + '&rid=' + round;
	window.open(destUrl);
}

//页面初始化
$(document).ready(function()
{
	$('#settingSel').show();
	$('.newToolBar').show();
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
	if(!$.isNullOrEmpty(sid))
	{
		$('#settingSel').val(sid);
	}
<<<<<<< HEAD
	var conf = getConfValue();
	createLeagueMatchOddsTable(conf);	
	stateListeners.add(stateChange);
	
	$('#btnRefresh').on('click', function(){
		$('#gridTable').soccerTable('destroy');
	});

	$('#hideChosen').on('click', function(){
		//layer.msg('Recovery');
		sorter.asc = !sorter.asc;
		table.options.sorter = sorter;
		$('#gridTable').soccerTable(table);
	});
});

=======
	getIssueMatchOdds();
	
	$(document).on('click','.game_select',function(){
		$(this).children('.pl-wind-ss').show();
		$(this).find('ul').show();
	}).on('mouseleave','.game_select',function(){
		$(this).children('.pl-wind-ss').hide();
		$(this).find('ul').hide();
	});
	
	$(".sx_form_b input").bind("click", function(){
        var oMatchList = $("#leagueList input");
        switch($(this).val()){
            case "全选":
                oMatchList.prop("checked", true);
                break;
            case "反选":
                oMatchList.each(function(){
                    $(this).prop("checked") ?  $(this).prop("checked", false) : $(this).prop("checked", true);
                })
                break;
            case "全不选":
                oMatchList.prop("checked", false);
                break;
            default: ;
        }
        showMatchData(false);
    })
	
	//刷新数据
	$('#btnRefresh').bind('click', function(){
		getIssueMatchOdds();
	});
	$('#settingSel').on('change',function(){
		getIssueMatchOdds();
	});
	$('#issueSel').on("change", function(){
    	issue = '';
    	getIssueMatchOdds();
    });
	$('#typeSel').on('change',function(){
		getIssueMatchOdds();
	});
	
	$('#btnConfigure').bind('click', function(){
		showMatchData(false);
	});
	
	$('#leagueList').on("click", "input", function(){
		showMatchData(false);
    });	
	
	$('#gridTable').on('click', 'input', function(){
		showMatchData(false);
	});

	$('#oddsType').on('change', function(){
		showMatchData(false);
	});
	$('#sort').on('change', function(){
		showMatchData(false);
	});
	
	$('#threshold').on('change', function(){
		showMatchData(false);
	});
	$('#sameLeague').on('change', function(){
		showMatchData(false);
	});	
	$('#hideChosen').bind('click', function(){
		 var oLeagueList = $("#leagueList input");
		 oLeagueList.each(function(){
			 $(this).prop('checked', true);
		 });
		 showMatchData(false);
	});	
});
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
</script>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.bean.data.table.League"%>
<%@page import="com.loris.soccer.bean.data.table.Round"%>
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
		sid = "";
	}
%>
<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<link rel="stylesheet" type="text/css" href="../content/scripts/soccer/soccer-table.css" />
<script type="text/javascript" src="../content/scripts/soccer/soccer-table.js"></script>

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


<script type="text/javascript">

var url = "../soccerdata/getRoundMatchesOdds";
//系统参数
var sid = '<%=sid%>';
var lid = '<%=lid%>';
var season = '<%=season%>'
var round = '<%=rid%>';

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
	
	if(!$.isNullOrEmpty(sid))
	{
		$('#settingSel').val(sid);
	}
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
</script>
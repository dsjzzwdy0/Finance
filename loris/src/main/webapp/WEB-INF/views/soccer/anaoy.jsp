<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%
    String issue = request.getParameter("issue");
	String type = request.getParameter("type");
	String sid = request.getParameter("sid");
	
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
	if(StringUtils.isEmpty(type))
	{
		type = "bd";
	}
	if(StringUtils.isEmpty(sid))
	{
		sid = "dbf4bb31-4024-4c2c-a7d7-73e0049d45c2";
	}
%>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<link rel="stylesheet" type="text/css" href="../content/scripts/soccer/soccer-table.css" />
<script type="text/javascript" src="../content/scripts/soccer/soccer-table.js"></script>

<div id="content" class="container_wrapper">
	<%@include file="./analysis/anatoolbar.jsp"%>

	<div id="main" class="main_wrapper">		
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true"
			data-show-refresh="true">
		</table>
	</div>
</div>

<script type="text/javascript">
var url = "../soccerdata/getMatchItems";

//系统参数
var issue = "<%=issue%>";
var type = "<%=type%>";
var sid = "<%=sid%>"

//基础数据
var table = null;

var options = { 
	refresh: false,	
	sorter: null,
	relator: null,
	rows: null,
	results: null,
	columns: null,
	setting: null,
	first: true,
	filter: null,
	clear: function()
	{
		this.columns = null;
		this.rows = null;
		this.setting = null;
	},
	postshow: function()
	{
		var total = 0;
		var shownum = 0;
		if($.isNotNullOrEmpty(this.rows))
		{
			total = this.rows.length;
		}
		if($.isNotNullOrEmpty(this.results))
		{
			shownum = this.results.length;
		}
		$('#matchNumAll').text(total);
		$('#matchNumHide').text(total - shownum);
	}
};

//用于获得配置数据
function createMatchOddsTable(conf)
{
	sid = conf.sid;
	issue = conf.issue;
	type = conf.type;
	var relator = new Relator(conf.threshold, conf.sameLeague, false);
	var sorter = new MatchOddsFieldSorter('ordinary', true);
	var source = {
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"sid": sid,
			"issue": issue,
			"type": type,
		},
		jsonp:'callback',
		success: null,
		error: null,
		presuccess: function(json, soccerTable)
		{
			if ($.isNotNullOrEmpty(json.data.setting)) {
				var corpSetting = new CorpSetting(json.data.setting);
				soccerTable.options.setting = corpSetting;
				soccerTable.options.columns = new SoccerTableColumns().createPerformColumns(corpSetting);
			}
			if ($.isNotNullOrEmpty(json.data.matches)) {
				soccerTable.options.rows = json.data.matches;
				initLeaguePanel(json.data.matches);
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
	var sourceId = $(source).attr('id');
	if(sourceId == 'settingSel' ||
			sourceId == 'typeSel' ||
			sourceId == 'issueSel')
	{
		options.clear();
		createMatchOddsTable(conf);
	}
	else
	{		
		var filter = options.filter;
		if($.isNullOrEmpty(filter))
		{
			filter = new FieldFilter('lid', [], true);
			options.filter = filter;
		}
		filter.clear();
		if($.isNotNullOrEmpty(conf.lids))
		{
			filter.setValues(conf.lids);
		}		
		table.update();
	}
}

$(document).ready(function() {
	showNewToolBar();
	showSettingSel();
	
	if($.isNotNullOrEmpty(sid))
	{
		$('#settingSel').val(sid);
	}
	if($.isNotNullOrEmpty(issue))
	{
		$('#issueSel').val(issue);
	}

	var conf = getConfValue();
	createMatchOddsTable(conf);	
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
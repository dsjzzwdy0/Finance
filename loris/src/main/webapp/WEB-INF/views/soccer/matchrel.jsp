<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.bean.table.League"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%
	String mids = request.getParameter("mids");
	String sid = request.getParameter("sid");		//配置编号

	if(StringUtils.isEmpty(sid))
	{
		sid = "d774110d-8fbc-4284-bb07-1a98f2cfdc0d";
	}

%>
<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<link rel="stylesheet" type="text/css" href="../content/scripts/soccer/soccer-table.css" />
<script type="text/javascript" src="../content/scripts/soccer/soccer-table.js"></script>

<div id="content" class="container_wrapper">
	<%@include file="./analysis/anatoolbar.jsp"%>
	
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

var url = "../soccerdata/getRelationMatchesOdds";
//系统参数
var mids = "<%=mids%>";
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
	firstWithLast: true,
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
			"mids": mids,
		},
		jsonp:'callback',
		success: null,
		error: null,
		presuccess: function(json, soccerTable)
		{
			if($.isNullOrEmpty(json.data))
			{
				return;
			}
			if ($.isNotNullOrEmpty(json.data.setting)) {
				var corpSetting = new CorpSetting(json.data.setting);
				soccerTable.options.setting = corpSetting;
				soccerTable.options.columns = new SoccerTableColumns().createCorpSettingColumns(corpSetting);
			}
			if ($.isNotNullOrEmpty(json.data.matches)) {
				soccerTable.options.rows = json.data.matches;
				initLeaguePanel(json.data.matches);
			}
		},
		complete: function(){
			//$('#gridTable tbody').on('click', '.relation', function(){
			//	getRelatedMatch($(this));
			//});
		}
	}	
	options.source = source;
	options.relator = relator;
	options.sorter = sorter;
	table = new SoccerTable(options);
	$('#gridTable').soccerTable(table);
}

//获得比赛的数据
function getRelatedMatch(element)
{
	var mid = $(element).attr('mid');
	var gid = $(element).attr('gid');
	var index = $(element).attr('index');
	var val = $(element).text();
	
	var mids = [];
	mids.push(mid);
	$(element).parent().parent().siblings().each(function()
	{
		var div = $(this).find('div[gid="' + gid + '"]');
		if($.isNullOrEmpty(div))
		{
			return;
		}
		if(div.length > 0)
		{
			var len = div.length;
			for(var i = 0; i < len; i ++)
			{
				var mid = $(div[i]).attr('mid');
				var idx = $(div[i]).attr('index');
				if(idx == index)
					mids.push(mid);
			}
			
		}	
	});
	
	//layer.msg(mid + ': ' + gid + ', ' + val + ': ' + mids.join(';'));
	window.open('../soccer/ralation?mids=' + mids.join(','));
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
	$('#issueSel').hide();
	$('#typeSel').hide();
	$('#sameLeague').attr('checked', false);
	$('.top-chosse #settingSel').attr('disabled', 'disabled');
	
	if($.isNotNullOrEmpty(sid))
	{
		$('#settingSel').val(sid);
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
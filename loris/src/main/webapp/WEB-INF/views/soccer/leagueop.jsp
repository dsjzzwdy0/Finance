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
<link rel="stylesheet" type="text/css" href="../content/scripts/soccer/soccer-table.css" />
<<<<<<< HEAD
<script type="text/javascript" src="../content/scripts/soccer/soccer-table.js"></script>
=======
<style>
.main_wrapper .gridTable th
{
	background-color: #008B8B;
	font-weight: 700;
	color: #fff;
	white-space: nowrap;
}

.gridTable thead th .sortable {
    cursor: pointer;
    background-position: right;
    background-repeat: no-repeat;
    padding-right: 30px;
}

.gridTable thead th .both {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAQAAADYWf5HAAAAkElEQVQoz7X QMQ5AQBCF4dWQSJxC5wwax1Cq1e7BAdxD5SL+Tq/QCM1oNiJidwox0355mXnG/DrEtIQ6azioNZQxI0ykPhTQIwhCR+BmBYtlK7kLJYwWCcJA9M4qdrZrd8pPjZWPtOqdRQy320YSV17OatFC4euts6z39GYMKRPCTKY9UnPQ6P+GtMRfGtPnBCiqhAeJPmkqAAAAAElFTkSuQmCC');
}

.gridTable thead th .desc {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAYAAAByUDbMAAAAZUlEQVQ4y2NgGAWjYBSggaqGu5FA/BOIv2PBIPFEUgxjB+IdQPwfC94HxLykus4GiD+hGfQOiB3J8SojEE9EM2wuSJzcsFMG4ttQgx4DsRalkZENxL+AuJQaMcsGxBOAmGvopk8AVz1sLZgg0bsAAAAASUVORK5CYII= ');
}

.gridTable thead th .asc {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAYAAAByUDbMAAAAZ0lEQVQ4y2NgGLKgquEuFxBPAGI2ahhWCsS/gDibUoO0gPgxEP8H4ttArEyuQYxAPBdqEAxPBImTY5gjEL9DM+wTENuQahAvEO9DMwiGdwAxOymGJQLxTyD+jgWDxCMZRsEoGAVoAADeemwtPcZI2wAAAABJRU5ErkJggg==');
}

/*
.gridTable thead .sortable
{
	cursor: pointer;
	height: 28px;
	line-height: 28px;	
}
.gridTable thead .both
{
	display: inline-block;
	margin-left: 5px;
	text-decoration: none;
	width: 15px;
	height: 16px;
	background-position: left -40px;
	background: url(../content/images/updown.png) no-repeat;
}*/
</style>
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855

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
=======
<script type="text/javascript" src="../content/scripts/soccer/soccer.js"></script>
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
<script type="text/javascript">

var url = "../soccerdata/getRoundMatchesOdds";

//系统参数
var sid = '<%=sid%>';
var lid = '<%=lid%>';
var season = '<%=season%>'
var round = '<%=rid%>';

//基础数据
var table = null;
<<<<<<< HEAD

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
var relator = new Relator(0.03, true, true, false);

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
	jsonp:'callback'
}

var options = { 
	refresh: false,	
	rows: msg.data.matches,
	sorter: sorter,
	relator: relator,
	setting: corpSetting,
	columns: corpSetting.createColumns()
};


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
	
	if(!$.isNullOrEmpty(sid))
	{
		$('#settingSel').val(sid);
	}
	var conf = getConfValue();
	createLeagueMatchOddsTable(conf);	
	stateListeners.add(stateChange);
	
	$('#btnRefresh').on('click', function(){
=======
        success : function (msg)
        {
        	if(msg.status == '200')
        	{
        		corpSetting = new CorpSetting(msg.data.setting); 

        		var sorter = new FieldSorter(['ordinary'], true);
        		//matchDoc = new MatchDoc(msg.data.matches);
        		//选项数据
        		var options = { 
        			refresh: false,	
        			rows: msg.data.matches,
        			sorter: sorter,
        			relator: new Relator(0.03, true, true, false),
        			setting: corpSetting,
        			columns: corpSetting.createColumns()
        		};
        		
        		table = new SoccerTable(options);
        		$('#gridTable').soccerTable(table);
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

$(document).ready(function() {	
	getIssueMatchOdds();
	$('#btnRefresh').on('click', function(){
		//layer.msg('Test');
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
		$('#gridTable').soccerTable('destroy');
	});

	$('#hideChosen').on('click', function(){
<<<<<<< HEAD
		//layer.msg('Recovery');
=======
		layer.msg('Recovery');
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
		sorter.asc = !sorter.asc;
		table.options.sorter = sorter;
		$('#gridTable').soccerTable(table);
	});
});
</script>
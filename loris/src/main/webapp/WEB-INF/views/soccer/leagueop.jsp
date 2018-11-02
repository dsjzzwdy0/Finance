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

<script type="text/javascript" src="../content/scripts/soccer/soccer.js"></script>
<script type="text/javascript">

var url = "../soccerdata/getRoundMatchesOdds";

//系统参数
var sid = '<%=sid%>';
var lid = '<%=lid%>';
var season = '<%=season%>'
var round = '<%=rid%>';

//基础数据
var table = null;
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
		$('#gridTable').soccerTable('destroy');
	});

	$('#hideChosen').on('click', function(){
		layer.msg('Recovery');
		sorter.asc = !sorter.asc;
		table.options.sorter = sorter;
		$('#gridTable').soccerTable(table);
	});
});
</script>
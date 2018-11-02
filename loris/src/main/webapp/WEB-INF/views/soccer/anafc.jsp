<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<%
    String issue = request.getParameter("issue");
	String type = request.getParameter("type");
	String lid = "";
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
	if(StringUtils.isEmpty(type))
	{
		type = "jc";
	}
%>

<style>
.main_wrapper .gridTable th
{
	background-color: #008B8B;
	font-weight: 700;
	color: #fff;
	white-space: nowrap;
}

thead th .asc {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAYAAAByUDbMAAAAZ0lEQVQ4y2NgGLKgquEuFxBPAGI2ahhWCsS/gDibUoO0gPgxEP8H4ttArEyuQYxAPBdqEAxPBImTY5gjEL9DM+wTENuQahAvEO9DMwiGdwAxOymGJQLxTyD+jgWDxCMZRsEoGAVoAADeemwtPcZI2wAAAABJRU5ErkJggg==');
}

thead th .desc {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAYAAAByUDbMAAAAZUlEQVQ4y2NgGAWjYBSggaqGu5FA/BOIv2PBIPFEUgxjB+IdQPwfC94HxLykus4GiD+hGfQOiB3J8SojEE9EM2wuSJzcsFMG4ttQgx4DsRalkZENxL+AuJQaMcsGxBOAmGvopk8AVz1sLZgg0bsAAAAASUVORK5CYII= ');
}

thead th .both {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAQAAADYWf5HAAAAkElEQVQoz7X QMQ5AQBCF4dWQSJxC5wwax1Cq1e7BAdxD5SL+Tq/QCM1oNiJidwox0355mXnG/DrEtIQ6azioNZQxI0ykPhTQIwhCR+BmBYtlK7kLJYwWCcJA9M4qdrZrd8pPjZWPtOqdRQy320YSV17OatFC4euts6z39GYMKRPCTKY9UnPQ6P+GtMRfGtPnBCiqhAeJPmkqAAAAAElFTkSuQmCC');
}

thead th .sortable {
    cursor: pointer;
    background-position: right;
    background-repeat: no-repeat;
    padding-right: 10px;
}

.gridTable .red
{
	color: red;
}

.gridTable tr:hover .red
{
	color: yellow;
}

.gridTable tr:hover .green
{
	color: blue;
}

.gridTable .green
{
	color: green;
}

</style>

<div id="content" class="container_wrapper">
	<%@include file="./analysis/anatoolbar.jsp"%>

	<div id="main" class="main_wrapper">		
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true"
			data-show-refresh="true">
			<thead>
				<tr>
					<th rowspan="2" class="baseinfo">编号</th>
					<th rowspan="2" class="baseinfo">联赛</th>
					<th rowspan="2" class="matchname">比赛</th>
					<th rowspan="2" class="baseinfo">时间</th>
					<th colspan="9">平均值</th>
					<th colspan="9">方差值</th>
					<th rowspan="2">备注</th>
				</tr>
				<tr>
					<th data-field="firstwin" class="oddsvalue"><div class="sortable both">初胜</div></th>
					<th data-field="firstdraw" class="oddsvalue"><div class="sortable both">初平</div></th>
					<th data-field="firstlose" class="oddsvalue"><div class="sortable both">初负</div></th>
					<th data-field="win" class="oddsvalue"><div class="sortable both">胜</div></th>
					<th data-field="draw" class="oddsvalue"><div class="sortable both">平</div></th>
					<th data-field="lose" class="oddsvalue"><div class="sortable both">负</div></th>
					<th data-field="windiff" class="oddsvalue"><div class="sortable both">胜差</div></th>
					<th data-field="drawdiff" class="oddsvalue"><div class="sortable both">平差</div></th>
					<th data-field="losediff" class="oddsvalue"><div class="sortable both">负差</div></th>
					<th data-field="fwinstd" class="oddsvalue"><div class="sortable both">初胜</div></th>
					<th data-field="fdrawstd" class="oddsvalue"><div class="sortable both">初平</div></th>
					<th data-field="flosestd" class="oddsvalue"><div class="sortable both">初负</div></th>
					<th data-field="winstd" class="oddsvalue"><div class="sortable both">胜</div></th>
					<th data-field="drawstd" class="oddsvalue"><div class="sortable both">平</div></th>
					<th data-field="losestd" class="oddsvalue"><div class="sortable both">负</div></th>
					<th data-field="winstddiff" class="oddsvalue"><div class="sortable both">胜差</div></th>
					<th data-field="drawstddiff" class="oddsvalue"><div class="sortable both">平差</div></th>
					<th data-field="losestddiff" class="oddsvalue"><div class="sortable both">负差</div></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</div>

<script type="text/javascript">
var url = "../soccerdata/getMatchesOpVar";

//系统参数
var issue = '<%=issue%>';
var lid = '<%=lid%>';
var type = '';

//比赛数据
var matchDoc = null;

function getMatchFieldValue(match, field)
{
	var v = 'undefined';
	switch(field){
	case 'firstwin':
		v = match.vars[0].win.avg;
		break;
	case 'firstdraw':
		v = match.vars[0].draw.avg;
		break;
	case 'firstlose':
		v = match.vars[0].lose.avg;
		break;
	case 'win':
		v = match.vars[1].win.avg;
		break;
	case 'draw':
		v = match.vars[1].draw.avg;
		break;
	case 'lose':
		v = match.vars[1].lose.avg;
		break;
	case 'windiff':
		v = match.vars[1].win.avg - match.vars[0].win.avg;
		break;
	case 'drawdiff':
		v = match.vars[1].draw.avg - match.vars[0].draw.avg;
		break;
	case 'losediff':
		v = match.vars[1].lose.avg - match.vars[0].lose.avg;
		break;
	case 'fwinstd':
		v = match.vars[0].win.stderr;
		break;
	case 'fdrawstd':
		v = match.vars[0].draw.stderr;
		break;
	case 'flosestd':
		v = match.vars[0].lose.stderr;
		break;
	case 'winstd':
		v = match.vars[1].win.stderr;
		break;
	case 'drawstd':
		v = match.vars[1].draw.stderr;
		break;
	case 'losestd':
		v = match.vars[1].lose.stderr;
		break;
	case 'winstddiff':
		v = match.vars[1].win.stderr - match.vars[0].win.stderr;
		break;
	case 'drawstddiff':
		v = match.vars[1].draw.stderr - match.vars[0].draw.stderr;
		break;
	case 'losestddiff':
		v = match.vars[1].lose.stderr - match.vars[0].lose.stderr;
		break;
	default:
		break;
	}
	return v;
}

function MatchDoc(list)
{
	this.list = list;
	this.size = list.length;
	
	this.sortList = function(field, asc)
	{
		if($.isNullOrEmpty(field))
		{
			return this.list;
		}
		if($.isNullOrEmpty(asc))
		{
			asc = true;
		}
		
		var matches = [];
		matches = matches.concat(this.list);
		matches.sort(function(a, b){
			var var1 = getMatchFieldValue(a, field);
			var var2 = getMatchFieldValue(b, field);
			var r = 0;
			if($.isNullOrEmpty(var1) || var1 == 'undefined')
			{
				r = -1;
			}
			if($.isNullOrEmpty(var2) || var2 == 'undefined')
			{
				r = 1;
			}
			if(r === 0)
			{
				r = var1 - var2;				
			}
			return asc ? r : -r;
		});
		return matches;
	}
}

//获得比赛的方差数据
function getIssueMatchVars()
{	
	type = $('#typeSel').val();
	if($.isNullOrEmpty(issue)) issue = $('#issueSel').val();
	else $('#issueSel').val(issue);
	
	var params = {
		"issue": issue,
	};
	
	$.ajax({
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"issue": issue,
			"lid": lid
		},
		jsonp:'callback',
        success : function (msg)
        {
        	if(msg.status == '200')
        	{
        		matchDoc = new MatchDoc(msg.data);
        		initLeaguePanel(matchDoc.list);
        		showMatchData();
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

//数据格式化
function formatValue(value)
{
	return value.toFixed(2);
}

function showMatchData()
{
	var conf = getConfValue();
	//var list = matchDoc.sortList('lid', conf.sortField, conf.asc);	
	var list = filterDataList(matchDoc.sortList(conf.sortField, conf.asc));
	var len = list.length;
	
	var total = matchDoc.size;
	var shownum = list.length;
	
	$('#matchNumAll').text(total);
	$('#matchNumHide').text(total - shownum);
	
	var text = [];
	for(var i = 0; i < len; i ++)
	{
		var match = list[i];
		text.push('<tr>');
		text.push('<td>' + match.ordinary + '</td>');
		text.push('<td>' + formatLeagueInfo(match) + '</td>');
		text.push('<td>' + formatMatchTeamInfo(match) + '</td>');
		
		var t = formatDate(match.matchtime, 'hh:mm');
		var title = '比赛时间：' + match.matchtime + ', 投注结束时间：' + match.closetime;
		text.push('<td title="' + title + '">' + t + '</td>');
		
		var var0 = match.vars[0];
		var var1 = match.vars[1];
		text.push('<td title="初赔胜平均">' + formatValue(var0.win.avg) + '</td>');
		text.push('<td title="初赔平平均">' + formatValue(var0.draw.avg) + '</td>');
		text.push('<td title="初赔负平均">' + formatValue(var0.lose.avg) + '</td>');
		
		var clazzname = getValueClassName(var0.win.avg , var1.win.avg);
		text.push('<td title="胜平均" class="'+ clazzname  + '">' + formatValue(var1.win.avg) + '</td>');
		clazzname = getValueClassName(var0.draw.avg , var1.draw.avg);
		text.push('<td title="平平均" class="'+ clazzname  + '">' + formatValue(var1.draw.avg) + '</td>');
		clazzname = getValueClassName(var0.lose.avg , var1.lose.avg);
		text.push('<td title="负平均"class="'+ clazzname  + '">' + formatValue(var1.lose.avg) + '</td>');
		
		clazzname = getValueClassName(var0.win.avg , var1.win.avg);
		text.push('<td title="胜差" class="'+ clazzname  + '">' + formatValue(var1.win.avg - var0.win.avg) + '</td>');
		clazzname = getValueClassName(var0.draw.avg , var1.draw.avg);
		text.push('<td title="平差" class="'+ clazzname  + '">' + formatValue(var1.draw.avg - var0.draw.avg) + '</td>');
		clazzname = getValueClassName(var0.lose.avg , var1.lose.avg);
		text.push('<td title="负差"class="'+ clazzname  + '">' + formatValue(var1.lose.avg - var0.lose.avg) + '</td>');
		
		text.push('<td title="初赔胜平均">' + formatValue(var0.win.stderr) + '</td>');
		text.push('<td title="初赔平平均">' + formatValue(var0.draw.stderr) + '</td>');
		text.push('<td title="初赔负平均">' + formatValue(var0.lose.stderr) + '</td>');
		
		var clazzname = getValueClassName(var0.win.stderr , var1.win.stderr);
		text.push('<td title="胜平均" class="'+ clazzname  + '">' + formatValue(var1.win.stderr) + '</td>');
		clazzname = getValueClassName(var0.draw.avg , var1.draw.avg);
		text.push('<td title="平平均" class="'+ clazzname  + '">' + formatValue(var1.draw.stderr) + '</td>');
		clazzname = getValueClassName(var0.lose.avg , var1.lose.avg);
		text.push('<td title="负平均"class="'+ clazzname  + '">' + formatValue(var1.lose.stderr) + '</td>');
		
		clazzname = getValueClassName(var0.win.stderr , var1.win.stderr);
		text.push('<td title="胜差" class="'+ clazzname  + '">' + formatValue(var1.win.stderr - var0.win.stderr) + '</td>');
		clazzname = getValueClassName(var0.draw.stderr , var1.draw.stderr);
		text.push('<td title="平差" class="'+ clazzname  + '">' + formatValue(var1.draw.stderr - var0.draw.stderr) + '</td>');
		clazzname = getValueClassName(var0.lose.stderr , var1.lose.stderr);
		text.push('<td title="负差"class="'+ clazzname  + '">' + formatValue(var1.lose.stderr - var0.lose.stderr) + '</td>');
		text.push('<td></td>')
		text.push('</tr>');
	}
	$('#gridTable tbody').html(text.join(''));
}

function getValueClassName(first, last)
{
	var clazzname = withinErrorMargin(first, last) ? '' : 
		(first > last) ? 'red' : 'green';
	return clazzname;
}

//页面的配置内容
function getConfValue()
{
	var sortField = '';
	var asc = false;
	
	$("#gridTable thead .sortable").each(function(){
		if($(this).hasClass('desc'))
		{
			asc = false;
			sortField = $(this).parent().attr('data-field');
		}
		else if($(this).hasClass('asc'))
		{
			asc = true;
			sortField = $(this).parent().attr('data-field');
		}
	});
	
	return {
		sortField: sortField,
		asc: asc
	}
}

//过滤数据
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

//页面初始化
$(document).ready(function()
{	
	getIssueMatchVars();
	$(document).on('click','.game_select',function(){
		$(this).children('.pl-wind-ss').show();
		$(this).find('ul').show();
	}).on('mouseleave','.game_select',function(){
		$(this).children('.pl-wind-ss').hide();
		$(this).find('ul').hide();
	});
	
	$('#btnRefresh').click(function(){
		var conf = getConfValue();
		layer.msg('Field: ' + conf.sortField + ', Asc: ' + conf.asc);
	});
	$('#leagueList').on("click", "input", function(){
		showMatchData();
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
        showMatchData();
    })
	$('.gridTable thead').on('click', '.sortable', function(){
		$(this).parent().siblings().find('.sortable').each(function(){
			$(this).removeClass('desc asc');
			$(this).addClass('both');
		})
		if($(this).hasClass('both') || $(this).hasClass('desc'))
		{
			$(this).removeClass('both desc');
			$(this).addClass('asc');
		}
		else
		{
			$(this).removeClass('asc');
			$(this).addClass('desc');
		}
		
		showMatchData();
	});
	$('#hideChosen').bind('click', function(){
		 var oLeagueList = $("#leagueList input");
		 oLeagueList.each(function(){
			 $(this).prop('checked', true);
		 });
		 showMatchData();
	});	
});

/*初始化表格头部信息
function initHeader()
{
	$('#gridTable thead').html('');
	var header = [];
	header.push('<tr>')
	header.push('<th rowspan="2" class="baseinfo">编号</th>');
	header.push('<th rowspan="2" class="baseinfo">联赛</th>');
	header.push('<th rowspan="3" class="matchname">比赛</th>');
	header.push('<th rowspan="3" class="baseinfo">时间</th>');
	header.push('<th class="oddsvalue"><div class="sortable both">初胜</div></th>');
	header.push('<th class="oddsvalue"><div class="sortable both">初平</div></th>');
	header.push('<th class="oddsvalue"><div class="sortable both">初负</div></th>');
	header.push('<th class="oddsvalue"><div class="sortable both">胜</div></th>');
	header.push('<th class="oddsvalue"><div class="sortable both">平</div></th>');
	header.push('<th class="oddsvalue"><div class="sortable both">负</div></th>');
	header.push('</tr>');
	$('#gridTable thead').html(header.join(''));
}*/
</script>
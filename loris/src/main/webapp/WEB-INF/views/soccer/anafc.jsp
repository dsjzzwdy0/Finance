<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>

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
var url = '../soccerdata/getMatchesOpVar';
var issue = '<%=issue%>';
var type = '<%=type%>';

function OpStandardErrorSorter(field, asc, fieldName)
{
	this.field = field;
	this.asc = asc;
	this.element = null;
	this.fieldName = fieldName;

	this.setSorter = function(field, asc, element)
	{
		this.field = field;
		this.asc = asc;
		this.element = element;
		if($.isNotNullOrEmpty(element))
			this.fieldName = element.parent().text();
	}
	
	this.compare = function(a, b)
	{
		if(this.field == 'vars')
		{
			return this.compareVars(a, b);
		}
		else
		{
			r = a > b ? 1 : a < b ? -1 : 0;
			return this.asc ? r : -r;
		}		
	}
	
	this.compareVars = function(a, b)
	{
		var aValue = this.getFieldValue(a);
		var bValue = this.getFieldValue(b);
		
		var r =0;
		if($.isNullOrEmpty(bValue))
		{
			r = 1;
		}
		else if($.isNullOrEmpty(aValue))
		{
			r = -1;
		}
		else
		{
			r = (aValue > bValue) ? 1 : (aValue < bValue) ? -1 : 0;
		}
		return this.asc ? r : -r;
	}
	
	this.getFieldValue = function(rec)
	{
		var vars = rec.vars;
		if($.isNullOrEmpty(vars))
		{
			return null;
		}
		var v;
		switch(this.fieldName){
		case '初胜':
			v = vars[0].win.avg;
			break;
		case '初平':
			v = vars[0].draw.avg;
			break;
		case '初负':
			v = vars[0].lose.avg;
			break;
		case '胜':
			v = vars[1].lose.avg;
			break;
		case '平':
			v = vars[1].draw.avg;
			break;
		case '负':
			v = vars[1].lose.avg;
			break;
		case '胜差':
			v = vars[1].win.avg - vars[0].win.avg;
			break;
		case '平差':
			v = vars[1].draw.avg - vars[0].draw.avg;
			break;
		case '负差':
			v = vars[1].lose.avg - vars[0].lose.avg;
			break;
		case '初胜差':
			v = vars[0].win.stderr;
			break;
		case '初平差':
			v = vars[0].draw.stderr;
			break;
		case '初负差':
			v = vars[0].lose.stderr;
			break;
		case '胜差':
			v = vars[1].lose.stderr;
			break;
		case '平差':
			v = vars[1].draw.stderr;
			break;
		case '负差':
			v = vars[1].lose.stderr;
			break;
		case '胜差差':
			v = vars[1].win.stderr - vars[0].win.stderr;
			break;
		case '平差差':
			v = vars[1].draw.stderr - vars[0].draw.stderr;
			break;
		case '负差差':
			v = vars[1].lose.stderr - vars[0].lose.stderr;
			break;
		}
		return v;
	}
}

var options = { 
	refresh: false,	
	sorter: new OpStandardErrorSorter('ordinary', true, null),
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

function createColumns()
{
	var soccerTableColumns = new SoccerTableColumns();
	var columns = [];
	var columns1 = soccerTableColumns.createBasicMatchColumns(2);
	columns1.push({
	   title: '欧赔平均值',
	   colspan: 9
	});
	columns1.push({
		title: '欧赔方差值',
		colspan: 9
	});
	columns1.push(soccerTableColumns.createOperatorColumns(2));
	
	var columns2 = [{
    	field: 'vars',
    	sortable: true,
    	title: '初胜',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[0].win.avg);
 		}
	},
	{
    	field: 'vars',
    	sortable: true,
    	title: '初平',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[0].draw.avg);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '初负',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[0].lose.avg);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '胜',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].win.avg);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '平',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].draw.avg);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '负',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].lose.avg);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '胜差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].win.avg - value[0].win.avg, true);
 		}
	},
	{
    	field: 'vars',
    	sortable: true,
    	title: '平差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].draw.avg - value[0].draw.avg, true);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '负差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].lose.avg - value[0].lose.avg, true);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '初胜差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[0].win.stderr);
 		}
	},
	{
    	field: 'vars',
    	sortable: true,
    	title: '初平差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[0].draw.stderr);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '初负差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[0].lose.stderr);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '胜差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].win.stderr);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '平差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].draw.stderr);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '负差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].lose.stderr);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '胜差差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].win.stderr - value[0].win.stderr, true);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '平差差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].draw.stderr - value[0].draw.stderr, true);
 		}
	},{
    	field: 'vars',
    	sortable: true,
    	title: '负差差',
    	formatter: function(value, row, index)
 		{
 			return formatValue(value[1].lose.stderr - value[0].lose.stderr, true);
 		}
	}
	];
	
	columns.push(columns1);
	columns.push(columns2);
	return columns;
}

//数据格式化
function formatValue(value, flag)
{
	return '<div class="oddsvalue ' + (flag ? (value > 0 ? 'green' : value < 0 ? 'red' : 'blue') : '')
		+ '">' + value.toFixed(2) + '</div>';
}

//用于获得配置数据
function createMatchOddsTable(conf)
{
	issue = conf.issue;
	type = conf.type;
	var source = {
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"issue": issue,
			"type": type,
		},
		jsonp:'callback',
		success: null,
		error: null,
		presuccess: function(json, soccerTable)
		{
			if ($.isNotNullOrEmpty(json.data)) {
				soccerTable.options.rows = json.data;
				initLeaguePanel(json.data);
			}
		},
		complete: function(){
			/*$('#gridTable tbody .relation').off('click').on('click', function(){
				getRelatedMatch($(this));
			});*/
		}
	}
	options.columns = createColumns();
	options.source = source;
	table = new SoccerTable(options);
	$('#gridTable').soccerTable(table);
}
function stateChange(state, source, conf)
{
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

//页面初始化
$(document).ready(function()
{
	if($.isNotNullOrEmpty(issue))
	{
		$('#issueSel').val(issue);
	}
	var conf = getConfValue();
	createMatchOddsTable(conf);
	stateListeners.add(stateChange);
});

</script>
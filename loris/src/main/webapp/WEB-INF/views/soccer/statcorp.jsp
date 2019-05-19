<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<link rel="stylesheet" type="text/css" href="../content/scripts/soccer/soccer-table.css" />
<script type="text/javascript" src="../content/scripts/soccer/soccer-table.js"></script>

<div id="content" class="container_wrapper">
	<%@include file="./stat/toolbar.jsp"%>
	
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
var url = "../soccerdata/getCorpStatItems"


function CorpStatItemsSorter(field, asc, fieldName)
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
		var opVar = null;
		switch(this.field)
		{
		case 'baseOpVar':
			opVar = rec.baseOpVar;
			break;
		case 'winOpVar':
			opVar = rec.winOpVar;
			break;
	    case 'drawOpVar':
			opVar = rec.drawOpVar;
			break;
		case 'loseOpVar':
			opVar = rec.loseOpVar;
			break;
		default:
			break;
		}
		if($.isNullOrEmpty(opVar))
		{
			return rec;
		}
		var v;
		switch(this.fieldName){
		case '数场':
			v = opVar.num;
			break;
		case '胜差':
			v = opVar.vars[0];
			break;
		case '平差':
			v = opVar.vars[1];
			break;
		case '负差':
			v = opVar.vars[2];
			break;
		case '胜方差':
			v = opVar.vars[3];
			break;
		case '平方差':
			v = opVar.vars[4];
			break;
		case '负方差':
			v = opVar.vars[5];
			break;
		}
		return v;
	}
}

//数据列表
var columns = [
	[{
       	field: 'name',
       	title: '序号',
       	sortable: true,
       	rowspan: 2,
       	formatter: function(value, row, index)
 		{
 			return index + 1;
 		}
    },
	{
    	field: 'name',
     	title: '公司名称',
     	sortable: true,
     	rowspan: 2,
 	},
 	{
    	title: '全部比赛',
    	colspan: 7
	},
 	{
    	title: '胜比赛',
    	colspan: 7
 	},
 	{
    	title: '平比赛',
    	colspan: 7
 	},
 	{
    	title: '负比赛',
    	colspan: 7
 	}],
 	[{
    	field: 'baseOpVar',
    	sortable: true,
    	title: '场数',
    	formatter: function(value, row, index)
 		{
 			return value.num;
 		}
	},
	{
 		field: 'baseOpVar',
        sortable: true,
        title: '胜差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 0);
     	}
 	},
 	{
 		field: 'baseOpVar',
        sortable: true,
        title: '平差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 1);
     	}
 	},
 	{
 		field: 'baseOpVar',
        sortable: true,
        title: '负差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 2);
     	}
 	},
 	{
 		field: 'baseOpVar',
        sortable: true,
        title: '胜方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 3);
     	}
 	},
 	{
 		field: 'baseOpVar',
        sortable: true,
        title: '平方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 4);
     	}
 	},
 	{
 		field: 'baseOpVar',
        sortable: true,
        title: '负方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 5);
     	}
 	},
 	{
    	field: 'winOpVar',
    	sortable: true,
    	title: '场数',
    	formatter: function(value, row, index)
 		{
 			return value.num;
 		}
	},
 	{
 		field: 'winOpVar',
        sortable: true,
        title: '胜差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 0);
     	}
 	},
 	{
 		field: 'winOpVar',
        sortable: true,
        title: '平差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 1);
     	}
 	},
 	{
 		field: 'winOpVar',
        sortable: true,
        title: '负差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 2);
     	}
 	},
 	{
 		field: 'winOpVar',
        sortable: true,
        title: '胜方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 3);
     	}
 	},
 	{
 		field: 'winOpVar',
        sortable: true,
        title: '平方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 4);
     	}
 	},
 	{
 		field: 'winOpVar',
        sortable: true,
        title: '负方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 5);
     	}
 	},
 	{
    	field: 'drawOpVar',
    	sortable: true,
    	title: '场数',
    	formatter: function(value, row, index)
 		{
 			return value.num;
 		}
	},
 	{
 		field: 'drawOpVar',
        sortable: true,
        title: '胜差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 0);
     	}
 	},
 	{
 		field: 'drawOpVar',
        sortable: true,
        title: '平差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 1);
     	}
 	},
 	{
 		field: 'drawOpVar',
        sortable: true,
        title: '负差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 2);
     	}
 	},
 	{
 		field: 'drawOpVar',
        sortable: true,
        title: '胜方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 3);
     	}
 	},
 	{
 		field: 'drawOpVar',
        sortable: true,
        title: '平方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 4);
     	}
 	},
 	{
 		field: 'drawOpVar',
        sortable: true,
        title: '负方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 5);
     	}
 	},
 	{
    	field: 'loseOpVar',
    	sortable: true,
    	title: '场数',
    	formatter: function(value, row, index)
 		{
 			return value.num;
 		}
	},
 	{
 		field: 'loseOpVar',
        sortable: true,
        title: '胜差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 0);
     	}
 	},
 	{
 		field: 'loseOpVar',
        sortable: true,
        title: '平差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 1);
     	}
 	},
 	{
 		field: 'loseOpVar',
        sortable: true,
        title: '负差',
        formatter: function(value, row, index)
     	{
     		return formatVars(value.vars, 2);
     	}
 	},
 	{
 		field: 'loseOpVar',
        sortable: true,
        title: '胜方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 3);
     	}
 	},
 	{
 		field: 'loseOpVar',
        sortable: true,
        title: '平方差',
        formatter: function(value, row, index)
     	{
        	return formatVars(value.vars, 4);
     	}
 	},
 	{
 		field: 'loseOpVar',
        sortable: true,
        title: '负方差',
        formatter: function(value, row, index)
     	{
     		return formatVars(value.vars, 5);
     	}
 	}]
];

var options = { 
	refresh: false,	
	sorter: new CorpStatItemsSorter('baseOpVar', true, null),
	relator: null,
	rows: null,
	results: null,
	columns: columns,
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
function createStatCorpTables()
{
	var source = {
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
		},
		jsonp:'callback',
		success: null,
		error: null,
		presuccess: function(json, soccerTable)
		{
			if ($.isNotNullOrEmpty(json.data)) {
				soccerTable.options.rows = json.data;
			}
		}
	}	
	options.source = source;
	table = new SoccerTable(options);
	$('#gridTable').soccerTable(table);
}

function formatVars(vars, index)
{
	if(!$.isNullOrEmpty(vars))
	{
		return vars[index].toFixed(2);
	}
	else
	{
		return '';
	}
}

$(document).ready(function() {
	//调用函数，初始化表格  
	createStatCorpTables();
});
</script>
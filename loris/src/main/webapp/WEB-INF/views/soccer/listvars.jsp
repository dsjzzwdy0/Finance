<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%
    String issue = request.getParameter("issue");
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
%>
<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<style>
.gridTable .decrease
{
	color: red;
	font-weight: bold;
}

.gridTable .increase
{
	color: green;
	font-weight: bold;
}

</style>

<div id="content" class="container_wrapper">
	<%@include file="./analysis/toolbar.jsp"%>
	
	<div id="main" class="main_wrapper">	
		<table id="gridTable" class="gridTable table-hover"
			data-pagination="true"
			data-show-refresh="true">
		</table>
	</div>
</div>

<script type="text/javascript">
var url = "../soccerdata/getMatchesOpVar";
var issue = "<%=issue%>";
var refresh = false;

function updateData(request) {
	//var r = request;
	$.ajax({
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"issue" : issue,
			"refresh" : refresh,
		},
		jsonp:'callback',
        success : function (msg)
        {			
			request.success({
                row : msg.data
            });
            $('#gridTable').bootstrapTable('load', msg.data);
        },
		error:function(){
			layer.msg("错误");
		}
    });
}

function initTable() 
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		ajax: updateData,
		striped: false, //表格显示条纹 
		pagination: false, //启动分页 
		search: false, //是否启用查询 
		showColumns: false, //显示下拉框勾选要显示的列 
		showRefresh: false, //显示刷新按钮 
		sidePagination: "server", //表示服务端请求 
		//toolbar: "#toolbar",
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined",
		columns: [
			[{
				field: 'ordinary',
				title: '序号',
				rowspan: 2,
				valign: 'middle',
				formatter: function(value, row, index)
				{
					return value;
				}
			},
			{
				field: "leaguename",
				title: "比赛",
				rowspan: 2,
				valign: 'middle',
				formatter: function(value, row, index)
				{
					return '<a href="league?lid=' + row.lid + '">' + value + '</a>';
				}
			},
			{
	        	field: 'homename',
	         	title: '球队',
	         	rowspan: 2,
	         	valign: 'middle',
	         	width: 225,
	         	formatter: function(value, row, index)
	         	{
	         		var str = '<div class="team"><a class="teamInfo left" tid="' + row.homeid 
	         		str	+= '" href="#" onclick="showTeamInfo(this, ' + row.homeid + ')" title="'; 
	         		str	+= row.homename + '">';
	         		str += $.isNullOrEmpty(row.homerank) ? '' : '[' + row.homerank + ']';
	         		str += row.homename 
	         		str	+= '</a> <div class="vsclass" > vs </div> <a class="teamInfo right" tid="' 
	         		str += row.clientid +'" href="#" onclick="showTeamInfo(this, ' + row.homeid + ')" title="';
	         		str	+= row.clientname + '">' + row.clientname; 
	         		str += $.isNullOrEmpty(row.clientrank) ? '' : '[' + row.clientrank + ']';
	         		str += '</a></div>';
	         		return str;
	         	},
	     	},
	     	{
	        	field: 'matchtime',
	        	title: '比赛时间',
	        	valign: 'middle',
	        	valign: 'middle',
	        	rowspan: 2,
	        	formatter: function(value, row, index)
				{
	        		var t = formatDate(value, 'hh:mm');	        		
					return '<div class="teamInfo" title="' + value + '">' + t + "</div>";
				}
	    	},
	     	{
	        	title: '欧赔初盘',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	     	{
	        	title: '即时初盘',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	     	{
	        	title: '初盘标准差',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	     	{
	        	title: '即时标准差',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	    	{
	        	title: '差值',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	    	{
	        	field: 'mid',
	        	title: '详细信息',
	        	valign: 'middle',
	        	rowspan: 2,
	        	formatter: function(value, row, index) {
	        		var mid = value;
			    	var html = '<a class="analysis" href="bjop?mid=' + mid + '" target="blank">欧</a>';
			    	html += '<a class="analysis" href="ypdb?mid=' + mid + '" target="blank">亚</a>';
			    	html += '<a class="analysis" href="bfyc?mid=' + mid + '" target="blank">析</a>';
	        		return html;
				},
	    	}
	    	],
	    	[{
	    		field: 'vars',
	    		title: '胜赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatNumber(value[0].win.avg);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '平赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatNumber(value[0].draw.avg);
	    			//return formatVar(value.variance, row.drawvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '负赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatNumber(value[0].lose.avg);
	    			//return formatVar(value.variance, row.losevar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '胜赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatLatest(value[1].win.avg, value[0].win.avg);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '平赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatLatest(value[1].draw.avg, value[0].draw.avg);
	    			//return formatVar(value.variance, row.drawvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '负赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatLatest(value[1].lose.avg, value[0].lose.avg);
	    			//return formatVar(value.variance, row.losevar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '胜赔标差',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatNumber(value[0].win.stderr);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '平赔标差',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatNumber(value[0].draw.stderr);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '负赔标差',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatNumber(value[0].lose.stderr);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '胜赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatLatest(value[1].win.stderr, value[0].win.stderr);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '平赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatLatest(value[1].draw.stderr, value[0].draw.stderr);
	    			//return formatVar(value.variance, row.drawvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '负赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatLatest(value[1].lose.stderr, value[0].lose.stderr);
	    			//return formatVar(value.variance, row.losevar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '胜赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatError(value[1].win.stderr, value[0].win.stderr);
	    			//return formatVar(value.variance, row.winvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '平赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatError(value[1].draw.stderr, value[0].draw.stderr);
	    			//return formatVar(value.variance, row.drawvar.variance);
	    		}
	    	},
	    	{
	    		field: 'vars',
	    		title: '负赔',
	    		valign: 'middle',
	    		formatter: function(value, row, index) 
	    		{
	    			return formatError(value[1].lose.stderr, value[0].lose.stderr);
	    			//return formatVar(value.variance, row.losevar.variance);
	    		}
	    	}],
     	],
		onLoadSuccess: function(){ //加载成功时执行 
		    layer.msg("加载成功");
			//$("#cusTable").TabStyle();
		    
		}, 
		onLoadError: function(){ //加载失败时执行 
			layer.msg("加载数据失败", {time : 1500, icon : 2}); 
		} 
	});
}

function formatError(cur, first)
{
	return '<div class="' + getTableGridClass(first, cur) + '">' + formatNumber(cur - first) + '</div>'
}

function formatLatest(cur, first)
{
	return '<div class="' + getTableGridClass(first, cur) + '">' + formatNumber(cur) + '</div>'
}

function getTableGridClass(first, last)
{
	if(Math.abs(last - first) < 0.017) return '';
	else if(last > first) return 'increase';
	else return 'decrease';
}

function formatNumber(value)
{
	return $.isNullOrEmpty(value) ? "" : value.toFixed(2) ; 	
}

function formatVar(first, last)
{
	if($.isNullOrEmpty(first) || $.isNullOrEmpty(last))
	{
		return '<div>无</div>';	
	}
	var html = '';
	html = formatVarDetailValue(first.min, first.max, first.avg, first.stderr, 
			last.min, last.max, last.avg, last.stderr);
	return html;
}

function formatVarDetailValue(fmin, fmax, favg, fvar, lmin, lmax, lavg, lvar)
{
	var html = '';
	html += '<div class="firstvar">';
	html += '<div class="varvalue">' + fmin.toFixed(2) + '</div>';
	html += '<div class="varvalue">' + fmax.toFixed(2) + '</div>';
	html += '<div class="varvalue">' + favg.toFixed(2) + '</div>';
	html += '<div class="varvalue">' + fvar.toFixed(2) + '</div>';
	html += '</div>';
	
	html += '<div class="lastvar">';
	html += '<div class="varvalue">' + lmin.toFixed(2) + '</div>';
	html += '<div class="varvalue">' + lmax.toFixed(2) + '</div>';
	html += '<div class="varvalue">' + lavg.toFixed(2) + '</div>';
	html += '<div class="varvalue">' + lvar.toFixed(2) + '</div>';
	html += '</div>';
	return html;
}


//格式化数据值
function formatVarValue(variance, type)
{
	if($.isNullOrEmpty(variance))
	{
		return '';
	}
	var html = '';
	switch(type)
	{
	case 'win':
		html += variance.winmin + ' ' + variance.winmax + ' ' + variance.winavg + ' ' + variance.winvar;
		break;
	case 'draw':
		html += variance.drawmin + ' ' + variance.drawmax + ' ' + variance.drawavg + ' ' + variance.drawvar;
		break;
	case 'lose':
		html += variance.losemin + ' ' + variance.losemax + ' ' + variance.loseavg + ' ' + variance.losevar;
		break;
	default:
		break;
	}
	return html;
}

function btnUpdate()
{
	layer.msg("update");
}

function btnRefresh()
{
	refresh = true;
	$("#gridTable").bootstrapTable('refresh', {});
	refresh = false;
}

function showTeamInfo(element, tid)
{
	//layer.msg(element.innerText + ": " + tid);
	dialogOpen({
		width: "500px",
		height: "300px",
		url: "bjop?mid=" + tid
	});
	//$(this).webuiPopover('destroy').webuiPopover(settings);   
}

//页面初始化
$(document).ready(function()
{
	$("#txtDate").val(issue);
	$("#txtDate").datepicker({format: "yyyy-mm-dd"});
	
	//当点击查询按钮的时候执行  
	$("#btnSearch").bind("click", function(){
		issue = $("#txtDate").val();
		var opt = {
		    url: url,
		    query:{
		    	issue: issue,
		        refresh: false
			}
		};			
		$("#gridTable").bootstrapTable('refresh', opt);
	});	
	$("#btnUpdate").bind("click", btnUpdate);
	$("#btnRefresh").bind("click", btnRefresh);

	//调用函数，初始化表格  
	initTable();
	
	$(document).on('click','.game_select',function(){
		$(this).children('.pl-wind-ss').show();
		$(this).find('ul').show();
	}).on('mouseleave','.game_select',function(){
		$(this).children('.pl-wind-ss').hide();
		$(this).find('ul').hide();
	})
	//downloadData();	
});
</script>
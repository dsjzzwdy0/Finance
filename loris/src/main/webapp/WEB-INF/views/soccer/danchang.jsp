<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<%
    String issue = request.getParameter("issue");
	String type = request.getParameter("type");
	
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
	if(StringUtils.isEmpty(type))
	{
		type = "bd";
	}
%>

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
var sourceOkooo = "okooo";
var sourceZgzcw = "zgzcw";
var url = "../soccerdata/getMatchItems";
var issue = "<%=issue%>";
var type = "<%=type%>";
var refresh = false;
var dataList = null;

function LeagueRec(lid, name)
{
	this.lid = lid;
	this.num = 1;
	this.name = name;	
}

LeagueRec.prototype.setName = function(name)
{
	this.name = name;
}
LeagueRec.prototype.addNum = function()
{
	this.num ++;
}

function showSelectLeagues()
{
	var lids = [];
	var oMatchList = $("#leagueList input");
	oMatchList.each(function(){
        if($(this).prop("checked"))
        {
        	lids.push($(this).val());
        }
    });
	//layer.msg('重新显示: ' + lids.join(', '));
	
	//dataList.shift();
	//$('#gridTable').bootstrapTable('load', dataList);
}

function updateData(request) 
{
	$.ajax({
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"issue" : issue,
			"type" : type,
			"refresh" : refresh,
		},
		jsonp:'callback',
        success : function (msg)
        {
        	dataList = msg.data;
			request.success({
                row : msg.data
            });
            $('#gridTable').bootstrapTable('load', dataList);
            initLeaguePanel(dataList);
        },
		error:function(){
			layer.msg("错误");
		}
    });
}

function initLeaguePanel(matches)
{
	var len = matches.length;
	var recs = [];
	for(var i = 0; i < len; i ++)
	{
		var m = matches[i];
		if(!addLeagueRec(m.lid, recs))
		{
			var r = new LeagueRec(m.lid, m.leaguename);
			recs.push(r);
		}
	}
	
	len = recs.length;
	var html = [];
	for(var i = 0; i < len; i ++)
	{
		var rec = recs[i];
		html.push('<label><input name="CheckboxGroup1" value="' + rec.lid + '" type="checkbox">');
		html.push('<em class="echao" style="background-color: rgb(102, 153, 0)">' + rec.name + '</em>[' + rec.num + ']');
		html.push('</label>');		
	}
	
	$('#leagueList').html(html.join(''));
}

/**
 * 加入联赛的信息
 */
function addLeagueRec(lid, recs)
{
	var num = recs.length;
	for(var j = 0; j < num; j ++)
	{
		var rec = recs[j];
		if(rec.lid == lid)
		{
			rec.addNum();
			return true;
		}
	}
	return false;
}

function initTable() 
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		ajax: updateData,
		//url: url,
		/*queryParams: function queryParams(params) 
		{
			//设置查询参数  
            var param = {issue: issue};    
            return param;                   
        },*/
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
					return value;
				}
			},
			{
	        	field: 'homename',
	         	title: '球队',
	         	rowspan: 2,
	         	valign: 'middle',
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
	        	title: '欧赔数据',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	     	{
	        	title: '亚盘数据',
	        	colspan: 3,
	        	valign: 'middle',
	    	},
	    	{
				field: "homePerf",
				title: "主队战绩",
				rowspan: 2,
				valign: 'middle',
				formatter: function(value, row, index)
				{
					
					return '<div class="oddsvalue" title="' + performInfo(value) + '">' +
						($.isNullOrEmpty(value) ? '' : value.score) + "</div>";
				}
			},
	    	{
				field: "clientPerf",
				title: "客队战绩",
				rowspan: 2,
				valign: 'middle',
				formatter: function(value, row, index)
				{					
					return '<div class="oddsvalue" title="' + performInfo(value) + '">' +
						($.isNullOrEmpty(value) ? '' : value.score + "</div>");
				}
			},
			{
				field: "lastMatch",
				title: "最近交战",
				rowspan: 2,
				valign: 'middle',
				formatter: function(value, row, index)
				{					
					return '<div class="oddsvalue" title="' + matchInfo(row) + '">' +
						($.isNullOrEmpty(value) ? '无' : formatDate(value.matchtime, 'yy-MM-dd'))
						+ "</div>";
				}
			},
	    	{
	        	field: 'mid',
	        	title: '详细信息',
	        	valign: 'middle',
	        	rowspan: 2,
	        	formatter: function(value, row, index) {
	        		var mid = value;
			    	var html = '<a class="analysis" href="match?type=bjop&mid=' + mid + '" target="blank">欧</a>';
			    	html += '<a class="analysis" href="match?type=ypop&mid=' + mid + '" target="blank">亚</a>';
			    	html += '<a class="analysis" href="match?type=bfyc&mid=' + mid + '" target="blank">析</a>';
	        		return html;
				},
	    	}
	    	],
    		[{
	        	field: 'opItems',
	        	title: '平均欧赔',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
			    	var gid = "0";
			    	return formatOpValues(value.items, gid, sourceZgzcw);
				},
	    	},
	     	{
	        	field: 'opItems',
	        	title: '澳门',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
			    	var gid = "80";
			    	return formatOpValues(value.items, gid, sourceZgzcw);
				},
	    	},
	     	{
	        	field: 'opItems',
	        	title: '威廉希尔',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
			    	var gid = "115";
			    	return formatOpValues(value.items, gid, sourceZgzcw);
				},
	    	},
	     	{
	        	field: 'ypItems',
	        	title: '澳门',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
			    	var gid = "1";
			    	return formatYpValues(value.items, gid, sourceZgzcw);
				},
	    	},
	     	{
	        	field: 'ypItems',
	        	title: 'Interwetten',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
			    	var gid = "43";
			    	return formatYpValues(value.items, gid, sourceOkooo);
				},
	    	},
	     	{
	        	field: 'ypItems',
	        	title: '金宝博(188bet)',
	        	valign: 'middle',
	        	formatter: function(value, row, index) {
			    	var gid = "322";
			    	return formatYpValues(value.items, gid, sourceOkooo);
				},
	    	}]
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

function formatOpValues(opitems, gid, source)
{
	var len = opitems.length;
	for(var i = 0; i < len; i ++)
	{
		var op = opitems[i];
		if(op.gid == gid && op.source == source)
		{
			if($.isNullOrEmpty(op.values))
			{
				return '无';
			}
			var html = '<div class="oddsvalue">' + op.values[0].toFixed(2) + '</div>';
			html += '<div class="oddsvalue">' + op.values[1].toFixed(2) + '</div>';
			html += '<div class="oddsvalue">' + op.values[2].toFixed(2) + '</div>';
			return html;
		}
	}
	return '无';
}

function formatYpValues(ypitems, gid, source)
{
	var len = ypitems.length;
	for(var i = 0; i < len; i ++)
	{
		var yp = ypitems[i];
		if(yp.gid == gid && yp.source == source)
		{
			if($.isNullOrEmpty(yp.values))
			{
				return '无';
			}
			
			var html = '';
			if(source == sourceZgzcw)
			{
				html = '<div class="oddsvalue" title="来源：' + source + '">' + yp.values[0].toFixed(2) + '</div>';
				html += '<div class="handicap" title="' + yp.handicap + '">' + getHandicapName(yp.values[1]) + '</div>';
				html += '<div class="oddsvalue" title="来源：' + source + '">' + yp.values[2].toFixed(2) + '</div>';
			}
			else
			{
				html = '<div class="oddsvalue" title="来源：' + source + '">' + (yp.values[0] - 1.0).toFixed(2) + '</div>';
				html += '<div class="handicap" title="' + yp.handicap + '">' + getHandicapName(yp.values[1]) + '</div>';
				html += '<div class="oddsvalue" title="来源：' + source + '">' + (yp.values[2] - 1.0).toFixed(2) + '</div>';
			}			
			return html;
		}
	}
	return '无';
}

function matchInfo(row)
{
	var match = row;
	var lastMatch = row.lastMatch;	
	if($.isNullOrEmpty(lastMatch))
	{
		return '无';
	}
	
	var info = (match.homeid == lastMatch.homeid) ? match.homename : match.clientname;
	info += " vs " + ((match.homeid == lastMatch.homeid) ? match.clientname : match.homename);
	info += " 比分 " + lastMatch.score;
	return info;
}

function performInfo(perform)
{
	if($.isNullOrEmpty(perform))
	{
		return '无战绩记录';
	}
	var info = '最近' + perform.gamenum + '场,胜' + perform.winnum + '场,平' + perform.drawnum +
		'场,负' + perform.losenum + '场,进' + perform.goal + '球, 失' + perform.losegoal + '球';
	return info;
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

	$(document).on('click','.game_select',function(){
		$(this).children('.pl-wind-ss').show();
		$(this).find('ul').show();
	}).on('mouseleave','.game_select',function(){
		$(this).children('.pl-wind-ss').hide();
		$(this).find('ul').hide();
	})
	//调用函数，初始化表格  
	initTable();
	
	$('#leagueList').on("click", "input", function(){
        showSelectLeagues()
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
        showSelectLeagues()
    })
});
</script>
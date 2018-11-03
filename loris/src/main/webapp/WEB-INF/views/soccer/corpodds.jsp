<<<<<<< HEAD
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
=======
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="../content/css/soccer/bfyc.css" />
<style type="text/css">
.minibars
{
	width: 99% !important;
}

.data_wrapper
{
	width: 100%;
	margin-top: 20px;
}

.data_left
{
	width: 49%;
	float: left;
	border: 1px solid #ddd;
	min-height: 300px;
	margin-left: 3px;
}

.data_right
{
	width: 49%;
	float: right;
	border: 1px solid #ddd;
	margin-right: 3px;
	min-height: 300px;
}

.bootstrap-table .gridTable thead th
{
	border-bottom: 1px solid #ddd;
	padding: 8px;
	line-height: 28px;
	vertical-align: top;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	border: 1px solid #dddddd;
}

.bootstrap-table .gridTable tbody td
{
	border-left: 1px solid #dddddd;
}

.bootstrap-table .gridTable tbody .up
{
	color: red;
}

.bootstrap-table .gridTable tbody .down
{
	color: green;
}



</style>
<div id="content" class="container_wrapper">
	<%@include file="./analysis/matchbar.jsp"%>
	<div class="data_wrapper">
		<div class="data_left bootstrap-table">
			<table id="oddsoplist" class="table-hover table gridTable">
				<thead>
					<tr>
						<th id="optitle" colspan="10"></th>
					</tr>
					<tr>
						<th rowspan="2" style="vertical-align:middle;">序号</th>
						<th rowspan="2" style="vertical-align:middle;">时间</th>
						<th colspan="3">指数</th>
						<th colspan="2">最新概率(100%)</th>
						<th colspan="2">凯利指数</th>
						<th rowspan="2" style="vertical-align:middle;">赔付率</th>
					</tr>
					<tr>						
						<th>胜</th>
						<th>平</th>
						<th>负</th>
						<th>主</th>
						<th>客</th>
						<th>主</th>
						<th>客</th>
					</tr>
				</thead>
				<tbody id="oplist">
				
				</tbody>
			</table>
		</div>
		<div class="data_right bootstrap-table">
			<table id="oddsyplist" class="table-hover table gridTable">
				<thead>
					<tr>
						<th id="yptitle" colspan="10"></th>
					</tr>
					<tr>
						<th rowspan="2" style="vertical-align:middle;">序号</th>
						<th rowspan="2" style="vertical-align:middle;">时间</th>
						<th colspan="3">指数</th>
						<th colspan="2">最新概率(100%)</th>
						<th colspan="2">凯利指数</th>
						<th rowspan="2" style="vertical-align:middle;">赔付率</th>
					</tr>
					<tr>						
						<th>胜</th>
						<th>盘口</th>
						<th>负</th>
						<th>主</th>
						<th>客</th>
						<th>主</th>
						<th>客</th>
					</tr>
				</thead>
				<tbody id="yplist">
				
				</tbody>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">

var optype = 'op';
var yptype = 'yp';
var gid = request('gid');
var type = request('type');
var mid = request('mid');
var source = request('source');
var url = '../soccerdata/getMatchCompOdds';

//页面初始化
$(document).ready(function()
{
	$.ajax({
		type: "GET",
		url: url,
		contentType: "application/json;charset=utf-8",
		dataType: "json",
		data : {
			"mid": mid,
			"type": type,
			"gid": gid,
			"source": source
		},
		jsonp: 'callback',
        success: function (msg)
        {
			var m = msg.data;
			
			var opcorp = m.opCorp;
			if(!$.isNullOrEmpty(opcorp))
			{
				setTableHeaderTitle($('#optitle'), opcorp);
			}
			var ypcorp = m.ypCorp;
			if(!$.isNullOrEmpty(ypcorp))
			{
				setTableHeaderTitle($('#yptitle'), ypcorp);
			}
			
			var opitems = m.opItems;
			if(!$.isNullOrEmpty(opitems))
			{
				setOpListTable($('#oplist'), opitems, optype);	
			}
			
			var ypitems = m.ypItems;
			if(!$.isNullOrEmpty(ypitems))
			{
				setOpListTable($('#yplist'), ypitems, yptype);
			}
        	layer.msg(m.homename + ' vs ' + m.clientname);
        },
        error:function(){
			layer.msg("错误");
		}
	});
});

/**
 * 设置数据头部
 */
function setTableHeaderTitle(div, corp)
{
	var html = '<div>';
	
	if(corp.type == optype)
	{
		html += '欧赔数据：';
	}
	else
	{
		html += '亚盘数据：';
	}
	html += corp.name + '</div>';
	
	$(div).html(html);
}

/**
 * 设置表格数据
 */
function setOpListTable(div, opitems, type)
{
	var html = [];
	
	var len = opitems.length;
	var op, prevop;
	var winclass, drawclass, loseclass;
	for(var i = 0; i < len; i ++)
	{
		winclass = '';
		drawclass = '';
		loseclass = '';
		op = opitems[i];
		if(i == len - 1)
		{
			prevop = null;
		}
		else
		{
			prevop = opitems[i + 1];
			winclass = op.winodds > prevop.winodds ? 'up': (op.winodds < prevop.winodds ? 'down' : '');
			loseclass = op.loseodds > prevop.loseodds ? 'up' : (op.loseodds < prevop.loseodds ? 'down' : '');
		}

		html.push('<tr>')
		html.push('<td>' + (i + 1) + '</td>')
		html.push('<td>' + getDateString(op.lasttime) + '</td>')
		html.push('<td class="' + winclass + '">' + op.winodds.toFixed(2) + '</td>')
		
		if(type == optype)
		{
			html.push('<td>' + op.drawodds.toFixed(2) + '</td>')
		}
		else
		{
			html.push('<td>' + op.handicap + '</td>')
		}
		html.push('<td class="' + loseclass + '">' + op.loseodds.toFixed(2) + '</td>')
		html.push('<td>' + op.winprob.toFixed(2) + '</td>')
		html.push('<td>' + op.loseprob.toFixed(2) + '</td>')
		html.push('<td>' + op.winkelly.toFixed(2) + '</td>')
		html.push('<td>' + op.losekelly.toFixed(2) + '</td>')
		html.push('<td>' + op.lossratio.toFixed(2) + '</td>')
		html.push('</tr>')
	}
	
	$(div).html(html.join());
}

/**
 * 格式化时间
 */
function getDateString(lasttime)
{
	if($.isNullOrEmpty(lasttime))
	{
		return '';
	}
	try
	{
		var t = parseInt(lasttime);
		return formatDate(new Date(t), 'yyyy-MM-dd hh:mm');
	}
	catch(exeption)
	{
		//Exception
	}
	return lasttime;
}

</script>
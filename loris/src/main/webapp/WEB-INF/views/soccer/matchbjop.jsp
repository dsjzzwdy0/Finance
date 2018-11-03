<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<%
	String mid = request.getParameter("mid");
%>

<%@include file="./match/matchtoolbar.jsp"%>

<div class="main">
	<!-- 头部 -->
	<div id="data-header" class="data-header">
		<table class="bf-tab-00" width="100%" border="0" cellspacing="0" cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1"></colgroup>
			<colgroup class="#1 chushi-0" span="1"></colgroup>
			<colgroup class="#2 zuixin-0" span="1"></colgroup>
			<colgroup class="#3 touzhu-0" span="1"></colgroup>
			<colgroup class="#4 kaili-0" span="1"></colgroup>
			<colgroup class="peifulv" span="1"></colgroup>
			<colgroup class="lishi" span="1"></colgroup>
			<tbody>
				<tr class="tabhead">
					<th><span class="sx-tag">筛选</span></th>
					<th><select id="com-type">
							<option value="x" selected="selected">百家欧赔</option>
							<option value="0">主流公司</option>
							<option value="1">交易公司</option>
							<option value="2">亚洲公司</option>
							<option value="5">普通公司</option>
						</select>
					</th>
					<th class="#1"><button class="hiddenBtn">隐藏</button> 初始赔率</th>
					<th class="#2"><button class="showBtn" style="display: none;">初始</button>
						最新赔率</th>
					<th class="#3">最新概率(%)</th>
					<th class="#4">最新凯利指数</th>
					<th>赔付率</th>
					<th>历史</th>
				</tr>
			</tbody>
		</table>
		<table class="bf-tab-01" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="gengxin" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="peifulv" span="1"></colgroup>
			<colgroup class="lishi" span="1"></colgroup>
			<thead>
				<tr class="tabtit">
					<th class="border-r">序号</th>
					<th class="border-r border-l"><a href="javascript:;" class="pm" data="$a">公司</a></th>
					<th class="#1 border-l"><a href="javascript:;" class="pm" data="$b">胜</a></th>
					<th class="#1"><a href="javascript:;" class="pm" data="$c">平</a></th>
					<th class="#1 border-r"><a href="javascript:;" class="pm" data="$d">负</a></th>
					<th class="#2 border-l"><a href="javascript:;" class="pm" data="$e">胜</a></th>
					<th class="#2"><a href="javascript:;" class="pm" data="$f">平</a></th>
					<th class="#2"><a href="javascript:;" class="pm" data="$g">负</a></th>
					<th class="border-r">&nbsp;</th>
					<th class="#3 border-l"><a href="javascript:;" class="pm" data="$h">主</a></th>
					<th class="#3"><a href="javascript:;" class="pm" data="$i">平</a></th>
					<th class="#3 border-r"><a href="javascript:;" class="pm" data="$j">客</a></th>
					<th class="#4 border-l"><a href="javascript:;" class="pm" data="$k">主</a></th>
					<th class="#4"><a href="javascript:;" class="pm" data="$l">平</a></th>
					<th class="#4 border-r"><a href="javascript:;" class="pm" data="$m">客</a></th>
					<th class="border-l border-r"><a href="javascript:;" class="pm" data="$n">值</a></th>
					<th class="border-l">值</th>
				</tr>
				<tr class="shaixuan" style="display: none;">
					<th colspan="2" class="first border-r"><p class="fl">高级筛选	&gt;&gt;</p><p class="fr">最小<br> 最大</p></th>
					<th class="#1 border-l border-r"><input name="data-sift" type="text"> <br> <input name="data-sift" type="text"></th>
					<th class="#1"><input name="data-sift" type="text"> <br><input name="data-sift" type="text"></th>
					<th class="#1 border-r"><input name="data-sift" type="text"><br> <input name="data-sift" type="text"></th>
					<th class="#2 border-l"><input name="data-sift" type="text"><br> <input name="data-sift" type="text"></th>
					<th class="#2"><input name="data-sift" type="text"> <br><input name="data-sift" type="text"></th>
					<th class="#2"><input name="data-sift" type="text"> <br><input name="data-sift" type="text"></th>
					<th class="border-r">&nbsp;</th>
					<th class="#3 border-l"><input name="data-sift" type="text"><br> <input name="data-sift" type="text"></th>
					<th class="#3"><input name="data-sift" type="text"> <br><input name="data-sift" type="text"></th>
					<th class="#3 border-r"><input name="data-sift" type="text"><br> <input name="data-sift" type="text"></th>
					<th class="#4 border-l"><input name="data-sift" type="text"><br> <input name="data-sift" type="text"></th>
					<th class="#4"><input name="data-sift" type="text"> <br><input name="data-sift" type="text"></th>
					<th class="#4 border-r"><input name="data-sift" type="text"><br> <input name="data-sift" type="text"></th>
					<th colspan="2" class="border-l"><span class="shaixuan-btn"><input value="筛 选" type="button"> <a href="javascript:;">关闭</a></span></th>
				</tr>
			</thead>
		</table>
	</div>
	<!-- 内容 -->
	<div id="data-body" class="data-main">
		<table id="tblOps" class="bf-tab-02" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="gengxin" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="peifulv" span="1"></colgroup>
			<colgroup class="lishi" span="1"></colgroup>			
			<tbody>	</tbody>
		</table>
	</div>
	<!-- 底部 -->
	<div id="data-footer" class="footer-fix">
		<table class="bf-tab-03" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="foot-console" span="1"></colgroup>
			<colgroup class="foot-value" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#1 chushi" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="#2 zuixin" span="1"></colgroup>
			<colgroup class="gengxin" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#3 touzhu" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="#4 kaili" span="1"></colgroup>
			<colgroup class="peifulv" span="1"></colgroup>
			<colgroup class="lishi" span="1"></colgroup>
			<tbody>
				<tr>
					<td class="border-r"><input id="bjop_xsxz" class="bf-input1" value="显示选择" type="button"> 
						<a href="javascript:;" class="bf-a1">全选</a>
						<a href="javascript:;" class="bf-a1">反选</a>
						<a href="javascript:;" class="bf-a2">恢复</a>
					</td>
					<td class="border-r border-l">平均值</td>
					<td class="#1 border-l"></td>
					<td class="#1"></td>
					<td class="#1 border-r"></td>
					<td class="#2 border-l tdbg" id="avg_win"></td>
					<td class="#2 tdbg" id="avg_same"></td>
					<td class="#2 tdbg" id="avg_lost"></td>
					<td class="border-r tdbg">-</td>
					<td class="#3 border-l">-</td>
					<td class="#3">-</td>
					<td class="#3 border-r">-</td>
					<td class="#4 border-l">-</td>
					<td class="#4">-</td>
					<td class="#4 border-r">-</td>
					<td class="border-l border-r">-</td>
					<td class="border-l">&nbsp;</td>
				</tr>
				<tr>
					<td class="border-r"><span class="grey">共[<i id="com-count" class="red"></i>]家公司 </span></td>
					<td class="border-r border-l">最大值</td>
					<td class="#1 border-l"></td>
					<td class="#1"></td>
					<td class="#1 border-r"></td>
					<td class="#2 border-l tdbg"></td>
					<td class="#2 tdbg"></td>
					<td class="#2 tdbg"></td>
					<td class="border-r tdbg">-</td>
					<td class="#3 border-l"></td>
					<td class="#3"></td>
					<td class="#3 border-r"></td>
					<td class="#4 border-l"></td>
					<td class="#4"></td>
					<td class="#4 border-r">1.05</td>
					<td class="border-l border-r">0.99</td>
					<td class="border-l">&nbsp;</td>
				</tr>
				<tr>
					<td class="border-r"><a href="http://fenxi.zgzcw.com/export/2286943/bjop" target="_blank" class="bf-input1">Excel下载</a></td>
					<td class="border-r border-l">最小值</td>
					<td class="#1 border-l"></td>
					<td class="#1"></td>
					<td class="#1 border-r"></td>
					<td class="#2 border-l tdbg"></td>
					<td class="#2 tdbg"></td>
					<td class="#2 tdbg"></td>
					<td class="border-r tdbg">-</td>
					<td class="#3 border-l">29.46</td>
					<td class="#3">29.67</td>
					<td class="#3 border-r">30.08</td>
					<td class="#4 border-l">0.73</td>
					<td class="#4">0.80</td>
					<td class="#4 border-r">0.78</td>
					<td class="border-l border-r">0.80</td>
					<td class="border-l">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="17">
						<div class="data-f-ps">
							<span class="otherodds">离散度% 1.47 0.82 1.17 | 中足网方差% 2.16	0.67 1.36</span> 
							<label> <input id="checkbox-scroll"	checked="checked" value="头尾浮动" type="checkbox"> 头尾浮动</label> 
							<span><em class="hot"></em>为主流公司</span>
							<span>
								<i class="bold-bla">黑粗最大</i>
								<i class="bold-g">绿粗最小</i>
								<i class="red">↑上升</i>
								<i class="blue">↓下降</i>
								<i class="bold-r">凯利指数红粗超过1</i>
							</span>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>


<script type="text/javascript">
var url = '../soccerdata/getMatchOps';
var mid = '<%=mid%>';
var match = null;

var matchData = '${match.matchtimevalue}';

function getOpValues()
{
	$.ajax({
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
		data : {
			"mid": mid
		},
		jsonp:'callback',
        success : function (msg)
        {
        	if(msg.status == '200')
        	{
        		match = msg.data;
        		showMatchOps();
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

function showMatchOps()
{
	if($.isNullOrEmpty(match))
	{
		return;
	}
	var ops = match.ops;
	var text = [];
	var len = ops.length;
	for(var i = 0; i < len; i ++)
	{
		var op = ops[i];
		text.push('<tr class="" firsttime="' + op.firsttime + '" lasttime="' + op.lasttime + '" ismore="1">');
		text.push('<td class="border-r"><label class="sxinput">');
		text.push('<input type="checkbox">' + op.ordinary + '</label>');
		text.push('	</td>');
		text.push('<td data="' + op.gname + '" class="border-r border-l">' + (op.ismain ? '<font class="hot"></font>' : '') + op.gname + '</td>');
		text.push('<td data="'+ op.firstwinodds + '" class="#1 border-l">' + formatValue(op.firstwinodds) + '</td>');
		text.push('<td data="' + op.firstdrawodds + '" class="#1 ">' + formatValue(op.firstdrawodds) + '</td>');
		text.push('<td data="' + op.firstloseodds + '" class="#1 border-r">' + formatValue(op.firstloseodds) + '</td>');
		text.push('<td cid="0" data="${op.winodds }" class="#2 border-l tdbg">');
		text.push('<a href="zhishu?company_id=' + op.gid + '&amp;mid=' + op.mid + '" target="_blank" class="' +
			(op.winodds > op.firstwinodds ? 'blue' : op.winodds < op.firstwinodds ? 'red' : '') + '">' + formatValue(op.winodds) + '</a>');
		text.push('</td>');
		text.push('<td cid="0" data="' + op.drawodds + '" class="#2 tdbg">')
		text.push('<a href="zhishu?company_id=' + op.gid + '&amp;mid=${op.mid}" target="_blank" class="'  + 
			(op.drawodds > op.firstdrawodds ? 'blue' : op.drawodds < op.firstdrawodds ? 'red' : '') + '">' + formatValue(op.drawodds) + '</a>');
		text.push('</td>');
		text.push('<td cid="0" data="' + op.loseodds + '" class="#2 tdbg">');
		text.push('<a href="zhishu?company_id=' + op.gid + '&amp;mid=' + op.mid + '}" target="_blank" class="' +
				(op.loseodds > op.firstloseodds ? 'blue' : op.loseodds < op.firstloseodds ? 'red' : '') + '">' + formatValue(op.loseodds) + '</a>');
		text.push('</td>');
		text.push('<td class="border-r tdbg"><em class="gengxin-1" title="' + op.lasttime + '"></em></td>');
		text.push('<td data="' + op.winprob  + '" class="#3 border-l">' + formatValue(op.winprob) + '</td>');
		text.push('<td data="' + op.drawprob + '" class="#3			">' + formatValue(op.drawprob) + '</td>');
		text.push('<td data="' + op.loseprob + '" class="#3 border-r">' + formatValue(op.loseprob) + '</td>');
		text.push('<td data="' + op.winkelly + '" class="#4 border-l ' + (op.winkelly >= 1.00 ? 'bold-r' : '') + '">' + formatValue(op.winkelly) + '</td>');
		text.push('<td data="' + op.drawkelly + '" class="#4 ' + (op.drawkelly >= 1.00 ? 'bold-r' : '') + '">' + formatValue(op.drawkelly) + '</td>');
		text.push('<td data="' + op.losekelly + '" class="#4 ' + (op.losekelly >= 1.00 ? 'bold-r' : '') + '">' + formatValue(op.losekelly) + '</td>');
		text.push('<td data="' + op.lossratio + '" class="border-l border-r"><span class="">' + formatValue(op.lossratio) + '</span></td>');
		text.push('<td class="border-l">');
		text.push('<a rel="external nofollow" href="http://fenxi.zgzcw.com/2286943/bjop/zhu?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94&amp;win=2.43&amp;same=3.11&amp;lost=2.99" target="_blank" class="bf-a1">主</a> ');
		text.push('<a rel="external nofollow" href="http://fenxi.zgzcw.com/2286943/bjop/ke?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94&amp;win=2.43&amp;same=3.11&amp;lost=2.99" target="_blank" class="bf-a1">客</a>'); 
		text.push('<a rel="external nofollow" href="http://fenxi.zgzcw.com/2286943/bjop/tong?company_id=0&amp;win=2.43&amp;same=3.11&amp;lost=2.99&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94" target="_blank" class="bf-a1">同</a>');
		text.push('</td>');
		text.push('</tr>');
	}
	$('#tblOps tbody').html(text.join(''));
	OP.init();
	initUpdateTime();
	initHeader();
}

//数据格式化
function formatValue(value)
{
	return value.toFixed(2);
}

function setJF(obj){
    var dl = obj.find('[id=cur]').clone();
    $("#hg_top").find('dl:gt(0)').remove();
    $("#hg_top").find('dl.first').after(dl);
}

$(document).ready(function(){
	getOpValues();
	
	$("#hg_top,#scoreTop").on('mouseenter ',function(){
        $("#scoreTop").show()
	});
	
	$("#hg_top,#scoreTop").on('mouseleave',function(){
	    $("#scoreTop").hide();
	});
	
	$("#scoreTop div.con").delegate('dl','mouseenter',function(){
        if($(this).attr('class') == 'first') return ;
        $(this).addClass('hover');
  	})

   	$("#scoreTop div.con").delegate('dl','mouseleave',function(){
        if($(this).attr('class') == 'first') return ;
        $(this).removeClass('hover');
   	});
   	
   	$("#scoreTop :radio").unbind().bind('click',function(){
        var t = $(this).attr('t');
        if(!t) return ;
        $("#s_"+t).show().siblings('.con').hide()
        $("#s_"+t).parent().find('div.con:first').show()
        //setJF($("#s_"+t));

  	})

  	$("#scoreTop div.con").delegate('dl','mouseenter',function(){
        if($(this).attr('class') == 'first') return ;
        $(this).addClass('hover');
  	})

   	$("#scoreTop div.con").delegate('dl','mouseleave',function(){
        if($(this).attr('class') == 'first') return ;
        $(this).removeClass('hover');
   	})
});

</script>
<script type="text/javascript" src="../content/scripts/soccer/bjop.js"></script>

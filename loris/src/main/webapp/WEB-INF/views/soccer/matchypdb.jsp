<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="./match/matchtoolbar.jsp"%>

<div class="main">

	<!-- 宽度修正 -->
	<style type="text/css">
	
		.xvhao,
		.foot-value {
			width:5%;
		}
		
		.gongsi,
		.foot-console {
			width:10%;
		}
		
		.chushi-yp-0,
		.zuixin-yp-0 {
			width:22%;
		}
		
		.xiuzheng {
			width:10%;
		}		
	</style>
	
	<!-- 头部 -->
	<div id="data-header" class="data-header">
		<table class="bf-tab-00" width="100%" border="0" cellspacing="0" cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp-0" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp-0" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp-0" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp-0" span="1"></colgroup>
			<colgroup class="peifulv-yp" span="1"></colgroup>
			<colgroup class="lishi-yp" span="1"></colgroup>
			<tbody>
				<tr class="tabhead">
					<th><span class="sx-tag">筛选</span></th>
					<th><!-- 
						<select id="com-type">
							<option value="x">百家欧赔</option>
							<option value="0">主流公司</option>
							<option value="1">交易公司</option>
							<option value="2">亚洲公司</option>
							<option value="5">普通公司</option>
						</select> -->
					</th>
					<th class="#1-yp"><button class="hiddenBtn">隐藏</button>初始赔率</th>
					<th class="#2-yp"><button class="showBtn" style="display:none;">初始</button>最新赔率</th>
					<th class="#3-yp">最新概率(%)</th>
					<th class="#4-yp">最新凯利指数</th>
					<th>赔付率</th>
					<th>历史</th>
				</tr>
			</tbody>
		</table>
		<table class="bf-tab-01" width="100%" border="0" cellspacing="0" cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp xiuzheng" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp xiuzheng" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp" span="1"></colgroup>
			<colgroup class="gengxin" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp" span="1"></colgroup>
			<colgroup class="peifulv-yp" span="1"></colgroup>
			<colgroup class="lishi-yp" span="1"></colgroup>
			<tbody>			
				<tr class="tabtit">
					<th class="border-r">序号</th>
					<th class="border-r border-l"><a href="javascript:;" class="pm" data="$a">公司</a></th>
					<th class="#1-yp border-l"><a href="javascript:;" class="pm" data="$b">主</a></th>
					<th class="#1-yp"><a href="javascript:;" class="pm" data="$c">盘</a></th>
					<th class="#1-yp border-r"><a href="javascript:;" class="pm" data="$d">客</a></th>
					<th class="#2-yp border-l"><a href="javascript:;" class="pm" data="$e">主</a></th>
					<th class="#2-yp"><a href="javascript:;" class="pm" data="$f">盘</a></th>
					<th class="#2-yp"><a href="javascript:;" class="pm" data="$g">客</a></th>
					<th class="border-r">&nbsp;</th>
					<th class="#3-yp border-l"><a href="javascript:;" class="pm" data="$h">主</a></th>
					<th class="#3-yp border-r"><a href="javascript:;" class="pm" data="$i">客</a></th>
					<th class="#4-yp border-l"><a href="javascript:;" class="pm" data="$j">主</a></th>
					<th class="#4-yp border-r"><a href="javascript:;" class="pm" data="$k">客</a></th>
					<th class="border-l border-r"><a href="javascript:;" class="pm" data="$l">值</a></th>
					<th class="border-l">值</th>
				</tr>
				<tr class="shaixuan" style="display:none;">
					<th colspan="2" class="first border-r"><p class="fl">高级筛选 &gt;&gt;</p><p class="fr">最小<br>最大</p></th>
					<th class="#1-yp border-l border-r"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#1-yp"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#1-yp border-r"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#2-yp border-l"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#2-yp"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#2-yp"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="border-r">&nbsp;</th>
					<th class="#3-yp border-l"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#3-yp border-r"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#4-yp border-l"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th class="#4-yp border-r"><input name="data-sift" type="text"><br><input name="data-sift" type="text"></th>
					<th colspan="2" class="border-l"><span class="shaixuan-btn"><input value="筛 选" type="button"> <a href="javascript:;">关闭</a></span></th>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- 内容 -->
	<div id="data-body" class="data-main">
		<table class="bf-tab-02" width="100%" border="0" cellspacing="0" cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp xiuzheng" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp xiuzheng" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp" span="1"></colgroup>
			<colgroup class="gengxin" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp" span="1"></colgroup>
			<colgroup class="peifulv-yp" span="1"></colgroup>
			<colgroup class="lishi-yp" span="1"></colgroup>			
			<tbody>
				<c:forEach var="yp" items="${yplist}">
					<tr class="" firsttime="2018-02-19 23:20:40" ismore="0">
						<td class="border-r"><label class="sxinput"><input type="checkbox">${yp.ordinary }</label></td>
						<td data="" class="border-r border-l">${yp.gname }</td>
						<td id="chupan-w-1" data="0.85" class="#1-yp border-l	bold-g">${yp.firstwinodds }</td>
						<td id="chupan-s-1" data="平手" class="#1-yp			">${yp.firsthandicap }</td>
						<td id="chupan-l-1" data="1.01" class="#1-yp border-r	bold-bla">${yp.firstloseodds }</td>
						<td cid="1" data="0.96" class="#2-yp border-l tdbg"><a href="zhishu?company_id=1&amp;company=%E6%BE%B3%E9%97%A8" target="_blank" class="blue">${yp.winodds }</a></td>
						<td cid="1" data="平手" class="#2-yp tdbg"><a href="zhishu?company_id=1&amp;company=%E6%BE%B3%E9%97%A8" target="_blank" class="">${yp.handicap }</a></td>
						<td cid="1" data="0.90" class="#2-yp tdbg"><a href="zhishu?company_id=1&amp;company=%E6%BE%B3%E9%97%A8" target="_blank" class="red">${yp.loseodds }</a></td>
						<td class="border-r tdbg"><em id="gengxin-1" class="gengxin-2" title="更新时间：赛前9分"></em></td>
						<td data="49.22" class="#3-yp border-l	">${yp.winprob }</td>
						<td data="50.78" class="#3-yp border-r	">${yp.loseprob }</td>
						<td data="0.98" class="#4-yp border-l	">${yp.winkelly }</td>
						<td data="0.95" class="#4-yp border-r	">${yp.losekelly }</td>
						<td data="0.96" class="border-l border-r"><span class="">${yp.lossratio }</span></td>
						<td class="border-l">
							<a rel="external nofollow" href="http://fenxi.zgzcw.com/2286943/ypdb/zhu?company_id=1&amp;company=%E6%BE%B3%E9%97%A8" target="_blank" class="bf-a1">主</a>
							<a rel="external nofollow" href="http://fenxi.zgzcw.com/2286943/ypdb/ke?company_id=1&amp;company=%E6%BE%B3%E9%97%A8" target="_blank" class="bf-a1">客</a>
							<a rel="external nofollow" href="http://fenxi.zgzcw.com/2286943/ypdb/tong?company_id=1&amp;win=0.85&amp;same=0.00&amp;lost=1.01&amp;company=%E6%BE%B3%E9%97%A8" target="_blank" class="bf-a1">同</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
    </div>
    <!-- 底部 -->
	<div id="data-footer" class="data-footer">
	
		<table class="bf-tab-03" width="100%" border="0" cellspacing="0" cellpadding="0">
		
			<colgroup class="foot-console" span="1"></colgroup>
			<colgroup class="foot-value" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp xiuzheng" span="1"></colgroup>
			<colgroup class="#1-yp chushi-yp" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp xiuzheng" span="1"></colgroup>
			<colgroup class="#2-yp zuixin-yp" span="1"></colgroup>
			<colgroup class="gengxin" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp" span="1"></colgroup>
			<colgroup class="#3-yp touzhu-yp" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp" span="1"></colgroup>
			<colgroup class="#4-yp kaili-yp" span="1"></colgroup>
			<colgroup class="peifulv-yp" span="1"></colgroup>
			<colgroup class="lishi-yp" span="1"></colgroup>
			
			<tbody><tr>
				<td class="border-r"><input class="bf-input1" value="显示选择" type="button"></td>
				<td class="border-r border-l">平均值</td>
				<td class="#1-yp border-l">0.97</td>
				<td class="#1-yp"></td>
				<td class="#1-yp border-r">0.93</td>
				<td class="#2-yp border-l tdbg"><a target="_blank">0.96</a></td>
				<td class="#2-yp tdbg"></td>
				<td class="#2-yp tdbg">0.97</td>
				<td class="border-r tdbg">-</td>
				<td class="#3-yp border-l">50.15</td>
				<td class="#3-yp border-r">49.85</td>
				<td class="#4-yp border-l">0.98</td>
				<td class="#4-yp border-r">0.98</td>
				<td class="border-l border-r"></td>
				<td class="border-l">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="border-r"><a href="javascript:;" class="bf-a1">全选</a> <a href="javascript:;" class="bf-a1">反选</a> <a href="javascript:;" class="bf-a2">恢复</a></td>
				<td class="border-r border-l">最大值</td>
				<td class="#1-yp border-l">1.06</td>
				<td class="#1-yp"></td>
				<td class="#1-yp border-r">1.01</td>
				<td class="#2-yp border-l tdbg"><a target="_blank">1.06</a></td>
				<td class="#2-yp tdbg"></td>
				<td class="#2-yp tdbg">1.01</td>
				<td class="border-r tdbg">-</td>
				<td class="#3-yp border-l">51.15</td>
				<td class="#3-yp border-r">52.42</td>
				<td class="#4-yp border-l">1.03</td>
				<td class="#4-yp border-r">1</td>
				<td class="border-l border-r"></td>
				<td class="border-l">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="border-r"><span class="grey">共[<i id="com-count" class="red">13</i>]家公司</span></td>
				<td class="border-r border-l">最小值</td>
				<td class="#1-yp border-l">0.85</td>
				<td class="#1-yp"></td>
				<td class="#1-yp border-r">0.83</td>
				<td class="#2-yp border-l tdbg"><a target="_blank">0.91</a></td>
				<td class="#2-yp tdbg"></td>
				<td class="#2-yp tdbg">0.87</td>
				<td class="border-r tdbg">-</td>
				<td class="#3-yp border-l">47.58</td>
				<td class="#3-yp border-r">48.85</td>
				<td class="#4-yp border-l">0.96</td>
				<td class="#4-yp border-r">0.93</td>
				<td class="border-l border-r"></td>
				<td class="border-l">&nbsp;</td>
			</tr>
			
			<tr>
				<td colspan="15">
					<div class="data-f-ps">
						<a href="http://fenxi.zgzcw.com/export/2286943/ypdb" target="_blank" class="bf-input1">Excel下载</a>
						<span class="otherodds">
						</span>
						<label>
							<input id="checkbox-scroll" checked="checked" value="头尾浮动" type="checkbox">头尾浮动
						</label>
						<span>
							<em class="hot"></em>为主流公司
						</span>
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
		</tbody></table>
	</div>
</div>
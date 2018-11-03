<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
	<div style="float: left;">
		<span class="location">当前位置：</span><a href="../" class="aco">东彩首页</a> &gt; <b>数据比较分析</b>
	</div>
</div>

<div class="top-chosse">
	<div class="top-plnav">
		<ul>
			<li class="n_1"><a title="全部赛事">全部赛事</a></li>
			<li class="n_3"><a href="<c:if test="${page=='jingcai'}">javascript:void(0);</c:if><c:if test="${page!='jingcai'}">jingcai</c:if>" title="竞彩足球" class="<c:if test="${page=='jingcai'}">cur</c:if>">竞彩足球</a></li>
			<li class="n_4"><a href="<c:if test="${page=='danchang'}">javascript:void(0);</c:if><c:if test="${page!='danchang'}">danchang</c:if>" title="北京单场" class="<c:if test="${page=='danchang'}">cur</c:if>">北京单场</a></li>
			<li class="n_5"><a href="<c:if test="${page=='listvars'}">javascript:void(0);</c:if><c:if test="${page!='listvars'}">listvars</c:if>" title="北京单场" class="<c:if test="${page=='listvars'}">cur</c:if>">方差分析</a></li>
			<li class="n_6"><a href="/bqc/" title="半全场">半全场</a></li>
			<li class="n_7"><a href="/sfc/" title="胜负彩(任九)">胜负彩(任九)</a></li>
		</ul>
	</div>
	<div class="sx_form_c pl-chosse-cont">
		<div class="fl">
			<input id="retainChosen" class="pl-topbtn-cur" value="保留"	type="button">
			<input id="hideChosen" class="pl-topbtn" value="隐藏" type="button">
		</div>
		
		<div id="toolbar" class="toolbar">
			<div class=btn-group style="margin-right:5px;">
				<label for="txtDate">期号：
					<input type="text" id="txtDate" class="form-control" data-format="YYYY-MM-DD" style="height:26px;" placeholder="查询数据"/>
				</label>
			</div>
			<div class=btn-group style="margin-right:20px;">
				<button id="btnSearch" class="btn btn-default"><i class="glyphicon glyphicon-zoom-in">浏览</i> </button>					
			</div>
			<div class="btn-group btn-group-sm" style="margin-right:20px;"> 
				<button id="btnUpdate" class="btn btn-default"><i class="glyphicon glyphicon-plus">更新</i></button> 
			 	<button id="btnRefresh2" class="btn btn-default"><i class="glyphicon glyphicon-heart"></i></button> 
			 	<button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button> 
			 	<button id="btnRefresh" class="btn btn-default"><i class="glyphicon glyphicon-refresh">刷新</i></button>
			</div>
			<div class="btn-group btn-group-sm">
				<div id="sortBtn" class="btn btn-default">
					<i class="glyphicon glyphicon-trash">排序</i>
					<div class="sortElement">	
						<div class="lottoryKinds_item clearfix">
							<div class="caizhong_t">
								<em class="navIcon icon_jingcai"></em> 
								<a href="jingcai.html">竞彩</a>
							</div>
							<div class="caizhong_content">
								<a href="jingcai.html">胜平负</a>
							</div>
						</div>
						<div class="lottoryKinds_item clearfix">
							<div class="caizhong_t">
								<em class="navIcon icon_danchang"></em>
								<a href="danguan.html">单场</a>
							</div>
							<div class="caizhong_content">
								<a href="danguan.html">胜平负</a> 
								<a href="danguanfen.html">比分</a>
								<a href="http://www.okooo.com/danchang/jinqiu/">总进球</a>
								<a href="http://www.okooo.com/danchang/banquan/">半全场</a> 
								<a href="http://www.okooo.com/danchang/danshuang/">上下单双</a>
								<a href="http://www.okooo.com/danchang/guoguan/">胜负过关</a>
							</div>
						</div>
						<div class="lottoryKinds_item clearfix">
							<div class="caizhong_t">
								<em class="navIcon icon_zucai"></em>
								<a href="zucai.html">足彩</a>
							</div>
							<div class="caizhong_content">
								<a href="zucai.html">胜负彩</a> 
								<a href="ren9.html">任选九</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="pl-wind-b game_select" id="ssxz">
			赛事选择
			<div class="pl-wind-ss" style="display: none;">
				<div class="pl-wind-b1">赛事选择</div>
				<div style="clear:both;"></div>
				<div class="pl-wind-cont">
					<ul class="ssxz-ul" style="display: none;">
						<li id="leagueList">
						</li>
						<div style="clear: both;"></div>
						<li class="pl-wind-btn sx_form_b">
							<input value="全选" type="button" id="btnFull">
							<input value="反选" type="button" id="btnRevert">
							<input value="全不选" type="button" id="btnNone">
						</li>
					</ul>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<select id="matchSel">
			<option value="180901" selected="selected">180901</option>
			<option value="180805">180805</option>
			<option value="180804">180804</option>
			<option value="180803">180803</option>
			<option value="180802">180802</option>
			<option value="180801">180801</option>
			<option value="180706">180706</option>
			<option value="180705">180705</option>
			<option value="180704">180704</option>
			<option value="180703">180703</option>
		</select> 
		<div style="float:left;margin-left:3px;">共 <span id="matchNumAll">442</span>场比赛，隐藏了 <span id="matchNumHide">0</span>场。
		</div>
		<a href="javascript:;" style="display: none" id="recover">恢复</a>
		<div class="sx_form_a" style="float: right; background: #FFFFFF; border: 0 none; padding: 0;">
			<fieldset class="right">
				<label><input id="goalSound" checked="true" type="checkbox">进球声效：</label>
				<select id="readioList">
					<option value="0">胜利</option>
					<option value="1">哨子</option>
					<option value="2">号角</option>
					<option value="3">欢呼</option>
					<option value="4">投币</option>
					<option value="5">口哨</option>
					<option value="6">播报</option>
					<option value="7">警报</option>
					<option value="8">嘀嘀</option>
					<option value="9">叮咚</option>
				</select> 
				<label><input id="goalPop" checked="true" type="checkbox">进球弹窗</label>
				<label><input id="ranking" checked="true" type="checkbox">球队排名</label>
				<label><input id="redTip" checked="true" type="checkbox">红牌提示</label>
			</fieldset>
		</div>
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="lqnav" style="position: relative; margin-bottom: 0px; border-bottom: none; background-color: #fafafa;">
	<div style="float: left;">
		<span class="location">当前位置：</span>
		<a href="../" class="aco">东彩首页</a> 
		<span>&gt;</span> 
		<a href="analysis" class="aco">数据比较分析</a>
		<span>&gt;</span>  
		<b> ${title } <c:if test="${league!=null}"> (&nbsp;<a href="league?lid=${lid}" class="league">${league.name }</a>&nbsp;)</c:if></b>
	</div>
</div>

<div class="top-chosse">
	<div class="top-plnav">
		<ul>
			<li class="n_1">
				<a href="
					<c:if test="${page=='anarel'}">javascript:void(0);</c:if>
					<c:if test="${page!='anarel'}">analysis?type=anarel<c:if test="${not empty lid}">&lid=${lid}</c:if>
					</c:if>" 
					title="欧赔数据关联分析" class="<c:if test="${page=='anarel'}">cur</c:if>">关联分析
				</a>
			</li>
			<li class="n_2"><a href="<c:if test="${page=='anaoy'}">javascript:void(0);</c:if><c:if test="${page!='anaoy'}">analysis?type=anaoy<c:if test="${not empty lid}">&lid=${lid}</c:if></c:if>" title="欧亚对比" class="<c:if test="${page=='anaoy'}">cur</c:if>">欧亚对比</a></li>
			<li class="n_3"><a href="<c:if test="${page=='anaop'}">javascript:void(0);</c:if><c:if test="${page!='anaop'}">analysis?type=anaop<c:if test="${not empty lid}">&lid=${lid}</c:if></c:if>" title="欧赔对比" class="<c:if test="${page=='anaop'}">cur</c:if>">欧赔对比</a></li>
			<li class="n_4"><a href="<c:if test="${page=='anayp'}">javascript:void(0);</c:if><c:if test="${page!='anayp'}">analysis?type=anayp<c:if test="${not empty lid}">&lid=${lid}</c:if></c:if>" title="亚盘对比" class="<c:if test="${page=='anayp'}">cur</c:if>">亚盘对比</a></li>
			<li class="n_5"><a href="<c:if test="${page=='anafc'}">javascript:void(0);</c:if><c:if test="${page!='anafc'}">analysis?type=anafc<c:if test="${not empty lid}">&lid=${lid}</c:if></c:if>" title="欧赔方差分析" class="<c:if test="${page=='anafc'}">cur</c:if>">方差分析</a></li>
			<li class="n_6"><a href="<c:if test="${page=='anazj'}">javascript:void(0);</c:if><c:if test="${page!='anazj'}">analysis?type=anazj<c:if test="${not empty lid}">&lid=${lid}</c:if></c:if>" title="战绩对比分析" class="<c:if test="${page=='anazj'}">cur</c:if>">战绩分析</a></li>
			<li class="n_7"><a href="<c:if test="${page=='anazh'}">javascript:void(0);</c:if><c:if test="${page!='anazh'}">analysis?type=anazh<c:if test="${not empty lid}">&lid=${lid}</c:if></c:if>" title="综合分析" class="<c:if test="${page=='anazh'}">cur</c:if>">综合分析</a></li>
		</ul>
	</div>
	<div class="sx_form_c pl-chosse-cont">
		<div class="fl">
			<input id="btnRefresh" class="pl-topbtn-cur" value="刷  新" type="button">
			<input id="hideChosen" class="pl-topbtn" value="恢 复" type="button">
		</div>
		<select id="typeSel" class="sel_list">
			<option value="bd" selected="selected">北单数据</option>
			<option value="jc">竞彩数据</option>
		</select> 
		<select id="issueSel" class="sel_list">
			<c:forEach items="${issues }" var="issue" varStatus="status">
				<option value="${issue }" <c:if test="${status.count == 1}"> selected="selected" </c:if> >${issue}</option>
			</c:forEach>
		</select>
		<select id="settingSel" class="sel_list" style="display: none;">
			<c:forEach items="${settings }" var="setting" varStatus="status">
				<option value="${setting.id }" >${setting.name}</option>
			</c:forEach>
		</select>
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
		
		<div style="float:left; margin-left:3px;">
			共 <span id="matchNumAll">10</span>场比赛，隐藏了 <span id="matchNumHide">0</span>场。
			<a href="javascript:;" style="display: none" id="recover">恢复</a>
		</div>
		
		<div id="newToolbar" class="newToolBar" style="display: none; float: right; margin-right: 10px;">
			<label for="sameLeague" class="check_same_league"><input id="sameLeague" style="margin-right: 4px;" type="checkbox" checked="true" />同联赛内比较</label>
			<select id="oddsType" class="sel_list" style="width:70px;">
				<option value="start" selected>初盘</option>
				<option value="now">即时</option>
			</select>
			<select id="sort" class="sel_list" style="width: 70px;">
				<option value="asc" selected>升序</option>
				<option value="desc">降序</option>
			</select>
			<select id="threshold" class="sel_list" title="关联差值间隔" style="width: 70px; margin-right: 15px;" data-style="btn-warning">
				<option value="0.01">0.01</option>
				<option value="0.02">0.02</option>
				<option value="0.03" selected>0.03</option>
				<option value="0.04">0.04</option>
				<option value="0.05">0.05</option>
				<option value="0.08">0.08</option>
				<option value="0.10">0.10</option>
				<option value="0.15">0.15</option>
				<option value="0.20">0.20</option>
				<option value="0.25">0.25</option>
				<option value="0.30">0.30</option>
			</select>
			<input type="button" class="pl-topbtn" id="btnConfigure" value="排 序"/>
			<input type="button" class="pl-topbtn" id="btnReUnion" value="重 置"/>		
		</div>
	</div>
</div>
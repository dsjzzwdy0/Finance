<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="login_bg"></div>
<div class="Newheader_box">
	<div class="Newheader_content clearfix">
		<div class="float_r">
			<div class="navlist_box ">
				<span>足彩网站 <em class="navIcon icon_down_sm"></em></span>
				<div class="tc_xiala top_short">
					<em class="navIcon icon_jiantou"></em>
					<a href="http://www.zgzcw.com/" target="_blank">中国足彩网</a>
					<a href="http://www.okooo.com/" target="_blank">澳客网</a>
					<a href="http://www.500.com/" target="_blank">500彩票网</a>
				</div>
			</div>
			<div class="navlist_box ">
				<span>比分直播 <em class="navIcon icon_down_sm"></em></span>
				<div class="tc_xiala top_short">
					<em class="navIcon icon_jiantou"></em> 
					<a href="http://www.okooo.com/livecenter/football/">足球直播</a> 
					<a href="http://www.okooo.com/livecenter/jingcai/">竞彩直播</a>
					<a href="http://www.okooo.com/livecenter/danchang/">单场直播</a>
				</div>
			</div>
			<div class="navlist_box">
				<span><a href="http://www.okooo.com/store/">投注站</a></span>
			</div>
			<div class="navlist_box">
				<span><a href="../download/admin">数据下载</a></span>
			</div>
			
			<c:if test="${not empty user}">
				<div class="navlist_box bdrn">
					<span><em style="color: red">欢迎您,${user.username }</em>&nbsp;&nbsp;<a href="../user/logout">退出</a></span>
				</div>
			</c:if>
			<c:if test="${empty user}">
				<div class="navlist_box bdrn">
					<span><a href="../user/login">登录</a></span>
				</div>
			</c:if>
		</div>
	</div>
</div>
<div class="search_box_wrap clearfix">
	<a href="#">
		<div class="indexLogo_box">
			<div class="indexLogo_img">
				<img src="../content/images/soccer4.png" alt="东方足彩网"
					class="searchbox_logo float_l">
			</div>
		</div>
	</a>
	<div class="search_box float_l">
		<span class="search_text">
			<input name="wd" type="text" class="s_txt focus" id="txtKwSearch" placeholder="比赛、球队、投注站、小组">
		</span> 
		<a class="search_btn" id="btnSearchForm" target="_blank">搜索</a>
	</div>
</div>
<div class="header_mainNav2">
	<div class="header_mainNav_inner clearfix">
		<div class="selectLottory_nav">
			<a href="javascript:void(0)" class="chooselottoryBtn">竞彩足球 <em class="navIcon icon_down_lg"></em></a>
			<div class="lottoryKinds">
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
		<div class="subShow_nav">
			<!-- 
			<div class="navlist_box">
				<span class="<c:if test="${type==''}">check01</c:if>"><a href="sfc">竞彩足球</a></span>
			</div>
			<div class="navlist_box" slide="a">
				<span><a href="danguan.html">足球单场</a></span>
			</div>
			<div class="navlist_box" slide="a">
				<span><a href="zhucai.html">胜负彩</a></span>
			</div>
			 -->
			<div class="navlist_box" slide="a">
				<span class="<c:if test="${type=='analysis'}">check01</c:if>"><a href="#">分析中心<em class="navIcon icon_triangle"></em></a></span>
				<div class="tc_xiala">
					<em class="navIcon icon_jiantou"></em> 
					<a href="analysis?type=anarel">关联分析</a>
					<a href="analysis?type=anaoy">欧亚对比</a> 
					<a href="analysis?type=anaop">欧赔对比</a> 
				</div>
			</div>
			<div class="navlist_box" slide="a">
				<span><a href="#">数据分析<em class="navIcon icon_triangle"></em></a></span>
				<div class="tc_xiala">
					<em class="navIcon icon_jiantou"></em> 
					<a href="jingcai">欧亚比较</a>
					<a href="association">关联分析</a>
					<a href="listvars">方差指数</a>
				</div>
			</div>
			<div class="navlist_box" slide="a">
				<span> <a href="sszx">赛事中心</a></span>
			</div>
			<!-- 
			<div class="navlist_box" slide="a">
				<span> <a href="kaijiang.html">开奖结果</a></span>
			</div>
			<div class="navlist_box" slide="a">
				<span> <a href="result.html" target="_blank">比分直播</a></span>
			</div>
			 -->
			<div class="navlist_box" slide="a">
				<span class="<c:if test="${type=='setting'}">check01</c:if>"><a href="settings" target="_blank">用户设置</a></span>
			</div>
			<div class="navlist_box" slide="a">
				<span><a href="computeodds" target="_blank">计算工具</a></span>
			</div>
		</div>
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<div id="login_bg"></div>
	<div class="Newheader_box">
		<div class="Newheader_content clearfix">
			<div class="float_r">
				<div class="navlist_box ">
					<span>本系统网站 <em class="navIcon icon_down_sm"></em></span>
					<div class="tc_xiala top_short">
						<em class="navIcon icon_jiantou"></em>
						<a href="../soccer/danchang" target="_blank">东方足彩网</a>
						<a href="../stock/index" target="_blank">财经网</a>
					</div>
				</div>
				<div class="navlist_box ">
					<span>足彩网站 <em class="navIcon icon_down_sm"></em></span>
					<div class="tc_xiala top_short">
						<em class="navIcon icon_jiantou"></em>
						<a href="http://www.zgzcw.com/" target="_blank">中国足彩网</a>
						<a href="http://www.okooo.com/" target="_blank">澳客网</a>
						<a href="http://www.500.com/" target="_blank">500彩票网</a>
					</div>
				</div>
<<<<<<< HEAD
				<!-- 
=======
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
				<div class="navlist_box ">
					<span>股票网站<em class="navIcon icon_down_sm"></em></span>
					<div class="tc_xiala top_short">
						<em class="navIcon icon_jiantou"></em> 
						<a href="http://www.eastmoney.com">东方财富网</a> 
						<a href="http://finance.sina.com.cn/stock/">新浪网－股票</a>
						<a href="https://xueqiu.com/">雪球网</a>
						<a href="http://www.cninfo.com.cn/cninfo-new/index">巨潮咨讯</a>
						<a href="http://www.sse.com.cn/">上交所官网</a>
						<a href="http://www.szse.cn/">深交所官网</a>					
					</div>
				</div>
<<<<<<< HEAD
				 -->
=======
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
				<div class="navlist_box bdrn">
					<span><a href="login.html">登录</a></span>
				</div>
				<div class="navlist_box bdrn">
					<span><a href="register.html">注册</a></span>
				</div>
			</div>
		</div>
	</div>
	<div class="search_box_wrap clearfix">
		<div class="logo_box">
			<div class="indexLogo_img">
				<img src="../content/images/downloadlogo.png" alt="数据下载"
				class="searchbox_logo float_l" />
			</div>	
		</div>
		<!-- 
		<a href="#">
			<div class="indexLogo_box">
				<div class="indexLogo_img">
					<img src="../content/images/okooologo218x40.png" alt="数据下载"
						class="searchbox_logo float_l">
				</div>
				<div class="indexLogo_txt">数据下载</div>
			</div>
		</a>
		<div class="search_box float_l">
			<span class="search_text"> <input name="wd" type="text"
				class="s_txt focus" id="txtKwSearch" value="比赛、球队、投注站、小组">
			</span> 
			<a class="search_btn" id="btnSearchForm" target="_blank">搜索</a>
		</div> -->
	</div>
	<div class="header_mainNav2">
		<div class="header_mainNav_inner clearfix">
			<div class="subShow_nav">
				<div class="navlist_box">
					<span class="<c:if test="${page=='admin'}">check01</c:if>"><a href="admin">首页</a></span>
				</div>
				<div class="navlist_box" slide="a">
					<span class="<c:if test="${page=='current'}">check01</c:if>"> <a href="current">当前正在下载</a></span>
				</div>
				
				<c:forEach var="cates" items="${settings }">
					<div class="navlist_box" slide="a">
					<span><a href="#">${cates.key}<em class="navIcon icon_triangle"></em></a></span>
					<div class="tc_xiala">
						<em class="navIcon icon_jiantou"></em>
						
						<c:forEach var="setting" items="${cates.value}">
							<a href="downpage?wid=${setting.wid }" target="${setting.wid }">${setting.name }</a>						
						</c:forEach>
					</div>
				</div>
				</c:forEach>
<<<<<<< HEAD
				<!-- 
=======
				
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
				<div class="navlist_box" slide="a">
					<span><a href="#">下载配置<em class="navIcon icon_triangle"></em></a></span>
					<div class="tc_xiala">
						<em class="navIcon icon_jiantou"></em>
						<a href="../calendar.html" target="_blank">股票日历设置</a>
						<a href="../soccer/usercorp" target="_blank">博彩公司设置</a>
					</div>
				</div>
<<<<<<< HEAD
				 -->
=======
				
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
				<!-- 
				<div class="navlist_box" slide="a">
					<span><a href="#">股票数据下载<em class="navIcon icon_triangle"></em></a></span>
					<div class="tc_xiala">
						<em class="navIcon icon_jiantou"></em>
						<a href="downpage?wid=">明细数据下载</a>
						<a href="down?">日交易数据下载</a>
						<a href="http://www.okooo.com/jingcai/shuju/zhishu/">胜负指数</a> 
						<a href="http://www.okooo.com/jingcai/shuju/pankou/">盘口数据</a>
					</div>
				</div>
				<div class="navlist_box" slide="a">
					<span><a href="#">足彩数据下载<em class="navIcon icon_triangle"></em></a></span>
					<div class="tc_xiala">
						<em class="navIcon icon_jiantou"></em>
						<a href="down?">当日竞彩数据</a>
						<a href="down?">当日澳客数据</a>
						<a href="http://www.okooo.com/jingcai/shuju/zhishu/">胜负指数</a> 
						<a href="http://www.okooo.com/jingcai/shuju/pankou/">盘口数据</a>
					</div>
				</div>
				 -->
				<div class="navlist_box" slide="a">
					<span class="<c:if test="${page=='data'}">cur</c:if>"> <a href="data.html">数据中心</a></span>
				</div>
				<div class="navlist_box" slide="a">
					<span class="<c:if test="${page=='help'}">cur</c:if>"> <a href="kaijiang.html">下载说明</a></span>
				</div>

			</div>
		</div>
	</div>
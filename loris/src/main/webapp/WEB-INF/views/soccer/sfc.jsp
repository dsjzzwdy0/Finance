<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/sfcitem.css" />
<%
    String issue = request.getParameter("issue");
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
%>


<!-- 主要内容 -->
<div id="wrapper_content" class="box">
</div>

<!-- 购买彩票 -->
<%@include file="./shared/buysfc.jsp" %>

<div class="Clear"></div>
<div class="newsbox newsjc">
	<p>澳客网提示您：本站销售的竞彩足球让球胜平负、半全场、总进球投注时间为官方截止前0分钟，全国最晚。
		您可直接购买竞彩足球或参与竞彩足球合买，也可以在竞彩足球选单提交后保存方案进行竞彩足球模拟投注，
		还可以将竞彩足球选单在线过滤。为您提供竞彩足球过关赔率、竞彩足球开奖公告、竞彩足球奖金计算器、
		竞彩足球预测推荐等内容，方便您全面分析竞彩足球，储值提款即时到账奖金实时派发让您足不出户购买竞彩足球。</p>
</div>

<script type="text/javascript" src="../content/scripts/soccer/xiaomishu.js"></script>
<script type="text/javascript" src="../content/scripts/soccer/sfc.js"></script>
<script type="text/javascript" src="../content/scripts/soccer/search.js"></script>

<script type="text/javascript">
$(document).ready(function () {
    var issue = '<%=URLEncoder.encode(issue, "UTF-8")%>';
    DisplaySfcList(issue);
});
</script>


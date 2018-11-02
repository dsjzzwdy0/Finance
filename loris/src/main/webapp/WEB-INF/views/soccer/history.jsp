<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<!--  head meta, include css, title, etc. -->
<%@include file="./shared/basic.jsp"%>
<link rel="stylesheet" type="text/css" href="../content/css/soccer/newmatch.css" />

<body>
<!-- 页头 -->
<div class="top_header" align="center">
	<%@include file="./shared/header.jsp"%>
</div>

<!-- 主要内容 -->
<%@include file="./analysis/battlehistory.jsp"%>

<div class="bottom_footer" align="center">
	<!-- 页脚 -->
	<%@include file="./shared/footer.jsp" %>
</div>

<script type="text/javascript" src="../content/scripts/soccer/xiaomishu.js"></script>
<script type="text/javascript" src="../content/scripts/soccer/search.js"></script>

</body>
</html>
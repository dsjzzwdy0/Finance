<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<tiles:insertAttribute name="meta" />
</head>
<body>
<!-- 页头 -->
<div class="top_header" align="center">
	<tiles:insertAttribute name="header" />
</div>
<tiles:insertAttribute name="body" />
<div class="bottom_footer" align="center">
	<!-- 页脚 -->
	<tiles:insertAttribute name="footer" />
</div>	
</body>
</html>
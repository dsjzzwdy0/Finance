<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>错误页面</title>
<link rel="stylesheet" type="text/css" href="../content/scripts/bootstrap/bootstrap.min.css" />

<style>
.container-fluid
{
	margin: auto;
	width: 1120px;
	text-align: center;
}

.widget-box
{
	margin-top: 120px;
	min-height: 600px;
}

.error-info
{
	margin-bottom: 120px;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="widget-box">
			<div class="widget-title">
				<span class="icon"> <i class="icon-info-sign"></i>
				</span>
				<h5>Error 405</h5>
			</div>
			<div class="widget-content">
				<div class="error_ex">
					<h1>405</h1>
					<h3>Something is wrong here. Method not allowed!</h3>
					<p>Access to this page is forbidden</p>
					<h4 class="error-info">${info }</h4>
					<a class="btn btn-warning btn-big" href="index.html">返回主页</a>
					<a class="btn btn-warning btn-big" href="#" onclick="javascript:history.back(-1);">返回上一页</a>
					<a class="btn btn-warning btn-big" href="#" onclick="javascript:window.close();">关闭</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
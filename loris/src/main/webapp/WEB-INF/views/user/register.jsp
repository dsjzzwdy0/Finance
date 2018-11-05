<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="container-register">
	<div id="title"><i class="material-icons lock">lock</i>东方足球网－注册</div>
  	<form method="post" action="">
	    <div class="input">
	      	<div class="input-addon"><i class="material-icons">email</i></div>
	      	<input id="email" name="email" placeholder="Email" type="email" required class="validate" <c:if test="${code=='1'}">value="${email}"</c:if> autocomplete="off"></div>
	    <div class="clearfix"></div>
	    <div class="input">
	      	<div class="input-addon"><i class="material-icons">face</i></div>
	      	<input id="username" name="user" placeholder="用户名" type="text" required class="validate" <c:if test="${code=='1'}">value="${user}"</c:if> autocomplete="off"></div>
	    <div class="clearfix"></div>
	    <div class="input">
	      	<div class="input-addon"><i class="material-icons">vpn_key</i></div>
	      	<input id="password" name="password" placeholder="密码" type="password" required class="validate" <c:if test="${code=='1'}">value="${password}"</c:if> autocomplete="off"></div>
	    <div class="remember-me" <c:if test="${code=='1'}">style="display: none;"</c:if>>
	      	<input type="checkbox">
	      	<span style="color: #757575">接受东方足球网的服务条款</span>
	    </div>
	    <c:if test="${code!='1'}"><input type="submit" value="注 册" /></c:if>		    
	</form>
	<c:if test="${code!='1'}">
  		<div class="privacy"><a href="#">Privacy Policy</a></div>
  	</c:if>
  	<div class="register">
    	<span style="color: #657575">
    		<c:if test="${code=='1'}">您的用户账号'${user}'已经注册成功.</c:if>
    		<c:if test="${code!='1'}">您已经有了账号密码?</c:if>
    	</span>
    	<a href="login"><button id="register-link">点此登录</button></a>
  	</div>
  	<div class="error">${error }</div>
</div>
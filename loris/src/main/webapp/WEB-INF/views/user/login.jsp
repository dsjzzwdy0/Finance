<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  

<div id="container-login">
	<div id="title"><i class="material-icons lock">lock</i>东方足球网-登录</div>
  	<form method="post" action="login">
    	<div class="input">
      		<div class="input-addon"><i class="material-icons">face</i></div>
      		<input id="user" name="user" placeholder="用户名" type="text" required class="validate" autocomplete="off"></div>
    		<div class="clearfix"></div>
    		<div class="input"><div class="input-addon"><i class="material-icons">vpn_key</i></div>
      		<input id="password" name="password" placeholder="密码" type="password" required class="validate" autocomplete="off">
      		<input type="hidden" name="redirect" value="${redirect}" />
      	</div>
    	<div class="remember-me">
      		<input type="checkbox">
      		<span style="color: #757575">记住用户和密码</span>
      	</div>
    	<input type="submit" value="登 录" />
    </form>
  	<div class="forgot-password"><a href="#">Forgot your password?</a></div>
  	<div class="privacy"><a href="#">Privacy Policy</a></div>
  	<div class="register" style="display: none;">
  		<span style="color: #657575">Don't have an account yet?</span>
    	<a href="#"><button id="register-link">Register here</button></a>
  	</div>
  	<div class="error">${error }</div>
</div>
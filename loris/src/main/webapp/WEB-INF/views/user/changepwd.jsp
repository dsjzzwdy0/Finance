<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="container-register">
	<div id="title"><i class="material-icons lock">lock</i>东方足球网－更改密码</div>
  	<form method="post" action="" onsubmit="return checkValue()">
  		<div class="input">
	      	<div class="input-addon">用户名</div>
	      	<input id="username" name="user" placeholder="用户名" type="text" required readonly="readonly" value="${user.username }" class="validate" autocomplete="off">
	    </div>
	    <div class="clearfix"></div>
	    <div class="input">
	      	<div class="input-addon">原密码</div>
	      	<input name="oldpassword" onfocus="this.type='password'" placeholder="原密码" type="password" required class="validate" value="" autocomplete="off">
	    </div>
	    
	    <div class="input">
	      	<div class="input-addon">新密码</div>
	      	<input id="password" name="password" placeholder="新密码" type="password" required class="validate" value="" autocomplete="off">
	    </div>
	    <div class="input">
	      	<div class="input-addon">新密码</div>
	      	<input id="password1" name="password1" placeholder="确认新密码" type="password" required class="validate" value="" autocomplete="off">
	    </div>
	    <div class="remember-me" style="display: none;">
	      	<input type="checkbox">
	      	<span style="color: #757575">接受东方足球网的服务条款</span>
	    </div>
	    <c:if test="${code!='1'}"><input type="submit" value="更改密码" /></c:if>		    
	</form>
	<c:if test="${code!='1'}">
  		<div class="privacy"><a href="#">Privacy Policy</a></div>
  	</c:if>
  	<div class="register">
    	<span style="color: #657575">使用别的账号密码?
    	</span>
    	<a href="login"><button id="register-link">点此登录</button></a>
  	</div>
  	<div class="error">${error }</div>
</div>

<script type="text/javascript">

function checkValue()
{
	var p1 = document.getElementById('password').value;
	var p2 = document.getElementById('password1').value;
	
	if(p1 == undefined || p2 == undefined || p1.length < 6 || p2.length < 6)
	{
		layer.alert('输入新密码，至少6位数字、字母');
		return false;
	}
	if(p1 != p2)
	{
		layer.alert('两次输入的密码不一致，请重新输入');
		return false;
	}
}

$(document).ready(function{
	
});
</script>
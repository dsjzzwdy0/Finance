<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<!DOCTYPE html>
<html lang="en">
<head>
<title>登录-东方足彩网</title>
<meta name="keywords" content="竞彩足球胜平负,竞彩足球投注,竞彩胜平负推荐,竞彩胜平负数据,中国竞彩网首页">
<meta name="description" content="超过200家】免费提供竞彩胜平负开奖、竞彩胜平负推荐、竞彩胜平负数据等内容，方便彩民在线购买竞彩足球胜平负">
<meta name="robots" content="all">
<meta name="copyright" content="东方足球网">
<meta name="mobile-agent" content="format=html5;url=http://m.okooo.com/">
<meta name="mobile-agent" content="format=xhtml;url=http://wap.okooo.com/">
<link rel="Shortcut Icon" href="../content/images/loris_favicon_2.ico">
<link rel="stylesheet" href="../content/css/login/login.css">
</head>
<body>
    <header>
        <div class="header-line"></div>
    </header>
    <div class="content">
        <img class="content-logo" src="../content/images/login.png" alt="logo">
        <h1 class="content-title">东方足球网－登录</h1>
        <div class="content-form">
            <form method="post" action="" onsubmit="return submitTest()">
                <div id="change_margin_1">
                	<label class="username_icon"></label>
                    <input class="user" type="text" name="user" placeholder="请输入用户名" onblur="oBlur_1()" onfocus="oFocus_1()">
                </div>
                <!-- input的value为空时弹出提醒 -->
                <p id="remind_1"></p>
                <div id="change_margin_2">
                    <input class="password" type="password" name="password" placeholder="请输入密码" onblur="oBlur_2()" onfocus="oFocus_2()">
                </div>
                <!-- input的value为空时弹出提醒 -->
                <p id="remind_2"></p>           
                <div id="authBox" class="hide" style="margin-left: 42px; display:none;" data-authtype="okooo">
                	<dl class="clearfix hide" id="authCode" style="display: block;">
                		<dd class="yanzm_input">
                			<input name="AuthCode" class="textinput" id="AuthCode" tip="请输入右侧验证码。" title="输入验证码" value="" placeholder="验证码" type="text"> 
                			<div id="info_AuthCode" class="notice_msg"></div>
                		</dd>
                		<dd class="yanzm_display" style="">
                			<p><img id="randomNoImg" name="randomNoImg" alt="校验码" src="../content/images/out.png" width="100%" height="100%" align="absmiddle"></p>
                			<a class="link_blue" id="regChangeRandImg" href="javascript:void(0);"></a> 
                		</dd>
                	</dl>
                </div>                
                <!-- input的value为空时弹出提醒 -->
                <p id="remind_3"></p>
                <div id="change_margin_3">
                    <input class="content-form-signup" type="submit" value="登录">
                    <!-- <input type="hidden" name="redirect" value="${fromUrl }"> -->
                </div>
            </form>
        </div>
        <div>
       		<div class="error">${error}</div>
	        <div class="content-login-description">没有账户？<a class="content-login-link" href="sign_up.html">注册</a></div>
        </div>
    </div>
<script type="text/javascript">
//全局变量a和b，分别获取用户框和密码框的value值
var a;
var b;
//var c = document.getElementsByTagName("input")[2].value;

//用户框失去焦点后验证value值
function oBlur_1() {
	initValue();
    if (!a) { //用户框value值为空
        document.getElementById("remind_1").innerHTML = "请输入用户名！";
        document.getElementById("change_margin_1").style.marginBottom = 1 + "px";
    } else { //用户框value值不为空
        document.getElementById("remind_1").innerHTML = "";
        document.getElementById("change_margin_1").style.marginBottom = 19 + "px";
    }
}

//密码框失去焦点后验证value值
function oBlur_2() {
	initValue();
    if (!b) { //密码框value值为空
        document.getElementById("remind_2").innerHTML = "请输入密码！";
        document.getElementById("change_margin_2").style.marginBottom = 1 + "px";
        document.getElementById("change_margin_3").style.marginTop = 2 + "px";
    } else { //密码框value值不为空
        document.getElementById("remind_2").innerHTML = "";
        document.getElementById("change_margin_2").style.marginBottom = 19 + "px";
        document.getElementById("change_margin_3").style.marginTop = 19 + "px";
    }
}

//用户框获得焦点的隐藏提醒
function oFocus_1() {
    document.getElementById("remind_1").innerHTML = "";
    document.getElementById("change_margin_1").style.marginBottom = 19 + "px";
}

//密码框获得焦点的隐藏提醒
function oFocus_2() {
    document.getElementById("remind_2").innerHTML = "";
    document.getElementById("change_margin_2").style.marginBottom = 19 + "px";
    document.getElementById("change_margin_3").style.marginTop = 19 + "px";
}

function initValue()
{
	a = document.getElementsByTagName("input")[0].value;
	b = document.getElementsByTagName("input")[1].value;
}

//若输入框为空，阻止表单的提交
function submitTest() {
	initValue();
    if (!a && !b) { //用户框value值和密码框value值都为空
        document.getElementById("remind_1").innerHTML = "请输入用户名！";
        document.getElementById("change_margin_1").style.marginBottom = 1 + "px";
        document.getElementById("remind_2").innerHTML = "请输入密码！";
        document.getElementById("change_margin_2").style.marginBottom = 1 + "px";
        document.getElementById("change_margin_3").style.marginTop = 2 + "px";
        return false; //只有返回true表单才会提交
    } else if (!a) { //用户框value值为空
        document.getElementById("remind_1").innerHTML = "请输入用户名！";
        document.getElementById("change_margin_1").style.marginBottom = 1 + "px";
        return false;
    } else if (!b) { //密码框value值为空
        document.getElementById("remind_2").innerHTML = "请输入密码！";
        document.getElementById("change_margin_2").style.marginBottom = 1 + "px";
        document.getElementById("change_margin_3").style.marginTop = 2 + "px";
        return false;
    } 
    /*else if(!c){
    	document.getElementById("remind_3").innerHTML = "请输入验证码！";
    	return;
    }*/
}

window.load = function()
{
	document.getElementsByTagName('password').value='';
};
</script>
</body>
</html>
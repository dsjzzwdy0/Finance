$(function() {
	var optionFlag = true;
	$('[slide]').bind('mouseenter', function() {
		optionFlag = false;
		var $this = $(this);
		setTimeout(function() {
			if (!optionFlag) {
				$('[slide]').find("div.tc_xiala").hide();
				$('[slide]').find("span").removeClass("check");
				$this.find("span").addClass("check");
				$this.find("div.tc_xiala").show();
			}
		}, 50);
	});
	$('[slide]').bind('mouseleave', function() {
		optionFlag = true;
		var $this = $(this);
		if ($this.find("span").attr("cur")) {
			return false;
		}
		setTimeout(function() {
			if (optionFlag) {
				$this.find("span").removeClass("check");
				$this.find("div.tc_xiala").hide()
				if (!$('[slide]').find("span[cur]").hasClass("check")) {
					$('[slide]').find("span[cur]").addClass("check")
				}
			}
		}, 100);
	});

	function showStationMsgNotice() {
		$.get("/I/?method=ok.user.pm.GetMessageCount", {}, function(data) {
			if (data && data.code > 1 && data.info.count > 0) {
				(data.info.count > 99) ? data.info.count = 99 : data.info.count = data.info.count;
				$(".userinfo_img .msg_numdot").text("" + data.info.count).show();
				$("#userLoginSucInfo .msg_num").text("" + data.info.count);
			} else {
				$(".userinfo_img .msg_numdot").hide();
				$("#userLoginSucInfo .msg_num").hide();
			}
		}, "json");
	}
	$("#userLoginDiv .user_login").bind("click", function() {
		LoginShow(function() {
			showStationMsgNotice();
		});
	});
	showStationMsgNotice();
	$("#userLoginDiv").next().unbind("mouseenter").unbind("mouseleave").hover(function() {
		optionFlag = false;
		setTimeout(function() {
			if (!optionFlag) {
				$("#userLoginSucInfo").show();
				$(".userinfo_img .msg_numdot").hide();
				var pm = $("#userLoginSucInfo .msg_num");
				if (pm.text() != "") {
					pm.show();
				} else {
					pm.hide();
				}
			}
		}, 50);
	}, function() {
		optionFlag = true;
		setTimeout(function() {
			if (optionFlag) {
				$("#userLoginSucInfo").hide();
				var pm = $(".userinfo_img .msg_numdot");
				if (pm.text() != "") {
					pm.show();
				} else {
					pm.hide();
				}
				$("#userLoginSucInfo .msg_num").hide();
			}
		}, 100);
	});
	var keyword, placeHolder = "比赛、投注站、小组";
	if (location.href.indexOf("wd=") > -1) {
		var reg = new RegExp("(^|&)" + wd + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		keyword = unescape(r[2]);
		$("#txtKwSearch").val(decodeURI(keyword));
	}
	$("#btnSearchForm").bind("click", function() {
		var kw = $("#txtKwSearch").val() + "";
		if (kw.length < 2) {
			alert('关键字长度大于两个字符！');
			return false;
		}
		if (kw.trim() == "" || kw == placeHolder) {
			window.open("/search/");
		} else {
			if (keyword) {
				$(this).attr("target", "_self");
			}
			$(this).attr("href", "/search/?wd=" + encodeURI(kw) + "&type=match");
		}
	});
	$("#txtKwSearch").bind('keydown', function(event) {
		if (event.keyCode == 13) {
			var kw = $("#txtKwSearch").val() + "";
			if (kw.length < 2) {
				alert('关键字长度大于两个字符！');
				return false;
			}
			if (kw.trim() == "" || kw == placeHolder) {
				window.open("/search/");
			} else {
				if (keyword) {
					location.href = "/search/?wd=" + encodeURI(kw) + "&type=match";
				} else {
					window.open("/search/?wd=" + encodeURI(kw) + "&type=match");
				}
			}
		}
	});
	$("#txtKwSearch").bind('focus', function(event) {
		var kw = $("#txtKwSearch").val();
		if (kw == placeHolder) {
			this.value = "";
			$(this).removeClass("focus");
		}
	});
	$("#txtKwSearch").bind('blur', function(event) {
		var kw = $("#txtKwSearch").val();
		if (kw == '') {
			this.value = placeHolder;
			$(this).addClass("focus");
		}
	});
});
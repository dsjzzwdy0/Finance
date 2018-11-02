var store = (function() {
	var api = {}, win = window,
		doc = win.document,
		localStorageName = 'localStorage',
		globalStorageName = 'globalStorage',
		storage;
	api.set = function(key, value) {};
	api.get = function(key) {};
	api.remove = function(key) {};
	api.clear = function() {};
	if (localStorageName in win && win[localStorageName]) {
		storage = win[localStorageName];
		api.set = function(key, val) {
			storage.setItem(key, val)
		};
		api.get = function(key) {
			return storage.getItem(key)
		};
		api.remove = function(key) {
			storage.removeItem(key)
		};
		api.clear = function() {
			storage.clear()
		};
	} else if (globalStorageName in win && win[globalStorageName]) {
		storage = win[globalStorageName][win.location.hostname];
		api.set = function(key, val) {
			storage[key] = val
		};
		api.get = function(key) {
			return storage[key] && storage[key].value
		};
		api.remove = function(key) {
			delete storage[key]
		};
		api.clear = function() {
			for (var key in storage) {
				delete storage[key]
			}
		};
	} else if (doc.documentElement.addBehavior) {
		function getStorage() {
			if (storage) {
				return storage
			}
			storage = doc.body.appendChild(doc.createElement('div'));
			storage.style.display = 'none';
			storage.addBehavior('#default#userData');
			storage.load(localStorageName);
			return storage;
		}
		api.set = function(key, val) {
			var storage = getStorage();
			storage.setAttribute(key, val);
			storage.save(localStorageName);
		};
		api.get = function(key) {
			var storage = getStorage();
			return storage.getAttribute(key);
		};
		api.remove = function(key) {
			var storage = getStorage();
			storage.removeAttribute(key);
			storage.save(localStorageName);
		}
		api.clear = function() {
			var storage = getStorage();
			var attributes = storage.XMLDocument.documentElement.attributes;;
			storage.load(localStorageName);
			for (var i = 0, attr; attr = attributes[i]; i++) {
				storage.removeAttribute(attr.name);
			}
			storage.save(localStorageName);
		}
	}
	return api;
})();;

function okXiaoMiShu(paras) {
	if (screen.width <= 1024) {
		return;
	}
	paras = paras || {};
	var self = this,
		defConfig = $.extend({
			titlePic: true,
			titleName: '',
			titleShow: true,
			defMenu: true,
			pageType: '',
			htmlText: '',
			menuList: [],
			showToTop: true,
			hasRelaPro: true,
			defMsgText: '请尽量详细描述您的问题，以便我们快速回复您的问题。（5-1000个汉字）'
		}, paras);
	this.msgText = defConfig.defMsgText;
	this.defaultMenu = [{
		defcls: 'kefu_function01',
		cls: 'kefu_function01',
		url: 'http://www.okooo.com/help/',
		tar: '_blank',
		desc: '帮助中心'
	}, {
		defcls: 'kefu_function02',
		cls: 'kefu_function02',
		desc: '问题反馈',
		event: function(obj) {
			obj.click(function() {
				self.tiwenFn();
			});
		}
	}];
	this.tiwenFn = function() {
		$.getJSON('/I/?method=user.user.getInitData&AgentLoginType=', null, function(res) {
			if (typeof res.user_getinitdata_response != 'undefined') {
				res = res.user_getinitdata_response;
			} else {
				res = '';
			}
			if (res) {
				if (!res.UserInfo || !res.UserInfo.UserName || !res.UserInfo.UserID) {
					if (typeof(LoginShow) === "undefined") {
						if (/www\.itou\.com/.test(window.location.href)) window.location.href = "/itou/login.php";
						else window.location.href = "/User/RegForm.php";
					} else {
						LoginShow(function() {
							self.showMessageBox();
						});
					}
				} else {
					self.showMessageBox();
				}
			} else {
				Common.LightBox.showMessage('操作失败，请重试！');
			}
		});
	};
	this.showMessageBox = function() {
		var messageText = $('#xms_problem_detail_text');
		messageText.val(self.msgText);
		messageText.show();
		$('#jy_submit_success').hide();
		$('#xms_jianyi_submit').attr('opt_type', '').val('提 交');
		$('#xms_jianyi_submit').attr('onClick', "_gaq.push(['_trackEvent', '小秘书点击统计', '点击留言提交', '分类：" + defConfig.pageType + "']);");
		$('#xms_jianyibox').loadDialog();
		$('#xms_jianyi_close').click(function() {
			self.msgText = defConfig.defMsgText;
			$('#xms_jianyibox').loadDialog('close');
		});
		$('#okUploadPic').unbind('change').bind('change', function() {
			var filePath = $(this).val();
			fnOkUploadPic(filePath, {
				uploadPicUrl: '/I/?method=ok.user.comment.UploadMessage',
				picMaxSize: 1024 * 1024 * 4
			});
		});
		$('#xms_jianyi_submit').unbind('click').bind('click', function() {
			if ($(this).attr('opt_type') == 'close') {
				self.msgText = defConfig.defMsgText;
				$('#xms_jianyibox').loadDialog('close');
				return;
			}
			var sVal = $.trim(messageText.val());
			var contentLength = Math.ceil(OkoooUtil.cnLength(sVal) / 2);
			if (sVal == defConfig.defMsgText) {
				Common.LightBox.showMessage('请输入问题后提交！');
				return;
			}
			if (contentLength < 5) {
				Common.LightBox.showMessage('请至少输入5个汉字');
				return;
			}
			if (contentLength > 1000) {
				Common.LightBox.showMessage('您最多可以输入1000个汉字');
				return;
			}
			if (window.location.pathname == '/userinfo/service/') {
				purl = document.referrer;
				if (purl == "") {
					purl = window.location.href;
				}
			} else {
				purl = window.location.href;
			}
			var $uploadPics = $('#okUploadPic').parent().siblings('.upImgItem_box'),
				uploadPicArr = [];
			$uploadPics.each(function() {
				var imgPath = $(this).find('img').attr('data-path');
				if (imgPath) uploadPicArr.push(imgPath);
			});
			var submitParam = {
				'category': defConfig.pageType,
				'message': encodeURI(sVal),
				'purl': encodeURI(purl)
			};
			if (uploadPicArr.length > 0) $.extend(submitParam, {
				'img_list[]': uploadPicArr
			});
			$.post('/I/?method=ok.user.comment.Comment', submitParam, function(res) {
				if (res && res.code > 0) {
					messageText.hide();
					$('#jy_submit_success').show();
					$('#xms_jianyi_submit').attr('opt_type', 'close').val('关 闭');
				} else {
					Common.LightBox.showMessage(res.msg);
					return;
				}
			}, 'json');
		});
		messageText.focus(function() {
			if ($(this).val() == defConfig.defMsgText) {
				$(this).val('');
			}
		}).blur(function() {
			if ($(this).val() == '') {
				$(this).val(defConfig.defMsgText);
			}
		});
	};
	this.HtmlTpl = '<div class="kefu_bar" id="ok_xms_service" style="display: none;">' + '<div id="xms_menulist" class="kefu_bar_bg"></div>' + '</div>';
	this.insertToPage = function() {
		$('body').append(self.getStaticHtml() + defConfig.htmlText);
		var qlist = '',
			menuObj = $('<ul></ul>'),
			allMenuList = [];
		if (defConfig.titleShow) {
			menuObj.append('<li class="kefu_title">' + defConfig.titleName + '</li>');
		}
		if (defConfig.menuList.length) {
			allMenuList = defConfig.menuList;
		}
		if (defConfig.defMenu) {
			allMenuList = self.defaultMenu.concat(allMenuList);
		}
		for (var i = 0, len = allMenuList.length; i < len; i++) {
			var data = allMenuList[i],
				liObj = $('<li class="item-list"><a defcls="' + data.cls + '"><span class="kufu_item">' + data.desc + '</span></a></li>'),
				aObj = liObj.find('a');
			if (data.cls) {
				aObj.addClass(data.cls);
			}
			if (data.url) {
				aObj.attr('href', data.url);
				if (data.tar) {
					aObj.attr('target', data.tar);
				}
			} else {
				aObj.attr('href', 'javascript:void(0)');
			}
			aObj.attr('onClick', 'google_p(["小秘书点击统计", "点击' + data.desc + '", "分类：' + defConfig.pageType + '"]);');
			if (data.event) {
				if (data.hasOwnProperty('callFun')) {
					if (data.callFun) {
						data.event.call(this, aObj);
					}
				} else {
					data.event.call(this, aObj);
				}
			}
			if (data.detailinfo) {
				liObj.append(data.detailinfo);
			}
			menuObj.append(liObj);
		}
		if (defConfig.showToTop) {
			var $rtop = $('<li class="item-list"><a defcls="backtotop" class="backtotop" href="javascript:void(0);"><span class="kufu_item">返回顶部</span></a></li>').appendTo(menuObj).hide().click(function() {
				$(window).scrollTop(0);
			});
		}

		function tTopResize() {
			if ($rtop)
				$(window).scrollTop() > 0 ? $rtop.show() : $rtop.hide();
		}
		$(window).scroll(tTopResize);
		if ($('#ok_xms_service').size() > 0) $('#ok_xms_service').remove();
		$('body').append(self.HtmlTpl);
		if (defConfig.titlePic) {
			$('#xms_menulist').append('<span class="kefu_bar_jy"></span>');
		}
		$('#xms_menulist').append(menuObj);
		var effWidth = (screen.width - $(document).width() > 50 && $(document).width() > 1000) ? $(document).width() : screen.width;
		$('#ok_xms_service').css('left', (1000 + (effWidth - 1000) / 2 + 20) + 'px').show();
		window.onresize = function() {
			if ((1000 + ($(document).width() - 1000) / 2 + 20) > 1000) {
				$('#ok_xms_service').css('left', (1000 + ($(document).width() - 1000) / 2 + 20) + 'px');
			}
			tTopResize();
		}
		tTopResize();
		if (defConfig.pageType && defConfig.hasRelaPro) {
			$.get('/I/?method=user.knowledge.help&category=' + defConfig.pageType, function(res) {
				if (typeof res == 'string') {
					res = eval('(' + res + ')');
				}
				if (res.knowledge_help_response && res.knowledge_help_response.length) {
					var data = res.knowledge_help_response;
					for (var i = 0, len = data.length; i < len; i++) {
						qlist += '<p class="ctrl_queslist"><a target="_blank" href="/userinfo/service/detail/?id=' + data[i].id + '">' + data[i].title + '</a></p>';
					}
					if (qlist) {
						$('#xms_questionlink').append(qlist).mouseenter(function() {
							clearTimeout(window.xms_hidetime);
						}).mouseleave(function() {
							window.xms_hidetime = setTimeout(function() {
								$('#xms_questionlink').fadeOut();
							}, 300);
						});
					}
				}
			}, 'json');
		}
		self.changeDefCls();
	};
	this.getStaticHtml = function() {
		if (/www\.itou\.com/.test(window.location.href)) message_link = "/itou/UserMessage.php";
		else message_link = "/User/UserMessage.php";
		return '<div class="jianyibox" id="xms_jianyibox" style="display: none;">' + '<div>' + '<div class="jy_title"><a target="_blank" href="' + message_link + '"></a><span id="xms_jianyi_close" class="jianyiboxclose">&times;</span></div>' + '<div class="jy_content"><textarea class="inputpanel" rows="2" cols="" name="" id="xms_problem_detail_text">请尽量详细描述您的问题，以便我们快速回复您的问题。（5-1000个汉字）</textarea><p id="jy_submit_success" class="jy_success" style="display:none;">提交成功，感谢您的反馈，工作时间（工作日9:00-18:00）会尽快回复您，其他时间将顺延至工作日回复，请注意查收! &nbsp;&nbsp;&nbsp;<a href="/User/UserMessage.php" target="_blank">查看留言记录&gt;&gt;</a></p></div>' + '<div class="store_complain_img">上传相关截图,便于快速处理(图片最大4M，支持.jpg，.jpeg和.png格式)<div class="upImgBox clearfix"><span class="upImgBtn"><input type="file" accept="image/\*" name="upload_img" id="okUploadPic"></span></div></div>' + '<p class="jy_btn"><input type="button" id="xms_jianyi_submit" value="提交" name="JianyiSubmit" class="orangebtn100"></p>' + '</div>' + '</div>';
	};
	this.changeDefCls = function() {
		$('li.item-list a', $('#xms_menulist')).bind('mouseenter.item', function() {
			var defcls = $(this).attr('defcls');
			$(this).removeClass(defcls).addClass('kefu_function_hover');
		}).bind('mouseleave.item', function() {
			var defcls = $(this).attr('defcls');
			$(this).removeClass('kefu_function_hover').addClass(defcls);
		});
	};
	if (!paras.otherType) this.insertToPage();
	return this;
};

function warnMsg(url) {
	var lotterytips = "";
	var lotterytipsarr = ['WDL', 'Score', 'TotalGoals', 'HalfFull', 'OverUnder'];
	if (typeof(LotteryType) != 'undefined' && jQuery.inArray(LotteryType, lotterytipsarr) >= 0) {
		lotterytips = "北京单场";
	} else {
		lotterytips = "竞彩";
	}
	var htm = '<div id="jctips">\
     <div class="tsbox01">\
      <h2>请注意！</h2><p>您将要打开的数据页面中，主客场位置与现在投注页相反，投注时请确认主客队位置，一旦提交，我们将按照您的所选选项执行。</p>\
      <p class="tsbtnbox">\
       <input class="yellowbtn50"  type="button" value="确 定"/>\
      </p>\
     </div>\
    </div>';
	$("body").append(htm);
	jQuery.facybox({
		div: "#jctips"
	});
	$('#jctips').remove();
	if ($.browser.msie && ($.browser.version == "6.0")) {
		$("#facybox_overlay").bgiframe();
	}
	$(".content").find('.yellowbtn50').click(function() {
		jQuery(document).trigger('close.facybox');
		window.open(url);
	});
};;
(function($) {
	$.fn.fixPNG = function() {
		return this.each(function() {
			var image = $(this).css('backgroundImage');
			if (image.match(/^url\(["']?(.*\.png)["']?\)$/i)) {
				image = RegExp.$1;
				$(this).css({
					'backgroundImage': 'none',
					'filter': "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=" + ($(this).css('backgroundRepeat') == 'no-repeat' ? 'crop' : 'scale') + ", src='" + image + "')"
				}).each(function() {
					var position = $(this).css('position');
					if (position != 'absolute' && position != 'relative')
						$(this).css('position', 'relative');
				});
			}
		});
	};
	$.facybox = function(data, klass) {
		$.facybox.loading();
		$.facybox.content_klass = klass;
		if (data.ajax) revealAjax(data.ajax);
		else if (data.image) revealImage(data.image);
		else if (data.images) revealGallery(data.images, data.initial);
		else if (data.div) revealHref(data.div);
		else if ($.isFunction(data)) data.call($);
		else $.facybox.reveal(data);
	}
	$.extend($.facybox, {
		settings: {
			opacity: 0.3,
			overlay: true,
			modal: false,
			imageTypes: ['png', 'jpg', 'jpeg', 'gif']
		},
		html: function() {
			return '\
  <div id="facybox" style="display:none;"> \
   <div class="popup"> \
    <table> \
     <tbody> \
      <tr style="display:none"> \
       <td class="nw"/><td class="n" /><td class="ne"/> \
      </tr> \
      <tr> \
       <td class="w" /> \
       <td class="body"> \
       <div class="footer"> </div> \
       <a href="#" class="close"></a>\
       <div class="content"> \
       </div> \
      </td> \
       <td class="e"/> \
      </tr> \
      <tr style="display:none"> \
       <td class="sw"/><td class="s"/><td class="se"/> \
      </tr> \
     </tbody> \
    </table> \
   </div> \
  </div> \
  <div class="loading"></div> \
 '
		},
		loading: function() {
			init();
			if ($('.loading', $('#facybox'))[0]) return;
			showOverlay();
			$.facybox.wait();
			if (!$.facybox.settings.modal) {
				$(document).bind('keydown.facybox', function(e) {
					if (e.keyCode == 27) $.facybox.close();
				});
			}
			$(document).trigger('loading.facybox');
		},
		wait: function() {
			var $f = $('#facybox');
			$('.content', $f).empty();
			$('.body', $f).children().hide().end().append('<div class="loading"></div>');
			$f.fadeIn('fast');
			$.facybox.centralize();
			$(document).trigger('reveal.facybox').trigger('afterReveal.facybox');
		},
		centralize: function() {
			var $f = $('#facybox');
			var pos = $.facybox.getViewport();
			var wl = parseInt(pos[0] / 2) - parseInt($f.find("table").width() / 2);
			var fh = parseInt($f.height());
			if (pos[1] > fh) {
				var t = (pos[3] + (pos[1] - fh) / 2);
				$f.css({
					'left': wl,
					'top': t
				});
			} else {
				var t = (pos[3] + (pos[1] / 10));
				$f.css({
					'left': wl,
					'top': t
				});
			}
		},
		getViewport: function() {
			return [$(window).width(), $(window).height(), $(window).scrollLeft(), $(window).scrollTop()];
		},
		reveal: function(content) {
			$(document).trigger('beforeReveal.facybox');
			var $f = $('#facybox');
			$('.content', $f).attr('class', ($.facybox.content_klass || '') + ' content').html(content);
			$('.loading', $f).remove();
			var $body = $('.body', $f);
			$body.children().fadeIn('fast');
			$.facybox.centralize();
			$(document).trigger('reveal.facybox').trigger('afterReveal.facybox');
		},
		close: function() {
			$(document).trigger('close.facybox');
			return false;
		}
	})
	$.fn.facybox = function(settings) {
		var $this = $(this);
		if (!$this[0]) return $this;
		if (settings) $.extend($.facybox.settings, settings);
		if (!$.facybox.settings.noAutoload) init();
		$this.bind('click.facybox', function() {
			$.facybox.loading();
			var klass = this.rel.match(/facybox\[?\.(\w+)\]?/);
			$.facybox.content_klass = klass ? klass[1] : '';
			revealHref(this.href);
			return false;
		});
		return $this;
	}

	function init() {
		if ($.facybox.settings.inited) return;
		else $.facybox.settings.inited = true;
		$(document).trigger('init.facybox');
		makeBackwardsCompatible();
		var imageTypes = $.facybox.settings.imageTypes.join('|');
		$.facybox.settings.imageTypesRegexp = new RegExp('\.(' + imageTypes + ')', 'i');
		$('body').append($.facybox.html());
		var $f = $("#facybox");
		if ($.browser.msie) {
			$(".n, .s, .w, .e, .nw, .ne, .sw, .se", $f).fixPNG();
			if (parseInt($.browser.version) <= 6) {
				var css = "<style type='text/css' media='screen'>* html #facybox_overlay { position: absolute; height: expression(document.body.scrollHeight > document.body.offsetHeight ? document.body.scrollHeight : document.body.offsetHeight + 'px');}</style>"
				$('head').append(css);
				$(".close", $f).fixPNG();
				$(".close", $f).css({
					'right': '15px'
				});
			}
			$(".w, .e", $f).css({
				width: '13px',
				'font-size': '0'
			}).text("&nbsp;");
		}
		if (!$.facybox.settings.noAutoload) {}
		$('#facybox .close').click($.facybox.close);
	}

	function preloadImages() {}

	function makeBackwardsCompatible() {
		var $s = $.facybox.settings;
		$s.imageTypes = $s.image_types || $s.imageTypes;
		$s.facyboxHtml = $s.facybox_html || $s.facyboxHtml;
	}

	function revealHref(href) {
		if (href.match(/#/)) {
			var url = window.location.href.split('#')[0];
			var target = href.replace(url, '');
			if (target == '#') return
			$.facybox.reveal($(target).html(), $.facybox.content_klass);
		} else {
			revealAjax(href)
		}
	}

	function revealGallery(hrefs, initial) {
		var position = $.inArray(initial || 0, hrefs);
		if (position == -1) {
			position = 0;
		}
		var $footer = $('#facybox div.footer');
		$footer.append($('<div class="navigation"><a class="prev"/><a class="next"/><div class="counter"></div></div>'));
		var $nav = $('#facybox .navigation');
		$(document).bind('afterClose.facybox', function() {
			$nav.remove()
		});

		function change_image(diff) {
			position = (position + diff + hrefs.length) % hrefs.length;
			revealImage(hrefs[position]);
			$nav.find('.counter').html(position + 1 + " / " + hrefs.length);
		}
		change_image(0);
		$('.prev', $nav).click(function() {
			change_image(-1)
		});
		$('.next', $nav).click(function() {
			change_image(1)
		});
		$(document).bind('keydown.facybox', function(e) {
			if (e.keyCode == 39) change_image(1);
			if (e.keyCode == 37) change_image(-1);
		});
	}

	function revealImage(href) {
		var $f = $("#facybox");
		$('#facybox .content').empty();
		$.facybox.loading();
		var image = new Image();
		image.onload = function() {
			$.facybox.reveal('<div class="image"><img src="' + image.src + '" /></div>', $.facybox.content_klass);
			var $footer = $("div.footer", $f);
			var $content = $("div.content", $f);
			var $navigation = $("div.navigation", $f);
			var $next = $("a.next", $f);
			var $prev = $("a.prev", $f);
			var $counter = $("div.counter", $f);
			var size = [$content.width(), $content.height()];
			$footer.width(size[0]).height(size[1]);
			$navigation.width(size[0]).height(size[1]);
			$next.width(parseInt(size[0] / 2)).height(size[1]).css({
				left: (size[0] / 2)
			});
			$prev.width(size[0] / 2).height(size[1]);
			$counter.width(parseInt($f.width() - 26)).css({
				'opacity': 0.5,
				'-moz-border-radius': '8px',
				'-webkit-border-radius': '8px'
			})
		}
		image.src = href;
	}

	function revealAjax(href) {
		$.get(href, function(data) {
			$.facybox.reveal(data)
		});
	}

	function skipOverlay() {
		return $.facybox.settings.overlay == false || $.facybox.settings.opacity === null
	}

	function showOverlay() {
		if (skipOverlay()) return;
		if ($('#facybox_overlay').length == 0) {
			$("body").append('<div id="facybox_overlay" class="facybox_hide"></div>');
		}
		$('#facybox_overlay').hide().addClass("facybox_overlayBG").css('opacity', $.facybox.settings.opacity).fadeIn(200);
		if (!$.facybox.settings.modal) {}
	}

	function hideOverlay() {
		if (skipOverlay()) return;
		$('#facybox_overlay').fadeOut(200, function() {
			$("#facybox_overlay").removeClass("facybox_overlayBG").addClass("facybox_hide").remove();
		})
	}
	$(document).bind('close.facybox', function() {
		$(document).unbind('keydown.facybox');
		var $f = $("#facybox");
		if ($.browser.msie) {
			$('#facybox').hide();
			hideOverlay();
			$('#facybox .loading').remove();
		} else {
			$('#facybox').fadeOut('fast', function() {
				$('#facybox .content').removeClass().addClass('content');
				hideOverlay();
				$('#facybox .loading').remove();
			})
		}
		$(document).trigger('afterClose.facybox');
	});
})(jQuery);
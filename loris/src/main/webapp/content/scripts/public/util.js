
var OkoooUtil = {
    getAbsCenterAxis: function(el) {
        var clientWidth, clientHeight;
        if (!el.find("div:first").hasClass("md-effect-1")) {
            el.hide();
        } else {
            el = el.find("div:first");
        }
        var winWidth = $(window).width(),
            winHeight = $(window).height(),
            scrLeft = $(window).scrollLeft(),
            scrTop = $(window).scrollTop();
        var out_width = parseInt(el.width());
        out_height = parseInt(el.height());
        var t = 0;
        if (winHeight > out_height) {
            t = (scrTop + (winHeight - out_height) / 2);
        } else {
            t = (scrTop + (winHeight / 10));
        }
        return {
            left: (winWidth - el.width()) / 2,
            top: t
        };
    },
    setObjAbsCenter: function(el, isHide) {
        var poxy = this.getAbsCenterAxis(el);
        el.css({
            "position": "absolute",
            "left": poxy.left + "px",
            "top": poxy.top + "px",
            "z-Index": "5555"
        });
        if (el.find("div:first").hasClass("md-effect-1")) {
            el.addClass("md-show");
        } else {
            if (isHide) el.hide();
            else el.show();
        }
        return el;
    },
    clearInputVal: function() {
        $('.identify_content_item input,.pay_layer input').unbind('keyup').bind('keyup', function() {
            if ($.trim($(this).val()) != '' && $(this).css('visibility') != 'hidden') {
                if ($(this).siblings('.clearInput').size() == 0) {
                    $(this).parent().append('<em class="clearInput"></em>');
                } else {
                    $(this).siblings('.clearInput').show();
                }
            } else {
                $(this).siblings('.clearInput').hide();
            }
            $('.clearInput').unbind('click').bind('click', function() {
                var input = $(this).siblings('input');
                input.removeClass('cur').val(input.attr('tips') || '');
                $(this).hide().siblings('label').show();
            })
        });
    },
    showErrorTips: function(content) {
        if ($('.errorTips').size() > 0) {
            $('.errorTips').text(content)
        } else {
            var tipsDiv = ' <p class="errorTips">' + content + '</p>';
            $('.confirm_btn').before(tipsDiv);
        }
    },
    cnLength: function(str) {
        var escStr = escape(str),
            numI = 0,
            escStrlen = escStr.length;
        for (i = 0; i < escStrlen; i++) {
            if (escStr.charAt(i) == '%') {
                if (escStr.charAt(++i) == 'u') {
                    numI++;
                }
            }
        }
        return str.length + numI;
    },
    strToArr: function(str) {
        var arr = [];
        for (var i = 0; i < str.length; i++) {
            arr.push(str.substr(i, 1));
        }
        return arr;
    },
    changeIdCard: function(idcard) {
        if (idcard.length == 15) {
            idcard = idcard.substr(0, 6) + '19' + idcard.substr(6);
            return idcard = idcard + OkoooUtil.getIdCardAuth(idcard);
        }
        return idcard;
    },
    getAge: function(idcard) {
        if (idcard.length == 15) {
            idcard = idcard.substr(0, 6) + '19' + idcard.substr(6);
        }
        var year = idcard.substr(6, 4);
        var mounth = idcard.substr(10, 2);
        var day = idcard.substr(12, 2);
        var bornTime = new Date(year + '/' + mounth + '/' + day).getTime();
        var age = (new Date().getTime() - bornTime) / (365 * 24 * 60 * 60 * 1000);
        return age;
    },
    getIdCardAuth: function(idcard) {
        var coefficient = ['7', '9', '10', '5', '8', '4', '2', '1', '6', ' 3', '7', '9', '10', '5', '8', '4', '2'];
        var authCode = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
        var add = 0;
        idcard = OkoooUtil.changeIdCard(idcard);
        var idArr = OkoooUtil.strToArr(idcard);
        for (var i = 0; i < coefficient.length; i++) {
            add += parseInt(idArr[i]) * parseInt(coefficient[i]);
        }
        return authCode[parseInt(add % 11)];
    },
    IdCardVerify: function() {
        var IdCard = OkoooUtil.changeIdCard($('#IdCard').val());
        var flag = true;
        if (IdCard == '' || IdCard == '身份证号码') {
            OkoooUtil.showErrorTips('请输入身份证号码');
            return flag = false;
        }
        var date = IdCard.substr(10, 4);
        var authCode = IdCard.substr(17, 1);
        var age = OkoooUtil.getAge(IdCard);
        var condition1 = /^\d{6}(19|20|21)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(IdCard);
        var condition2 = /0[469]31|1131|02(30|31)/i.test(date);
        if (!condition1 || condition2 || age > 100 || authCode != OkoooUtil.getIdCardAuth(IdCard)) {
            OkoooUtil.showErrorTips('身份证号码输入有误');
            return flag = false;
        }
        if (age < 18) {
            OkoooUtil.showErrorTips('很抱歉，18周岁以下不能注册购彩！');
            return flag = false;
        }
        return flag;
    },
    realNameVerify: function() {
        var RealName = $("#RealName").val();
        var flag = true;
        if (RealName == '' || RealName == '真实姓名') {
            OkoooUtil.showErrorTips('请输入真实姓名');
            return flag = false;
        }
        if (!/[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*/.test(RealName)) {
            OkoooUtil.showErrorTips('真实姓名输入有误');
            return flag = false;
        }
        return flag;
    },
    mobileVerify: function(mobile) {
        var flag = true;
        if (mobile == '' || mobile == '填写手机号码' || mobile == '原手机号' || mobile == '手机号' || mobile == '请输入手机号') {
            OkoooUtil.showErrorTips('手机号不能为空');
            return flag = false;
        }
        if (!/^1[0-9]{10,10}$/.test(mobile)) {
            OkoooUtil.showErrorTips('手机号格式错误');
            return flag = false;
        }
        return flag;
    },
    mobileAuthVerify: function() {
        var vcode = $('#vcode').val();
        var flag = true;
        if (!vcode || vcode == '输入验证码' || vcode == '请填写收到的验证码') {
            OkoooUtil.showErrorTips('验证码不能为空');
            return flag = false;
        } else if (vcode.length != 6) {
            OkoooUtil.showErrorTips('验证码只能6位');
            return flag = false;
        }
        return flag;
    },
    payVerify: function(type) {
        var password = $('#payPassWord').val();
        var flag = true;
        if (!password || password == '输入支付密码') {
            OkoooUtil.showErrorTips('支付密码不能为空');
            return flag = false;
        } else if (password.length < 8 || password.length > 20) {
            OkoooUtil.showErrorTips('密码长度需要在8-20位字符之间');
            return flag = false;
        }
        if (type != 'only') {
            var confirm = $('#passwordConfirm').val();
            if (!confirm || confirm == '确认支付密码') {
                OkoooUtil.showErrorTips('确认支付密码不能为空');
                return flag = false;
            } else if (password != confirm) {
                OkoooUtil.showErrorTips('两次支付密码不一致');
                return flag = false;
            }
        }
        return flag;
    },
    emailVerify: function() {
        var email = $.trim($("#Email").val());
        var flag = true;
        if (email == '' || email == '邮箱地址' || email == '旧邮箱地址' || email == '新邮箱地址') {
            OkoooUtil.showErrorTips('邮箱不能为空');
            return flag = false;
        }
        if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test(email)) {
            OkoooUtil.showErrorTips('邮箱格式错误');
            return flag = false;
        }
        return flag;
    },
    emailAuthVerify: function() {
        var vcode = $('#vcode').val();
        var flag = true;
        if (!vcode || vcode == '输入验证码') {
            OkoooUtil.showErrorTips('验证码不能为空');
            return flag = false;
        } else if (vcode.length != 6) {
            OkoooUtil.showErrorTips('验证码只能6位');
            return flag = false;
        }
        return flag;
    },
    wordsVerify: function(str, num, gt) {
        var words = $.trim(str.replace(/[\s\n\r]/g, '')),
            wordsCount = Math.ceil(OkoooUtil.cnLength(words) / 2),
            flag = true;
        if (gt && wordsCount < num) {
            flag = false;
        } else if (!gt && wordsCount > num) {
            flag = false;
        }
        return flag;
    },
    sendAuth: function(el, type, mobile) {
        var is_send = false;
        var intval_deinfed = 60;
        var intval = intval_deinfed;
        el.unbind('click').bind('click', function(e) {
            e.preventDefault();
            if (is_send) return false;
            var param = {};
            var url = '/I/?method=ok.user.settings.sendmobileauth';
            if (type == 'email') {
                if (!OkoooUtil.emailVerify()) return false;
                param.email = $("#Email").val();
                url = '/I/?method=ok.user.settings.sendemailauth';
            } else {
                if ($('#Mobile').val() != $('#Mobile').attr('oldmobile')) {
                    mobile = $('#Mobile').val();
                }
                if (!OkoooUtil.mobileVerify(mobile)) return false;
                param.mobile = mobile || $('#Mobile').val();
                param.type = type;
            }
            $.post(url, param, function(data) {
                if (typeof data == 'undefined' || parseInt(data.code) < 0) {
                    alert(data.msg);
                    return false;
                }
                is_send = true;
                $('.errorTips').text('');
                el.html(intval + '秒后可重发');
                var ct = window.setInterval(function() {
                    --intval;
                    if (intval == 0) {
                        intval = intval_deinfed;
                        is_send = false;
                        window.clearInterval(ct);
                        el.attr('disabled', false).html('发送验证码');
                    } else {
                        el.attr('disabled', true).html(intval + '秒后可重发');
                    }
                }, 1000);
            }, 'json');
            return false;
        });
    },
    mobileAuthShow: function(param, type, callback) {
        $.post('/I/?method=ok.user.userinfo.getuserbaseattr', {}, function(res) {
            $('.errorTips').text('');
            if (res && res.code > 0) {
                if (type == 'modify') {
                    res.info.dealMobile = '';
                    res.info.title = '输入新手机号码';
                } else {
                    res.info.title = '手机认证';
                }
                var html = new EJS({
                    url: BaseUrl + '/Buy06/template/auth_mobile.ejs?v=' + ejsversion
                }).render(res.info);
                $(document.body).append(html);
                if ($('#Mobile').val() != '' && $('#Mobile').val() != '请输入手机号') {
                    $('#Mobile').addClass('cur').parent().append('<em class="clearInput"></em>');
                }
                $('.clearInput').unbind('click').bind('click', function() {
                    var input = $(this).siblings('input');
                    input.removeClass('cur').val(input.attr('tips') || '');
                    $(this).hide().siblings('label').show();
                });
                $('#auth_mobile_dialog').css('z-index', '9999').loadDialog();
                $('#dialog_overlay').css('z-index', '9998');
                placeholder();
                OkoooUtil.clearInputVal();
                OkoooUtil.sendAuth($('#send_sms'), 'setting', res.info.mobile);
                $('.identify_close').unbind('click').bind('click', function() {
                    $('#auth_mobile_dialog').loadDialog('close');
                    $('#auth_mobile_dialog').remove();
                    if ($('.sendBtn').hasClass('disabled')) {
                        $('.sendBtn').removeClass('disabled');
                    }
                });
                $('#submit_mobile_btn').unbind('click').bind('click', function() {
                    $('.errorTips').text('');
                    var url = '';
                    var mobile;
                    if ($('#Mobile').val() != $('#Mobile').attr('oldmobile')) {
                        mobile = $('#Mobile').val();
                    } else {
                        mobile = res.info.mobile;
                    }
                    if (!OkoooUtil.mobileVerify(mobile)) return false;
                    if (!OkoooUtil.mobileAuthVerify()) return false;
                    param.code = $('#vcode').val();
                    param.mobile = mobile;
                    if (type == 'auth' || type == 'onlyAuth') {
                        url = '/I/?method=ok.user.settings.setauthmobile';
                    } else if (type == 'modify') {
                        url = '/I/?method=ok.user.settings.modifyauthmobile';
                    }
                    $.ajax({
                        url: url,
                        type: 'post',
                        data: param,
                        dataType: 'json',
                        success: function(data) {
                            $('.errorTips').text('');
                            if (data.code > 0) {
                                if (typeof callback === 'function') {
                                    callback();
                                } else if (type != 'onlyAuth') {
                                    location.reload();
                                }
                                $('#auth_mobile_dialog').loadDialog('close');
                                $('#auth_mobile_dialog').remove();
                                if ($('.sendBtn').hasClass('disabled')) $('.sendBtn').removeClass('disabled')
                            } else {
                                OkoooUtil.showErrorTips(data.msg);
                            }
                        },
                        error: function() {
                            OkoooUtil.showErrorTips('提交验证时，网络错误');
                        }
                    });
                });

                function placeholder() {
                    $('input').focus(function() {
                        if ($(this).val() == $(this).attr('tips')) {
                            $(this).val('').addClass('cur');
                        }
                    }).blur(function() {
                        if ($(this).val() == '' && $(this).attr('type') != "password") {
                            $(this).val($(this).attr('tips'));
                            $(this).removeClass('cur');
                        }
                    });
                }
            } else {
                $('.errorTips').text('');
                OkoooUtil.showErrorTips(res.msg);
            }
        }, 'json');
    }
};
$(window).bind('resize', function() {
    if ($("#connactDialogDiv").size() > 0 && $("#connactDialogDiv").is(":visible")) {
        OkoooUtil.setObjAbsCenter($("#connactDialogDiv"));
    }
});;
if (typeof(googlestarttime) == 'undefined') {
    var googledataobjs = new Date();
    var googlestarttime = googledataobjs.getTime();
}
var current = window.location.hostname;
var googlecurrentlindex = '';
var _gaq = _gaq || [];
var urlAge = /^(www|bbs|cdn|buy|data|zucaidanchang|zucai|jingcai|jingcaiwang|shuangseqiu|3d|qilecai|22x5|daletou|qixingcai|p3|p5|shishicai|shiyiyunduojin|klsf|qunyinghui|kuaile8|pk10|11x5|gd11x5|ouguan|i|dejia|yijia|yingchao|xijia|gg)\.okooo\.com/;
if (urlAge.test(current)) {
    googlecurrentlindex = '.okooo.com';
} else {
    googlecurrentlindex = 'auto';
}
if (typeof(googlegaurl) == 'undefined') {
    var googlegaurl = "http://imgv1.okoooimg.com";
}
if (typeof(googleversion) == 'undefined') {
    var versionobj = new Date();
    var versionval = "" + versionobj.getFullYear() + (versionobj.getMonth() + 1);
    var googleversion = versionval;
}
_gaq.push(['_setAccount', 'UA-144633-3']);
_gaq.push(['_setDomainName', googlecurrentlindex]);
_gaq.push(['a1._setAccount', 'UA-27437686-1']);
_gaq.push(['a1._setDomainName', googlecurrentlindex]);
_gaq.push(['a2._setAccount', 'UA-27437686-2']);
_gaq.push(['a2._setDomainName', googlecurrentlindex]);
_gaq.push(['_setAllowLinker', true]);
_gaq.push(['_setAllowHash', true]);
_gaq.push(['_addOrganic', 'soso', 'w']);
_gaq.push(['_addOrganic', '3721', 'name']);
_gaq.push(['_addOrganic', 'youdao', 'q']);
_gaq.push(['_addOrganic', 'vnet', 'kw']);
_gaq.push(['_addOrganic', 'sogou', 'query']);
_gaq.push(['_addOrganic', '360', 'q']);
var thisPathName = window.location.pathname;
if (typeof(googleLotteryType) == 'undefined' || googleLotteryType == "") {
    var pathHtmlAge = /\.html$|\/$|.php$/;
    if (pathHtmlAge.test(thisPathName)) {
        _gaq.push(['_trackPageview']);
    } else {
        _gaq.push(['_trackPageview', thisPathName + '/']);
    }
} else {
    var pathNameAge = /\.php$/;
    var pathHtmlAge = /\.html$|\/$/;
    if (pathNameAge.test(thisPathName)) {
        thisPathName = thisPathName.replace(pathNameAge, "/" + googleLotteryType + "/");
        _gaq.push(['_trackPageview', thisPathName + window.location.search]);
    } else if (pathHtmlAge.test(thisPathName)) {
        _gaq.push(['_trackPageview']);
    } else {
        _gaq.push(['_trackPageview', thisPathName + '/']);
    }
}
(function() {
    var ga = document.createElement('script');
    ga.type = 'text/javascript';
    ga.async = true;
    ga.src = (googlegaurl + "/JS/public/ga.js?v=" + googleversion);
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(ga, s);
})(); if (window.addEventListener) {
    window.addEventListener("load", function() {
        googleinit(2);
    }, false);
} else {
    window.attachEvent("onload", function() {
        googleinit(2)
    });
}
var currenturl = gettimeurl();

function googleinit(id) {
    var googledataobje = new Date();
    var googleendtime = googledataobje.getTime();
    try {
        var oldtime = googleendtime - googlestarttime;
    } catch (ex) {
        return false;
    }
    id = isNaN(id) ? 1 : id;
    var newpagename;
    if (oldtime < 0 || oldtime > 100000) {
        newpagename = "errorPageLoadTime";
    } else {
        newpagename = "PageLoadTime";
    }
    if (id == 1) {
        _gaq.push(['_trackTiming', newpagename, 'DOMContentloaded', oldtime, currenturl, 100]);
    } else if (id == 2) {
        _gaq.push(['_trackTiming', newpagename, 'onload', oldtime, currenturl, 100]);
    }
}

function gettimeurl() {
    var hrefurl = window.location.href;
    var proname = location.protocol;
    hrefurl = hrefurl.substr(proname.length + 2);
    hrefurl = hrefurl.replace(/.*\.qq\.okooo\.com/, "all.qq.okooo.com");
    if (hrefurl.indexOf("?") > -1) {
        var hrefarr = hrefurl.split("?");
        hrefarr[0] = replaceStr(hrefarr[0]);
        if (hrefarr[1].indexOf("LotteryType=") > -1) {
            var hrefget = hrefarr[1].split("LotteryType=");
            if (hrefget[1].indexOf("&") > -1) {
                var LotteryTypeval = hrefget[1].split("&");
                return hrefarr[0] + "?LotteryType=" + LotteryTypeval[0];
            } else {
                return hrefarr[0] + "?LotteryType=" + hrefget[1];
            }
        } else {
            return hrefarr[0];
        }
    } else {
        return replaceStr(hrefurl);
    }
}

function replaceStr(str) {
    var age = /\/\d+\//g;
    while (age.test(str)) {
        str = str.replace(age, "/no/");
    }
    var age = /\-\d+\-\d+\-\d+\./g;
    while (age.test(str)) {
        str = str.replace(age, "-no-no-no.");
    }
    var age = /\/\d+\./g;
    while (age.test(str)) {
        str = str.replace(age, "/no.");
    }
    return str;
}
if (document.addEventListener) {
    document.addEventListener("DOMContentLoaded", googleinit, false);
} else {
    if (document.documentElement.doScroll)
        (function() {
            try {
                document.documentElement.doScroll("left");
                googleinit(1)
            } catch (error) {
                setTimeout(arguments.callee, 0);
                return;
            }
        })();
}

function google_p(data, type, user) {
    user = user ? user + "." : "";
    type = type ? type : "_trackEvent";
    if (!data) {
        return true;
    }
    for (var i in data) {
        if (typeof(data[i]) == "string") {
            data[i] = "'" + data[i] + "'";
        } else {
            data[i] = Number(data[i]);
        }
    }
    if (typeof(GoogleTJKG) == "undefined" || GoogleTJKG) {
        eval("_gaq.push(['" + user + type + "'," + data.join(",") + "])");
    }
}

function ajax_google_Tj_fun($) {
        if (typeof(ajax_google_KG) == "undefined" || ajax_google_KG) {
            $().ajaxSend(function(evt, request, settings) {
                var startObj = new Date();
                settings.google_start_time = startObj.getTime();
                settings.google_start_type = false;
                settings.google_satrt_val = true;
            });
            $().ajaxSuccess(function(evt, request, settings) {
                settings.google_start_type = true;
            });
            $().ajaxComplete(function(evt, request, settings) {
                if (!settings.google_satrt_val) return false;
                settings.google_satrt_val = false;
                var startTime = settings.google_start_time;
                var endObj = new Date();
                var endTime = endObj.getTime();
                var urlVal = settings.url;
                var arg = /\d+/g;
                while (arg.test(urlVal)) {
                    urlVal = urlVal.replace(arg, "x");
                }
                if (settings.google_start_type) {
                    google_p(["ajaxTime", "ajaxSucc", endTime - startTime, urlVal, 20], "_trackTiming");
                } else {
                    google_p(["ajaxTime", "ajaxError", endTime - startTime, urlVal, 20], "_trackTiming");
                }
            });
        }
    }
    (function($) {
        if (!$) {
            var jQueryScriptObj = document.createElement('script');
            jQueryScriptObj.type = 'text/javascript';
            jQueryScriptObj.async = true;
            jQueryScriptObj.src = (googlegaurl + "/JS/jquery/jquery.js?v=2012092701");
            jQueryScriptObj.onloadDone = false;
            jQueryScriptObj.onload = function() {
                jQueryScriptObj.onloadDone = true;
                if (!$.noConflict) {
                    jQuery.noConflict();
                }
                ajax_google_Tj_fun(jQuery);
            }
            jQueryScriptObj.onreadystatechange = function() {
                if (("loaded" === jQueryScriptObj.readyState || "complete" === jQueryScriptObj.readyState) && !jQueryScriptObj.onloadDone) {
                    jQueryScriptObj.onloadDone = false;
                    if (!$.noConflict) {
                        jQuery.noConflict();
                    }
                    ajax_google_Tj_fun(jQuery);
                }
            }
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(jQueryScriptObj, s);
        } else {
            ajax_google_Tj_fun(jQuery);
        }
    })(typeof(jQuery) == "undefined" ? false : true);;
(function(jsCombineTag, url, root, fileType) {
    this.onloadScriptFile = function(fileUrl, callBack) {
        var scriptObj = document.createElement("script");
        scriptObj.src = fileUrl + ".js";
        scriptObj.async = true;
        scriptObj.type = "text/javascript";
        scriptObj.onloadDone = false;
        scriptObj.onload = function() {
            scriptObj.onloadDone = true;
            callBack();
        }
        scriptObj.onreadystatechange = function() {
            if (("loaded" === scriptObj.readyState || "complete" === scriptObj.readyState) && !scriptObj.onloadDone) {
                scriptObj.onloadDone = true;
                callBack();
            }
        }
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(scriptObj, s);
    }
    if (jsCombineTag) {
        var ok_a = {};
        var ok_url = url + "/min/?b=" + root + "&f=config.js,controller%2Findex.js&v=" + confingVersion;
        this.onloadScriptFile(ok_url, function() {
            ok_a = new controller_index({
                hostName: url,
                root: root,
                fileType: fileType,
                jsCombineTag: jsCombineTag
            });
            for (var ok_i = 0, ok_ilen = ok_controller.length; ok_i < ok_ilen; ok_i++) {
                ok_a.add_m(ok_controller[ok_i]);
            }
            for (var ok_i = 0, ok_ilen = ok_controllerFile.length; ok_i < ok_ilen; ok_i++) {
                ok_a.add_f(ok_controllerFile[ok_i]);
            }
            ok_controller = [];
            ok_controllerFile = [];
        });
    } else {
        var ok_v_url = url + "/" + root + "/config.js?v=" + (confingVersion ? confingVersion : new Date().getTime());
        var ok_m_url = url + "/" + root + "/controller/index.js?v=" + (confingVersion ? confingVersion : new Date().getTime());
        var ok_a = {};
        this.onloadScriptFile(ok_v_url, function() {
            this.onloadScriptFile(ok_m_url, function() {
                ok_a = new controller_index({
                    hostName: url,
                    root: root,
                    fileType: fileType
                });
                for (var ok_i = 0, ok_ilen = ok_controller.length; ok_i < ok_ilen; ok_i++) {
                    ok_a.add_m(ok_controller[ok_i]);
                }
                for (var ok_i = 0, ok_ilen = ok_controllerFile.length; ok_i < ok_ilen; ok_i++) {
                    ok_a.add_f(ok_controllerFile[ok_i]);
                }
                ok_controller = [];
                ok_controllerFile = [];
            });
        });
    }
    var ok_controller = [];
    var ok_controllerFile = [];
    var ok_m_fun = function() {
        var o = this;
        this.add_m = function() {
            if (typeof(ok_a.add_m) === "undefined") {
                ok_controller.push(arguments);
            } else {
                ok_a.add_m(arguments);
            }
        }
        this.add_f = function() {
            if (typeof(ok_a.add_f) === "undefined") {
                ok_controllerFile.push(arguments);
            } else {
                ok_a.add_f(arguments);
            }
        }
        this.inArr = function(val, arr) {
            for (var i = 0, ilen = arr.length; i < ilen; i++) {
                if (val === arr[i]) {
                    return i
                }
            }
            return -1;
        }
        this.getArg = function(arg, type) {
            var newarg = [];
            for (var i = 0, ilen = arg.length; i < ilen; i++) {
                if (this.inArr(arg[i], fileType) > -1) {
                    return arg;
                }
                newarg.push(arg[i]);
            }
            newarg.push(type);
            return newarg;
        }
        this.add_c = function() {
            o.add_m.apply(this, o.getArg(arguments, "c"));
        }
        this.add_p = function() {
            var type = ["p"];
            o.add_m.apply(this, o.getArg(arguments, "p"));
        }
        this.add_pc = function() {
            var type = ["pc"];
            o.add_m.apply(this, o.getArg(arguments, "pc"));
        }
    }
    var ok_M = new ok_m_fun();
    this.ok_m = ok_M.add_m;
    this.ok_c = ok_M.add_c;
    this.ok_p = ok_M.add_p;
    this.ok_f = ok_M.add_f;
})(jsCombineTag, StaticUrl, "JS", ["m", "c", "p"]);;
(function($) {
    $.extend({
        loadMsgDialog: function(options) {
            var settings = $.extend({
                title: '提示',
                id: 'msg_dialog',
                data: ''
            }, options);
            var self = $('#' + settings.id);
            if (self.length) {
                self.remove();
            }
            var dialogStr = '<div id="' + settings.id + '" class="ok_dialog">' + '<h3>' + settings.title + '</h3><a href="javascript:void(0);" class="close">X</a>' + '<div class="ok_dialog_content">' + settings.data + '</div>' + '<div class="ok_dialog_button"></div>' + '</div>';
            $('body').append(dialogStr);
            self = $('#' + settings.id);
            $('.close', self).click(function() {
                self.loadDialog('close');
            });
            if (settings.buttons) {
                var butt = settings.buttons;
                var buttonListObj = $('.ok_dialog_button', self);
                $.each(butt, function(i, e) {
                    (function(handler) {
                        $('<a>').attr('href', 'javascript:void(0);').css('margin-right', '4px').text($.msgButtonCh[i]).appendTo(buttonListObj).bind('click', function(event) {
                            return handler.apply(self);
                        });
                    })(butt[i]);
                });
                buttonListObj.find('a:first').css('margin-right', '0px');
            }
            self.loadDialog(options);
        },
        msgButtonCh: {
            'sure': '确认',
            'close': '关闭',
            'cancel': '取消'
        },
        okSlider: function(contentid, pageid, time) {
            var sliderObj = $(contentid + ' li');
            var sliderNum = sliderObj.length;
            var pagehtml = '';
            for (var i = 1; i <= sliderNum; i++) {
                pagehtml += '<a href="javascript:void(0);">' + i + '</a>';
            }
            pagehtml += '<div class="clear"></div>';
            $(pageid).html(pagehtml).width(36 * sliderNum);
            window.okSliderCount = 0;
            sliderObj.eq(window.okSliderCount).show();
            $(pageid + ' a:eq(' + window.okSliderCount + ')').addClass('sliderpagesel');
            var okSliderMain = function() {
                window.okSliderTime = setTimeout(function() {
                    sliderObj.eq(window.okSliderCount).fadeOut(time / 3, function() {
                        window.okSliderCount += 1;
                        if (window.okSliderCount >= sliderNum) {
                            window.okSliderCount = 0;
                        };
                        sliderObj.eq(window.okSliderCount).fadeIn(time / 3);
                        $('.sliderpagesel', $(pageid)).removeClass('sliderpagesel');
                        $(pageid + ' a:eq(' + window.okSliderCount + ')').addClass('sliderpagesel');
                        window.okSliderCallMain = setTimeout(function() {
                            okSliderMain();
                        }, time);
                    });
                }, time);
            };
            okSliderMain();
            $(pageid + ' a').mouseenter(function() {
                var nowpage = Number($(this).text());
                window.okSliderCount = nowpage - 1;
                clearTimeout(window.okSliderTime);
                clearTimeout(window.okSliderCallMain);
                $('.sliderpagesel', $(pageid)).removeClass('sliderpagesel');
                $(this).addClass('sliderpagesel');
                sliderObj.hide().eq(nowpage - 1).show();
            }).mouseleave(function() {
                okSliderMain();
            });
        },
        okPicSlider: function(options) {
            clearTimeout(window.picSliderTime);
            var settings = $.extend({
                picWidth: 205,
                picHeight: 330,
                picNum: 5,
                zIndexVal: 200,
                leftPre: 3.5,
                topPre: 0.15,
                whPre: 0.8,
                time: 4000
            }, options);
            var self = this;
            var picsliderobj = $('#' + settings.id);
            if (picsliderobj.data('htmlstr')) {
                picsliderobj.html(picsliderobj.data('htmlstr'));
            } else {
                picsliderobj.data('htmlstr', picsliderobj.html());
            }
            var width = picsliderobj.innerWidth();
            var middleleft = width / 2 - settings.picWidth / 2;
            var pagechangeobj = $('div.changepic_btn', picsliderobj);
            pagechangeobj.css('left', width / 2 - pagechangeobj.innerWidth() / 2);
            var heightWidthMapping = [
                    [Math.floor(settings.picWidth * settings.whPre), Math.floor(settings.picHeight * settings.whPre)],
                    [Math.floor(settings.picWidth * (Math.pow(settings.whPre, 2))), Math.floor(settings.picHeight * (Math.pow(settings.whPre, 2)))]
                ],
                leftMapping = [
                    [Math.floor(middleleft - heightWidthMapping[0][0] / settings.leftPre), Math.floor(middleleft + settings.picWidth - (heightWidthMapping[0][0] / settings.leftPre) * (settings.leftPre - 1))],
                    [Math.floor(middleleft - (heightWidthMapping[1][0] / settings.leftPre) * 2), Math.floor(middleleft + settings.picWidth - (heightWidthMapping[0][0] / settings.leftPre) * (settings.leftPre - 1) + heightWidthMapping[0][0] - (heightWidthMapping[1][0] / settings.leftPre) * (settings.leftPre - 1))]
                ],
                topMapping = [Math.floor(settings.picHeight * settings.topPre), Math.floor(settings.picHeight * (settings.topPre * 2))],
                zIndexMapping = [settings.zIndexVal - 10, settings.zIndexVal - 20];
            $('img', picsliderobj).each(function(i, e) {
                $(e).width(settings.picWidth);
                $(e).height(settings.picHeight);
                $(e).parent().attr('pagenum', String(i));
            });
            var middlePicIndex = (settings.picNum - 1) / 2;
            self.init = function() {
                $('div.ctrl_pic:eq(' + middlePicIndex + ')', picsliderobj).css({
                    'left': middleleft + 'px',
                    'top': '0px',
                    'zIndex': settings.zIndexVal
                });
                var arrIndex = 0;
                var picIndex = 1;
                for (var i = middlePicIndex; i > 0; i--) {
                    var index = i - 1;
                    $('div.ctrl_pic:eq(' + index + ')', picsliderobj).css({
                        'left': leftMapping[arrIndex][0] + 'px',
                        'top': topMapping[arrIndex] + 'px',
                        'zIndex': zIndexMapping[arrIndex]
                    }).find('img').css({
                        'width': heightWidthMapping[arrIndex][0] + 'px',
                        'height': heightWidthMapping[arrIndex][1] + 'px'
                    });
                    $('div.ctrl_pic:eq(' + (index + picIndex * 2) + ')', picsliderobj).css({
                        'left': leftMapping[arrIndex][1] + 'px',
                        'top': topMapping[arrIndex] + 'px',
                        'zIndex': zIndexMapping[arrIndex]
                    }).find('img').css({
                        'width': heightWidthMapping[arrIndex][0] + 'px',
                        'height': heightWidthMapping[arrIndex][1] + 'px'
                    });
                    arrIndex += 1, picIndex += 1;
                }
                $('.ctrl_pic', picsliderobj).click(function() {
                    clearTimeout(window.picSliderTime);
                    var pagenum = $(this).attr('pagenum');
                    self.changPicSelected(pagenum);
                    self.countIndex = Number(pagenum) + 1;
                    self.changePic($(this));
                    self.play();
                });
                $('a', pagechangeobj).click(function() {
                    clearTimeout(window.picSliderTime);
                    var pagenum = $(this).attr('pagenum');
                    self.changPicSelected(pagenum);
                    self.countIndex = Number(pagenum) + 1;
                    var dobj = $('div.ctrl_pic:eq(' + pagenum + ')', picsliderobj);
                    self.changePic(dobj);
                    self.play();
                });
            };
            self.changePic = function(sobj) {
                if (typeof sobj.attr('ctrlmiddle') != 'undefined') {
                    return;
                }
                var tobj = $('div.ctrl_pic[ctrlmiddle=1]', picsliderobj);
                sobj.attr('ctrlmiddle', 1);
                tobj.removeAttr('ctrlmiddle');
                var swidth = $('img', sobj).width(),
                    sheight = $('img', sobj).height(),
                    sleftval = sobj.css('left'),
                    stopval = sobj.css('top'),
                    szindex = sobj.css('zIndex');
                sobj.find('img').animate({
                    width: settings.picWidth,
                    height: settings.picHeight
                });
                sobj.css('zIndex', settings.zIndexVal).animate({
                    top: 0,
                    left: middleleft
                });
                tobj.find('img').animate({
                    width: swidth,
                    height: sheight
                });
                tobj.css('zIndex', szindex).animate({
                    top: stopval,
                    left: sleftval
                });
            };
            self.countIndex = 0;
            self.play = function() {
                window.picSliderTime = setTimeout(function() {
                    if (self.countIndex > 4) {
                        self.countIndex = 0;
                    }
                    var curobj = $('div.ctrl_pic:eq(' + self.countIndex + ')', picsliderobj);
                    self.changePic(curobj);
                    self.changPicSelected(self.countIndex);
                    self.countIndex += 1;
                    self.play();
                }, settings.time);
            };
            self.changPicSelected = function(num) {
                $('.selected', pagechangeobj).removeClass('selected');
                $('a:eq(' + num + ')', pagechangeobj).addClass('selected');
            };
            self.init();
            self.play();
        }
    });
    $.fn.extend({
        loadDialog: function(options, closeoverlay) {
            if (options == 'close') {
                $(this).hide();
                if (!closeoverlay && $('#dialog_overlay').length) {
                    $('#dialog_overlay').remove();
                }
                return;
            }
            var setting = $.extend({
                opacity: 0.3,
                overlay: true,
                create: false
            }, options);
            var self = '';
            var winWidth = $(window).width(),
                winHeight = $(window).height(),
                scrLeft = $(window).scrollLeft(),
                scrTop = $(window).scrollTop();
            if (setting.create) {} else {
                self = $(this);
            }
            if (setting.overlay && !$('#dialog_overlay').length) {
                $('<div>').attr('id', 'dialog_overlay').css('opacity', setting.opacity).appendTo('body');
                ok_p("bgiframe", function() {
                    $('#dialog_overlay').bgiframe();
                });
            }
            $('.close', self).click(function() {
                self.loadDialog('close');
            });
            self.addClass('dialog_layer');
            var out_width = parseInt(self.width());
            out_height = parseInt(self.height());
            var wl = parseInt((winWidth - out_width) / 2);
            if (winHeight > out_height) {
                var t = (scrTop + (winHeight - out_height) / 2);
            } else {
                var t = (scrTop + (winHeight / 10));
            }
            self.css({
                'left': wl,
                'top': t
            }).show();
        },
        bdHover: function(hover) {
            this.hover(function() {
                var css = hover || $(this).attr('hover') || 'hover';
                $(this).addClass(css);
            }, function() {
                var css = hover || $(this).attr('hover') || 'hover';
                $(this).removeClass(css);
            });
            return this;
        }
    });
})(jQuery);

function okMsgDialog(data, lay) {
    $.loadMsgDialog({
        data: data,
        overlay: lay ? false : true,
        buttons: {
            'close': function() {
                $(this).loadDialog('close', lay);
            }
        }
    });
    google_p(["/virtual/n/" + (GetCookie('IMUserID') ? GetCookie('IMUserID') : '') + "/u/" + (GetCookie('IMUserName') ? GetCookie('IMUserName') : '') + "/s/" + data + "/"], "_trackPageview", "a1");
    google_p([window.location.pathname, window.location.hostname, data], false, "a2");
}

function navShowSubMenu(parentobj, obj) {
    $('.menu_hover', parentobj).removeClass('menu_hover');
    $('.menu_hover2', parentobj).removeClass('menu_hover2');
    obj.parent().addClass('show_second');
    obj.addClass('menu_hover');
    $('.hd_sub_menu', $('#lottery_type_downmenu')).mouseleave(function(e) {
        if (!$(e.relatedTarget).hasClass('nav_sub_pointer')) {
            $('.show_second', $('#lottery_type_downmenu')).removeClass('show_second');
            $('.menu_hover', $('#lottery_type_downmenu')).removeClass('menu_hover');
        }
    });
}
var CustomMenu = {
    lotteryCharObj: {
        'SSQ': {
            'name': '双色球',
            'className': 'nav_ssq',
            'url': LoginDomain + '/shuangseqiu/',
            'prize': true
        },
        '3D': {
            'name': '3D',
            'className': 'nav_3d',
            'url': LoginDomain + '/3d/'
        },
        '7LC': {
            'name': '七乐彩',
            'className': 'nav_7lc',
            'url': LoginDomain + '/qilecai/'
        },
        'SuperLotto': {
            'name': '大乐透',
            'className': 'nav_dlt',
            'url': LoginDomain + '/daletou/'
        },
        'P3': {
            'name': '排列3',
            'className': 'nav_p3',
            'url': LoginDomain + '/p3/'
        },
        'P7': {
            'name': '七星彩',
            'className': 'nav_p7',
            'url': LoginDomain + '/qixingcai/'
        },
        'P5': {
            'name': '排列5',
            'className': 'nav_p5',
            'url': LoginDomain + '/p5/'
        },
        'WDL': {
            'name': '单场胜平负',
            'className': 'nav_danchang',
            'url': LoginDomain + '/danchang/'
        },
        'Score': {
            'name': '单场比分',
            'className': 'nav_danchang',
            'url': LoginDomain + '/danchang/bifen/'
        },
        'TotalGoals': {
            'name': '单场总进球',
            'className': 'nav_danchang',
            'url': LoginDomain + '/danchang/jinqiu/'
        },
        'HalfFull': {
            'name': '单场半全场',
            'className': 'nav_danchang',
            'url': LoginDomain + '/danchang/banquan/'
        },
        'OverUnder': {
            'name': '单场上下单双',
            'className': 'nav_danchang',
            'url': LoginDomain + '/danchang/danshuang/'
        },
        'ToTo': {
            'name': '足彩胜负彩',
            'className': 'nav_zucai',
            'url': LoginDomain + '/zucai/'
        },
        'NineToTo': {
            'name': '足彩任选九',
            'className': 'nav_zucai',
            'url': LoginDomain + '/zucai/ren9/'
        },
        'WCFourGoal': {
            'name': '足彩进球彩',
            'className': 'nav_zucai',
            'url': LoginDomain + '/zucai/jinqiu/'
        },
        'WCSixHalfToTo': {
            'name': '足彩六场半全',
            'className': 'nav_zucai',
            'url': LoginDomain + '/zucai/liuban/'
        },
        'SportteryWDL': {
            'name': '竞彩足球胜平负',
            'className': 'nav_jingcai',
            'url': LoginDomain + '/jingcai/'
        },
        'SportteryDanScore': {
            'name': '竞彩足球单关比分',
            'className': 'nav_jingcai',
            'url': LoginDomain + '/jingcai/danguanbf/'
        },
        'SportteryTotalGoals': {
            'name': '竞彩足球总进球',
            'className': 'nav_jingcai',
            'url': LoginDomain + '/jingcai/jinqiu/'
        },
        'SportteryScore': {
            'name': '竞彩足球过关比分',
            'className': 'nav_jingcai',
            'url': LoginDomain + '/jingcai/bifen/'
        },
        'SportteryHalfFull': {
            'name': '竞彩足球半全场',
            'className': 'nav_jingcai',
            'url': LoginDomain + '/jingcai/banquan/'
        },
        'SportteryTopOne': {
            'name': '竞彩足球猜冠军',
            'className': 'nav_jingcai',
            'url': LoginDomain + '/jingcai/caiguanjun/'
        },
        'SportteryHWL': {
            'name': '竞彩篮球让分胜负',
            'className': 'nav_lancai',
            'url': LoginDomain + '/jingcailanqiu/rangfen/'
        },
        'SportteryDanWS': {
            'name': '竞彩篮球单关胜分差',
            'className': 'nav_lancai',
            'url': LoginDomain + '/jingcailanqiu/danguansfc/'
        },
        'SportteryBS': {
            'name': '竞彩篮球大小分',
            'className': 'nav_lancai',
            'url': LoginDomain + '/jingcailanqiu/daxiaofen/'
        },
        'SportteryWS': {
            'name': '竞彩篮球过关胜分差',
            'className': 'nav_lancai',
            'url': LoginDomain + '/jingcailanqiu/shengfencha/'
        },
        'SportteryWL': {
            'name': '竞彩篮球胜负',
            'className': 'nav_lancai',
            'url': LoginDomain + '/jingcailanqiu/'
        },
        'SYY': {
            'name': '十一运夺金',
            'className': 'nav_syy',
            'url': LoginDomain + '/syy/'
        },
        'SSC': {
            'name': '时时彩',
            'className': 'nav_ssc',
            'url': LoginDomain + '/shishicai/'
        },
        'XJSYY': {
            'name': '11选5',
            'className': 'nav_xjsyy',
            'url': LoginDomain + '/11x5/'
        },
        'KL8': {
            'name': '快乐8',
            'className': 'nav_kl8',
            'url': LoginDomain + '/kl8/'
        },
        'KL10': {
            'name': '快乐十分',
            'className': 'nav_kl10',
            'url': LoginDomain + '/klsf/'
        },
        'PK10': {
            'name': 'PK10',
            'className': 'nav_pk10',
            'url': LoginDomain + '/pk10/'
        },
        'GDSYY': {
            'name': '广东11选5',
            'className': 'nav_gdsyy',
            'url': LoginDomain + '/gd11x5/'
        }
    },
    init: function() {
        var self = this;
        if (!GetCookie('userCustomLottery')) {
            var LoginStatus = GetCookie('LStatus');
            if (LoginStatus != 'N') {
                $.ajax({
                    'url': '/Remoting/json.php/UserService.getRecentLottery',
                    'data': '',
                    'type': 'GET',
                    'dataType': 'json',
                    'success': function(data) {
                        var obj = data;
                        if (obj.code == 1 && obj.data.length > 0) {
                            setCustomLotteryCookie(obj.data);
                            self.getCustomMenuHtml(obj.data);
                            if (GetCookie('showCustomMenu') == 1) {
                                self.switchCusMenu(1);
                            } else {
                                self.switchCusMenu(0);
                            }
                        } else {
                            self.switchCusMenu(0);
                        }
                    },
                    'error': function() {
                        self.switchCusMenu(0);
                    }
                })
            } else {
                self.switchCusMenu(0);
            }
        } else {
            var cookieStr = GetCookie('userCustomLottery');
            self.getCustomMenuHtml(cookieStr);
            if (GetCookie('showCustomMenu') == 1) {
                self.switchCusMenu(1);
            } else {
                self.switchCusMenu(0);
            }
        }
        $('#defMenuBtn').unbind().click(function() {
            self.switchCusMenu(0);
        })
        $('#customMenuBtn').unbind().click(function() {
            self.switchCusMenu(1);
        })
    },
    switchCusMenu: function(type) {
        if (type == 1) {
            if (GetCookie('userCustomLottery')) {
                SetCookie('showCustomMenu', 1, 100 * 24 * 60 * 60, '/');
            }
            $('#jsCustomMenu').show();
            $('#jsDefMenu').hide();
            $('#defMenuBtn').removeClass('selected');
            $('#customMenuBtn').addClass('selected');
        } else {
            if (GetCookie('userCustomLottery')) {
                SetCookie('showCustomMenu', 2, 100 * 24 * 60 * 60, '/');
            }
            $('#jsCustomMenu').hide();
            $('#jsDefMenu').show();
            $('#defMenuBtn').addClass('selected');
            $('#customMenuBtn').removeClass('selected');
        }
    },
    getCustomMenuHtml: function(cookieStr) {
        var arr = cookieStr.split(',');
        if (arr.length > 5) {
            arr = arr.splice(0, 5);
        }
        var htmlStr = '';
        for (var i = 0, len = arr.length; i < len; i++) {
            var itm = arr[i];
            if (!this.lotteryCharObj[itm]) continue;
            var jImg = this.lotteryCharObj[itm]['prize'] ? ' <img border="0" alt="加奖" src="' + StaticUrl + '/image/jiajiangup.gif" class="jiajimg" />' : '';
            htmlStr += '<div class="lotterybox_bg bottomline_bg">' + '<div class="lotterybox_bg_inner" >' + '<span class="navtit ' + this.lotteryCharObj[itm]['className'] + '"></span>' + '<div class="lottery_subtype_my" >' + '<a href="' + this.lotteryCharObj[itm]['url'] + '" ' + ' title="' + this.lotteryCharObj[itm]['name'] + '">' + this.lotteryCharObj[itm]['name'] + jImg + '</a>' + '</div>' + '<div class="clear Clear"></div>' + '</div>' + '</div>';
        }
        $('#jsCustomMenu').children('.lotterybox_bg').not('#jsAllLotMenuType').remove();
        $('#jsCustomMenu').prepend(htmlStr);
    }
}

function setCustomLotteryCookie(type) {
    if (!type) return;
    var oldCookie = GetCookie('userCustomLottery');
    if (!oldCookie) {
        var str = type;
        SetCookie('userCustomLottery', str, 100 * 24 * 60 * 60, '/');
    } else {
        var nameArr = oldCookie.split(',');
        var idx = $.inArray(type, nameArr);
        if (idx != -1) {
            nameArr.splice(idx, 1);
            nameArr.unshift(type);
        } else {
            nameArr.unshift(type);
        }
        if (nameArr.length > 5) {
            nameArr = nameArr.splice(0, 5);
        }
        var newStr = '';
        newStr = nameArr.join(',');
        SetCookie('userCustomLottery', newStr, 100 * 24 * 60 * 60, '/');
    }
}

function cnLength(str) {
    var escStr = escape(str);
    var numI = 0;
    var escStrlen = escStr.length;
    for (i = 0; i < escStrlen; i++)
        if (escStr.charAt(i) == '%')
            if (escStr.charAt(++i) == 'u')
                numI++;
    return str.length + numI;
}

function checkUserNameEffect(val) {
    var checkResult = {};
    checkResult.ret = true;
    checkResult.msg = '';
    if (cnLength(val) < conf_okooo.userNameMinLength || cnLength(val) > conf_okooo.userNameMaxLength) {
        checkResult.ret = false;
        checkResult.msg = '用户名长度应为' + conf_okooo.userNameMinLength + '-' + conf_okooo.userNameMaxLength + '位字符！';
        return checkResult;
    }
    var isRight = true;
    if (/\s+/g.test(val)) isRight = false;
    var pattern = new RegExp("[`~!@#$\\%^&*()\\-\\+=|{}':;',\\[\\].<>/?~！@#￥……&*（）?|{}【】‘；：”“'。，、？]")
    if (isRight) {
        for (var i = 0; i < val.length; i++) {
            if (!isRight) break;
            if (pattern.test(val.substr(i, 1))) isRight = false;
        }
    }
    if (!isRight) {
        checkResult.ret = false;
        checkResult.msg = '只能由字母、数字、下划线或汉字组成！';
        return checkResult;
    } else {
        return checkResult;
    }
}

function checkUserEmail(val) {
    var checkResult = {};
    checkResult.ret = true;
    checkResult.msg = '';
    var reg = /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if (!val.match(reg)) {
        checkResult.ret = false;
        checkResult.msg = '您输入的邮箱地址不合法，请重新输入！';
        return checkResult;
    }
    if (val.length > 32) {
        checkResult.ret = false;
        checkResult.msg = '邮箱地址最大长度为32个字符！';
        return checkResult;
    }
    return checkResult;
}

function getInviteNum(type) {
    var type = 'winner';
    $.get('/Remoting/json.php/UserService.getInviteLink/' + type, null, function(res) {
        var res = eval('(' + res + ')');
        if (res.code > 0 && res.num > 0) {
            $('#invite_friend_btn').show().text('邀请(' + res.num + ')');
        }
    });
}

function showInviteDialog(type) {
    var type = 'winner';
    $.get('/Remoting/json.php/UserService.getInviteLink/' + type, null, function(res) {
        var res = eval('(' + res + ')');
        if (res.code > 0) {
            var links = '',
                i = 1;
            for (var key in res.msg) {
                links += '<p>' + i + '、' + res.msg[key] + '</p>';
                i += 1;
            }
            if ($('#invite_dialog').length) {
                $('#invite_link_num').text(res.num);
                $('#invite_link_list').html(links);
                $('#invite_dialog').loadDialog();
            } else {
                var str = '<div id="invite_dialog" class="yaoqing_ceng">' + '<div class="title">' + '<a class="close" href="javascript:void(0);" id="login_close">X</a>' + '</div>' + '<h2>邀请好友来使用此功能（<span id="invite_link_num">' + res.num + '</span>个名额）：</h2>' + '<p>请复制下面邀请链接发给您的好友，您的好友点击链接激活后便可使用该功能！</p>' + '<div id="invite_link_list" class="scroll_box">' + links + '</div>' + '</div>';
                $('body').append(str);
                $('#invite_dialog').loadDialog();
            }
        } else {
            okMsgDialog(res.msg);
        }
    });
}

function GetHost() {
    var host = window.location.host;
    if (host) return host;
    else return '';
}

function checkUserPassword(val) {
    var checkResult = {};
    checkResult.ret = true;
    checkResult.msg = '';
    if (cnLength(val) < conf_okooo.loginPassMinLength || cnLength(val) > conf_okooo.loginPassMaxLength) {
        checkResult.ret = false;
        checkResult.msg = '密码长度应为' + conf_okooo.loginPassMinLength + '-' + conf_okooo.loginPassMaxLength + '位字符！';
        return checkResult;
    }
    var isRight = true;
    if (/\s+/.test(val)) isRight = false;
    if (/[^a-zA-Z0-9!@#$%^&*()]/.test(val)) isRight = false;
    if (!isRight) {
        checkResult.ret = false;
        checkResult.msg = '密码只能由字母、数字和字符 !@#$%^&*() 组成！';
        return checkResult;
    } else {
        var len = val.length;
        var strone = val.substr(0, 1);
        var strtmp = [];
        for (var i = 0; i < len; i++) {
            strtmp[i] = strone;
        }
        var strtmpstr = strtmp.join('');
        if (strtmpstr == val || is_countinues_num(val)) {
            checkResult.ret = false;
            checkResult.msg = '密码太简单，请尝试数字、字母和特殊字符的组合！';
            return checkResult;
        } else {
            return checkResult;
        }
    }
}
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

function copytext(text) {
    var f = navigator.userAgent.toLowerCase();
    var g = f.indexOf("opera") != -1 && opera.version();
    var h = f.indexOf("msie") != -1 && !g && f.substr(f.indexOf("msie") + 5, 3);
    if (!h) {
        prompt("您使用的是非IE核心浏览器，请按下 Ctrl+C 复制代码到剪贴板", text);
    } else {
        clipboardData.setData('Text', text);
        alert("复制成功，可以直接粘贴在相应位置");
    }
}

function GetCookieVal(offset) {
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1)
        endstr = document.cookie.length;
    return unescape(document.cookie.substring(offset, endstr));
}

function SetCookie(name, value) {
    var expdate = new Date();
    var argv = SetCookie.arguments;
    var argc = SetCookie.arguments.length;
    var expires = (argc > 2) ? argv[2] : null;
    var path = (argc > 3) ? argv[3] : null;
    var domain = (argc > 4) ? argv[4] : null;
    var secure = (argc > 5) ? argv[5] : false;
    if (expires == null) expires = 3600;
    expdate.setTime(expdate.getTime() + (expires * 1000));
    document.cookie = name + "=" + escape(value) + ((expires == null) ? "" : ("; expires=" + expdate.toGMTString())) + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure == true) ? "; secure" : "");
}

function GetCookie(name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg)
            return GetCookieVal(j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return '';
}

function is_countinues_num(val) {
    var len = val.length;
    var ascarr = [];
    for (var i = 0; i < len; i++) {
        var ascval = val.substr(i, 1).charCodeAt(0);
        ascarr[i] = ascval;
    }
    if (ascarr.length == 0) return false;
    for (var i = 0; i < ascarr.length - 1; i++) {
        if (/^\d+$/.test(ascarr[i])) {
            if (Math.abs(ascarr[i + 1] - ascarr[i]) !== 1) return false;
        } else {
            return false;
        }
    }
    return true;
}
setTimeout(function() {
    loadWebsocketFile();
}, 3 * 1000);

function onloadScriptFile(fileUrl, callBack) {
    var scriptObj = document.createElement("script");
    scriptObj.src = fileUrl + ".js";
    scriptObj.async = true;
    scriptObj.type = "text/javascript";
    scriptObj.onloadDone = false;
    scriptObj.onload = function() {
        scriptObj.onloadDone = true;
        callBack();
    }
    scriptObj.onreadystatechange = function() {
        if (("loaded" === scriptObj.readyState || "complete" === scriptObj.readyState) && !scriptObj.onloadDone) {
            scriptObj.onloadDone = true;
            callBack();
        }
    }
    document.getElementsByTagName("body")[0].appendChild(scriptObj);
}

function loadWebsocketFile() {
    if (GetCookie('LStatus') != 'N' && window.Client_UserID) {
        var sPhpSessionId = GetCookie('PHPSESSID'),
            sRoomName = 'pm_' + window.Client_UserID;
        if (typeof(js_version) == "undefined") {
            js_version = new Date().getTime();
        }
        if (typeof WEB_SOCKET_SWF_LOCATION == 'undefined') {
            onloadScriptFile(StaticUrl + '/min/?b=JS&f=web_socket%2Fswfobject.js,web_socket%2Fweb_socket.js,socket_connect%2Fsocket_connect.js&v=' + js_version, function() {});
        } else if (window.WebSocket && typeof WEB_SOCKET_SWF_LOCATION != 'undefined') {
            onloadScriptFile(StaticUrl + '/min/?b=JS&f=WSPMMessage.js&v=' + js_version, function() {});
        }
    }
}

function refreshProjectInfoLayerData(res) {
    res = eval('(' + res + ')');
    if (res.raw == "prize_review") {
        if (typeof(updateProject) != "undefined" && typeof(updateProject) == "function") updateProject(res.data);
        return false;
    }
    google_p(['站内消息', '推送通知层', '弹出']);
    $.post('/I/?method=user.pm.getnotifytype&notify_type=ws', null, function(re_data) {
        if (re_data.pm_getnotifytype_response.body == 'Y') {
            data = res.data;
            if (!data || window.Client_UserID != data.ownerid) {
                return;
            }
            var sMsgId = data.id,
                sIsSave = Number(data.isSave);
            var obj = $('#user_projectinfo_layer');
            $('.project_layer_close', obj).attr('save', sIsSave).unbind('click').click(function() {
                clearTimeout(window.closeProjectInfo);
                obj.slideUp();
                google_p(['站内消息', '推送通知层', '关闭']);
                var sFlagSave = $(this).attr('save');
                if (sFlagSave == 1) {
                    $.post('/I/?method=user.pm.readall', {
                        nids: sMsgId
                    }, function(read_data) {}, 'json');
                }
            });
            $('#user_projectinfo_content').html(data.msg);
            if (res.entityId == 'lotteryStatistic_win') {
                $.post('/ajax/?method=lottery.match.projecttrends', {
                    project_id: data.msg_json.lotteryId,
                    user_id: data.msg_json.ownerId
                }, function(sub_res) {
                    if (sub_res) {
                        $('#user_projectinfo_content p.ctrl_notice').html(sub_res.status_detail);
                    }
                }, 'json');
            }
            if (res.entityId == 'lotteryStatistic_not_win') {
                $('#user_projectinfo_content p.ctrl_notice').html('方案结束');
            }
            if (!obj.is(':hidden')) {
                clearTimeout(window.closeProjectInfo);
            } else {
                obj.slideDown();
            }
            $('#project_status_count').show();
            if (typeof window.PROJECT_MOUSE_EVENT == 'undefined') {
                showBuyProjectStateList();
            }
            if (sIsSave != 1) {
                window.closeProjectInfo = setTimeout(function() {
                    $('#user_projectinfo_layer').slideUp();
                }, 15 * 1000);
            }
        }
    }, 'json');
};
(function() {
    ok_f({
        m: ["headerModule", "loginModule"],
        f: ["public/down_menu_config.js", "public/baidushare/bds_s_v2.js"],
        p: ["bgiframe", "json"]
    });
})();;
$(function() {
    var ad_ids = [],
        timer = Math.floor(new Date().getTime() / 1000);
    var $bg = $('<div class="adv_ok_mask"></div>').appendTo('body').hide();
    var $w = $(window);
    var $advs = $('.adv_ok').each(function() {
        var ad_id = $(this).attr('id').replace(/[^0-9]/g, '') * 1;
        ad_id && ad_ids.push(ad_id);
    });

    function renderAdPlace(data) {
        var scroll = [],
            okAdvData = Common.getLocalStorage('okAdvData') || {};
        $.each(data, function(k, v) {
            if (!showThisAdv(k, okAdvData)) {
                return false;
            }
            var adss = [],
                $adv = $advs.filter('#adv_' + k),
                mod = v.mod;
            if (typeof(v.ads) == 'undefined' || v.ads.length == 0) return false;
            $.each(v.ads, function(i, t) {
                if (t.status === 1 && t.start_time <= timer && (t.end_time === 0 || t.end_time >= timer)) {
                    adss.push(t);
                }
            });
            if (adss.length) {
                if (mod === 3) {
                    var screenW = $w.width();
                    var advW = $adv.width();
                    var leftGapW = (screenW - 1000) / 2;
                    var adjustW = leftGapW - 10 * 2;
                    if (leftGapW < 0) {
                        return false;
                    }
                    if (advW > leftGapW) {
                        console.log($adv, $adv.find('img'), $adv.children('img'));
                        $adv.css({
                            width: adjustW + 'px'
                        });
                        advW = adjustW;
                    }
                    scroll.push(function() {
                        $adv.css({
                            top: 200,
                            left: leftGapW - advW - 10,
                            'z-index': 9999
                        });
                    });
                } else if (mod === 4) {} else if (mod === 2) {}
                typeof scroll === 'function' && $w.scroll(scroll);
                var ads_index = Math.floor(Math.random() * (adss.length));
                var close_timer = adss[ads_index].auto_close || 0,
                    save_close = adss[ads_index].save_close || 0;
                if (adss[ads_index].mode == 1) {
                    $adv.html(adss[ads_index].content + (save_close ? ('<p class="adv_ok_close" save_close=' + adss[ads_index].adid + '>') : '') + '</p>');
                } else {
                    $adv.html(adss[ads_index].content);
                }
                $adv.children('img').css({
                    width: adjustW + 'px'
                });
                var $close = $adv.find('.adv_ok_close').hover(function() {
                    $(this).addClass('adv_ok_close_hover');
                }, function() {
                    $(this).removeClass('adv_ok_close_hover');
                }).click(function() {
                    var adid = $(this).attr('save_close') || 0;
                    saveAdvInfoToStorage(k, okAdvData);
                    $(this).parents('.adv_ok').remove();
                    $.each(scroll, function(i, t) {
                        t();
                    });
                });
                $adv.show().find('img').load(function() {
                    $.each(scroll, function(i, t) {
                        t();
                    });
                });
                close_timer && setTimeout(function() {
                    $close.click();
                }, close_timer * 1000);
                var adopts = $adv.attr('adopts') ? $adv.attr('adopts').split(',') : [],
                    opts = {};
                $.each(adopts, function(i, t) {
                    var o = t.split(':');
                    opts[o[0]] = o[1];
                });
                opts.close === 'disable' && $adv.find('.adv_ok_close').remove();
            }
        });
        scroll.length && $w.resize(function() {
            $.each(scroll, function(i, t) {
                t();
            });
        });
        scroll.length && $w.scroll(function() {
            $.each(scroll, function(i, t) {
                t();
            });
        });
    }
    if (ad_ids.length > 0) {
        var parms = {
            id: ad_ids.join(',')
        };
        /mode=preview/.test(location.href) && (parms['mode'] = 'preview');
        parms.v = (typeof adversion === 'undefined' || !adversion) ? new Date().getTime() : adversion;
        $.get('/I/?method=system.data.ad', parms, function(data) {
            renderAdPlace(data.data_ad_response);
        }, 'json');
    }
    var pageType = $('.scrollnotice_box').attr('data-pagetype');
    if (typeof pageType != 'undefined') {
        $.get('/I/?method=ok.news.info.getnotice', {
            "type": pageType
        }, function(res) {
            if (res.code > 0 && res.info.length > 0) {
                res.info.forEach(function(v) {
                    $('.scrollnotice').append('<li><a href="' + v.url + '" target="_blank">' + v.title + '</a></li>');
                });
                if (res.info.length == 1) return false;
                var initScrollTop = 0,
                    initTime = 4000,
                    scrollTimer = null,
                    scrollObj = $('.scrollnotice_container'),
                    noticeBoxH = $('.scrollnotice').height(),
                    singleLiH = $('.scrollnotice li').height();
                scrollObj.append($('.scrollnotice').clone());
                scrollTimer = setInterval(fnStarMove, initTime);
                $('.scrollnotice li').mouseover(function() {
                    clearInterval(scrollTimer);
                }).mouseout(function() {
                    scrollTimer = setInterval(fnStarMove, initTime);
                });

                function fnStarMove() {
                    if (initScrollTop == 0) {
                        scrollObj.css({
                            'top': initScrollTop
                        });
                        initScrollTop -= singleLiH;
                        scrollObj.animate({
                            'top': initScrollTop
                        }, 400);
                    } else {
                        scrollObj.animate({
                            'top': initScrollTop
                        }, 400);
                    }
                    initScrollTop -= singleLiH;
                    if (Math.abs(initScrollTop) > noticeBoxH) initScrollTop = 0;
                }
            }
        }, 'json')
    }

    function showThisAdv(locationId, okAdvData) {
        if (!(locationId in okAdvData)) return true;
        var startTime = okAdvData[locationId].start_time;
        if (new Date().getTime() - startTime >= 2 * 60 * 60 * 1000) {
            return true;
        } else {
            return false;
        }
    }

    function saveAdvInfoToStorage(locationId, okAdvData) {
        if (!(locationId in okAdvData)) {
            okAdvData[locationId] = {};
        }
        okAdvData[locationId].start_time = new Date().getTime();
        Common.saveLocalStorage('okAdvData', okAdvData);
    }
});
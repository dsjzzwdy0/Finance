
var Common = {};
Common.IntervalClass = {
    keyArr: [],
    intervalNum: 0,
    addFn: function(key, fun, args) {
        var add = true;
        this.keyArr.forEach(function(item, index, allitem) {
            if (item.a == key) add = false;
        });
        if (add) this.keyArr.push({
            a: key,
            b: fun
        });
    },
    delFn: function(key) {
        var num = -1;
        this.keyArr.forEach(function(item, index, allitem) {
            if (item.a == key) num = index;
        });
        if (num != -1) this.keyArr.splice(num, 1)
    },
    intervalFn: function() {
        this.intervalNum++;
        this.keyArr.forEach(function(item, index, allitem) {
            try {
                item.b();
            } catch (e) {
                console.log("异常：", e);
            }
        });
    }
};
Common.LightBox = {
    element: null,
    init: function() {
        var height = '100%';
        var position = "fixed";
        var isIE = /msie/i.test(navigator.userAgent),
            ieVersion = navigator.userAgent.match(/msie (\d)\.0/i);
        ieVersion ? ieVersion[1] : 0;
        if (isIE && (ieVersion == "6.0") && !$.support.style) {
            height = window.screen.availHeight + 'px';
            position = "absolute";
        }
        var html;
        if (isIE) {
            html = '<div id="lightbox" style="left:0; background:rgb(0, 0, 0); top:0; width:100%; height:' + height + '; filter:alpha(opacity=30); -moz-opacity: 0.3; opacity: 0.3;zoom:1; position:' + position + '; z-index:7;transition: all 0.3s; " ><iframe src="" marginwidth="0" framespacing="0" marginheight="0" frameborder="0" width="100%" height="100%" style="left:0; background:rgb(255,255,255); top:0; width:100%; filter:alpha(opacity=0); -moz-opacity: 0; opacity: 0;zoom:1; position:absolute; z-index: 9"></iframe></div>';
        } else {
            html = '<div id="lightbox" style="left:0; background:rgb(0, 0, 0); top:0; width:100%; height:' + height + '; filter:alpha(opacity=30); -moz-opacity: 0.3; opacity: 0.3;zoom:1; position:' + position + '; z-index:7;-webkit-transition: all 0.3s;-moz-transition: all 0.3s;transition: all 0.3s;" ></div>';
        }
        this.element = $(html).appendTo(document.body);
        this.count = 0;
    },
    getZIndex: function() {
        return parseInt(this.element.css("zIndex")) || -1;
    },
    hide: function() {
        if (this.element) {
            this.count--;
            this.element.hide();
        }
    },
    resetZIndex: function() {
        return this.setZIndex("9");
    },
    setZIndex: function(zIndex) {
        if (!this.element) {
            this.init();
        }
        return this.element.css("zIndex", zIndex || "+=2");
    },
    show: function() {
        if (!this.element) {
            this.init();
        }
        this.element.show();
        this.setZIndex("999");
        if (this.count < 0) this.count = 0;
        this.count++;
        return this;
    },
    ShowLoading: function(isTrue) {
        if (isTrue && $('#loadingIcon').size() == 0) {
            $('body').append('<img src="' + StaticUrl + '/oae/index/img/loading.gif" id="loadingIcon"/>');
            var loadingIcon = $('#loadingIcon');
            OkoooUtil.setObjAbsCenter(loadingIcon);
            loadingIcon.css({
                'z-index': '1000',
                'width': '60px',
                'height': '60px'
            });
        } else if ($('#loadingIcon').size() > 0) {
            $('#loadingIcon').hide();
        }
    },
    showMessage: function(msg, callback) {
        var str = '<div class="hide" id="lightBoxDiv"><div class="blackShade"></div><div id="lightBoxMessage" class="pop_common msg_pop">';
        str += '<div class="pop_headerc "><a href="javascript:void(0)" class="pop_close"></a></div>';
        str += '<div class="pop_contentc2 clearfix">' + msg + ' </div>';
        str += '<div class="pop_footerc2"><a href="javascript:void(0)" class="msgPopClose">确定</a></div>';
        str += '</div></div';
        $(document.body).append(str);
        $("#lightBoxDiv").show();
        $("#lightBoxMessage").find(".msgPopClose, .pop_close").unbind("click").bind('click', function() {
            $("#lightBoxDiv").remove();
            if (callback && typeof callback == 'function') callback();
        });
    }
};
Common.stopBubble = function(e) {
    if (e && e.stopPropagation)
        e.stopPropagation();
    else
        window.event.cancelBubble = true;
};
Common.fnClickHideOtherDom = function(targetClassName, callback) {
    $('body').click(function(e) {
        var $target = $(e.target);
        if (!$target.hasClass(targetClassName)) {
            callback()
        }
    });
};
Common.fnMarqueenRoll = function(scrollObj, rollTimer, delayTime) {
    clearInterval(rollTimer)
    var initScrollTop = 0,
        delayTime = delayTime || 4000
    changeTime = 400, singleLiH = scrollObj.children('.roll_box').find('li').height();
    scrollObj.append(scrollObj.children('.roll_box').clone());
    rollTimer = setInterval(fnStarMove, delayTime);
    scrollObj.children('.roll_box').find('li').mouseover(function() {
        clearInterval(rollTimer);
    }).mouseout(function() {
        rollTimer = setInterval(fnStarMove, delayTime);
    });

    function fnStarMove() {
        if (initScrollTop == 0) {
            scrollObj.css({
                'top': initScrollTop
            });
            initScrollTop -= singleLiH;
            scrollObj.animate({
                'top': initScrollTop
            }, changeTime);
        } else {
            scrollObj.animate({
                'top': initScrollTop
            }, changeTime);
        }
        initScrollTop -= singleLiH;
        if (Math.abs(initScrollTop) > scrollObj.children('.roll_box').height()) initScrollTop = 0;
    }
}
Common.selectSwitch = function(callback) {
    var $selectItem = $('.selected_item');
    var $buyItmes = $('.imitate_select ul');
    var indexNum = 0;
    $('.imitate_select li').each(function() {
        $(this).attr('index', indexNum);
        indexNum++;
    }).unbind('click').bind('click', function() {
        console.log($(this).attr('index'))
    });
    $selectItem.unbind('click').bind('click', function() {
        $buyItmes.slideDown(400, function() {
            $(this).children('li').unbind('click').bind('click', function(e) {
                e.preventDefault();
                var showContent = $(this).text(),
                    selectType = $(this).attr('data-type');
                $(this).addClass('hide').siblings().removeClass('hide');
                $selectItem.attr('data-type', selectType).children('i').text(showContent);
                $buyItmes.slideUp();
                if (callback && typeof(callback) == 'function') {
                    callback();
                }
            })
        });
    })
};
Common.saveSessionStorage = function(key, val) {
    var data = typeof val === "object" ? JSON.stringify(val) : val;
    sessionStorage.setItem(key, data);
}, Common.getSessionStorage = function(key) {
    return JSON.parse(sessionStorage.getItem(key));
}, Common.saveLocalStorage = function(key, val) {
    var data = typeof val === "object" ? JSON.stringify(val) : val;
    localStorage.setItem(key, data);
}, Common.getLocalStorage = function(key) {
    return JSON.parse(localStorage.getItem(key));
}, Common.loadScript = function(url, cb) {
    var script = document.createElement('script'),
        head = document.getElementsByTagName('head')[0];
    script.async = true;
    var loaded = false;
    script.onload = script.onreadystatechange = function() {
        if (!loaded && (!script.readyState || "loaded" === script.readyState || "complete" === script.readyState)) {
            loaded = true;
            setTimeout(function() {
                if (cb && typeof cb == 'function') cb();
            }, 0);
        }
    };
    script.src = url;
    head.appendChild(script);
};
Common.tabSwitch = function(paras, callback) {
    var paras = paras || {},
        config = $.extend({
            tabNav: '.tab_nav',
            tabContent: '.tab_content',
            index: 0,
            curClass: 'tab_cur'
        }, paras);
    var $tabNavBtn = $(config.tabNav).children();
    var $tabContent = $(config.tabContent).children();
    $tabNavBtn.each(function() {
        $(this).attr('index', config.index);
        config.index++;
    });
    $tabNavBtn.unbind('click').bind('click', function(e) {
        Common.stopBubble(e);
        if ($(this).hasClass('disabled')) return false;
        $(this).addClass(config.curClass).siblings().removeClass(config.curClass);
        $tabContent.eq($(this).attr('index')).show().siblings().hide();
        if (callback && typeof(callback) == 'function') {
            callback();
        }
    })
};
Common.generateRandomNum = function(num, min, max) {
    var arr = [],
        obj = {};
    while (arr.length < num) {
        var ranNum = Math.floor(Math.random() * max + min);
        if (!obj[ranNum]) {
            obj[ranNum] = true;
            arr.push(ranNum);
        }
    }
    return arr;
};
Common.arrSortNum = function(arr, method) {
    return arr.sort(function(a, b) {
        return method == true ? a - b : b - a;
    })
};
Common.fnGeetest = function(data, $selector, callback) {
    initGeetest({
        gt: data.gt,
        challenge: data.challenge,
        offline: !data.success
    }, function(authObj) {
        authObj.appendTo($selector);
        $($selector).attr({
            'data-authtype': 'geetest'
        }).show();
        var geetestData = $($selector).attr('data-geetestdata');
        if (typeof geetestData == 'undefined') {
            $($selector).attr('data-geetestdata', data.gt + ',' + data.challenge + ',' + data.success);
        }
        authObj.onSuccess(function() {
            var validState = authObj.getValidate();
            $($selector).attr('data-authvalid', validState.geetest_challenge + ',' + validState.geetest_validate + ',' + validState.geetest_seccode)
        });
        authObj.onFail(function() {
            $($selector).attr('data-authvalid', '');
        });
        if (callback && typeof callback == 'function') callback(authObj);
    });
};
Common.fnOkCodeAuth = function($selector, $authCode) {
    $($selector).attr('data-authtype', 'okooo').show();
    if (typeof $authCode != 'undefined') {
        $($authCode).show();
    } else {
        $('#authCode').show();
    }
    $('#randomNoImg,#regChangeRandImg').bind('click', function() {
        $('#randomNoImg').attr('src', '/I/?method=ok.user.settings.authcodepic' + '&r' + Math.random());
    });
};
Common.isLogin = function(callback) {
    var userId = GetCookie('LStatus') !== 'N' ? window.Client_UserID : 0;
    if (userId) {
        typeof callback === 'function' && callback();
    } else {
        LoginShow(callback);
    }
};
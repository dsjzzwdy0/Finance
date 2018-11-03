/**
 *
 */
(function ($) {
    var zIndex = 5000;
    $.browser = {};
    $.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
    $.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
    $.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
    $.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
    if ($.browser.msie) {
        var trim_Version = navigator.appVersion.split(";")[1].replace(/[ ]/g, "");
        if (navigator.appName === "Microsoft Internet Explorer" && trim_Version === "MSIE6.0") {
            $.browser.isIe6 = true;
        } else if (navigator.appName === "Microsoft Internet Explorer" && trim_Version === "MSIE7.0") {
            $.browser.isIe7 = true;
        } else if (navigator.appName === "Microsoft Internet Explorer" && trim_Version === "MSIE8.0") {
            $.browser.isIe8 = true;
        } else if (navigator.appName === "Microsoft Internet Explorer" && trim_Version === "MSIE9.0") {
            $.browser.isIe9 = true;
        }
    }
    $.getZindex = function () {
        return zIndex++;
    };
})(jQuery);

DisplaySfcList = function (issue) {
    var params = {
        issue: issue
    };
    $.ajax({
        url: "sfclist",
        data: params,
        context: document.body,
        success: function (json) {
            $("#wrapper_content").html(json);
            layer.msg("下载成功", {time:2000});
            InitPage();
            
            //开始动画显示动态信息
            startDynamic();
        }
    });
};

function bug() {
    this.timer = new Date().getTime();
    this.alert = function (group) {
        alert(group + ' : ' + (new Date().getTime() - this.timer));
        this.timer = new Date().getTime();
    }
}

function myCalc(ggWay, sa, sb, minabs, maxabs) {
    var ggArr = ggWay.split("_");
    var c = Number(ggArr[0]);
    var macthNum = sa.length + sb.length;
    var zuShu = 0;
    if (sb.length == 0) {
        if (macthNum > 15 && ComputeCombineNum(sa.length, c) > 100000) {
            return false;
        }
        var saZh = arrcl(sa, c);
        for (var i = 0, ilen = saZh.length; i < ilen; i++) {
            var thispl = arral(saZh[i]);
            zuShu += zhuHeGetZuShu(ggWay, thispl, c);
        }
    } else {
        for (var d = minabs; d <= maxabs; d++) {
            if (macthNum > 15 && ComputeCombineNum(sb.length, d) > 100000) {
                return false;
            }
            var sbZuHe = arrcl(sb, d);
            var saZuHeVal = c - d;
            if (sa.length > 0) {
                if (saZuHeVal >= 0) {
                    if (macthNum > 15 && ComputeCombineNum(sa.length, saZuHeVal) > 100000) {
                        return false;
                    }
                    saZuHe = arrcl(sa, saZuHeVal);
                    if (sbZuHe.length * saZuHe.length > 100000) {
                        return false;
                    }
                    var temSbZuHe = [];
                    for (var sk in sbZuHe) {
                        for (var ak in saZuHe) {
                            temSbZuHe.push(sbZuHe[sk].concat(saZuHe[ak]));
                        }
                    }
                    sbZuHe = temSbZuHe;
                } else {
                    continue;
                }
            }
            for (var z = 0, zlen = sbZuHe.length; z < zlen; z++) {
                var thispl = arral(sbZuHe[z]);
                zuShu += zhuHeGetZuShu(ggWay, thispl, c);
            }
        }
    }
    return zuShu;
}

function ComputeCombineNum(x, y) {
    var FirstValue = 1;
    var SecondValue = 1;
    if (y > x) {
        return (0);
    } else {
        for (var I = 0; I < y; I++) {
            FirstValue *= x - I;
            SecondValue *= I + 1;
        }
        var ResultNum = FirstValue / SecondValue;
        return (ResultNum);
    }
}

function arrcl(arr, n, z) {
    var r = [];
    fn([], arr, n);
    return r;

    function fn(t, a, n) {
        if (n === 0 || z && r.length == z) {
            return r[r.length] = t;
        }
        for (var i = 0, l = a.length - n; i <= l; i++) {
            if (!z || r.length < z) {
                var b = t.slice();
                b.push(a[i]);
                fn(b, a.slice(i + 1), n - 1);
            }
        }
    }
};

function arral(A2, fn) {
    var n = 0,
        codes = [],
        code = [],
        isTest = typeof fn == 'function',
        stop;
    each(A2, n);

    function each(A2, n) {
        if (stop || n >= A2.length) {
            if (isTest && false === fn(code)) {
                stop = true;
            } else {
                codes.push(code.slice());
                code.length = n - 1;
            }
        } else {
            var cur = A2[n];
            for (var i = 0, j = cur.length; i < j; i++) {
                code.push(cur[i]);
                each(A2, n + 1);
            }
            if (n) {
                code.length = n - 1;
            }
        }
    }

    return codes;
};

function zhuHeGetZuShu(ggWay, zuHe, c) {
    var zhus = 0,
        maxjj = {
            2: 100000,
            3: 100000,
            4: 250000,
            5: 250000,
            6: 500000,
            7: 500000,
            8: 500000
        };
    var voteType = GetVotetype(ggWay);
    var voteArr = voteType.split(" ");
    for (var sai = 0, slen = zuHe.length; sai < slen; sai++) {
        var thisZuHe = zuHe[sai];
        for (var v = 0, vlen = voteArr.length; v < vlen; v++) {
            var vVal = voteArr[v];
            var thisBetWagerCount = BJBetWagerCount[c][vVal];
            var thisBetWagerCountArr = thisBetWagerCount.split(",");
            for (var betV = 0, blen = thisBetWagerCountArr.length; betV < blen; betV++) {
                var betZuHeVal = thisBetWagerCountArr[betV];
                var betZuHeArr = betZuHeVal.split("/");
                var zc = 1;
                for (var bz = 0, bzlen = betZuHeArr.length; bz < bzlen; bz++) {
                    var thisVal = thisZuHe[Number(betZuHeArr[bz]) - 1].split("-");
                    zc *= Number(thisVal[1]);
                    var maxxx = maxjj[vVal];
                    if (maxxx && zc > maxxx) {
                        zc = maxxx;
                    }
                }
                zhus += zc;
            }
        }
    }
    return zhus;
}

var BJBetWagerCount = new Array(17);
for (var i = 0; i < BJBetWagerCount.length; i++) {
    BJBetWagerCount[i] = new Array(17);
}
BJBetWagerCount[1][1] = "1";
BJBetWagerCount[2][1] = "1,2";
BJBetWagerCount[2][2] = "1/2";
BJBetWagerCount[3][1] = "1,2,3";
BJBetWagerCount[3][2] = "1/2,1/3,2/3";
BJBetWagerCount[3][3] = "1/2/3";
BJBetWagerCount[4][1] = "1,2,3,4";
BJBetWagerCount[4][2] = "1/2,1/3,2/3,1/4,2/4,3/4";
BJBetWagerCount[4][3] = "1/2/3,1/2/4,1/3/4,2/3/4";
BJBetWagerCount[4][4] = "1/2/3/4";
BJBetWagerCount[5][1] = "1,2,3,4,5";
BJBetWagerCount[5][2] = "1/2,1/3,2/3,1/4,2/4,3/4,1/5,2/5,3/5,4/5";
BJBetWagerCount[5][3] = "1/2/3,1/2/4,1/3/4,2/3/4,1/2/5,1/3/5,2/3/5,1/4/5,2/4/5,3/4/5";
BJBetWagerCount[5][4] = "1/2/3/4,1/2/3/5,1/2/4/5,1/3/4/5,2/3/4/5";
BJBetWagerCount[5][5] = "1/2/3/4/5";
BJBetWagerCount[6][1] = "1,2,3,4,5,6";
BJBetWagerCount[6][2] = "1/2,1/3,2/3,1/4,2/4,3/4,1/5,2/5,3/5,4/5,1/6,2/6,3/6,4/6,5/6";
BJBetWagerCount[6][3] = "1/2/3,1/2/4,1/3/4,2/3/4,1/2/5,1/3/5,2/3/5,1/4/5,2/4/5,3/4/5,1/2/6,1/3/6,2/3/6,1/4/6,2/4/6,3/4/6,1/5/6,2/5/6,3/5/6,4/5/6";
BJBetWagerCount[6][4] = "1/2/3/4,1/2/3/5,1/2/4/5,1/3/4/5,2/3/4/5,1/2/3/6,1/2/4/6,1/3/4/6,2/3/4/6,1/2/5/6,1/3/5/6,2/3/5/6,1/4/5/6,2/4/5/6,3/4/5/6";
BJBetWagerCount[6][5] = "1/2/3/4/5,1/2/3/4/6,1/2/3/5/6,1/2/4/5/6,1/3/4/5/6,2/3/4/5/6";
BJBetWagerCount[6][6] = "1/2/3/4/5/6";
BJBetWagerCount[7][1] = "1,2,3,4,5,6,7";
BJBetWagerCount[7][2] = "1/2,1/3,2/3,1/4,2/4,3/4,1/5,2/5,3/5,4/5,1/6,2/6,3/6,4/6,5/6,1/7,2/7,3/7,4/7,5/7,6/7";
BJBetWagerCount[7][3] = "1/2/3,1/2/4,1/3/4,2/3/4,1/2/5,1/3/5,2/3/5,1/4/5,2/4/5,3/4/5,1/2/6,1/3/6,2/3/6,1/4/6,2/4/6,3/4/6,1/5/6,2/5/6,3/5/6,4/5/6,1/2/7,1/3/7,2/3/7,1/4/7,2/4/7,3/4/7,1/5/7,2/5/7,3/5/7,4/5/7,1/6/7,2/6/7,3/6/7,4/6/7,5/6/7";
BJBetWagerCount[7][4] = "1/2/3/4,1/2/3/5,1/2/4/5,1/3/4/5,2/3/4/5,1/2/3/6,1/2/4/6,1/3/4/6,2/3/4/6,1/2/5/6,1/3/5/6,2/3/5/6,1/4/5/6,2/4/5/6,3/4/5/6,1/2/3/7,1/2/4/7,1/3/4/7,2/3/4/7,1/2/5/7,1/3/5/7,2/3/5/7,1/4/5/7,2/4/5/7,3/4/5/7,1/2/6/7,1/3/6/7,2/3/6/7,1/4/6/7,2/4/6/7,3/4/6/7,1/5/6/7,2/5/6/7,3/5/6/7,4/5/6/7";
BJBetWagerCount[7][5] = "1/2/3/4/5,1/2/3/4/6,1/2/3/5/6,1/2/4/5/6,1/3/4/5/6,2/3/4/5/6,1/2/3/4/7,1/2/3/5/7,1/2/3/6/7,1/2/4/5/7,1/2/4/6/7,1/2/5/6/7,1/3/4/5/7,1/3/4/6/7,1/3/5/6/7,1/4/5/6/7,2/3/4/5/7,2/3/4/6/7,2/3/5/6/7,2/4/5/6/7,3/4/5/6/7";
BJBetWagerCount[7][6] = "1/2/3/4/5/6,1/2/3/4/5/7,1/2/3/4/6/7,1/2/3/5/6/7,1/2/4/5/6/7,1/3/4/5/6/7,2/3/4/5/6/7";
BJBetWagerCount[7][7] = "1/2/3/4/5/6/7";
BJBetWagerCount[8][1] = "1,2,3,4,5,6,7,8";
BJBetWagerCount[8][2] = "1/2,1/3,2/3,1/4,2/4,3/4,1/5,2/5,3/5,4/5,1/6,2/6,3/6,4/6,5/6,1/7,2/7,3/7,4/7,5/7,6/7,1/8,2/8,3/8,4/8,5/8,6/8,7/8";
BJBetWagerCount[8][3] = "1/2/3,1/2/4,1/3/4,2/3/4,1/2/5,1/3/5,2/3/5,1/4/5,2/4/5,3/4/5,1/2/6,1/3/6,2/3/6,1/4/6,2/4/6,3/4/6,1/5/6,2/5/6,3/5/6,4/5/6,1/2/7,1/3/7,2/3/7,1/4/7,2/4/7,3/4/7,1/5/7,2/5/7,3/5/7,4/5/7,1/6/7,2/6/7,3/6/7,4/6/7,5/6/7,1/2/8,1/3/8,2/3/8,1/4/8,2/4/8,3/4/8,1/5/8,2/5/8,3/5/8,4/5/8,1/6/8,2/6/8,3/6/8,4/6/8,5/6/8,1/7/8,2/7/8,3/7/8,4/7/8,5/7/8,6/7/8";
BJBetWagerCount[8][4] = "1/2/3/4,1/2/3/5,1/2/4/5,1/3/4/5,2/3/4/5,1/2/3/6,1/2/4/6,1/3/4/6,2/3/4/6,1/2/5/6,1/3/5/6,2/3/5/6,1/4/5/6,2/4/5/6,3/4/5/6,1/2/3/7,1/2/4/7,1/3/4/7,2/3/4/7,1/2/5/7,1/3/5/7,2/3/5/7,1/4/5/7,2/4/5/7,3/4/5/7,1/2/6/7,1/3/6/7,2/3/6/7,1/4/6/7,2/4/6/7,3/4/6/7,1/5/6/7,2/5/6/7,3/5/6/7,4/5/6/7,1/2/3/8,1/2/4/8,1/3/4/8,2/3/4/8,1/2/5/8,1/3/5/8,2/3/5/8,1/4/5/8,2/4/5/8,3/4/5/8,1/2/6/8,1/3/6/8,2/3/6/8,1/4/6/8,2/4/6/8,3/4/6/8,1/5/6/8,2/5/6/8,3/5/6/8,4/5/6/8,1/2/7/8,1/3/7/8,2/3/7/8,1/4/7/8,2/4/7/8,3/4/7/8,1/5/7/8,2/5/7/8,3/5/7/8,4/5/7/8,1/6/7/8,2/6/7/8,3/6/7/8,4/6/7/8,5/6/7/8";
BJBetWagerCount[8][5] = "1/2/3/4/5,1/2/3/4/6,1/2/3/5/6,1/2/4/5/6,1/3/4/5/6,2/3/4/5/6,1/2/3/4/7,1/2/3/5/7,1/2/3/6/7,1/2/4/5/7,1/2/4/6/7,1/2/5/6/7,1/3/4/5/7,1/3/4/6/7,1/3/5/6/7,1/4/5/6/7,2/3/4/5/7,2/3/4/6/7,2/3/5/6/7,2/4/5/6/7,3/4/5/6/7,1/2/3/4/8,1/2/3/5/8,1/2/3/6/8,1/2/3/7/8,1/2/4/5/8,1/2/4/6/8,1/2/4/7/8,1/2/5/6/8,1/2/5/7/8,1/2/6/7/8,1/3/4/5/8,1/3/4/6/8,1/3/4/7/8,1/3/5/6/8,1/3/5/7/8,1/3/6/7/8,1/4/5/6/8,1/4/5/7/8,1/4/6/7/8,1/5/6/7/8,2/3/4/5/8,2/3/4/6/8,2/3/4/7/8,2/3/5/6/8,2/3/5/7/8,2/3/6/7/8,2/4/5/6/8,2/4/5/7/8,2/4/6/7/8,2/5/6/7/8,3/4/5/6/8,3/4/5/7/8,3/4/6/7/8,3/5/6/7/8,4/5/6/7/8";
BJBetWagerCount[8][6] = "1/2/3/4/5/6,1/2/3/4/5/7,1/2/3/4/6/7,1/2/3/5/6/7,1/2/4/5/6/7,1/3/4/5/6/7,2/3/4/5/6/7,1/2/3/4/5/8,1/2/3/4/6/8,1/2/3/4/7/8,1/2/3/5/6/8,1/2/3/5/7/8,1/2/3/6/7/8,1/2/4/5/6/8,1/2/4/5/7/8,1/2/4/6/7/8,1/2/5/6/7/8,1/3/4/5/6/8,1/3/4/5/7/8,1/3/4/6/7/8,1/3/5/6/7/8,1/4/5/6/7/8,2/3/4/5/6/8,2/3/4/5/7/8,2/3/4/6/7/8,2/3/5/6/7/8,2/4/5/6/7/8,3/4/5/6/7/8";
BJBetWagerCount[8][7] = "1/2/3/4/5/6/7,1/2/3/4/5/6/8,1/2/3/4/5/7/8,1/2/3/4/6/7/8,1/2/3/5/6/7/8,1/2/4/5/6/7/8,1/3/4/5/6/7/8,2/3/4/5/6/7/8";
BJBetWagerCount[8][8] = "1/2/3/4/5/6/7/8";
BJBetWagerCount[9][9] = "1/2/3/4/5/6/7/8/9";
BJBetWagerCount[10][10] = "1/2/3/4/5/6/7/8/9/10";
BJBetWagerCount[11][11] = "1/2/3/4/5/6/7/8/9/10/11";
BJBetWagerCount[12][12] = "1/2/3/4/5/6/7/8/9/10/11/12";
BJBetWagerCount[13][13] = "1/2/3/4/5/6/7/8/9/10/11/12/13";
BJBetWagerCount[14][14] = "1/2/3/4/5/6/7/8/9/10/11/12/13/14";
BJBetWagerCount[15][15] = "1/2/3/4/5/6/7/8/9/10/11/12/13/14/15";

function GetVotetype(typevote) {
    switch (typevote) {
        case "1_1":
            return "1";
        case "2_1":
            return "2";
        case "2_3":
            return "1 2";
        case "3_1":
            return "3";
        case "3_3":
            return "2";
        case "3_4":
            return "2 3";
        case "3_7":
            return "1 2 3";
        case "4_1":
            return "4";
        case "4_2":
            return "2";
        case "4_4":
            return "3";
        case "4_5":
            return "3 4";
        case "4_6":
            return "2";
        case "4_11":
            return "2 3 4";
        case "4_15":
            return "1 2 3 4";
        case "5_1":
            return "5";
        case "5_2":
            return "2";
        case "5_5":
            return "4";
        case "5_6":
            return "4 5";
        case "5_10":
            return "2";
        case "5_16":
            return "3 4 5";
        case "5_20":
            return "2 3";
        case "5_26":
            return "2 3 4 5";
        case "5_31":
            return "1 2 3 4 5";
        case "6_1":
            return "6";
        case "6_2":
            return "2";
        case "6_6":
            return "5";
        case "6_7":
            return "5 6";
        case "6_15":
            return "2";
        case "6_20":
            return "3";
        case "6_22":
            return "4 5 6";
        case "6_35":
            return "2 3";
        case "6_42":
            return "3 4 5 6";
        case "6_50":
            return "2 3 4";
        case "6_57":
            return "2 3 4 5 6";
        case "6_63":
            return "1 2 3 4 5 6";
        case "7_1":
            return "7";
        case "7_7":
            return "6";
        case "7_8":
            return "6 7";
        case "7_21":
            return "5";
        case "7_35":
            return "4";
        case "7_120":
            return "2 3 4 5 6 7";
        case "8_1":
            return "8";
        case "8_8":
            return "7";
        case "8_9":
            return "7 8";
        case "8_28":
            return "6";
        case "8_56":
            return "5";
        case "8_70":
            return "4";
        case "8_247":
            return "2 3 4 5 6 7 8";
        case "9_1":
            return "9";
        case "10_1":
            return "10";
        case "11_1":
            return "11";
        case "12_1":
            return "12";
        case "13_1":
            return "13";
        case "14_1":
            return "14";
        case "15_1":
            return "15";
        default:
            if (typevote.indexOf("二关") > -1)
                return "2";
            if (typevote.indexOf("三关") > -1)
                return "3";
            else
                return "0";
    }
}

function getChuans() {
    var c = [];
    var a = document.getElementById('guogfs').getElementsByTagName('input');
    var b = document.getElementById('chuans').getElementsByTagName('input');
    $.each(a, function () {
        this.checked && this.value && c.push(this.value);
    });
    $.each(b, function () {
        this.checked && this.value && c.push(this.value);
    });
    return c;
}

function startDynamic()
{
	var initScrollTop = 0,
    initTime = 4000,
    scrollTimer = null,
    scrollObj = $('.scrollnotice_container'),
    noticeBoxH = $('.scrollnotice').height(),
    singleLiH = $('.scrollnotice li').height();
	scrollObj.append($('.scrollnotice').clone());
	scrollTimer = setInterval(fnStarMove, initTime);
	
	$('.scrollnotice li').mouseover(function () {
        	clearInterval(scrollTimer);
    	}).mouseout(function () {
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

InitPage = function () {
    (function ($) {
        !window.console && (window.console = {
            log: new Function('')
        });
        var chuan = [],
            dans = [],
            hit_dans = [0, 0],
            hideCount = 0,
            MultiNum = 1,
            fn = {}, msort = {}, osort = [],
            match = {}, chuans = {
                1: {
                    v: '1_1',
                    t: '单关'
                },
                2: {
                    v: '2_1',
                    t: '2串1'
                },
                3: {
                    v: '3_1',
                    t: '3串1'
                },
                4: {
                    v: '4_1',
                    t: '4串1'
                },
                5: {
                    v: '5_1',
                    t: '5串1'
                },
                6: {
                    v: '6_1',
                    t: '6串1'
                },
                7: {
                    v: '7_1',
                    t: '7串1'
                },
                8: {
                    v: '8_1',
                    t: '8串1'
                }
            }, chuanMore = {
                '3_1': {
                    '3_3': '3串3',
                    '3_4': '3串4'
                },
                '4_1': {
                    '4_4': '4串4',
                    '4_5': '4串5',
                    '4_6': '4串6',
                    '4_11': '4串11'
                },
                '5_1': {
                    '5_5': '5串5',
                    '5_6': '5串6',
                    '5_10': '5串10',
                    '5_16': '5串16',
                    '5_20': '5串20',
                    '5_26': '5串26'
                },
                '6_1': {
                    '6_6': '6串6',
                    '6_7': '6串7',
                    '6_15': '6串15',
                    '6_20': '6串20',
                    '6_22': '6串22',
                    '6_35': '6串35',
                    '6_42': '6串42',
                    '6_50': '6串50',
                    '6_57': '6串57'
                },
                '7_1': {
                    '7_7': '7串7',
                    '7_8': '7串8',
                    '7_21': '7串21',
                    '7_35': '7串35',
                    '7_120': '7串120'
                },
                '8_1': {
                    '8_8': '8串8',
                    '8_9': '8串9',
                    '8_28': '8串28',
                    '8_56': '8串56',
                    '8_70': '8串70',
                    '8_247': '8串247'
                }
            }, dgwf = {
                "10": "0",
                "11": "1",
                "13": "3",
                "14": "0",
                "15": "1",
                "16": "3",
                "20": "3-3",
                "21": "3-1",
                "22": "3-0",
                "23": "1-3",
                "24": "1-1",
                "25": "1-0",
                "26": "0-3",
                "27": "0-1",
                "28": "0-0",
                "30": "1:0",
                "31": "2:0",
                "32": "2:1",
                "33": "3:0",
                "34": "3:1",
                "35": "3:2",
                "36": "4:0",
                "37": "4:1",
                "38": "4:2",
                "39": "5:0",
                "40": "5:1",
                "41": "5:2",
                "42": "胜其他",
                "43": "0:0",
                "44": "1:1",
                "45": "2:2",
                "46": "3:3",
                "47": "平其他",
                "48": "0:1",
                "49": "0:2",
                "50": "1:2",
                "51": "0:3",
                "52": "1:3",
                "53": "2:3",
                "54": "0:4",
                "55": "1:4",
                "56": "2:4",
                "57": "0:5",
                "58": "1:5",
                "59": "2:5",
                "60": "负其他",
                "00": "0",
                "01": "1",
                "02": "2",
                "03": "3",
                "04": "4",
                "05": "5",
                "06": "6",
                "07": "7+"
            }, choose = {}, choose_len = 0,
            isDanGuan = false,
            danGuanNoDanGuanWfs = [0, 0],
            isDanWanfa = false,
            wflist = [],
            zhushuAndMoney = [0, 0, 0, 0],
            logs = [];
        window.choose = choose, window.match = match, window.msort = msort, window.logs = logs, window.osort = osort, window.dans = dans, window.hit_dans = hit_dans, window.chuan = chuan, window.chuanMore = chuanMore, window.zhushuAndMoney = zhushuAndMoney;
        fn.isWanFa = function () {
            var cwf = null,
                dy = true,
                isdg = null;
            wflist = [];
            $.each(choose, function () {
                $.each(this, function (wf, d) {
                    wf = wf * 1;
                    $.inArray(wf, wflist) === -1 && wflist.push(wf);
                    if (cwf === null) {
                        cwf = wf;
                    } else if (cwf !== wf) {
                        dy = false;
                    }
                });
            });
            isDanWanfa = dy, isDanGuan = danGuanNoDanGuanWfs[0] === 0 && danGuanNoDanGuanWfs[1] > 0;
            var $ch = $('#chuan_1'),
                input = $ch.find('input').get(0);
            input && (isDanGuan ? (input.disabled = false, $ch.removeClass('color')) : ((input.checked && fn.delChuan(input.value)), input.checked = false, input.disabled = true, $ch.addClass('color')));
        };
        var calculaterun = null;
        fn.calculate = function () {
            clearTimeout(calculaterun);
            calculaterun = setTimeout(function () {
                var zs_mn = fn.countMatch() || [0, 0],
                    zs = zs_mn[0],
                    yl = zs_mn[1];
                $('#zongjin').html((zs === 'max' || zs > 200000) ? '<a>超过20万</a>' : ('<a>' + zs * MultiNum * 2 + '</a>元'));
                var bouns = '<a>点击查看</a>',
                    maxyl = Math.round(yl * 2 * MultiNum * 100) / 100,
                    minyl = Math.round(zs_mn[3] * 2 * MultiNum * 100) / 100;
                if (yl === 0) {
                    bouns = '<a>0</a>元';
                } else if (minyl === 0 || maxyl === minyl) {
                    bouns = '<a>' + maxyl + '</a>元';
                } else if (yl !== 'max' && yl < 1000000) {
                    bouns = '<a>' + minyl + '～' + maxyl + '</a>元';
                }
                $('#jiangjin').html(bouns);
                $changcishuliang.html(choose_len);
                CanJjyh(), CanGlkc();
                if (zs_mn[2]) {
                    $('#determine').hide(), $('#pingcexiangqing').show();
                } else {
                    if (ifpay) {
                        $('#determine').show(), $('#pingcexiangqing').hide();
                    } else {
                        $('#determine').hide(), $('#pingcexiangqing').show();
                    }
                }
            }, 30);
            setTimeout(function () {
                if (choose_len === 1 && isDanGuan) {
                    $('#nobisai').hide();
                    $('#guoguanfangshi').show();
                } else if (choose_len > 1) {
                    $('#nobisai').hide();
                    $('#guoguanfangshi').show();
                } else {
                    $('#nobisai').show();
                    $('#guoguanfangshi').hide();
                    if (!isDanGuan) {
                        if (choose_len === 0) {
                            $('#nobisai').html('请在上方选择比赛');
                        } else if (choose_len === 1) {
                            $('#nobisai').html('请再选择一场比赛');
                        }
                    }
                }
                if (choose_len > 0) {
                    $('.icon', $yixuan).show();
                    $yixuan.addClass('yi_bg').find('.icon').show();
                } else {
                    $yixuan.removeClass('yi_bg').find('.icon').hide();
                    $goumai_ceng.hide();
                }
                isDanGuan ? $('#chuan_1').show() : $('#chuan_1').hide();
            });
        };
        fn.countMatch = function () {
            var d = [];
            $.each(dans, function (i, t) {
                choose[t] && d.push(t);
            });
            dans = d;
            var count = [0, 0, 0, 0];
            var zhushu = 0,
                qianshu = 0,
                wfconf = {
                    0: 'spf',
                    1: 'rqspf',
                    2: 'bf',
                    3: 'bqc',
                    4: 'zjq'
                };
            $.each(getChuans(), function (i, ggWay) {
                var sa = [],
                    sb = [];
                var a = [],
                    b = [];
                $.each(choose, function (mod) {
                    var tp = [],
                        n = {
                            0: 0,
                            1: 0,
                            2: 0,
                            3: 0,
                            4: 0
                        };
                    $.each(this, function (wf) {
                        $.each(this, function (wz, t) {
                            n[wf]++;
                        });
                    });
                    $.each(n, function (wf, c) {
                        if (c) {
                            tp.push(wfconf[wf] + '-' + c);
                        }
                    });
                    if ($.inArray(mod * 1, dans) > -1) {
                        sb.push(tp);
                    } else {
                        sa.push(tp);
                    }
                });
                zhushu += myCalc(ggWay, sa, sb, hit_dans[0], hit_dans[1]);
                $.each(match, function (mod, t) {
                    var tp = [];
                    $.each(t.aMax, function (wf, sp) {
                        tp.push(sp ? (wfconf[wf] + '-' + sp) : 'min-0');
                    });
                    if (t.dan) {
                        b.push(tp);
                    } else {
                        a.push(tp);
                    }
                });
                qianshu += myCalc(ggWay, a, b, hit_dans[0], hit_dans[1]);
            });
            count[0] = zhushu;
            count[1] = qianshu;
            $.each(match, function () {
                count[2] = count[2] || this.end;
            });
            $.each(match, function () {
                count[2] = count[2] || this.end;
            });
            count[3] = 0;
            zhushuAndMoney = count;
            return count;
        };
        fn.getPosition = function (n) {
            n = -1 * Number(n);
            var zsArr = n > 0 ? [1, 0, -1] : [1];
            var zfArr = n < 0 ? [1, 0, -1] : [-1];
            if (Math.abs(n) === 1) {
                var zsArr = n > 0 ? [1, 0] : [1];
                var zfArr = n < 0 ? [0, -1] : [-1];
            } else {
                var zsArr = n > 0 ? [1, 0, -1] : [1];
                var zfArr = n < 0 ? [1, 0, -1] : [-1];
            }
            var spArr = [
                [
                    [0],
                    [1 - 0 - n],
                    [0],
                    [0, 1],
                    [1],
                    [0]
                ],
                [
                    [0],
                    [2 - 0 - n],
                    [1],
                    [0, 1],
                    [2],
                    [0]
                ],
                [
                    [0],
                    [2 - 1 - n],
                    [2],
                    [0, 1, 2],
                    [3],
                    [0]
                ],
                [
                    [0],
                    [3 - 0 - n],
                    [3],
                    [0, 1],
                    [3],
                    [0]
                ],
                [
                    [0],
                    [3 - 1 - n],
                    [4],
                    [0, 1, 2],
                    [4],
                    [0]
                ],
                [
                    [0],
                    [3 - 2 - n],
                    [5],
                    [0, 1, 2],
                    [5],
                    [0]
                ],
                [
                    [0],
                    [4 - 0 - n],
                    [6],
                    [0, 1],
                    [4],
                    [0]
                ],
                [
                    [0],
                    [4 - 1 - n],
                    [7],
                    [0, 1, 2],
                    [5],
                    [0]
                ],
                [
                    [0],
                    [4 - 2 - n],
                    [8],
                    [0, 1, 2],
                    [6],
                    [0]
                ],
                [
                    [0],
                    [5 - 0 - n],
                    [9],
                    [0, 1],
                    [5],
                    [0]
                ],
                [
                    [0],
                    [5 - 1 - n],
                    [10],
                    [0, 1, 2],
                    [6],
                    [0]
                ],
                [
                    [0],
                    [5 - 2 - n],
                    [11],
                    [0, 1, 2],
                    [7],
                    [0]
                ],
                [
                    [0], zsArr, [12],
                    [0, 1, 2],
                    [6, 7],
                    [0]
                ],
                [
                    [1],
                    [0 - 0 - n],
                    [13],
                    [4],
                    [0],
                    [0]
                ],
                [
                    [1],
                    [1 - 1 - n],
                    [14],
                    [3, 4, 5],
                    [2],
                    [0]
                ],
                [
                    [1],
                    [2 - 2 - n],
                    [15],
                    [3, 4, 5],
                    [4],
                    [0]
                ],
                [
                    [1],
                    [3 - 3 - n],
                    [16],
                    [3, 4, 5],
                    [6],
                    [0]
                ],
                [
                    [1],
                    [-1 * n],
                    [17],
                    [3, 4, 5],
                    [7],
                    [0]
                ],
                [
                    [2],
                    [0 - 1 - n],
                    [18],
                    [7, 8],
                    [1],
                    [1]
                ],
                [
                    [2],
                    [0 - 2 - n],
                    [19],
                    [7, 8],
                    [2],
                    [1]
                ],
                [
                    [2],
                    [1 - 2 - n],
                    [20],
                    [6, 7, 8],
                    [3],
                    [1]
                ],
                [
                    [2],
                    [0 - 3 - n],
                    [21],
                    [7, 8],
                    [3],
                    [1]
                ],
                [
                    [2],
                    [1 - 3 - n],
                    [22],
                    [6, 7, 8],
                    [4],
                    [1]
                ],
                [
                    [2],
                    [2 - 3 - n],
                    [23],
                    [6, 7, 8],
                    [5],
                    [1]
                ],
                [
                    [2],
                    [0 - 4 - n],
                    [24],
                    [7, 8],
                    [4],
                    [1]
                ],
                [
                    [2],
                    [1 - 4 - n],
                    [25],
                    [6, 7, 8],
                    [5],
                    [1]
                ],
                [
                    [2],
                    [2 - 4 - n],
                    [26],
                    [6, 7, 8],
                    [6],
                    [1]
                ],
                [
                    [2],
                    [0 - 5 - n],
                    [27],
                    [7, 8],
                    [5],
                    [1]
                ],
                [
                    [2],
                    [1 - 5 - n],
                    [28],
                    [6, 7, 8],
                    [6],
                    [1]
                ],
                [
                    [2],
                    [2 - 5 - n],
                    [29],
                    [6, 7, 8],
                    [7],
                    [1]
                ],
                [
                    [2], zfArr, [30],
                    [6, 7, 8],
                    [6, 7],
                    [1]
                ]
            ];
            return spArr;
        };
        fn.countMaxOdds = function (mod, odds, rq, end, mid) {
            var position = fn.getPosition(rq),
                len = 0,
                max = null,
                min = 10000;
            var max_vs = {}, zkn = null;
            $.each(position, function (i) {
                var v = 0,
                    s = 0,
                    vs = [];
                $.each(this, function (wf) {
                    vs[wf] = [];
                    $.each(this, function () {
                        var wz = wf === 1 ? (this > 0 ? 0 : (this < 0 ? 2 : 1)) : this;
                        odds[wf] && odds[wf][wz] && (vs[wf].push(odds[wf][wz].sp));
                    });
                    if (vs[wf].length > 0) {
                        v += Math.max.apply(vs[wf], vs[wf]);
                        s += Math.min.apply(vs[wf], vs[wf]);
                    }
                });
                if (max === null || v > max) {
                    max = v, zkn = i;
                }
                if (s && (min === null || s < min)) {
                    min = s;
                }
            });
            $.each(position[zkn], function (wf) {
                $.each(this, function () {
                    var wz = wf === 1 ? (this > 0 ? 0 : (this < 0 ? 2 : 1)) : this;
                    if (odds[wf] && odds[wf][wz]) {
                        max_vs[wf] = odds[wf][wz].sp;
                    }
                });
            });
            $.each(odds, function (wf) {
                $.each(this, function () {
                    len++;
                });
                if (!max_vs[wf]) {
                    max_vs[wf] = 0;
                }
            });
            var mc = match[mod] || {
                moder: mod,
                max: 0,
                min: 0,
                len: 0,
                dan: 0,
                end: end,
                mid: mid
            };
            choose[mod] ? (mc.max = max, mc.min = min, mc.len = len, mc.aMax = max_vs, match[mod] = mc) : (delete match[mod]);
        };
        fn.addChuan = function (c) {
            chuan = getChuans();
        };
        fn.delChuan = function (c) {
            chuan = getChuans();
        };
        fn.resetMoreChuan = function () {
            if (moreChuan) {
                choose_len > 2 ? $guogfs.show() : $guogfs.hide();
                if ($.inArray(2, wflist) > -1 || $.inArray(3, wflist) > -1) {
                    $('#more_5_1,#more_6_1,#more_7_1,#more_8_1').hide();
                } else if ($.inArray(4, wflist) > -1) {
                    $('#more_7_1,#more_8_1').hide();
                    $('#more_5_1,#more_6_1').show();
                } else {
                    $('#more_5_1,#more_6_1,#more_7_1,#more_8_1').show();
                }
                var shows = [],
                    isMoreChuan = false;
                $chuans.find('li:visible').each(function () {
                    shows.push($(this).attr('v'));
                });
                $.each(chuanMore, function (k) {
                    if ($.inArray(k, shows) > -1) {
                        $('#more_' + k).show();
                    } else {
                        $('#more_' + k).hide();
                        $.each(this, function () {
                            this.checked = false;
                            fn.delChuan(this.value);
                        });
                    }
                });
                $('#guogfs').children('.guofs_zk').find('.zk_1').each(function () {
                    if (this.style.display === 'none') {
                        $(this).find('input').each(function () {
                            this.checked = false;
                        });
                    }
                });
                chuan = getChuans();
                $.each(chuanMore, function () {
                    $.each(this, function () {
                        if (this.checked) {
                            isMoreChuan = true;
                            return false;
                        }
                    });
                });
                moreChuan.checked = isMoreChuan;
            }
        };
        fn.setChuans = function (create) {
            if (create) {
                var data = chuans[choose_len],
                    $li;
                if (!$('#chuan_' + choose_len).get(0) && data) {
                    var html;
                    if (data.v === '1_1') {
                        html = '<li id="chuan_' + choose_len + '" v="' + data.v + '" class="color"><label><input id="chuan' + data.v + '" type="checkbox" value="' + data.v + '" disabled="disabled">' + data.t + '</label></li>';
                    } else {
                        html = '<li id="chuan_' + choose_len + '" v="' + data.v + '"><label><input type="checkbox" id="chuan' + data.v + '" value="' + data.v + '">' + data.t + '</label></li>';
                    }
                    $li = $(html).appendTo($chuans).find('input').click(function () {
                        if (this.checked && chuan.length >= 5) {
                            this.checked = false;
                            alert('组合过关方式不能找过5个');
                            return false;
                        }
                        this.checked ? fn.addChuan(this.value) : fn.delChuan(this.value);
                        fn.calculate();
                    }).bind('del', function () {
                        fn.delChuan(this.value);
                        $(this).parents('li:first').remove();
                    });
                }
            } else {
                $chuans.find('#chuan_' + (choose_len + 1) + ' input').trigger('del');
            }
        };
        fn.resChuan = function () {
            if ($.inArray(2, wflist) > -1 || $.inArray(3, wflist) > -1) {
                $chuans.find('#chuan_5,#chuan_6,#chuan_7,#chuan_8').hide();
            } else if ($.inArray(4, wflist) > -1) {
                $chuans.find('#chuan_7,#chuan_8').hide();
                $chuans.find('#chuan_5,#chuan_6').show();
            } else {
                $chuans.find('#chuan_5,#chuan_6,#chuan_7,#chuan_8').show();
            }
            var $lis = $chuans.find('li:visible'),
                len = hit_dans[0];
            $lis.css('line-height', $lis.length > 4 ? '35px' : 'auto');
            len && $lis.each(function () {
                var i = this.id.substr(6, 1) * 1;
                if (i < len) {
                    $(this).addClass('color').find('input').get(0).disabled = true;
                } else {
                    $(this).removeClass('color').find('input').get(0).disabled = false;
                }
            });
            $chuans.find('li:hidden').each(function () {
                var input = $(this).find('input').get(0);
                input.checked && (fn.delChuan(input.value), input.checked = false);
            });
        };

        function loadPageDan() {
            var len = dans.length;
            var $ul = $danmx.find('ul').html('');
            $danmx.find('.shu').html(len);
            if (len > 0) {
                for (var i = 1; i <= len; i++) {
                    $('<li v="' + i + '" hover="li_bg">' + i + '</li>').appendTo($ul).click(function () {
                        var $t = $(this),
                            $p = $t.parents('#danmin,#danmax'),
                            v = $t.html() * 1;
                        $p.find('.shu').html(v);
                        if ($p.attr('id') === 'danmin') {
                            hit_dans[0] = v;
                            if (hit_dans[0] > hit_dans[1]) {
                                $('#danmax [v="' + hit_dans[0] + '"]').click();
                                return;
                            }
                        } else {
                            hit_dans[1] = v;
                            if (hit_dans[1] < hit_dans[0]) {
                                $('#danmin [v="' + hit_dans[1] + '"]').click();
                                return;
                            }
                        }
                        fn.resChuan(), fn.calculate();
                        setTimeout(function () {
                            $danmx.find('.bg').removeClass('bg');
                        });
                    });
                }
                $danfanwei.show();
            } else {
                $danfanwei.hide();
            }
        }

        fn.addDan = function (c) {
            c = c * 1;
            if ($.inArray(c, dans) === -1) {
                dans.push(c), match[c].dan = 1;
                var len = dans.length;
                hit_dans = [len, len];
                setTimeout(loadPageDan);
                $chuans.find('li').each(function () {
                    var n = this.id.split('_')[1] * 1,
                        input = $(this).find('input:first').get(0);
                    if (n === 1) {
                        isDanGuan && (input.checked = false, fn.delChuan(input.value));
                    } else if (n < len) {
                        input.checked = false;
                        input.disabled = true;
                        fn.delChuan(input.value);
                        $(this).addClass('color');
                    } else {
                        input.disabled = false;
                        $(this).removeClass('color');
                    }
                });
            }
        };
        fn.delDan = function (c) {
            c = c * 1;
            var i = $.inArray(c, dans);
            if (i > -1) {
                dans.splice(i, 1), match[c].dan = 0;
                var len = dans.length;
                hit_dans = [len, len];
                setTimeout(loadPageDan);
                $chuans.find('li').each(function (i) {
                    var n = this.id.split('_')[1] * 1,
                        input = $(this).find('input:first').get(0);
                    if (n === 1) {
                        isDanGuan ? ($(this).removeClass('color'), input.disabled = false) : ($(this).addClass('color'), input.disabled = true);
                    } else if (n < len) {
                        input.checked = false;
                        input.disabled = true;
                        fn.delChuan(input.value);
                        $(this).addClass('color');
                    } else {
                        input.disabled = false;
                        $(this).removeClass('color');
                    }
                });
            }
        };
        fn.setOption = function (chos, tdata, pdata, $t) {
            setTimeout(function () {
                var $row = $('#opts_' + pdata.mid, $shedanrows),
                    optid = 'opt_' + pdata.mid + '_' + tdata.xx;
                if (!chos) {
                    if (!$row.get(0)) {
                        var $match = $('#match_' + pdata.mid),
                            zhu = pdata.hname,
                            ke = pdata.aname,
                            html = '<div id="opts_' + pdata.mid + '" data-index="' + pdata.index + '" class="gc_cont">' + '<div class="gc_d">' + pdata.ordercn + '</div>' + '<div class="gc_c"><label z>' + zhu + '</label>&nbsp;vs&nbsp;<label k>' + ke + '</label></div>' + '<div class="gc_cen"></div>' + '<div class="gc_c2"><input value="' + pdata.morder + '" type="checkbox"></div>' + '<div class="gc_c3" hover="font_red">X</div>' + '<div class="clear"></div>' + '</div>';
                        $row = $(html).appendTo($shedanrows);
                        $row.find('input').click(function () {
                            this.checked ? fn.addDan(this.value) : fn.delDan(this.value);
                            fn.calculate();
                        });
                        $row.find('.gc_c3').data(pdata).click(function () {
                            fn.clearChoose($(this).data());
                        });
                        setTimeout(function () {
                            $shedanrows.find('.gc_cont').each(function () {
                                var $t = $(this),
                                    index = $t.data().index;
                                if (index > pdata.index) {
                                    $t.before($row);
                                    return false;
                                }
                            });
                        });
                    }
                    var html;
                    if (tdata.wf === 0) {
                        html = '<div id="' + optid + '" class="gc1"><p></p> <p>' + (tdata.wz === 0 ? pdata.hname : tdata.wz === 2 ? pdata.aname : '平') + '</p> <p></p> <p>' + $t.find('.peilv').html() + '</p></div>';
                    } else if (tdata.wf === 1) {
                        var rq = tdata.wz === 2 ? (-1 * pdata.rq) : pdata.rq;
                        rq = rq > 0 ? ('+' + rq) : rq;
                        var name = (tdata.wz === 0 ? pdata.hname : tdata.wz === 2 ? pdata.aname : '平')
                        if (name === '平') {
                            html = '<div id="' + optid + '" class="gc1"><p>' + rq + '</p> <p>(' + name + ')</p> <p></p> <p>' + $t.find('.peilv').html() + '</p></div>';
                        } else {
                            html = '<div id="' + optid + '" class="gc1"><p>' + name + '</p> <p>(' + rq + ')</p> <p></p> <p>' + $t.find('.peilv').html() + '</p></div>';
                        }
                    } else {
                        html = '<div id="' + optid + '" class="gc1"><p></p> <p>' + $t.find('.peilv:first').text() + '</p> <p></p> <p>' + $t.find('.peilv_1').html() + '</p></div>';
                    }
                    $(html).appendTo($row.find('.gc_cen')).click(function () {
                        var arr = this.id.split('_');
                        $('#match_' + arr[1] + ',#more_' + arr[1]).find('[data-xx="' + arr[2] + '"]').click();
                    });
                } else {
                    $row.find('#' + optid).remove();
                    if ($row.find('.gc1').length < 1) {
                        $row.remove();
                    }
                }
                fn.resetOptionHeight();
                fn.resetMoreChuan();
            });
        };
        fn.resetOptionHeight = function () {
            if ($shedanrows.get(0).scrollHeight > 198) {
                $shedanrows.css({
                    height: 198,
                    'overflow-y': 'auto',
                    'overflow-x': 'hidden',
                    'background-color': '#FFF'
                });
            } else {
                $shedanrows.css({
                    height: 'auto',
                    'overflow': 'hidden'
                });
            }
        };
        fn.reSetOption = function () {
            fn.resetOptionHeight();
            setTimeout(function () {
                $chuans.find('li:gt(' + choose_len + '),li:eq(' + choose_len + ')').each(function () {
                    var input = $(this).find('input').get(0);
                    if (input.checked) {
                        fn.delChuan(input.value);
                    }
                    $(this).remove();
                });
                var $lis = $chuans.find('li:visible')
                $lis.css('line-height', $lis.length > 4 ? '35px' : 'auto');
                fn.resetMoreChuan();
                fn.isWanFa();
                fn.calculate();
            });
        };
        fn.choose = function () {
            var $t = $(this),
                css = $t.attr('choose'),
                chos = $t.hasClass(css);
            var tdata = $t.data(),
                data = MatchIndex[tdata.mid],
                end = data.end,
                rq = data.rq,
                mod = data.morder,
                wf = tdata.wf,
                wz = tdata.wz,
                xx = tdata.xx,
                dg = tdata.dg ? 1 : tdata.wf == 2 ? 1 : 0,
                sp = chos ? null : tdata.sp * 1;
            if (chos) {
                $t.removeClass(css);
                danGuanNoDanGuanWfs[dg]--;
            } else {
                $t.addClass(css);
                danGuanNoDanGuanWfs[dg]++;
            }
            logs.push(mod + '@' + xx);
            var create = null,
                odds = choose[mod] || (choose_len++, create = 1, {});
            !odds[wf] && (odds[wf] = {});
            !msort[mod] && (msort[mod] = []);
            if (sp !== null) {
                odds[wf][wz] = {
                    sp: sp,
                    xx: xx,
                    dg: dg
                };
                $.inArray(xx, msort[mod]) === -1 && msort[mod].push(xx);
                $.inArray(mod, osort) === -1 && osort.push(mod);
            } else {
                delete odds[wf][wz];
                $.isEmptyObject(odds[wf]) && (delete odds[wf]);
                var i = $.inArray(xx, msort[mod]);
                i > -1 && msort[mod].splice(i, 1);
            }
            if ($.isEmptyObject(odds)) {
                delete choose[mod], delete msort[mod], choose_len--, create = 0;
                var i = $.inArray(mod, osort);
                i > -1 && osort.splice(i, 1);
                fn.delDan(mod);
            } else {
                choose[mod] = odds;
            }
            fn.countMaxOdds(mod, odds, rq, end, tdata.mid);
            create !== null && fn.setChuans(create);
            fn.isWanFa();
            fn.resChuan();
            fn.calculate();
            fn.setOption(chos, tdata, data, $t);
        };
        fn.clearChoose = function (data) {
            setTimeout(function () {
                if (data) {
                    var $opts = $('#opts_' + data.mid),
                        $more = $('#more_' + data.mid),
                        $match = $('#match_' + data.mid);
                    $opts.remove();
                    $more.find('[choose]').removeClass('sel1 sel1t');
                    $match.find('[choose]').removeClass('sel1 sel1t');
                    fn.delDan($opts.find('input').val());
                    if (choose[data.morder]) {
                        delete choose[data.morder], delete match[data.morder], delete msort[data.morder], choose_len--;
                    }
                    $match.find('.more .count').html('').removeClass('more_select');
                } else {
                    $.each(match, function () {
                        MatchIndex[this.mid].$t.find('[choose]').removeClass('sel1 sel1t');
                        if (MatchIndex[this.mid].$more)
                            MatchIndex[this.mid].$more.find('[choose]').removeClass('sel1 sel1t');
                    });
                    choose = {}, match = {}, msort = {}, chuan = [], dans = [], hit_dans = [0, 0], choose_len = 0;
                    $shedanrows.html('');
                    $('#zongjin,#jiangjin').html(0);
                    $shedanrows.css('height', 'auto');
                    $changcishuliang.html(0);
                    $AllMatch.find('.more .count').html('').removeClass('more_select');
                }
                fn.reSetOption();
            });
        };
        window.fn = fn;

        function CanJjyh() {
            var msg = '';
            if (isDanGuan && $('#chuan1_1:checked').get(0)) {
                $('#jiangjinyouhua').show();
            } else if (zhushuAndMoney[2] || choose_len < 2 || chuan.length < 1 || (zhushuAndMoney[0] > 500 || zhushuAndMoney[0] === 'max')) {
                $('#jiangjinyouhua').hide();
            } else if (choose_len > 15) {
                msg = "奖金优化最多支持15场比赛";
                $('#jiangjinyouhua').hide();
            } else if (dans.length > 0) {
                msg = '奖金优化不支持定胆！';
                $('#jiangjinyouhua').hide();
            } else {
                $('#jiangjinyouhua').show();
            }
            return msg;
        }

        function CanGlkc() {
            var msg = '';
            if (zhushuAndMoney[2] || choose_len < 2 || chuan.length > 1 || $.inArray('1_1', chuan) !== -1) {
                $('#xuandanguolv').hide();
                msg = '过滤快车不支持多选组合过关方式！';
            } else if (choose_len > 15) {
                msg = "过滤快车最多支持15场比赛";
                $('#xuandanguolv').hide();
            } else if (wflist.length > 1) {
                msg = '过滤快车不支持多玩法方式！';
                $('#xuandanguolv').hide();
            } else {
                $('#xuandanguolv').show();
            }
            return msg;
        }

        var $AllMatch, $filters, $conts, $guogfs, $chuans, $shedanrows, $changcishuliang, $sellie, $danabs, $danmx,
            $goumai_ceng, $danfanwei, $yixuan, ifpay = 1;
        var moreChuan;
        $(function () {
            var Config = {
                wf: {
                    SportteryNWDL: 0,
                    SportteryWDL: 1
                },
                wf2: {
                    0: 'SportteryNWDL',
                    1: 'SportteryWDL',
                    2: 'SportteryScore',
                    3: 'SportteryHalfFull',
                    4: 'SportteryTotalGoals',
                    5: 'SportterySoccerMix'
                }
            };
            var $form = $('#myform'),
                $endday = null;
            $danfanwei = $('#danfanwei'), $danabs = $('#danabs'), $danmx = $('#danmin,#danmax'), $goumai_ceng = $('#goumai_ceng'), $changcishuliang = $('#changcishuliang'), $chuans = $('#chuans'), $shedanrows = $('#shedanrows');
            setTimeout(function () {
                var zzz = 20;
                $conts = $('#content').find('.cont').each(function () {
                    $(this).css('z-index', zzz--).data('');
                });
            }, 5000);
            window.shouqi = function (t) {
                var $t = $(t);
                if ($t.hasClass('shouqi_hover')) {
                    $t.removeClass('shouqi_hover');
                    $t.parents('.cont:first').find('.touzhu').show();
                    resizeFooter();
                } else {
                    $t.addClass('shouqi_hover');
                    $t.parents('.cont:first').find('.touzhu').hide();
                    resizeFooter();
                }
            }
            $AllMatch = $('.touzhu_1');
            $('#bisais').delegate('.bisai', 'click', function () {
                var mtime, ttime, $t = $(this);
                $t.css('display', 'none').siblings('.bisai').css('display', '');
                $('#selectts').removeClass('ting').find('[select-title]').html($t.text());
                $('.shijian[mtime]', $AllMatch).each(function () {
                    ttime = $(this).html(), mtime = $(this).attr('mtime');
                    $(this).html(mtime), $(this).attr('mtime', ttime);
                });
            });
            var MatchIndex = {}, isShowEnd = false;
            $AllMatch.each(function (i) {
                var $t = $(this),
                    data = $t.data(),
                    filter = $t.attr('filterdata').split(',');
                var $btmore = $t.find('.more');
                var data = $.extend(data, {
                    filter: filter,
                    '$t': $t,
                    '$more': null,
                    index: i,
                    del: 0
                });
                $t.data('index', i);
                $btmore.data(data);
                $t.find('[choose]').data('mid', data.mid);
                MatchIndex[data.mid] = data;
                !data.end && ifpay++;
            });
            $AllMatch.delegate('[choose]', 'click', fn.choose).delegate('.xulie', 'click', function () {
                var $row = $(this).parents('.touzhu_1:first').hide(),
                    data = $row.data();
                MatchIndex[data.mid].del = 1;
                hideCount++;
                showFilterHuifu();
                fn.clearChoose(data);
            });
            window.MatchIndex = MatchIndex;
            var filters;

            function countFilter() {
                filters = {
                    rq: ['0'],
                    ss: [],
                    pl: []
                };
                !$filters && ($filters = $('#filters input[filter]'));
                $filters.each(function () {
                    if (this.checked) {
                        var filter = $(this).attr('filter');
                        filters[filter].push(filter === 'pl' ? this.value.split(',') : this.value);
                    }
                });
            }

            function showFilterHuifu() {
                var $hf = $('#filters .yingcang');
                hideCount ? $hf.show() : $hf.hide();
                $hf.find('i').html('&nbsp;' + hideCount + '&nbsp;');
            }

            function filterMacht() {
                isShowEnd ? $endday.show() : $endday.hide();
                hideCount = 0;
                $.each(MatchIndex, function () {
                    var t = this,
                        $t = t['$t'],
                        f = t.filter,
                        hide = 0;
                    if (!isShowEnd && t.end) {
                        hide = 1;
                    } else {
                        if (t.del) {
                            hide = 1;
                        } else if ($.inArray(f[0], filters.rq) < 0) {
                            hide = 1;
                        } else if ($.inArray(f[1], filters.ss) < 0) {
                            hide = 1;
                        } else if (filters.pl.length === 0) {
                            hide = 1;
                        } else {
                            var show = 0;
                            $.each(filters.pl, function () {
                                var t = this,
                                    min = t[0] * 1,
                                    max = t[1] * 1 || 10000000,
                                    v = f[2] * 1;
                                if ((v >= min && v <= max)) {
                                    show = 1;
                                    return true;
                                }
                            });
                            hide = !show;
                        }
                    }
                    if (hide) {
                        $t.hide();
                        setTimeout(function () {
                            var $more = $('#more_' + (t.mid)),
                                $opts = $('#opts_' + (t.mid));
                            if ($more.get(0)) {
                                $more.hide();
                                var $m = $t.find('.more');
                                $m.removeClass($m.attr('rsel') + ' hover1').attr('hover', 'hover1');
                            }
                            $t.find('.sel1,.sel1t').removeClass('sel1 sel1t');
                            $more.find('.sel1,.sel1t').removeClass('sel1 sel1t');
                            $opts.remove();
                        });
                        var moder = t.morder;
                        match[moder] && (delete match[moder], delete choose[moder], choose_len--);
                        if (isShowEnd) {
                            hideCount++;
                        } else if (!isShowEnd && !t.end) {
                            hideCount++;
                        }
                    } else {
                        $t.show();
                    }
                });
                showFilterHuifu();
                fn.reSetOption();
            }

            $('#filterSh').on("mouseenter", function () {
                !$endday && ($endday = $('.endday'));
                $filters = $('#filters input[filter]').click(function () {
                    countFilter(), filterMacht();
                });
                $('#filter5dLx').click(function () {
                    var ls = {
                        17: '英超',
                        23: '意甲',
                        8: '西甲',
                        34: '法甲',
                        35: '德甲'
                    };
                    $('#filterls input').each(function () {
                        var v = this.value;
                        ls[v] ? this.checked = true : this.checked = false;
                    });
                    countFilter(), filterMacht();
                });
                $('#filterAlLs').click(function () {
                    $('#filterls input').each(function () {
                        this.checked = true;
                    });
                    countFilter(), filterMacht();
                });
                $('#filterFxLx').click(function () {
                    $('#filterls input').each(function () {
                        this.checked = !this.checked;
                    });
                    countFilter(), filterMacht();
                });
                $('#filterQqLx').click(function () {
                    $('#filterls input').each(function () {
                        this.checked = false;
                    });
                    countFilter(), filterMacht();
                });
                $('#filters .huifu').click(function () {
                    $filters.each(function () {
                        this.checked = true;
                    });
                    $.each(MatchIndex, function () {
                        this.del = 0;
                    });
                    countFilter(), filterMacht();
                });
                countFilter();
            });
            $('#showEndMatch').one("mouseenter", countFilter).click(function () {
                isShowEnd = this.checked;
                if (this.checked) {
                    $('#gengduo').html('赛果');
                } else {
                    $('#gengduo').html('更多');
                }
                !$endday && ($endday = $('.endday'));
                filterMacht();
            }).get(0).checked && (isShowEnd = true);
            $('.more[rsel]', $AllMatch).click(function () {
                var $t = $(this),
                    data = $t.data(),
                    $p = data['$t'],
                    css = $t.attr('rsel');
                var $more = MatchIndex[data.mid]['$more'];
                if (!$t.hasClass(css)) {
                    if ($more) {
                        $t.addClass(css).attr('hover', '');
                        $more.show();
                    } else {
                        $.get('/jingcai/', {
                            action: 'more',
                            LotteryNo: $('#LotteryNo').val(),
                            MatchOrder: data.morder
                        }, function (html) {
                            if (!$t.hasClass(css)) {
                                $t.addClass(css).attr('hover', '');
                                $more = $(html).insertAfter($p.next('.clear')).attr({
                                    'id': 'more_' + data.mid,
                                    'data-morder': data.morder
                                }).data(data);
                                $more.find('[choose]').data('mid', data.mid).click(function () {
                                    fn.choose.call(this);
                                    var $more = $(this).parents('.more_zk');
                                    setTimeout(function () {
                                        var $count = $p.find('.more .count');
                                        if ($count.get(0)) {
                                            var len = $more.find('.sel1,.sel1t').length;
                                            if (len) {
                                                $count.html(len).addClass('more_select');
                                            } else {
                                                $count.html('').removeClass('more_select');
                                            }
                                        }
                                    });
                                });
                                MatchIndex[data.mid]['$more'] = $more;
                            }
                        }, 'html');
                    }
                } else {
                    $t.removeClass(css).attr('hover', 'hover1');
                    $more && $more.hide();
                }
            });

            function hideYiXuan(e) {
                var $t = $(e.target);
                if ($t.attr('id') === 'yixuan' || $t.parents('#yixuan').get(0)) {
                    return;
                }
                if ($t.hasClass('boot_l1') || !($t.hasClass('goumai_ceng') || $t.parents('.goumai_ceng').get(0) || $t.parents('#chuans').get(0))) {
                    $goumai_ceng.hide();
                    $yixuan.find('.icon').removeClass('i_ho');
                    $(document).unbind('mousedown', hideYiXuan);
                }
            }

            $yixuan = $('#yixuan').click(function () {
                var $icon = $(this).find('.icon');
                if (choose_len > 0) {
                    if ($icon.hasClass('i_ho')) {
                        $goumai_ceng.hide();
                        $yixuan.find('.icon').removeClass('i_ho');
                        $(document).unbind('mousedown', hideYiXuan);
                    } else {
                        $goumai_ceng.show();
                        fn.resetOptionHeight();
                        $icon.addClass('i_ho');
                        $(document).bind('mousedown', hideYiXuan);
                    }
                } else {
                    $goumai_ceng.hide();
                    $yixuan.find('.icon').removeClass('i_ho');
                    $(document).unbind('mousedown', hideYiXuan);
                }
            });
            $goumai_ceng.find('.boot_l1').click(hideYiXuan);
            var showChageRun = null,
                showChageMid = 0,
                ouyaInit = false;

            function bindOuYa() {
                ouyaInit = true;
                setTimeout(function () {
                    $('.zhishu', $AllMatch).hover(function () {
                        if (showPid) {
                            var data = $(this).parents('.touzhu_1:first').data(),
                                parm = {
                                    pid: showPid,
                                    limit: 3,
                                    isReversion: data.rev,
                                    matchId: data.mid,
                                    format: 'json',
                                    bid: typeid
                                };
                            showChageMid = data.mid;
                            var $self = $(this),
                                offset = $self.offset(),
                                $p = $self.parents('.touzhu_1');

                            function success(data) {
                                var conf = {
                                    0: {
                                        cs: 'red_up',
                                        tx: '↑'
                                    },
                                    1: {
                                        cs: '',
                                        tx: '→'
                                    },
                                    2: {
                                        cs: 'red_dow',
                                        tx: '↓'
                                    },
                                    '-1': {
                                        cs: '',
                                        tx: ''
                                    }
                                };
                                if (showChageMid && showChageMid === parm.matchId && data && data.count > 0) {
                                    if (data.count === 2) {
                                        var $pop = $('<div class="zhusbh_1" id="popChange" style="z-index:500">' + '<div class="zhusbh_tit">' + data.providerName + '指数变化</div>' + '<div rows>' + '</div>' + '<div class="up" start></div>' + '</div>').appendTo('body');
                                        var $rows = $pop.find('[rows]'),
                                            $start = $pop.find('[start]');
                                        $.each(data.oddsList, function (i) {
                                            var t = this;
                                            if (i === 'start') {
                                                $start.html('<div class="zs_co1">' + '<p class="p2">' + t.odds[0] + '<em></em></p>' + '<p class="p2">' + t.odds[1] + '<em></em></p>' + '<p class="p2">' + t.odds[2] + '<em></em></p>' + '</div>' + '<div class="zs_co2" title="' + t.time + '">初</div>' + '<div class="zs_co1">' + '<p class="p3">' + t.ah[0] + '<em></em></p>' + '<p class="p4">' + t.ah[1] + '<em></em></p>' + '<p class="p3">' + t.ah[2] + '<em></em></p>' + '</div>');
                                            } else {
                                                $rows.append('<div class="zs_co1">' + '<div>' + '<p class="p2 ' + conf[t.oddsChange[0]].cs + '">' + t.odds[0] + '<em>' + conf[t.oddsChange[0]].tx + '</em></p>' + '<p class="p2 ' + conf[t.oddsChange[1]].cs + '">' + t.odds[1] + '<em>' + conf[t.oddsChange[1]].tx + '</em></p>' + '<p class="p2 ' + conf[t.oddsChange[2]].cs + '">' + t.odds[2] + '<em>' + conf[t.oddsChange[2]].tx + '</em></p>' + '</div>' + '</div>' + '<div class="zs_co2">' + t.time + '</div>' + '<div class="zs_co1">' + '<p class="p3 ' + conf[t.ahChange[0]].cs + '">' + t.ah[0] + '<em>' + conf[t.ahChange[0]].tx + '</em></p>' + '<p class="p4 ' + conf[t.ahChange[1]].cs + '">' + t.ah[1] + '<em>' + conf[t.ahChange[1]].tx + '</em></p>' + '<p class="p3 ' + conf[t.ahChange[2]].cs + '">' + t.ah[2] + '<em>' + conf[t.ahChange[2]].tx + '</em></p>' + '</div>');
                                            }
                                        });
                                    } else if (data.count === 1) {
                                        var $pop = $('<div class="zhusbh_1" id="popChange" style="width:265px;z-index:500">' + '<div class="zhusbh_tit">' + data.providerName + '指数变化</div>' + '<div rows></div>' + '<div class="up" start></div>' + '</div>').appendTo('body');
                                        var $rows = $pop.find('[rows]'),
                                            $start = $pop.find('[start]'),
                                            k = data.oddsKey,
                                            k2 = k + 'Change',
                                            cs = k === 'ah' ? ['p3', 'p4'] : ['p2', 'p2'];
                                        $.each(data.oddsList, function (i) {
                                            var t = this;
                                            if (i === 'start') {
                                                $start.html('<div class="zs_co2" title="' + t.time + '">初</div>' + '<div class="zs_co1">' + '<p class="' + cs[0] + '">' + t[k][0] + '<em></em></p>' + '<p class="' + cs[1] + '">' + t[k][1] + '<em></em></p>' + '<p class="' + cs[0] + '">' + t[k][2] + '<em></em></p>' + '</div>');
                                            } else {
                                                $rows.append('<div class="zs_co2">' + t.time + '</div>' + '<div class="zs_co1">' + '<p class="' + cs[0] + ' ' + conf[t[k2][0]].cs + '">' + t[k][0] + '<em>' + conf[t[k2][0]].tx + '</em></p>' + '<p class="' + cs[1] + ' ' + conf[t[k2][1]].cs + '">' + t[k][1] + '<em>' + conf[t[k2][1]].tx + '</em></p>' + '<p class="' + cs[0] + ' ' + conf[t[k2][2]].cs + '">' + t[k][2] + '<em>' + conf[t[k2][2]].tx + '</em></p>' + '</div>');
                                            }
                                        });
                                    }
                                    var ftop = $footer.offset().top,
                                        selh = $self.height();
                                    offset.top += selh;
                                    offset.left = $p.offset().left + $p.width() - $pop.width();
                                    if (offset.top + $pop.height() > ftop) {
                                        offset.top -= selh + $pop.height();
                                    }
                                    $pop.offset(offset);
                                    showChageMid = parm.matchId;
                                }
                            }

                            var kname = parm.pid + '_' + parm.matchId + parm.bid;
                            showChageRun = setTimeout(function () {
                                !window.CacheCatchData && (window.CacheCatchData = {});
                                if (window.CacheCatchData[kname]) {
                                    success(window.CacheCatchData[kname]);
                                } else {
                                    $.ajax({
                                        url: '/I/?method=lottery.match.oddschangeline',
                                        cache: true,
                                        type: 'get',
                                        data: parm,
                                        dataType: 'json',
                                        success: function (data) {
                                            var d = data.match_oddschangeline_response;
                                            window.CacheCatchData[kname] = d;
                                            success(d);
                                        }
                                    });
                                }
                            }, 30);
                        }
                    }, function () {
                        clearTimeout(showChageRun), showChageRun = null, showChageMid = 0;
                        $('#popChange').remove();
                    });
                }, 3000);
            }

            var typeid = 0,
                defSellie = store.get('hunhe-other'),
                showPid = 0;
            $sellie = $('#sellie').mouseleave(function () {
                $('.xia_l', $sellie).slideUp();
            });
            $('[sel][data-pid]', $sellie).attr({
                'sel-group': 'a',
                'sel-essential': '1'
            }).click(function () {
                var $t = $(this),
                    data = $t.data();
                store.set('hunhe-other', data.typeid + ',' + data.pid);
                typeid = data.typeid;
                showPid = data.pid;
                $sellie.find('[ttype]').html('').parent().removeClass('blue');
                if (data.typeid === 1 || data.typeid === 2) {
                    $sellie.find('[ttype="' + data.typeid + '"]').html($t.html()).parent().addClass('blue');
                    var $p = $t.parent().parent(),
                        hover = $p.attr('hover');
                    $p.removeClass(hover);
                }
                data.lotteryType = $('#LotteryType').val();
                data.lotteryNo = $('#DateString').val();
                data.typeId = typeid;
                data.format = 'json';
                if (!ouyaInit && (typeid === 2 || typeid === 1)) {
                    bindOuYa();
                }
                try {
                    $.get('/I/?method=lottery.match.custom', data, function (data) {
                        var json = data.match_custom_response;
                        $AllMatch.each(function () {
                            var matchid = $(this).data().mid,
                                data = json[matchid] || {};
                            var $zhishu = $(this).find('.zhishu');
                            if (typeid === 3 || typeid === 5) {
                                $zhishu.html('<div class="zz2"><span class="zhishu_2">' + (data.home || '') + '</span><span class="zhishu_2">' + (data.away || '') + '</span></div>');
                            } else if (typeid === 2) {
                                $zhishu.html('<div class="zz1"><span class="zhishu_3">' + (data.home || '') + '</span><span class="zhishu_1">' + (data.draw || '') + '</span> <span class="zhishu_3">' + (data.away || '') + '</span></div>');
                            } else {
                                $zhishu.html('<div class="zz1"><span class="zhishu_4">' + (data.home || '') + '</span><span class="zhishu_4">' + (data.draw || '') + '</span> <span class="zhishu_4">' + (data.away || '') + '</span></div>');
                            }
                        });
                    }, 'json');
                }
                catch (err) {
                    alert("Error: " + err);
                }
                $sellie.removeClass('nine_hover');
                $sellie.find('label:first-child').html($t.text() + (data.typeid === 1 ? '欧赔' : data.typeid === 2 ? '亚盘' : ''));
                $('.xia_l', $sellie).slideUp();
            });
            $('#op,#yp').click(function () {
                $(this).parent().siblings('.dan_2').find('ul').slideUp();
                var $ul = $(this).next('ul');
                if ($ul.css('display') === 'none') {
                    $ul.slideDown();
                } else {
                    $ul.slideUp();
                }
            });
            if (defSellie) {
                defSellie = defSellie.split(',');
                var $sel = $('[data-typeid="' + defSellie[0] + '"][data-pid="' + defSellie[1] + '"]', $sellie).click();
            } else {
                var $sel = $('[data-typeid="1"][data-pid="24"]', $sellie).click();
            }
            $sel.addClass($sel.attr('sel'));
            var ifzoushi = store.get('hunhe-ifzoushi'),
                loadChar = false,
                loadZoushi = false,
                shenpfTimeout = null,
                cacheZoushi = null;
            ifzoushi = $.isNumeric(ifzoushi) ? ifzoushi * 1 : 1;

            function randZoushi($t, data, rev) {
                if (shenpfTimeout && data && data.length > 0) {
                    $('#zhushubianhua').remove();
                    var offset = $t.offset(),
                        top = offset.top + $t.height(),
                        lefpy = $t.hasClass('rangqiuspf') ? 0 : 47;
                    var $pop = $('<div id="zhushubianhua" class="zhusbh">' + '<div class="zhusbh_tit"><span class="float_l bianhua">竞彩奖金指数变化</span><span class="fclose">x</span></div>' + '<div bd="list"></div>' + '</div>').appendTo('body').offset({
                        top: top,
                        left: offset.left + lefpy
                    }).css({
                        'z-index': 1000
                    });
                    $pop.find('.fclose').click(function () {
                        $('#zhushubianhua').remove();
                    });
                    var $list = $pop.find('[bd="list"]'),
                        conf = {
                            0: ['↑', 'red_up'],
                            1: ['→', ''],
                            2: ['↓', 'red_dow']
                        };
                    $.each(data, function () {
                        var t = this,
                            time = t.time.split(' ');
                        $('<div class="zs_co">' + '<p class="p1">' + (time[0].replace('-', '/')) + '</p>' + '<p class="p1">' + time[1] + '</p>' + '<p class="p2 ' + conf[t.change[0]][1] + '">' + t.odds[0] + '<em>' + conf[t.change[0]][0] + '</em></p>' + '<p class="p2 ' + conf[t.change[1]][1] + '">' + t.odds[1] + '<em>' + conf[t.change[1]][0] + '</em></p>' + '<p class="p2 ' + conf[t.change[2]][1] + '">' + t.odds[2] + '<em>' + conf[t.change[2]][0] + '</em></p>' + '</div>').appendTo($list);
                    });
                }
            }

            function randZoushiChart($t, data, rev) {
                if (shenpfTimeout && data && data.length > 0) {
                    $('#zhushubianhua').remove();
                    var offset = $t.offset(),
                        top = offset.top + $t.height(),
                        lefpy = $t.hasClass('rangqiuspf') ? 0 : 47;
                    var $pop = $('<div id="zhushubianhua" class="zhusbh">' + '<div class="zhusbh_tit"><span class="float_l bianhua">竞彩奖金指数变化</span><span class="fclose">x</span></div>' + '<div bd="chart"></div>' + '</div>').appendTo('body').offset({
                        top: top,
                        left: offset.left + lefpy
                    }).css({
                        'z-index': 1000
                    });
                    $pop.find('.fclose').click(function () {
                        $('#zhushubianhua').remove();
                    });
                    var $chart = $pop.find('[bd="chart"]');
                    var home = [],
                        draw = [],
                        away = [];
                    $.each(data, function () {
                        var t = this,
                            tmier = t.timestamp * 1000;
                        if (rev === 'Y') {
                            var tmp = t.change[0],
                                tm2 = t.odds[0];
                            t.change[0] = t.change[2];
                            t.change[2] = tmp;
                            t.odds[0] = t.odds[2];
                            t.odds[0] = tm2;
                        }
                        home.unshift([tmier, t.odds[0] * 1]);
                        draw.unshift([tmier, t.odds[1] * 1]);
                        away.unshift([tmier, t.odds[2] * 1]);
                    });
                    var options = {
                        chart: {
                            height: 180,
                            width: 289
                        },
                        legend: {
                            enabled: false
                        },
                        title: {
                            text: ''
                        },
                        tooltip: {
                            xDateFormat: '%Y-%m-%d %H:%I:%S',
                            crosshairs: true,
                            pointFormat: '{series.name}: <b>{point.y}</b><br/>',
                            shared: true
                        },
                        xAxis: {
                            type: 'datetime',
                            dateTimeLabelFormats: {
                                second: '%H:%M:%S',
                                minute: '%H:%M',
                                hour: '%H:%M',
                                day: '%b月%e日',
                                week: '%b月%e日',
                                month: '%y年%b月',
                                year: '%Y年'
                            }
                        },
                        plotOptions: {
                            series: {
                                marker: {
                                    radius: 1
                                }
                            }
                        },
                        yAxis: {
                            title: {
                                enabled: false
                            }
                        },
                        series: [{
                            data: home,
                            color: '#AB4946',
                            step: true,
                            name: '主队'
                        }, {
                            data: draw,
                            color: '#B0B0B0',
                            step: true,
                            'name': '平局'
                        }, {
                            data: away,
                            color: '#577FAF',
                            step: true,
                            name: '客队'
                        }]
                    };
                    $chart.highcharts(options);
                    $chart.find('tspan:contains(Highcharts.com)').html('');
                }
            }

            function iniZouShi() {
                $AllMatch.delegate('.peilv', 'mouseenter', function () {
                    if (ifzoushi && !shenpfTimeout) {
                        var $t = $(this).parents('.shenpf:first'),
                            isWf = 0;
                        if (!$t.get(0)) {
                            $t = $(this).parents('.rangqiuspf:first'), isWf = 1;
                        }
                        shenpfTimeout = setTimeout(function () {
                            var $p = $t.parents('.touzhu_1'),
                                data = $p.data(),
                                parm = {};
                            parm.lotteryType = 'SportteryNWDL,SportteryWDL';
                            parm.lotteryNo = $('#DateString').val();
                            parm.limit = 6;
                            var tOrder = data.morder,
                                cz = Config.wf2[isWf];
                            var randfun = ifzoushi === 2 ? randZoushiChart : randZoushi;
                            if (!cacheZoushi) {
                                $.post('/I/?method=lottery.match.oddstrend&format=json', parm, function (data) {
                                    cacheZoushi = data.match_oddstrend_response;
                                    randfun($t, cacheZoushi[cz][tOrder], data.rev);
                                }, 'json');
                            } else {
                                randfun($t, cacheZoushi[cz][tOrder], data.rev);
                            }
                        }, 350);
                    }
                });
                $AllMatch.delegate('.shenpf,.rangqiuspf', 'mouseleave', function () {
                    clearTimeout(shenpfTimeout), $('#zhushubianhua').remove(), shenpfTimeout = null;
                });
                loadZoushi = true;
            }

            function iniZouShiTuBiao() {
                $.ajax(StaticUrl + '/min/?b=JS&f=jquery/highcharts.js', {
                    dataType: 'script',
                    cache: true,
                    success: function () {
                        Highcharts.setOptions({
                            lang: {
                                months: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                                shortMonths: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                                weekdays: ['1', '2', '3', '4', '5', '6', '7']
                            },
                            global: {
                                useUTC: false
                            }
                        });
                    }
                }), loadChar = true;
            }

            ifzoushi && iniZouShi();
            ifzoushi === 2 && iniZouShiTuBiao();
            $('#ifzoushi').removeClass('b1').addClass('b' + ifzoushi).attr('title', ifzoushi === 2 ? '奖金变化走势图' : ifzoushi === 1 ? '奖金变化列表' : '不显示奖金变化').click(function () {
                var $t = $(this).removeClass('b0 b1 b2');
                ifzoushi = (ifzoushi + 1) % 3;
                $t.addClass('b' + ifzoushi);
                ifzoushi && !loadZoushi && iniZouShi();
                ifzoushi === 2 && !loadChar && iniZouShiTuBiao();
                store.set('hunhe-ifzoushi', ifzoushi);
                $(this).attr('title', ifzoushi === 2 ? '奖金变化走势图' : ifzoushi === 1 ? '奖金变化列表' : '不显示奖金变化');
            });

            function closeChangeDate(e) {
                var $t = $(e.target);
                if (!($t.attr('id') === 'ChangeDate' || $t.parents('#ChangeDate').get(0))) {
                    $('#ChangeDate').removeClass('time_l1');
                    $(document).unbind('mousedown', closeChangeDate);
                }
            }

            $('#ChangeDate').click(function () {
                var $t = $(this);
                if ($t.hasClass('time_l1')) {
                    $('#ChangeDate').removeClass('time_l1');
                    $(document).unbind('mousedown', closeChangeDate);
                } else {
                    $t.addClass('time_l1');
                    $(document).bind('mousedown', closeChangeDate);
                }
            });
            $('#clearall').click(function () {
                fn.clearChoose();
                $yixuan.find('.icon').removeClass('hover');
            });

            function getWf() {
                return $('#chuan1_1:checked').get(0) ? 'SportterySoccerMix' : (isDanWanfa ? Config.wf2[wflist[0]] : 'SportterySoccerMix');
            }

            function getMatchs() {
                var matchs = [];
                var mmm = [];
                $.each(match, function (mod) {
                    mmm.push([MatchIndex[this.mid].index, mod]);
                });
                mmm.sort(function (a, b) {
                    return a[0] - b[0];
                });
                osort = [];
                $.each(mmm, function () {
                    osort.push(this[1]);
                });
                $.each(osort, function () {
                    var xxs = [],
                        morder = this;
                    $.each(msort[morder], function () {
                        var xx = this;
                        xxs.push($('#chuan1_1:checked').get(0) ? xx : isDanWanfa ? (dgwf[xx] || xx) : xx);
                    });
                    matchs.push(morder + ':' + xxs.join(','));
                });
                return matchs.join(';');
            }

            window.getMatchs = getMatchs;

            function getAbsMatches() {
                return dans.join(',');
            }

            function getChuansStr() {
                chuan = getChuans();
                return chuan.join(',');
            }

            function formPost(action, options, target) {
                var chuanstr = getChuansStr();
                if (options.SubmitType !== 'FilterShrink' && chuan.length < 1) {
                    alert('请选择过关方式！');
                } else if (choose_len < 1) {
                    alert('请选择比赛！');
                } else if (zhushuAndMoney[0] === 'max' || zhushuAndMoney[0] > 50000) {
                    alert("单倍方案不能超过5万注!");
                } else if (MultiNum > 100000) {
                    alert("倍数最高只能是10万倍!");
                } else {
                    $form.attr({
                        action: action
                    });
                    $form.attr({
                        target: target || '_blank'
                    });
                    $form.find('#logUserData').val(logs.join(','));
                    $form.find('#LotteryType').val(getWf());
                    $form.find('#Matches').val(getMatchs());
                    $form.find('#AbsOrder').val(getAbsMatches());
                    $form.find('#MultiNum').val(MultiNum);
                    $form.find('#WagerCount').val(zhushuAndMoney[0]);
                    $form.find('#TotalMoney').val(zhushuAndMoney[0] * 2 * MultiNum);
                    $form.find('#PassType').val(chuanstr);
                    $form.find('#MinHit').val(hit_dans[0] > dans.length ? dans.length : hit_dans[0]);
                    $form.find('#MaxHit').val(hit_dans[1] > dans.length ? dans.length : hit_dans[1]);
                    if (options.Manner == "7") {
                        $form.find("#manner").val("7");
                    } else {
                        if (chuanstr === '1_1') {
                            $form.find("#manner").val("201");
                        } else if (dans.length > 0) {
                            $form.find("#manner").val("1");
                        } else {
                            $form.find("#manner").val("0");
                        }
                    }
                    $.each($.extend({
                        PrizeType: '',
                        SubmitType: 'Combin',
                        FSVer: '',
                        Absmatchorder: ''
                    }, options || {}), function (k) {
                        $form.find('#' + k).val(this);
                    });
                    $form.submit();
                }
            }

            $('#touzhulan').one('mouseenter', function () {
                var $beishu = $('#beishu').on('click', function () {
                    this.focus && this.select();
                });

                function changeBeishu() {
                    var v = this.value,
                        nv = v.replace(/[^0-9]/g, '') * 1 || 1;
                    nv < 1 && (nv = 1);
                    nv > 100000 && (nv = 100000);
                    if ((nv + '') !== v) {
                        this.value = nv;
                    }
                    MultiNum = nv;
                    fn.calculate();
                }

                $beishu.blur(changeBeishu).keyup(function () {
                    this.value !== '' && changeBeishu.call(this);
                });
                $('#beishus').find('.jian,.jia').click(function () {
                    var $t = $(this);
                    if ($t.hasClass('jia')) {
                        $beishu.val(MultiNum + 1);
                    } else {
                        $beishu.val(MultiNum - 1);
                    }
                    changeBeishu.call($beishu.get(0));
                });

                function closeJiangjinpc(e) {
                    var $t = $(e.target);
                    if ($t.hasClass('close') || !$t.parents('#pingce').get(0)) {
                        $('#pingce').hide();
                        $(document).unbind('mousedown', closeJiangjinpc);
                    }
                }

                $('#jiangjin').click(function () {
                    if (zhushuAndMoney[0]) {
                        var youHuaHtml = '<div tpl="1" class="pc_bt" style="">奖金不满意，可使用<a class="quyouhua">奖金优化</a></div>' + '<div tpl="1" class="pc_bt1">' + '<p>奖金不满意，可选择购买优化方案：</p>' + '</div>';
                        $(document).bind('mousedown', closeJiangjinpc);
                        $('#pingce').html('<div class="pc_tit"><p class="float_l tit_1">奖金评测</p><p class="float_l tit_2"><a id="pingcexq">详情</a></p><p class="float_l close" hide="#pingce">X</p></div>' + '<div tpl="0" class="pc_4"><img src="' + StaticUrl + '/style/img/hunhe/jindt.gif" width="160" height="13"></div>' + '<div tpl="2" class="pc_3">未中奖</div>' + '<div tpl="1" class="pc_main">' + '<div class="pc_1">' + '<span>命中场次</span><p>最小奖金 ～ 最大奖金</p>' + '</div>' + '</div>' + (chuan[0] !== '1_1' ? youHuaHtml : '')).find('.close').click(closeJiangjinpc);
                        $('#pingce .quyouhua').click(openyouhua);
                        $('#pingce #pingcexq').click(openPingce);
                        var $p = $('#pingce').attr('hide', 'auto').show(),
                            $lists = $p.find('.pc_main').css('height', 'auto'),
                            $other = $p.find('.pc_bt1');
                        $lists.find('.pc_2').remove();
                        $other.find('.pc_bt1_1').remove();
                        $p.find('[tpl]').hide(), $p.find('[tpl="0"]').show();
                        var post = {
                            LotteryNo: $('#LotteryNo').val(),
                            MultiNum: MultiNum,
                            MinHit: hit_dans[0],
                            MaxHit: hit_dans[1]
                        };
                        post.type = getWf();
                        post.Matches = getMatchs();
                        post.AbsMatches = getAbsMatches();
                        post.PassTypeStr = getChuansStr();
                        if (dans.length === 0 && zhushuAndMoney[0] <= 500 && isDanWanfa && MultiNum > 1) {
                            var minchuan = 0;
                            $.each(chuan, function () {
                                var t = this.split("_")[0] * 1;
                                (t < minchuan || !minchuan) && (minchuan = t);
                            });
                            if (minchuan < osort.length) {
                                post.PrizeType = '2';
                            } else {
                                var isFuXuan = false;
                                $.each(match, function () {
                                    if (this.len > 1) {
                                        isFuXuan = true;
                                        return false;
                                    }
                                });
                                if (isFuXuan) {
                                    if (chuan.length > 1) {
                                        post.PrizeType = '2';
                                    } else {
                                        post.PrizeType = '2,3,4';
                                    }
                                }
                            }
                        }
                        var mn = zhushuAndMoney[0] * 2 * MultiNum;
                        post.PrizeType && (post.LotteryTool = 2, post.TotalMoney = mn);
                        $.post('/Lottery06/SportterySoccer/ajax.php?action=getLotteryBonus', post, function (data) {
                            $p.find('[tpl]').hide();
                            if (data.Error_Code) {
                                $p.find('[tpl="2"]').show();
                            } else {
                                $p.find('[tpl="1"]').show();
                                var PrizeType = 0,
                                    isHit = 0,
                                    showTeptip = false;
                                $.each(data, function (k) {
                                    if (k === 'PrizeType') {
                                        PrizeType = 1;
                                        var isRepeatSp = false;
                                        $.each(choose, function () {
                                            var sparr = [];
                                            $.each(this, function () {
                                                $.each(this, function (k, v) {
                                                    $.inArray(v.sp, sparr) > -1 && (isRepeatSp = true);
                                                    sparr.push(v.sp);
                                                    if (isRepeatSp)
                                                        return false;
                                                });
                                                if (isRepeatSp)
                                                    return false;
                                            });
                                            if (isRepeatSp)
                                                return false;
                                        });
                                        $.each(this, function (tp) {
                                            var t = this;
                                            $('<div class="pc_bt1_1" data-type="' + tp + '" data-val="' + t.lineList + '"><div class="bt1_1">' + (tp === '2' ? '平均优化' : (tp === '3' ? '博热优化' : '博冷优化')) + '</div><div class="bt1_2"><label ' + (t.MinMoney > mn ? 'class="red_ed"' : '') + '>' + t.MinMoney + '</label> ～ <label ' + (t.MaxMoney > mn ? 'class="red_ed"' : '') + '>' + t.MaxMoney + '</label></div><div class="bt1_3"><a ck="gm">购买</a></div><div class="bt1_4">' + ((isRepeatSp && tp != 2) ? ('<span class="repsp">!</span>') : '') + '<a ck="wt">微调</a></div></div>').appendTo($other).find('[ck]').click(function () {
                                                var $t = $(this),
                                                    ck = $t.attr('ck'),
                                                    data = $t.parents('.pc_bt1_1:first').data();
                                                if (ck === 'gm') {
                                                    formPost('/Lottery06/SportterySoccer/BJBetProcess.php', {
                                                        SubmitType: 'Combin',
                                                        Absmatchorder: data.val,
                                                        PrizeType: data.type,
                                                        LotteryTool: 2,
                                                        TotalMoney: mn,
                                                        Manner: "7"
                                                    });
                                                } else {
                                                    formPost('/jingcai/prizereview/', {
                                                        PrizeType: data.type
                                                    }, 'jjyh');
                                                }
                                            });
                                            (isRepeatSp && tp != 2) && (showTeptip = true);
                                        });
                                        showTeptip && $('<p class="reptip"><span>!</span> 同一场比赛出现赔率相同情况，系统已按默认方式分配。</p>').appendTo($other);
                                    } else {
                                        var t = this;
                                        $('<div class="pc_2"><span>' + (t.hitnum ? (isHit = 1, t.hitnum) : (t.dannum + '<a style="width: 30px;display: inline-block;"></a>' + t.tuonum)) + '</span><p><label ' + (t.MinMoney > mn ? 'class="red_ed"' : '') + '>' + t.MinMoney + '</label> ～ <label ' + (t.MaxMoney > mn ? 'class="red_ed"' : '') + '>' + t.MaxMoney + '</label></p></div>').appendTo($lists);
                                    }
                                });
                                if (isHit) {
                                    $p.find('.pc_1 span').html('命中场次');
                                } else {
                                    $p.find('.pc_1 span').html('胆命中    拖命中');
                                }
                                if (PrizeType) {
                                    $other.show();
                                    $p.find('.pc_bt').hide();
                                    $('#pingce').find('.pc_bt').remove();
                                } else {
                                    $other.hide();
                                    $p.find('.pc_bt').show();
                                }
                                if (zhushuAndMoney[2]) {
                                    $p.find('.pc_bt').hide();
                                } else {
                                    $p.find('.pc_bt').show();
                                }
                                $lists.height() > 160 && $lists.height(160);
                            }
                        }, 'json');
                    }
                });

                function openPingce() {
                    if (chuan.length < 1) {
                    } else
                        formPost('/Lottery06/SportterySoccer/SportteryTest.php', {});
                }

                $('#pingcexiangqing').click(openPingce);

                function openyouhua() {
                    var msg = CanJjyh();
                    if (msg) {
                        alert(msg);
                    } else {
                        formPost('/jingcai/prizereview/', {
                            PrizeType: 2
                        }, 'jjyh');
                    }
                }

                $('#jiangjinyouhua').click(openyouhua);
                $('#xuandanguolv').click(function () {
                    var msg = CanGlkc()
                    if (msg) {
                        alert(msg);
                    } else {
                        formPost('/Lottery06/SportterySoccer/BJBetProcess.php', {
                            SubmitType: 'FilterShrink',
                            FSVer: 3
                        }, 'glkc');
                    }
                });
                $('#determine').click(function () {
                    formPost('/Lottery06/SportterySoccer/BJBetProcess.php', {
                        SubmitType: 'Combin'
                    });
                });
            });
            $('#danmax,#danmin').hover(function () {
                $(this).find('.jia').addClass('bg');
            }, function () {
                $(this).find('.jia').removeClass('bg');
            });

            function updateSp() {
                var parm = {
                    LotteryType: 'SportteryNWDL,SportteryWDL'
                };
                try {
                    $.get('/I/?method=lottery.spinfo.mixspinfo', parm, function (data) {
                        var data = data.spinfo_mixspinfo_response;
                        $.each(MatchIndex, function () {
                            var $t = this['$t'],
                                morder = this.morder;
                            if (!this.end) {
                                $.each(Config.wf, function (k) {
                                    var wf = this,
                                        t = data[k] ? (data[k][morder] || {}) : {};
                                    $.each(t, function (wz) {
                                        var $o = $t.find('[data-wf="' + wf + '"][data-wz="' + wz + '"]');
                                        $o.attr('data-sp', this).data('sp', this);
                                        $o.find('.peilv').html(this);
                                    });
                                });
                            }
                        });
                    }, 'json');
                }
                catch (err) {
                    alert("Error: " + err);
                }
            }

            var $w = $(window),
                $footer = $('#touzhulan'),
                $content = $('#wrapper_content'),
                footer_h = $footer.height(),
                $last = $content.last(),
                //position = $.browser.isIe6 ? 'absolute' : 'fixed';
                position = 'fixed';

            function resizeFooter() {
                var ofs = $content.offset(),
                    wh = $w.height(),
                    top = ofs.top + $content.height() + footer_h + 6,
                    wtop = $w.scrollTop() + wh;
                if (wtop < top) {
                    $footer.css({
                        position: position,
                        top: ($.browser.isIe6 ? wtop - footer_h : wh - footer_h),
                        left: $last.offset().left
                    });
                } else {
                    $footer.css({
                        position: 'relative',
                        top: 4,
                        left: 1
                    });
                }
            }

            $w.scroll(resizeFooter), $w.resize(resizeFooter);
            resizeFooter(), setInterval(updateSp, 1000 * 100);
            $('#stopdetermine').hover(function () {
                $('#tixing').show();
            }, function () {
                $('#tixing').hide();
            });
            $guogfs = $('#guogfs');

            function bdCloseGuogfs(e) {
                var $t = e ? $(e.target) : 0;
                if ($t && ($t.parents('#guogfsbtn').get(0) || $t.attr('id') === 'guogfsbtn')) {
                    return;
                }
                if (!$t || (!$t.parents('.guofs_zk').get(0) && !$t.hasClass('guofs_zk'))) {
                    $guogfs.removeClass('ggss').find('.guofs_zk:first').hide();
                    $(document).unbind('mousedown', bdCloseGuogfs);
                }
            }

            $('#guogfsbtn').click(function () {
                var $pop = $guogfs.find('.guofs_zk:first');
                if ($pop.css('display') === 'none') {
                    fn.resetMoreChuan();
                    $pop.show(), $guogfs.addClass('ggss');
                } else {
                    bdCloseGuogfs();
                }
            })
            $('#content').one('mouseenter', function () {
                moreChuan = $('#guogfsbtn input').click(function () {
                    $('#guogfsbtn').click();
                    return false;
                }).get(0);
                var $more = $guogfs.find('.guofs_zk'),
                    $last = $more.find('.ooo:first');
                $.each(chuanMore, function (k) {
                    var $row = $('<div id="more_' + k + '" class="zk_1" style="display:none;"></div>').insertBefore($last);
                    $.each(this, function (c, v) {
                        var $one = $('<p><input id="chuan' + c + '" type="checkbox" value="' + c + '"><label for="chuan' + c + '">' + v + '</label></p>').appendTo($row);
                        chuanMore[k][c] = $one.find('input').click(function () {
                            if (this.checked) {
                                if (chuan.length >= 5) {
                                    this.checked = false;
                                    alert('组合过关方式不能找过5个');
                                    return false;
                                }
                                fn.addChuan(this.value);
                                moreChuan.checked = true;
                            } else {
                                fn.delChuan(this.value);
                                var isMoreChuan = false;
                                $.each(chuanMore, function () {
                                    $.each(this, function () {
                                        if (this.checked) {
                                            isMoreChuan = true;
                                            return false;
                                        }
                                    });
                                });
                                moreChuan.checked = isMoreChuan;
                            }
                            fn.calculate();
                        }).get(0);
                    });
                });
            });
            $('#hideguogfs').click(function () {
                bdCloseGuogfs();
            });
        });
        window.fn = fn;
    })(jQuery);

    $(function () {
        $(document).on('mouseenter', '[hover]', function () {
            var $t = $(this),
                css = $t.attr('hover'),
                delay = $t.attr('hover-delay') * 1000 || 0;
            $t.attr('hover-delay-ing', '1');
            if (delay) {
                setTimeout(function () {
                    $t.attr('hover-delay-ing') === '1' && $t.addClass(css);
                }, delay);
            } else {
                $t.addClass(css);
            }
        });
        $(document).on('mouseleave', '[hover]', function () {
            var $t = $(this),
                css = $t.attr('hover');
            $t.removeClass(css).attr('hover-delay-ing', '');
        });
        setTimeout(function () {
            $('[sel]').click(function () {
                var $t = $(this),
                    self = this,
                    css = $t.attr('sel') || 'sel',
                    group = $t.attr('sel-group') || '';
                if ($t.attr('sel-essential') && $t.hasClass(css)) {
                    return;
                } else {
                    if (group) {
                        $('[sel][sel-group="' + group + '"]').each(function () {
                            this !== self && $(this).removeClass($(this).attr('sel'));
                        });
                    }
                    $t.hasClass(css) ? $t.removeClass(css) : $t.addClass(css);
                }
            });
        }, 8000);
    });
};
/*! caipiao siguang.liu 2016-02-24 v0.1.0  */
define("../js/example/zc.buy", ["lib-cpBase", "lib-json", "./zc.tool.js", "./zc.odd.js"], function (t, a) {
    function e(t, a) {
        this.config = {
            30001: {
                name: "胜负彩14场",
                len: 14,
                oldId: 13,
                r9: !1,
                tm: 3,
                typeArr: ["3", "1", "0"]
            },
            50001: {
                name: "胜负彩14场包胆包赔",
                len: 14,
                oldId: 13,
                r9: !1
            },
            30102: {
                name: "胜负彩任9场",
                len: 14,
                oldId: 13,
                r9: !0
            },
            3010203: {
                name: "胜负彩任9场",
                len: 14,
                oldId: 13,
                dt: !0,
                r9: !0,
                tm: 3,
                maxDan: 8,
                typeArr: ["3", "1", "0"]
            },
            30203: {
                name: "六场半全场",
                len: 12,
                oldId: 15,
                r9: !1,
                tm: 3,
                typeArr: ["3", "1", "0"]
            },
            30304: {
                name: "四场进球",
                len: 8,
                oldId: 16,
                r9: !1,
                px: !0,
                tm: 4,
                typeArr: ["0", "1", "2", "3"]
            }
        },
        this.lotId = t,
        this.playId = a,
        this.oldId = this.config[t + a].oldId,
        this.datas = [],
        this.nb = [],
        this.dan = [],
        this.curConfig = {},
        this.count = 0,
        this.amount = 0,
        this.multiple = 1,
        this.buyType = 2,
        this.issue = "",
        this.jiezhi = !1,
        this.init()
    }
    var s = t("lib-cpBase").CPB,
        i = t("lib-json").JSON,
        l = t("./zc.tool"),
        n = s.sf;
    e.prototype = {
            init: function () {
                var t = this,
                    a = this.config;
                t.curConfig = a[t.lotId + t.playId],
                t.initData(),
                t.bindEvent();
                var e = window.zcTool = l.zcTool();
                e.init()
            },
            bindEvent: function () {
                var t = this,
                    a = t.curConfig;
                $("#zcList .n").die().live("click", function () {
                        if (0 >= zcTool.leftTime || "已截止" == $("#remainTime").text()) return alert("您的投注期次已截期，可以选择下一期再投."),
                        !1;
                        var a = $(this).attr("id").split("_"),
                            e = a[1],
                            s = a[2],
                            i = $(this).hasClass("x"),
                            l = 0;
                        i ? $(this).removeClass("x") : ($(this).addClass("x"), l = 1),
                        t.handleNumber(l, e, s),
                        t.handleAllBtn(),
                        t.outSelectField(),
                        t.getMoney()
                    }),
                $("#zcList .quan").die().live("click", function () {
                        if (0 >= zcTool.leftTime || "已截止" == $("#remainTime").text()) return alert("您的投注期次已截期，可以选择下一期再投."),
                        !1;
                        var e = $(this).attr("class").indexOf("tgr");
                        if (-1 != e) return !1;
                        var s = $(this).attr("id").split("_"),
                            i = $(this).hasClass("xz"),
                            l = s[1],
                            n = a.typeArr,
                            r = a.tm,
                            o = 0;
                        if (i) for (var c = 0; r > c; c++) $("#n_" + l + "_" + n[c]).removeClass("x");
                        else {
                                for (var c = 0; r > c; c++) $("#n_" + l + "_" + n[c]).addClass("x");
                                o = 1
                            }
                        t.handleNumber(o, l, "h"),
                        t.handleAllBtn(),
                        t.outSelectField(),
                        t.getMoney()
                    }),
                $("#allNum .all").die().live("click", function () {
                        if (0 >= zcTool.leftTime || "已截止" == $("#remainTime").text()) return alert("您的投注期次已截期，可以选择下一期再投."),
                        !1;
                        var e = $(this).attr("id").split("_"),
                            s = Number(e[1]),
                            i = a.len,
                            l = $(this).attr("checked"),
                            n = 0;
                        if (l) {
                                for (var r = 0; i > r; r++) $("#n_" + r + "_" + s).addClass("x");
                                n = 1
                            } else for (var r = 0; i > r; r++) $("#n_" + r + "_" + s).removeClass("x");
                        t.handleNumber(n, s, "v"),
                        t.handleAllBtn(),
                        t.outSelectField(),
                        t.getMoney()
                    }),
                $("#clearAll").unbind().bind("click", function () {
                        t.clearAll()
                    }),
                $("#doubleUp").unbind().bind("click", function () {
                        var a = t.multiple,
                            e = $("#double");
                        a++,
                        a > 99 ? (t.multiple = 99, e.val("99"), $(this).removeClass("jia_manu").addClass("jia_manu_d")) : (t.multiple = a, e.val(a), $(this).removeClass("jia_manu_d").addClass("jia_manu")),
                        $("#doubleDown").hasClass("jian_manu_d") && $("#doubleDown").removeClass("jian_manu_d").addClass("jian_manu"),
                        t.getMoney()
                    }),
                $("#doubleDown").unbind().bind("click", function () {
                        var a = t.multiple,
                            e = $("#double");
                        a--,
                        1 > a ? (t.multiple = 1, e.val("1"), $(this).removeClass("jian_manu").addClass("jian_manu_d")) : (t.multiple = a, e.val(a), $(this).removeClass("jian_manu_d").addClass("jian_manu")),
                        $("#doubleUp").hasClass("jia_manu_d") && $("#doubleUp").removeClass("jia_manu_d").addClass("jia_manu"),
                        t.getMoney()
                    }),
                $("#double").unbind().bind("keyup", function () {
                        var a = $(this).val(),
                            e = /\D/g;
                        $("#doubleUp").removeClass("jia_manu_d").addClass("jia_manu"),
                        $("#doubleDown").removeClass("jian_manu_d").addClass("jian_manu"),
                        e.test(a) ? $(this).val(t.multiple) : Number(a) > 99 ? (t.multiple = 99, $(this).val("99"), $("#doubleUp").removeClass("jia_manu").addClass("jia_manu_d")) : 1 > Number(a) ? (t.multiple = 1, $(this).val("1"), $("#doubleDown").removeClass("jian_manu").addClass("jian_manu_d")) : t.multiple = Number(a),
                        t.getMoney()
                    }),
                $("#zcList .dan").die().live("click", function () {
                        for (var e = $(this).attr("checked"), s = Number($(this).val()), i = t.nb, l = 0, n = 0, r = t.dan.length; r > n; n++) t.dan[n].select && l++;
                        if (e) {
                            if (0 == i[s].length) return $(this).attr("checked", !1),
                            alert("您未选中这场比赛，不能将这场比赛作胆"),
                            !1;
                            if (l >= a.maxDan) return $(this).attr("checked", !1),
                            alert("胆的个数不能超过8个"),
                            !1;
                            t.dan[s].select = !0
                        } else t.dan[s].select = !1;
                        t.getMoney()
                    }),
                $("#danTip").mouseover(function () {
                        $("#dantipsEm").show()
                    }),
                $("#dantips").click(function () {
                        $("#dantipsEm").hide()
                    }),
                $(".tzWf").hover(function () {
                        $(this).addClass("show")
                    }, function () {
                        $(this).removeClass("show")
                    }),
                $("#issueList a").die().live("click", function () {
                        var a = $("#issueList a").index($(this));
                        $("#wIssue option:eq(0)").attr("selected", "selected"),
                        $(".betHead").show(),
                        $(".bifen").hide(),
                        $(".w-8-caiguo").hide(),
                        $("#caiguo").hide(),
                        $(".footer-fix").show(),
                        $("#lotteryList").removeClass("lcbqcw"),
                        zcTool.appendIssue(a),
                        t.clearAll()
                    }),
                $("#wIssue").change(function () {
                        var a = $(this).val();
                        $(".all").removeAttr("checked"),
                        2 == a ? $("#issueList a:eq(0)").click() : -1 != a ? ($(".betHead").show(), $(".bifen").hide(), $(".w-8-caiguo").hide(), $("#caiguo").hide(), $("#issueList a:eq(" + a + ")").click(), $(".footer-fix").show()) : ($(".betHead").hide(), $(".bifen").show(), $(".w-8-caiguo").show(), $("#caiguo").show(), $(".footer-fix").hide(), $("#issueList li").attr("class", ""), zcTool.curIssue = $("#wIssue :selected").text(), t.getWangMatchInfo(zcTool.curIssue))
                    }),
                $("div.tzbtn").delegate("a", "click", function () {
                        var a = "";
                        a = "dgBuy" == $(this).attr("id") ? "dg" : "hm",
                        t.getBuyInfo(a)
                    })
            },
            isBackEdit: function () {
                var t = this,
                    a = t.curConfig;
                if (isEdit = $.cookie("zcBackEdit"), dataStore = i.parse(decodeURI($.cookie("zcLotteryNumber"))), !isEdit) return $.cookie("zcBackEdit", null, {
                        path: "/"
                    }),
                !1;
                if (zcTool.curIssue != dataStore.issus || 0 >= zcTool.leftTime) return alert("期次已截期请重新选择号码"),
                $.cookie("zcBackEdit", null, {
                        path: "/"
                    }),
                !1;
                var e = dataStore.buyNumber,
                    s = [],
                    l = t.datas,
                    n = dataStore.multiple;
                if (t.multiple = n, $("#double").val(n), a.r9) if (-1 != e.indexOf("@")) {
                        var r = e.split("@");
                        s = r[0].split("*"),
                        oDanAr = r[1].split("*")
                    } else s = e.split("*");
                else s = e.split("*");
                if (a.r9 && -1 != e.indexOf("@")) {
                        for (var o = 0, c = s.length; c > o; o++) {
                            var d = s[o].split("");
                            "4" != d && $.each(d, function (t, a) {
                                l[o][a] = !0,
                                $("#n_" + o + "_" + a).trigger("click")
                            })
                        }
                        $.each(oDanAr, function (a, e) {
                            var s = Number(e) - 1;
                            t.dan[s].select = !0,
                            $("#zcList .dan").eq(s).attr("checked", !0)
                        }),
                        t.getMoney()
                    } else for (var o = 0, c = s.length; c > o; o++) {
                        var d = s[o].split("");
                        t.r9 || "4" == d || $.each(d, function (t, a) {
                            l[o][a] = !0,
                            $("#n_" + o + "_" + a).trigger("click")
                        })
                    }
                $.cookie("zcBackEdit", null, {
                        path: "/"
                    }),
                $.cookie("zcLotteryNumber", null, {
                        path: "/"
                    })
            },
            isDetailed: function () {
                var t = this,
                    a = t.curConfig;
                isDetailed = $.cookie("zcDetailed");
                var e = decodeURI($.cookie("zcLotteryNumber")),
                    s = e.split("^");
                if (!isDetailed) return $.cookie("zcDetailed", null, {
                        path: "/"
                    }),
                !1;
                if (zcTool.curIssue != s[5] || 0 >= zcTool.leftTime) return alert("期次已截期请重新选择号码"),
                $.cookie("zcDetailed", null, {
                        path: "/"
                    }),
                !1;
                var i = s[1],
                    l = [],
                    n = t.datas,
                    r = s[3];
                if (t.multiple = r, $("#double").val(r), a.r9 ? s[6] ? (l = i.split("*"), oDanAr = s[6].split(",")) : l = i.split("*") : l = i.split("*"), a.r9 && s[6]) {
                        for (var o = 0, c = l.length; c > o; o++) {
                            var d = l[o].split("");
                            "4" != d && $.each(d, function (t, a) {
                                n[o][a] = !0,
                                $("#n_" + o + "_" + a).trigger("click")
                            })
                        }
                        $.each(oDanAr, function (a, e) {
                            var s = Number(e) - 1;
                            t.dan[s].select = !0,
                            $("#zcList .dan").eq(s).attr("checked", !0)
                        }),
                        t.getMoney()
                    } else for (var o = 0, c = l.length; c > o; o++) {
                        var d = l[o].split("");
                        t.r9 || "4" == d || $.each(d, function (t, a) {
                            n[o][a] = !0,
                            $("#n_" + o + "_" + a).trigger("click")
                        })
                    }
                $.cookie("zcDetailed", null, {
                        path: "/"
                    }),
                $.cookie("zcLotteryNumber", null, {
                        path: "/"
                    })
            },
            getWangMatchInfo: function (t) {
                var a = this;
                $.get("/lottery/zcplayvs.action?lotteryId=" + a.oldId + "&issue=" + t + "&v=" + n.getTimeStamp(), function (t) {
                    var e = i.parse(t);
                    $("#zcList").html("<tr><td colspan='11'>数据载入中……</tr></td>"),
                    $("#periodStatus").html("已截止"),
                    a.makeWangMatchInfo(e)
                })
            },
            makeWangMatchInfo: function (t) {
                var a = this,
                    e = "",
                    i = t.matchInfo;
                if (!t) return !1;
                for (var l = 0, n = i.length; n > l; l++) {
                        var r = i[l],
                            o = 2 * l,
                            c = 2 * l + 1,
                            d = s.str.formatDate(r.gameStartDate),
                            h = (r.homePh.split("|"), r.guestPh.split("|"), r.europeSp.split(" ")),
                            p = r.zuizhongbifen.split(";"),
                            u = "quan",
                            m = "beginBet";
                        l % 2 && (m = "beginBet oddBeginBet");
                        var f = "";
                        2 == r.leageType ? f = '<a href="http://saishi.zgzcw.com/soccer/cup/' + r.legageId + '" target="_blank">' + r.leageName + "</a>" : 1 == r.leageType && (f = '<a href="http://saishi.zgzcw.com/soccer/league/' + r.legageId + '" target="_blank">' + r.leageName + "</a>");
                        var g = !1;
                        if (-1 != r.playId.indexOf("-") && (r.playId = r.playId.replace("-", ""), g = !0), "303" == lotteryId) {
                                for (var v = [], b = 0; p.length > b; b++) for (var y = p[b].split(":"), z = 0; y.length > z; z++) v.push(y[z]);
                                var w = "-",
                                    I = "-";
                                w = v[o],
                                w = w > 3 ? "3+" : w,
                                I = v[c],
                                I = I > 3 ? "3+" : I,
                                zcTool.fs || (u = "quan tgr"),
                                e += '<tr id="tr_' + r.playId + '" class="' + m + '">' + '<td class="wh-1">' + "   <span><i>" + (o + 1) + "</i></span><hr><span>" + (c + 1) + "</span>" + "</td>" + '<td class="wh-2 b-l">' + "  <span>" + f + "</span>" + "</td>" + '<td class="wh-3">' + d + "</td>" + '<td class="wh-4 b-l r-s">' + '   <div class="tfr">' + a.parsePh(r.homePh) + '       <span> 主：<a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a></span>" + "   </div>" + "   <div>" + a.parsePh(r.guestPh) + '       <span>客：<a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a></span>" + "    </div>" + "</td>" + '<td class="wh-8 b-l xh">' + "   <strong>" + v[o] + "</strong><hr><strong>" + v[c] + "</strong>" + "</td>" + '<td class="bf b-l">' + "   <span>" + w + "</span><hr><span>" + I + "</span>" + "</td>" + '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + g + '" target="view_window">亚</a>' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + g + '" target="view_window">欧</a>' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + g + '" target="view_window">析</a>' + "</td>" + '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" + '<td class="wh-15 b-l">' + zcTool.makeTzbl(r.renqiInfo) + "</td>" + "</tr>"
                            } else if ("302" == lotteryId) {
                                $("#lotteryList").addClass("lcbqcw");
                                var _ = p[l].split(","),
                                    j = _[0].replace(/-/g, ":"),
                                    k = _[1].replace(/-/g, ":"),
                                    I = a.retMatchResult(_[0]),
                                    x = a.retMatchResult(_[1]);
                                e += '<tr id="tr_' + r.playId + '" class="' + m + '">' + '<td class="wh-1">' + "   <span><i>" + (o + 1) + "</i></span><hr><span>" + (c + 1) + "</span>" + "</td>" + '<td class="wh-2 b-l">' + "  <span>" + f + "</span>" + "</td>" + '<td class="wh-3">' + d + "</td>" + '<td class="wh-4 t-r r-l">' + a.parsePh(r.homePh) + '   <a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a></td>" + '<td class="wh-5">VS</td>' + '<td class="wh-6 t-l r-r"><a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a>" + a.parsePh(r.guestPh) + "</td>" + '<td class="wh-10 b-l"><span>半场</span><hr><span>全场</span></td>' + '<td class="wh-10 b-l bf"><span>' + j + "</span><hr><span>" + k + "</span></td>" + '<td class="wh-8 xh b-l"><span class="tre">' + I + '</span><hr><span class="tre">' + x + "</span></td>" + '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + g + '" target="view_window">亚</a>' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + g + '" target="view_window">欧</a>' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + g + '" target="view_window">析</a>' + "</td>" + '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" + '<td class="wh-15 b-l">' + zcTool.makeTzbl(r.renqiInfo) + "</td>" + "</tr>"
                            } else if ("300" == lotteryId || "301" == lotteryId) {
                                var T = r.zuizhongbifen.split(";"),
                                    w = a.retMatchResult(T[l]);
                                e += '<tr id="tr_' + r.playId + '" class="' + m + '">' + '<td class="wh-1">' + "   <i>" + (l + 1) + "</i>" + "</td>" + '<td class="wh-2 b-l">' + "  <span>" + f + "</span>" + "</td>" + '<td class="wh-3">' + d + "</td>" + '<td class="wh-4 t-r r-l">' + a.parsePh(r.homePh) + '   <a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a>" + "</td>" + '<td class="wh-5 bf">' + p[l].replace("-", ":") + "</td>" + '<td class="wh-6 t-l r-r"><a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a>" + a.parsePh(r.guestPh) + "</td>" + '<td class="wh-8-caiguo b-l bf">' + w + "</td>" + '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + g + '" target="view_window">亚</a>' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + g + '" target="view_window">欧</a>' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + g + '" target="view_window">析</a>' + "</td>" + '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" + '<td class="wh-15 b-l">' + zcTool.makeTzbl(r.renqiInfo) + "</td>" + "</tr>"
                            }
                    }
                $("#zcList").html(e)
            },
            parsePh: function (t) {
                if ("" == t || 3 != t.split("|").length) return "";
                var a = t.split("|");
                return "-" == a[1] ? '<em class="pm oneDat" _r="1">[' + a[0] + "]</em>" : "-" == a[2] ? '<em class="pm" _r="1">[' + a[0] + "]<br /><i>" + a[1] + "</i></em>" : '<em class="pm" _r="1">[' + a[0] + ']<br /><i title="' + a[2] + '">' + a[1] + "</i></em>"
            },
            retMatchResult: function (t) {
                if ("*" == t) return t;
                var a, e = t.split("-"),
                    s = Number(e[0]),
                    i = Number(e[1]);
                return "" == e[0] || "" == e[1] ? "-" : (s > i ? a = 3 : s == i ? a = 1 : i > s && (a = 0), a)
            },
            resetRx9Info: function () {},
            clearAll: function () {
                $("#totalItem").text("0"),
                $("#totalSum").text("0"),
                $("#changLen").text("0"),
                $("#v1Len").text("0"),
                $("#v2Len").text("0"),
                $("#v3Len").text("0"),
                $("#v4Len").text("0"),
                $("#changPos").parent().hide(),
                this.count = 0,
                this.amount = 0,
                $("#zcList .n").removeClass("x"),
                $("#zcList .quan").removeClass("xz"),
                $("#allNum input").attr("checked", !1),
                this.curConfig.r9 && $("#zcList .dan").attr("checked", !1),
                this.initData()
            },
            initData: function () {
                var t = this,
                    a = t.curConfig,
                    e = a.len;
                t.datas = [],
                t.nb = [];
                for (var s = 0; e > s; s++) {
                        var i = t.getModelData(),
                            l = [];
                        if (t.datas.push(i), t.nb.push(l), a.r9) {
                                var n = {};
                                n.select = !1,
                                t.dan.push(n)
                            }
                    }
            },
            getModelData: function () {
                for (var t = this, a = t.curConfig, e = a.typeArr, s = a.tm, i = {}, l = 0; s > l; l++) i[e[l]] = !1;
                return i
            },
            handleNumber: function (t, a, e) {
                var s = this,
                    i = s.curConfig,
                    l = s.datas,
                    n = !0,
                    r = i.typeArr;
                if (a = Number(a), n = 0 == t ? !1 : !0, "h" == e ? $.each(r, function (t) {
                        l[a][r[t]] = n
                    }) : "v" == e ? $.each(l, function (t) {
                        l[t][a] = n
                    }) : l[a][e] = n, i.r9) {
                        var n = !1;
                        $.each(l[a], function (t, a) {
                            return a ? (n = !0, !1) : void 0
                        }),
                        n || ($("#dan_" + a).attr("checked", !1), s.dan[a].select = !1)
                    }
                s.handleNb()
            },
            handleNb: function () {
                for (var t = this, a = t.datas, e = t.nb, s = 0, i = a.length; i > s; s++) e[s] = [],
                $.each(a[s], function (t, a) {
                    a && e[s].push(t)
                })
            },
            outSelectField: function () {
                var t = this,
                    a = t.nb,
                    e = 0,
                    s = 0,
                    i = 0,
                    l = 0,
                    n = 0;
                $.each(a, function (t, a) {
                        var r = a.length;
                        switch (r) {
                        case 1:
                            e++,
                            n++;
                            break;
                        case 2:
                            s++,
                            n++;
                            break;
                        case 3:
                            i++,
                            n++;
                            break;
                        case 4:
                            l++,
                            n++;
                            break;
                        default:
                        }
                        $("#changLen").text(n),
                        $("#v1Len").text(e),
                        $("#v2Len").text(s),
                        $("#v3Len").text(i),
                        $("#v4Len").text(l)
                    })
            },
            handleAllBtn: function () {
                for (var t, a = this, e = a.curConfig, s = a.datas, i = e.typeArr, l = s.length, n = a.curConfig.tm, r = 0; l > r; r++) {
                    var o = $("#quan_" + r);
                    t = !0,
                    $.each(s[r], function (a, e) {
                        e || (t = !1)
                    }),
                    t ? o.addClass("xz") : o.removeClass("xz")
                }
                for (var c = 0; n > c; c++) {
                    var d = $("#all_" + i[c]);
                    t = !0;
                    for (var h = 0; l > h; h++) s[h][i[c]] || (t = !1);
                    t ? d.attr("checked", !0) : d.attr("checked", !1)
                }
            },
            getR9DataFormat: function () {
                var t = this,
                    a = [],
                    e = t.dan,
                    s = this.nb;
                return $.each(s, function (t, s) {
                        var i = s.length;
                        if (i > 0 && !e[t].select) {
                            var l = [];
                            l.push(i + "-zc"),
                            a.push(l)
                        }
                    }),
                a
            },
            getBetting: function () {
                var t = this,
                    a = t.curConfig,
                    e = t.nb,
                    s = e.length,
                    i = 1,
                    l = [];
                if (a.r9) {
                        i = 0;
                        for (var r = 0, o = t.dan.length; o > r; r++) if (t.dan[r].select) {
                            var c = e[r].length,
                                d = [c + "-zc"];
                            l.push(d)
                        }
                        var h = t.getR9DataFormat(),
                            p = n.dl(h, l, 9);
                        $.each(p, function (t, a) {
                                var e = n.myCal("9_1", a);
                                i += e
                            })
                    } else for (var r = 0; s > r; r++) i *= e[r].length;
                return i
            },
            getMoney: function () {
                var t = this,
                    a = t.getBetting();
                if ("301" == t.lotId) {
                        var e = Number($("#changLen").html());
                        e > 9 ? $("#changPos").html(e).parent().show() : $("#changPos").parent().hide()
                    }
                t.count = a,
                t.amount = 2 * a * t.multiple,
                $("#totalItem").text(a),
                $("#totalSum").text(t.amount)
            },
            getBuyInfo: function (t) {
                var a = this,
                    e = this.config[a.lotId + a.playId];
                if (0 == a.count) return alert("你尚未选择号码"),
                !1;
                if (Number(a.amount) > 15e5) return alert("方案总金额不能大于150万!"),
                !1;
                var s = [],
                    l = "02",
                    n = "",
                    r = [],
                    o = [],
                    c = [];
                if (e.r9) {
                        for (var d = 0, h = a.datas.length; h > d; d++) {
                            var p = a.datas[d],
                                u = [];
                            $.each(p, function (t, a) {
                                    a && u.push(t)
                                }),
                            0 == u.length ? r.push("4") : r.push(u.join(""))
                        }
                        for (var m = 0, f = a.dan.length; f > m; m++) a.dan[m].select && o.push(m + 1);
                        o.length > 0 ? (n = r.join("*") + "@" + o.join("*"), l = "03") : n = r.join("*")
                    } else for (var d = 0, h = a.datas.length; h > d; d++) {
                        var p = a.datas[d],
                            u = [];
                        $.each(p, function (t, a) {
                                a && u.push(t)
                            }),
                        s.push(u.join(""))
                    }
                for (var g = zcTool.matchInfo.length, v = 0; g > v; v++) {
                        var b = zcTool.matchInfo[v],
                            y = {
                                hostName: b.hostNameFull,
                                guestName: b.guestNameFull,
                                hostId: b.hostId,
                                guestId: b.guestId
                            };
                        c.push(y)
                    }
                var z = {
                        lotteryId: a.lotId,
                        playId: a.playId,
                        modelId: a.oldId,
                        issus: zcTool.curIssue,
                        count: a.count,
                        multiple: a.multiple,
                        totalSum: a.amount,
                        buyNumber: e.r9 ? n : s.join("*"),
                        buyMatch: c,
                        betsType: t
                    },
                    w = "",
                    I = encodeURI(i.stringify(z));
                $("#lotteryData_match").text(I),
                "dg" == t ? (w = "/lottery/zucai/zc_affirm.jsp", $("#affForm").attr("action", w).submit()) : "hm" == t && (w = "/lottery/zucai/zc_buy_affirm.jsp", $("#affForm").attr("action", w).submit())
            }
        };
    var r = function (t, a) {
            return new e(t, a)
        };
    a.zc = r
}),
define("../js/example/zc.tool", ["lib-cpBase", "lib-json", "./zc.odd.js"], function (t, a) {
    function e() {
        this.args = {
            300: {
                name: "胜负彩14场",
                oldId: 13,
                cpTime: "周一至周五 9:00-00:00 周六/日 9:00-01:00"
            },
            301: {
                name: "胜负彩任9场",
                oldId: 13,
                cpTime: "周一至周五 9:00-00:00 周六/日 9:00-01:00"
            },
            302: {
                name: "六场半全场",
                oldId: 15,
                cpTime: "周一至周五 9:00-00:00 周六/日 9:00-01:00"
            },
            303: {
                name: "四场进球",
                oldId: 16,
                cpTime: "周一至周五 9:00-00:00 周六/日 9:00-01:00"
            }
        },
        this.oldId = this.args[lotteryId].oldId,
        this.lotId = lotteryId,
        this.curIssue = "",
        this.issues = [],
        this.ysIssues = [],
        this.matchInfo = [],
        this.colorful = ["rq_red", "rq_blue", "rq_green", "rq_light"],
        this.timer = null,
        this.leftTime = 0,
        this.fs = !0,
        this.isUp = !1,
        this.isDt = !1,
        this.jiezhi = !1,
        this.curUrl = n.urlHas(),
        "ds" == this.curUrl.nav && 1 == this.isUp,
        "3010203" == lotteryId + playId && (this.isDt = !0, this.time2Fs = 0),
        this.isSale()
    }
    var s = t("lib-cpBase").CPB,
        i = t("lib-json").JSON,
        l = s.sf,
        n = s.str,
        r = s.arr;
    e.prototype = {
            constructor: e,
            init: function () {
                this.appendIssue(0),
                this.getPrizeInfo(),
                $("#zcList .sj").die().live("click", function () {
                    var t = $(this).attr("data-href"),
                        a = $(this).attr("data-zlc");
                    "true" == a ? $.confirm("您好，您将要打开的数据页面中，主客场位置与现在投注页相反，投注时请确认主客队位置，一旦提交，我们将按照您的所选选项执行。", function () {
                            window.open(t)
                        }) : window.open(t)
                })
            },
            isSale: function () {
                var t = $.trim($.ajax({
                    url: "/lottery/checkLottery.action?lotteryId=" + lotteryId,
                    async: !1
                }).responseText);
                "true" == t && (this.jiezhi = !1),
                this.jiezhi = !1
            },
            appendIssue: function (t) {
                var a = this,
                    e = "/lottery/getissue.action?lotteryId=" + lotteryId + "&issueLen=20&d=" + l.getTimeStamp();
                $.get(e, function (e) {
                        var l = i.parse(e),
                            r = l.length - 1,
                            o = "";
                        if (0 == l.length) return !1;
                        a.curIssue = l[t].issue;
                        for (var c = 0, d = l.length; d > c; c++) {
                                var h = "",
                                    p = c == t ? "cur" : "";
                                a.isUp && $("#dsNowUp").attr("checked") && 1 != l[c].status && (h = "display:none"),
                                o += '<li class="' + p + '" style="' + h + '" _status="' + l[c].status + '"><a href="javascript:void(0);" value="' + l[c].issue + '">' + l[c].issue + "期</a></li>",
                                0 > r && (wIssue += '<option value="' + r + '">' + l[r].issue + "</option>"),
                                r--,
                                1 != l[c].status && a.ysIssues.push(l[c].issue),
                                a.issues.push(l[c].issue),
                                n.urlHas().issue && ($("#money").html(n.urlHas().money), $("#money1").html(Math.ceil(.7 * n.urlHas().money)), n.urlHas().issue == l[c].issue && (t = c))
                            }
                        a.isUp ? a.leftTime = l[t].fsLeftTime : a.isDt ? (a.leftTime = l[t].fsLeftTime, 0 > a.leftTime ? (a.leftTime = l[t].leftTime, a.isDt = !1) : a.time2Fs = l[t].leftTime - a.leftTime, a.switchRx9Fs()) : a.leftTime = l[t].leftTime,
                        clearInterval(a.timer),
                        $("#remainTime").html("加载中..."),
                        a.jiezhi ? $("#remainTime").html("已停售") : a.timer = setInterval(function () {
                                a.remainTime()
                            }, 1e3);
                        var u = "";
                        1 != l[t].status ? ($("#allNum .all").attr("disabled", !0), a.fs = !1, u = "未开售") : ($("#allNum .all").attr("disabled", !1), a.fs = !0, u = "热销中"),
                        $("#periodStatus").html(u);
                        var m = "";
                        if (a.isUp) {
                                m = s.str.parseDate(l[t].fsEndTime);
                                var f = new Date(m.getTime() - 24e5);
                                $("#more10w").html(s.str.formatDateA(f)),
                                $("#ge10w").html(s.str.formatDateA(f))
                            }
                        m = l[t].fsEndTime,
                        $("#endTime").html(l[t].endTime),
                        $("#issueList").html(o),
                        $("#dsEndTime").html(m).attr("dsEndTime", m),
                        $("#uploadEndTime").html(m),
                        a.getRaceData(a.curIssue),
                        a.getIssueSelectList()
                    })
            },
            remainTime: function () {
                var t = this;
                if (0 > t.leftTime) return t.time2Fs ? (t.leftTime = t.time2Fs - 1, t.time2Fs = 0, void 0) : (clearInterval(t.timer), t.clearGreenChannel(), !1);
                var a = 0,
                    e = 0,
                    s = 0,
                    i = 0,
                    l = 0,
                    n = 0;
                if (t.leftTime > 86400 ? (a = Math.floor(t.leftTime / 86400), l = t.leftTime % 86400, l > 3600 ? (e = Math.floor(l / 3600), n = l % 3600, n > 60 ? (s = Math.floor(n / 60), i = n % 60) : i = n) : (n = l % 3600, n > 60 ? (s = Math.floor(n / 60), i = n % 60) : i = n)) : t.leftTime > 3600 ? (e = Math.floor(t.leftTime / 3600), n = t.leftTime % 3600, n > 60 ? (s = Math.floor(n / 60), i = n % 60) : i = n) : t.leftTime > 60 ? (s = Math.floor(t.leftTime / 60), i = t.leftTime % 60) : i = t.leftTime, 10 > s && (s = "0" + s), 10 > i && (i = "0" + i), a > 0) var r = a + "天 " + e + "时" + s + "分" + i + "秒";
                else if (1 > a && e > 0) var r = e + "时" + s + "分" + i + "秒";
                else if (1 > a && 1 > e && s > 0) var r = s + "分" + i + "秒";
                else if (1 > a && 1 > e && 1 > s && i > 0) var r = i + "秒";
                else var r = "00秒";
                t.leftTime--,
                $("#remainTime").html(r)
            },
            clearGreenChannel: function () {
                $("#remainTime").html("已截止")
            },
            switchRx9Fs: function (t) {
                var a = this;
                0 >= a.leftTime ? ($("#dtEnd").html("<span>当前胆拖投注已经截止。</span>").addClass("tre"), $("#zcList .dan").removeAttr("checked").attr("disabled", "disabled"), t && (zcBuy.resetRx9Info(), $.alert("胆拖投注已经截止，当前场次只能按复式进行投注。"), setTimeout(function () {
                    $.alertClose()
                }, 5e3))) : ($("#dtEnd").html('胆拖截止：<label id="dsEndTime">载入中...</label>').removeClass("tre"), $("#zcList .dan").removeAttr("disabled").attr("disabled", ""))
            },
            getIssueSelectList: function () {
                var t = this,
                    a = "/lottery/getwqissuereturnall.action?lotteryId=" + lotteryId + "&issueLen=20&d=" + l.getTimeStamp();
                $.ajax({
                        url: a,
                        type: "get",
                        dataType: "json",
                        success: function (a) {
                            for (var e = '<option selected="selected" value="2" _isHis="1">历史期次</option>', s = 0, i = a.length; i > s; s++) {
                                var l = r.find(t.issues, a[s].issue); - 1 == l && "9" != a[s].status && (e += '<option value="-1">' + a[s].issue + "</option>")
                            }
                            $("#wIssue").html(e)
                        }
                    })
            },
            getPrizeInfo: function () {
                var t = this,
                    a = "/lottery/hisnumberzucai.action.action?lotteryId=" + t.oldId + "&issueLen=20&d=" + l.getTimeStamp();
                $.get(a, function (a) {
                        var e = i.parse(a);
                        t.readerPrizeInfo(e[0].gameIssueName)
                    })
            },
            readerPrizeInfo: function (t) {
                var a = this,
                    e = a.lotId,
                    s = "/lottery/hisnumberissue.action?lotteryId1=" + a.oldId + "&issue=" + t + "&lotteryId2=" + e + "&d=" + l.getTimeStamp();
                $.get(s, function (s) {
                        var l = i.parse(s)[0],
                            n = "",
                            r = "",
                            o = "";
                        return l.saleSummoney ? (n += '<span class="showNum info">' + t + "期开奖信息：", o = l.saleSummoney.split(","), "301" == lotteryId && o.length > 1 ? l.saleSummoney && (n += " 销售额:<strong>" + a.numFormat(l.saleSummoney.split(",")[1]) + "</strong>元，") : l.saleSummoney && (n += " 销售额:<strong>" + a.numFormat(l.saleSummoney.split(",")[0]) + "</strong>元，"), l.pageandmoney && (r = l.pageandmoney.split(","), "300" == e ? (n += "一等奖:<strong>" + a.numFormat(r[0].split("-")[0]) + "</strong>注  <strong>" + a.numFormat(r[0].split("-")[1]) + "</strong>元&nbsp;&nbsp;", n += "二等奖:<strong>" + a.numFormat(r[1].split("-")[0]) + "</strong>注  <strong>" + a.numFormat(r[1].split("-")[1]) + "</strong>元") : "301" == e ? n += "中奖注数:<strong>" + a.numFormat(r[2].split("-")[0]) + "</strong>注  <strong>" + a.numFormat(r[2].split("-")[1]) + "</strong>元" : ("302" == e || "303" == e) && (n += "中奖注数:<strong>" + a.numFormat(r[0].split("-")[0]) + "</strong>注  <strong>" + a.numFormat(r[0].split("-")[1]) + "</strong>元")), l.pondOfLottery && (n += " 滚存到下期:<strong>" + a.numFormat(l.pondOfLottery) + "</strong>元</span>"), $("#lastPeriodWin").html(n), void 0) : !1
                    })
            },
            numFormat: function (t) {
                return t ? 0 == t ? 0 : parseInt(t).toLocaleString().split(".")[0] : "-"
            },
            getRaceData: function (t) {
                var a = this,
                    e = this.args,
                    s = e[lotteryId].oldId,
                    l = "/lottery/zcplayvs.action?lotteryId=" + s + "&issue=" + t + "&v=" + (new Date).getTime();
                $.get(l, function (t) {
                        var e;
                        e = i.parse(t),
                        a.renderRace(e)
                    })
            },
            renderRace: function (t) {
                var a = this;
                if (!t) return !1;
                for (var e = "", i = a.matchInfo = t.matchInfo, l = 0, n = i.length; n > l; l++) {
                    var r = i[l],
                        o = 2 * l,
                        c = 2 * l + 1,
                        d = s.str.formatDate(r.gameStartDate),
                        h = r.europeSp.split(" "),
                        p = "quan",
                        u = "beginBet";
                    l % 2 && (u = "beginBet oddBeginBet"),
                    a.fs || (p = "quan tgr");
                    var m = "";
                    2 == r.leageType ? m = '<a href="http://saishi.zgzcw.com/soccer/cup/' + r.legageId + '" target="_blank">' + r.leageName + "</a>" : 1 == r.leageType && (m = '<a href="http://saishi.zgzcw.com/soccer/league/' + r.legageId + '" target="_blank">' + r.leageName + "</a>");
                    var f = !1; - 1 != r.playId.indexOf("-") && (r.playId = r.playId.replace("-", ""), f = !0), 
                    	"303" == lotteryId ? (
                    	e += '<tr id="tr_' + r.playId + '" class="' + u + '">' 
                    		+ '<td class="wh-1">' + "   <span><i>" + (o + 1) + "</i></span><hr><span>" + (c + 1) + "</span>" + "</td>"
                    		+ '<td class="wh-2 b-l">' + "  <span>" + m + "</span>" + "</td>" 
                    		+ '<td class="wh-3">' + d + "</td>" 
                    		+ '<td class="wh-4 b-l r-s">' 
                    			+ '   <div class="tfr">' + '       <span> 主：<a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a></span>" + "   </div>" 
                    			+ "   <div>" + '       <span>客：<a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a></span>" + "    </div>" 
                    		+ "</td>" 
                    		+ '<td class="wh-8 b-l xh">' 
                    			+ '   <div class="bets-area">', 
                    	e += a.fs ? '<a href="javascript:void(0)" class="n" id="n_' + o + '_0">0</a><a href="javascript:void(0)" class="n" id="n_' + o + '_1">1</a><a href="javascript:void(0)" class="n" id="n_' + o + '_2">2</a><a href="javascript:void(0)" class="n" id="n_' + o + '_3">3+</a>'
                      				: '<span class="ys4">未开售</span>', e += '   </div>   <hr>   <div class="bets-area">', 
                      	e += a.fs ? '<a href="javascript:void(0)" class="n" id="n_' + c + '_0">0</a><a href="javascript:void(0)" class="n" id="n_' + c + '_1">1</a><a href="javascript:void(0)" class="n" id="n_' + c + '_2">2</a><a href="javascript:void(0)" class="n" id="n_' + c + '_3">3+</a>' : '<span class="ys4">未开售</span>', 
                      	e += '   </div></td><td class="wh-9">   <span class="' + p + '" id="quan_' + o + '" style="display:block;cursor:pointer;">全</span><hr>' 
                      		+ '   <span class="' + p + '" id="quan_' + c + '" style="display:block;cursor:pointer;">全</span>' + "</td>" 
                      		+ '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' 
                      		+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + f + '" target="view_window">亚</a>' 
                      		+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + f + '" target="view_window">欧</a>' 
                      		+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + f + '" target="view_window">析</a>' 
                      		+ "</td>" 
                      		+ '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" 
                      		+ '<td class="wh-15 b-l">' + a.makeTzbl(r.renqiInfo) + "</td>" + "</tr>")
                      	: "302" == lotteryId ? (e += '<tr id="tr_' + r.playId + '" class="' + u + '">' 
                      		+ '<td class="wh-1">' + "   <span><i>" + (o + 1) + "</i></span><hr><span>" + (c + 1) + "</span>" + "</td>" 
                      		+ '<td class="wh-2 b-l">' + "  <span>" + m + "</span>" + "</td>" 
                      		+ '<td class="wh-3">' + d + "</td>" 
                      		+ '<td class="wh-4 t-r r-l"><a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a></td>" 
                      		+ '<td class="wh-5">VS</td>' 
                      		+ '<td class="wh-6 t-l r-r"><a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a></td>" 
                      		+ '<td class="wh-10 b-l"><span>半场</span><hr><span>全场</span></td>' 
                      		+ '<td class="wh-8 b-l xh">' + '   <div class="bets-area">', 
                      	e += a.fs ? '<a href="javascript:void(0)" class="n" id="n_' + o + '_3">3</a><a href="javascript:void(0)" class="n" id="n_' + o + '_1">1</a><a href="javascript:void(0)" class="n" id="n_' + o + '_0">0</a>' 
                      			: '<span class="ys">未开售</span>', e += '   </div>   <hr>   <div class="bets-area">', 
                      	e += a.fs ? '<a href="javascript:void(0)" class="n" id="n_' + c + '_3">3</a><a href="javascript:void(0)" class="n" id="n_' + c + '_1">1</a><a href="javascript:void(0)" class="n" id="n_' + c + '_0">0</a>' : '<span class="ys">未开售</span>', 
                      	e += '   </div></td><td class="wh-9">   <span class="' + p + '" id="quan_' + o + '" style="display:block;cursor:pointer;">全</span><hr>' + '   <span class="' + p + '" id="quan_' + c + '" style="display:block;cursor:pointer;">全</span>' + "</td>" 
                      		+ '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' 
                      			+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + f + '" target="view_window">亚</a>' 
                      			+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + f + '" target="view_window">欧</a>' 
                      			+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + f + '" target="view_window">析</a>' 
                      		+ "</td>" 
                      		+ '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" 
                      		+ '<td class="wh-15 b-l">' + a.makeTzbl(r.renqiInfo) + "</td>" 
                      	+ "</tr>") 
                      	: 
                      	"301" == lotteryId ? (
                      	e += '<tr id="tr_' + r.playId + '" class="' + u + '">' + '<td class="wh-1">' + "   <i>" + (l + 1) + "</i>" + "</td>" 
	                      	+ '<td class="wh-2 b-l">' + "  <span>" + m + "</span>" + "</td>" 
	                      	+ '<td class="wh-3">' + d + "</td>" 
	                      	+ '<td class="wh-4 t-r r-l"><a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a></td>" 
							+ '<td class="wh-5">VS</td>' + '<td class="wh-6 t-l r-r"><a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a></td>" 
							+ '<td class="wh-8 b-l xh">' + '   <div class="bets-area">', 
						e += a.fs ? '<a href="javascript:void(0)" class="n" id="n_' + l + '_3">3</a><a href="javascript:void(0)" class="n" id="n_' + l + '_1">1</a><a href="javascript:void(0)" class="n" id="n_' + l + '_0">0</a>' 
								: '<span class="ys">未开售</span>', 
						e += '   </div></td><td class="wh-9">   <span class="' + p + '" id="quan_' + l + '" style="display:block;cursor:pointer;">全</span><hr>' + "</td>" 
							+ '<td><input type="checkbox" value="' + l + '" class="dan" id="dan_' + l + '"></td>' 
							+ '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' + '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + f + '" target="view_window">亚</a>' 
								+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + f + '" target="view_window">欧</a>' 
								+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + f + '" target="view_window">析</a>' 
								+ "</td>" 
							+ '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" 
							+ '<td class="wh-15 b-l">' + a.makeTzbl(r.renqiInfo) + "</td>" + "</tr>") 
						: 
						"300" == lotteryId && (
						e += '<tr id="tr_' + r.playId + '" class="' + u + '">' 
							+ '<td class="wh-1">' + "   <i>" + (l + 1) + "</i>" + "</td>" 
							+ '<td class="wh-2 b-l">' + "  <span>" + m + "</span>" + "</td>" 
							+ '<td class="wh-3">' + d + "</td>" 
							+ '<td class="wh-4 t-r r-l"><a href="http://saishi.zgzcw.com/soccer/team/' + r.hostId + '" target="_blank" title="' + r.hostNameFull + '">' + r.hostName + "</a></td>" 
							+ '<td class="wh-5">VS</td>' 
							+ '<td class="wh-6 t-l r-r"><a href="http://saishi.zgzcw.com/soccer/team/' + r.guestId + '" target="_blank" title="' + r.guestNameFull + '">' + r.guestName + "</a></td>" 
							+ '<td class="wh-8 b-l xh">' + '   <div class="bets-area">', 
						e += a.fs ? '<a href="javascript:void(0)" class="n" id="n_' + l + '_3">3</a><a href="javascript:void(0)" class="n" id="n_' + l + '_1">1</a><a href="javascript:void(0)" class="n" id="n_' + l + '_0">0</a>' 
								: '<span class="ys">未开售</span>', 
						e += '   </div></td><td class="wh-9">   <span class="' + p + '" id="quan_' + l + '" style="display:block;cursor:pointer;">全</span>' + "</td>" 
							+ '<td class="wh-10 b-l sp" oldplayid="0" newplayid="' + r.playId + '">' 
								+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/ypdb" class="sj" data-zlc="' + f + '" target="_blank">亚</a>' 
								+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bjop" class="sj" data-zlc="' + f + '" target="_blank">欧</a>' 
								+ '   <a href="javascript:void(0)" data-href="http://fenxi.zgzcw.com/' + r.playId + '/bfyc" class="sj" data-zlc="' + f + '" target="_blank">析</a>' 
							+ "</td>" 
							+ '<td class="wh-11 b-l">' + '   <div class="oupei-area" mid="' + r.playId + '" rel="9"><span>' + h[0] + '</span><span class="mid">' + h[1] + "</span><span>" + h[2] + "</span></div>" + "</td>" 
							+ '<td class="wh-15 b-l">' + a.makeTzbl(r.renqiInfo) + "</td>" 
						+ "</tr>")
                }
                $("#zcList").html(e);
                var g = s.str.queryUrl(location.href, "nav");
                if (!g || "yt" != g) try {
                    var v = $.cookie("zcBackEdit"),
                        b = $.cookie("zcDetailed");
                    v ? zcBuy.isBackEdit() : b && zcBuy.isDetailed()
                } catch (y) {}
            },
            makeTzbl: function (t) {
                var a = this,
                    e = "";
                if (t) {
                        var s = !0,
                            i = a.lotId.charAt(2) > 1,
                            l = 3 == a.lotId.charAt(2) ? "4" : "3";
                        "" == t.replace(/(0\.00%)/gi, " ").replace(/ /gi, "") && (s = !1, t = t.replace(/(0\.00%)/gi, "-"));
                        for (var n = t.split(" "), r = "", o = "", c = "", d = "", h = a.comWitdh(n), p = 0, u = n.length; u > p; p++) i && p >= n.length / 2 ? (o += "<li>" + n[p] + "</li>", d += '<li class="' + a.colorful[p - n.length / 2] + '" style="width:' + h[p] + '"></li>') : (r += "<li>" + n[p] + "</li>", c += '<li class="' + a.colorful[p] + '" style="width:' + h[p] + '"></li>');
                        i && (e += "<span>"),
                        e += '<ul class="rq_chart_new rq_rate_new' + l + '">' + r + "</ul>",
                        s && (e += '<ul class="rq_chart_new">' + c + "</ul>"),
                        i && (e += '</span><hr /><span><ul class="rq_chart_new rq_rate_new' + l + '">' + o + "</ul>", s && (e += '<ul class="rq_chart_new">' + d + "</ul>"))
                    }
                return e
            },
            comWitdh: function (t) {
                var a = 164;
                return $.map(t, function (t) {
                    var e = (a * parseFloat(t) / 100).toFixed(2);
                    return Math.max(e, 1) + "px"
                })
            },
            alertBox: function (t, a, e, i) {
                var l, n = this,
                    r = {
                        0: {
                            msg: "<span class='tbe'>恭喜您，您的方案已经成功提交！</span>",
                            show: 1,
                            cz: 1
                        },
                        "0000": {
                            msg: "恭喜您，您的方案已经成功提交！",
                            show: 1,
                            cz: 1
                        },
                        "0001": {
                            msg: "您的方案已保存，但余额不足，是否<a href='/usercenter/accmanage/charge.jsp' target='_blank'>立即充值</a>？",
                            show: 1,
                            cz: 1
                        },
                        1000: {
                            msg: "您的方案已经保存成功！",
                            show: 1,
                            cz: 10
                        },
                        2002: {
                            msg: "该方案已成功提交，是否再次提交？",
                            show: 2,
                            cz: 6
                        },
                        5: {
                            msg: "没有发现上传文件",
                            show: 2,
                            cz: 3
                        },
                        1: {
                            msg: "用户未登录",
                            show: 2,
                            cz: 5
                        },
                        1202: {
                            msg: "扣款失败,是否<a href='/usercenter/accmanage/charge.jsp' target='_blank'>立即充值</a>？",
                            show: 2,
                            cz: 3
                        },
                        8: {
                            msg: "上传文件格式错误",
                            show: 2,
                            cz: 3
                        },
                        "0301": {
                            msg: "充值不足,是否<a href='/usercenter/accmanage/charge.jsp' target='_blank'>立即充值</a>？",
                            show: 2,
                            cz: 2
                        },
                        "0501": {
                            msg: "您的投注期次已截期",
                            show: 2,
                            cz: 4
                        },
                        "0502": {
                            msg: "您的投注期次不存在",
                            show: 2,
                            cz: 3
                        },
                        "0503": {
                            msg: "您的账户已被锁定",
                            show: 2,
                            cz: 3
                        },
                        "0504": {
                            msg: "此方案已经撤销",
                            show: 2,
                            cz: 0
                        },
                        "0505": {
                            msg: "您上传的方案格式有误",
                            show: 2,
                            cz: 3
                        },
                        "0507": {
                            msg: "本期未开售",
                            show: 2,
                            cz: 3
                        },
                        "0508": {
                            msg: "很抱歉，您的方案提交失败",
                            show: 0,
                            cz: 0
                        },
                        "0510": {
                            msg: "暂无此期次",
                            show: 2,
                            cz: 3
                        },
                        "0511": {
                            msg: "您的投注期次已截期",
                            show: 2,
                            cz: 3
                        },
                        "0528": {
                            msg: "每期只能发起2个包买包赔方案",
                            show: 2,
                            cz: 1
                        },
                        "0529": {
                            msg: "包买包赔方案金额不能大于10000",
                            show: 2,
                            cz: 1
                        },
                        "0528": {
                            msg: "每期只能发起2个包买包赔方案",
                            show: 2,
                            cz: 1
                        },
                        "0529": {
                            msg: "包买包赔方案金额不能大于10000",
                            show: 2,
                            cz: 1
                        },
                        9999: {
                            msg: "系统未知异常",
                            show: 2,
                            cz: 0
                        },
                        333: {
                            msg: "战团用户不能发起合买",
                            show: 2,
                            cz: 0
                        },
                        444: {
                            msg: "战团用户不能进行战团结束之后的追号",
                            show: 2,
                            cz: 0
                        },
                        4444: {
                            msg: "短信系统设备调试，该彩种暂停销售。",
                            show: 2,
                            cz: 3
                        }
                    },
                    o = r[t] || {
                        msg: "因网络原因，方案提交失败！",
                        show: 2,
                        cz: 0
                    },
                    c = r[t] ? o.msg : a || o.msg,
                    d = "" + c + "<p>",
                    h = "",
                    p = "",
                    u = "",
                    m = m;
                if (u += document.domain.indexOf("diyicai.com") > -1 ? "http://www.diyicai.com/huodong/song10/index.html" : "http://www.zgzcw.com/huodong/song10/index.html", p += "", 1 == o.cz ? +n.fee ? d += '您可以<a href="javascript:void(0)" class="tu goonBuy" id="goonBuy">继续投注</a>，或者可以前往<a href="/uc/betmanage/betprosave.action" class="tu" target="_blank">保存的方案</a>查看该方案。' : i ? (d += 9 == i ? '您可以前往 <a href="/lottery/getproject.action?lotteryId=' + m + '&tabId=1" class="tu tzgbBn" target="_blank"> 方案列表 </a> 或 <a href="/uc/betmanage/betrecord.action" class="tu" target="_blank">投注记录</a> 查看该单式方案。' : '<p>温馨提示：请务必在本期投注截止前上传方案。</p>您可以<a href="javascript:void(0)" class="tu goonBuy" id="goonBuy">继续投注</a>，也可以前往 <a href="/lottery/getproject.action?lotteryId=' + m + '&tabId=1" class="tu tzgbBn" target="_blank"> 方案列表 </a> 或 <a href="/uc/betmanage/betrecord.action" class="tu" target="_blank">投注记录</a> 查看该预投方案。', d += h) : d += '您可以<a href="javascript:void(0)" class="tu goonBuy" id="goonBuy">继续投注</a>，也可以前往 <a href="/lottery/getproject.action?lotteryId=' + m + '&tabId=1" class="tu tzgbBn" target="_blank"> 方案列表 </a> 或 <a href="/uc/betmanage/betrecord.action" class="tu" target="_blank">投注记录</a> 查看该方案。' + h : 4 == o.cz ? d += '您可以<span class="tb">选择下一期再投</span>，也可以去<a href="/" class="tu">购彩大厅</a>试试其他彩种！' : 5 == o.cz ? (d += '如果您已经是注册用户，请您点击这里<a class="tu bet_login" href="javascript:void(0)">登录</a>后再投注，<br/>如果您尚未注册，点击这里<a href="/login.jsp" target="_blank" class="tu">免费注册</a>。', l = function () {
                        $(".bet_login").unbind().bind("click", function () {
                            $.alertClose(),
                            showLoginDiv("/lottery/common/login.jsp")
                        }),
                        $("#nowBuy").val("尚未登录")
                    }) : d += 6 == o.cz ? "" : 10 == o.cz ? '您可以<a href="/uc/betmanage/betprosave.action" target="_blank">查看已经保存的方案列表</a>。' : '您可以<span class="tb">稍后再投</span>，也可以去<a href="/" class="tu">购彩大厅</a>试试其他彩种！', d += "</p>", $("#matchesTr").hide(), 1 == o.cz) {
                        if (n.fee > 0) {
                            var f = function () {
                                return e !== void 0 ? window.location.href = e : window.location.reload(),
                                !1
                            };
                            $.confirm(d, f)
                        } else refreshMoney(),
                        $.alertOk(d, function () {
                            return 9 == i && (window.opener = null, window.open("", "_self"), window.close()),
                            !1
                        });
                        l = function () {
                            $(".goonBuy").unbind().bind("click", function () {
                                n.tzUrl ? location.href = n.tzUrl : location.reload()
                            })
                        }
                    } else if (6 == o.cz) {
                        var f = function () {
                            return n.randId = s.sf.getTimeStamp(),
                            $("#nowBuy").trigger("click"),
                            !1
                        };
                        $.confirm(d, f)
                    } else 10 == o.cz ? $.alertOk(d) : $.alert(d);
                $.isFunction(l) && l.call()
            }
        };
    var o = function () {
            return new e
        };
    a.zcTool = o;
    var c = window.oddToll = t("./zc.odd").oddAfirm;
    c.init()
}),
define("../js/example/zc.odd", ["lib-cpBase", "lib-json"], function (t, a) {
    var e = t("lib-cpBase").CPB,
        s = t("lib-json").JSON,
        i = e.str,
        l = e.c,
        n = function () {
            this.opArgs = {
                europe: {
                    name: "欧赔",
                    ids: "get_oupan"
                },
                asian: {
                    name: "亚盘",
                    ids: "get_yapan"
                },
                kelly: {
                    name: "凯利",
                    ids: "get_kaili"
                },
                "return": {
                    name: "赔付",
                    ids: "get_peifulv"
                },
                probability: {
                    name: "概率",
                    ids: "get_gailv"
                },
                betting_ratio: {
                    name: "本站投注",
                    ids: "get_renqi"
                }
            },
            this.sortArgs = {
                get_oupan: ["场次号", "主胜赔", "客胜赔", "最小赔率", "主客差值"],
                get_yapan: ["场次号", "主队让球", "客队让球"],
                get_gailv: ["场次号", "主胜概率", "平局概率", "客胜概率"],
                get_kaili: ["场次号", "主胜凯利", "平局凯利", "客胜凯利"],
                get_peifulv: ["场次号", "赔付率"],
                get_renqi: ["场次号", "主队比例", "客队比例", "最高比例", "比例差值"]
            },
            this.ypFormat = {
                0: {
                    s: "平手",
                    l: "平手"
                },
                .25: {
                    s: "平/半",
                    l: "平手/半球"
                },
                .5: {
                    s: "半球",
                    l: "半球"
                },
                .75: {
                    s: "半/一",
                    l: "半球/一球"
                },
                1: {
                    s: "一球",
                    l: "一球"
                },
                1.25: {
                    s: "一/球半",
                    l: "一球/球半"
                },
                1.5: {
                    s: "球半",
                    l: "球半"
                },
                1.75: {
                    s: "球半/两",
                    l: "球半/两球"
                },
                2: {
                    s: "两球",
                    l: "两球"
                },
                2.25: {
                    s: "两球/两半",
                    l: "两球/两球半"
                },
                2.5: {
                    s: "两半",
                    l: "两球半"
                },
                2.75: {
                    s: "两半/三",
                    l: "两球半/三球"
                },
                3: {
                    s: "三球",
                    l: "三球"
                },
                3.25: {
                    s: "三/三半",
                    l: "三球/三球半"
                },
                3.5: {
                    s: "三半",
                    l: "三球半"
                },
                3.75: {
                    s: "三半/四",
                    l: "三球半/四球"
                },
                4: {
                    s: "四球",
                    l: "四球"
                },
                4.25: {
                    s: "四/四半",
                    l: "四球/四球半"
                },
                4.5: {
                    s: "四半",
                    l: "四球半"
                },
                4.75: {
                    s: "四半/五",
                    l: "四球半/五球"
                },
                5: {
                    s: "五球",
                    l: "五球"
                }
            },
            this.zcDUrl = "/ssc/lottery/jcsp/getzcspbyissue.jsp",
            this.zcMUrl = "/ssc/lottery/jcsp/getzcspbyplayId.jsp",
            this.opComp = "/ssc/lottery/jcsp/getjcspcompany.jsp",
            this.lotId = i.urlHas().lotteryId,
            this.type = i.urlHas().type,
            this.cid = 0,
            this.spType = "europe",
            this.cacheTime = 12e4,
            this.isSjSB = !1,
            this.isSpSB = !1,
            this.isload = !1
        };
    n.prototype = {
            constructor: n,
            init: function () {
                if ($(".pjopBtn:first").length) {
                    var t = $(".pjopBtn:first").attr("rel");
                    this.initCompP(t, "europe")
                }
                return this.setOptCookie(),
                this.bindEvent(),
                this
            },
            bindEvent: function () {
                var t = this;
                $("#spChange").click(function () {
                    var t = $(this).attr("checked"),
                        a = $(this).attr("id"),
                        e = $(this).attr("t"),
                        s = e + "_" + a;
                    t ? l.cookie(s, 1, {
                            expires: 30,
                            path: "/"
                        }) : l.cookie(s, null, {
                            expires: 0,
                            path: "/"
                        })
                }),
                $("span.pjopBtn").unbind().live("mouseenter", function () {
                    var a = this;
                    t.pBTimer = setTimeout(function () {
                        $(a).addClass("show");
                        var e = $(a).attr("rel");
                        return $("#opData" + e).find("li").length > 0 ? !1 : (t.setOpInfos(a), setTimeout(function () {
                            var t = $("#opData" + e).find(":radio").first();
                            t.attr("checked", !0),
                            t.trigger("mousedown")
                        }, 1500), void 0)
                    }, 500)
                }),
                $("span.pjopBtn").unbind().live("mouseleave", function () {
                    clearTimeout(t.pBTimer),
                    $(this).removeClass("show")
                }),
                $("span.paixvBtn").unbind().live("mouseenter", function () {
                    var a = this;
                    t.pxTimer = setTimeout(function () {
                        $(a).addClass("show")
                    }, 500)
                }),
                $("span.paixvBtn").unbind().live("mouseleave", function () {
                    clearTimeout(t.pxTimer),
                    $(this).removeClass("show")
                }),
                $(".paixvBtn").delegate("a", "click", function () {
                    var t, a = $(this).attr("rel"),
                        e = $(this).find("em").attr("class"),
                        s = e.indexOf("up") > -1 ? 1 : 0,
                        i = $(this).parent().attr("t");
                    $(".paixvBtn").find("a").find("em").removeClass("cur"),
                    $(this).find("em").addClass("cur"),
                    a.indexOf("主") > -1 && "主客差值" != a ? t = 0 : a.indexOf("平") > -1 || "赔付率" == a ? t = 1 : a.indexOf("客") > -1 && "主客差值" != a && (t = 2),
                    $("#pN" + i).text(a);
                    var l = $(".mb");
                    $.each(l, function (e) {
                            var n = $(l[e]).find("tr"),
                                r = n.sort(function (e, l) {
                                    var n, r;
                                    if ("场次号" == a) n = Number($(e).find("td:eq(0) i").text()) || 0,
                                    r = Number($(l).find("td:eq(0) i").text()) || 0;
                                    else if ("最小赔率" == a) {
                                        var o = $(e).find("td:eq(" + i + ") span"),
                                            c = $(l).find("td:eq(" + i + ") span");
                                        n = Math.min.call(Math, $(o[0]).text(), $(o[1]).text(), $(o[2]).text()) || 0,
                                        r = Math.min.call(Math, $(c[0]).text(), $(c[1]).text(), $(c[2]).text()) || 0
                                    } else if ("主客差值" == a) {
                                        var o = $(e).find("td:eq(" + i + ") span"),
                                            c = $(l).find("td:eq(" + i + ") span");
                                        n = Math.abs($(o[0]).text() - $(o[2]).text()) || 0,
                                        r = Math.abs($(c[0]).text() - $(c[2]).text()) || 0
                                    } else n = $($(e).find("td:eq(" + i + ") span")[t]).text() || 0,
                                    r = $($(l).find("td:eq(" + i + ") span")[t]).text() || 0;
                                    return s ? Number(n) - Number(r) : Number(r) - Number(n)
                                });
                            $(".mb").eq(e).empty().html(r)
                        }),
                    l.find("tr").attr("class", ""),
                    $.each(l.find("tr"), function (t) {
                            var a = "beginBet";
                            t % 2 && (a = "beginBet oddBeginBet"),
                            $(this).addClass(a)
                        })
                }),
                $(".opData").delegate("label", "mousedown", function () {
                    var a = $(this).find("input"),
                        e = a.attr("t"),
                        s = a.attr("p"),
                        i = a.val(),
                        l = a.attr("rel"),
                        n = $(this).parent().parent().attr("d");
                    $("#comp" + n).html(i).attr("cid", s).attr("t", e),
                    t.createPx(n, l),
                    t.getOpDate(e, s, n)
                }),
                $("div.oupei-area").unbind().live("mouseenter", function (a) {
                    var e = this;
                    clearTimeout(t.SjTimer),
                    t.SjTimer = setTimeout(function () {
                        t.isSjSB = !0,
                        t.setSjShowBonus(e, a)
                    }, 500)
                }),
                $("div.oupei-area").unbind().live("mousemove", function (a) {
                    if (!t.isSjSB) return !1;
                    var e = $(this),
                        s = e.attr("rel"),
                        i = $("#comp" + s).attr("t");
                    return "europe" != i && "asian" != i ? !1 : (t.setSpDivPostion(a, 1), void 0)
                }),
                $("div.oupei-area").unbind().live("mouseleave", function () {
                    t.isSjSB = !1,
                    t.isSpSB = !1,
                    clearTimeout(t.SjTimer),
                    clearTimeout(t.SpTimer),
                    t.setHideBonus()
                })
            },
            initCompP: function (t, a) {
                var e = $("#opData" + t);
                this.setOpInfos(e.parent().parent()),
                setTimeout(function () {
                    var t = e.find("[t=" + a + "]").first();
                    t.attr("checked", !0),
                    t.trigger("mousedown")
                }, 1500)
            },
            setOpInfos: function (t) {
                var a = this,
                    e = $(t).attr("rel");
                $.ajax({
                        url: this.opComp,
                        type: "post",
                        data: [],
                        success: function (t) {
                            var i = s.parse(t);
                            if ($.isEmptyObject(i)) return !1;
                            var l = a.createComp(i, e);
                            $("#opData" + e).empty().html(l),
                            a.isload = !0
                        },
                        error: function () {}
                    })
            },
            createComp: function (t, a) {
                var e = "";
                for (var s in t) if ("smg" != s && "betting_ratio" != s && "length" != s) {
                    e += "<li><em>" + this.opArgs[s].name + "</em>";
                    for (var i in t[s])"length" != i && (e += '<label><input type="radio" t=' + s + " p=" + i + " rel=" + this.opArgs[s].ids + "  value=" + t[s][i] + "  name=xht" + a + ">" + t[s][i] + "</label>");
                    e += "</li>"
                }
                return e
            },
            setOptCookie: function () {
                var t = this,
                    a = $("#spChange"),
                    e = a.attr("id"),
                    s = a.attr("t"),
                    i = l.cookie(s + "_" + e) ? !0 : !1;
                $("#spChange").attr("checked", i);
                var n = $("li.sh label:gt(0)"),
                    r = 1;
                $.each(n, function (a) {
                        var e = $(n[a]).find("input:checkbox"),
                            s = e.attr("id"),
                            i = e.attr("t"),
                            o = l.cookie(i + "_" + s) ? !0 : !1;
                        o && r++,
                        e.attr("checked", o),
                        t.setSJL(r)
                    })
            },
            setSJL: function (t) {
                var a, e = 1 == t ? "tz-wap" : 2 == t ? "tz-wap sheet-2" : "tz-wap sheet-3";
                $("#filterSpf").attr("checked") && (e += " rqfrq"),
                $("#tw").attr("class", e),
                e.indexOf("sheet-2") > -1 ? (a = $(".pjopBtn:eq(1)").attr("rel"), this.initCompP(a, "asian")) : e.indexOf("sheet-3") > -1 && (a = $(".pjopBtn:eq(2)").attr("rel"), this.initCompP(a, "kelly"))
            },
            setSjShowBonus: function (t, a) {
                var e = this,
                    s = $("#spChange").attr("checked"),
                    i = $(t).attr("rel"),
                    l = $(t).data("data"),
                    n = $("#comp" + i).attr("t");
                if ("europe" != n && "asian" != n) return $("div.bonusWap,div.bonus").hide(),
                !1;
                if (s) {
                        if ($("#spName").text($("#comp" + i).text()), l) {
                            if (2 == l) return $("div.bonusWap,div.bonus").hide(),
                            !1;
                            var r = e.createSpLi_zc(l);
                            $("#spInfos").html(r)
                        } else e.getSpsChangeDate(t);
                        e.setSpDivPostion(a, 1)
                    }
            },
            getSpsChangeDate: function (t) {
                var a, e, i = this,
                    l = $(t).attr("mid"),
                    n = $(t).attr("rel"),
                    r = $("#comp" + n).attr("cid"),
                    o = $("#comp" + n).attr("t"),
                    c = $(t).find("span").length;
                if (3 > c) return !1;
                var d, h;
                if (!lotteryId) return !1;
                if (201 == lotteryId || 202 == lotteryId || 47 == lotteryId) d = i.jcMUrl,
                a = $("#selectissue").val(),
                h = $(t).attr("pid"),
                e = {
                        cid: r,
                        matchId: l,
                        sptype: o,
                        date: a
                    };
                else if (400 == lotteryId) a = $("#selectissue").val(),
                d = i.bdMUrl,
                e = {
                        cid: r,
                        matchId: l,
                        sptype: o,
                        issue: a,
                        lottery: lotteryId
                    };
                else {
                        var p = "301" == lotteryId ? "300" : lotteryId;
                        a = zcTool.curIssue,
                        d = i.zcMUrl,
                        e = {
                            cid: r,
                            playid: l,
                            sptype: o,
                            issue: a,
                            lottery: p
                        }
                    }
                $.ajax({
                        url: d,
                        type: "post",
                        data: e,
                        success: function (a) {
                            var e = $(t);
                            if (!$.trim(a)) return e.data("data", 2),


                            function (t) {
                                setTimeout(function () {
                                    $(t).data("data", 0)
                                }, i.cacheTime)
                            }(e),
                            !1;
                            var l = s.parse(a),
                                n = "",
                                r = l.length;
                            return r ? (e.data("data", l), function (t) {
                                    setTimeout(function () {
                                        $(t).data("data", 0)
                                    }, i.cacheTime)
                                }(e), r ? (n += i.createSpLi_zc(l), $("#spInfos").empty().html(n), void 0) : !1) : (e.data("data", 2), !1)
                        },
                        error: function () {}
                    })
            },
            setSpDivPostion: function (t, a) {
                var e = i.urlHas().lotteryId || "",
                    s = e.indexOf("_dg") > -1 ? !1 : !0,
                    l = $("#spChange").prop("checked"),
                    n = $("div.bonusWap,div.bonus");
                if (!s || !l) return n.hide(),
                !1;
                var r = $(window),
                    o = $("div.bonusWap"),
                    c = o.width(),
                    d = o.height(),
                    h = r.height(),
                    p = r.scrollTop(),
                    u = r.scrollLeft(),
                    m = 0,
                    f = 0;
                f = t.clientX - c / 2 + 160 + (a ? -310 : 0) + u,
                m = t.clientY,
                m = t.clientY > h / 2 ? m - d + p : m + 10 + p,
                $("div.bonusWap").css({
                        left: f,
                        top: m
                    }),
                n.show()
            },
            createSpLi_zc: function (t) {
                var a = t[0],
                    e = a.length;
                if (0 == e) return !1;
                6 == e && a.splice(-3);
                var s, i = t.length,
                    l = "",
                    n = "";
                for (s = 0; i > s; s++) {
                        var r = t[s];
                        if (r.length > 0) if (0 == s) n += '<li class="spe"><span class="forSp">&nbsp;初</span><span class="firSp">' + r[0] + '</span><span class="secSp">' + r[1] + '</span><span class="thiSp">' + r[2] + "</span></li>";
                        else {
                            var o = t[s - 1];
                            l += '<li><span class="forSp">' + (this.formatDate(r[3], 0) || "") + "</span>" + '<span class="firSp">' + r[0] + "<s class=" + this.checkSpClass(r, o, 0) + "></s></span>" + '<span class="secSp">' + r[1] + "<s class=" + this.checkSpClass(r, o, 1) + "></s></span>" + '<span class="thiSp">' + r[2] + "<s class=" + this.checkSpClass(r, o, 2) + "></s></span>" + "</li>"
                        }
                    }
                return l += n
            },
            formatDate: function (t, a) {
                return t ? t.slice(5, t.length - a).replace(/-/g, "/") : !1
            },
            checkSpClass: function (t, a, e) {
                var s;
                return s = t[e] > a[e] ? "s" : t[e] < a[e] ? "j" : t[e] == a[e] ? "z" : ""
            },
            setHideBonus: function () {
                return $("div.bonusWap,div.bonus").hide(),
                $("#spInfos").empty(),
                !1
            },
            createPx: function (t, a) {
                if (!a) return !1;
                for (var e = this.sortArgs[a], s = "", i = "", l = 0, n = e.length; n > l; l++) {
                    var r = e[l];
                    s += "主客差值" == r ? '<a href="javascript:void(0)" rel=' + r + ">" + r + '<em class="down ' + i + '"></em></a>' : '<a href="javascript:void(0)" rel=' + r + ">" + r + '<em class="up ' + i + '"></em></a>'
                }
                $("#px" + t).empty().append(s)
            },
            getOpDate: function (t, a, e) {
                var i, l, n, r = this;
                if (!lotteryId) return !1;
                if (201 == lotteryId || 202 == lotteryId || 47 == lotteryId) i = r.jcDUrl,
                n = $("#selectissue").val(),
                l = {
                    cid: a,
                    sptype: t,
                    date: n
                };
                else if (400 == lotteryId) i = r.bdDUrl,
                n = $("#selectissue").val(),
                l = {
                    cid: a,
                    sptype: t,
                    issue: n,
                    lottery: lotteryId
                };
                else {
                    var o = "301" == lotteryId ? "300" : lotteryId;
                    i = r.zcDUrl,
                    n = zcTool.curIssue,
                    l = {
                        cid: a,
                        sptype: t,
                        issue: n,
                        lottery: o
                    }
                }
                $.ajax({
                    url: i,
                    type: "post",
                    data: l,
                    success: function (a) {
                        var i = s.parse(a);
                        return i.length ? (r.setSpInfos(i, e, t), void 0) : !1
                    },
                    error: function () {}
                })
            },
            setSpInfos: function (t, a, e) {
                var i, l = t.length;
                for (i = 0; l > i; i++) {
                    var n = 201 == lotteryId || 400 == lotteryId || 47 == lotteryId ? t[i].MATCHID : t[i].PLAYID,
                        r = t[i].SP;
                    if (n) {
                            var o = s.parse(r);
                            if (!o.length) return !1;
                            o = o.length > 3 ? o.slice(-3) : o;
                            for (var c = $("#tr_" + n).find("td").eq(a), d = 0, h = o.length; h > d; d++) if ("asian" == e && 1 == d) {
                                var p = Number(o[d]),
                                    u = "",
                                    m = "";
                                p > 0 && (u = "受让", m = "受让"),
                                p = Math.abs(p),
                                u += this.ypFormat[p].s,
                                m += this.ypFormat[p].l,
                                c.find("span").eq(d).html(u)
                            } else c.find("span").eq(d).html(o[d]).removeAttr("title")
                        }
                }
            }
        };
    var r = new n;
    a.oddAfirm = r
});
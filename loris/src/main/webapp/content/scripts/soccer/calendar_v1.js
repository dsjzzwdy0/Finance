jQuery.extend(Date.prototype, {
    equalsDate:function (d) {
        if (d instanceof Date) return this.format('YY-MM-DD') == d.format('YY-MM-DD');
        else return false;
    },
    format:function (tpl, fn) {
        var strs, w, keys, year, val;
        strs = [];
        tpl = tpl || 'YY\u5e74MM\u6708DD\u65e5 \u661f\u671fdd';
        w = 'FullYear,Month,Date,Hours,Minutes,Seconds,Day'.split(',');
        keys = [/YY/g, /Y/g, /MM/g, /M/g, /DD/g, /D/g, /hh/g, /h/g, /mm/g, /m/g, /ss/g, /s/g, /dd/g, /d/g];
        for (var i = 0; i < 7; i++) {
            val = this['get' + w[i]]() + (w[i] === 'Month' ? 1 : 0);
            strs.push(('0' + val).slice(-2), val)
        }
        year = [strs[1], strs[0]].concat(strs.slice(2, -2));
        year.push('\u65e5\u4e00\u4e8c\u4e09\u56db\u4e94\u516d'.substr(strs.slice(-1), 1), strs.slice(-1));
        for (var i = 0; i < 14; i++) {
            tpl = tpl.replace(keys[i], year[i])
        }
        return fn ? fn(tpl) : tpl
    },
    getMonthDays:function () {
        var m = (this.getMonth() + 1).toString();
        var a = '1,3,5,7,8,10,12'.split(','), b = '4,6,9,11'.split(',');
        return jQuery.inArray(m, a) > -1 ? 31 : jQuery.inArray(m, b) > -1 ? 30 : (function (y) {
            y = parseInt(y, 10);
            var r = y % 4 ? 28 : 29;
            if ((y % 100 == 0) && (y % 400 != 0)) r = 28;
            return r;
        })(this.getFullYear())
    },
    getWeekInYear:function () {//取年的第几周
        return Math.ceil(((this - new Date(this.getFullYear(), 0, 1)) / (86400000 * 7))) + (this.getDay() == 0 ? 1 : 0);
    },
    roll:function (dayOffset) {
        return new Date(this.getTime() + parseInt(dayOffset, 10) * 86400000);
    },
    rollMonth:function (monthOffet) {
        monthOffet = parseInt(monthOffet, 10);
        if (monthOffet == 0 || Math.abs(monthOffet) > 120) return this;
        var w = 'FullYear,Month,Date'.split(','), val = 0, a = [], timestr = this.format('hh:mm:ss');
        for (var i = 0; i < 3; i++) a.push(this['get' + w[i]]());
        var m = a[1] + monthOffet, y = a[0] + Math.floor(m / 12);
        m = (120 + Math.floor(m % 12)) % 12 + 1;
        var d_max = new Date(y + '/' + m + '/1').getMonthDays();  //获取计算后的月的最大天数
        if (a[2] > d_max) a[2] = d_max;
        return new Date('' + y + '/' + m + '/' + a[2] + ' ' + timestr);
    },
    getWeekMap:function (f) {
        var d = this.getDay(), b = [];
        for (var i = -d; i < 7 - d; i++) b.push(this.roll(i));
        return f > 0 ? b.slice(d + 1) : f < 0 ? b.slice(0, d) : b;
    },
    getSevenDaysMap:function () {
        var b = [];
        for (var i = -3; i <= 3 ; i++) b.push(this.roll(i));
        return b;
    },
    getFourWeeksMap:function () {
        var d = this.getDay(), b = [];
        for (var i = -14 - d; i < 14 - d; i++) b.push(this.roll(i));
        return b;
    },
    getMonthMap:function () {
        var d = this.getDate(), b = [];
        var l = this.getMonthDays();
        for (var i = 1 - d; i <= l - d; i++) b.push(this.roll(i));
        return b;
    },
    getMonthFirstDay:function (tpn) {
        var r = new Date(this.getFullYear(), this.getMonth(), 1);
        return tpn === undefined ? r : r.format(tpn);
    },
    getMonthLastDay:function (tpn) {
        var r = new Date(this.getFullYear(), this.getMonth() + 1, 1).roll(-1);
        return tpn === undefined ? r : r.format(tpn);
    }
})
jQuery.fn.toggleDispay = function (f) {
    if (f) this.show();
    else this.hide();
}
Array.prototype.unique = function () {
    var newArray = [], temp = {};
    for (var i = 0, len = this.length; i < len; i++) {
        temp[typeof(this[i]) + this[i]] = this[i];
    }
    for (var j in temp) {
        newArray.push(temp[j]);
    }
    return newArray;
};
var FootBallCalendar = function (id, options) {
    this.showDateObj = $();
    this.viewObj = $();
    jQuery.extend(this, options || {});
    this.container = $('#' + id);
    this.init();
}
FootBallCalendar.prototype = {
    init:function () {
        var now = new Date();
        this.days = []; //日期对象列表
        this.tar_date = now;
        this.sel_date = now;
        this.today = null;
        this.changday = null;
        this.selectday = null;//
        this.matchInfos = null; //存放固定时间的比赛信息
        this.matchFlag = false;
        this.matchArr = [];
        this.isGjFilter = false; //是否高级筛选
        this.isLoadInfos = false; //判断是否数据加载完成
        this.setCurDate(now);
        this.drawMonthPannel();
        this.bindEvent();
        this.setViewKey('one');
        this.showWeekView();
    },
    bindEvent:function () {
        var _this = this;
        $('#pre').click(function () {
            _this.tar_date = _this.tar_date.rollMonth(-1);
            _this.matchFlag = false;
            _this.drawMonthPannel();
        })
        $('#next').click(function () {
            _this.tar_date = _this.tar_date.rollMonth(1);
            _this.matchFlag = false;
            _this.drawMonthPannel();
        })
        $('#today_btn').click(function () {
            if (!_this.tar_date.equalsDate(new Date)) {
                _this.matchFlag = false;
                _this.setCurDate(_this.tar_date = new Date);
                _this.drawMonthPannel();
                _this.showWeekView();
                _this.createMatchInfos.apply(_this, _this.today);
            }
        })
        $('#preD').click(function () {
            _this.rollContition(-1);
            if (_this.tar_date.getMonth() != this.month) {
                _this.drawMonthPannel();
            }
            _this.showWeekView();
        })
        $('#nextD').click(function () {
            _this.rollContition(1);
            if (_this.tar_date.getMonth() != this.month) {
                _this.drawMonthPannel();
            }
            _this.showWeekView();
        })
        $('#changeViewBtn > em').click(function () {
            var cls = ['s_7', 's_5', 's_6'];
            $('#changeViewBtn').attr('class', cls[$(this).index() || 0]).children('em').removeClass('cur');
            var key = $(this).addClass('cur').attr('data_type') || 'one';
            _this.matchFlag = false;
            _this.setViewKey(key);
            _this.showWeekView();
        })
        this.viewObj.click(function (e) {
            var td = $(e.target).parents('td:first');
            var date = _this.parseDate(td.attr('id'));
            var issue = td.attr('issue');
            var zcissue = td.attr('zcissue');
            _this.checkIsLoadInfos();
            _this.matchArr = [];
            _this.createMatchInfos(issue, date, zcissue);
            //判断点击的时候是否是在同一个月
            if (_this.viewKey !== 'one') {
                if (_this.tar_date.getMonth() == parseInt(td.attr('id').split('-')[1], 10) - 1) {
                    _this.matchFlag = true;
                } else {
                    _this.matchFlag = false;
                }
            }
            _this.tar_date = date;
            _this.sel_date = date;
            _this.drawMonthPannel();
            _this.showWeekView(date);
        });
        $("#matchType :checkbox").unbind().on('click', function () {
            var flag = $(this).prop('checked');
            var sel_matches = "[matchname='" + $(this).val() + "']";
            _this.batchShow(flag, sel_matches);
        });
        $("#selectAll").click(function () {
            _this.selectAF(1);
        });
        $("#selectFan").click(function () {
            _this.selectAF(0);
        });
        $("#pankAll").click(function () {
            _this.selectPKAF(1);
        });
        $("#pankFan").click(function () {
            _this.selectPKAF(0);
        });
        $("#filterLottery :checkbox").click(function () {
            _this.filterLottery(this);
        });
        $("#filterPank :checkbox").click(function () {
            _this.filterPank($(this).prop('checked'), $(this).val());
        });
        $(".dot_1").click(function () {
            $("#matchFilter").show("slow");
            $(this).hide();
            $(".dot_2").show();
            _this.isGjFilter = true;
        });
        $(".dot_2").click(function () {
            $("#matchFilter").hide("slow");
            $(this).hide();
            $(".dot_1").show();
            _this.isGjFilter = false;
        });
    },

    checkIsLoadInfos : function(){  //判断数据是否还在加载中
         if(!this.isLoadInfos) return alert('数据正在加载中!');
    },

    selectAF:function (type) {  //赛事筛选 全选 反选
        var _this = this;
        if (type == 1) {//全选
            $("#matchType :checkbox").prop('checked', true);
            var sel_matches = "[matchname]";
            this.batchShow(1, sel_matches);
        } else if (type === 0) { //反选
            var sel_matches = [];
            $("#matchType :checkbox").each(function () {
                if (!$(this).prop('checked')) {
                    sel_matches.push("[matchname='" + $(this).val() + "']");
                }
                $(this).prop('checked', !$(this).prop('checked'));
            });
            $("#tabInfos tr").hide();
            sel_matches = sel_matches.join(',');
            this.batchShow($("#matchType :checked").length, sel_matches);
        }
    },
    selectPKAF:function (f, type) {   //盘口筛选 全选 反选
        var _this = this;
        $("#filterPank :checkbox").each(function () {
            var pankName = $(this).val();
            if (type === 1) {
                $(this).attr('checked', true);
                _this.filterPank(true, pankName);
            } else if (type === 0) {
                $(this).attr('checked', !$(this).attr('checked'));
                _this.filterPank($(this).attr('checked'), pankName);
            }
        });
    },
    batchShow:function (flag, s) { //单独每一个联赛
        var selector = s, sel_lots = [], sel_panks = [];
        if (this.isGjFilter) {
            $("#filterLottery :checked").each(function () {
                var typeName = $(this).val();
                sel_lots.push("[type*='" + typeName + "']");
            });
            sel_lots = sel_lots.join(',');
            $("#filterPank :checkbox").each(function () {
                var pankName = $(this).val();
                sel_panks.push("[pank='" + pankName + "']");
            })
            sel_panks = sel_panks.join(',');
            var trs = $("#tabInfos > tr").filter(selector).filter(sel_lots).filter(sel_panks).toggleDispay(flag);
        } else {
            $("#tabInfos > tr").filter(selector).toggleDispay(flag);
        }
    },
    filterLottery:function (obj) { //彩种筛选
        var flag = $(obj).prop('checked');
        var typeName = $(obj).val();
        var selector = "[type*='" + typeName + "']", sel_matches = [], sel_panks = [];
        $("#matchType :checked").each(function () {
            var matchName = $(this).val();
            sel_matches.push("[matchname='" + matchName + "']");
        });
        sel_matches = sel_matches.join(',');
        $("#filterPank :checkbox").each(function () {
            var pankName = $(this).val();
            sel_panks.push("[pank='" + pankName + "']");
        })
        sel_panks = sel_panks.join(',');
        var trs = $("#tabInfos tr" + selector).filter(sel_matches).filter(sel_panks).toggleDispay(flag);
    },
    filterPank:function (flag, panKName) { //盘口筛选
        var selector = "[pank='" + panKName + "']", sel_matches = [], sel_lots = [];
        $("#matchType :checked").each(function () {
            var matchName = $(this).val();
            sel_matches.push("[matchname='" + matchName + "']");
        });
        sel_matches = sel_matches.join(',');
        $("#filterLottery :checked").each(function () {
            var typeName = $(this).val();
            sel_lots.push("[type*='" + typeName + "']");
        })
        sel_lots = sel_lots.join(',');
        var trs = $("#tabInfos tr" + selector).filter(sel_matches).filter(sel_lots).toggleDispay(flag);
    },
    setViewKey:function (key) {
        this.viewKey = key;
        this.viewObj.parent().attr('class', (key == 'one' ? 'tab4' : 'tab1'))
    },
    showWeekView:function (date) {
        var keyObj = {'one':'drawWeekView', 'four':'drawFourWeeksView', 'month':'drawMonthView'};
        this[keyObj[this.viewKey]] && this[keyObj[this.viewKey]](date);
    },
    parseDate:function (str) { //yy-mm-hh
        return new Date(str.replace(/-/g, '/'))
    },
    rollContition:function (f) {
        var keyObj = {'one':'roll', 'four':'roll', 'month':'rollMonth'};
        if (this.viewKey == 'one') f *= 7;
        else if (this.viewKey == 'four') f *= 28;
        if (this.tar_date[keyObj[this.viewKey]]) {
            this.tar_date = this.tar_date[keyObj[this.viewKey]](f);
        }
    },
    setCurDate:function (d) {
        this.sel_date = d;
        this.month = this.sel_date.getMonth();
        this.week = this.sel_date.getWeekInYear();
    },
    selectDay:function (obj) {
        this.checkIsLoadInfos();
        obj = $(obj) , this.matchArr = [];
        this.container.children('li').removeClass('on');
        obj.addClass('on');
        var to_date = this.parseDate(obj.attr('date'));
        var issue = obj.attr('issue');
        var zcissue = obj.attr('zcissue');
        this.createMatchInfos(issue, to_date, zcissue);
        this.tar_date = to_date;
        this.sel_date = to_date;
        if (to_date.getMonth() != this.month) {
            this.drawMonthPannel();
        }
        this.matchFlag = false;
        this.showWeekView(to_date);
        this.setCurDate(to_date);
    },
    setMatchDate:function (obj) {
        var date = obj.split('-'), str;
        str = date[0] + '年' + date[1] + '月' + date[2] + ' ' + this.getWeek(date[0], date[1], date[2]);
        return str;
    },
    getWeek:function (y, m, d) {
        var w = new Date('' + y + '/' + m + '/' + d).getDay(), arr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
        return arr[w];
    },
    //重新封装一下数据格式
    setMatchInfos:function (arr) {
        var info = {};
        for (var i = 0, len = arr.length; i < len; i++) {
            var obj = { 'jcNum':arr[i].jc || 0,
                'bdissue':arr[i].bdissue || 0,
                'dcNum':arr[i].bd || 0,
                'issueStop':arr[i].zc || 0,
                'countNum':arr[i].all || 0
            };
            info[arr[i].date] = obj;
        }
        return info;
    },
    getMathcInfos:function (startTime, len) { //获取比赛数据信息
        startTime = startTime.format('YY-MM-DD');
        var data = {date:startTime, length:len};
        var newMatchInfos , _this = this;
        $.ajax({
            url:'../soccerdata/queryCPRL',
            type:'post',
            data:data,
            async:false,
            success:function (msg) {
                newMatchInfos = _this.setMatchInfos(msg.rows);
                _this.matchInfos = newMatchInfos;
            },
            error:function () {
                alert("您好，获取比赛信息数据出现异常，请稍后。");
            }
        });
        return newMatchInfos;
    },
    /*
    getMathcInfos:function (startTime, len) { //获取比赛数据信息
        startTime = startTime.format('YY-MM-DD');
        var data = {date:startTime, length:len};
        var newMatchInfos , _this = this;
        $.ajax({
            url:'http://cp.zgzcw.com/lottery/queryCPRL.action',
            type:'post',
            data:data,
            async:false,
            success:function (msg) {
                newMatchInfos = _this.setMatchInfos(eval('(' + msg + ')'));
                _this.matchInfos = newMatchInfos;
            },
            error:function () {
                alert("您好，获取比赛信息数据出现异常，请稍后。");
            }
        });
        return newMatchInfos;
    },*/
    createMatchInfos:function (issue, date, zcIssue) {
        this.isLoadInfos = false ;
        $("#statusMask").show();
        $("#statusBox").show();
        this.clearTable();
        var d = date.format('YY-MM-DD');
        var data = {'issue':issue, 'date':d, 'zcIssue':zcIssue};
        var _this = this, args = Array.prototype.slice.call(arguments);
        args[1] = d;
        $.ajax({
            url:'http://cp.zgzcw.com/ssc/lottery/getMatchListForRl.jsp',
            data:data,
            //dataType:'json',
            async:false,
            success:function (msg) {
                //msg = msg.replace(/[\r\n]/g,"");
                //msg = JSON.parse(msg);
                msg = eval('(' + msg + ')');
                _this.genMatchList(msg);
                _this.createTrHtml(args);
                $("#statusMask").hide();
                $("#statusBox").hide();
                _this.isLoadInfos = true ;
            },
            error:function () {
                  alert('获取北单比赛数据出现异常');
                  $("#statusMask").hide();
                  $("#statusBox").hide();
            }
        })
        $("#matchDate,#infoD").html(date.format("YY年MM月DD 星期dd"));
    },
    genMatchList:function (msg) {
        var matchArr = [], matchList = {};
        var g = function (m, d) {
            try {
                return (new Function('m', 'd', 'return (m[d])'))(m, d)
            } catch (e) {
            }
        }, 
        f = function (m, k) {
            var a;
            if (a = g(m, k)) {
                var b = k == 'jc' ? 'playid' : 'playId'; //bd playId
                if (k == 'zc') a = a['matchInfo'] || [];
                for (var i = 0, il = a.length; i < il; i++) {
                    var c = a[i][b];
                    if (!matchList[c]) {
                        matchList[c] = a[i];
                    } else { //repeat
                        matchList[c] = $.extend(matchList[c], a[i]);
                        if (k == 'zc') matchList[c]['i'] = i;
                    }
                    matchList[c]['playId'] = c;
                    matchList[c][k] = 1;
                }
            }
        }
        var arr = ['bd', 'jc', 'zc'];
        $.each(arr, function (i, n) {
            var m = msg;
            f(m, n);
        })
        this.matchList = matchList;
    },
    createTrHtml:function (issuesArr) {
        var m = this.matchList;
        var strarr = [];
        for (var i in m) {
            var match = m[i], iszc = match['zc'], type = '', pank = '', touz = '', home = match['matchHome'], guest = match['matchGuest'];
            var matchName = '', clsName = '', bifen = '', type = '', time = '', url_Y = 'http://fenxi.zgzcw.com/'+match['playId']+'/ypdb',
                url_O = 'http://fenxi.zgzcw.com/'+match['playId']+'/bjop', url_X = 'http://fenxi.zgzcw.com/'+match['playId']+'/bfyc';
            if (match['bd']) {
                type += 'dc';
                touz += '<a href="/lottery/bdplayvsforJsp.action?lotteryId=200&issue=' + issuesArr[0] + '" target="_blank" class="a1">单场</a>';
            }else touz += '<a name="bd" class="lose">单场</a>';
            if(match['jc']) {
                type += 'jc';
                touz += ' <a href="/lottery/jcplayvsForJsp.action?lotteryId=22&issue=' + issuesArr[1] + '" target="_blank" class="a1">竞彩</a>';
            }else  touz += ' <a name="jc" class="lose">竞彩</a>';
            if (!iszc) {
                bifen = match['bf'];
                if (bifen) bifen = match['bf'].split(',').splice(-2, 2).join('-');
                else bifen = '';
                matchName = match['matchName'];
                if(match['matchStartTime']) time = new Date(match['matchStartTime'].replace(/-/ig,'/')).format('hh:mm');
                touz += ' <a name="zc" class="lose">足彩</a>';
            } else {
                type += 'zc';
                matchName = match['leageName'];
                var temparr = match['zuizhongbifen'].split(';'), inx = match['i'];
                if (temparr < 2) bifen = '';
                else bifen = temparr[+inx] || '';
                time = new Date(match['gameStartDate'].replace(/-/ig,'/')).format('hh:mm');
                if (issuesArr[2] != '0')  touz += ' <a href="/lottery/14csfc/index.jsp?issueNumber=' + issuesArr[2] + '" target="_blank" class="a1">足彩</a>';
                else  touz += ' <a href="/lottery/14csfc/index.jsp" target="_blank" class="a1">足彩</a>';
                home = match['hostName'] || home;
                guest = match['guestName'] || guest;
            };
            clsName = match['leagueStyle'];
            this.matchArr.push(matchName);
            var europeSp = match['europeSp'], yapan = match['yapan'];
            if (!europeSp) europeSp = '  ';
            if (!yapan) yapan = '  ';
            europeSp = europeSp.split(' ');
            yapan = yapan.split(' ');
            pank = yapan[1].replace(/\//ig, '');
            if (',平手,平手半球,半球,半球一球,一球,一球球半,球半,球半两球,两球,'.indexOf(',' + pank + ',') < 0) pank = '其它';
            strarr.push('<tr matchName="' + matchName + '" type="' + type + '" pank ="' + pank + '" >' +
                '<td width="88" class="'+clsName+'" title="' + matchName + '">' + matchName.substring(0, 3) + '</td>' +
                '<td width="45">' + time + '</td>' +
                '<td width="102" class="home_c"><a href="http://saishi.zgzcw.com/soccer/team/'+match['homeId']+'" target="_blank" title="'+home+'">' + home + '</a></td>' +
                '<td width="40"><b class="font1">' + bifen + '</b></td>' +
                '<td width="102" class="guest_c"><a href="http://saishi.zgzcw.com/soccer/team/'+match['guestId']+'" target="_blank" title="'+guest+'">' + guest + '</a></td>' +
                '<td width="40"><a href=' + url_O + ' target=_blank class=>欧</a></td>' +
                '<td width="40"><a href=' + url_Y + ' target=_blank class=>亚</a></td>' +
                '<td width="40"><a href=' + url_X + ' target=_blank class=>析</a></td>' +
                '<td width="38"><b class="font2">' + europeSp[0] + '</b></td>' +
                '<td width="38"><b class="font2">' + europeSp[1] + '</b></td>' +
                '<td width="38"><b class="font2">' + europeSp[2] + '</b></td>' +
                '<td width="38"><b class="font2">' + yapan[0] + '</b></td>' +
                '<td width="98"><b class="font2">' + yapan[1] + '</b></td>' +
                '<td width="38"><b class="font2">' + yapan[2] + '</b></td><td>' + touz + '</td></tr>');
        }

        $("#tabInfos").html(strarr.join(''));
        this.createMatchType();
    },
    createMatchType:function () {
        var matchStr = ' ';
        this.matchArr = this.matchArr.unique();
        for (var j = 0, l = this.matchArr.length; j < l; j++) {
            var newName = (this.matchArr[j].length > 3) ? this.matchArr[j].substr(0, 6) : this.matchArr[j];
            matchStr += '<label><input type="checkbox" checked value="' + this.matchArr[j] + '"/>' + newName + '</label>'
        }
        $("#matchType").append(matchStr);
        $('#filterLottery :checkbox, #filterPank :checkbox').prop('checked', true);
    },
    clearTable:function () {
        $("#tabInfos").empty();
        $("#matchType").empty();
    },
    drawWeekView:function (date) {
        /*
        if (date && this.week == date.getWeekInYear()) {
            var inx = this.tar_date.getDay();
            this.viewObj.find('td').removeClass('on').eq(inx).addClass('on');
            return;
        } */
        var html = ['<tr>'], week_arr = this.tar_date.getSevenDaysMap();
        var matchDate = (this.matchFlag) ? this.matchInfos : this.getMathcInfos(week_arr[0], week_arr.length);
        if($.isEmptyObject(matchDate)) return false ;
        for (var i = 0, len = week_arr.length; i < len; i++) {
            var day = week_arr[i], day_arr = day.format('MM月DD日 星期dd').split(' ');
            var no = day.format('YY-MM-DD');
            var cls = (this.sel_date.equalsDate(day)||(this.sel_date.getDay() === day.getDay())) ? 'class="on"' : '';
            if ( !this.today && this.sel_date.equalsDate(day)) {
                this.today = [matchDate[no].bdissue, day, matchDate[no].issueStop];
                this.createMatchInfos(matchDate[no].bdissue, day, matchDate[no].issueStop);
            }
            html.push('<td id="' + no + '" ' + cls + '  issue="' + matchDate[no].bdissue + '" zcissue="' + matchDate[no].issueStop + '" ><p class="s_11"><i>' + day_arr[1] + '</i><br/><b class="font3">' + day_arr[0] + '</b><br/><span class="match_num">' + matchDate[no].countNum + '场比赛</span></p><p id="p-' + no + '" class="s_12">' + matchDate[no].countNum + '场比赛<br/>竞彩' + matchDate[no].jcNum + '场<br/>单场' + matchDate[no].dcNum + '场');
            if(matchDate[no].issueStop != 0)html.push('<br/>足彩' + matchDate[no].issueStop + '期停售</p>');
            html.push('</td>')
        }
        html.push('</tr>');
        this.viewObj.empty().append(html.join(''))
    },
    drawFourWeeksView:function () {
        var html = [], week_arr = this.tar_date.getFourWeeksMap();
        var matchDate = this.getMathcInfos(week_arr[0], week_arr.length);
        if($.isEmptyObject(matchDate)) return false ;
        html.push('<tr><td width="102" class="td_1">星期日</td><td width="102" class="td_1">星期一</td><td width="102" class="td_1">星期二</td><td width="102" class="td_1">星期三</td><td width="102" class="td_1">星期四</td><td width="102" class="td_1">星期五</td><td class="nonrbor td_1">星期六</td></tr>')
        html.push('<tr>');
        for (var i = 0, il = week_arr.length; i < il; i++) {
            var day = week_arr[i], day_arr = day.format('MM月DD日 星期dd').split(' ');
            var no = day.format('YY-MM-DD'), cls = this.sel_date.equalsDate(day) ? 'on' : '';
            if (i % 7 == 0 && i > 0)html.push('</tr><tr>');
            if (i % 7 == 6) cls += ' nonrbor';
            html.push('<td id="' + no + '" class="' + cls + '" issue="' + matchDate[no].bdissue + '" zcissue="' + matchDate[no].issueStop + '"><span class="td_2">' + day_arr[0] + '</span><p id="p-' + no + '"><span class="td_3">' + matchDate[no].countNum + '场比赛</span><span class="td_4">竞彩' + matchDate[no].jcNum + '场<br/>单场' + matchDate[no].dcNum + '场 <br/> 足彩' + matchDate[no].issueStop + '期停售 </span></p ></td>');
        }
        html.push('</tr>');
        this.viewObj.empty().append(html.join(''))
    },
    drawMonthView:function () {
        var html = [], f_d = this.tar_date.getMonthFirstDay(), l_d = this.tar_date.getMonthLastDay();
        var pre_days_arr = f_d.getWeekMap(-1);//取月第一天的星期
        var sbl_days_arr = l_d.getWeekMap(1);//取月最后一天的星期
        var days_arr = pre_days_arr.concat(this.tar_date.getMonthMap(), sbl_days_arr);
        var matchDate = (this.matchFlag) ? this.matchInfos : this.getMathcInfos(days_arr[0], days_arr.length);
        if($.isEmptyObject(matchDate)) return false ;
        html.push('<tr><td width="102" class="td_1">星期日</td><td width="102" class="td_1">星期一</td><td width="102" class="td_1">星期二</td><td width="102" class="td_1">星期三</td><td width="102" class="td_1">星期四</td><td width="102" class="td_1">星期五</td><td class="nonrbor td_1">星期六</td></tr>')
        html.push('<tr>');
        var pre_num = pre_days_arr.length, sbl_num = this.tar_date.getMonthDays() + pre_num;
        for (var i = 0, il = days_arr.length; i < il; i++) {
            var day = days_arr[i], day_arr = day.format('DD 星期dd').split(' ');
            var no = day.format('YY-MM-DD'), cls = '';
            if (i % 7 == 0 && i > 0)html.push('</tr><tr>');
            if (i % 7 == 6) cls += 'nonrbor';
            if (i < pre_num || i >= sbl_num) cls += ' off';
            if (this.sel_date.equalsDate(day)) cls += ' on';
            html.push('<td id="' + no + '" class="' + cls + '" issue="' + matchDate[no].bdissue + '" zcissue="' + matchDate[no].issueStop + '"><span class="td_2">' + day_arr[0] + '</span><p id="p-' + no + '"><span class="td_3">' + matchDate[no].countNum + '场比赛</span><span class="td_4">竞彩' + matchDate[no].jcNum + '场<br/>单场' + matchDate[no].dcNum + '场 <br/> 足彩' + matchDate[no].issueStop + '期停售 </span></p ></td>');
        }
        html.push('</tr>');
        this.viewObj.empty().append(html.join(''))
    },
    drawMonthPannel:function () {  //绘月视图
        var _this = this, f_d = this.tar_date.getMonthFirstDay(), l_d = this.tar_date.getMonthLastDay();
        var pre_days_arr = f_d.getWeekMap(-1);//取月第一天的星期
        var sbl_days_arr = l_d.getWeekMap(1);//取月最后一天的星期
        var days_arr = pre_days_arr.concat(this.tar_date.getMonthMap(), sbl_days_arr);
        this.days = [];
        this.container.empty();
        var today = new Date();
        var pre_num = pre_days_arr.length, sbl_num = this.tar_date.getMonthDays() + pre_num;
        if (days_arr.length < 42) days_arr = days_arr.concat(l_d.roll(7).getWeekMap());  //补全6行
        var len = days_arr.length, liObj = '';
        var matchDate = this.getMathcInfos(days_arr[0], days_arr.length);
        if($.isEmptyObject(matchDate)) return false ;
        for (var i = 0; i < len; i++) {
            var li = $('<li></li>');
            var spanObj = $('<span>' + days_arr[i].getDate() + '</span>');
            var no = days_arr[i].format('YY-MM-DD');
            li.append(spanObj);
            li.attr('date', no);
            li.attr('issue', matchDate[no].bdissue);
            li.attr('zcissue', matchDate[no].issueStop);
            if (i < pre_num || i >= sbl_num) li.addClass('off');
            if (today.equalsDate(days_arr[i])) li.addClass('cur');
            if (this.sel_date.equalsDate(days_arr[i])) {
                li.addClass('on');
            }
            (function (i) {
                li.click(function () {
                    _this.selectDay(this)
                })
            })(i);
            this.days.push(li);
            if (liObj == '') liObj = li;
            else liObj = liObj.add(li);
        }
        this.showDateObj.html(this.tar_date.format('YY年MM月'));
        this.container.append(liObj);
    }
}
$(document).ready(function () {
    window.calendar = new FootBallCalendar('calendar', {
        'showDateObj':$('#info'),
        'viewObj':$('#calendarMap')
    });
})
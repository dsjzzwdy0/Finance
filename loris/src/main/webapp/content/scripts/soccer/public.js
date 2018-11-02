var NAME_YPS =
	[ "平手", "平/半", "半球", "半/一", "一球", "一/球半", "球半", "球半/两", "两球", "两/两半", "两半", "两半/三", "三球", 
	  "受平/半", "受半球", "受半/一","受一球", "受一/球半", "受球半", "受球半/两", "受两球", "受两/两半", "受两半", "受两半/三", "受三球" ];

var NAME_YPS_VALUES =
	[ 0.0, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, -0.25, -0.50, -0.75, -1.0,
			-1.25, -1.5, -1.75, -2.0, -2.25, -2.5, -2.75, -3.0 ];

var DEFAUL_THRESHOLD =0.002;
/**
 * 通过让球名称获得让球的数量
 * @param value
 * @returns
 */
function getHandicapName(value){
	var len = NAME_YPS_VALUES.length;
	for(var i = 0; i < len ; i ++)
	{
		if(withinErrorMargin(value, NAME_YPS_VALUES[i]))
		{
			return NAME_YPS[i];
		}
	}
	return '';
}

/**
 * 通过让球的名称获得让球数
 * @param name
 * @returns
 */
function getHandicapValue(name)
{
	var len = NAME_YPS.length;
	for(var i = 0; i < len ; i ++)
	{
		if(name == NAME_YPS[i])
		{
			return NAME_YPS_VALUES[i];
		}
	}
	return -100;
}

//空表
function getEmptyOddsColumn()
{
	return '<td class="oddsvalue">无</td><td class="oddsvalue">无</td><td class="oddsvalue">无</td>';
}

//格式化亚盘数据
function formatYpValues(yp, source, first)
{
	var st = first ? 0 : 3;
	if($.isNullOrEmpty(yp.values))
	{
		return '无';
	}
	var handicapName;
		
	var html = '';
	handicapName = getHandicapName(yp.values[st + 1]);
	html = '<div class="oddsvalue" title="来源：' + source + '">' + yp.values[st].toFixed(2) + '</div>';
	html += '<div class="handicap" title="' + handicapName + '">' + handicapName+ '</div>';
	html += '<div class="oddsvalue" title="来源：' + source + '">' + yp.values[st + 2].toFixed(2) + '</div>';
	return html.join('');
}


/**
 * 格式化比赛的球队信息
 * @param match
 * @returns
 */
function formatMatchTeamInfo(match)
{
	var str = '<div class="team"><a class="teamInfo left" tid="' + match.homeid 
		str	+= '" href="#" onclick="showTeamInfo(this, ' + match.homeid + ')" title="'; 
		str	+= match.homename + '">';
		str += $.isNullOrEmpty(match.homerank) ? '' : '[' + match.homerank + ']';
		str += match.homename 
		str	+= '</a> <div class="vsclass" > vs </div> <a class="teamInfo right" tid="' 
		str += match.clientid +'" href="#" onclick="showTeamInfo(this, ' + match.homeid + ')" title="';
		str	+= match.clientname + '">' + match.clientname; 
		str += $.isNullOrEmpty(match.clientrank) ? '' : '[' + match.clientrank + ']';
		str += '</a></div>';
	return str;
}

/**
 * 格式化联赛信息
 * @param match
 * @returns
 */
function formatLeagueInfo(match)
{
	return '<a href="analeague?type=leaguerel&mid=' + match.mid + '" class="leagueInfo">' + match.leaguename + '</a>';
}

function showTeamInfo(src, tid)
{
	window.open('team?tid=' + tid);
}

/**
 * 联赛数据记录
 * @param lid
 * @param name
 * @returns
 */
function LeagueRec(lid, name)
{
	this.lid = lid;
	this.num = 1;
	this.name = name;
	
	this.setName = function(name)
	{
		this.name = name;
	}
	
	this.addNum = function()
	{
		this.num ++;
	}
}


/**
 * 初始化联赛控制面板
 * @param matches
 * @returns
 */
function initLeaguePanel(matches)
{
	var len = matches.length;
	var recs = [];
	for(var i = 0; i < len; i ++)
	{
		var m = matches[i];
		if(!addLeagueRec(m.lid, recs))
		{
			var r = new LeagueRec(m.lid, m.leaguename);
			recs.push(r);
		}
	}
	
	len = recs.length;
	var html = [];
	for(var i = 0; i < len; i ++)
	{
		var rec = recs[i];
		html.push('<label><input name="CheckboxGroup1" value="' + rec.lid + '" type="checkbox" checked>');
		html.push('<em class="echao" style="background-color: rgb(102, 153, 0)">' + rec.name + '</em>[' + rec.num + ']');
		html.push('</label>');		
	}
	
	$('#leagueList').html(html.join(''));
}

/**
 * 加入联赛的信息
 */
function addLeagueRec(lid, recs)
{
	var num = recs.length;
	for(var j = 0; j < num; j ++)
	{
		var rec = recs[j];
		if(rec.lid == lid)
		{
			rec.addNum();
			return true;
		}
	}
	return false;
}

/**
 * 检测是否在误差范围内
 * @param left
 * @param right
 * @returns
 */
function withinErrorMargin (left, right, threshold)
{
	if($.isNullOrEmpty(threshold) || threshold <= 0)
	{
		threshold = DEFAUL_THRESHOLD;
	}
	return Math.abs(left - right) < threshold;
}

/** 
 * 判断是否null 
 * @param data 
 */
function isNull(data) {
    return (data == "" || data == undefined || data == null);
}

$(function () {
    $.ajax2 = function (options) {
        var img = $("#progressImgage");
        var mask = $("#maskOfProgressImage");
        var complete = options.complete;
        options.complete = function (httpRequest, status) {
            img.hide();
            mask.hide();
            if (complete) {
                complete(httpRequest, status);
            }
        };
        options.async = true;
        img.show().css({
            "position": "fixed",
            "top": "50%",
            "left": "50%",
            "margin-top": function () { return -1 * img.height() / 2; },
            "margin-left": function () { return -1 * img.width() / 2; }
        });
        mask.show().css("opacity", "0.1");
        $.ajax(options);
    };
});


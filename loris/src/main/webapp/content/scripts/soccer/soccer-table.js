/**
 * Soccer图表js类
 */


/**
 * 从远程获得数据
 */
function getDataFromServer(soccerTable, gridTable) {
	var $body = $(gridTable).find('tbody');
	$($body).html('正在下载数据...');
	var source = soccerTable.options.source;
	if ($.isNullOrEmpty(source) && $.isNullOrEmpty(soccerTable.options.url)) {
		$($body).html('');
		layer.msg('错误，您没有设置服务器。');
		return;
	}
	source.success = function(json) {
		$($body).html('');
		if($.isNotNullOrEmpty(this.presuccess))
		{
			this.presuccess(json, soccerTable);
		}
		soccerTable.createTable(gridTable);
	};
	source.error = function() {
		$($body).html('');
		layer.msg(soccerTable.options.url + '出错.');
	}

	$.ajax(source);
}

/**
 * 扩展函数
 */
$.fn.extend({
	soccerTable: function(soccerTable)
	{
		var gridTable = this;
		var $body = $(this).find('tbody');
		if(soccerTable === 'destroy')
		{
			if(!$.isNullOrEmpty($body))	$($body).html('');
			return;
		}
		
		if($.isNullOrEmpty(soccerTable.options.columns) ||
				$.isNullOrEmpty(soccerTable.rows) ||
				soccerTable.refresh)
		{
			getDataFromServer(soccerTable, gridTable);
		}
		else
		{			
			soccerTable.createTable($(this));
		}
	},
});

//盘口数据的处理
var OddsUtil = OddsUtil || {};
OddsUtil.NAME_YPS =
	[ "平手", "平/半", "半球", "半/一", "一球", "一/球半", "球半", "球半/两", "两球", "两/两半", "两半", "两半/三", "三球", 
	  "受平/半", "受半球", "受半/一","受一球", "受一/球半", "受球半", "受球半/两", "受两球", "受两/两半", "受两半", "受两半/三", "受三球" ];
OddsUtil.NAME_YPS_VALUES =
	[ 0.0, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, -0.25, -0.50, -0.75, -1.0,
			-1.25, -1.5, -1.75, -2.0, -2.25, -2.5, -2.75, -3.0 ];
OddsUtil.DEFAUL_THRESHOLD =0.002;
OddsUtil.formatHandicap = function(value)
{
	var len = OddsUtil.NAME_YPS_VALUES.length;
	for(var i = 0; i < len ; i ++)
	{
		if(OddsUtil.withinErrorMargin(value, OddsUtil.NAME_YPS_VALUES[i], OddsUtil.DEFAUL_THRESHOLD))
		{
			return OddsUtil.NAME_YPS[i];
		}
	}
	return '';
}

if (!Array.isArray)
{
	Array.isArray = function(arg) {
		return Object.prototype.toString.call(arg) === '[object Array]';
	};
}

var Association = Association || {};
Association.AssClasses = ['association_red', 'association_thin', 'association_cyan', 'association_blue', 'association_orange'];

//取得关系的序数
Association.getRelationClass = function(index)
{
	if(index < 0)
	{
		return '';
	}
	if(index >= 5)
	{
		index = index % 5;
	}
	return Association.AssClasses[index];
}

/**
 * 检测是否在误差范围内
 * @param left
 * @param right
 * @returns
 */
OddsUtil.withinErrorMargin = function(left, right)
{
	return $.withinErrorMargin(left, right, 0.003);
}
OddsUtil.withinErrorMargin = function(left, right, threshold)
{
	if($.isNullOrEmpty(threshold) || threshold <= 0)
	{
		threshold = DEFAUL_THRESHOLD;
	}
	return Math.abs(left - right) < threshold;
}

/**
 * 通过让球的名称获得让球数
 * @param name
 * @returns
 */
OddsUtil.getHandicapValue = function(name)
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

/**
 * 状态改变监听器
 * @returns
 */
function StateListeners()
{
	this.listeners = [];
	this.add = function(listener)
	{
		this.listeners.push(listener);
	},
	this.notify = function(state, source, conf)
	{
		var len = this.listeners.length;
		for(var i = 0; i < len; i ++)
		{
			this.listeners[i](state, source, conf);
		}
	}
}

function Relator(threshold, sameLeague, useDraw)
{
	this.threshold = threshold;
	this.sameLeague = sameLeague;
	this.useDraw = useDraw;
}

//排序器
function FieldSorter(fields, asc)
{
	this.fields = fields;
	this.asc = asc;
	this.len = fields.length;
	
	this.addField = function(value)
	{
		this.fields.push(values);
		this.len = thie.fields.length;
	}	
	this.removeField = function(value)
	{
		var index = this.fields.indexOf(value);
		if (index > -1) {
			this.fields.splice(index, 1);
		}
		this.len = thie.fields.length;
	}
	this.clear = function()
	{
		this.fields.splice(0, this.fields.length);
		this.len = 0;
	}
	this.setFields = function(fields)
	{
		this.fields.splice(0, this.fields.length);
		this.fields.concat(fields);
		this.len = fields.length;
	}
	this.setSorter = function(fields, asc)
	{
		this.fields = fields;
		this.asc = asc;
		this.len = fields.length;
	}
	this.compare = function(a, b)
	{
		var asc = this.asc;
		var fs = this.fields;
		var r = 0;
		var field;
		for(var i = 0; i < this.len; i ++)
		{
			field = fs[i];
			var v0 = a[field];
			var v1 = b[field];			
			r = this.compareValue(v0, v1);
			if(r != 0)
			{
				break;
			}
		}		
		return asc ? r : -r;
	}
	this.compareValue = function(a, b)
	{
		var v0;
		var v1;		
		if($.checkNumber(a) && $.checkNumber(b))
		{
			v0 = parseFloat(a);
			v1 = parseFloat(b);
		}
		else
		{
			v0 = a;
			v1 = b;	
		}
		var r = 0;
		if(v0 < v1)
		{
			r = -1;
		}
		else if(v0 > v1)
		{
			r = 1;
		}
		return r;
	}
}

//排序器
function MatchOddsFieldSorter(field, asc)
{
	this.field = field;
	this.asc = asc;
	
	this.setField = function(value)
	{
		this.field = value;
	}
	this.setSorter = function(field, asc)
	{
		this.field = field;
		this.asc = asc;
	}
	this.compare = function(a, b)
	{
		var v0, v1;
		var r = 0;
		var asc = this.asc;
		var field = this.field.toString();		
		var vs = field.split(',');
		if(vs.length > 1)
		{
			field = vs[0];
			var gid = vs[1];
			var index = parseInt(vs[2]);
			var st = table.options.first ? 0 : 3;
			var odds0;
			var odds1;
			if(field == 'opItems')
			{
				odds0 = MatchDoc.getOpOdds(a, gid);
				odds1 = MatchDoc.getOpOdds(b, gid);
			}
			else
			{
				odds0 = MatchDoc.getYpOdds(a, gid);
				odds1 = MatchDoc.getYpOdds(b, gid);
			}
			if($.isNullOrEmpty(odds0))
			{
				v0 = -1000;
			}
			else
			{
				v0 = odds0.values[st + index];
			}
			if($.isNullOrEmpty(odds1))
			{
				v1 = -1000;
			}
			else
			{
				v1 = odds1.values[st + index];
			}
		}
		else if(field == 'homePerf' || field == 'clientPerf')
		{
			v0 = a[field].score;
			v1 = b[field].score;
		}
		else
		{
			v0 = a[field];
			v1 = b[field];	
		}		
		r = this.compareValue(v0, v1);
		return asc ? r : -r;
	}
	this.compareValue = function(a, b)
	{
		var v0;
		var v1;
		if(a == 'NaN')
		{
			return b == 'NaN' ? 0 : -1;
		}
		if(b == 'NaN') return 1;
		if($.checkNumber(a) && $.checkNumber(b))
		{
			v0 = parseFloat(a);
			v1 = parseFloat(b);
		}
		else
		{
			v0 = a;
			v1 = b;	
		}
		var r = 0;
		if(v0 < v1)
		{
			r = -1;
		}
		else if(v0 > v1)
		{
			r = 1;
		}
		return r;
	}
}

/**
 * 足球数据表的
 * @returns
 */
function SoccerTableColumns()
{
	this.createBasicMatchColumns = function(rowspan)
	{
		var columns = [];		
		var col0 = {
			field: 'ordinary',
			name: '编号',
			sortable: true,
			rowspan: rowspan,
			type: 'base',
		}
		var col1 = {
			field: 'leaguename',
			name: '联赛',
			sortable: true,
			rowspan: rowspan,
			type: 'base',
			formatter: function(value, row, index){
				return '<a href="analeague?type=leaguerel&mid=' + row.mid + '" class="leagueInfo">' + value + '</a>';
			},
		}
		var col2 = {
			field: 'mid',
			name: '比赛',
			rowspan: rowspan,
			type: 'base',
			formatter: function(value, row, index){
				return formatMatchTeamInfo(row);
			}
		}
		var col3 = {
			field: 'matchtime',
			sortable: true,
			name: '比赛时间',
			rowspan: rowspan,
			type: 'base',
			formatter: function(value, row, index){
				return '<div title="比赛时间: ' + value + '">' + formatDate(value, 'hh:mm') + '</div>';
			}			
		}
		columns.push(col0);
		columns.push(col1);
		columns.push(col2);
		columns.push(col3);
		return columns;
	}
	
	this.createBasicOpColumns = function(opcorp)
	{
		var columns = [];
		for(var j = 0; j < 3; j ++)
		{
			var c = {
				field: 'opItems,' + opcorp.gid + ',' + j,
				name: j == 0 ? '胜' : j == 1 ? '平' : '负',
				sortable: true,
				className: 'oddsvalue',
				param: opcorp,
				indexValue: j,
				type: 'odds',
				formatter: function(value, row, index, oddsIndex, first){
					var c = this.param;
					var j = oddsIndex;//this.indexValue;
					var odds = MatchDoc.getOpOdds(row, c.gid);
					if($.isNullOrEmpty(odds) || $.isNullOrEmpty(odds.values)) return '无';
					else if($.isNotNullOrEmpty(table.options.relator))
					{
						return formatOpValueColumn(j, row, odds, c, first);
					}
					else
					{
						var st = first ? 0 : 3;
						var title = first ? '初盘时间 ' + odds.firsttime : '即时盘时间 ' + odds.time;
						return '<div class="oddsvalue" title="' + (st) + '">' 
							+ formatValue(odds.values[st + j]) + '</div>';
					}
				}
			}
			columns.push(c);
		}
		return columns;
	}
	
	this.createBasicYpColumns = function(ypcorp)
	{
		var columns = [];
		for(var j = 0; j < 3; j ++)
		{
			var c = {
				field: 'ypItems,' + ypcorp.gid + ',' + j,
				name: j == 0 ? '胜' : j == 1 ? '盘' : '负',
				sortable: true,
				className: 'oddsvalue',
				param: ypcorp,
				indexValue: j,
				type: 'odds',
				formatter: function(value, row, index, oddsIndex, first){
					var c = this.param;
					var j = oddsIndex;
					var odds = MatchDoc.getYpOdds(row, c.gid);
					if($.isNullOrEmpty(odds)) return '无';
					else
					{							
						var st = first ? 0 : 3;
						if(j == 1)
						{
							return '<div class="oddsvalue">' + OddsUtil.formatHandicap(odds.values[st + j])+ '</div>';
						}
						else
						{
							return '<div class="oddsvalue">' + formatValue(odds.values[st + j]) + '</div>';
						}
					}
				}
			}
			columns.push(c);
		}
		return columns;
	}
	
	this.createHistoryColumns = function(rowspan)
	{
		var columns = [];
		var col1 = {
			field: "homePerf",
			name: "主队战绩",
			rowspan: rowspan,
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index)
			{
				return '<div class="oddsvalue" title="' + performInfo(value) + '">' +
					($.isNullOrEmpty(value) ? '' : value.score) + "</div>";
			}
		};
    	var col2 = {
			field: "clientPerf",
			name: "客队战绩",
			rowspan: rowspan,
			valign: 'middle',
			sortable: true,
			formatter: function(value, row, index)
			{					
				return '<div class="oddsvalue" title="' + performInfo(value) + '">' +
					($.isNullOrEmpty(value) ? '' : value.score + "</div>");
			}
		}
    	var col3 = {
			field: "lastMatch",
			name: "最近交战",
			rowspan: rowspan,
			valign: 'middle',
			formatter: function(value, row, index)
			{					
				return '<div class="oddsvalue" title="' + matchInfo(row) + '">' +
					($.isNullOrEmpty(value) ? '无' : formatDate(value.matchtime, 'yy-MM-dd'))
					+ "</div>";
			}
		}
    	columns.push(col1);
    	columns.push(col2);
    	columns.push(col3);
    	return columns;
	}
	
	this.createOperatorColumns = function(rowspan)
	{
		var col = {
			field: 'mid',
			name: '详细信息',
			rowspan: 2,
			type: 'base',
			formatter: function(value, row, index){
				var mid = value;
			   	var html = '<a class="analysis" href="match?type=bjop&mid=' + mid + '" target="blank">欧</a>';
			   	html += '<a class="analysis" href="match?type=ypdb&mid=' + mid + '" target="blank">亚</a>';
			   	html += '<a class="analysis" href="match?type=bfyc&mid=' + mid + '" target="blank">析</a>';
	        	return html;
			}
		}
		return col;
	}
	
	this.createPerformColumns = function(corpSetting)
	{
		var columns = [];
		var rows = [];
		var rows1 = [];
		
		rows = rows.concat(this.createBasicMatchColumns(2));	
		
		var opcorps = corpSetting.getOpCorps();
		var opcorplen = opcorps.length;
		var ypcorps = corpSetting.getYpCorps();
		var ypcorplen = ypcorps.length;
		
		for(var i = 0; i < opcorplen; i ++)
		{
			var corp = opcorps[i];
			var c = {
				name: corp.name + '(欧赔)',
				colspan: 3
			}			
			rows.push(c);
			rows1 = rows1.concat(this.createBasicOpColumns(corp));	
		}
		
		for(var i = 0; i < ypcorplen; i ++)
		{
			var corp = ypcorps[i];
			var c = {
				name: corp.name + '(亚盘)',
				colspan: 3
			}	
			rows.push(c);
			rows1 = rows1.concat(this.createBasicYpColumns(corp));	
		}
		rows = rows.concat(this.createHistoryColumns(2));
		rows.push(this.createOperatorColumns(2));		
		columns.push(rows);
		if(rows1.length > 0) columns.push(rows1);
		return columns;
	}
	
	//按照数据比较的配置进行数据分析
	this.createCorpSettingColumns = function(corpSetting)
	{
		var columns = [];
		var rows = [];
		var rows1 = [];
		
		rows = rows.concat(this.createBasicMatchColumns(2));	
		
		var opcorps = corpSetting.getOpCorps();
		var opcorplen = opcorps.length;
		var ypcorps = corpSetting.getYpCorps();
		var ypcorplen = ypcorps.length;
		
		for(var i = 0; i < opcorplen; i ++)
		{
			var corp = opcorps[i];
			var c = {
				name: corp.name + '(欧赔)',
				colspan: 3
			}			
			rows.push(c);
			rows1 = rows1.concat(this.createBasicOpColumns(corp));	
		}
		
		for(var i = 0; i < ypcorplen; i ++)
		{
			var corp = ypcorps[i];
			var c = {
				name: corp.name + '(亚盘)',
				colspan: 3
			}	
			rows.push(c);
			rows1 = rows1.concat(this.createBasicYpColumns(corp));	
		}
		rows.push(this.createOperatorColumns(2));		
		columns.push(rows);
		if(rows1.length > 0) columns.push(rows1);
		return columns;
	}
	
	//数据格式化
	function formatValue(value)
	{
		if($.isNullOrEmpty(value) || value == 'NaN')
		{
			return value;
		}
		return value.toFixed(2);

	}
	//格式化欧赔数据栏目
	function formatOpValueColumn(index, match, op, corp, first)
	{
		var relateClass = '';
		var relateIndex = -1;
		var doc = table.matchDoc;
		var st = first ? 0 : 3;
		var idx = MatchDoc.getOpMaxProbIndex(op, first);
		if($.isNotNullOrEmpty(doc) && (idx == (st + index)))
		{
			//var r = doc.getMatchRelation(match, corp, first);
			relateIndex = doc.getMatchRelationIndex(match, corp, first);
			relateClass = relateIndex >= 0 ? Association.getRelationClass(relateIndex) : '';	
		}		
		if($.isNotNullOrEmpty(relateClass))
		{
			relateClass += ' relation';
		}		
		var vals = op.values;
		var title = first ? '初盘时间 ' + op.firsttime + ' (' + vals[st + 0] + ',' + vals[st + 1] + ',' + vals[st + 2] + ')'
				: '即时盘时间 ' + op.time + ' (' + vals[st + 0] + ',' + vals[st + 1] + ',' + vals[st + 2] + ')';
		return '<div class="association ' + relateClass + '" title="' + title + '" index="' + relateIndex +
			'" mid="' + match.mid + '" gid="' + op.gid + '" type="' + first + '">' + formatValue(vals[st + index]) + '</div>'
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
	
	function performInfo(perform)
	{
		if($.isNullOrEmpty(perform))
		{
			return '无战绩记录';
		}
		var info = '最近' + perform.gamenum + '场, 胜' + perform.winnum + '场,平' + perform.drawnum +
			'场,负' + perform.losenum + '场,进' + perform.goal + '球, 失' + perform.losegoal + '球';
		return info;
	}

	function matchInfo(row)
	{
		var match = row;
		var lastMatch = row.lastMatch;	
		if($.isNullOrEmpty(lastMatch))
		{
			return '无';
		}		
		var info = (match.homeid == lastMatch.homeid) ? match.homename : match.clientname;
		info += " vs " + ((match.homeid == lastMatch.homeid) ? match.clientname : match.homename);
		info += " 比分 " + lastMatch.score;
		return info;
	}
}

//配置类
function CorpSetting(setting)
{
	this.corps = [];
	this.params = setting.params;
	this.name = setting.name;
	this.id = setting.id;
	this.size = setting.params.length;
	
	var plen = setting.params.length;
	for(var i = 0; i < plen; i ++)
	{
		var p = setting.params[i];
		if(p.type == 'corp')
		{
			var corp = {
				id: p.id,
				name: p.name,
				pid: p.pid,
				pname: p.pname,
				type: p.type,
				gid: p.value,
				source: p.value1,
				oddstype: p.value2,
				desc: p.desc
			}
			this.corps.push(corp);
		}
	}
	
	this.addTheoryCorp = function()
	{
		var corp = {
			id: '-100',
			name: '理论计算值',
			pname: '',
			gid: '-100',
			type: 'op',
			source: 'source',
			oddstype: 'op'
		};
		this.corps.push(corp);
	}

	
	this.getCorps = function()
	{
		return this.corps;
	}
	
	this.getOpCorps = function()
	{
		var ops = [];
		var corpsize = this.corps.length;
		for(var i = 0; i < corpsize; i ++)
		{
			var corp = this.corps[i];
			var type = corp.oddstype;
			if(type == 'op')
			{
				ops.push(corp);
			}
		}
		return ops;
	}
	
	this.getYpCorps = function()
	{
		var yps = [];
		var corpsize = this.corps.length;
		for(var i = 0; i < corpsize; i ++)
		{
			var corp = this.corps[i];
			var type = corp.oddstype;
			if(type == 'yp')
			{
				yps.push(corp);
			}
		}
		return yps;
	}
	
	this.getSize = function()
	{
		return this.corps.length;
	}
}


/**
 * 单字段值过滤器
 * @param key 字段名称
 * @param values 字段值
 * @param contained 是否过滤包含值列表的值的记录，是则过滤；否则不包含
 * @returns
 */
function FieldFilter(field, values, contained)
{
	this.values = values;
	this.key = field;
	this.contained = contained;
	
	/**
	 * 过滤器
	 */
	this.filter = function(rec)
	{
		var f = true;
		var len = this.values.length;
		for(var i = 0; i < len; i ++)
		{
			if(this.values[i] == rec[this.key])
			{
				f = false;
				break;
			}
		}
		if(contained)
		{
			f = !f;
		}
		return f;
	}
	
	this.addValue = function(value)
	{
		this.values.push(value);
	}	
	this.removeValue = function(value)
	{
		var index = this.values.indexOf(value);
		if (index > -1) {
			this.values.splice(index, 1);
		}
	}
	this.clear = function()
	{
		this.values.splice(0, this.values.length);
	}
	this.setValues = function(fieldValues)
	{
		this.clear();
		this.values = this.values.concat(fieldValues);
	}
}

/**
 * 足彩数据表格类
 * @param options
 * @returns
 */
function SoccerTable(options)
{
	this.options = options;
	
	this.$table = null;
	this.$body = null;
	this.$header = null;
	this.sortable = false;	
	this.matchDoc = null;
	
	this.createTable = function($table)
	{
		this.$table = $table;
		if($.isNullOrEmpty($table))
		{
			return;
		}		
		if(!$($table).hasClass('gridTable'))
		{
			$($table).addClass('gridTable');
		}		
		//初始化表格头部
		var $header = $($table).find('thead');
		if($.isNullOrEmpty($header) || $header.length == 0)
		{
			$header = $('<thead></thead>').appendTo($table);
		}
		this.$header = $header;
		//if($.isNullOrEmpty(($header).html().trim()))
		{
			this.createHeaders($header);
		}
		
		//处理表格内容
		var $body = $($table).find('tbody');
		if($.isNullOrEmpty($body) || $body.length == 0)
		{
			$body = $('<tbody></tbody>').appendTo($table);
		}
		this.$body = $body;
		this.createColumns($body);
	}
	
	/**
	 * 创建表头
	 */
	this.createHeaders = function(header)
	{
		var tableObj = this;
		var html = [];
		var columns = this.options.columns;
		if ($.isNullOrEmpty(columns)) {
			return;
		}
		var len = columns.length;
		var column;
		for (var i = 0; i < len; i++) {
			html.push('<tr>')
			column = columns[i];
			if(Array.isArray(column))
			{				
				var l = column.length;
				for(var j = 0; j < l; j ++)
				{
					var col = column[j];
					formatHeaderColumn(html, col);
				}
			}
			else
			{
				formatHeaderColumn(html, column);
			}
			html.push('</tr>')
		}
		$(header).html(html.join(''));		
		if(this.sortable)
		{
			this.addSortEventListeners(header);
		}
		
		//格式化头部栏目
		function formatHeaderColumn(html, column)
		{
			html.push('<th ' + (column.rowspan > 0 ? 'rowspan="' + column.rowspan + '" ' : '') 
					+ (column.colspan > 0 ? 'colspan="' + column.colspan + '" ' : '') 
					+ ($.isNullOrEmpty(column.className) ? '' : 'class="' + column.className + '" ')
					+ 'style="align: center;">');
			
			html.push('<div class="th-wrap">');			
			if ($.isNotNullOrEmpty(column.name)) {
				html.push(column.name);
			}
			else if ($.isNotNullOrEmpty(column.title)) {
				html.push(column.title);
			}
			else{
				html.push(column.field);
			}

			if ((!$.isNullOrEmpty(column.sortable)) && column.sortable) {
				tableObj.sortable = true;
				html.push('<div class="sorting-action" data-field="' + column.field + '">');
				html.push('<i class="sa-icon sa-up iconfont icon-sanjiao2"></i>');
				html .push('<i class="sa-icon sa-down iconfont icon-sanjiao1"></i>')
				html.push('</div>');
			}
			html.push('</div>');
			html.push('</th>');
		}
	}
	
	//添加排序的操作
	this.addSortEventListeners = function(header)
	{
		var tableObj = this;
		$(header).find('.sorting-action').each(function(){
			var flag = $(this);
			$(flag).parent().parent().on('click', function(){
				$(this).siblings().each(function(){
					$(this).find('.sorting-action').removeClass('sorting-down sorting-up');
				});	
				$(this).parent().siblings().each(function(){
					$(this).find('.sorting-action').removeClass('sorting-down sorting-up');
				});
				if($(flag).hasClass('sorting-down'))
				{
					$(flag).removeClass('sorting-down').addClass('sorting-up');
				}
				else if($(flag).hasClass('sorting-up'))
				{
					$(flag).removeClass('sorting-up').addClass('sorting-down');
				}
				else
				{
					$(flag).addClass('sorting-up');
				}

				tableObj.resortDataList();
			});
		})
	}
	
	this.update = function()
	{
		this.resortDataList();
	}
	
	this.resortDataList = function()
	{
		var sorter = this.getSorter();
		this.options.sorter.setSorter(sorter.field, sorter.asc);
		this.formatColumns(this.$body);
		//layer.msg('重新排序, Field: ' + sorter.field + ', Asc: ' + sorter.asc);
	}
	
	/**
	 * 获得排序的对象
	 */
	this.getSorter = function()
	{
		var sorter = {
			field: '',
			asc: false
		};
		$(this.$table).find('thead th .sorting-action').each(function(){
			if($(this).hasClass('sorting-down'))
			{
				sorter.field = $(this).attr('data-field');
				sorter.asc = false;
			}
			else if($(this).hasClass('sorting-up'))
			{
				sorter.field = $(this).attr('data-field');
				sorter.asc = true;
			}
		});
		return sorter;
	}
	
	/**
	 * 创建表格
	 */
	this.createColumns = function(tbody)
	{
		if($.isNullOrEmpty(this.options.rows))
		{
			return;
		}
		else
		{
			this.formatColumns(tbody);
		}		
	}
	
	/**
	 * 从远程获得数据
	 */
	this.getDataFromUrl = function(tbody)
	{
		$(table).html('正在下载数据...')
		$.ajax({
			method: this.options.method,
			url: this.options.url,
			data: this.options.params,
			success: function(json){
				this.options.rows = json.data;
				this.formatColumns(tbody);
			},
			error: function(){
				$(table).html('');
				layer.msg(this.options.url + '出错.');
			}
		});
	}
	
	/**
	 * 格式化表格数据
	 */
	this.formatColumns = function(tbody)
	{
		var columns = this.options.columns;
		if($.isNullOrEmpty(columns))
		{
			return;
		}
		var rows = this.options.rows;
		if($.isNullOrEmpty(rows))
		{
			return;
		}
		if($.isNotNullOrEmpty(this.options.relator))
		{
			this.matchDoc = new MatchDoc(rows);
			this.matchDoc.createMatchRelates(this.options.setting, this.options.relator);
		}				
		var filter = this.options.filter;
		if(!$.isNullOrEmpty(filter))
		{
			rows = this.filter(rows, filter);
		}
		var sorter = this.options.sorter;
		if(!$.isNullOrEmpty(sorter))
		{
			rows = this.sort(rows, sorter);
			
		}
		this.options.results = rows;
		var len = rows.length;
		var html = [];
		
		var cols = this.formlize(columns);
		for(var i = 0; i < len; i ++)
		{
			if(this.options.firstWithLast)
			{
				html.push(this.formatTwoRowValue(cols, rows[i], i));
			}
			else
			{
				html.push(this.formatRowValue(cols, rows[i], i));
			}
		}
		$(tbody).html(html.join(''));
		
		if($.isNotNullOrEmpty(this.options.postshow))
		{
			this.options.postshow();
		}
	}
	
	/**
	 * 格式化两行数据
	 */
	this.formatTwoRowValue = function(formats, row, index)
	{
		var len = formats.length;
		var html = [];
		var row1 = [];
		var row2 = [];
		row1.push('<tr>')
		row2.push('<tr>')
		for(var i = 0; i < len; i ++)
		{
			var format = formats[i];
			
			if(format.type == 'odds')
			{
				row1.push(this.formatColumnValue(format, row, index, true));
				row2.push(this.formatColumnValue(format, row, index, false));
			}
			else
			{
				row1.push(this.formatColumnValue(format, row, index, false, 2));
			}
		}
		row1.push('</tr>');
		row2.push('</tr>')
		
		html.push(row1.join(''));
		html.push(row2.join(''));
		return html.join('');
	}
	
	/**
	 * 格式化行数据
	 */
	this.formatRowValue = function(formats, row, index)
	{
		var len = formats.length;
		var html = [];
		html.push('<tr>');
		for(var i = 0; i < len; i ++)
		{
			var format = formats[i];
			html.push(this.formatColumnValue(format, row, index, i, this.options.first));	
		}
		html.push('</tr>');
		return html.join('');
	}
	
	/**
	 * 格式化字段数据
	 */
	this.formatColumnValue = function(format, row, index, rowIndex, first, rowspan)
	{
		var key = format.field;
		var html = [];
		html.push('<td');
		if(!$.isNullOrEmpty(format['className']))
		{
			html.push(' class="' + format['className'] + '"');
		}
		if(!$.isNullOrEmpty(format['title']))
		{
			html.push(' title="' + format['title'] + '"');
		}
		if($.isNotNullOrEmpty(rowspan) && rowspan > 1)
		{
			html.push(' rowspan="' + rowspan + '"');
		}
		html.push('>')
		if($.isNullOrEmpty(format['formatter']))
		{
			html.push(row[key]);
		}
		else
		{
			html.push(format.formatter(row[key], row, index, format.indexValue, first));
		}
		html.push('</td>')
		return html.join(''); 
	}
	
	/**
	 * 格式化表格头部
	 */
	this.formatHeader = function(header)
	{
		var html = [];
		if(!$.isNullOrEmpty(header.formatter))
		{
			html.push(header.formatter());
		}
		else
		{
			html.push(header.text);
		}
		return html.join('');
	}
	
	//过滤数据
	this.filter = function(rows, filter)
	{
		var results = [];
		var len = rows.length;
		var rec;
		for(var i = 0; i < len; i ++)
		{
			rec = rows[i];
			if(!filter.filter(rec))
			{
				results.push(rec);
			}
		}
		return results;
	}
	
	/**
	 * 数据排序
	 */
	this.sort = function(rows, sorter)
	{
		return rows.sort(function(a, b)
		{
			return sorter.compare(a, b);
		});
	}
	
	//标准化数据
	this.formlize = function(columns)
	{
		if($.isNullOrEmpty(columns))
		{
			return columns;
		}
		if(Array.isArray(columns[0]))
		{
			var cols = [];
			var row0 = columns[0];
			var len = row0.length;
			var starter = 0;
			for(var i = 0; i < len; i ++)
			{
				var r = row0[i];
				if((!$.isNullOrEmpty(r.field)) || (!$.isNullOrEmpty(r.formatter)))
				{
					cols.push(r);
				}
				if(r.colspan > 1)
				{
					starter += fillCols(cols, columns, starter, r.colspan);
				}
			}
			return cols;
		}
		else
		{
			return columns;
		}
		
		//填充列表
		function fillCols(cols, columns, start, num)
		{
			var len = columns.length;
			var fillnum = 0;
			var index = 0;
			for(var i = 1; i < len; i ++)
			{
				var row = columns[i];
				var l = row.length;
				for(var j = 0; j < l; j ++)
				{
					var r = row[j];
					if((!$.isNullOrEmpty(r.field)) || (!$.isNullOrEmpty(r.formatter)))
					{	
						if(index >= start)
						{
							cols.push(r);
							fillnum ++;
							if(fillnum == num)
							{
								return fillnum;
							}
						}						
						index ++;
					}
				}
			}
			return fillnum;
		}
	}	
}

/**
 * 比赛欧赔的关联关系类，表示某场比赛哪一个值与其它比赛关联
 * @param match 公司
 * @param corp 公司
 * @param index 关联值的序列号
 * @returns
 */
function MatchCorpRelation(match, corp, index)
{
	this.mid = match.mid;
	this.match = match;
	this.corp = corp;
	this.index = index;
}

//比赛的关联关系，这里可以表示多场比赛的同步关联关系
// corporate: 博彩公司
function MatchRelation(corporate)
{
	this.corp = corporate;
	this.relations = [];
	
	this.addMatchCorpRelation = function(relation)
	{
		this.relations.push(relation);
	}
	
	//是否同一博彩公司
	this.isSameCorp = function(corporate)
	{
		return (this.corp.gid == corporate.gid 
				&& this.corp.type == corporate.type
				&& this.corp.source == corporate.source);
	}
	
	//检测是否是同一个关联信息
	this.isMatchCorpRelated = function(match, corp, index)
	{
		var len = this.relations.length;
		for(var i = 0; i < len; i ++)
		{
			var r = this.relations[i];
			if(this.isSameCorp(corp) && r.mid == match.mid && r.index == index)
			{
				return true;
			}
		}
		return false;
	}
		
	this.size = function()
	{
		return this.relations.length;
	}
	
	this.exist = function(mid)
	{
		var len = this.relations.length;
		for(var i = 0; i < len; i ++)
		{
			var r = this.relations[i];
			if(r.mid == mid)
			{
				return true;
			}
		}
		return false;
	}
	
	this.existMatch = function(match)
	{
		return this.exist(match.mid);
	}
	
	this.isMatchRelated = function(mid, index)
	{
		var len = this.relations.length;
		for(var i = 0; i < len; i ++)
		{
			var r = this.relations[i];
			if(r.mid == mid && r.index == index)
			{
				return true;
			}
		}
		return false;
	}
}

//定义比赛数据类
function MatchDoc(matchList)
{
	this.list = matchList;
	this.size = matchList.length;
	this.useDrawOdds = false;
	
	this.relates = [];
	
	//获得比赛数据
	this.getMatchData = function(index)
	{
		return this.list[index];
	};
	
	// 按照要求进行比赛数据的排序
	// field: 排序的主字段： ordinary(默认，按照比赛的序号)、league(按照联赛编号)
	// corpid和oddindex: 博彩公司和胜平负的大小
	// asc: 是否按照升序进行排序
	this.getSortList = function(field, asc, corpid, oddindex)
	{
		if($.isNullOrEmpty(field))
		{
			field = 'ordinary';
		}
		if($.isNullOrEmpty(asc))
		{
			asc = true;
		}
		var matches = [];
		matches = matches.concat(this.list);
		
		//数组进行排序处理
		matches.sort(function(a, b){
			var r = 'undefined';
			
			//先需要按照
			if(!$.isNullOrEmpty(corpid) && !$.isNullOrEmpty(oddindex))
			{
				//如果选择了按照联赛排序，则优先使用联赛排序
				if('league' == field)
				{
					r = Number(a.lid) - Number(b.lid);
					if(r != 0)
					{
						return asc ? r : -r;
					}
				}
				var opRec1 = getOpOdds(a, corpid);
				var opRec2 = getOpOdds(b, corpid);
				
				var isNull1 = $.isNullOrEmpty(opRec1) || $.isNullOrEmpty(opRec1.values);
				var isNull2 = $.isNullOrEmpty(opRec2) || $.isNullOrEmpty(opRec2.values);				
				
				//检测是否为空
				if(!isNull1 && !isNull2)
				{
					var val1 = opRec1.values[oddindex];
					var val2 = opRec2.values[oddindex];
					
					r = Number(val1) - Number(val2);
				}
				else if(isNull1 && isNull2)
				{
					r = 0;
				}
				else
				{
					return isNull1 ? 1 : -1;
				}
				
				//
				if(r != 0)
				{
					return asc ? r : -r;
				}
			}
			
			if('league' == field)
			{
				r = Number(a.lid) - Number(b.lid);
				if(r == 0)
				{
					r = Number(a.ordinary) - Number(b.ordinary);
				}
			}
			else
			{
				r = Number(a.ordinary) - Number(b.ordinary);
			}				
			return asc ? r : -r;		
		});
		return matches;
	}
	
	// 检测是否已经存在关系库中
	// match: 比赛数据
	// corp: 博彩公司
	// index: 赔率值
	this.getMatchRelation = function(match, corp, index)
	{
		for(var i = 0, len = this.relates.length; i < len; i ++)
		{
			var r = this.relates[i];
			if(r.isMatchCorpRelated(match, corp, index))
			{
				return r;
			}
		}
		return null;
	}
	
	// 获得某一场比赛的关联关系的序号
	// match: 比赛信息
	// corpid: 博彩公司的编号
	this.getMatchRelationIndex = function(match, corp, first)
	{
		var op = MatchDoc.getOpOdds(match, corp.gid);
		if($.isNullOrEmpty(op))
		{
			return -1;
		}
		
		var index = MatchDoc.getOpMaxProbIndex(op, first);
		return this.getMatchRelationIndex0(match, corp, index);
	}
	
	// 获得某一场比赛的关联关系的序号
	// match: 比赛信息
	// corpid: 博彩公司的编号
	// index: 欧赔率值的序号
	this.getMatchRelationIndex0 = function(match, corp, index)
	{
		for(var i = 0, len = this.relates.length; i < len; i ++)
		{
			var r = this.relates[i];
			if(r.isMatchCorpRelated(match, corp, index))
			{
				return i;
			}
			
			/*if(matchRelation.isSameCorp(corp))
			{
				if(matchRelation.isMatchRelated(match.mid, index))
				{
					return i;
				}
			}*/
		}
		return -1;
	}
	
	this.createMatchRelates = function(setting, relator)
	{
		this.useDrawOdds = relator.useDraw;
		return this.createMatchRelates0(setting, relator.threshold, relator.sameLeague);
	}
	
	// 分析比赛之间的关联关系
	// setting： 设置类，使用哪些博彩公司数据
	// threshold: 分析的阈值范围
	// leagueOnly: 是否只分析联赛内部
	// return: 分析关系是否成功
	this.createMatchRelates0 = function(setting, threshold, sameLeague)
	{
		//清空数组
		this.relates.splice(0, this.relates.length);
		
		var corps = setting.getOpCorps();
		if($.isNullOrEmpty(corps))
		{
			return false;
		}
		
		var len = corps.length;
		for(var i = 0; i < len; i ++)
		{
			var corp = corps[i];
			if(corp.gid == '-2')
			{
				continue;
			}
			
			this.createMaxCorpRelates(corp, threshold, sameLeague);
		}
		return true;
	};
	
	// 对某一博彩公司的值分析比赛之间赔率的关联关系
	// corp: 博彩公司
	// threshold: 阈值
	// sameLeague: 是否同一联赛
	this.createMaxCorpRelates = function(corp, threshold, sameLeague)
	{
		var len = this.list.length;
		for(var i = 0; i < len - 1; i ++)
		{
			var m1 = this.list[i];
			for(var j = i + 1; j < len; j ++)
			{
				var m2 = this.list[j];
				
				//检测是否属于同联赛判断
				if(sameLeague && m1.lid != m2.lid)
				{
					continue;
				}
				
				this.checkRelation(m1, m2, corp, threshold, sameLeague, true);
				this.checkRelation(m1, m2, corp, threshold, sameLeague, false);
			}
		}
	}
	
	//检测两场比赛是否关联
	//如果关联，则建立关联关系
	//match1: 第1场比赛
	//match2: 第2场比赛
	//corp: 博彩公司
	//threshold: 阈值
	//sameLeague: 是否判断同一联赛
	this.checkRelation = function(match1, match2, corp, threshold, sameLeague, first)
	{
		//检测是否属于同联赛判断
		if(sameLeague && match1.lid != match2.lid)
		{
			return;
		}		
		var op1 = MatchDoc.getOpOdds(match1, corp.gid);
		var op2 = MatchDoc.getOpOdds(match2, corp.gid);
		
		//如果没有值，则不进行检测
		if($.isNullOrEmpty(op1) || $.isNullOrEmpty(op2))
		{
			return;
		}
		
		var v1 = MatchDoc.getOpMaxProb(op1, first);
		var v2 = MatchDoc.getOpMaxProb(op2, first);
		
		//如果两个值是关联的
		if(compare(v1, v2, threshold))
		{
			var index1 = MatchDoc.getOpMaxProbIndex(op1, first);
			var index2 = MatchDoc.getOpMaxProbIndex(op2, first);
			
			var relation = this.getMatchRelation(match1, corp, index1);
			if(!$.isNullOrEmpty(relation))
			{
				if(!relation.isMatchRelated(match2, index2))
				{
					var mr = new MatchCorpRelation(match2, corp, index2);
					relation.addMatchCorpRelation(mr);
				}
			}
			else
			{
				relation = this.getMatchRelation(match2, corp, index2);
				if($.isNullOrEmpty(relation))
				{
					relation = new MatchRelation(corp);
					relation.addMatchCorpRelation(new MatchCorpRelation(match2, corp, index2));
					this.relates.push(relation);
				}
				relation.addMatchCorpRelation(new MatchCorpRelation(match1, corp, index1));
			}
		}		
	}
	
	//比赛两个值的差值与阈值的大小关系
	function compare(value1, value2, threshold)
	{
		var v1 = Number(value1);
		var v2 = Number(value2);
		
		return Math.abs(v1 - v2) <= threshold;
	}
}


//获得概率值最大的值，
//op: 欧赔值
//first: 是否检测初值
MatchDoc.getOpMaxProb = function(op, first)
{
	var st = first ? 0 : 3;
	return op.values[st] < op.values[st + 2] ? op.values[st] : op.values[st + 2];
}
//获得概率值最大的值，
//op: 欧赔值
//first: 是否检测初值
MatchDoc.getOpMaxProbIndex = function(op, first)
{
	var st = first ? 0 : 3;
	return op.values[st] < op.values[st + 2] ? st : (st + 2);
}
//获得欧赔数据记录
MatchDoc.getOpOdds = function(match, gid)
{
	var opnum = match.opItems.length;
	for(var i = 0; i < opnum; i ++)
	{
		var op = match.opItems[i];
		if(op.gid == gid)
		{
			return op;
		}
	}	
	return null;
}

//获得亚盘数据记录
MatchDoc.getYpOdds = function(match, gid)
{
	var ypnum = match.ypItems.length;
	for(var i = 0; i < ypnum; i ++)
	{
		var yp = match.ypItems[i];
		if(yp.gid == gid)
		{
			return yp;
		}
	}
	return null;
}

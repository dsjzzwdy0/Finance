/**
 * 比赛数据关联分析应用
 */

//数据格式化
function formatValue(value)
{
	return value.toFixed(2);
}

//格式化欧赔数据栏目
function formatOpValueColumn(doc, match, op, corp, first)
{
	var st = first ? 0 : 3;
	var idx = getOpMaxProbIndex(op, first);
	//var r = doc.getMatchRelation(match, corp, first);
	var relateIndex = doc.getMatchRelationIndex(match, corp, first);
	var relateClass = relateIndex >= 0 ? getRelationClass(relateIndex) : '';
	
	var text = [];
	var vals = op.values;
	for(var k = st; k < st + 3; k ++)
	{
		text.push('<td align="center"><div class="association ' + ((idx == k) ? relateClass : '')
			+ '">' + formatValue(vals[k]) + '</div></td>');
	}
	return text.join('');
}

//格式化亚盘数据
function formatYpValueColumn(match, yp, corp, first)
{
	var st = first ? 0 : 3;
	var text = [];
	var vals = yp.values;
	text.push('<td><div class="oddsvalue">' + formatValue(vals[st]) + '</div></td>');
	text.push('<td><div class="handicap">' + getHandicapName(vals[st + 1]) + '</div></td>');
	text.push('<td><div class="oddsvalue">' + formatValue(vals[st + 2]) + '</div></td>');
	return text.join('');
}

//比赛两个值的差值与阈值的大小关系
function compare(value1, value2, threshold)
{
	var v1 = Number(value1);
	var v2 = Number(value2);
	
	return Math.abs(v1 - v2) <= threshold;
}

//获得概率值最大的值，
//op: 欧赔值
//first: 是否检测初值
function getOpMaxProb(op, first)
{
	var st = first ? 0 : 3;
	return op.values[st] < op.values[st + 2] ? op.values[st] : op.values[st + 2];
}

//获得概率值最大的值，
//op: 欧赔值
//first: 是否检测初值
function getOpMaxProbIndex(op, first)
{
	var st = first ? 0 : 3;
	return op.values[st] < op.values[st + 2] ? st : (st + 2);
}

//获得欧赔数据记录
function getOpOdds(match, gid)
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
function getYpOdds(match, gid)
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

//检测两场比赛的某一赔率值是否相关
// match： 当前的比赛
// value: 数据值
// index: 数据的序号（胜为0、平为1、负为2）
// corpid: 博彩公司编号
// threshold: 阈值
// sameLeague: 是否同一联赛
function isRelated(match1, match2, corpid, index, threshold, sameLeague)
{
	if(sameLeague && (match1.lid != match2.lid))
	{
		return false;
	}
	
	var opRec1 = getOpOdds(match1, corpid);
	var opRec2 = getOpOdds(match2, corpid);
	
	if($.isNullOrEmpty(opRec1) || $.isNullOrEmpty(opRec1.values) ||
		$.isNullOrEmpty(opRec2) || $.isNullOrEmpty(opRec2.values))
	{
		return false;
	}
	return compare(opRec1.values[index], opRec2.values[index], threshold);
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
			
			/*if(matchRelation.isSameCorp(corp))
			{
				if(matchRelation.exist(match.mid))
				{
					return matchRelation;
				}
			}*/
		}
		return null;
	}
	
	// 获得某一场比赛的关联关系的序号
	// match: 比赛信息
	// corpid: 博彩公司的编号
	this.getMatchRelationIndex = function(match, corp, first)
	{
		var op = getOpOdds(match, corp.gid);
		if($.isNullOrEmpty(op))
		{
			return -1;
		}
		
		var index = getOpMaxProbIndex(op, first);
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
	
	// 分析比赛之间的关联关系
	// setting： 设置类，使用哪些博彩公司数据
	// threshold: 分析的阈值范围
	// leagueOnly: 是否只分析联赛内部
	// return: 分析关系是否成功
	this.createMatchRelates = function(setting, threshold, sameLeague)
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
		var op1 = getOpOdds(match1, corp.gid);
		var op2 = getOpOdds(match2, corp.gid);
		
		//如果没有值，则不进行检测
		if($.isNullOrEmpty(op1) || $.isNullOrEmpty(op2))
		{
			return;
		}
		
		var v1 = getOpMaxProb(op1, first);
		var v2 = getOpMaxProb(op2, first);
		
		//如果两个值是关联的
		if(compare(v1, v2, threshold))
		{
			var index1 = getOpMaxProbIndex(op1, first);
			var index2 = getOpMaxProbIndex(op2, first);
			
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
}


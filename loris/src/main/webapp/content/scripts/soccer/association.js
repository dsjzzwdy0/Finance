//比赛两个值的差值与阈值的大小关系
function compare(value1, value2, threshold)
{
	var v1 = Number(value1);
	var v2 = Number(value2);
	
	return Math.abs(v1 - v2) <= threshold;
}

//获得欧赔数据记录
function getOpOdds(match, gid)
{
	var opnum = match.opItems.items.length;
	for(var i = 0; i < opnum; i ++)
	{
		var op = match.opItems.items[i];
		if(op.gid == gid)
		{
			return op;
		}
	}	
	return null;
}

//获得亚盘数据记录
function getYpOdds(rec, gid)
{
	var ypnum = rec.ypItems.items.length;
	for(var i = 0; i < ypnum; i ++)
	{
		var yp = rec.ypItems.items[i];
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

//比赛的关系类
// corporate: 博彩公司
// index: 是指关联的序号（胜为0、平为1、负为2）
function MatchRelation(corporate, index)
{
	this.corp = corporate;
	this.index = index;	
	this.matches = [];
	
	this.addMatch = function(match)
	{
		this.matches.push(match);
	}
	
	this.isSame = function(corporate, index)
	{
		return (this.index == index 
				&& this.corp.gid == corporate.gid 
				&& this.corp.type == corporate.type
				&& this.corp.source == corporate.source);
	}
	
	this.size = function()
	{
		return this.matches.length;
	}
	
	this.exist = function(mid)
	{
		for(var i = 0, len = this.matches.length; i < len; i ++)
		{
			var m = this.matches[i];
			if(m.mid == mid)
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
	
	// 按照欧赔公司的赔率获得相关联的比赛
	// match： 比赛数据
	// corpid: 博彩公司公司编号
	// threshold: 阈值
	// sameLeague: 是否只计算同一联赛
	this.getRelateMatchData = function(match, corpid, threshold, sameLeague)
	{
		var matchs = [];		
		var opRec1 = getOpOdds(match, corpid);
		
		for(var i = 0; i < this.size; i ++)
		{
			var m = this.list[i];
			if(m.mid == match.mid)
			{
				continue;
			}
			
			//判断是否同一联赛
			if(sameLeague && !(m.lid == match.lid))
			{
				continue;
			}
			
			var opRec2 = getOpOdds(m, corpid);
			if($.isNullOrEmpty(opRec2))
			{
				continue;
			}
			
			var r = this.getRelation(opRec1, opRec2, threshold);
			if(r != -1)
			{
				matchs.push(m);
			}
		}
		return matchs;
	}
	
	// 检测是否有相关的比赛
	// match： 当前的比赛
	// value: 数据值
	// index: 数据的序号（胜为0、平为1、负为2）
	// corpid: 博彩公司编号
	// threshold: 阈值
	// sameLeague: 是否同一联赛
	this.hasRelation = function(match, value, index, corpid, threshold, sameLeague)
	{
		for(var i = 0; i < this.size; i ++)
		{
			var m = this.list[i];
			if(m.mid == match.mid)
			{
				continue;
			}
			
			//判断是否同一联赛
			if(sameLeague && !(m.lid == match.lid))
			{
				continue;
			}
			
			var opRec2 = getOpOdds(m, corpid);
			if($.isNullOrEmpty(opRec2) || $.isNullOrEmpty(opRec2.values))
			{
				continue;
			}
			
			if(compare(value, opRec2.values[index], threshold))
			{
				return true;
			}
		}
		return false;
	}
	
	// 判断两个欧赔赔率是否相关
	// oddsRec1第一个欧赔
	// oddsRec2第二个欧赔
	// threshold: 阈值
	// return: -1，不相关；0胜赔相关；1平赔相关；2负赔相关
	this.getRelation = function(oddsRec1, oddsRec2, threshold)
	{
		if($.isNullOrEmpty(oddsRec1) || $.isNullOrEmpty(oddsRec2))
		{
			return -1;
		}
		
		var odds1 = oddsRec1.values;
		var odds2 = oddsRec2.values;
		
		if(compare(odds1[0], odds2[0], threshold))
		{
			return 0;
		}
		else if(compare(odds1[2], odds2[2], threshold))
		{
			return 2;
		}
		else if(compare(odds1[1],  odds2[1], threshold))
		{
			return 1;
		}
		return -1;
	}
	
	// 检测是否已经存在关系库中
	// match: 比赛数据
	// corp: 博彩公司
	// index: 赔率值
	this.getMatchRelation = function(match, corp, index)
	{
		for(var i = 0, len = this.relates.length; i < len; i ++)
		{
			var matchRelation = this.relates[i];
			if(matchRelation.isSame(corp, index))
			{
				if(matchRelation.exist(match.mid))
				{
					return matchRelation;
				}
			}
		}
		return null;
	}
	
	// 获得某一场比赛的关联关系的序号
	// match: 比赛信息
	// corpid: 博彩公司的编号
	// index: 欧赔率值的序号
	this.getMatchRelationIndex = function(match, corp, index)
	{
		for(var i = 0, len = this.relates.length; i < len; i ++)
		{
			var matchRelation = this.relates[i];
			if(matchRelation.isSame(corp, index))
			{
				if(matchRelation.exist(match.mid))
				{
					return i;
				}
			}
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
			if(corp.gid == '0')
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
		for(var k = 0; k < 3; k ++)
		{
			if(k == 1 && !this.useDrawOdds)
			{
				continue;
			}
			var indexMatches = this.getSortList('ordinary', true, corp.gid, k);
			var len = indexMatches.length;
			
			for(var i = 0; i < len - 1; i ++)
			{	
				while(true)
				{
					var m1 = indexMatches[i];
					var m2 = indexMatches[i + 1];
					
					if(isRelated(m1, m2, corp.gid, k, threshold, sameLeague))
					{
						var r = this.getMatchRelation(m1, corp, k);
						if($.isNullOrEmpty(r))
						{
							r = new MatchRelation(corp, k);
							this.relates.push(r);
							r.addMatch(m1);
							r.addMatch(m2);
						}
						else
						{
							r.addMatch(m2);
						}
						i ++;
					}
					else
					{
						break;
					}
				}
			}
		}
	}
	
	// 对某一博彩公司的值分析比赛之间赔率的关联关系
	// corp: 博彩公司
	// threshold: 阈值
	// sameLeague: 是否同一联赛
	this.createCorpRelates = function(corp, threshold, sameLeague)
	{
		for(var i = 0, len = this.list.length; i < len; i ++)
		{
			var m1 = this.list[i];
			for(var j = i + 1; j < len; j ++)
			{
				var m2 = this.list[j];
				
				for(var k = 0; k < 3; k ++)
				{
					//是否对平赔数据进行关联计算
					if(k == 1 && !this.useDrawOdds)
					{
						continue;
					}
					
					//检测是否存在关联关系
					if(isRelated(m1, m2, corp.gid, k, threshold, sameLeague))
					{
						var r1 = this.getMatchRelation(m1, corp, k);
						var r2 = this.getMatchRelation(m2, corp, k);
						
						if($.isNullOrEmpty(r1))
						{
							var r = new MatchRelation(corp, k);
							r.addMatch(m1);
							r.addMatch(m2);
							this.relates.push(r);
						}
						else
						{
							if($.isNullOrEmpty(r2) || (r1 != r2))
							{
								r1.addMatch(m2);
							}
						}
					}
				}				
			}
		}
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
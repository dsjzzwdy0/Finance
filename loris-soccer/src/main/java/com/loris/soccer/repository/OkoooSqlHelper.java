package com.loris.soccer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.loris.base.util.ArraysUtil;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.bean.okooo.OkoooJcMatch;
import com.loris.soccer.bean.okooo.OkoooMatch;
import com.loris.soccer.bean.okooo.OkoooMatchInfo;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.bean.table.Yp;
import com.loris.soccer.repository.service.OkoooBdMatchService;
import com.loris.soccer.repository.service.OkoooJcMatchService;
import com.loris.soccer.repository.service.OkoooMatchInfoService;
import com.loris.soccer.repository.service.OkoooMatchService;
import com.loris.soccer.repository.service.OkoooOpService;
import com.loris.soccer.repository.service.OkoooYpService;

@Component
public class OkoooSqlHelper
{
	@Autowired
	private OkoooJcMatchService okoooJcMatchService;
	
	@Autowired
	private OkoooBdMatchService okoooBdMatchService;
	
	@Autowired
	private OkoooOpService okoooOpService;
	
	@Autowired
	private OkoooYpService okoooYpService;
	
	@Autowired
	private OkoooMatchService okoooMatchService;
	
	@Autowired
	private OkoooMatchInfoService okoooMatchInfoService;
	
	
	private static OkoooSqlHelper singleton;
	
	private OkoooSqlHelper()
	{
		singleton = this;
	}
	
	/**
	 * Create the OkoooSqlHelper.
	 * @return
	 */
	public static OkoooSqlHelper getInstance()
	{
		if(singleton == null)
		{
			singleton = new OkoooSqlHelper();
		}
		return singleton;
	}
	
	/**
	 * 获得比赛的欧赔数据
	 * @param mid
	 * @param gids
	 * @return
	 */
	public List<OkoooOp> getOddsOp(String mid, List<String> gids, boolean hasFirst)
	{
		EntityWrapper<OkoooOp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.and().in("gid", gids);
		ew.and().isNotNull("firsttime");
		return okoooOpService.selectList(ew);
	}
	
	/**
	 * 查询欧赔数据集
	 * 
	 * @param mids 比赛编号
	 * @param gids 欧赔公司编号
	 * @return 欧赔数据列表
	 */
	public List<OkoooOp> getOddsOp(List<String> mids, List<String> gids, boolean onlyFirstOdds)
	{
		EntityWrapper<OkoooOp> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		if(mids != null && mids.size() > 0)
		{
			ew.andNew().in("mid", mids);
		}		
		if(gids != null && gids.size() > 0)
		{
			ew.andNew().in("gid", gids);
		}
		if(onlyFirstOdds)
		{
			ew.andNew().gt("firsttime", "0");
		}
		return okoooOpService.selectList(ew);
	}
	
	/**
	 * 查询亚盘数据集
	 * 
	 * @param mids 比赛编号
	 * @param gids 亚盘公司编号
	 * @return 亚盘数据列表
	 */
	public List<OkoooYp> getOddsYp(List<String> mids, List<String> gids, boolean onlyFirstOdds)
	{
		EntityWrapper<OkoooYp> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		ew.andNew().in("mid", mids);
		
		if(gids.size() > 0)
		{
			ew.andNew().in("gid", gids);
		}
		if(onlyFirstOdds)
		{
			ew.andNew().gt("firsttime", "0");
		}
		
		/*
		for (String mid : mids)
		{
			ew.or().eq("mid", mid);
		}*/
		
		/*
		if(gids.size() > 0)
		{
			ew.andNew();
			boolean first = true;
			for (String gid : gids)
			{
				if(first)
				{					
					first = false;
				}
				else
				{
					ew.or();
				}
				ew.eq("gid", gid);
			}
		}
		
		if(onlyFirstOdds)
		{
			ew.andNew().isNotNull("firsttime");
		}*/
		//System.out.println(ew.toString());
		return okoooYpService.selectList(ew);
	}
	
	/**
	 * 查询北单数据
	 * @param issue
	 * @return
	 */
	public List<OkoooBdMatch> getBdMatches(String issue)
	{
		EntityWrapper<OkoooBdMatch> ew = new EntityWrapper<>();
		ew.eq("issue", issue);
		return okoooBdMatchService.selectList(ew);
	}
	
	public List<OkoooJcMatch> getJcMatches(String issue)
	{
		EntityWrapper<OkoooJcMatch> ew = new EntityWrapper<>();
		ew.eq("issue", issue);
		return okoooJcMatchService.selectList(ew);
	}
	
	/**
	 * 保存竞彩数据
	 * @param matches
	 * @return
	 */
	public boolean addOkoooJcMatches(List<OkoooJcMatch> matches)
	{
		EntityWrapper<OkoooJcMatch> ew = new EntityWrapper<>();
		List<String> mids = ArraysUtil.getObjectFieldValue(matches, OkoooJcMatch.class, "mid");
		
		ew.in("mid", mids);
		List<OkoooJcMatch> list = okoooJcMatchService.selectList(ew);
		
		List<OkoooJcMatch> saveMatchs = new ArrayList<>();
		for (OkoooJcMatch okoooJcMatch : matches)
		{
			boolean exist = false;
			for (OkoooJcMatch okoooJcMatch2 : list)
			{
				if(okoooJcMatch2.getMid().equals(okoooJcMatch.getMid()))
				{
					exist = true;
				}
			}
			if(!exist)
			{
				saveMatchs.add(okoooJcMatch);
			}
		}
		
		if(saveMatchs.size() > 0)
		{
			return okoooJcMatchService.insertBatch(saveMatchs);
		}
		
		return true;
	}
	
	/**
	 * 存储北单数据
	 * @param matches
	 * @return
	 */
	public boolean addOkoooBdMatches(List<OkoooBdMatch> matches)
	{
		EntityWrapper<OkoooBdMatch> ew = new EntityWrapper<>();
		List<String> mids = ArraysUtil.getObjectFieldValue(matches, OkoooBdMatch.class, "mid");
		
		ew.in("mid", mids);
		List<OkoooBdMatch> list = okoooBdMatchService.selectList(ew);
		
		List<OkoooBdMatch> saveMatchs = new ArrayList<>();
		for (OkoooBdMatch okoooJcMatch : matches)
		{
			boolean exist = false;
			for (OkoooBdMatch okoooJcMatch2 : list)
			{
				if(okoooJcMatch2.getMid().equals(okoooJcMatch.getMid()))
				{
					exist = true;
				}
			}
			if(!exist)
			{
				saveMatchs.add(okoooJcMatch);
			}
		}
		
		if(saveMatchs.size() > 0)
		{
			return okoooBdMatchService.insertBatch(saveMatchs);
		}
		
		return true;
	}
	
	/**
	 * 添加新的欧赔数据到数据库
	 * @param mid 比赛编号
	 * @param ops 亚盘数据列表
	 * @return 添加是否成功的标志
	 */
	public boolean addNewOkoooOpList(String mid, List<OkoooOp> ops)
	{
		List<OkoooOp> newOps = new ArrayList<>();
		List<OkoooOp> existOps = getOkoooOpList(mid);
		for (OkoooOp op : ops)
		{
			if(!existOkoooOpValue(existOps, op))
			{
				newOps.add(op);
			}
		}
		
		
		if(newOps.size() > 0)
		{
			return okoooOpService.insertBatch(newOps);
		}
		return true;
	}
	
	/**
	 * 获取澳客网亚盘数据
	 * @param mid 比赛编号
	 * @return 亚盘数据列表
	 */
	public List<OkoooOp> getOkoooOpList(String mid)
	{
		EntityWrapper<OkoooOp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.orderBy("ordinary+0", true);
		return okoooOpService.selectList(ew);
	}
	
	/**
	 * 查出现有欧赔数据中是否已经存在该赔率
	 * 
	 * @param existOps 数据库中的欧赔数据列表
	 * @param op 待比较的欧赔数据
	 * @return 是否相等的标志
	 */
	public boolean existOkoooOpValue(List<OkoooOp> existYps, OkoooOp yp)
	{
		for (OkoooOp existYp : existYps)
		{
			if(yp.equals(existYp))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 添加亚盘数据列表
	 * @param yps 亚盘数据
	 * @return 添加是否成功的标志
	 */
	public boolean addOkoooOpList(List<OkoooOp> yps)
	{
		if(yps == null || yps.size() == 0)
		{
			return false;
		}
		return okoooOpService.insertBatch(yps);
	}
	
	/**
	 * 添加新的亚盘数据到数据库
	 * @param mid 比赛编号
	 * @param yps 亚盘数据列表
	 * @return 添加是否成功的标志
	 */
	public boolean addNewOkoooYpList(String mid, List<OkoooYp> yps)
	{
		List<OkoooYp> newYps = new ArrayList<>();
		List<OkoooYp> existYps = getOkoooYpList(mid);
		for (OkoooYp yp : yps)
		{
			if(!existOkoooYpValue(existYps, yp))
			{
				newYps.add(yp);
			}
		}		
		if(newYps.size() > 0)
		{
			return okoooYpService.insertBatch(newYps);
		}
		return true;
	}
	
	/**
	 * 获取澳客网亚盘数据
	 * @param mid 比赛编号
	 * @return 亚盘数据列表
	 */
	public List<OkoooYp> getOkoooYpList(String mid)
	{
		EntityWrapper<OkoooYp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.orderBy("ordinary+0", true);
		return okoooYpService.selectList(ew);
	}
	
	/**
	 * 查出现有欧赔数据中是否已经存在该赔率
	 * 
	 * @param existOps 数据库中的欧赔数据列表
	 * @param op 待比较的欧赔数据
	 * @return 是否相等的标志
	 */
	public boolean existOkoooYpValue(List<OkoooYp> existYps, Yp yp)
	{
		for (Yp existYp : existYps)
		{
			if(yp.equals(existYp))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 添加亚盘数据列表
	 * @param yps 亚盘数据
	 * @return 添加是否成功的标志
	 */
	public boolean addOkoooYpList(List<OkoooYp> yps)
	{
		if(yps == null || yps.size() == 0)
		{
			return false;
		}
		return okoooYpService.insertBatch(yps);
	}
	
	/**
	 * 
	 * @param matchs
	 * @return
	 */
	public boolean addOkoooMatch(List<OkoooMatch> matchs)
	{
		EntityWrapper<OkoooMatch> ew = new EntityWrapper<>();
		List<String> mids = ArraysUtil.getObjectFieldValue(matchs, OkoooMatch.class, "mid");
		ew.in("mid", mids);
		okoooMatchService.delete(ew);
		return okoooMatchService.insertBatch(matchs);
	}
	
	/**
	 * 
	 * @param mid
	 * @return
	 */
	public OkoooMatch getOkoooMatch(String mid)
	{
		EntityWrapper<OkoooMatch> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		return okoooMatchService.selectOne(ew);
	}
	
	public List<OkoooMatch> getOkoooMatchs(String lid, String season, String round)
	{
		EntityWrapper<OkoooMatch> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		ew.eq("round", round);
		
		return okoooMatchService.selectList(ew);
	}
	
	public List<OkoooMatchInfo> getOkoooMatchInfos(String lid, String season, String round)
	{
		EntityWrapper<OkoooMatchInfo> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		ew.eq("round", round);
		
		return okoooMatchInfoService.selectList(ew);
	}
	
	/**
	 * 
	 * @param mid
	 * @return
	 */
	public OkoooMatchInfo getOkoooMatchInfo(String mid)
	{
		EntityWrapper<OkoooMatchInfo> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		return okoooMatchInfoService.selectOne(ew);
	}
	
	/**
	 * 获得北单数据
	 * @param mids
	 * @return
	 */
	public List<OkoooBdMatch> getOkoooBdMatches(List<String> mids)
	{
		EntityWrapper<OkoooBdMatch> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return okoooBdMatchService.selectList(ew);
	}
}

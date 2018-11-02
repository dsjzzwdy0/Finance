package com.loris.soccer.bean;

public interface SoccerConstants
{
	/** 赛事类型是杯赛 */
	String MATCH_TYPE_LEAGUE = "league";
	
	/** 赛事类型是杯赛 */
	String MATCH_TYPE_CUP = "cup";
	
	/** 数据亚盘 */
	String ODDS_TYPE_YP = "yp";
	
	/** 欧赔数据*/
	String ODDS_TYPE_OP = "op";
	
	/** 数据来源：中国足彩网 */
	String DATA_SOURCE_ZGZCW = "zgzcw";
	
	/** 数据来源：澳客足球网 */
	String DATA_SOURCE_OKOOO = "okooo";
	
	/** 北京单场*/
	String LOTTERY_BD = "bd";
	
	/** 竞彩足球*/
	String LOTTERY_JC = "jc";
	
	/** 足彩*/
	String LOTTERY_ZC = "zc";
	
	/** 球队排名的信息 */
	String RANK_TOTAL = "total";
	String RANK_HOME= "home";
	String RANK_CLIENT = "guest";
	String RANK_LATEST_6 = "r6";
	String RANK_FIRST_HALF = "firsthalf";
	String RANK_SECOND_HALF = "secondhalf";
	
	//赔率类型
	public static String[] ODDS_KEY_NAMES = {"firstwin", "firstdraw",
			"firstlose", "win", "draw", "lose"};
	
}

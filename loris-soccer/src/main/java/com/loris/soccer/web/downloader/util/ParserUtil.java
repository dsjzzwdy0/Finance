package com.loris.soccer.web.downloader.util;

public class ParserUtil
{
	static String[] chars = {"↓", "↑", "→"};
	
	/**
	 * 标准化亚盘让球数据
	 * @param handicap
	 * @return
	 */
	public static String formatHandicap(String handicap)
	{
		for (String c : chars)
		{
			handicap = handicap.replace(c, "");
		}
		return handicap;
	}
}

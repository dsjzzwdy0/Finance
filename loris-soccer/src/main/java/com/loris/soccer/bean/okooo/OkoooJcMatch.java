package com.loris.soccer.bean.okooo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.table.JcMatch;

@TableName("soccer_okooo_lottery_jc")
public class OkoooJcMatch extends JcMatch
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public OkoooJcMatch()
	{
		this.source = SoccerConstants.DATA_SOURCE_OKOOO;
	}
}

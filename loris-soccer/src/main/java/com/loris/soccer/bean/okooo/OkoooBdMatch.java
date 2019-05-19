package com.loris.soccer.bean.okooo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.table.BdMatch;

@TableName("soccer_okooo_lottery_bd")
public class OkoooBdMatch extends BdMatch
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public OkoooBdMatch()
	{
		this.source = SoccerConstants.DATA_SOURCE_OKOOO;
	}

}

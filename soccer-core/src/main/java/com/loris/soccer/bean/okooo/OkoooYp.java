package com.loris.soccer.bean.okooo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.odds.Yp;

@TableName("soccer_okooo_odds_yp")
public class OkoooYp extends Yp
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public OkoooYp()
	{
		this.source = SoccerConstants.DATA_SOURCE_OKOOO;
	}

}

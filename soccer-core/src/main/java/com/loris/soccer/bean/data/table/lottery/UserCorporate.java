package com.loris.soccer.bean.data.table.lottery;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_lottery_corporate_user")
public class UserCorporate extends Corporate
{
	/***/
	private static final long serialVersionUID = 1L;

	private String userid;

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}
}

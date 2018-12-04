package com.loris.soccer.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("league_country_logo")
public class CountryLogo extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;

	private String country;
	private String logo;
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public String getLogo()
	{
		return logo;
	}
	public void setLogo(String logo)
	{
		this.logo = logo;
	}
	@Override
	public String toString()
	{
		return "CountryLogo [country=" + country + ", logo=" + logo + "]";
	}
}

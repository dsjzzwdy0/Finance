package com.loris.soccer.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("soccer_team")
public class Team extends AutoIdEntity
{
	/**Serial version uid. */
	private static final long serialVersionUID = 8335565979681656086L;

	private String tid;             	//球队编号
	private String name;            	//球队名称
	private String ename;           	//球队英文名称
	private String country;         	//所属国家
	private String city;            	//所在城市
	private String foundtime;       	//成立时间
	private String introduction;    	//球队介绍
	private String mainpage;        	//球队官网
	private String coach;           	//主教练
	private String logo; 				//球队的LOGO位置
	
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}	
	public String getLogo()
	{
		return logo;
	}
	public void setLogo(String logo)
	{
		this.logo = logo;
	}
	public String getEname()
	{
		return ename;
	}
	public void setEname(String ename)
	{
		this.ename = ename;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getFoundtime()
	{
		return foundtime;
	}
	public void setFoundtime(String foundtime)
	{
		this.foundtime = foundtime;
	}
	public String getIntroduction()
	{
		return introduction;
	}
	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}
	public String getMainpage()
	{
		return mainpage;
	}
	public void setMainpage(String mainpage)
	{
		this.mainpage = mainpage;
	}
	public String getCoach()
	{
		return coach;
	}
	public void setCoach(String coach)
	{
		this.coach = coach;
	}
	@Override
	public String toString()
	{
		return "Team [tid=" + tid + ", name=" + name + ", ename=" + ename + ", country=" + country + ", city=" + city
				+ ", foundtime=" + foundtime + ", introduction=" + introduction + ", mainpage=" + mainpage + ", coach="
				+ coach + ", logo=" + logo + "]";
	}
}

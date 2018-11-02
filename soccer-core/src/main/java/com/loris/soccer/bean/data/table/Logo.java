package com.loris.soccer.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("soccer_logo")
public class Logo extends AutoIdEntity
{
	/** */
	private static final long serialVersionUID = 1L;
	
	public static String LOGO_TYPE_TEAM = "team";
	public static String LOGO_TYPE_LEAGUE = "league";
	
	
	private String tid;
	private String type;
	private byte[] images;
	private String mediatype;
	private boolean exist;
	
	public Logo()
	{
		type = LOGO_TYPE_TEAM;
	}
	
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
	public byte[] getImages()
	{
		return images;
	}
	public void setImages(byte[] images)
	{
		this.images = images;
	}
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public boolean isExist()
	{
		return exist;
	}
	public void setExist(boolean exist)
	{
		this.exist = exist;
	}

	public String getMediatype()
	{
		return mediatype;
	}

	public void setMediatype(String mediatype)
	{
		this.mediatype = mediatype;
	}

	@Override
	public String toString()
	{
		return "Logo [tid=" + tid + ", type=" + type + ", exist=" + exist + "]";
	}
}

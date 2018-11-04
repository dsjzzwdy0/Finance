package com.loris.base.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.UUIDEntity;

@TableName("sys_user")
public class User extends UUIDEntity
{
	/***/
	private static final long serialVersionUID = 1L;

	private String realname;
	private String username;
	private String password;
	private String email;
	private String usertype;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getRealname()
	{
		return realname;
	}
	public void setRealname(String realname)
	{
		this.realname = realname;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getUsertype()
	{
		return usertype;
	}
	public void setUsertype(String usertype)
	{
		this.usertype = usertype;
	}
	@Override
	public String toString()
	{
		return "User [id=" + id + ", realname=" + realname + ", username=" + username + ", password=" + password
				+ ", email=" + email + "]";
	}
}

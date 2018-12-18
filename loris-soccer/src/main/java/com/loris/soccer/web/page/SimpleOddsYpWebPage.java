package com.loris.soccer.web.page;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_web_yp_content")
public class SimpleOddsYpWebPage extends SimpleOddsWebPage
{
	/**	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "SimpleOddsYpWebPage [mid=" + mid + ", loadtime=" + loadtime + ", completed=" + completed + ", id=" + id
				+ "]";
	}	
}

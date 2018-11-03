package com.loris.soccer.web.page;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_web_op_content")
public class SimpleOddsOpWebPage extends SimpleOddsWebPage
{
	/**	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString()
	{
		return "SimpleOddsOpWebPage [mid=" + mid + ", loadtime=" + loadtime + ", completed=" + completed + "]";
	}	
}

package com.loris.soccer.analysis.checker;

import com.loris.base.util.ArraysUtil.EqualChecker;
import com.loris.soccer.bean.item.IssueMatch;

public class IssueMatchChecker<T extends IssueMatch> implements EqualChecker<T>
{
	String issue;
	boolean isSame = true;
	
	public IssueMatchChecker(String isssue)
	{
		this(isssue, false);
	}
	
	public IssueMatchChecker(String issue, boolean same)
	{
		this.issue = issue;
		this.isSame = same;
	}
	
	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	public void setSame(boolean isSame)
	{
		this.isSame = isSame;
	}

	@Override
	public boolean isSameObject(T match)
	{
		String t = match.getDate();
		if(isSame)
		{
			return issue.compareTo(t) == 0;
		}
		return issue.compareTo(t)<= 0;
	}
}

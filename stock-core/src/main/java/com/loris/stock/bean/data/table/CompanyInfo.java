package com.loris.stock.bean.data.table;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 上市公司基本信息
 * 
 * @author dsj
 *
 */
@TableName("stock_company_info")
public class CompanyInfo extends StockInfo
{
	private static final long serialVersionUID = 1L;

	private String cname;
	private String ename;
	private String foundtime;
	private String exchange;
	private String industry;
	private double capital;
	private String telphone;
	private String fax;
	private String email;
	private String representative;
	private String secretary;
	private String sectelphone;
	private String secfax;
	private String secemail;
	private String mainpage;
	private String infopage;
	private String regaddr;
	private String workaddr;
	private String ipoprice;
	private String ipotime;
	private String underwriter;   //保荐人
	private String refree;        //承销人
	private String plate;
	private String scopebusi;     //营业范围
	private String introduction;
	
	/** The changes info list. */
	private List<CompanyChangeInfo> changes = new ArrayList<CompanyChangeInfo>();
	
	public String getCname()
	{
		return cname;
	}
	
	public void setCname(String name)
	{
		this.cname = name;
	}
	public String getEname()
	{
		return ename;
	}
	public void setEname(String ename)
	{
		this.ename = ename;
	}
	public String getFoundtime()
	{
		return foundtime;
	}
	public void setFoundtime(String foundtime)
	{
		this.foundtime = foundtime;
	}
	public String getExchange()
	{
		return exchange;
	}
	public void setExchange(String exchange)
	{
		this.exchange = exchange;
	}
	public String getIndustry()
	{
		return industry;
	}
	public void setIndustry(String industry)
	{
		this.industry = industry;
	}
	public double getCapital()
	{
		return capital;
	}
	public void setCapital(double capital)
	{
		this.capital = capital;
	}
	public String getTelphone()
	{
		return telphone;
	}
	public void setTelphone(String telphone)
	{
		this.telphone = telphone;
	}
	public String getFax()
	{
		return fax;
	}
	public void setFax(String fax)
	{
		this.fax = fax;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getRepresentative()
	{
		return representative;
	}
	public void setRepresentative(String representative)
	{
		this.representative = representative;
	}
	public String getSecretary()
	{
		return secretary;
	}
	public void setSecretary(String secretary)
	{
		this.secretary = secretary;
	}
	public String getSectelphone()
	{
		return sectelphone;
	}
	public void setSectelphone(String sectelphone)
	{
		this.sectelphone = sectelphone;
	}
	public String getSecfax()
	{
		return secfax;
	}
	public void setSecfax(String secfax)
	{
		this.secfax = secfax;
	}
	public String getSecemail()
	{
		return secemail;
	}
	public void setSecemail(String secemail)
	{
		this.secemail = secemail;
	}
	public String getMainpage()
	{
		return mainpage;
	}
	public void setMainpage(String mainpage)
	{
		this.mainpage = mainpage;
	}
	public String getInfopage()
	{
		return infopage;
	}
	public void setInfopage(String infopage)
	{
		this.infopage = infopage;
	}
	public String getRegaddr()
	{
		return regaddr;
	}
	public void setRegaddr(String regaddr)
	{
		this.regaddr = regaddr;
	}
	public String getWorkaddr()
	{
		return workaddr;
	}
	public void setWorkaddr(String workaddr)
	{
		this.workaddr = workaddr;
	}
	public String getIpoprice()
	{
		return ipoprice;
	}
	public void setIpoprice(String ipoprice)
	{
		this.ipoprice = ipoprice;
	}
	public String getIpotime()
	{
		return ipotime;
	}
	public void setIpotime(String ipotime)
	{
		this.ipotime = ipotime;
	}
	public String getUnderwriter()
	{
		return underwriter;
	}
	public void setUnderwriter(String underwriter)
	{
		this.underwriter = underwriter;
	}
	public String getRefree()
	{
		return refree;
	}
	public void setRefree(String refree)
	{
		this.refree = refree;
	}
	public String getPlate()
	{
		return plate;
	}
	public void setPlate(String plate)
	{
		this.plate = plate;
	}
	public String getScopebusi()
	{
		return scopebusi;
	}
	public void setScopebusi(String scopebusi)
	{
		this.scopebusi = scopebusi;
	}
	public String getIntroduction()
	{
		return introduction;
	}
	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}
	
	/**
	 * Add the change info. 
	 * 
	 * @param info
	 */
	public void addChangeInfo(CompanyChangeInfo info)
	{
		changes.add(info);
	}
	
	/**
	 * Get the changes.
	 * @return
	 */
	public List<CompanyChangeInfo> getChanges()
	{
		return changes;
	}
	
	@Override
	public String toString()
	{
		return "CompanyInfo [id=" + id + ", cname=" + cname + ", ename=" + ename + ", foundtime=" + foundtime
				+ ", exchange=" + exchange + ", industry=" + industry + ", capital=" + capital + ", telphone="
				+ telphone + ", fax=" + fax + ", email=" + email + ", representative=" + representative + ", secretary="
				+ secretary + ", sectelphone=" + sectelphone + ", secfax=" + secfax + ", secemail=" + secemail
				+ ", mainpage=" + mainpage + ", infopage=" + infopage + ", regaddr=" + regaddr + ", workaddr="
				+ workaddr + ", ipoprice=" + ipoprice + ", ipotime=" + ipotime + ", underwriter=" + underwriter
				+ ", refree=" + refree + ", plate=" + plate + ", scopebusi=" + scopebusi + ", introduction="
				+ introduction + ", symbol=" + symbol + "]";
	} 
}

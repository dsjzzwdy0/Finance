package com.loris.stock.bean.data.table;

import com.loris.base.bean.entity.AutoIdEntity;

/**
 *｜净利润(万元)          ｜  77843.73｜  15407.41｜ 347159.09｜ 398173.85｜
 *｜净利润增长率(%)       ｜   74.3397｜  -95.5619｜  -12.8122｜   15.6523｜
 *｜营业总收入(万元)      ｜13815993.1｜27419678.2｜27704852.9｜28857087.4｜
 *｜营业总收入增长率(%)   ｜   -1.4934｜   -1.0293｜   -3.9929｜   -4.9901｜
 *｜加权净资产收益率(%)   ｜    1.0000｜    0.2000｜    4.4000｜    5.2000｜
 *｜资产负债比率(%)       ｜   61.0230｜   62.5744｜   61.9634｜   57.9021｜
 *｜ ───────────┼─────┼─────┼─────┼─────┤
 *｜净利润现金含量(%)     ｜ 6382.0312｜51614.7861｜ 2570.3854｜ 2321.3272｜
 *｜基本每股收益(元)      ｜    0.0367｜    0.0073｜    0.1638｜    0.1878｜
 *｜每股收益-扣除(元)     ｜    0.0472｜   -0.0027｜    0.0677｜    0.1886｜
 *｜每股收益-摊薄(元)     ｜    0.0367｜    0.0073｜    0.1638｜    0.1878｜
 *｜───────────┼─────┼─────┼─────┼─────┤
 *｜每股资本公积金(元)    ｜    1.3122｜    1.3121｜    1.3121｜    1.3121｜
 *｜每股未分配利润(元)    ｜    1.4253｜    1.3886｜    1.4454｜    1.3564｜
 *｜每股净资产(元)        ｜    3.6979｜    3.6564｜    3.7120｜    3.6485｜
 *｜每股经营现金流量(元)  ｜    2.3438｜    3.7518｜    4.2098｜    4.3606｜
 *｜经营活动现金净流量增长｜   12.7680｜  -10.8796｜   -3.4577｜   10.8669｜
 *｜率(%
 *
 * @author usr
 *
 */
public class StockFinancial extends AutoIdEntity
{
	private static final long serialVersionUID = 1L;

	/** 净利润 */
	private double netincome;
	
	/** 净资产 */
	private double netassets;
	
	/** 净利润增长率 */
	private double netincomeRatio;
	
	/** 营业总收入 */
	private double totalincome;
	
	/** 营业总收入增长率 */
	private double totalincomeRatio;
	
	/** 资产负债比率 */
	private double liability;
	
	public StockFinancial()
	{
	}
	
	
	/**
	 * Set the net assets.
	 * @param netassets
	 */
	public void setNetassets(double netassets)
	{
		this.netassets = netassets;
	}
	/**
	 * Get the netassessts.
	 * 
	 * @return
	 */
	public double getNetassets()
	{
		return netassets;
	}
	
	/**
	 * Get the liability.
	 * 
	 * @return
	 */
	public double getLiability()
	{
		return liability;
	}
	
	/**
	 * Set the liability.
	 * 
	 * @param liability
	 */
	public void setLiability(double liability)
	{
		this.liability = liability;
	}
	
	/**
	 * Set the Total income ratio.
	 * 
	 * @param ratio
	 */
	public void setTotalincomeRatio(double ratio)
	{
		this.totalincomeRatio = ratio;
	}
	
	/**
	 * Get the tota income ratio.
	 * 
	 * @return
	 */
	public double getTotalincomeRatio()
	{
		return totalincomeRatio;
	}
	
	/**
	 * 
	 * Set the net income.
	 * 
	 * @param netincome
	 */
	public void setNetincome(double netincome)
	{
		this.netincome = netincome;
	}
	
	/**
	 * Get net income.
	 * @return
	 */
	public double getNetincome()
	{
		return netincome;
	}
	
	/**
	 * Get the netin
	 * @return
	 */
	public double getNetincomeRatio()
	{
		return netincomeRatio;
	}
	
	/**
	 * Set the net income ratio.
	 * 
	 * @param ratio
	 */
	public void setNetincomeRatio(double ratio)
	{
		this.netincomeRatio = ratio;
	}
	
	/**
	 * Total income value.
	 * 
	 * @param total
	 */
	public void setTotalincome(double total)
	{
		this.totalincome = total;
	}
	
	/**
	 * Get the total income.
	 * @return
	 */
	public double getTotalincome()
	{
		return totalincome;
	}
}

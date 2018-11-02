package com.loris.stock.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

/**
 * 日交易数据信息
 * 
 * @author dsj
 *
 */
@TableName("stock_daily_record")
public class DailyRecord extends AutoIdEntity
{
	private static final long serialVersionUID = 1L;
	private String day;
	private String symbol;
	private String code;
	private String name;
	private double trade;
	private double pricechange;
	private double changepercent;
	private double buy;
	private double sell;
	private double settlement;
	private double open;
	private double high;
	private double low;
	private int volume;
	private int amount;
	private String ticktime;
	private double per;
	private double per_d;
	private double nta;
	private double pb;
	private double nmc;
	private double mktcap;
	private double turnoverratio;
	
	/**
	 * Get the day.
	 * @return
	 */
	public String getDay()
	{
		return day;
	}

	public void setDay(String day)
	{
		this.day = day;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getTrade()
	{
		return trade;
	}

	public void setTrade(double trade)
	{
		this.trade = trade;
	}

	public double getPricechange()
	{
		return pricechange;
	}

	public void setPricechange(double pricechange)
	{
		this.pricechange = pricechange;
	}

	public double getChangepercent()
	{
		return changepercent;
	}

	public void setChangepercent(double changepercent)
	{
		this.changepercent = changepercent;
	}

	public double getBuy()
	{
		return buy;
	}

	public void setBuy(double buy)
	{
		this.buy = buy;
	}

	public double getSell()
	{
		return sell;
	}

	public void setSell(double sell)
	{
		this.sell = sell;
	}

	public double getSettlement()
	{
		return settlement;
	}

	public void setSettlement(double settlement)
	{
		this.settlement = settlement;
	}

	public double getOpen()
	{
		return open;
	}

	public void setOpen(double open)
	{
		this.open = open;
	}

	public double getHigh()
	{
		return high;
	}

	public void setHigh(double high)
	{
		this.high = high;
	}
	
	public double getMktcap()
	{
		return mktcap;
	}

	public void setMktcap(double mktcap)
	{
		this.mktcap = mktcap;
	}

	public double getLow()
	{
		return low;
	}

	public void setLow(double low)
	{
		this.low = low;
	}

	public int getVolume()
	{
		return volume;
	}

	public void setVolume(int volume)
	{
		this.volume = volume;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getTicktime()
	{
		return ticktime;
	}

	public void setTicktime(String ticktime)
	{
		this.ticktime = ticktime;
	}

	public double getPer()
	{
		return per;
	}

	public void setPer(double per)
	{
		this.per = per;
	}

	public double getPer_d()
	{
		return per_d;
	}

	public void setPer_d(double per_d)
	{
		this.per_d = per_d;
	}
	
	
	/**
	 * Get the pb.
	 * @return
	 */
	public double getNta()
	{
		return nta;
	}
	
	/**
	 * Set the pb value.
	 * @param pb
	 */
	public void setNta(double nta)
	{
		this.nta = nta;
	}
	
	/**
	 * Get the pb.
	 * @return
	 */
	public double getPb()
	{
		return pb;
	}
	
	/**
	 * Set the pb value.
	 * @param pb
	 */
	public void setPb(double pb)
	{
		this.pb = pb;
	}
	
	/**
	 * Set the nmc.
	 * @param nmc
	 */
	public void setNmc(double nmc)
	{
		this.nmc = nmc;
	}
	
	/**
	 * Get the nmc.
	 * @return
	 */
	public double getNmc()
	{
		return nmc;
	}
	
	/**
	 * Set the turnoverratio.
	 * @param ratio
	 */
	public void setTurnoverratio(double ratio)
	{
		this.turnoverratio = ratio;
	}
	
	/**
	 * Get the ratio
	 * @return
	 */
	public double getTurnoverratio()
	{
		return turnoverratio;
	}
	
	@Override
	public String toString()
	{
		return "DailyRecord [day=" + day + ", symbol=" + symbol + ", code=" + code + ", name=" + name + ", trade="
				+ trade + ", pricechange=" + pricechange + ", changepercent=" + changepercent + ", buy=" + buy
				+ ", sell=" + sell + ", settlement=" + settlement + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", volume=" + volume + ", amount=" + amount + ", ticktime=" + ticktime + ", per=" + per + ", per_d="
				+ per_d + ", nta=" + nta + ", pb=" + pb + ", nmc=" + nmc + ", mktcap=" + mktcap + ", turnoverratio="
				+ turnoverratio + "]";
	}
}

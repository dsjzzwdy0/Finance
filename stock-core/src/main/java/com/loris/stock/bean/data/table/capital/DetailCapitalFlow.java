package com.loris.stock.bean.data.table.capital;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.stock.bean.item.capital.CapitalFlowItem;

@TableName("stock_capital_flow")
public class DetailCapitalFlow extends AbstractCapitalFlow
{
	private static final long serialVersionUID = 1L;
	/** The max value is 16. */
	public static final int size = 15;
	
	private double b100;
	private double s100;
	private double b200;
	private double s200;
	private double b300;
	private double s300;
	private double b400;
	private double s400;
	private double b500;
	private double s500;
	private double b600;
	private double s600;
	private double b700;
	private double s700;
	private double b800;
	private double s800;
	private double b900;
	private double s900;
	private double b1000;
	private double s1000;
	private double b2000;
	private double s2000;
	private double b3000;
	private double s3000;
	private double b4000;
	private double s4000;
	private double b5000;
	private double s5000;
	private double bmax;
	private double smax;
	
	/**
	 * Create a new instance of BasicCapitalFlow2
	 */
	public DetailCapitalFlow()
	{
		this(size);
	}
	
	/**
	 * BasicCapitalFlow2
	 * @param size
	 */
	protected DetailCapitalFlow(int size)
	{
		super(size);
		
		if(size > 20)
		{
			throw new UnsupportedOperationException();
		}
		
		CapitalFlowItem item;
		for(int i = 0; i < size; i ++)
		{
			item = getItem(i);
			if(i < 10)
			{
				item.setMinparam(i * 100);
				item.setMaxparam((i + 1) * 100);
			}
			else if(i < 20)
			{
				item.setMinparam((i - 10 + 1) * 1000);
				item.setMaxparam((i - 10 + 2) * 1000);
			}
			if(i == size - 1)
			{
				item.setMaxparam(MAX_VALUE);
				//maxValues[i] = MAX_VALUE;
			}
			item.setName("m" + item.getMaxparam());
		}
	}
	
	public void setB100(double value)
	{
		b100 = value;
		setBuyValue(0, value);
	}
	
	public void setS100(double value)
	{
		s100 = value;
		setSellValue(0, value);
	}
	
	public double getB100()
	{
		return b100;
	}
	
	public double getS100()
	{
		return s100;
	}

	public void setB200(double value)
	{
		b200 = value;
		setBuyValue(1, value);
	}
	
	public void setS200(double value)
	{
		s200 = value;
		setSellValue(1, value);
	}
	
	public double getB200()
	{
		return b200;
	}
	
	public double getS200()
	{
		return s200;
	}
	
	public void setB300(double value)
	{
		b300 = value;
		setBuyValue(2, value);
	}
	
	public void setS300(double value)
	{
		s300 = value;
		setSellValue(2, value);
	}
	
	public double getB300()
	{
		return b300;
	}
	
	public double getS300()
	{
		return s300;
	}
	
	public void setB400(double value)
	{
		b400 = value;
		setBuyValue(3, value);
	}
	
	public void setS400(double value)
	{
		s400 = value;
		setSellValue(3, value);
	}
	
	public double getB400()
	{
		return b400;
	}
	
	public double getS400()
	{
		return s400;
	}
	
	public void setB500(double value)
	{
		b500 = value;
		setBuyValue(4, value);
	}
	
	public void setS500(double value)
	{
		s500 = value;
		setSellValue(4, value);
	}
	
	public double getB500()
	{
		return b500;
	}
	
	public double getS500()
	{
		return s500;
	}
	
	public void setB600(double value)
	{
		b600 = value;
		setBuyValue(5, value);
	}
	
	public void setS600(double value)
	{
		s600 = value;
		setSellValue(5, value);
	}
	
	public double getB600()
	{
		return b600;
	}
	
	public double getS600()
	{
		return s600;
	}
	
	public void setB700(double value)
	{
		b700 = value;
		setBuyValue(6, value);
	}
	
	public void setS700(double value)
	{
		s700 = value;
		setSellValue(6, value);
	}
	
	public double getB700()
	{
		return b700;
	}
	
	public double getS700()
	{
		return s700;
	}
	
	public void setB800(double value)
	{
		b800 = value;
		setBuyValue(7, value);
	}
	
	public void setS800(double value)
	{
		s800 = value;
		setSellValue(7, value);
	}
	
	public double getB800()
	{
		return b800;
	}
	
	public double getS800()
	{
		return s800;
	}
	
	public void setB900(double value)
	{
		b900 = value;
		setBuyValue(8, value);
	}
	
	public void setS900(double value)
	{
		s900 = value;
		setSellValue(8, value);
	}
	
	public double getB900()
	{
		return b900;
	}
	
	public double getS900()
	{
		return s900;
	}
	
	public void setB1000(double value)
	{
		b1000 = value;
		setBuyValue(9, value);
	}
	
	public void setS1000(double value)
	{
		s1000 = value;
		setSellValue(9, value);
	}
	
	public double getB1000()
	{
		return b1000;
	}
	
	public double getS1000()
	{
		return s1000;
	}
	
	public void setB2000(double value)
	{
		b2000 = value;
		setBuyValue(10, value);
	}
	
	public void setS2000(double value)
	{
		s2000 = value;
		setSellValue(10, value);
	}
	
	public double getB2000()
	{
		return b2000;
	}
	
	public double getS2000()
	{
		return s2000;
	}
	
	public void setB3000(double value)
	{
		b3000 = value;
		setBuyValue(11, value);
	}
	
	public void setS3000(double value)
	{
		s3000 = value;
		setSellValue(11, value);
	}
	
	public double getB3000()
	{
		return b3000;
	}
	
	public double getS3000()
	{
		return s3000;
	}
	
	public void setB4000(double value)
	{
		b4000 = value;
		setBuyValue(12, value);
	}
	
	public void setS4000(double value)
	{
		s4000 = value;
		setSellValue(12, value);
	}
	
	public double getB4000()
	{
		return b4000;
	}
	
	public double getS4000()
	{
		return s4000;
	}
	
	public void setB5000(double value)
	{
		b5000 = value;
		setBuyValue(13, value);
	}
	
	public void setS5000(double value)
	{
		s5000 = value;
		setSellValue(13, value);
	}
	
	public double getB5000()
	{
		return b5000;
	}
	
	public double getS5000()
	{
		return s5000;
	}
	
	public void setBmax(double value)
	{
		bmax = value;
		setBuyValue(14, value);
	}
	
	public void setSmax(double value)
	{
		smax = value;
		setSellValue(14, value);
	}
	
	public double getBmax()
	{
		return bmax;
	}
	
	public double getSmax()
	{
		return smax;
	}
	
	/**
	 * 两个数据同步处理
	 */
	public void synchronize()
	{
		b100 = getBuyValue(0);
		s100 = getSellValue(0);
		b200 = getBuyValue(1);
		s200 = getSellValue(1);
		b300 = getBuyValue(2);
		s300 = getSellValue(2);
		b400 = getBuyValue(3);
		s400 = getSellValue(3);
		b500 = getBuyValue(4);
		s500 = getSellValue(4);
		b600 = getBuyValue(5);
		s600 = getSellValue(5);
		b700 = getBuyValue(6);
		s700 = getSellValue(6);
		b800 = getBuyValue(7);
		s800 = getSellValue(7);
		b900 = getBuyValue(8);
		s900 = getSellValue(8);
		b1000 = getBuyValue(9);
		s1000 = getSellValue(9);
		b2000 = getBuyValue(10);
		s2000 = getSellValue(10);
		b3000 = getBuyValue(11);
		s3000 = getSellValue(11);
		b4000 = getBuyValue(12);
		s4000 = getSellValue(12);
		b5000 = getBuyValue(13);
		s5000 = getSellValue(13);
		bmax = getBuyValue(14);
		smax = getSellValue(14);
	}
}

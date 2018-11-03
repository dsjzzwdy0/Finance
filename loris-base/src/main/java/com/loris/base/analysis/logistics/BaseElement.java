package com.loris.base.analysis.logistics;

import java.util.ArrayList;
import java.util.List;

public class BaseElement implements Element
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected double label;
	protected String labelname;
	protected List<Double> attributes;

	@Override
	public double getLabel()
	{
		return label;
	}

	@Override
	public List<Double> getAttributes()
	{
		return attributes;
	}

	public void setLabel(double label)
	{
		this.label = label;
	}

	public void setAttributes(List<Double> attributes)
	{
		this.attributes = attributes;
	}
	
	public void setAttributes(double[] values)
	{
		attributes = new ArrayList<Double>();
		for (double d : values)
		{
			attributes.add(d);
		}
	}

	@Override
	public int attributeSize()
	{
		return attributes.size();
	}

	public String getLabelname()
	{
		return labelname;
	}

	public void setLabelname(String labelname)
	{
		this.labelname = labelname;
	}

	@Override
	public String toString()
	{
		return "BaseElement [label=" + label + ", labelname=" + labelname + ", attributes=" + attributes + "]";
	}
}

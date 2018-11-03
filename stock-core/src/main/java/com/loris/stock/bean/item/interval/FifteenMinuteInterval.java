package com.loris.stock.bean.item.interval;

import java.io.Serializable;
import java.util.Calendar;

import com.loris.base.util.SerialVersion;

/**
 *
 * @author viorel.gheba
 */
public class FifteenMinuteInterval extends Interval implements Serializable
{

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public FifteenMinuteInterval()
    {
        super("15 Min", true);
		timeParam = "15";
    }

    public long startTime()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, -3);
        return c.getTimeInMillis();
    }

    public String getTimeParam()
    {
        return timeParam;
    }

    public int getLengthInSeconds()
    {
        return 900;
    }

}

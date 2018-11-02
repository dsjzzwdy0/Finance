/*
 * @Author Irakli Nadareishvili
 * CVS-ID: $Id: MonitorThread.java,v 1.5 2005/01/25 17:31:02 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.scheduler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.loris.base.web.util.Monitor;
import com.loris.base.web.util.DashBoard;


public class MonitorThread extends Thread
{

	private static Logger log = Logger.getLogger(MultiTaskSchedulerThread.class);
	private static final String FILENAME = "monitor.log";
	private BufferedWriter out;

	/** to be able to print records list every 10 output interval **/
	private static int recordsCounter = 0;

	private long milliseconds;

	public MonitorThread(long milliseconds)
	{
		this.milliseconds = milliseconds;

		try
		{
			// -- Purge old file and initialize new.
			this.out = new BufferedWriter(new FileWriter(FILENAME));
			this.out.write("Monitor started at: " + Monitor.getCurrentTime() + "\n");
			this.out.close();
		}
		catch (IOException e)
		{
			log.error(" Could not initialize monitor log");
		}

	}

	public void run()
	{

		try
		{
			this.out = new BufferedWriter(new FileWriter(FILENAME, true));

			while (!Monitor.stopCrawler && Monitor.numOfMainSchedulerThread > 0)
			{

				try
				{
					// -- Purge old file and initialize new.
					this.out.write(Monitor.getCurrentTime() + this.displayMonitorInfo());
					this.out.flush();
				}
				catch (IOException e)
				{
					log.error(" Could not append to monitor log");
				}

				try
				{
					MonitorThread.sleep(this.milliseconds);
				}
				catch (InterruptedException ex)
				{
					log.warn("TaskMonitor interrupted");
				}
			}

			this.out.close();

		}
		catch (IOException ex1)
		{
			log.error(" Could not close monitor log");
		}
	}

	private String displayMonitorInfo()
	{
		long elapsedSeconds = 0L;
		try
		{
			elapsedSeconds = Math.round((float) (System.currentTimeMillis() - Monitor.getStartTime()) / 1000.0);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		String display = " ====== Elapsed: " + elapsedSeconds + " secs =====";
		long numProcessed = Monitor.fetchedCounter;
		try
		{
			float speed = (float) Math.round((float) numProcessed / elapsedSeconds * 100) / 100;
			display += "\nAverage Speed: " + speed + " pages/second fetched ";
			display += "\nCurrent Speed: " + getCurrentSpeed() + " pages/second fetched ";
			display += "\nActive Threads: " + Monitor.getThreadCounter();
		}
		catch (Exception ex1)
		{
		}

		recordsCounter++;
		if (recordsCounter == 20)
		{
			display += "\n ------------------ RECORD URLs ---------------------------\r\n";
			display += DashBoard.print();
			display += "\n ---------------------------------------------------------- ";
			recordsCounter = 0;
		}

		return display + "\n";
	}

	/** used to calculate current speed **/
	private static long startTimeCS = 0;

	/** used to calculate current speed **/
	private static long endTimeCS = 0;

	/** used to calculate current speed **/
	private static float speedCS = 0;

	/** used to calculate current speed **/
	private static final long PERIOD_CS = 3;

	/**
	 * This is kinda more accurate than the average speed of a multihour
	 * process.
	 *
	 * It counts time needed for 3 iterations.
	 *
	 * @return int
	 */
	private static float getCurrentSpeed()
	{
		return speedCS;
	}

	public static void calculateCurrentSpeed()
	{
		float speed = 0;

		if (Monitor.fetchedCounter == 0)
		{
			startTimeCS = System.currentTimeMillis();
		}

		if (Monitor.fetchedCounter % PERIOD_CS == 0 && Monitor.fetchedCounter > 0)
		{

			endTimeCS = System.currentTimeMillis();

			speed = (float) PERIOD_CS / (float) (endTimeCS - startTimeCS) * 1000;
			speed = (float) Math.round(speed * 100.0) / 100;

			startTimeCS = endTimeCS;

			speedCS = (float) speed;
		}

	}

}

package com.loris.stock.repository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.stock.web.repository.StockPageManager;

public class BackupManager implements AutoCloseable
{
	private static Logger log = Logger.getLogger(BackupManager.class);
	
	/** The table name. */
	private static String webCapitalTable = "STOCK_WEB_CAPITAL_CONTENT";

	/** The create table sql. */
	private static String createTableSQL = "CREATE table STOCK_WEB_CAPITAL_CONTENT(" +
			"SYMBOL varchar(10), URL varchar(100), TYPE varchar(5),	CREATETIME char(20)," + 
			"CONTENT longtext,COMPLETE tinyint(1))";
	
	/** The insert table value sql. */
	private static String insertCapitalSQL = "insert into STOCK_WEB_CAPITAL_CONTENT(" +
			"SYMBOL, URL, TYPE, CREATETIME, CONTENT, COMPLETE) values(?, ?, ?, ?, ?, ?)";
			
	/** The regular Pattern */
	private static Pattern pattern = Pattern.compile("[-]");

	/** The Base directory. */
	private String baseDir;

	/** The SQLite prefix connection string. */
	private String preConnStr = "jdbc:sqlite:";
	
	/** The sql session. */
	//private SqlSession session = null;
	
	/** The PageMapper. */
	//private PageMapper pageMapper = null;
	
	/** The conection of the main database. */
	Connection mainConn = null;
	
	/** The StockManager. */
	StockManager stockManager;
	
	/** The StockPageManager. */
	StockPageManager stockPageManager;
	
	/** Context. */
	LorisContext context;
	
	/**
	 * Create new instance of BackupManager.
	 */
	public BackupManager(LorisContext context)
	{
		//session = StockContext.getInstance().getSqlSessionFactory().openSession();
		//pageMapper = session.getMapper(PageMapper.class);
		this.context = context;
		stockManager = context.getApplicationContext().getBean(StockManager.class);
		stockPageManager = context.getApplicationContext().getBean(StockPageManager.class);
	}

	/**
	 * Set the base directory.
	 * 
	 * @param baseDir
	 */
	public void setBaseDir(String baseDir)
	{
		this.baseDir = baseDir;
	}
	
	/**
	 * Backup all the web capital content value.
	 * @throws IOException
	 * @throws SQLException
	 */
	public void backupAllWebCapitalContent(boolean all) throws IOException, SQLException
	{
		if(mainConn == null)
		{
			mainConn = context.getConnection();
		}
		
		List<String> backupDays = new ArrayList<String>();
		if(!all)
		{
			scanBackupDays(new File(baseDir), backupDays);
			log.info("Scan: " + backupDays);
		}
		
		List<String> webDays = stockPageManager.getDetailDownloadDays();
		log.info("Backup : " + webDays);
		
		int size = webDays.size();
		int num = 1;
		for (String day : webDays)
		{
			log.info("Backup [" + num + " of " + size + "] " + day);
			
			if(!backupDays.contains(day))
				backupWebCapitalContent(day);
			num ++;
		}

		log.info("Backup data completed.");
	}
	

	/**
	 * Backup Web Capital content.
	 * 
	 * @param day
	 * @throws SQLException
	 */
	public void backupWebCapitalContent(String day) throws IOException, SQLException
	{	
		String sqlDbName = getSQLiteDbFile(day);

		long start = System.currentTimeMillis();
		log.info("Starting to backup stock_web_capital_content where '" + day + "' to " + sqlDbName);
		try (Connection backConn = DriverManager.getConnection(preConnStr + sqlDbName))
		{
			backConn.setAutoCommit(false);
			try (Statement st = backConn.createStatement())
			{
				if (!checkExistOrCreateTable(st, webCapitalTable))
				{
					throw new IOException("Error when check and create " + sqlDbName + ".");
				}
				
				log.info("Loading data from main database...");
				
				String sql = "select symbol, url, type, createtime, content, complete" + 
						" from stock_web_capital_content where createtime='" + day +"'";
				Statement mainSt = mainConn.createStatement();
				ResultSet rs = mainSt.executeQuery(sql);
				int num = 1;
				
				PreparedStatement ps = backConn.prepareStatement(insertCapitalSQL);
				while (rs.next())
				{					
					ps.setString(1, rs.getString(1));
					ps.setString(2, rs.getString(2));
					ps.setString(4, rs.getString(3));
					ps.setString(4, rs.getString(4));
					ps.setString(5, rs.getString(5));
					ps.setBoolean(6, rs.getBoolean(6));
					ps.addBatch();
					
					if(num % 20 == 0)
					{
						ps.executeBatch();
						backConn.commit();
						ps.clearBatch();
						log.info("Backup [" + num + "] records." );
					}
					
					num ++;
				}
				
				
				/*List<WebPage> pages = pageMapper.getPageListByCreateTime(day);				
				int size = pages.size();
				int num = 1;
				
				PreparedStatement ps = backConn.prepareStatement(insertCapitalSQL);
				for (WebPage page : pages)
				{					
					ps.setString(1, page.getSymbol());
					ps.setString(2, page.getURL());
					ps.setString(4, page.getType());
					ps.setString(4, page.getCreatetime());
					ps.setString(5, page.getContent());
					ps.setBoolean(6, page.isCompleted());
					ps.addBatch();
					
					if(num % 20 == 0)
					{
						ps.executeBatch();
						backConn.commit();
						ps.clearBatch();
						log.info("Backup [" + num + " of " + size + "]" );
					}
					
					num ++;
				}

				pages.clear();
				pages = null;
				*/
				
				ps.executeBatch();
				backConn.commit();
				ps.clearBatch();
				
				ps.close();	
				
				rs.close();
				mainSt.close();
				
			}
		}
		System.gc();
		long end = System.currentTimeMillis();
		log.info("Backup stock_web_capital_content '" + day + "' to " + sqlDbName + " spend " + (end - start) + "ms.");
	}

	/**
	 * Create the table name.
	 * 
	 * @param st
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean checkExistOrCreateTable(Statement st, String tableName) throws SQLException
	{
		String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + tableName + "'";
		try (ResultSet rs = st.executeQuery(sql))
		{
			if (rs.next() && rs.getInt(1) > 0)
			{
				return true;
			}
		}
		st.execute(createTableSQL);
		return true;
	}

	/**
	 * Get the sqlite db file, if the path of the file is not exist,
	 * This will create the path.
	 * 
	 * @param day
	 * @return
	 * @throws IOException
	 */
	public String getSQLiteDbFile(String day) throws IOException
	{
		String[] strs = pattern.split(day);
		if (strs == null || strs.length < 3)
		{
			throw new IllegalArgumentException("The day '" + day + "' is not a validate string value.");
		}
		String sqlDbName;
		String sqlDbDir = baseDir + File.separator + strs[0] + File.separator + strs[1];

		File file = new File(sqlDbDir);
		if (!file.exists() && !file.isDirectory())
		{
			file.mkdirs();
		}
		sqlDbName = sqlDbDir + File.separator + day + ".db";
		return sqlDbName;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private void scanBackupDays(File dir, List<String> days) throws IOException
	{
		if(dir.exists())
		{
			File[] files = dir.listFiles();
			for (File file : files)
			{
				if(file.isFile())
				{
					String name = file.getName().toLowerCase();
					if(name.endsWith(".db"))
					{
						name = name.replace(".db", "");
						days.add(name);
					}
				}
				else if(file.isDirectory())
				{
					scanBackupDays(file, days);
				}
			}
		}
	}
	
	
	/**
	 * Close the opened session.
	 */
	public void close()
	{
		try
		{
			if(mainConn != null)
			{
				mainConn.close();
			}
		}
		catch(Exception e)
		{			
		}
	}
	
}

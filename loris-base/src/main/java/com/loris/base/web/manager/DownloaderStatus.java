package com.loris.base.web.manager;

public interface DownloaderStatus
{
	public static final int STATUS_INIT = 0;          		//初始化 "init"
	public static final int STATUS_PREPARED = 1;  			//准备就绪"prepared"
	public static final int STATUS_DOWN = 2;        		//正在下载中"downing"
	public static final int STATUS_STOP = 3;           		//停止中"stop"
	public static final int STATUS_FINISH = 4;       		//完成下载了"finish"
}

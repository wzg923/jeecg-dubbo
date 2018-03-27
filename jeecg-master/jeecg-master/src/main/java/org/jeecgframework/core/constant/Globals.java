package org.jeecgframework.core.constant;

import org.jeecgframework.core.util.ResourceUtil;


/**  
* 项目名称：jeecg
* 类名称：Globals   
* 类描述：  全局变量定义
* 创建人： 张代浩      
* @version    
*
 */
public final class Globals {
	/**
	 * 保存用户到SESSION
	 */
	public static final String USER_SESSION="USER_SESSION";
	/**
	 * 人员类型
	 */
	public static final Short User_Normal=1;//正常
	public static final Short User_Forbidden=0;//禁用
	public static final Short User_ADMIN=-1;//超级管理员
	
	/**
	 * 逻辑删除标记
	 */
	/**
	 * 删除
	 */
	public static final Short Delete_Forbidden=1;
	/**
	 * 正常
	 */
	public static final Short Delete_Normal=0;
	
	/**
	 *日志级别定义
	 */
	public static final Short Log_Leavel_INFO=1;
	public static final Short Log_Leavel_WARRING=2;
	public static final Short Log_Leavel_ERROR=3;
	 /**
	  * 日志类型
	  */
	 public static final Short Log_Type_LOGIN=1; //登陆
	 public static final Short Log_Type_EXIT=2;  //退出
	 public static final Short Log_Type_INSERT=3; //插入
	 public static final Short Log_Type_DEL=4; //删除
	 public static final Short Log_Type_UPDATE=5; //更新
	 public static final Short Log_Type_UPLOAD=6; //上传
	 public static final Short Log_Type_OTHER=7; //其他
	 
	 
	 /**
	  * 词典分组定义
	  */
	 public static final String TypeGroup_Database="database";//数据表分类
	 
	 /**
	  * 权限等级
	  */
	 public static final Short Function_Leave_ONE=0;//一级权限
	 public static final Short Function_Leave_TWO=1;//二级权限
	 
	 /**
	  * 权限等级前缀
	  */
	 public static final String Function_Order_ONE="ofun";//一级权限
	 public static final String Function_Order_TWO="tfun";//二级权限
	 /**
	  * 权限类型
	  */
	 public static final Short Function_TYPE_PAGE=0;//页面（菜单：菜单类型）
	 public static final Short Function_TYPE_FROM=1;//表单/或者弹出（菜单：访问类型）
	 /**
	  * 没有勾选的操作code
	  */
	 public static final String NOAUTO_OPERATIONCODES ="noauto_operationCodes";
	 /**
	  * 勾选了的操作code
	  */
	 public static final String OPERATIONCODES ="operationCodes";
	 
	 
	 /**
	  * 权限类型
	  */
	 public static final Short OPERATION_TYPE_HIDE = 0;//页面
	 public static final Short OPERATION_TYPE_DISABLED = 1;//表单/或者弹出
	 
	 
	 /**
	  * 数据权限 - 菜单数据规则集合
	  */
	 public static final String MENU_DATA_AUTHOR_RULES ="MENU_DATA_AUTHOR_RULES";
	 /**
	  * 数据权限 - 菜单数据规则sql
	  */
	 public static final String MENU_DATA_AUTHOR_RULE_SQL ="MENU_DATA_AUTHOR_RULE_SQL";
	 /**
	  * 新闻法规
	  */
	 public static final Short Document_NEW=0; //新建
	 public static final Short Document_PUBLICH=0; //发布
	 
	 /**
	  * 内部邮件系统
	  */
	 public static final String MAIL_STATUS_UNSEND ="00"; //草稿
	 public static final String MAIL_STATUS_SEND ="01"; //已发送
	 public static final String MAIL_STATUS_DEL ="02"; //删除   已发送的邮件不能真正删除，不然接收人就看不到邮件了。
	 public static final String MAILRECEIVER_STATUS_UNREAD ="00"; //未读
	 public static final String MAILRECEIVER_STATUS_READ ="01";//已读

	 
	 
	 

		/**
		 * 保存用户到SESSION
		 */
		/**
		 * 人员类型
		 */
		public static Short User_QUIT=2;//员工离职
		 /**
		  * 日志类型
		  */
		 public static Short Log_Type_QUERY=8; //查询
		 public static Short Log_Type_EXEC_Procudure=9;//执行过程
		 public static Short Log_Type_UpdateDingdanStatus_status20=10;

		 
		 
		 
		/**Redis服务器是否正常开启**/
//		public static boolean Redis_Enable_Flag=false;//默认不开启
		
		/**ClusterSession flag*/
//		public static boolean Cluster_Session_Flag=false;//默认不开启
		
		//发送邮件通知功能开启标志
		/*public static boolean Mail_Flag=false;//默认开启//发送邮件通知功能开启标志
		///发送邮件通知功能开启标志    Redis 监控
		public static boolean Redis_Mail_Flag=false;//默认关闭
		
		//Session 共享集群 redis db
		public static Integer Session_DataBase=1;
		//用户登录信息 redis db
		public static Integer LoginInfo_DataBase=0;
		//网站数据缓存 redis db
		public static Integer WebsiteData_DataBase=2;
		//极光推送	redis db
		public static Integer Jpush_DataBase=3;
		//APP 用户token	redis db
		public static Integer AppToken_DataBase=4;
		//日志	redis db
		public static Integer Log_DataBase=0;
		
		//APP redis配置
		public static final String APP_TOKEN="logintoken";
		public static final String APP_KEY="hid";
		//app端登录保存缓存 口令key 前缀
		public static final String APP_TOKEN_KEY="app:logintoken:";
		//APP登录失败次数
		public static final String APP_LOGIN_FAILED_NUM="app:loginFailedNum:";
		//微信OPENID
		public static final String WEIXIN_OPENID="openid";
		//微信redirect_uri
		public static final String WEIXIN_REDIRECT_URL ="weixin_redirect_uri";
		//网站 redirect_uri
		public static final String WEBSITE_REDIRECT_URL ="web_redirect_uri";*/
		
		 
	 
	 
	 /**
	  * 配置系统是否开启按钮权限控制
	  */
	 public static boolean BUTTON_AUTHORITY_CHECK = false;
	 static{
		 String button_authority_jeecg = ResourceUtil.getSessionattachmenttitle("button.authority.jeecg");
		 if("true".equals(button_authority_jeecg)){
			 BUTTON_AUTHORITY_CHECK = true;
		 }
		 
		//redis 服务器 正常开启标志
		 //Redis_Enable_Flag= StringUtil.isEmpty(ResourceUtil.getConfigByName("redis.enable.flag")) ? false: Boolean.valueOf(ResourceUtil.getConfigByName("redis.enable.flag"));
		 //集群session
		 //Cluster_Session_Flag=StringUtil.isEmpty(ResourceUtil.getConfigByName("clusterSession.enable.flag")) ? false: Boolean.valueOf(ResourceUtil.getConfigByName("clusterSession.enable.flag"));
	
		 //redis 错误报警通知
//		 Redis_Mail_Flag=StringUtil.isEmpty(ResourceUtil.getConfigByName("redis.mail.flag")) ? false: Boolean.valueOf(ResourceUtil.getConfigByName("redis.mail.flag"));
		 //系统错误报警通知 
//		 Mail_Flag=StringUtil.isEmpty(ResourceUtil.getConfigByName("log.mail.flag")) ? false: Boolean.valueOf(ResourceUtil.getConfigByName("log.mail.flag"));

		 
	 }
}

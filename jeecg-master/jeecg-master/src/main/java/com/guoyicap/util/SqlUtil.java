package com.guoyicap.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.StringUtil;

/**
 * SQL 工具类
 * @author guang
 *
 */
public class SqlUtil {
	public static final String PAGINATION_HEAD= "select * from ( select row_.*, rownum rownum_ from ( ";
	public static final String PAGINATION_TAIL=") row_ where rownum <= :pager * :rows  ) where rownum_ >(:pager -1)*:rows";
	public static final String COUNT_START_HEAD="select count(*) from ( ";
	public static final String COUNT_START_TAIL=")";
	
	/**
	 * 手动获取jdbc connect时，操作完后，要关闭ResultSet，Statement
	 * @param statement
	 * @param resultSet
	 */
	public static void closeStatement(Statement statement,ResultSet resultSet) throws SQLException{
		if(resultSet!=null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				LogUtil.error("关闭ResultSet异常",e);
				throw e;
			}finally{
				if(statement!=null){
					try {
						statement.close();
					} catch (SQLException e) {			
						LogUtil.error("关闭Statement异常",e);
						throw e;
					}
				}				
			}
		}
		
		if(statement!=null){
			try {
				statement.close();
			} catch (SQLException e) {			
				LogUtil.error("关闭Statement异常",e);
				throw e;
			}
		}	
		
		
	}
	
	/**
	 * 手动获取jdbc connect时，操作完后，要关闭Statement
	 * @param statement
	 */
	public static void closeStatement(Statement statement) throws SQLException{
		if(statement!=null){
			try {
				statement.close();
			} catch (SQLException e) {
				LogUtil.error("关闭Statement异常",e);
				throw e;
			}
		}
		

	}
	
	/**手动关闭ReslutSet
	 * @param resultSet
	 * @throws SQLException 
	 */
	public static void closeResultSet(ResultSet resultSet) throws SQLException{
		if(resultSet!=null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				LogUtil.error("关闭ResultSet异常",e);
				throw e;
			}finally{
				if(resultSet.getStatement()!=null){
					try {
						resultSet.getStatement().close();	
					} catch (SQLException e) {			
						LogUtil.error("关闭Statement异常",e);
						throw e;
					}
				}
					
			}
		}
	}
	
	/**
	 * 自定义模糊查询参数处理
	 * 如果查询参数支持模糊查询，参数中带‘*’特殊字符
	 * 将‘*’字符替换为oracle支持的‘%’字符
	 * @param string
	 * @return
	 */
	public static String likeReplace(String s){
		if(s==null || "".equals(s)){
			return s;
		}
		//替换首字符*
		if(s.indexOf("*")==0){
			s="%"+s.substring(1);
		}
		
		/**替换其他部位字符‘*’ */
		//字符*下标
		int index=s.indexOf("*");
		while(index>0){
			//替换*字符为%字符
			s=s.substring(0,index)+"%" + s.substring(index+1, s.length());
			//字符*下标
			index=s.indexOf("*");
		}
		return s;
	}
	
	/**
	* @Title: setInParameters
	* @Description: sql in类型参数封装
	* @param @param inParams in类型参数，用逗号隔开
	* @param @return    设定文件
	* @return String    返回类型
	*/ 
	public static String setInParameters(String inParams){
		//参数为空
		if(StringUtil.isEmpty(inParams)){
			return "''";
		}
		String[] paramsArray=inParams.split(",");
		StringBuilder sql=new StringBuilder("(");
		if(paramsArray.length>0){
			//拼接参数
			for(int i=0;i<paramsArray.length;i++){
				if(i==paramsArray.length-1){
					sql.append(":IN_"+i);
				}else{
					sql.append(":IN_"+i+",");
				}				
			}
			sql.append(")");
		}		
		return sql.toString();
	}
	
	/**
	* @Title: setInValues
	* @Description: 设置in类型参数 value值
	* @param @param query
	* @param @param inParams  in类型参数
	* @return void    返回类型
	*/ 
	public static void setInValues(Query query,String inParams){
		if(query!=null && StringUtil.isNotEmpty(inParams)){
			String[] paramsArray=inParams.split(",");
			if(paramsArray.length>0){
				//设置value值
				for(int i=0;i<paramsArray.length;i++){
					query.setString("IN_"+i, paramsArray[i]);
				}
			}
		}
	}
	/**
	* @Title: 把 ResultSet 转成List<Map> 格式
	* @param @param ResultSet  查询出的结果集
	* @return List<Map>    返回类型
	*/ 
	public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {   
        if (rs == null)   
            return Collections.EMPTY_LIST;   
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
        List list = new ArrayList();   
        Map rowData = new HashMap();   
        while (rs.next()) {   
         rowData = new HashMap(columnCount);   
         for (int i = 1; i <= columnCount; i++) {    ///循环属性
                 rowData.put(md.getColumnName(i), rs.getObject(i));    ///复制
         }   
         list.add(rowData);   
//         System.out.println("list:" + list.toString());   
        }   
        return list;   
	}
	

}

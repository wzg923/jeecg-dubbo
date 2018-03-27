package org.jeecgframework.core.common.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.OracleDialect;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BlobType;
import org.hibernate.type.ClobType;
import org.hibernate.type.StandardBasicTypes;

/**
 * 自定义Oracle Dialect
 * @author guang
 *
 */
public class MyOracleDialect extends OracleDialect{
	
	public MyOracleDialect(){
		super();
		registerHibernateType(Types.NCLOB,StandardBasicTypes.CLOB.getName());//NClob
		//registerHibernateType(Types.CLOB,ClobType.INSTANCE.getName());//clob		
		//registerHibernateType(Types.BLOB,BlobType.INSTANCE.getName());//blob
		//registerHibernateType(Types.DECIMAL, BigDecimalType.INSTANCE.getName());//decimal  
		registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());//nchar
		registerHibernateType(Types.NVARCHAR,StandardBasicTypes.STRING.getName());//nvarchar
		registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.CHARACTER.getName());//longnvarchar
	}
}

package com.lys.sys.mvc.dao.daoJdbcImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.FatalBeanException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.lys.sys.log.Log;
import com.lys.sys.mvc.dao.BaseDao;
import com.lys.sys.mvc.system.dictionary.SysStatic;
import com.lys.sys.utils.BeanUtils;
import com.lys.sys.utils.StringUtils;
import com.lys.sys.utils.annotation.MyTable;
import com.lys.sys.utils.pagination.QueryResult;

/**
 * 用于 SQL 的 执行。取得各类型返回数据。
 * @作者： shuang
 * @创建日期：2013-5-16
 * @版本： V 1.0
 * @类说明：
 */
@Repository("BaseDaoImpl")
public class BaseDaoImpl extends JdbcDaoSupport implements BaseDao  {  

	/**
	 * 注入 JdbcTemplate  方便使用其里面的  操作数据库方式。
	 */
	@Resource(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate; 
	/**
	 * 配置JdbcDaoSupport 中的 数据源
	 * @param dataSource
	 */
	@Resource(name = "dataSource")
	public void setDataSoure(DataSource dataSource){
		super.setDataSource(dataSource);
	}
	/**
	 * 创建一个简化插入的工具类对象
	 * @return SimpleJdbcInsert
	 */
	public SimpleJdbcInsert getSimpleJdbcInsert(){
		return new SimpleJdbcInsert(jdbcTemplate);
	}
	/**
	 * 将参数加入sql语句中
	 * 
	 * @param pStatement
	 * @param obj
	 * @return PreparedStatement
	 */
	public void formatSql(PreparedStatement pStatement,Object... obj) {
		try {
			if (obj.length != 0) 
				for (int i = 0; i < obj.length; i++) 
					pStatement.setObject(i + 1, obj[i]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 操作对象
	 * 添加-----单表单条数据-----向数据库添加一条数据，普通插入，不能获取到自增的主键值
	 * @param obj 传入有数据的实体对象,实体对象必须有@MyTable注解
	 * @return 影响行数 
	 * @基于SimpleJdbc 
	 * @使用示例：Test test=new Test(); Integer num=insert(test) ;
	 */
	public Integer insert(Object obj) {
		if(obj!=null){
			MyTable table=obj.getClass().getAnnotation(MyTable.class);
			if(table!=null){
				Map<String, Object> arg=BeanUtils.turnMap(obj,true);
			    SimpleJdbcInsert insert = getSimpleJdbcInsert();
			    insert.withTableName(table.tableName());
			    insert.compile();
			    Log.in.info("执行的SQL ："+insert.getInsertString());
			    return insert.execute(arg);
			}else{
				throw new RuntimeException("请为你要保存的数据bean 添加@MyTable注解，并指定表名!");
			}
		}else{
			throw new RuntimeException("传入保存对象不能为空!");
		}
	}
	/**
	 * 操作对象
	 * 添加-----单表单条数据-----向数据库添加一条数据，插入且能获得自增的主键值，主键存入实体对象中
	 * @param obj 传入有数据的实体对象，并实体有@MyTable 注解的 pkName值，主键类型只支持 Integer/Long/String三种类型
	 * @基于SimpleJdbc 
	 * @使用示例：Test test=new Test();  insertAndReturnKey(test) ; 但是test 中不能含有主键值
	 */
	public void insertAndReturnKey(Object obj) {
		if(obj!=null){
			MyTable table=obj.getClass().getAnnotation(MyTable.class);
			if(table==null)
				throw new RuntimeException("请为你要保存的数据bean 添加@MyTable注解，并指定表名和主键名!");
			Map<String, Object> arg=BeanUtils.turnMap(obj,true);
		    SimpleJdbcInsert insert = getSimpleJdbcInsert();
		    insert.withTableName(table.tableName());
		    insert.setGeneratedKeyName(table.pkName().toLowerCase());
		    Number id = insert.executeAndReturnKey(arg);
		    try {
		    	/**为obj 对象的 主键赋值**/
		    	PropertyDescriptor sourcePd =BeanUtils.getPropertyDescriptor(obj.getClass(), table.pkName().toLowerCase());
		    	Method writeMethod = sourcePd.getWriteMethod();
		    	if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()))
					writeMethod.setAccessible(true);
		    	String paramType= writeMethod.getParameterTypes()[0].getName();
		    	if("java.lang.Integer".equals(paramType)){
					writeMethod.invoke(obj,id.intValue() );
		    	}else if("java.lang.Long".equals(paramType)){
					writeMethod.invoke(obj,id.longValue() );
		    	}else if("java.lang.String".equals(paramType)){
					writeMethod.invoke(obj,id.toString() );
		    	}
			} catch (Exception e) {
				throw new RuntimeException("将ID值赋给对象失败，原因："+e.getMessage());
			}
		    Log.in.info("执行的SQL ："+insert.getInsertString());
		}else{
			throw new RuntimeException("传入保存对象不能为空!");
		}
	}
	/**
	 * 操作对象
	 * 添加-----单表多条数据-----向数据库添加多条数据，普通插入，不能获取到自增的主键值==批处理
	 * @param objs 传入有数据的  List集合实体对象
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	 * @基于SimpleJdbc 
	 * @使用示例：List<Test> tsets=new ArrayList<Test>();  insertBatch(tsets,Test.class) ;
	 */
	@SuppressWarnings("unchecked")
	public void insertBatch(List<?> objs,Class<?> obj) {
		if(objs!=null&&objs.size()>0 && obj!=null){
			MyTable table=(MyTable) obj.getAnnotation(MyTable.class);
			if(table==null)
				throw new RuntimeException("请为你要保存的 数据 bean 添加@MyTable注解，并指定表名!");
			int objsSize=objs.size();
			@SuppressWarnings("rawtypes")
			Map[] args=new Map[objsSize];
			for(int i=0;i<objsSize;i++)
				args[i]=BeanUtils.turnMap(objs.get(i),true);
			SimpleJdbcInsert insert = getSimpleJdbcInsert();
			insert.withTableName(table.tableName());
			insert.executeBatch(args);//返回批影响行数
			Log.in.info("执行的批SQL ："+insert.getInsertString());
		}else{
			throw new RuntimeException("传入保存的map不能为空或者 实体 类型不能为空!");
		}
	}
	/**
	 * 操作执行SQL
	 * 添加
	 * @param sql 执行的语句
	 * @param params 可变参数
	 * @return 添加成功后的数据自增主键值
	 * @说明：如果操作的表没有自增主键，那么则抛出异常
	 */
	public Number insertAndReturnKey(final String sql, final Object... params) {
		Log.in.info("save执行的SQL:" + sql);
		if(!StringUtils.hasText(sql))
			throw new RuntimeException("执行的sql不能为空!");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				formatSql(ps, params);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey();
	}
	/**
	 * 操作执行sql
	 * 删除/更新/添加
	 * @param sql 执行的语句（可以是预编译的sql）
	 * @param params 可变参数
	 * @return 影响条数 
	 * @使用示例：String sql=" update test set name=?,birthday=? where id=? ";  execute(sql,name值,birthday值,id值);或  execute(sql,new Object[]{xx,xx,xx});
	 */
	public Integer execute(final String sql, final Object... params) {
		Log.in.info("执行的SQL:" + sql);
		if(params!=null&&params.length>0){
			StringBuffer paramssb=new StringBuffer();
			for(int i=0,b=params.length;i<b;i++){
				paramssb.append("'").append(params[i]).append("'").append(",");
			}
			paramssb.deleteCharAt(paramssb.lastIndexOf(","));
			Log.in.info("相关的SQL参数:" + paramssb);
		}
		if(!StringUtils.hasText(sql))
			throw new RuntimeException("执行的SQL不能为空!");
		return jdbcTemplate.update(sql,params);
	}
	/**
	 * 操作执行SQL
	 * 删除---根据主键ID删除
	 * @param id   主键ID 值
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 pkName值
	 * @return 影响行数
	 * @使用示例 delete(188,Test.calss);
	 */
	public Integer delete(Object id,Class<?> obj){
		if(id!=null&&obj!=null){
			MyTable table=(MyTable) obj.getAnnotation(MyTable.class);
			if(table==null)
				throw new RuntimeException("请为你要保存的 数据 bean 添加@MyTable注解，并指定表名!");
			StringBuffer sb=new StringBuffer("delete from ").append(table.tableName()).append(" where ").append(table.pkName().toLowerCase()).append("=?");
			return execute(sb.toString(),id);
		}else{
			throw new RuntimeException("传入ID不能为空或者 实体 类型不能为空!");
		}
	}
	/**
	 * 操作执行SQL---该方法待测试
	 * 删除---根据主键ID删除
	 * @param ids   主键ID 值数组
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 pkName值
	 * @return 影响行数
	 * @使用示例 deletes(new Object[]{x,x,x,x},Test.calss); 该删除方法原理为构建一条删除SQL 语句，x值不能为''
	 */
	public Integer deletes(Object[] ids,Class<?> obj){
		if(ids!=null&&ids.length>0&&obj!=null){
			MyTable table=(MyTable) obj.getAnnotation(MyTable.class);
			if(table==null)
				throw new RuntimeException("请为你要保存的 数据 bean 添加@MyTable注解，并指定表名!");
			StringBuffer sb=new StringBuffer("delete from ").append(table.tableName()).append(" where ").append(table.pkName().toLowerCase()).append(" in (");
			int num=ids.length;
			while (num-->0) {
				sb.append("?,");
			}
			sb.deleteCharAt(sb.lastIndexOf(",")).append(")");
			return execute(sb.toString(),ids);
		}else{
			throw new RuntimeException("传入ids长度必须大于1或者 实体 类型不能为空!");
		}
	}
	/**
	 * 操作执行SQL
	 * 删除---根据多个条件删除
	 * @param map   条件的字段(key)和对应的值(value)
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	 * @return 影响条数
	 */
	public Integer delete(Map<String, Object> map,Class<?> obj){
		if(map!=null&&map.size()>0&&obj!=null){
			MyTable table=(MyTable) obj.getAnnotation(MyTable.class);
			if(table==null)
				throw new RuntimeException("请为你要保存的 数据 bean 添加@MyTable注解，并指定表名!");
			Integer a=0;
			StringBuffer sb=new StringBuffer(" delete from  ").append(table.tableName()).append(" where 1=1 ");
			Iterator<String> iter = map.keySet().iterator();
			Object[] o=new Object[map.size()];
			while (iter.hasNext()) {
				Object key = iter.next();
				sb.append(" and ").append(key).append("=? ");
				o[a++]=map.get(key);
			}
			return execute(sb.toString(),o);
		}else{
			throw new RuntimeException("传入条件的map不能为空或者 实体 类型不能为空!");
		}
	}
	/**
	 * 操作执行SQL
	 * 更新
	 * @param map 要修改的字段(key)和对应的值(value)，里面必须包含主键key和value
	 * @param obj 要修改的实体类型，或获取实体对象里面的@MyTable注解值
	 * @return 影响条数
	 * @使用示例：Map map=new HashMap();map.put("id", 189);map.put("name","改后了");  update(map,Test.calss); ， 本方法以后在考虑联合主键问题
	 */
	public Integer update(Map<String, Object> map,Class<?> obj){
		if(map!=null&&map.size()>0&&obj!=null){
			MyTable table=(MyTable) obj.getAnnotation(MyTable.class);
			if(table==null) 
				throw new RuntimeException("请为你要保存的 数据 bean 添加@MyTable注解，并指定表名!");
			String pkName=table.pkName();
			Integer a=0;
			StringBuffer sb=new StringBuffer(" update ").append(table.tableName()).append(" set ");
			Iterator<String> iter = map.keySet().iterator();// 考虑下如果为空的情况下
			Object[] o=new Object[map.size()+1];
			while (iter.hasNext()) {
				Object key = iter.next();
				sb.append(key).append("=?, ");
				o[a++]=map.get(key);
			}
			sb.deleteCharAt(sb.lastIndexOf(",")).append(" where ").append(pkName).append("=? ");
			o[a]=map.get(pkName);
			return execute(sb.toString(),o);
		}else{
			throw new RuntimeException("传入修改的map不能为空或者 实体 类型不能为空!");
		}
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询一行、单列 Int类型数据，并返回int型结果
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	@SuppressWarnings("deprecation")
	public Integer queryForInt( String sql){
		return jdbcTemplate.queryForInt(sql);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args  参数数组
	 * @return 查询一行、单列 Int类型数据，并返回int型结果
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	@SuppressWarnings("deprecation")
	public Integer queryForInt( String sql,Object[] args){
		return jdbcTemplate.queryForInt(sql,args);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询一行、单列 Int类型数据，并返回int型结果
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	@SuppressWarnings("deprecation")
	public Integer queryForInt(String sql, Object[] args, int[] argTypes){
		return jdbcTemplate.queryForInt(sql, args, argTypes);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param requiredType 指定返回数据的类型
	 * @return 查询一行、单列任何类型的数据，返回结果参数指定的类型
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Object queryForObject(String sql,Class<?> requiredType ){
		return jdbcTemplate.queryForObject(sql,requiredType);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param requiredType 指定返回数据的类型
	 * @return 查询一行、单列任何类型的数据，返回结果参数指定的类型
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Object queryForObject(String sql,Object[] args,Class<?> requiredType ){
		return jdbcTemplate.queryForObject(sql,args,requiredType);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param params 参数数组
	 * @param argTypes 对映的参数类型数组
	 * @param requiredType 指定返回数据的类型
	 * @return 查询一行、单列任何类型的数据，返回结果参数指定的类型
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Object queryForObject(String sql,Object[] args, int[] argTypes,Class<?> requiredType ){
		return jdbcTemplate.queryForObject(sql, args, argTypes, requiredType);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询一行、多列数据并将该行数据转换为Map返回
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Map<String, Object> queryForMap(String sql){
		return jdbcTemplate.queryForMap(sql);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @return 查询一行、多列数据并将该行数据转换为Map返回
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Map<String, Object> queryForMap(String sql, Object[] args){
		return jdbcTemplate.queryForMap(sql, args);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询一行、多列数据并将该行数据转换为Map返回
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes){
		return jdbcTemplate.queryForMap(sql, args, argTypes);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param elementType 返回对象集合
	 * @return 查询多行、一列数据并将该行数据转换为List<Object>返回
	 * @说明： 可以是String类型--得到一列的值 
	 */
	@SuppressWarnings("rawtypes")
	public List  queryForList(String sql,Class<?> elementType){
		return jdbcTemplate.queryForList(sql, elementType);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param elementType 返回对象集合
	 * @return 查询多行、一列数据并将该行数据转换为List<Object>返回
	 * @说明： 可以是String类型--得到一列的值 
	 */
	@SuppressWarnings("rawtypes")
	public List queryForList(String sql,Object[] args,Class<?> elementType){
		return jdbcTemplate.queryForList(sql, args,  elementType);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @param elementType 返回对象集合
	 * @return 查询多行、一列数据并将该行数据转换为List<Object>返回
	 * @说明： 可以是String类型--得到一列的值 
	 */
	@SuppressWarnings("rawtypes")
	public List queryForList(String sql,Object[] args,int[] argTypes,Class<?> elementType){
		return jdbcTemplate.queryForList(sql, args, argTypes, elementType);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询多行、多列数据并将该行数据转换为List<Map<String,Object>>返回
	 */
	public List<Map<String,Object>> queryForList(String sql){
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @return 查询多行、多列数据并将该行数据转换为List<Map<String,Object>>返回
	 */
	public List<Map<String,Object>> queryForList(String sql,Object[] args){
		return jdbcTemplate.queryForList(sql, args);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询多行、多列数据并将该行数据转换为List<Map<String,Object>>返回
	 */
	public List<Map<String,Object>> queryForList(String sql,Object[] args,int[] argTypes){
		return jdbcTemplate.queryForList(sql, args, argTypes);
	}
	/**
	 * 分页查询
	 * @param sql 查询的分页数据
	 * @param sqlCount 查询的分页数据的总条数
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @return QueryResult的分页对象
	 * @注：该分页是 分页前 排序。
	 */
	public QueryResult getPages(String sql,String sqlCount,Integer pageIndex,Integer pageSize){
		return getPages(sql, sqlCount, pageIndex, pageSize, null);
	}
	/**
	 * 分页查询
	 * @param sql 查询的分页数据
	 * @param sqlCount 查询的分页数据的总条数
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @param args 参数数组
	 * @return QueryResult的分页对象
	 * @注：该分页是 分页前 排序。
	 */
	public QueryResult getPages(String sql,String sqlCount,Integer pageIndex,Integer pageSize,Object[] args){
		QueryResult bean=new QueryResult();
		if(!StringUtils.hasText(sql)||!StringUtils.hasText(sqlCount))
			throw new RuntimeException("传入的两条SQL语句不能为空!");
		Log.in.info("执行的分页语句1--查询总行数：" + sqlCount);
		bean.setTotal(this.queryForInt(sqlCount,args));//查询出总行数
		Integer totalPages=(bean.getTotal() + pageSize - 1) / pageSize;//总页数
		if(pageIndex > totalPages ){//如果当前页码大于总页数， 则默认 为 总页数
			pageIndex=totalPages;
		}else if(pageIndex<1){
			pageIndex=1;
		}
		if (pageIndex > 0 && pageSize > 0){
			if(SysStatic.MYSQL.equals(SysStatic.dbName)){
				sql=sql+" limit "+(pageIndex - 1) * pageSize+","+pageSize;
			}else if(SysStatic.ORACLE.equals(SysStatic.dbName)){
				sql="select * from (select page_table.*,rownum page_rn from (select * from ("+sql+") page_tablen order by rownum ) page_table where rownum<="+(pageIndex*pageSize)+") where page_rn>"+(pageIndex-1)*pageSize;
			}
		}
		Log.in.info("执行的分页语句2--查询页数据：" + sql);
		bean.setResult(this.queryForList(sql,args));
		return bean;
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询一批数据，返回为SqlRowSet，类似于ResultSet，但不再绑定到连接上
	 * @说明：可以遍历SqlRowSet ，得到各种样式的数据
	 */
	public SqlRowSet queryForRowSet(String sql){
		return jdbcTemplate.queryForRowSet(sql);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @return 查询一批数据，返回为SqlRowSet，类似于ResultSet，但不再绑定到连接上
	 * @说明：可以遍历SqlRowSet ，得到各种样式的数据
	 */
	public SqlRowSet queryForRowSet(String sql,Object[] args){
		return jdbcTemplate.queryForRowSet(sql, args);
	}
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询一批数据，返回为SqlRowSet，类似于ResultSet，但不再绑定到连接上
	 * @说明：可以遍历SqlRowSet ，得到各种样式的数据。
	 * 使用示例：
	 *	SqlRowSet rs; int ColumnCount=rs.getMetaData().getColumnCount();
	 *	while (rs.next()) { Map<String, Object> obj = new LinkedHashMap<String, Object>();//一行的值
	 *		int i = 1; for (int j = 0; j < ColumnCount; j++)  obj.put(rs.getMetaData().getColumnName(j+1), rs.getString(i++));
	 *	}
	 */
	public SqlRowSet queryForRowSet(String sql,Object[] args,int[] argTypes){ 
		return jdbcTemplate.queryForRowSet(sql, args, argTypes);
	}
	/**
	 * 操作执行SQL
	 * 查询--对应的java bean集合
	 * @param sql 执行的语句
	 * @param clazz 传入返回List对象的类型，例如：User.class， 得到的值可以转换成(List<User>)进行转换
	 * @return clazz的对象集合List
	 */
	@SuppressWarnings("rawtypes")
	public List queryForListT(String sql,Class<?> clazz){
		return trunSqlRowSet(jdbcTemplate.queryForRowSet(sql), clazz);
	}
	/**
	 * 操作执行SQL
	 * 查询--对应的java bean集合
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param clazz 传入返回List对象的类型，例如：User.class， 得到的值可以转换成(List<User>)进行转换
	 * @return clazz的对象集合List
	 */
	@SuppressWarnings("rawtypes")
	public List queryForListT(String sql,Object[] args,Class<?> clazz){
		return trunSqlRowSet(jdbcTemplate.queryForRowSet(sql, args), clazz);
	}
	/**
	 * 操作执行SQL
	 * 查询--对应的java bean集合
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @param clazz 传入返回List对象的类型，例如：User.class， 得到的值可以转换成(List<User>)进行转换
	 * @return clazz的对象集合List
	 */
	@SuppressWarnings("rawtypes")
	public List queryForListT(String sql,Object[] args,int[] argTypes,Class<?> clazz){
		return trunSqlRowSet(jdbcTemplate.queryForRowSet(sql, args, argTypes), clazz);
	}
	/***
	 * 存储过程--- 查询
	 * @param callName  传入的存储过程名字
	 * @param params	传入的预处理的参数
	 * @return 结果集的List<Map<String, Object>>类型的数据
	 * @说明：主要用于查询得到结果集，不能获取返回的值。例如支持：TestCall11(IN P_ID int,IN P_NAME varchar(100)) 等只有IN 的类似存储过程
	 * @使用示例： executeCallReturnData("TestCall22", 1,"中国人call");
	 */
	public List<Map<String, Object>> executeCallReturnData(final String callName,final Object...params) {
		if(!StringUtils.hasText(callName))
			throw new RuntimeException("执行存储过程名不能为空!");
		return jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String sql="";
				if(params!=null&&params.length>0){//这个地方可以改进
					sql=callSql(callName, params.length);
				}else{
					sql=callSql(callName, 0);
				}
				Log.in.info("执行的存储过程语句：" + sql);
				CallableStatement call =null;
					try {
						call =con.prepareCall(sql);
						getCallableStatement(call, params);
					} catch (Exception e) {
						e.printStackTrace();
					}
				return call;
			}
		}, new CallableStatementCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				ResultSet rs =cs.executeQuery();
				List<Map<String, Object>> objs =  new LinkedList<Map<String, Object>>();
				if(rs!=null){
					try {
						int columnCount = rs.getMetaData().getColumnCount();
						while (rs.next()) {
							Map<String, Object> obj =  new LinkedCaseInsensitiveMap<Object>();//不区分大小写
							int i = 1;
							for (int j = 0; j < columnCount; j++) 
								obj.put(rs.getMetaData().getColumnName(j+1), rs.getString(i++));
							objs.add(obj);
						}
					} catch (Exception e) {
						throw new RuntimeException("结果集转换成List<Map<String, Object>>失败!");
					}
				}
				return objs;
			}
		});
	}
	/***
	 * 存储过程---执行（增删改）
	 * @param callName  传入的存储过程名字
	 * @param params	传入的预处理的参数
	 * @说明：主要用于通过存储过程执行操作数据的增删改。只支持：TestCall22(IN pid int,IN pname varchar(100),out flag int,out message varchar(1000)) 等必须有 两个输出参数的存储过程
	 * 		  flag =1 代表执行成功，msg=执行失败后的消息返回
	 * @使用示例： executeCall("TestCall22", 1,"中国人call"); 此处不需要传入后面两个返回的参数值
	 */
	public void executeCall(final String callName,final Object...params) {
		if(!StringUtils.hasText(callName))
			throw new RuntimeException("执行存储过程名不能为空!");
		final Integer paramsSize = params != null && params.length > 0 ? params.length: 0;//这个地方可以改进
		jdbcTemplate.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String sql = callSql(callName, paramsSize + 2);// 额外的加入两个返回参数，便于得到执行结果
				Log.in.info("执行的存储过程语句：" + sql);
				CallableStatement call = null;
				try {
					call = con.prepareCall(sql);
					getCallableStatement(call, params);
					call.registerOutParameter(paramsSize + 1, Types.INTEGER);// 执行标志
					call.registerOutParameter(paramsSize + 2, Types.VARCHAR);// 结果信息
				} catch (Exception e) {
					e.printStackTrace();
				}
				return call;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				int flag = cs.getInt(paramsSize + 1);// 错误标志Num
				String errmsg = cs.getString(paramsSize + 2);// 错误提示信息
				if (flag != 1) 
					throw new RuntimeException(errmsg);
				return null;
			}
		});
	}
	/**
	 * 使用函数， 待定
	 * @param functionName  传入的函数名字
	 * @param params	传入的预处理的参数
	 * @说明：此功能暂时不开通，因为函数的使用可以在 普通sql 中， 那么就可以直接使用jdbcTemplate. queryForList得到返回值
	 */
	@Deprecated
	public void executeFunction(String functionName,Object... params) {
	    StoredProcedure lengthFunction =new StoredProcedure(jdbcTemplate,functionName) {};
	    lengthFunction.execute(params);//得到一个map对象
	}
	
	/**
	 * 为存储过程  构建参数
	 * @param call
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void getCallableStatement(CallableStatement call,Object...params) throws Exception{
		if(params!=null&&params.length>0)
			 for(int i=1;i<=params.length;i++)
				 call.setObject(i,params[i-1]);
	}
	/**
	 * 为存储过程   构建执行的SQL
	 * @param callName
	 * @param params
	 * @return
	 */
	public String callSql(String callName,int paramsSize){
		if(paramsSize>0){
			StringBuffer sql=new StringBuffer("{Call "+callName+"(");
			for(int i=0;i<paramsSize;i++)
				sql.append("?,"); 
			sql.deleteCharAt(sql.length()-1);
			sql.append(")}");
		    return sql.toString();
		}else{
			return "{Call "+callName+"()}";
		}
	}

	/**
	 * 遍历了SqlRowSet对象，转换成List<T>类型数据
	 * @param callName
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List trunSqlRowSet(SqlRowSet rs,Class<?> clazz){
		List list=new ArrayList();
		Map<String,Object> map=null;
		if (clazz!=null) {
			int ColumnCount=rs.getMetaData().getColumnCount();//总列数
			Object obj=null;
			while (rs.next()) { 
				try {
					obj=clazz.newInstance();
				} catch (Throwable e) {
					throw new FatalBeanException("创建"+clazz+"的新实例失败!",e);
				}
				int m = 1;//列的下标
				Object value=null;//数据库表单元格值
				map=new LinkedCaseInsensitiveMap<Object>();//new 一个新的空间，用做存储一行多列的数据
				for (int n = 1; n <= ColumnCount; n++){
					value= rs.getString(m++);
					if(value==null){continue;}
					map.put(rs.getMetaData().getColumnName(n), value);
				}
				obj=BeanUtils.turnBean(map, clazz, true,true);
				list.add(obj);
			}
		}
		return list;
	}
}

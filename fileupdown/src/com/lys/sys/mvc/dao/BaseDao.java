package com.lys.sys.mvc.dao;
 
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.lys.sys.utils.pagination.QueryResult;


/**
 * JDBC数据访问层源接口
 * @author shuang
 */
public interface BaseDao  { 
	/**
	 * 操作对象
	 * 添加-----单表单条数据-----向数据库添加一条数据，普通插入，不能获取到自增的主键值
	 * @param obj 传入有数据的实体对象,实体对象必须有@MyTable注解
	 * @return 影响行数 
	 * @基于SimpleJdbc 
	 * @使用示例：Test test=new Test(); Integer num=insert(test) ;
	 */
	public Integer insert(Object obj);
	/**
	 * 操作对象
	 * 添加-----单表单条数据-----向数据库添加一条数据，插入且能获得自增的主键值，主键存入实体对象中
	 * @param obj 传入有数据的实体对象，并实体有@MyTable 注解的 pkName值，主键类型只支持 Integer/Long/String三种类型
	 * @基于SimpleJdbc 
	 * @使用示例：Test test=new Test();  insertAndReturnKey(test) ; 但是test 中不能含有主键值
	 */
	public void insertAndReturnKey(Object obj);
	/**
	 * 操作对象
	 * 添加-----单表多条数据-----向数据库添加多条数据，普通插入，不能获取到自增的主键值==批处理
	 * @param objs 传入有数据的  List集合实体对象
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	 * @基于SimpleJdbc 
	 * @使用示例：List<Test> tsets=new ArrayList<Test>();  insertBatch(tsets,Test.class) ;
	 */
	public void insertBatch(List<?> objs,Class<?> obj);
	/**
	 * 操作执行SQL
	 * 添加-----
	 * @param sql 执行的语句
	 * @param params 可变参数
	 * @return 添加成功后的数据自增主键值
	 * @说明：如果操作的表没有自增主键，那么则抛出异常
	 */
	public Number insertAndReturnKey(final String sql, final Object... params) ;
	/**
	 * 操作执行sql
	 * 删除/更新/添加
	 * @param sql 执行的语句（可以是预编译的sql）
	 * @param params 可变参数
	 * @return 影响条数 
	 * @使用示例：String sql=" update test set name=?,birthday=? where id=? ";  execute(sql,name值,birthday值,id值);或  execute(sql,new Object[]{xx,xx,xx});
	 */
	public Integer execute(final String sql, final Object... params) ;
	/**
	 * 操作执行SQL
	 * 删除---根据主键ID删除
	 * @param id   主键ID 值
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 pkName值
	 * @return 影响行数
	 * @使用示例 delete(188,Test.calss);
	 */
	public Integer delete(Object id,Class<?> obj);	
	/**
	 * 操作执行SQL---该方法待测试
	 * 删除---根据主键ID删除
	 * @param ids   主键ID 值数组
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 pkName值
	 * @return 影响行数
	 * @使用示例 deletes(new Object[]{x,x,x,x},Test.calss); 该删除方法原理为构建一条删除SQL 语句
	 */
	public Integer deletes(Object[] ids,Class<?> obj);
	/**
	 * 操作执行SQL
	 * 删除---根据多个条件删除
	 * @param map   条件的字段(key)和对应的值(value)
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	 * @return 影响条数
	 */
	public Integer delete(Map<String, Object> map,Class<?> obj);
	/**
	 * 操作执行SQL
	 * 更新
	 * @param map 要修改的字段(key)和对应的值(value)
	 * @param obj 要修改的实体类型，或获取实体对象里面的@MyTable注解值
	 * @return 影响条数
	 * @使用示例：Map map=new HashMap();map.put("id", 189);map.put("name","改后了");  update(map,Test.calss); 
	 */
	public Integer update(Map<String, Object> map,Class<?> obj);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询一行、单列 Int类型数据，并返回int型结果
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Integer queryForInt( String sql);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args  参数数组
	 * @return 查询一行、单列 Int类型数据，并返回int型结果
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Integer queryForInt( String sql,Object[] args);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询一行、单列 Int类型数据，并返回int型结果
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Integer queryForInt(String sql, Object[] args, int[] argTypes);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param requiredType 指定返回数据的类型
	 * @return 查询一行、单列任何类型的数据，返回结果参数指定的类型
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Object queryForObject(String sql,Class<?> requiredType );
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param requiredType 指定返回数据的类型
	 * @return 查询一行、单列任何类型的数据，返回结果参数指定的类型
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Object queryForObject(String sql,Object[] args,Class<?> requiredType );
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
	public Object queryForObject(String sql,Object[] args, int[] argTypes,Class<?> requiredType );
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询一行、多列数据并将该行数据转换为Map返回
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Map<String, Object> queryForMap(String sql);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @return 查询一行、多列数据并将该行数据转换为Map返回
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Map<String, Object> queryForMap(String sql, Object[] args);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询一行、多列数据并将该行数据转换为Map返回
	 * @说明：如果查询不到，或者查询多行， 则抛出异常
	 */
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param elementType 返回对象集合
	 * @return 查询多行、多列数据并将该行数据转换为List<Object>返回
	 * @说明： 可以是String类型--得到一列的值，可以是实体对象类型---得到一个实体集合
	 */
	@SuppressWarnings("rawtypes")
	public List  queryForList(String sql,Class<?> elementType);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param elementType 返回对象集合
	 * @return 查询多行、多列数据并将该行数据转换为List<Object>返回
	 * @说明： 可以是String类型--得到一列的值，可以是实体对象类型---得到一个实体集合
	 */
	@SuppressWarnings("rawtypes")
	public List  queryForList(String sql,Object[] args,Class<?> elementType);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @param elementType 返回对象集合
	 * @return 查询多行、多列数据并将该行数据转换为List<Object>返回
	 * @说明： 可以是String类型--得到一列的值，可以是实体对象类型---得到一个实体集合
	 */
	@SuppressWarnings("rawtypes")
	public List queryForList(String sql,Object[] args,int[] argTypes,Class<?> elementType);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询多行、多列数据并将该行数据转换为List<Map<String,Object>>返回
	 */
	public List<Map<String,Object>> queryForList(String sql);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @return 查询多行、多列数据并将该行数据转换为List<Map<String,Object>>返回
	 */
	public List<Map<String,Object>> queryForList(String sql,Object[] args);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param argTypes 对映的参数类型数据
	 * @return 查询多行、多列数据并将该行数据转换为List<Map<String,Object>>返回
	 */
	public List<Map<String,Object>> queryForList(String sql,Object[] args,int[] argTypes);
	/**
	 * 分页查询
	 * @param sql 查询的分页数据
	 * @param sqlCount 查询的分页数据的总条数
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @return QueryResult的分页对象
	 * @注：该分页是 分页前 排序。
	 */
	public QueryResult getPages(String sql,String sqlCount,Integer pageIndex,Integer pageSize);
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
	public QueryResult getPages(String sql,String sqlCount,Integer pageIndex,Integer pageSize,Object[] args);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @return 查询一批数据，返回为SqlRowSet，类似于ResultSet，但不再绑定到连接上
	 * @说明：可以遍历SqlRowSet ，得到各种样式的数据
	 */
	public SqlRowSet queryForRowSet(String sql);
	/**
	 * 操作执行SQL
	 * 查询
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @return 查询一批数据，返回为SqlRowSet，类似于ResultSet，但不再绑定到连接上
	 * @说明：可以遍历SqlRowSet ，得到各种样式的数据
	 */
	public SqlRowSet queryForRowSet(String sql,Object[] args);
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
	public SqlRowSet queryForRowSet(String sql,Object[] args,int[] argTypes);
	/**
	 * 操作执行SQL
	 * 查询--对应的java bean集合
	 * @param sql 执行的语句
	 * @param clazz 传入返回List对象的类型，例如：User.class， 得到的值可以转换成(List<User>)进行转换
	 * @return clazz的对象集合List
	 */
	@SuppressWarnings("rawtypes")
	public List queryForListT(String sql,Class<?> clazz);
	/**
	 * 操作执行SQL
	 * 查询--对应的java bean集合
	 * @param sql 执行的语句
	 * @param args 参数数组
	 * @param clazz 传入返回List对象的类型，例如：User.class， 得到的值可以转换成(List<User>)进行转换
	 * @return clazz的对象集合List
	 */
	@SuppressWarnings("rawtypes")
	public List queryForListT(String sql,Object[] args,Class<?> clazz);
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
	public List queryForListT(String sql,Object[] args,int[] argTypes,Class<?> clazz);
	/***
	 * 存储过程--- 查询
	 * @param callName  传入的存储过程名字
	 * @param params	传入的预处理的参数
	 * @return 结果集的List<Map<String, Object>>类型的数据
	 * @说明：主要用于查询得到结果集，不能获取返回的值。例如支持：TestCall11(IN P_ID int,IN P_NAME varchar(100)) 等只有IN 的类似存储过程
	 * @使用示例： executeCallReturnData("TestCall22", 1,"中国人call");
	 */
	public List<Map<String, Object>> executeCallReturnData(final String callName,final Object...params);
	/***
	 * 存储过程---执行（增删改）
	 * @param callName  传入的存储过程名字
	 * @param params	传入的预处理的参数
	 * @说明：主要用于通过存储过程执行操作数据的增删改。只支持：TestCall22(IN pid int,IN pname varchar(100),out flag int,out message varchar(1000)) 等必须有 两个输出参数的存储过程
	 * 		  flag =1 代表执行成功，msg=执行失败后的消息返回
	 * @使用示例： executeCall("TestCall22", 1,"中国人call"); 此处不需要传入后面两个返回的参数值
	 */
	public void executeCall(final String callName,final Object...params) ;
	
	/**
	 * 使用函数， 待定
	 * @param functionName  传入的函数名字
	 * @param params	传入的预处理的参数
	 * @说明：此功能暂时不开通，因为函数的使用可以在 普通sql 中， 那么就可以直接使用jdbcTemplate. queryForList得到返回值
	 */
	@Deprecated
	public void executeFunction(String functionName,Object... params) ;
}

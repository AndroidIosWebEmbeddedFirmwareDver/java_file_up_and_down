package com.lys.sys.utils.db;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.lys.sys.utils.OtherUtil;


/**
 * 用于构建批量操作数据库而完成的事务 准备类。把需要执行的语句以List<BizTransUtil>方式存入，然后在baseBiz中执行。
 * @author lys
 *
 */
public class BizTransUtil { 
	private Object add_obj;
	private List<?> add_list;//执行对象--传入有数据的  List集合实体对象
	private Class<?> add_class;//执行对象--传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	private Object del_id;//根据ID删除
	private Object[] del_ids;//批量根据ID删除
	private Map<String, Object> del_map;//删除的map条件
	private Class<?> del_obj;//删除的对象
	private Map<String, Object> upd_map;//修改的map条件
	private Class<?> upd_obj;//修改的对象
	private String execute_sql;//自定义SQL
	private Object[] execute_params;//自定义SQL需要的参数
	private Object returnvalue;//返回值
	private String returnType;//返回值类型
	private String executeType;//'insert'/'update'/'delete'
	
	private Boolean iscase=false;//是否开启执行sql的影响函数比较,默认不开启
	private int when;//异常消息
	private String erorrmsg;//异常消息
	/**
	 * 操作对象
	 * 添加-----单表单条数据-----向数据库添加一条数据，普通插入，不能获取到自增的主键值
	 * @param obj 传入有数据的实体对象,实体对象必须有@MyTable注解
	 * @return true or false 
	 * @使用示例：Test test=new Test();  addTRANS(test) ;
	 */
	public BizTransUtil(Object add_obj){
		this.add_obj=add_obj;
		this.executeType=OtherUtil.INSERT;
	}
	/**
	 * 操作对象
	 * 添加-----单表多条数据-----向数据库添加多条数据，普通插入，不能获取到自增的主键值==批处理
	 * @param objs 传入有数据的  List集合实体对象
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	 * @基于SimpleJdbc 
	 * @使用示例：List<Test> tsets=new ArrayList<Test>();  addsTRANS(tsets,Test.class) ;
	 */
	public BizTransUtil(List<?> add_list,Class<?> add_class){
		this.add_list=add_list;
		this.add_class=add_class;
		this.executeType=OtherUtil.INSERT;
	}
	/**
	 * 操作执行SQL
	 * 删除/更新/添加
	 * @param sql 执行的语句（可以是预编译的sql）
	 * @param params 可变参数
	 * @param executeType 执行类型CommonUtil.DELETE 
	 * @return 影响条数 
	 * @使用示例：String sql=" update test set name=?,birthday=? where id=? ";  executeTRANS(sql,name值,birthday值,id值);或  execute(sql,new Object[]{xx,xx,xx});
	 */
	public BizTransUtil(String execute_sql, Object[] execute_params,String executeType) {
		this.execute_sql=execute_sql;
		this.execute_params=execute_params;
		this.executeType=executeType;
	}
	/**
	 * 操作执行SQL
	 * 删除-----根据主键ID删除
	 * @param id   主键ID 值
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 pkName值
	 * @return 影响行数
	 * @使用示例 delete(188,Test.calss);
	 */
	public BizTransUtil(Object del_id,Class<?> del_obj){
		this.del_id=del_id;
		this.del_obj=del_obj;
		this.executeType=OtherUtil.DELETE;
	}
	/**
	 * 操作执行SQL---该方法待测试
	 * 删除---根据主键ID删除
	 * @param ids   主键ID 值数组
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 pkName值
	 * @return 影响行数
	 * @使用示例 deleteTRANS(new Object[]{x,x,x,x},Test.calss); 该删除方法原理为构建一条删除SQL 语句
	 */
	public BizTransUtil(Object[] del_ids,Class<?> del_obj){
		this.del_ids=del_ids;
		this.del_obj=del_obj;
		this.executeType=OtherUtil.DELETE;
	}
	/**
	 * 操作执行SQL
	 * 删除---根据多个条件删除 
	 * @param map   条件的字段(key)和对应的值(value)
	 * @param obj 传入表对应的 实体对象 类型，并实体有@MyTable 注解的 tableName值
	 * @return 影响行数
	 * 操作执行SQL
	 * 更新--通过map 来进行一些字段值的修改。
	 * @param map 要修改的字段(key)和对应的值(value)，里面必须包含主键key和value
	 * @param obj 要修改的实体类型，或获取实体对象里面的@MyTable注解值
	 * @return 影响条数
	 * @使用示例：Map map=new HashMap();map.put("id", 189);map.put("name","改后了");  updateTRANS(map,Test.calss); 
	 */
	public BizTransUtil(Map<String, Object> map,Class<?> obj,String executeType){
		if(OtherUtil.DELETE.equals(executeType)){
			this.del_map=map;
			this.del_obj=obj;
		}else if(OtherUtil.UPDATE.equals(executeType)){
			this.upd_map=map;
			this.upd_obj=obj;
		}
		this.executeType=executeType;
	}
	
	
	public Object getAdd_obj() {
		return add_obj;
	}
	public void setAdd_obj(Object addObj) {
		add_obj = addObj;
	}
	public List<?> getAdd_list() {
		return add_list;
	}
	public void setAdd_list(List<?> addList) {
		add_list = addList;
	}
	public Class<?> getAdd_class() {
		return add_class;
	}
	public void setAdd_class(Class<?> addClass) {
		add_class = addClass;
	}
	public Object getDel_id() {
		return del_id;
	}
	public void setDel_id(Object delId) {
		del_id = delId;
	}
	public Object[] getDel_ids() {
		return del_ids;
	}
	public void setDel_ids(Object[] delIds) {
		del_ids = delIds;
	}
	public Map<String, Object> getDel_map() {
		return del_map;
	}
	public void setDel_map(Map<String, Object> delMap) {
		del_map = delMap;
	}
	public Class<?> getDel_obj() {
		return del_obj;
	}
	public void setDel_obj(Class<?> delObj) {
		del_obj = delObj;
	}
	public Map<String, Object> getUpd_map() {
		return upd_map;
	}
	public void setUpd_map(Map<String, Object> updMap) {
		upd_map = updMap;
	}
	public Class<?> getUpd_obj() {
		return upd_obj;
	}
	public void setUpd_obj(Class<?> updObj) {
		upd_obj = updObj;
	}
	public String getExecute_sql() {
		return execute_sql;
	}
	public void setExecute_sql(String executeSql) {
		execute_sql = executeSql;
	}
	public Object[] getExecute_params() {
		return execute_params;
	}
	public void setExecute_params(Object[] executeParams) {
		execute_params = executeParams;
	}
	public Object getReturnvalue() {
		return returnvalue;
	}
	public void setReturnvalue(Object returnvalue) {
		this.returnvalue = returnvalue;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getExecuteType() {
		return executeType;
	}
	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	public Boolean getIscase() {
		return iscase;
	}
	public void setIscase(Boolean iscase) {
		this.iscase = iscase;
	}
	public int getWhen() {
		return when;
	}
	public void setWhen(int when) {
		this.when = when;
	}
	public String getErorrmsg() {
		return erorrmsg;
	}
	public void setErorrmsg(String erorrmsg) {
		this.erorrmsg = erorrmsg;
	}
	@Override
	public String toString() {
		return "BizTransUtil对象值 [add_class=" + add_class + ", add_list="
				+ add_list + ", add_obj=" + add_obj + ", del_id=" + del_id
				+ ", del_ids=" + Arrays.toString(del_ids) + ", del_map="
				+ del_map + ", del_obj=" + del_obj + ", executeType="
				+ executeType + ", execute_params="
				+ Arrays.toString(execute_params) + ", execute_sql="
				+ execute_sql + ", returnType=" + returnType + ", returnvalue="
				+ returnvalue + ", upd_map=" + upd_map + ", upd_obj=" + upd_obj
				+ "]";
	}
	
}

<%@ page language="java" contentType="text/html" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-cn">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    
    <script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>

	<title>文件下载</title>
  </head>
 
	<body>
		下载列表
		
    	<table>
    		<c:forEach items="${list }" var="ele">
    			<tr bgcolor="#fff";>
						
						<td>${ele.fki_nm_id }</td>
						<td>${ele.fki_tt_cd }</td>
						<td>${ele.fki_tt_md }</td>
						<td>${ele.fki_st_fn }</td>
						<td>${ele.fki_st_fz }</td>
						<td><a href="${pageContext.request.contextPath}/fileupdown/down/testdown2.do?info=${ele.fki_st_fp }">下载</a></td>
						<td>${ele.fki_st_ft }</td>
						<td>${ele.fki_st_ip }</td>
					</tr>
    		</c:forEach>
    	</table>  
	</body>
	<script>
	</script>
</html>

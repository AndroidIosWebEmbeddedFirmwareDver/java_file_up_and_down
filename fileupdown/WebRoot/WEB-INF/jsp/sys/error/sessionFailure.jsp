<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<base href="http://${header['host']}${pageContext.request.contextPath}/">
    
    <title>重新登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="shortcut icon" href="http://${header['host']}${pageContext.request.contextPath}/favicon.ico" />
  </head>
  
  <body>
   您的帐号需要登录了。 请点此处 
   <a href="power/admin" target="_top">重新登录</a>
   <br/>
   造成的原因可能是管理员正在维护您的角色，或者是有其他人登录了您的帐号。
  </body>
</html>

<%@ page language="java" contentType="text/html" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    
    <script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/fileoperate.js"></script>

	<title>文件上传</title>
  </head>
 
  <body>
      <form action="" enctype="multipart/form-data" method="post">
        <!--   上传用户：<input type="text" name="username"><br/>-->
         上传文件：<input id="file" type="file" name="file"/><br/>
     
        
     </form>
     <input type="button" value="提交" onclick="shangchuan()"/>
   </body>
   <script>
   		function shangchuan(){
   			up();
   		}
   </script>
</html>

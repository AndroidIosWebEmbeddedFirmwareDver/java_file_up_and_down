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

	<title>文件上传</title>
  </head>
 
  <body>
      <form action="${pageContext.request.contextPath}/fileupdown/up/upload.do" enctype="multipart/form-data" method="post">
         上传用户：<input type="text" name="username"><br/>
         上传文件1：<input id="file" type="file" name="file"><br/>
    <!--       
         上传文件2：<input type="file" name="file"><br/>
     -->    
         <input type="submit" value="提交">
     </form>
     <input type="button" value="测试按钮" onclick="upfile()"/>
     <div id="progressNumber"></div>
   </body>
   <script>
   function upfile(callback) {
		var File = document.getElementById("file").files[0];
		
		var fd = new FormData();
		if (File) {
			var filename = File.name;
			fd.append("filename", filename);
			fd.append("file", File);
		}
		
		var xhr = new XMLHttpRequest();
	    xhr.upload.addEventListener("progress", uploadProgress, false);
		xhr.open('POST', "${pageContext.request.contextPath}/fileupdown/up/testupload.do", false);
		
		xhr.onreadystatechange = function() {
			if(xhr.readyState == 4 && xhr.status == 200){  
				alert("上传成功");
	        }else if(xhr.readyState == 4&& xhr.status != 200){
	        	 var jsonError = eval("(" + xhr.responseText + ")");
	        	 var errorType = jsonError.type;
	        	alert("上传失败"); 
		         
	        }
		       
		    };
		    
		xhr.send(fd);
	}
	 
	  function uploadProgress(evt) {
        if (evt.lengthComputable) {
          var percentComplete = Math.round(evt.loaded * 100 / evt.total);
             document.getElementById('progressNumber').innerHTML = '<font color=red>当前进度:'+percentComplete.toString() + '%</font>';
        }
        else {
             document.getElementById('progressNumber').innerHTML = 'unable to compute';
        }
	  }
   </script>
</html>

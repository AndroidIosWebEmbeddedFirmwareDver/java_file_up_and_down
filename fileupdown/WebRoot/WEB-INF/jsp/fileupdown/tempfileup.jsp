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
      <form action="" enctype="multipart/form-data" method="post">
         上传用户：<input type="text" name="username"><br/>
         上传文件1：<input id="file" type="file" name="file"><br/>
    <!--       
         上传文件2：<input type="file" name="file"><br/>
     -->    
         <input type="submit" value="提交">
     </form>
     <%--<input type="button" value="测试按钮" onclick="upfile()"/>
     --%><input type="button" value="测试按钮2" onclick="test()"/>
     <div id="progressNumber"></div>
   </body>
   <script>
   
   var xhr=new XMLHttpRequest();
   var start;
   var end;
   var blob;
   const LENGTH=1024*100;
   var File;
   var switchdata;
   function test(){
	   start=0;
	   end=LENGTH+start;
	   File = document.getElementById("file").files[0];
	   if(!File){
		   alert('请选择文件');
		   return;
	   }
	       
	   upfile();
   }
   
   function upfile() {
	   if(start<File.size){
			var fd = new FormData();
			blob=File.slice(start,end);
			var fr=new FileReader();
			fr.readAsDataURL(blob);
			//fr.readAsBinaryString(blob);
			//var switchdata=fr.result;
			fr.onloadend=function(e){
				switchdata=e.target.result;
				var filename = File.name;
				fd.append("filename", filename);
				fd.append("passstr", switchdata);
				if(start==0){
					fd.append("flag", "0");
				}else{
					fd.append("flag", "1");
				}
				//console.debug(switchdata);
				senddata(fd);
			}
			
			
	   }
		
	}
	
   function senddata(fd){
	   var xhr = new XMLHttpRequest();
	    xhr.upload.addEventListener("progress", uploadProgress, false);
		xhr.open('POST', "${pageContext.request.contextPath}/fileupdown/up/testupload2.do", false);
		
		xhr.onreadystatechange = function() {
			if(xhr.readyState == 4 && xhr.status == 200){  
				//alert("上传成功");
			    var jsonError =xhr.responseText;
			    if(jsonError=="notsucc"){
				    setTimeout('upfile()',1);
			    }else{
					start=end;
				    end=start+LENGTH;
				    setTimeout('upfile()',1);
				    console.debug(jsonError);
			    }
	        }else if(xhr.readyState == 4&& xhr.status != 200){
	        	// var jsonError = eval("(" + xhr.responseText + ")");
	        	// var errorType = jsonError.type;
	        	//alert("上传失败"); 
		         upfile();
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

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
         上传文件：<input id="file" type="file" name="file"><br/>
     
        
     </form>
     <input type="button" value="提交" onclick="test()"/>
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
   var allsize;
   var percent;
   var uuidname="";
   function test(){
	   $.ajax({
		   type:"POST",
		   url:"${pageContext.request.contextPath}/fileupdown/up/getuuidname.do",
		   async:false,
		   success: function(data){
			     uuidname=data;
		   }
	   });
	  
	   start=0;
	   end=LENGTH+start;
	   File = document.getElementById("file").files[0];
	   allsize=File.size;
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
				fd.append("size",allsize);
				fd.append("uuidname",uuidname);
				fd.append("end",end);
				senddata(fd);
			}
	   }
	}
	
   function senddata(fd){
	    var xhr = new XMLHttpRequest();
	   
		xhr.open('POST', "${pageContext.request.contextPath}/fileupdown/up/filesliceup.do", false);
		
		xhr.onreadystatechange = function() {
			if(xhr.readyState == 4 && xhr.status == 200){  
			    var jsonError =xhr.responseText;
			    if(jsonError=="notsucc"){
				    setTimeout('upfile()',1);
			    }else{
					start=end;
				    if(start+LENGTH>allsize){
				    	end=allsize;
				    }else{
					    end=start+LENGTH;
				    }
				    percent=Math.round((end/allsize) * 100);
				    document.getElementById('progressNumber').innerHTML = '<font color=red>当前进度:'+percent.toString() + '%</font>';
				    setTimeout('upfile()',1);
				    if(jsonError=="上传成功"){
				    	alert(jsonError);
				    }
			    }
	        }else if(xhr.readyState == 4&& xhr.status != 200){
	        	
		         upfile();
	        }
		       
		};
		    
		xhr.send(fd);
   } 
   
   </script>
</html>

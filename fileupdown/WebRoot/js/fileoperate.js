const LENGTH=1024*100;//100K片段大小
var fileinfo=new Object();
fileinfo.start=0;//文件起始片段
fileinfo.end=0;//文件结束片段
fileinfo.allsize=0;//文件总大小
fileinfo.uuidname="";//文件uuid编号
fileinfo.percent=0;//上传百分比
fileinfo.switchdata="";//文件分片后片段的base64编码
fileinfo.File=null;//文件

function up(){
	
	
    $.ajax({
	   type:"POST",
	   url:"fileupdown/up/getuuidname.do",
	   async:false,
	   success: function(data){
		     fileinfo.uuidname=data;
	   }
    });
  
    fileinfo.start=0;
    fileinfo.end=LENGTH+fileinfo.start;
    fileinfo.File = document.getElementById("file").files[0];
    fileinfo.allsize=fileinfo.File.size;
    
    if(!fileinfo.File){
	   alert('请选择文件');
	   return;
    }
    upfile();
}
function upfile() {
	  
	   if(fileinfo.start<fileinfo.allsize){
			var fd = new FormData();
			var blob=fileinfo.File.slice(fileinfo.start,fileinfo.end);
			var fr=new FileReader();
			fr.readAsDataURL(blob);
			fr.onloadend=function(e){
				fileinfo.switchdata=e.target.result;
				var filename = fileinfo.File.name;
				fd.append("filename", filename);
				fd.append("passstr", fileinfo.switchdata);
				if(fileinfo.start==0){
					fd.append("flag", "0");
				}else{
					fd.append("flag", "1");
				}
				fd.append("size",fileinfo.allsize);
				fd.append("uuidname",fileinfo.uuidname);
				fd.append("end",fileinfo.end);
				senddata(fd);
			}
	   }
	}
	
function senddata(fd){
	    var xhr = new XMLHttpRequest();
	   
		xhr.open('POST', "fileupdown/up/filesliceup.do", false);
		
		xhr.onreadystatechange = function() {
			if(xhr.readyState == 4 && xhr.status == 200){  
			    var jsonError =xhr.responseText;
			    if(jsonError=="notsucc"){
				    setTimeout('upfile()',1);
			    }else{
			    	fileinfo.start=fileinfo.end;
				    if(fileinfo.start+LENGTH>fileinfo.allsize){
				    	fileinfo.end=fileinfo.allsize;
				    }else{
				    	fileinfo.end=fileinfo.start+LENGTH;
				    }
				    fileinfo.percent=Math.round((fileinfo.end/fileinfo.allsize) * 100);
				    var progressdiv=document.getElementById('progressNumber');
				    if(progressdiv!=null){
				    	document.getElementById('progressNumber').innerHTML = '<font color=red>当前进度:'+fileinfo.percent.toString() + '%</font>';
				    }
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
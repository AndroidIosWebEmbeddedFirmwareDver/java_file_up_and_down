package com.fileupdown.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sun.misc.BASE64Decoder;

import com.lys.sys.mvc.action.SysAction;

@Scope(value = "prototype")
@Controller("FileUpAction")
@RequestMapping(value="/fileupdown/up")
public class FileUpAction extends SysAction{
	@RequestMapping(value="/uploadtest")
	public String uploadtest(){
		return "fileupdown/fileup";
	}
	@RequestMapping(value="/uploadfile")
	public String uploadtest1(){
		return "fileupdown/filesliceupsec";
	}
	
	@RequestMapping(value="/upload")
	public void uploadTRANS(@RequestParam("file") CommonsMultipartFile[] files,HttpServletRequest request, HttpServletResponse response){
		//获取上传文件的保存目录，将上传文件放在web-inf/(时间文件夹)下
		String timefolder=dataFormat(new Date());
		System.out.println(request.getRealPath("/WEB-INF/file/"+timefolder));
		String savePath=request.getRealPath("/WEB-INF/file/"+timefolder);
		//上传时临时生成的目录
		String tempPath = request.getRealPath("/WEB-INF/temp");
		File tmpFile=new File(tempPath);
		if(!tmpFile.exists()){
			//创建临时目录
			tmpFile.mkdirs();
		}
		String message="";
		String ipaddr=request.getRemoteAddr();
		try {
			 for(MultipartFile mf : files){
				 
		        CommonsMultipartFile cf= (CommonsMultipartFile)mf; 
		        FileItem item = cf.getFileItem(); 
		        
				 //如果fileitem中封装的是普通输入项的数据
				 if(item.isFormField()){
					 String name = item.getFieldName();
					//解决普通输入项的数据的中文乱码问题
					 String value = item.getString("UTF-8");
					 System.out.println(name + "=" + value);
				 }else{//如果fileitem中封装的是上传文件
					 String filename = item.getName();
					 System.out.println(filename);
					 if(filename==null || filename.trim().equals("")){
						 continue;
					 }
					//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					 filename = filename.substring(filename.lastIndexOf("\\")+1);
					//得到上传文件的扩展名
					 String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
					 String filefontName=filename.substring(0, filename.lastIndexOf("."));
					 System.out.println("上传的文件的文件名是："+filefontName);
					 System.out.println("上传的文件的扩展名是："+fileExtName);
					 String fileUUIDName=UUID.randomUUID().toString();
					 System.out.println("上传的文件的UUID是："+fileUUIDName);
					 //如果需要做扩展名限制，可以在这里做。
					 InputStream in = item.getInputStream();
					//得到文件保存的名称
					 String saveFilename = fileUUIDName+"."+fileExtName;
					 //保存的真实路径
					 String realSavePath = savePath+"\\"+saveFilename;
					 //相对路径，保存到数据库的路径
					 String relativePath="/WEB-INF/file/"+timefolder+"/"+saveFilename;
					 System.out.println(realSavePath);
					 Date appendDate=new Date();
					 int fileSize=in.available();
					 System.out.println("文件上传者ip："+ipaddr+";文件大小："+fileSize);
					 //数据库操作
					 StringBuffer sb=new StringBuffer();
					 sb.append("insert into aaa_file_keep_info(fki_tt_cd,fki_tt_md,fki_st_fn,fki_st_fz,fki_st_fp,fki_st_ft,fki_st_ip) ");
					 sb.append(" value(?,?,?,?,?,?,?) ");
					 baseBiz.executeTRANS(sb.toString(), new Object[]{appendDate,appendDate,filefontName,fileSize,relativePath,fileExtName,ipaddr});
					 
					 File file = new File(savePath);
					 System.out.println("savePath:"+savePath);
					 System.out.println(file.exists());
					 //如果目录不存在
					 if(!file.exists()){
					 //创建目录
					    file.mkdirs();
					 }
					 FileOutputStream out = new FileOutputStream(realSavePath);
					//创建一个缓冲区
					 byte buffer[] = new byte[1024];
					//判断输入流中的数据是否已经读完的标识
					 int len = 0;
					//循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
					 while((len=in.read(buffer))>0){
						 out.write(buffer, 0, len);
					 }
					 //关闭输入
					 in.close();
					 //关闭输出
					 out.close();
					 message="文件上传成功";
				 }
			 }
			 
		} catch (Exception e) {
			message= "文件上传失败！";
			e.printStackTrace();
		}
		try {
			response.getWriter().print(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/testupload")
	public void testuploadTRANS(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request, HttpServletResponse response){
		String timefolder=dataFormat(new Date());
		System.out.println(request.getRealPath("/WEB-INF/file/"+timefolder));
		String savePath=request.getRealPath("/WEB-INF/file/"+timefolder);
		String tempPath = request.getRealPath("/WEB-INF/temp");
		File tmpFile=new File(tempPath);
		if(!tmpFile.exists()){
		//创建临时目录
				tmpFile.mkdirs();
		}
		String message="";
		String ipaddr=request.getRemoteAddr();
		try {
			 //for(MultipartFile mf : file){
					FileItem item = file.getFileItem(); 
		        
					 InputStream in = item.getInputStream();
					 String filename = item.getName();
					 System.out.println(filename);
					 
					//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					 filename = filename.substring(filename.lastIndexOf("\\")+1);
					//得到上传文件的扩展名
					 String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
					 String filefontName=filename.substring(0, filename.lastIndexOf("."));
					 System.out.println("上传的文件的文件名是："+filefontName);
					 System.out.println("上传的文件的扩展名是："+fileExtName);
					 String fileUUIDName=UUID.randomUUID().toString();
					// System.out.println("上传的文件的UUID是："+fileUUIDName);
					 //如果需要做扩展名限制，可以在这里做。
					// InputStream in = item.getInputStream();
					//得到文件保存的名称
					 String saveFilename = fileUUIDName+"."+fileExtName;
					 //保存的真实路径
					 String realSavePath = savePath+"\\"+saveFilename;
					 //相对路径，保存到数据库的路径
					 String relativePath="/WEB-INF/file/"+timefolder+"/"+saveFilename;
					 System.out.println(realSavePath);
					 Date appendDate=new Date();
					 int fileSize=in.available();
					 //数据库操作
					 StringBuffer sb=new StringBuffer();
					 sb.append("insert into aaa_file_keep_info(fki_tt_cd,fki_tt_md,fki_st_fn,fki_st_fz,fki_st_fp,fki_st_ft,fki_st_ip) ");
					 sb.append(" value(?,?,?,?,?,?,?) ");
					 baseBiz.executeTRANS(sb.toString(), new Object[]{appendDate,appendDate,filefontName,fileSize,relativePath,fileExtName,ipaddr});
					 File myfile = new File(savePath);
					 System.out.println("savePath:"+savePath);
					 System.out.println(myfile.exists());
					 //如果目录不存在
					 if(!myfile.exists()){
					 //创建目录
						 myfile.mkdirs();
					 }
					 FileOutputStream out = new FileOutputStream(realSavePath);
					//创建一个缓冲区
					 byte buffer[] = new byte[1024];
					//判断输入流中的数据是否已经读完的标识
					 int len = 0;
					//循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
					 while((len=in.read(buffer))>0){
						 out.write(buffer, 0, len);
					 }
					 //关闭输入
					 in.close();
					 //关闭输出
					 out.close();
					 message="文件上传成功";
				 //}
			 //}//
			 
		} catch (Exception e) {
			message= "文件上传失败！";
			e.printStackTrace();
		}
		try {
			response.getWriter().print(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/filesliceup")
	public void filesliceup(String passstr,String flag,String filename,String size,String uuidname,String end,HttpServletResponse response){
		 Date appendDate=new Date();
		 String timefolder=dataFormat(appendDate);
		 //System.out.println(request.getRealPath("/WEB-INF/file/"+timefolder));
		 String savePath=request.getRealPath("/WEB-INF/file/"+timefolder);
		 String tempPath = request.getRealPath("/WEB-INF/temp");
		 File tmpFile=new File(tempPath);
		 if(!tmpFile.exists()){
		 //创建临时目录
			 tmpFile.mkdirs();
		 }
		 String message="";
		 String ipaddr=request.getRemoteAddr();
		//得到上传文件的扩展名
		 String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
		//得到文件名（不包括扩展名的部分） 
		 String filefontName=filename.substring(0, filename.lastIndexOf("."));
		// System.out.println("上传的文件的文件名是："+filefontName);
		// System.out.println("上传的文件的扩展名是："+fileExtName);
		 //存到数据库上的文件的名字
		 String saveFilename = uuidname+"."+fileExtName;
		 //文件存放的真实路径
		 String realSavePath = savePath+"\\"+saveFilename;
		 //存到数据库的相对路径
		 String relativePath="/WEB-INF/file/"+timefolder+"/"+saveFilename;
		 String tempstr=passstr.replace(" ", "+");
		 tempstr=tempstr.substring(tempstr.lastIndexOf(",")+1);
		 File file = new File(savePath);
		
		 //如果目录不存在
		 if(!file.exists()){
		 //创建目录
		    file.mkdirs();
		 }
		 if(flag.equals("0")){
			 StringBuffer sb=new StringBuffer();
			 sb.append("insert into aaa_file_keep_info(fki_tt_cd,fki_tt_md,fki_st_fn,fki_st_fz,fki_st_fp,fki_st_ft,fki_st_ip) ");
			 sb.append(" value(?,?,?,?,?,?,?) ");
			 baseBiz.executeTRANS(sb.toString(), new Object[]{appendDate,appendDate,filefontName,size,relativePath,fileExtName,ipaddr});
		 }
		 try {
			 byte[] bf=new BASE64Decoder().decodeBuffer(tempstr);
			 for (int i = 0; i < bf.length; ++i) {  
	              if (bf[i] < 0) {  
                    // 调整异常数据  
                    bf[i] += 256;  
	              }  
	          } 
			 if(flag.equals("0")){
				 
				 FileOutputStream out = new FileOutputStream(realSavePath);
				 out.write(bf);
				 out.flush();
				 out.close();
				 
			 }else{
				 
				 RandomAccessFile randomFile = new RandomAccessFile(realSavePath, "rw");
				 long fileLength = randomFile.length();
				 randomFile.seek(fileLength);
				 randomFile.write(bf);
				 randomFile.close();  
				 
			 }
		} catch (IOException e) {
			// TODO: handle exception
			try {
				response.getWriter().print("notsucc");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		 if(end.equals(size)){
			 try {
				response.getWriter().print("上传成功");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}
	
	@RequestMapping(value="/getuuidname")
	public void getUuidName(HttpServletResponse response){
		String fileUUIDName=UUID.randomUUID().toString();
		try {
			//response.getWriter().println("{\'uuid\':\'"+fileUUIDName+"\'}");
			response.getWriter().print(fileUUIDName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String dataFormat(Date date){
		DateFormat df=DateFormat.getDateInstance();
		String str=df.format(date);
		return str;
	}
}

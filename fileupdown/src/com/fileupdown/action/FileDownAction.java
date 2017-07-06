package com.fileupdown.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fileupdown.bean.Aaa_file_keep_info;
import com.fileupdown.bean.vo.File_keep_info_Vo;
import com.lys.sys.mvc.action.SysAction;


@Scope(value = "prototype")
@Controller("FileDownAction")
@RequestMapping(value="/fileupdown/down")
public class FileDownAction extends SysAction{
	@RequestMapping(value="/downloadtest")
	public String uploadtest(){
//		List<Map<String,Object>> list=baseBiz.queryForList("select * from aaa_file_keep_info");
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT fki_nm_id,DATE_FORMAT(fki_tt_cd,'%Y-%m-%d') fki_tt_cd,DATE_FORMAT(fki_tt_md,'%Y-%m-%d') fki_tt_cd,CONCAT(fki_st_fn,'.',fki_st_ft) fki_st_fn,fki_st_fp FROM aaa_file_keep_info");
		List<File_keep_info_Vo> list=baseBiz.queryForListT(sb.toString(),File_keep_info_Vo.class);
		model.addAttribute("list",list);
		return "fileupdown/filedown";
	}
	
	@RequestMapping(value="/testdown", method = RequestMethod.GET)
	public void testdown(String info,HttpServletRequest request,HttpServletResponse response){
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT CONCAT(fki_st_fn,'.',fki_st_ft) FROM aaa_file_keep_info WHERE fki_st_fp=?");
		String downname=(String)baseBiz.queryForObject(sb.toString(),new Object[]{info}, String.class);
		String filename=info.substring(13);
		String downPath=request.getRealPath("/WEB-INF/file/"+filename);
		//设置响应头，控制浏览器下载该文件
		response.setContentType("application/octet-stream; charset=utf-8"); 
		response.setHeader("Content-Disposition", "attachment; filename=" + downname);
		System.out.println(downPath);
		try {
			//读取要下载的文件，保存到文件输入流
			FileInputStream in = new FileInputStream(downPath);
			//创建输出流
			OutputStream out = response.getOutputStream();
			//创建缓冲区
			byte buffer[] = new byte[1024];
			int len = 0;
			//循环将输入流中的内容读取到缓冲区当中
			while((len=in.read(buffer))>0){
				//输出缓冲区的内容到浏览器，实现文件下载
				out.write(buffer, 0, len);
			}
			//关闭文件输入流
			in.close();
			//关闭输出流
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/testdown2", method = RequestMethod.GET)
	public ResponseEntity<byte[]> testdown2(String info,HttpServletRequest request,HttpServletResponse response) throws IOException{
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT CONCAT(fki_st_fn,'.',fki_st_ft) FROM aaa_file_keep_info WHERE fki_st_fp=?");
		String downname=(String)baseBiz.queryForObject(sb.toString(),new Object[]{info}, String.class);
		String filename=info.substring(13);
		String downPath=request.getRealPath("/WEB-INF/file/"+filename);
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", downname);
		File file=new File(downPath);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
	}
}

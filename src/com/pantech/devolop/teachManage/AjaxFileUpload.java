package com.pantech.devolop.teachManage;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class AjaxFileUpload extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AjaxFileUpload() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");   
		response.setCharacterEncoding("UTF-8");
		System.out.print("开始上传。。。");
//		String realDir = request.getSession().getServletContext().getRealPath("");
//		String contextpath = request.getContextPath();
//		String basePath = request.getScheme() + "://"
//		+ request.getServerName() + ":" + request.getServerPort()
//		+ contextpath + "/";
		//获取配置路径
		String basePath=MyTools.getProp(request, "Base.upLoadPath");
		//D:\Tomcat 7.0\webapps\STUGR /STUGR http://localhost:8080/STUGR/
		//System.out.println(realDir+" "+contextpath+" "+basePath);
		 //JSONArray jal = new JSONArray();
		 JSONArray jal = null;
		try {
		//String filePath = "uploadfiles";
		//String realPath = realDir+"\\"+filePath;
		//判断路径是否存在，不存在则创建
		File dir = new File(basePath);
		if(!dir.isDirectory())
		    dir.mkdir();

		if(ServletFileUpload.isMultipartContent(request)){
			System.out.print("开始上传。。.........。");
			DiskFileItemFactory fac = new DiskFileItemFactory();  
			ServletFileUpload upload = new ServletFileUpload(fac);  
			upload.setHeaderEncoding("utf-8");  
			List fileList = null;  
			fileList = upload.parseRequest(request);  
		    Iterator<FileItem> it = fileList.iterator();  
		    String name = "";   //文件名
		    String extName = "";  //文件后缀名
		    String fileName = "";
			String state="SUCCESS";
		    while(it.hasNext()){
		    	FileItem item = it.next(); 
		    	if (!item.isFormField()) {
		    		name = item.getName();
			        fileName = name;
			        long size = item.getSize(); 
			        
			        if (name == null || name.trim().equals("")) {  
			            continue;  
			        }  
			        //扩展名格式    
			        if (name.lastIndexOf(".") >= 0) {  
			            extName = name.substring(name.lastIndexOf("."));  
			        }  
			        File file = null;
			        String filePath = "";  //文件路径
			        String tempFilePath = ""; //文件临时路径
			        String newname=item.getName();  
			        newname=new Date().getTime()+newname.substring(newname.lastIndexOf("."),newname.length());
			        filePath = basePath + newname ;
				    tempFilePath = "/" + newname ;

			        file = new File(filePath);  
			       
			        //在服务器生成文件
		            File saveFile = new File(filePath);  
			        item.write(saveFile);
			        
			        //判断如果文件是txt文件的话，根据不同编码修改编码为UTF-8，以免预览出现乱码
		            if(".txt".equalsIgnoreCase(extName)){
		            	String code = judgeCharset(new File(filePath));//判断文件编码
		            	//System.out.println("++++++++++++++++++++++++++++++上传文件字符集为："+code);
		            	if(!"UTF-8".equalsIgnoreCase(code)){
		            		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath)),"UTF-8")); 
			            	bw.write(new String(item.getString(code).getBytes("UTF8"),"UTF-8"));
			            	bw.flush();
			            	bw.close();
		            	}
		            }
		            jal = JsonUtil.addJsonParams(jal,"src",newname);
		            jal = JsonUtil.addJsonParams(jal,"filename",item.getName());
				    jal = JsonUtil.addJsonParams(jal,"status","success");
		    	} 
		        
		    }
		    response.setStatus(200);
		   
		    System.out.println(jal.toString());
		    response.getWriter().print(jal.toString());
		}
		}catch(Exception ee) {
			ee.printStackTrace();
		}
		
	}
	public void init() throws ServletException {
		// Put your code here
	}

	//判断文件编码
	public String judgeCharset(File file) {
		String charset = "GBK";
		byte [] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) return charset;
			if (first3Bytes[0] == (byte)0xFF && first3Bytes[1] == (byte)0xFE) {
				charset = "UTF-16LE";
				checked = true;
			}else if(first3Bytes[0] == (byte)0xFE && first3Bytes[1] == (byte)0xFF) {
				charset = "UTF-16BE";
				checked = true;
			}else if(first3Bytes[0] == (byte)0xEF && first3Bytes[1] == (byte)0xBB && first3Bytes[2] == (byte)0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			
			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0)
						break;
					if (0x80<=read && read <= 0xBF) //单独出现BF以下的，也算是GBK
						break;
					if (0xC0<=read && read <= 0xDF) {
						read = bis.read();
						if (0x80<= read && read <= 0xBF)//双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {//也有可能出错，但是几率较小
						read = bis.read();
					if (0x80<= read && read <= 0xBF) {
						read = bis.read();
						if (0x80<= read && read <= 0xBF) {
							charset = "UTF-8";
							break;
						} else
							break;
					} else
						break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return charset;
	}
}
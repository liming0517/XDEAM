package com.pantech.devolop.teachManage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.pantech.base.common.tools.MyTools;
  

  
/** 
 * doc docx格式转换 
 *  
 * @author Administrator 
 *  
 */  
public class Office2SWF {  
    private static final int environment = 1;// 环境 1：windows 2:linux  
    // (只涉及pdf2swf路径问题)  
    private String fileString;  
    private String outputPath = "";//输入路径 ，如果不设置就输出在默认的位置  
    private String fileName;  
    private File pdfFile;  
    private File swfFile;  
    private File docFile;
    private String MSG;
    
	private HttpServletRequest request;
    
    public Office2SWF(HttpServletRequest request, String fileString) {
    	this.request = request;
        init(fileString);  
    }  
   
    /** 
     * 重新设置file 
     *  
     * @param fileString 
     */  
    public void setFile(String fileString) {  
        init(fileString);  
    }  
  
    /** 
     * 初始化 
     *  
     * @param fileString 
     */  
    private void init(String fileString) {  
        this.fileString = fileString;  
        fileName = fileString.substring(0, fileString.lastIndexOf("."));  
        docFile = new File(fileString);  
        pdfFile = new File(fileName + ".pdf");  
        swfFile = new File(fileName + ".swf");  
    } 
    
    /** 
     * 转为PDF 
     *  
     * @param file 
     */  
    private void doc2pdf() throws Exception {  
        if (docFile.exists()) {  
            if (!pdfFile.exists()) {  
                try {
                	OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);  
                    connection.connect();  
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
                    converter.convert(docFile, pdfFile);  
                    // close the connection  
                    connection.disconnect();  
                    System.out.println("****pdf转换成功，PDF输出：" + pdfFile.getPath() + "****");  
                } catch (java.net.ConnectException e) {  
                    e.printStackTrace();  
                    System.out.println("****swf转换器异常，openoffice服务未启动！****");
                    Runtime rt = Runtime.getRuntime();
                    String batPath = MyTools.getProp(request, "Base.openofficeBatPath");
                    java.io.File file = new java.io.File(batPath);  
            	    // 检查BAT文件是否存在  
            	    if (file.isFile() && file.exists()) {
            	    	 rt.exec(batPath);
                         this.setMSG("openoffice服务未启动");
            	    }else{
            	    	this.setMSG("openofficeBat文件不存在");
            	    }
                   
                    throw e;  
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {  
                    e.printStackTrace();  
                    System.out.println("****swf转换器异常，读取转换文件失败****");  
                    throw e;  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    throw e;  
                }  
            } else {  
                System.out.println("****已经转换为pdf，不需要再进行转化****");  
            }  
        } else {  
            System.out.println("****swf转换器异常，需要转换的文档不存在，无法转换****");  
        }  
    }
    
    /**
	 * 转换成 swf
	 */
	private void pdf2swf(boolean flag) throws Exception {
		Runtime r = Runtime.getRuntime();
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				if (environment == 1) {// windows环境处理
					try {
						String swfToolsPath = MyTools.getProp(request, "Base.swfToolsPath");
						Process p = r.exec(swfToolsPath + " "+ pdfFile.getPath() + " -o "+ swfFile.getPath() + " -T 9 -s languagedir=C:/xpdf/xpdf-chinese-simplified");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.out.println("****swf转换成功，文件输出："	+ swfFile.getPath() + "****");
						if (pdfFile.exists() && flag) { //pdf文件存在并且源文件不是PDF格式
							pdfFile.delete();
						}

					} catch (IOException e) {
						e.printStackTrace();
						throw e;
					}
				} else if (environment == 2) {// linux环境处理
					try {
						Process p = r.exec("pdf2swf " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9 -s languagedir=C:/xpdf/xpdf-chinese-simplified");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			} else {
				System.out.println("****pdf不存在,无法转换****");
			}
		} else {
			System.out.println("****swf已经存在不需要转换****");
		}
	}

	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();

		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}

		return buffer.toString();
	}

	/**
	 * 转换主方法
	 */
	public boolean conver() {
		boolean flag = false;
		if (swfFile.exists()) {
			System.out.println("****swf转换器开始工作，该文件已经转换为swf****");
			return true;
		}

		if (environment == 1) {
			System.out.println("****swf转换器开始工作，当前设置运行环境windows****");
		} else {
			System.out.println("****swf转换器开始工作，当前设置运行环境linux****");
		}
		try {
			if (!pdfFile.exists()) {
				flag = true;
				doc2pdf();
				pdf2swf(flag);
			}else{
				pdf2swf(flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (swfFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回文件路径
	 * 
	 * @param s
	 */
	public String getswfPath() {
		if (swfFile.exists()) {
			String tempString = swfFile.getPath();
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} else {
			return "";
		}
	}

	/**
	 * 设置输出路径
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		if (!outputPath.equals("")) {
			String realName = fileName.substring(fileName.lastIndexOf("/"),
					fileName.lastIndexOf("."));
			if (outputPath.charAt(outputPath.length()) == '/') {
				swfFile = new File(outputPath + realName + ".swf");
			} else {
				swfFile = new File(outputPath + realName + ".swf");
			}
		}
	}
	
	/**
	 *  Get&&Set方法
	 */
	public String getFileString() {
		return fileString;
	}

	public void setFileString(String fileString) {
		this.fileString = fileString;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(File pdfFile) {
		this.pdfFile = pdfFile;
	}

	public File getSwfFile() {
		return swfFile;
	}

	public void setSwfFile(File swfFile) {
		this.swfFile = swfFile;
	}

	public File getDocFile() {
		return docFile;
	}

	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}

	public String getOutputPath() {
		return outputPath;
	}
	
	public String getMSG() {
		return MSG;
	}

	public void setMSG(String msg) {
		MSG = msg;
	}

}
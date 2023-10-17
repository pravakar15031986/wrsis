package com.csmpl.wrsis.webportal.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;


@Controller
public class FileDownloadController extends WrsisPortalAbstractController{
	
	private static final Logger LOG = LoggerFactory.getLogger(FileDownloadController.class);

	
	@Value("${apk.server.path}")
	private String apkFilePath;
	
	@GetMapping(value = "/downLoadLatestApk")
	public void downLoadLatestApkFile(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		
		try{
			
			
			File filePath = new File(apkFilePath);
			
			try {
				if(!filePath.exists()){
					filePath.mkdirs();
					LOG.error("FileDownloadController::downLoadLatestApkFile(): APK folder not exist");
					out=response.getWriter();
					out.write("<html>");
					out.write("<head><title>WRSIS</title></head>");
					out.write("<body>");
					out.write("<center><h2 style='color: red;'>File not Found</h2><br><br><a href='javascript:window.close();'>Close</a></center>");
					out.write("</body>");
					out.write("</html>");
					//out.write("<script>alert('File not Found')</script>");
					out.close();
					return;
				}
				
				if((filePath.listFiles()).length==0) {
					LOG.error("FileDownloadController::downLoadLatestApkFile(): APK file not exist");
					out=response.getWriter();
					out.write("<html>");
					out.write("<head><title>WRSIS</title></head>");
					out.write("<body>");
					out.write("<center><h2 style='color: red;'>File not Found</h2><br><br><a href='javascript:window.close();'>Close</a></center>");
					out.write("</body>");
					out.write("</html>");
					//out.write("<script>alert('File not Found')</script>");
					out.close();
					return;
				}
				
				String fileName=filePath.listFiles()[0].getName();
		         
		    
			File file = new File(apkFilePath + File.separator + fileName);
			if(!file.exists()){
				LOG.error("FileDownloadController::downLoadLatestApkFile(): File not found");
				out=response.getWriter();
				out.write("<html>");
				out.write("<head><title>WRSIS</title></head>");
				out.write("<body>");
				out.write("<center><h2 style='color: red;'>File not Found</h2><br><br><a href='javascript:window.close();'>Close</a></center>");
				out.write("</body>");
				out.write("</html>");
				//out.write("<script>alert('File not Found')</script>");
				out.close();
				return;
				
			}
			
	       
			InputStream is = new FileInputStream(file);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment; filename=\""
	                + file.getName() + "\"");
	        OutputStream os = response.getOutputStream();
	        byte[] buffer = new byte[1024];
	        int len;
	        while ((len = is.read(buffer)) != -1) {
	           os.write(buffer, 0, len);
	        }
	        os.flush();
	        os.close();
	        is.close();
	        
			} catch(ArrayIndexOutOfBoundsException e) {
				LOG.error("FileDownloadController::downLoadLatestApkFile():"+e);
				out=response.getWriter();
				out.write("<html>");
				out.write("<head><title>WRSIS</title></head>");
				out.write("<body>");
				out.write("<center><h2 style='color: red;'>File not Found</h2><br><br><a href='javascript:window.close();'>Close</a></center>");
				out.write("</body>");
				out.write("</html>");
				//out.write("<script>alert('File not Found')</script>");
				out.close();
				return;
		      }
  
	}catch(Exception e){
		LOG.error("FileDownloadController::downLoadLatestApkFile():"+e);
		return;
	}	
}
	
	
	

}

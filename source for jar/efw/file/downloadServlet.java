/**** efw3.X Copyright 2016 efwGrp ****/
package efw.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import efw.log.LogManager;

/**
 * ファイルをWEBサーバからクライアントへダウンロードする
 * @author Chang Kejun
 */
@SuppressWarnings("serial")
@WebServlet(name="downloadServlet",urlPatterns={"/downloadServlet"})
public final class downloadServlet extends HttpServlet {
    /**
     * レスポンスの文字セット定数、XMLHttpRequestのデフォルトに合わせ、「UTF-8」に固定。
     */
    private static final String RESPONSE_CHAR_SET="UTF-8";
    private static final String EFW_DOWNLOAD_FILE="efw.download.file";
    private static final String EFW_DOWNLOAD_ZIP="efw.download.zip";
    private static final String EFW_DOWNLOAD_SAVEAS="efw.download.saveas";
    private static final String EFW_DOWNLOAD_DELETEAFTERDOWNLOAD="efw.download.deleteafterdownload";
    private static final String EFW_DOWNLOAD_ZIPBASEPATH="efw.download.zipBasePath";
    /**
     * get方法でファイルをダウンロードする
     * ダウンロード方法などの情報は、セッションから渡す。
	 * @param request HttpServletRequest オブジェクト。
	 * @param response ファイル内容を含む HttpServletResponse オブジェクト 。
	 * @throws ServletException IOException 
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	HttpSession sn=request.getSession();
		String attr_file=(String)sn.getAttribute(EFW_DOWNLOAD_FILE);
		String attr_zip=(String)sn.getAttribute(EFW_DOWNLOAD_ZIP);
		String attr_saveas=(String)sn.getAttribute(EFW_DOWNLOAD_SAVEAS);
		String attr_deleteafterdownload=(String)sn.getAttribute(EFW_DOWNLOAD_DELETEAFTERDOWNLOAD);
		String attr_zipBasePath=(String)sn.getAttribute(EFW_DOWNLOAD_ZIPBASEPATH);
		sn.removeAttribute(EFW_DOWNLOAD_FILE);
		sn.removeAttribute(EFW_DOWNLOAD_ZIP);
		sn.removeAttribute(EFW_DOWNLOAD_SAVEAS);
		sn.removeAttribute(EFW_DOWNLOAD_DELETEAFTERDOWNLOAD);
		sn.removeAttribute(EFW_DOWNLOAD_ZIPBASEPATH);

		String tmp_zip=null;
		String[] tmp_files=null;

		OutputStream os = response.getOutputStream();
		try {
			if(attr_zip!=null&&!"".equals(attr_zip)){
				tmp_files=attr_zip.split("\\|");
				File zipFile=File.createTempFile("efw", "zip",new File(FileManager.getStorageFolder()));
				tmp_zip=zipFile.getName();
				attr_file=zipFile.getName();
				FileManager.zip(tmp_zip, tmp_files, attr_zipBasePath);
				
				if(attr_saveas==null||"".equals(attr_saveas)){
					attr_saveas="attachment.zip";
				}
			}else if(attr_file!=null&&!"".equals(attr_file)){
				if(attr_saveas==null||"".equals(attr_saveas)){
					attr_saveas=FileManager.get(attr_file).getName();//Download file name is the last name in the folder name string.
				}
			}else{
				//do nothing in this case because it is an error in client js.
				return;
			}
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename=\""+java.net.URLEncoder.encode(attr_saveas, RESPONSE_CHAR_SET)+"\"");
			Cookie ck=new Cookie("efw_Downloaded","OK");
			ck.setPath("/");
			response.addCookie(ck);

			FileInputStream hFile = new FileInputStream(FileManager.getStorageFolder()+"/"+attr_file);
			BufferedInputStream bis = new BufferedInputStream(hFile);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = bis.read(buffer)) >= 0) os.write(buffer, 0, len);
			bis.close();
			if("true".equals(attr_deleteafterdownload)){
				if(attr_zip!=null&&!"".equals(attr_zip)){
					for(int i=0;i<tmp_files.length;i++){
						FileManager.remove(FileManager.get(tmp_files[i]));
					}
				}else if(attr_file!=null&&!"".equals(attr_file)){
					FileManager.remove(FileManager.get(attr_file));
				}
			}

		} catch (IOException e) {
			LogManager.ErrorDebug(e.getMessage());
			response.reset();
			response.setCharacterEncoding(RESPONSE_CHAR_SET);//URLEncoder.encodeと関連
			response.setContentType("text/html;charset=UTF-8"); 
			response.getWriter().print("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>");
			response.getWriter().print(e.getMessage());
			response.getWriter().print("</body></html>");
			throw e;
		} finally {
			if (os != null) {
				try {
					os.close();
				}catch (Exception e){
				} finally {
					os = null;
				}
			}
			if(tmp_zip!=null){
				try {
					(new File(FileManager.getStorageFolder()+"/"+tmp_zip)).delete();
				}catch (Exception e){
				} finally {
				}
				
			}
		}
	}
	
}

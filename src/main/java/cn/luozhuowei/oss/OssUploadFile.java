package cn.luozhuowei.oss;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Oss File.
 * 
 * @author zhuowei.luo
 * @date 2017/6/2 
 */  
public class OssUploadFile {
	
	private String directory;
	private String fileName;
	private File file;
	private InputStream input;
	private URL url;
	private Object object;
	private String contentType;
	
	/**
	 * 根据文件上传
	 * 
	 * @param file 将要上传的文件
	 * @param directory 目录
	 * @param fileName 可空
	 * @param contentType 文件类型，可空
	 */
	public OssUploadFile(File file, String directory, String fileName, FileContentType contentType) {
		this(directory, fileName == null ? file.getName() : fileName, contentType);
		this.file = file;
		this.object = this.file;
	}
	
	/**
	 * 根据IO流上传
	 * 
	 * @param input 文件流
	 * @param directory 目录
	 * @param fileName 文件名
	 * @param contentType 文件类型，可空
	 */
	public OssUploadFile(InputStream input, String directory, String fileName, FileContentType contentType) {
		this(directory, fileName, contentType);
		this.input = input;
		this.object = this.input;
	}
	
	/**
	 * 根据网络链接上传
	 * 
	 * @param url 网络链接
	 * @param directory 目录
	 * @param fileName 文件名
	 * @param contentType 文件类型，可空
	 */
	public OssUploadFile(URL url, String directory, String fileName, FileContentType contentType) {
		this(directory, fileName, contentType);
		this.url = url;
		this.object = this.url;
	}
	
	private OssUploadFile(String directory, String fileName, FileContentType contentType) {
		// 目录不能以/开头
		if (directory.startsWith("/") || directory.startsWith("\\")) {
			directory = directory.substring(1);
		}
		// 目录必须以/结尾
		if (!directory.endsWith("/")) {
			directory += "/";
		}
		// 文件名不能包含/，有则替换为_
		if (fileName.indexOf("/") != -1) {
			fileName = fileName.replace("/", "_");
		}
		if (contentType != null) {
			this.contentType = contentType.getValue();
		} else if (fileName != null) {
			this.contentType = FileContentType.getContentType(fileName).getValue();
		}
		this.directory = directory;
		this.fileName = fileName;
	}
	
	public String getOssKey() {
		return directory + fileName;
	}
	
	public Object getObject() {
		return object;
	}
	
	public String getContentType() {
		return contentType;
	}
}

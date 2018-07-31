package cn.luozhuowei.oss;

/**
 * 文件类型.
 * 
 * @author zhuowei.luo
 * @date 2017/8/25
 */
public enum FileContentType {
	
	TEXT_HTML("text/html"),
	IMAGE_JPG("image/jpeg"),
	IMAGE_GIF("image/gif"),
	IMAGE_BMP("image/bmp"),
	PPT("application/vnd.ms-powerpoint"),
	DOC("application/msword"),
	XML("text/xml"),
	TEXT("text/plain"),
	HTML("text/html"),
	VSD("application/vnd.visio"),
	MP4("audio/mp4");
	
	private String value;
	
	private FileContentType(String value){
		this.value=value;
	}
	
	/**
	 * 获取 ContentType
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * 获取文件类型. 
	 */
	public static final FileContentType getContentType(String fileName) {
		if (fileName == null || !fileName.contains(".")){
			return null;
		}
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));
		if ("bmp".equalsIgnoreCase(fileExtension)) {
			return FileContentType.IMAGE_BMP;
		}
		if ("gif".equalsIgnoreCase(fileExtension)) {
			return FileContentType.IMAGE_GIF;
		}
		if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)
				|| "png".equalsIgnoreCase(fileExtension)) {
			return FileContentType.IMAGE_JPG;
		}
		if ("html".equalsIgnoreCase(fileExtension)) {
			return FileContentType.HTML;
		}
		if ("txt".equalsIgnoreCase(fileExtension)) {
			return FileContentType.TEXT;
		}
		if ("vsd".equalsIgnoreCase(fileExtension)) {
			return FileContentType.VSD;
		}
		if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
			return FileContentType.PPT;
		}
		if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
			return FileContentType.DOC;
		}
		if ("xml".equalsIgnoreCase(fileExtension)) {
			return FileContentType.XML;
		}
		if ("mp4".equalsIgnoreCase(fileExtension)) {
			return FileContentType.MP4;
		}
		return FileContentType.TEXT_HTML;
	}
	
}

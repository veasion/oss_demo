package cn.luozhuowei.oss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;

/**
 * Oss Util.
 * 
 * @author zhuowei.luo
 * @date 2017/6/5
 */
public class OssUtil {
	
	private static String ossHttp = "https://";
	private static String endpoint = "oss-cn-beijing.aliyuncs.com";
	
	/**
	 * 服务端获取OssClient
	 * 
	 * @desc 用完记得关闭控制台 ossClient.shutdown();
	 */
	public static OSSClient getOssClient() throws Exception {
		// 获取RAM授权key.
		JSONObject json = ClinetUtil.getOssClientKey();
		ossHttp = json.getString(ClinetUtil.ossHttp);
		endpoint = json.getString(ClinetUtil.endpoint);
		return new OSSClient(ossHttp + endpoint, json.getString(ClinetUtil.accessKeyId),
				json.getString(ClinetUtil.accessKeySecret), json.getString(ClinetUtil.securityToken));
	}
	
	/**
	 * 上传文件
	 * 
	 * @param bucketName bucketName
	 * @param ossListener 进度监听器，可空
	 * @return url链接地址
	 */
	public static String uploadObject(OSSClient ossClient, BucketName bucketName, OssUploadFile ossUploadFile,
			OssListener ossListener) throws Exception {

		String bucket = bucketName.getBucketName();
		Object object = ossUploadFile.getObject();
		String key = ossUploadFile.getOssKey();
		PutObjectRequest putObjectRequest = null;
		ObjectMetadata metdata = new ObjectMetadata();
		metdata.setContentEncoding("UTF-8");
		metdata.setContentType(ossUploadFile.getContentType());

		if (object instanceof File) {
			putObjectRequest = new PutObjectRequest(bucket, key, (File) object);
		} else if (object instanceof InputStream) {
			putObjectRequest = new PutObjectRequest(bucket, key, (InputStream) object, metdata);
		} else if (object instanceof URL) {
			putObjectRequest = new PutObjectRequest(bucket, key, ((URL) object).openStream(), metdata);
		} else {
			throw new Exception("未知文件！");
		}

		// 上传监听
		if (ossListener != null) {
			putObjectRequest.<PutObjectRequest> withProgressListener(ossListener);
		}
		ossClient.putObject(putObjectRequest);
		return getOssFileUrl(bucketName, key);
	}
	
	/**
	 * 创建虚拟文件夹
	 */
	public static boolean createDirectory(OSSClient ossClient, BucketName bucketName, String directory) {
		try {
			if (directory.startsWith("/") || directory.startsWith("\\"))
				directory = directory.substring(1);
			if (!directory.endsWith("/"))
				directory += "/";
			ossClient.putObject(bucketName.getBucketName(), directory, new ByteArrayInputStream(new byte[0]));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 下载文件
	 * 
	 * @param listener 进度监听器
	 * @param key 目录 + 文件名
	 */
	public static InputStream downloadFile(OSSClient ossClient, BucketName bucketName, String key, OssListener listener)
			throws Exception {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName.getBucketName(), key);
		// 下载监听
		if (listener != null) {
			getObjectRequest.<GetObjectRequest> withProgressListener(listener);
		}
		OSSObject object = ossClient.getObject(getObjectRequest);
		if (object != null) {
			return object.getObjectContent();
		} else {
			return null;
		}
	}
	
	/**
	 * 删除文件
	 * @param key 目录 + 文件名
	 */
	public static boolean deleteFile(OSSClient ossClient, BucketName bucketName, String key) {
		try {
			ossClient.deleteObject(bucketName.getBucketName(), key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 根据文件夹获取其下的文件和文件夹.
	 * 
	 * @param ossFilePage 分页
	 * @return getObjectSummaries所有文件，getCommonPrefixes所有文件夹
	 */
	public static ObjectListing listObjects(OSSClient ossClient, BucketName bucketName, String directory,
			OssFilePage ossFilePage) {
		if (ossFilePage == null) {
			ossFilePage = new OssFilePage(100);
		}
		if (directory.startsWith("/") || directory.startsWith("\\")) {
			directory = directory.substring(1);
		}
		if (!directory.endsWith("/")) {
			directory += "/";
		}
		ListObjectsRequest objectListRequest = new ListObjectsRequest();
		objectListRequest.setBucketName(bucketName.getBucketName());
		objectListRequest.setPrefix(directory);
		objectListRequest.setMarker(ossFilePage.getNextMarker());
		objectListRequest.setMaxKeys(ossFilePage.getMaxFile());
		objectListRequest.setDelimiter("/");
		ObjectListing objectList = ossClient.listObjects(objectListRequest);

		ossFilePage.setHasNext(objectList.isTruncated());
		List<String> history = ossFilePage.getHistoryMarker();
		String nextMarker = objectList.getNextMarker();
		if (!history.contains(nextMarker)) {
			history.add(nextMarker);
		}
		
		return objectList;
	}
	
	/** 
	 * 获取文件链接 
	 * @param key 目录 + 文件名
	 */
	public static String getOssFileUrl(BucketName bucketName, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(ossHttp).append(bucketName.getBucketName()).append(".");
		sb.append(endpoint).append("/").append(key);
		return sb.toString();
	}
	
}

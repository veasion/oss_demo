package cn.luozhuowei.test;

import java.io.File;

import com.aliyun.oss.OSSClient;

import cn.luozhuowei.oss.BucketName;
import cn.luozhuowei.oss.OssUploadFile;
import cn.luozhuowei.oss.OssUtil;

/**
 * Oss测试
 * 
 * @author zhuowei.luo
 */
public class OssTest {

	public static void main(String[] args) throws Exception {
		OSSClient ossClient = null;
		try {
			ossClient = OssUtil.getOssClient();
			OssUploadFile ossUpFile = new OssUploadFile(new File("C:\\test.txt"), "test/", null, null);
			String url = OssUtil.uploadObject(ossClient, BucketName.TEST, ossUpFile, null);
			System.out.println(url);
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
			}
		}
	}
	
}

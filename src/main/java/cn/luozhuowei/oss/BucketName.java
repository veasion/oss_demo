package cn.luozhuowei.oss;

/**
 * Oss Bucket Name
 * 
 * @author zhuowei.luo
 * @date 2017/6/2
 */
public enum BucketName {

	TEST("wtk-test-002");

	private String bucketName;

	private BucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketName() {
		return bucketName;
	}

}

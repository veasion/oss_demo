package cn.luozhuowei.oss;

import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

import cn.luozhuowei.common.ConfigUtil;

/**
 * 获取Oss RAM授权key.
 * 
 * @author zhuowei.luo
 * @date 2017/6/5
 */
public class ClinetUtil {

	public final static String ossHttp = "ossHttp";
	public final static String endpoint = "endpoint";
	public final static String accessKeyId = "accessKeyId";
	public final static String accessKeySecret = "accessKeySecret";
	public final static String securityToken = "securityToken";
	public final static String tokenExpireTime = "tokenExpireTime";
	private final static long expireTime = 3600L; // 过期时间

	/**
	 * 获取创建OssClient参数.
	 * 
	 * @return json： {accessKeyId:"xxx",accessKeySecret:"xxx",securityToken:"xxx"}
	 */
	public static JSONObject getOssClientKey() throws Exception {

		String accessKeyId = ConfigUtil.getProperty(ClinetUtil.accessKeyId);
		String accessKeySecret = ConfigUtil.getProperty(ClinetUtil.accessKeySecret);
		String endpoint = ConfigUtil.getProperty(ClinetUtil.endpoint);
		String ossHttp = ConfigUtil.getProperty(ClinetUtil.ossHttp);
		// RoleArn 需要在 RAM 控制台上获取
		String roleArn = ConfigUtil.getProperty("roleArn");
		// RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
		String roleSessionName = ConfigUtil.getProperty("roleSessionName");

		String policy = "{\"Statement\":[{\"Action\":[\"oss:*\"],\"Effect\":\"Allow\",\"Resource\":[\"acs:oss:*:*:*\"]}],\"Version\":\"1\"}";

		final AssumeRoleResponse stsResponse = assumeRole(accessKeyId, accessKeySecret, roleArn, roleSessionName,
				policy, ProtocolType.HTTPS, ClinetUtil.expireTime);
		Map<String, String> respMap = new LinkedHashMap<String, String>();
		respMap.put(ClinetUtil.accessKeyId, stsResponse.getCredentials().getAccessKeyId());
		respMap.put(ClinetUtil.accessKeySecret, stsResponse.getCredentials().getAccessKeySecret());
		respMap.put(ClinetUtil.securityToken, stsResponse.getCredentials().getSecurityToken());
		respMap.put(ClinetUtil.endpoint, endpoint);
		respMap.put(ClinetUtil.ossHttp, ossHttp);
		respMap.put(ClinetUtil.tokenExpireTime, String.valueOf(expireTime));
		return (JSONObject) JSON.toJSON(respMap);
	}

	/** 
	 * 授权 
	 */
	private static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
			String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds)
			throws ClientException {
		// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);

		final AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion("2015-04-01");
		request.setMethod(MethodType.POST);
		request.setProtocol(protocolType);
		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		request.setPolicy(policy);
		request.setDurationSeconds(durationSeconds);
		// 发起请求，并得到response
		return client.getAcsResponse(request);
	}

}

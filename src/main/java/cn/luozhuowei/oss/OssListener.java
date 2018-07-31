package cn.luozhuowei.oss;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;

/**
 * Oss文件上传/下载进度监听器.
 * 
 * @author zhuowei.luo
 * @date 2017/6/5
 */
public class OssListener implements ProgressListener, Serializable {

	private static final long serialVersionUID = 2398996942232853926L;

	private long bytesWritten = 0;
	private long totalBytes = -1;
	private Boolean succed = null;

	@Override
	public void progressChanged(ProgressEvent progressEvent) {
		long bytes = progressEvent.getBytes();
		ProgressEventType eventType = progressEvent.getEventType();
		switch (eventType) {
		case TRANSFER_STARTED_EVENT:
			System.out.println("开始上传...");
			break;

		case REQUEST_CONTENT_LENGTH_EVENT:
			this.totalBytes = bytes;
			break;

		case REQUEST_BYTE_TRANSFER_EVENT:
			this.bytesWritten += bytes;
			break;

		case TRANSFER_COMPLETED_EVENT:
			this.succed = true;
			break;

		case TRANSFER_FAILED_EVENT:
			this.succed = false;
			break;
		default:
			break;
		}
	}

	/**
	 * 是否成功！
	 * 
	 * @desc 没有结果时返回 null， 否则成功 true，失败 false
	 */
	public Boolean isSucced() {
		return succed;
	}

	public Boolean getSucced() {
		return this.isSucced();
	}

	/**
	 * 已上传字节数
	 */
	public long getBytesWritten() {
		return succed != null && succed ? totalBytes : bytesWritten;
	}

	/**
	 * 总字节数
	 */
	public long getTotalBytes() {
		return totalBytes < 0 ? 0 : totalBytes;
	}

	/**
	 * 获取百分比
	 * 
	 * @since 成功100%，失败-1%，进行中xx%
	 */
	public String getBfb() {
		DecimalFormat df = new DecimalFormat("0.0000");
		if (totalBytes < 0)
			return "0%";
		if (succed != null && succed)
			return "100%";
		if (succed != null && !succed)
			return "-1%";
		return df.format(((double) bytesWritten / totalBytes) * 100d) + "%";
	}

	/**
	 * 获取百分比小数(保留四位)
	 */
	public Double getBfbDouble() {
		DecimalFormat df = new DecimalFormat("0.0000");
		if (totalBytes < 0) {
			return 0d;
		}
		if (succed != null && succed) {
			return 100d;
		}
		if (succed != null && !succed) {
			return -1d;
		}
		String temp = df.format(((double) bytesWritten / totalBytes) * 100d);
		return Double.parseDouble(temp);
	}

}
package cn.luozhuowei.oss;

import java.util.ArrayList;
import java.util.List;

/**
 * Oss获取文件分页.
 * 
 * @author zhuowei.luo
 * @date 2017/6/5
 */
public class OssFilePage {

	/**
	 * Oss获取文件分页.
	 * 
	 * @param maxFile 最大文件数
	 */
	public OssFilePage(int maxFile) {
		this.maxFile = maxFile;
		historyMarker.add("");
	}

	/**
	 * 当前页数
	 */
	private int index = 1;

	/**
	 * 最大文件数
	 */
	private int maxFile = 100;

	/**
	 * 是否有下一页
	 */
	private boolean hasNext = false;

	/**
	 * 下一个操作Marker
	 */
	private String nextMarker = "";

	/**
	 * 当前Marker
	 */
	private String useMarker = "";

	/**
	 * 历史页
	 */
	private final List<String> historyMarker = new ArrayList<>();

	/**
	 * 执行上一页/下一页等.
	 */
	public void execute(PageIndex pageIndex) {
		if (historyMarker.isEmpty()) {
			historyMarker.add("");
		}

		if (pageIndex == null) {
			this.nextMarker = "";
		} else {
			this.nextMarker = this.getMarker(pageIndex);
		}

		this.useMarker = this.nextMarker;
	}

	/**
	 * 当前页数
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 是否有下一页
	 */
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * 是否有下一页
	 */
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	/**
	 * 最大文件数
	 */
	public void setMaxFile(int maxFile) {
		this.maxFile = maxFile;
	}

	/**
	 * 最大文件数
	 */
	public int getMaxFile() {
		return maxFile;
	}

	/**
	 * 下一个操作Marker
	 */
	public String getNextMarker() {
		return "".equals(nextMarker) ? null : nextMarker;
	}

	/**
	 * 获取操作的下一个Marker
	 */
	private String getMarker(PageIndex pageIndex) {
		// 第一页
		if (pageIndex == PageIndex.FirstPage) {
			index = 1;
			return "";
		}
		// 当前页
		if (pageIndex == PageIndex.UsePage) {
			return useMarker;
		}
		// 上一页
		if (pageIndex == PageIndex.TopPage) {
			for (int i = 0, l = historyMarker.size(); i < l; i++) {
				String marker = historyMarker.get(i);
				if (i > 0 && (marker == useMarker || useMarker.equals(marker))) {
					index--;
					return historyMarker.get(i - 1);
				}
			}
			index = 1;
			return "";
		}
		// 下一页
		if (pageIndex == PageIndex.NextPage) {
			if (hasNext) {
				for (int i = 0, l = historyMarker.size() - 1; i < l; i++) {
					String marker = historyMarker.get(i);
					if (marker == useMarker || useMarker.equals(marker)) {
						index++;
						return historyMarker.get(i + 1);
					}
				}
				index = 1;
				return "";
			} else
				return useMarker;
		}
		index = 1;
		return "";
	}

	/**
	 * 获取历史记录
	 */
	public List<String> getHistoryMarker() {
		return historyMarker;
	}
}
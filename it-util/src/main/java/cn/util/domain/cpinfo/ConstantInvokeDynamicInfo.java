package cn.util.domain.cpinfo;

public class ConstantInvokeDynamicInfo {
	private int tag;
	private int indexAttr;
	private int indexType;

	public ConstantInvokeDynamicInfo() {
	}

	public ConstantInvokeDynamicInfo(int tag, int indexAttr, int indexType) {
		super();
		this.tag = tag;
		this.indexAttr = indexAttr;
		this.indexType = indexType;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getIndexAttr() {
		return indexAttr;
	}

	public void setIndexAttr(int indexAttr) {
		this.indexAttr = indexAttr;
	}

	public int getIndexType() {
		return indexType;
	}

	public void setIndexType(int indexType) {
		this.indexType = indexType;
	}

	@Override
	public String toString() {
		return "ConstantInvokeDynamicInfo [tag=" + tag + ", indexAttr="
				+ indexAttr + ", indexType=" + indexType + "]";
	}
}

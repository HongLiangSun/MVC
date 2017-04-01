package cn.util.domain.cpinfo;

public class ConstantMethodTypeInfo {
	private int tag;
	private int indexDesc;

	public ConstantMethodTypeInfo() {
	}

	public ConstantMethodTypeInfo(int tag, int indexDesc) {
		super();
		this.tag = tag;
		this.indexDesc = indexDesc;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getIndexDesc() {
		return indexDesc;
	}

	public void setIndexDesc(int indexDesc) {
		this.indexDesc = indexDesc;
	}

	@Override
	public String toString() {
		return "ConstantMethodTypeInfo [tag=" + tag + ", indexDesc="
				+ indexDesc + "]";
	}

}

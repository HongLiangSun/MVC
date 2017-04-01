package cn.util.domain.cpinfo;

public class ConstantClassInfo {
	private int tag;
	private int index;

	public ConstantClassInfo() {
	}

	public ConstantClassInfo(int tag, int index) {
		super();
		this.tag = tag;
		this.index = index;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "ConstantClassInfo [tag=" + tag + ", index=" + index + "]";
	}

}

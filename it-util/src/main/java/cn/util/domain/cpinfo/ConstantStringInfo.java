package cn.util.domain.cpinfo;

public class ConstantStringInfo {
	private int tag;
	private int index;
	
	public ConstantStringInfo() {
	}
	
	public ConstantStringInfo(int tag, int index) {
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
		return "ConstantStringInfo [tag=" + tag + ", index=" + index + "]";
	}

}

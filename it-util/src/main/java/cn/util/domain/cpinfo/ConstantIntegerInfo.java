package cn.util.domain.cpinfo;

public class ConstantIntegerInfo {
	private int tag;
	private String bytes;

	public ConstantIntegerInfo() {
	}

	public ConstantIntegerInfo(int tag, String bytes) {
		super();
		this.tag = tag;
		this.bytes = bytes;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	@Override
	public String toString() {
		return "ConstantIntegerInfo [tag=" + tag + ", bytes=" + bytes + "]";
	}

}

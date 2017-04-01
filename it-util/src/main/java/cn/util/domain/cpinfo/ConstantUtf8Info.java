package cn.util.domain.cpinfo;

public class ConstantUtf8Info {
	private int tag;
	private int length;
	private String bytes;

	public ConstantUtf8Info() {
	}

	public ConstantUtf8Info(int tag, int length, String bytes) {
		super();
		this.tag = tag;
		this.length = length;
		this.bytes = bytes;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	@Override
	public String toString() {
		return "ConstantUtf8Info [tag=" + tag + ", length=" + length
				+ ", bytes=" + bytes + "]";
	}

}

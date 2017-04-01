package cn.util.domain.cpinfo;

public class ConstantMethodrefInfo {
	private int tag;
	private int indexClass;
	private int indexName;
	
	public ConstantMethodrefInfo() {
	}
	public ConstantMethodrefInfo(int tag, int indexClass, int indexName) {
		super();
		this.tag = tag;
		this.indexClass = indexClass;
		this.indexName = indexName;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getIndexClass() {
		return indexClass;
	}
	public void setIndexClass(int indexClass) {
		this.indexClass = indexClass;
	}
	public int getIndexName() {
		return indexName;
	}
	public void setIndexName(int indexName) {
		this.indexName = indexName;
	}
	@Override
	public String toString() {
		return "ConstantMethodrefInfo [tag=" + tag + ", indexClass="
				+ indexClass + ", indexName=" + indexName + "]";
	}
	
}

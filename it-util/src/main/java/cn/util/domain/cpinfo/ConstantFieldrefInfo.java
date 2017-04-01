package cn.util.domain.cpinfo;

public class ConstantFieldrefInfo {
	
	private int tag;
	private int indexCLass;
	private int indexName;
	
	public ConstantFieldrefInfo() {
	}
	public ConstantFieldrefInfo(int tag, int indexCLass, int indexName) {
		super();
		this.tag = tag;
		this.indexCLass = indexCLass;
		this.indexName = indexName;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getIndexCLass() {
		return indexCLass;
	}
	public void setIndexCLass(int indexCLass) {
		this.indexCLass = indexCLass;
	}
	public int getIndexName() {
		return indexName;
	}
	public void setIndexName(int indexName) {
		this.indexName = indexName;
	}
	@Override
	public String toString() {
		return "ConstantFieldrefInfo [tag=" + tag + ", indexCLass="
				+ indexCLass + ", indexName=" + indexName + "]";
	}
	
	
}

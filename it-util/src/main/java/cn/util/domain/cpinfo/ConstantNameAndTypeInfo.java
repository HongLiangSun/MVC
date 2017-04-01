package cn.util.domain.cpinfo;

public class ConstantNameAndTypeInfo {
	
	private int tag;
	private int indexFiled;
	private int indexDesc;
	
	public ConstantNameAndTypeInfo() {
	}
	public ConstantNameAndTypeInfo(int tag, int indexFiled, int indexDesc) {
		super();
		this.tag = tag;
		this.indexFiled = indexFiled;
		this.indexDesc = indexDesc;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getIndexFiled() {
		return indexFiled;
	}
	public void setIndexFiled(int indexFiled) {
		this.indexFiled = indexFiled;
	}
	public int getIndexDesc() {
		return indexDesc;
	}
	public void setIndexDesc(int indexDesc) {
		this.indexDesc = indexDesc;
	}
	@Override
	public String toString() {
		return "ConstantNameAndTypeInfo [tag=" + tag + ", indexFiled="
				+ indexFiled + ", indexDesc=" + indexDesc + "]";
	}
	
}

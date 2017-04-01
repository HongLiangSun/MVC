package cn.util.domain.cpinfo;

public class ConstantMethodHandleInfo {
	private int tag;
	private int indexRefkind;
	private int indexRefIndex;

	public ConstantMethodHandleInfo() {
	}

	public ConstantMethodHandleInfo(int tag, int indexRefkind, int indexRefIndex) {
		super();
		this.tag = tag;
		this.indexRefkind = indexRefkind;
		this.indexRefIndex = indexRefIndex;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getIndexRefkind() {
		return indexRefkind;
	}

	public void setIndexRefkind(int indexRefkind) {
		this.indexRefkind = indexRefkind;
	}

	public int getIndexRefIndex() {
		return indexRefIndex;
	}

	public void setIndexRefIndex(int indexRefIndex) {
		this.indexRefIndex = indexRefIndex;
	}

	@Override
	public String toString() {
		return "ConstantMethodHandleInfo [tag=" + tag + ", indexRefkind="
				+ indexRefkind + ", indexRefIndex=" + indexRefIndex + "]";
	}

}

package cn.util.constant;

public enum AttributeTypeEnum {
	CODE("Code"),//Code
	LOCAL_VARIABLE_TABLE("LocalVariableTable");//LocalVariableTable
	private String code;

	private AttributeTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

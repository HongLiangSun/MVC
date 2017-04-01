package cn.util.constant;

public enum ConstantsPoolTypeEnum {
	CONSTANT_UTF8_INFO(1, "CONSTANT_Utf8_info"),
	CONSTANT_INTEGER_INFO(3,"CONSTANT_Integer_info"),
	CONSTANT_FLOAT_INFO(4,"CONSTANT_Float_info"),
	CONSTANT_LONG_INFO(5,"CONSTANT_Long_info"),
	CONSTANT_DOUBLE_INFO(6,"CONSTANT_Double_info"),
	CONSTANT_CLASS_INFO(7,"CONSTANT_Class_info"),
	CONSTANT_STRING_INFO(8,"CONSTANT_String_info"),
	CONSTANT_FIELDREF_INFO(9,"CONSTANT_Fieldref_info"),
	CONSTANT_METHODREF_INFO(10,"CONSTANT_Methodref_info"),
	CONSTANT_INTERFACE_METHODREF_INFO(11,"CONSTANT_InterfaceMedhodref_info"),
	CONSTANT_NAME_AND_TYPE_INFO(12,"CONSTANT_NameAndType_info"),
	CONSTANT_METHOD_HANDLE_INFO(15,"CONSTANT_MethodHandle_info"),
	CONSTANT_METHOD_TYPE_INFO(16,"CONSTANT_MethodType_info"),
	CONSTANT_INVOKE_DYNAMIC_INFO(18,"CONSTANT_InvokeDynamic_info");
	
	private int type;
	private String name;

	private ConstantsPoolTypeEnum(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}

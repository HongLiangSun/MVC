package cn.util.provide;

import static cn.util.provide.ByteUtils.bin2HexStr;
import static cn.util.provide.ByteUtils.decode;
import static cn.util.provide.ByteUtils.parseStr2Int10;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.util.constant.AttributeTypeEnum;
import cn.util.constant.ConstantsPoolTypeEnum;
import cn.util.domain.cpinfo.ConstantClassInfo;
import cn.util.domain.cpinfo.ConstantDoubleInfo;
import cn.util.domain.cpinfo.ConstantFieldrefInfo;
import cn.util.domain.cpinfo.ConstantFloatInfo;
import cn.util.domain.cpinfo.ConstantIntegerInfo;
import cn.util.domain.cpinfo.ConstantInterfaceMethodrefInfo;
import cn.util.domain.cpinfo.ConstantInvokeDynamicInfo;
import cn.util.domain.cpinfo.ConstantLongInfo;
import cn.util.domain.cpinfo.ConstantMethodHandleInfo;
import cn.util.domain.cpinfo.ConstantMethodTypeInfo;
import cn.util.domain.cpinfo.ConstantMethodrefInfo;
import cn.util.domain.cpinfo.ConstantNameAndTypeInfo;
import cn.util.domain.cpinfo.ConstantStringInfo;
import cn.util.domain.cpinfo.ConstantUtf8Info;


public class ClassUtils {

	private static final int U1 = 1 * 2;
	private static final int U2 = 2 * 2;
	private static final int U3 = 3 * 2;
	private static final int U4 = 4 * 2;
	private static final int U5 = 5 * 2;
	private static final int U6 = 6 * 2;
	private static final int U8 = 8 * 2;
	private static final int U9 = 9 * 2;
	private static final int U10 = 10 * 2;
	private static final int U14 = 14 * 2;
	private static final int U16 = 16 * 2;
	private static final int U18 = 18 * 2;
	
	private static final String FILE_POINT = ".";
	private static final String SEPARATOR = "/";
	private static final String CLASS_END = ".class";
	private static final String FILE_PROTOCOL = "file";
	
	//常量池集合Map Integer -> 元素下表  ; Object 常量池元素
	private static Map<Integer, Object> constanPoolMap = null;
	//方法集合Map String -> 方法名称 ;List<String> 方法的局部变量表
	private static Map<String,List<String>> methodMap = null;
	
	private static Logger logger = Logger.getLogger(ClassUtils.class);

	/**
	 * @从class文件获取方法的本地变量表
	 * @author hongliang.sun
	 * @version 1.0
	 * @param class文件的路径
	 * @return Map<String,List<String>> key:方法名称，value：方法对应的局部变量表
	 */
	public static Map<String,List<String>> getMethodMap(String filePath) throws Exception{
		constanPoolMap = new LinkedHashMap<Integer, Object>();
		methodMap = new HashMap<String,List<String>>();
		String classContent = getClassContent16(filePath);
		//获取常量池里面元素个数
		int constantPoolContent = parseStr2Int10(classContent.substring(U8, U10)) - 1;
		classContent = classContent.substring(U10);
		// 解析cp_info结构
		classContent = parseConstantContent2Map(classContent,constantPoolContent);
		int interfaceContent =  parseStr2Int10(classContent.substring(U6, U8));
		int fieldsCountStartSub = U8 + interfaceContent * U2;
		//接口的个数
		int fieldsConut = parseStr2Int10(classContent.substring(fieldsCountStartSub,fieldsCountStartSub + U2));
		int fieldLength = 0;
		classContent = classContent.substring(fieldsCountStartSub + U2);
		if(fieldsConut > 0){
			fieldLength = parseFieldInfoLength(classContent,fieldsConut);
		}
		int methodsCount = parseStr2Int10(classContent.substring(fieldLength,fieldLength+U2));
		classContent = classContent.substring(fieldLength+U2);
		parseMethodContent2Map(classContent,methodsCount);
		return methodMap;
	}
	
	/**
	 * @从class文件获取方法的本地变量表
	 * @author hongliang.sun
	 * @version 1.0
	 * @param class文件对象
	 * @return Map<String,List<String>> key:方法名称，value：方法对应的局部变量表
	 */
	public static Map<String,List<String>> getMethodMap(File file) throws Exception{
		return getMethodMap(file.getAbsolutePath());
	}
	
	/**
	 * 获取指定包名下的所有的类
	 * 
	 * @throws IOException
	 * 
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> getAllClassesByPackage(String basePackage)throws IOException, ClassNotFoundException {
		Set<Class<?>> result = new LinkedHashSet<Class<?>>();
		Map<File, Class<?>> clazzsMap = getAllClassesMapByPackage(basePackage);
		clazzsMap.forEach((key,value) -> result.add(value));
		return result;
	}
	
	/**
	 *  文件形式获取指定包名下的所有的类。class File对象
	 * 
	 * @throws IOException
	 * 
	 * @throws ClassNotFoundException
	 */
	public static Set<File> getAllClassesFileByPackage(String basePackage)throws IOException, ClassNotFoundException {
		Map<File, Class<?>> clazzsMap = getAllClassesMapByPackage(basePackage);
		return clazzsMap.keySet();
	}
	
	/**
	 *  获取指定包名下的所有的类。
	 * 
	 * @throws IOException
	 * 
	 * @throws ClassNotFoundException
	 */
	public static Map<File,Class<?>> getAllClassesMapByPackage(String basePackage)throws IOException, ClassNotFoundException {
		Map<File,Class<?>> clazzsMap = new LinkedHashMap<File,Class<?>>();
		Enumeration<URL> urls = ClassUtils.class.getClassLoader().getResources(basePackage.replace(FILE_POINT, SEPARATOR));
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url != null) {
				String protocol = url.getProtocol();
				//遍历获取.class所有文件
				if(FILE_PROTOCOL.equals(protocol)){
					String basePath = url.getPath();
					String packageName = basePackage;
					addClass(clazzsMap,basePath,packageName);
				}
			}
		}
		return clazzsMap;
	}
	
	private static void addClass(Map<File,Class<?>> clazzsMap, String basePath, String packageName) throws ClassNotFoundException {
		//过滤出.class结尾以及文件夹的File
		File[] listFiles = new File(basePath).listFiles(pathName -> pathName.getName().endsWith(CLASS_END) || pathName.isDirectory());
		if(!StringUtils.isEmpty(packageName)){
			packageName = packageName + FILE_POINT;
		}
		for(File file : listFiles){
			if(file.getName().endsWith(CLASS_END)){
				String loadClassName =packageName + file.getName().substring(0,file.getName().indexOf(FILE_POINT));
				clazzsMap.put(file,ClassUtils.class.getClassLoader().loadClass(loadClassName));
			}else if(file.isDirectory()){
				String subPackageName = packageName + file.getName();
				String subBasePath = basePath + SEPARATOR + file.getName();
				addClass(clazzsMap, subBasePath , subPackageName);
			}
		}
		
	}
	
	/**
	 * 过滤出含有指定注解的类
	 * @param basePackage
	 * @param annotations
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Set<Class<?>> getAllClassesByPackage(String basePackage,@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) throws ClassNotFoundException, IOException{
		//获取所有Class对象
		Set<Class<?>> clazzs = getAllClassesByPackage(basePackage);
		Set<Class<?>> result = new HashSet<Class<?>>();
		for(Class<?> clazz : clazzs){
			lable:
			for(Class<? extends Annotation> annotation : annotations){
				if(clazz.isAnnotationPresent(annotation)){
					result.add(clazz);
					break lable;
				}
			}
		}
		return result;
	}
	
	private static void parseMethodContent2Map(String classContent,int methodContent) {
		for (int i = 1; i <=  methodContent; ++i) {
			int methodNameIndex = parseStr2Int10(classContent.substring(U2, U4));
			//方法的名称
			String methodName = getStrTextByConstantPool(methodNameIndex);
			//方法的属性数量
			int methodAttrCount = parseStr2Int10(classContent.substring(U6, U8));
			classContent = classContent.substring(U8);
			List<String> localVariableTable = new LinkedList<String>();
			if(methodAttrCount > 0){
				for(int j=0 ; j < methodAttrCount ; ++j){
					int attrUleng = findlocalVariableTableAttr(classContent,localVariableTable);
					classContent = classContent.substring(attrUleng);
				}
			}
			methodMap.put(methodName, localVariableTable);
		}
	}

	private static int findlocalVariableTableAttr(String classContent,List<String> localVariableTable) {
		int methodAttrNameIndex = parseStr2Int10(classContent.substring(0, U2));
		int attrLeng = parseStr2Int10(classContent.substring(U2, U6));
		int attrUleng = attrLeng << 1;
		//属性名
		String attrName = getStrTextByConstantPool(methodAttrNameIndex);
		if(AttributeTypeEnum.CODE.getCode().equals(attrName)){
			int codeLength = parseStr2Int10(classContent.substring(U10,U14));
			int codeUlength = codeLength << 1;
			int exceptionLength = parseStr2Int10(classContent.substring(U14 + codeUlength , U16 + codeUlength));
			int allExceptionLength = U8*exceptionLength;
			int attributeCount = parseStr2Int10(classContent.substring(U16 + codeUlength + allExceptionLength , U18 + codeUlength + allExceptionLength));
			classContent = classContent.substring(U18 + codeUlength + allExceptionLength);
			for(int j = 0 ;j < attributeCount ; ++j){
				int attrTem = findlocalVariableTableAttr(classContent,localVariableTable);
				classContent = classContent.substring(attrTem);
			}
		}else if(AttributeTypeEnum.LOCAL_VARIABLE_TABLE.getCode().equals(attrName)){
			//本地变量表的个数
			int location_variable_table_leng = parseStr2Int10(classContent.substring(U6,U8));
			for(int k = 1 ; k <= location_variable_table_leng ; ++k){
				int paramNameIndex = parseStr2Int10(classContent.substring(U8+(k-1)*U10 + U4,U8+(k-1)*U10 + U6));
				String paramName = getStrTextByConstantPool(paramNameIndex);
				localVariableTable.add(paramName);
			}
		}
		return attrUleng + U6;
	}

	/**
	 * 常量池集合中获取指向方法名称的类型
	 */
	private static String getStrTextByConstantPool(int methodNameIndex) {
		Object object = constanPoolMap.get(methodNameIndex);
		if(object instanceof ConstantUtf8Info){
			return ((ConstantUtf8Info)object).getBytes();
		}else if(object instanceof ConstantClassInfo){
			return getStrTextByConstantPool(((ConstantClassInfo)object).getIndex());
		}else if(object instanceof ConstantStringInfo){
			return getStrTextByConstantPool(((ConstantStringInfo)object).getIndex());
		}else if(object instanceof ConstantMethodTypeInfo){
			return getStrTextByConstantPool(((ConstantMethodTypeInfo)object).getIndexDesc());
		}
		return null;
	}

	/**
	 * 解析field_info结构
	 */
	private static int parseFieldInfoLength(String classContent,int fieldsConut) {
		int result = 0;
		for(int i=0 ; i < fieldsConut ; i++){
			//字段表中的属性个数
			int attributeAccount = parseStr2Int10(classContent.substring(U6,U8));
			int allAttributeULen = 0;
			classContent = classContent.substring(U8);
			for(int j = 0;j < attributeAccount ; j++){
				int attrULen = parseAttrbuteULength(classContent);
				allAttributeULen += attrULen;
				classContent = classContent.substring(attrULen);
			}
			classContent = classContent.substring(allAttributeULen);
			result += U8+allAttributeULen;
		}
		return result;
	}

	/**
	 * 解析属性表的长度
	 */
	private static int parseAttrbuteULength(String classContent) {
		int attrULength = parseStr2Int10(classContent.substring(U2,U6)) << 1;
		return U6 + attrULength;
	}

	/**
	 * 解析cp_info结构 
	 */
	private static String parseConstantContent2Map(String classContent,int constantPoolContent)
			throws Exception {
		for (int i = 1; i <= constantPoolContent; ++i) {
			int constantPoolType = parseStr2Int10(classContent.substring(0, U1));
			int startSub = 0;
			if (constantPoolType == ConstantsPoolTypeEnum.CONSTANT_UTF8_INFO.getType()) {
				int length = parseStr2Int10(classContent.substring(U1, U3));
				int Ulenth = length << 1;
				String decodeTypes = decode(classContent.substring(U3, U3+ Ulenth));
				constanPoolMap.put(i, new ConstantUtf8Info(constantPoolType , length, decodeTypes));
				startSub = U3 + Ulenth;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_INTEGER_INFO.getType()){
				String bytes = classContent.substring(U1, U5);
				constanPoolMap.put(i, new ConstantIntegerInfo(constantPoolType, bytes));
				startSub = U5;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_FLOAT_INFO.getType()){
				String bytes = classContent.substring(U1, U5);
				constanPoolMap.put(i, new ConstantFloatInfo(constantPoolType, bytes));
				startSub = U5;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_LONG_INFO.getType()){
				String bytes = classContent.substring(U1, U9);
				constanPoolMap.put(i, new ConstantLongInfo(constantPoolType, bytes));
				startSub = U9;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_DOUBLE_INFO.getType()){
				String bytes = classContent.substring(U1, U9);
				constanPoolMap.put(i, new ConstantDoubleInfo(constantPoolType, bytes));
				startSub = U9;
			} else if (constantPoolType == ConstantsPoolTypeEnum.CONSTANT_CLASS_INFO.getType()) {
				int index = parseStr2Int10(classContent.substring(U1, U3));
				constanPoolMap.put(i, new ConstantClassInfo(constantPoolType, index));
				startSub = U3;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_STRING_INFO.getType()){
				int index = parseStr2Int10(classContent.substring(U1, U3));
				constanPoolMap.put(i, new ConstantStringInfo(constantPoolType, index));
				startSub = U3;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_FIELDREF_INFO.getType()){
				int indexCLass = parseStr2Int10(classContent.substring(U1, U3));
				int indexName = parseStr2Int10(classContent.substring(U3, U5));
				constanPoolMap.put(i, new ConstantFieldrefInfo(constantPoolType, indexCLass, indexName));
				startSub = U5;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_METHODREF_INFO.getType()){
				int indexCLass = parseStr2Int10(classContent.substring(U1, U3));
				int indexName = parseStr2Int10(classContent.substring(U3, U5));
				constanPoolMap.put(i, new ConstantMethodrefInfo(constantPoolType, indexCLass, indexName));
				startSub = U5;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_INTERFACE_METHODREF_INFO.getType()){
				int indexCLass = parseStr2Int10(classContent.substring(U1, U3));
				int indexName = parseStr2Int10(classContent.substring(U3, U5));
				constanPoolMap.put(i, new ConstantInterfaceMethodrefInfo(constantPoolType, indexCLass, indexName));
				startSub = U5;
			} else if (constantPoolType == ConstantsPoolTypeEnum.CONSTANT_NAME_AND_TYPE_INFO.getType()) {
				int indexFiled = parseStr2Int10(classContent.substring(U1, U3));
				int indexDesc = parseStr2Int10(classContent.substring(U3, U5));
				constanPoolMap.put(i, new ConstantNameAndTypeInfo(constantPoolType, indexFiled, indexDesc));
				startSub = U5;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_METHOD_HANDLE_INFO.getType()){
				int indexFiled = parseStr2Int10(classContent.substring(U1, U2));
				int indexDesc = parseStr2Int10(classContent.substring(U2, U4));
				constanPoolMap.put(i, new ConstantMethodHandleInfo(constantPoolType, indexFiled, indexDesc));
				startSub = U5;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_METHOD_TYPE_INFO.getType()){
				int index = parseStr2Int10(classContent.substring(U1, U3));
				constanPoolMap.put(i, new ConstantMethodTypeInfo(constantPoolType, index));
				startSub = U3;
			} else if(constantPoolType == ConstantsPoolTypeEnum.CONSTANT_INVOKE_DYNAMIC_INFO.getType()){
				int indexAttr = parseStr2Int10(classContent.substring(U1, U3));
				int indexType = parseStr2Int10(classContent.substring(U3, U5));
				constanPoolMap.put(i, new ConstantInvokeDynamicInfo(constantPoolType, indexAttr, indexType));
				startSub = U5;
			} else {
				logger.error("读取文件常量池格式异常。");
				throw new RuntimeException("读取文件常量池格式异常。");
			}
			classContent = classContent.substring(startSub);
		}
		return classContent;
	}
	
	/**
	 * 获取class文件的16进制格式的内容
	 */
	private static String getClassContent16(String filePath){
		FileInputStream fis = null;
		BufferedInputStream bs = null;
		StringBuilder sb = new StringBuilder();
		try {
			fis = new FileInputStream(filePath);
			bs = new BufferedInputStream(fis);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = bs.read(buff)) != -1) {
				sb.append(bin2HexStr(buff, len));
			}
		} catch (Exception e) {
			logger.error("class文件读取异常", e);
		}finally{
			if(bs != null){
				try {
					bs.close();
				} catch (IOException e) {
					logger.error("文件流关闭异常", e);
				}
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("文件流关闭异常", e);
				}
			}
		}
		return sb.toString();
	}
}
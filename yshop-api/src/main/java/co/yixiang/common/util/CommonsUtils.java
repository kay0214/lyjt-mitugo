package co.yixiang.common.util;

import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiasq
 * @version CommonUtils, v0.1 2017/11/11 11:51
 */
@Slf4j
public class CommonsUtils {

	/**
	 * 提供对象属性null转""方法，目前只支持String的属性
	 *
	 * @param obj
	 */
	public static Object convertNullToEmptyString(Object obj) {
		if (obj == null) {
            return obj;
        }
		// 获取对象属性
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			// 跳过静态属性
			String mod = Modifier.toString(field.getModifiers());
			if (mod.indexOf("static") != -1) {
                continue;
            }

			// 得到属性的类名
			String className = field.getType().getSimpleName();
			if ("String".equalsIgnoreCase(className)) {
				try {
					field.setAccessible(true); // 设置些属性是可以访问的
					Object val = field.get(obj);
					if (val == null) {
						field.set(obj, "");
					}
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
				}
			}
		}
		return obj;
	}

	/**
	 * 提供对象属性null转""方法，目前只支持String的属性
	 *
	 * @param colls
	 * @return
	 */
	public static Collection convertNullToEmptyString(Collection colls) {
		for (Object coll : colls) {
			convertNullToEmptyString(coll);
		}
		return colls;
	}

	/**
	 * 如果为空就从collection中移除
	 * @auth framework
	 * @param
	 * @return
	 */
	public static void removeIfNullOrNullStr(Collection collection){
		collection.removeAll(Collections.singleton(null));
		collection.removeAll(Collections.singleton(""));
	}





	/**
	 * list数据循环copyProperties
	 * @param sources
	 * @param clazz
	 * @param <S>
	 * @param <T>
	 * @return
	 * @author zhangyk
	 * @date 2018年6月6日14:57:50
	 */
	public static <S, T> List<T> convertBeanList(List<S> sources, Class<T> clazz) {
		return sources.stream().map(source -> convertBean(source, clazz)).collect(Collectors.toList());
	}

	/**
	 * 简单属性copy
	 * @param s
	 * @param clazz
	 * @param <S>
	 * @param <T>
	 * @author zhangyk
	 * @date 2018年6月6日14:57:50
	 */
	public static <S, T> T convertBean(S s, Class<T> clazz) {
		if (s == null) {
			return null;
		}
		try {
			T t = clazz.newInstance();
			BeanUtils.copyProperties(s, t);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("拷贝属性异常", e);
			throw new RuntimeException("拷贝属性异常");
		}
	}


	/**
	 * 生成二维码
	 *
	 * @param text     跳转内容
	 * @param filePath
	 * @throws WriterException
	 * @throws IOException
	 */
	/*
	public static void generateQRCodeImage(String text, String filePath) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();

		int width = 350;
		int height = 350;
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		// 判断文件是否存在
		File desc = new File(filePath);
		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		if (!desc.exists()) {
			desc.createNewFile();
		}
		Path path = FileSystems.getDefault().getPath(filePath);

		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}

	public static void generateQRCodeImageNew(String text, String filePath,String logoPath) throws IOException {
		// qrcodelogo.png
		QrcodeConfig config = new QrcodeConfig()
				.setPadding(10)
				.setMasterColor("#464646")
				.setLogoBorderColor("#B0C4DE")
				// 眼码样式
				.setCodeEyesPointColor("#438CCB")
				.setCodeEyesBorderColor("#000000")
				.setCodeEyesFormat(QreyesFormat.R2_BORDER_C_POINT);
		new SimpleQrcodeGenerator(config).setLogo(logoPath).generate(text).toFile(filePath);
	}*/

	public static String getMapToString(LinkedHashMap<String,String> map){
		Set<String> keySet = map.keySet();
		//将set集合转换为数组
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		//给数组排序(升序)
//		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyArray.length; i++) {
			// 参数值为空，则不参与签名 这个方法trim()是去空格
			if (map.get(keyArray[i]).trim().length() > 0) {
				sb.append(keyArray[i]).append(":").append(map.get(keyArray[i]).trim());
			}
			if(i != keyArray.length-1){
				sb.append("&");
			}
		}
		return sb.toString();
	}

	/**
	 * 将版本前三位转成九位数字、每位前补0补齐3位
	 *
	 * @param version
	 * @return
	 */
	public static Integer getVersionInt(String version) {
		// 判空校验
		if (StringUtils.isBlank(version)) {
			return 0;
		}
		try {
			String resultStr = "";
			String versionList[] = version.split("\\.");
			DecimalFormat df = new DecimalFormat("000");
			if (null != versionList && versionList.length >= 3) {
				resultStr = resultStr.concat(df.format(Integer.parseInt(versionList[0]))).concat(df.format(Integer.parseInt(versionList[1]))).concat(df.format(Integer.parseInt(versionList[2])));
				return Integer.parseInt(resultStr);
			}
			return 0;
		} catch (Exception e) {
			log.error("版本格式化错误、version:" + version, e);
			return 0;
		}
	}
}

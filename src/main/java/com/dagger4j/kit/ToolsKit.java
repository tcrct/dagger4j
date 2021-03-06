package com.dagger4j.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.dagger4j.exception.IException;
import com.dagger4j.mvc.annotation.Bean;
import com.dagger4j.mvc.dto.HeadDto;
import com.dagger4j.mvc.dto.ReturnDto;
import com.dagger4j.utils.DaggerId;
import com.dagger4j.utils.DataType;
import com.dagger4j.utils.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by laotang on 2017/10/31.
 */
public final class ToolsKit {

    private static Logger logger = LoggerFactory.getLogger(ToolsKit.class);

    private static SerializeConfig jsonConfig = new SerializeConfig();

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public final static Map<String, String> HTML_CHAR = new HashMap<>();

    public static SerializerFeature[] serializerFeatureArray = {
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.NotWriteRootClassName,
            SerializerFeature.WriteDateUseDateFormat
    };

    private static SerializerFeature[] serializerFeatureArray2 = {
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.NotWriteRootClassName
    };

    static {
        HTML_CHAR.put("&", "&#38;");
        HTML_CHAR.put("\"", "&#34;");
        HTML_CHAR.put("<", "&#60;");
        HTML_CHAR.put(">", "&#62;");
        HTML_CHAR.put("'", "&#39;");
        jsonConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj)
            return bool;
        else if (obj == "" || "".equals(obj))
            return bool;
        else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0)
                return bool;
            if ("null".equalsIgnoreCase(obj+""))
                return bool;
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0)
                return bool;
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0)
                return bool;
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0)
                return bool;
        }
        return !bool;
    }

    /**
     * 判断是否JSON字符串
     * @param jsonString
     * @return
     */
    public static boolean isMapJsonString(String jsonString) {
        return jsonString.startsWith("{") && jsonString.endsWith("}");
    }

    /**
     * 判断是否数据JSON字符串
     * @param jsonString
     * @return
     */
    public static boolean isArrayJsonString(String jsonString) {
        return jsonString.startsWith("[") && jsonString.endsWith("]");
    }

    /**
     * 判断是否是数组
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj) {
        return obj instanceof List || obj instanceof Array || obj.getClass().isArray();
    }


    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj, jsonConfig, serializerFeatureArray);
    }

    public static String toJsonString(Object obj, SerializeFilter filter) {
        return JSON.toJSONString(obj, jsonConfig, filter, serializerFeatureArray);
    }

    public static String toJsonString2(Object obj) {
        return JSON.toJSONString(obj, jsonConfig, serializerFeatureArray2);
    }

    public static byte[] toJsonBytes(Object obj) {
        return JSON.toJSONBytes(obj, jsonConfig, serializerFeatureArray);
    }

    public static <T> T jsonParseObject(String jsonText, Class<T> clazz) {
        return JSON.parseObject(jsonText, clazz);
    }

    public static <T> T jsonParseObject(String jsonText, Type typeClazz) {
        return JSON.parseObject(jsonText, typeClazz);
    }

    public static <T> T jsonParseObject(String jsonText, TypeReference<T> typeReference) {
        return JSON.parseObject(jsonText, typeReference);
    }

    public static <T> List<T> jsonParseArray(String jsonText, Class<T> clazz) {
        return JSON.parseArray(jsonText, clazz);
    }

    public static <T> T jsonParseObject(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }

    public static <T> T xmlParseObject(String xmlText, Class<T> clazz) {
        String json = toJsonString(XmlHelper.of(xmlText).toMap());
        return jsonParseObject(json, clazz);
    }

    public static String getCurrentDateString() {
        try {
            return SDF.format(new Date());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 关键字是否存在于map中, 如果存在返回true, 不存在返回false
     *
     * @param key
     * @param map
     * @return
     */
    public static boolean isExist(String key, Map map) {
        if (map.containsKey(key)) {
            return true;
        }
        return false;
    }

    public static DaggerId message2DaggerId(String id) {
        boolean isObjectId = ToolsKit.isValidDaggerId(id);
        if (isObjectId) {
            return new DaggerId(id);
        } else {
            throw new IllegalArgumentException(id + " is not Vaild DaggerId");
        }
    }

    /**
     * 验证是否为MongoDB 的ObjectId
     *
     * @param str
     *            待验证字符串
     * @return  如果是则返回true
     */
    public static boolean isValidDaggerId(String str) {
        if (ToolsKit.isEmpty(str)) {
            return false;
        }
        int len = str.length();
        if (len != 24) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if ((c < '0') || (c > '9')) {
                if ((c < 'a') || (c > 'f')) {
                    if ((c < 'A') || (c > 'F')) {
                        logger.warn(str + " is not DaggerId!!");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 根据format字段，格式化日期
     * @param date          日期
     * @param format        格式化字段
     * @return
     */
    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     *  将字符串日期根据format格式化字段转换成日期类型
     * @param stringDate    字符串日期
     * @param format           格式化日期
     * @return
     */
    public static Date parseDate(String stringDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getFieldName(Field field) {
        JSONField jsonField = field.getAnnotation(JSONField.class);
        return (ToolsKit.isEmpty(jsonField)) ? field.getName() :
                (ToolsKit.isEmpty(jsonField.format()) ? jsonField.name() : jsonField.format());
    }

    public static FileFilter fileFilter(final File dir, final String extName){
        return new FileFilter() {
            public boolean accept(File file) {
                if(".class".equalsIgnoreCase(extName)) {
                    return ( file.isFile() && file.getName().endsWith(extName) ) || file.isDirectory();
                } else if(".jar".equalsIgnoreCase(extName)) {
                    return ( file.isFile() && file.getName().endsWith(extName) ) || file.isFile();
                } else {
                    throw new IllegalArgumentException();
                }
            }
        };
    }


    /**
     * HTML字符转换表
     */
    public static final StringBuilder toHTMLChar(String str) {
        if (str == null) {
            return new StringBuilder();
        }
        StringBuilder sb = new StringBuilder(str);
        char tempChar;
        String tempStr;
        for (int i = 0; i < sb.length(); i++) {
            tempChar = sb.charAt(i);
            if (HTML_CHAR.containsKey(Character.toString(tempChar))) {
                tempStr = HTML_CHAR.get(Character.toString(tempChar));
                sb.replace(i, i + 1, tempStr);
                i += tempStr.length() - 1;
            }
        }
        return sb;
    }

    public static final String htmlChar2String(String htmlChar) {
        if (isEmpty(htmlChar)) return "";
        for (Iterator<Map.Entry<String, String>> it = HTML_CHAR.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            htmlChar = htmlChar.replace(entry.getValue(), entry.getKey());
        }
        return htmlChar;
    }

    /**
     *
     * @param exception
     * @param obj
     * @return
     */
    public static ReturnDto<Object> buildReturnDto(IException exception, Object obj) {
        ReturnDto<Object> dto = new ReturnDto<Object>();
        HeadDto head = new HeadDto();
        if(isEmpty(head)) {
            head = new HeadDto();
        }
        if (ToolsKit.isEmpty(exception)) {
            head.setRet(IException.SUCCESS_CODE);
            head.setMsg(IException.SUCCESS_MESSAGE);
        } else {
            head.setRet(exception.getCode());
            head.setMsg(exception.getMessage());
        }
        dto.setHead(head);
        dto.setData(obj);
        return dto;
    }

    public static boolean isDaggerBean(Class<?> parameterType) {
        if(DataType.isBaseType(parameterType)) {
            return false;
        }
        return parameterType.isAnnotationPresent(Bean.class)
                || DataType.isIdEntityType(parameterType)
                || ObjectKit.newInstance(parameterType) instanceof Serializable;
    }

}

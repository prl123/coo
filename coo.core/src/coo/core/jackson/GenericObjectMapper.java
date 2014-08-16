package coo.core.jackson;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import coo.base.util.DateUtils;

/**
 * 对Jackson的ObjectMapper封装。设置json的输出格式、时间格式及Hibernate懒加载支持。
 */
@SuppressWarnings("serial")
public class GenericObjectMapper extends ObjectMapper {
	/**
	 * 构造方法。
	 */
	public GenericObjectMapper() {
		configure(SerializationFeature.INDENT_OUTPUT, true);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		setDateFormat(new SimpleDateFormat(DateUtils.SECOND));
		Hibernate4Module hibernateModule = new Hibernate4Module();
		hibernateModule.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING,
				true);
		registerModule(hibernateModule);
		registerModule(new JodaModule());
	}

	/**
	 * 注册枚举转换模块
	 * 
	 * @return
	 */
	public GenericObjectMapper registerEnumModule() {
		SimpleModule enumModule = new SimpleModule();
		enumModule.addSerializer(new JsonIEnumSerializer());
		this.registerModule(enumModule);
		return this;
	}
}

package com.baomidou.jobs.core.glue;

import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.glue.impl.SpringGlueFactory;
import groovy.lang.GroovyClassLoader;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * glue factory, product class/object by name
 *
 * @author xuxueli 2016-1-2 20:02:27
 */
public class IGlueFactory {


	private static IGlueFactory glueFactory = new IGlueFactory();
	public static IGlueFactory getInstance(){
		return glueFactory;
	}
	public static void refreshInstance(int type){
		if (type == 0) {
			glueFactory = new IGlueFactory();
		} else if (type == 1) {
			glueFactory = new SpringGlueFactory();
		}
	}


	/**
	 * groovy class loader
	 */
	private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
	private ConcurrentHashMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

	/**
	 * load new instance, prototype
	 *
	 * @param codeSource
	 * @return
	 * @throws Exception
	 */
	public IJobsHandler loadNewInstance(String codeSource) throws Exception{
		if (codeSource!=null && codeSource.trim().length()>0) {
			Class<?> clazz = getCodeSourceClass(codeSource);
			if (clazz != null) {
				Object instance = clazz.newInstance();
				if (instance!=null) {
					if (instance instanceof IJobsHandler) {
						this.injectService(instance);
						return (IJobsHandler) instance;
					} else {
						throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, "
								+ "cannot convert from instance["+ instance.getClass() +"] to IJobsHandler");
					}
				}
			}
		}
		throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, instance is null");
	}
	private Class<?> getCodeSourceClass(String codeSource){
		try {
			// md5
			byte[] md5 = MessageDigest.getInstance("MD5").digest(codeSource.getBytes());
			String md5Str = new BigInteger(1, md5).toString(16);

			Class<?> clazz = CLASS_CACHE.get(md5Str);
			if(clazz == null){
				clazz = groovyClassLoader.parseClass(codeSource);
				CLASS_CACHE.putIfAbsent(md5Str, clazz);
			}
			return clazz;
		} catch (Exception e) {
			return groovyClassLoader.parseClass(codeSource);
		}
	}

	/**
	 * inject service of bean field
	 *
	 * @param instance
	 */
	public void injectService(Object instance) {
		// do something
	}

}

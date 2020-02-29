package com.fucking.great.hotreload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 热加载就这么简单。
 * 需要注意每次都要新new HotReLoader();
 * 否则会报
 * java.lang.LinkageError: loader (instance of  com/fucking/great/hotreload/HotReLoader): attempted  duplicate class definition for name: "com/fucking/great/hotreload/DoHotReloadEntity"
 */
public class HotReLoader extends ClassLoader{
	protected static Log logger = LogFactory.get();
	/*
     * Finds a specified class.
     * The bytecode for that class can be modified.
     */
	public Class loadClass(String name) throws ClassNotFoundException {
		synchronized (getClassLoadingLock(name)) {
			// First, check if the class has already been loaded
			// 查看缓存,防止LinkageError报错.
				Class<?> c = findLoadedClass(name);
				logger.debug("classNmae=[{}]开始执行c[{}]",name,c);
				if (c == null) {
					long t0 = System.nanoTime();
					try {
						if( !name.startsWith("java.") ){
							try {
								// *modify the CtClass object here*
								String internalName = name.replace(".", "/");
								InputStream is = null;
								String name1 = "/" + internalName + ".class";
								Class<?> aClass = this.getClass();
								URL resource = aClass.getResource(name1);
								if( resource != null ){

									String file = resource.getFile();
									Date date = FileUtil.lastModifiedTime(file);
									byte[] b = IoUtil.readBytes(aClass.getResourceAsStream(name1));
									logger.debug("className为[{}]类加载路径为[{}]最后加载时间为[{}]",name,file,date);
									c = defineClass(name, b, 0, b.length);
								}
							} catch (SecurityException e) {

							}catch (Throwable e) {
								logger.debug(e);
							}
						}
						if (c == null){
							c = getSystemClassLoader().loadClass(name);
						}
					} catch (ClassNotFoundException e) {
						// ClassNotFoundException thrown if class not found
						// from the non-null parent class loader
					}

					if (c == null) {
						// If still not found, then invoke findClass in order
						// to find the class.
						long t1 = System.nanoTime();
						c = findClass(name);

						// this is the defining class loader; record the stats
						sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
						sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
						sun.misc.PerfCounter.getFindClasses().increment();
					}
				}
				return c;
			}
	}


}

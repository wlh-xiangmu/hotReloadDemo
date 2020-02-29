package com.fucking.great.ioc;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.fucking.great.hotreload.HotReLoader;

public class TestStartUp {
    public static final Log log = LogFactory.get();
    public static void main(String[] args) throws Exception {
        while (true){
            try {
                HotReLoader classLoader = new HotReLoader();
                Class aClass = classLoader.loadClass("com.fucking.great.ioc.TestIoc");
                ReflectUtil.invoke(aClass.newInstance(),"helloByEntity");
                Thread.sleep(10000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        //        log.debug("{},{}",bean.toString(),TestIoc.class.getDeclaredFields().length);
    }
}

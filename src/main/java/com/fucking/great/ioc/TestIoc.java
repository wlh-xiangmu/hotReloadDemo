package com.fucking.great.ioc;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class TestIoc {
    public static final Log log = LogFactory.get();
    public  void helloByEntity(){
        new DoHotReloadEntity().helloByEntity();;
    }
}

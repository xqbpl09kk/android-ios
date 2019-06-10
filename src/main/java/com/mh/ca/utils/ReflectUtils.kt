package com.mh.ca.utils

import java.lang.reflect.InvocationTargetException

/**
 * Created by tinytitan on 16/4/7.
 */
internal object ReflectUtils {
    
    fun createInstanceFor(klass: Class<out Any>, paraType: Class<out Any>? = null, paraValue: Any? = null): Any? {
        try {
            // 带参数的情况
            if (paraType != null) {
                val constructor = klass.getConstructor(paraType)
                return constructor.newInstance(paraValue)
            }else {
                val constructor = klass.getConstructor()
                return constructor.newInstance()
            }
           
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 创建不了， 就返回空！！
        return null
    }
}

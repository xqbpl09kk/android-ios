package com.mh.anko

/**
 * Created by tinytitan on 2017/6/14.
 * 自定义异常 == 裁剪 Anko 框架中的部分概念， 其实很简单
 */
open class AnkoException(message: String): RuntimeException(message) {
}
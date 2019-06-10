package com.mh.anko.uikit

import android.content.Context

/**
 * Created by tinytitan on 2017/6/27.
 * 统一注入的上下文对象， 一定要在 Application 启动的时候调用，
 * 然后就可以 使用不带参数的 UI控件了
 */
object UIKit {
	
	// 暴露给自定义组件进行， 模拟的上下文
	var context: Context
		get() = _context
		set(value) {}  // 不能设置的属性
	// 内部使用的上下文 == 只能由 CAConfig 通过 init 方式初始化 一次
	internal lateinit var _context: Context
}
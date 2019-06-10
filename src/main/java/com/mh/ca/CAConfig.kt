package com.mh.ca

import android.content.*
import com.mh.anko.*
import com.mh.anko.uikit.*

/**
 * Created by tinytitan on 2017/3/12.
 */
// 设置是否开发模式，开发模式会进行严格的检测
object CAConfig {
	var DEV_MODE: Boolean = false
	
	var designFactor: Float
		get() = Design.factor
		set(value) {
			Design.factor = value
		}
	
	// 初始化上下文，外观模式
	fun init(contxt: Context) {
		UIImage._context = contxt
		Design._context = contxt
		UIKit._context = contxt
	}
}
package com.mh.skt.anko

import android.view.View
import android.widget.RadioGroup

/**
 * Created by tinytitan on 16/10/10.
 * MARK:线性布局 - 扩展到函数上面,进行标准化设置
 */
private val defaultInit: Any.() -> Unit = {}

fun <T: View> T.radioGroup(
		c: android.content.Context?,
		attrs: android.util.AttributeSet?,
		init: RadioGroup.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = RadioGroup.LayoutParams(c!!, attrs!!)
	layoutParams.init()
	this@radioGroup.layoutParams = layoutParams
	return this
}

fun <T: View> T.radioGroup(
		width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		init: RadioGroup.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = RadioGroup.LayoutParams(width, height)
	layoutParams.init()
	this@radioGroup.layoutParams = layoutParams
	return this
}

fun <T: View> T.radioGroup(
		source: android.view.ViewGroup.LayoutParams?,
		init: RadioGroup.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = RadioGroup.LayoutParams(source!!)
	layoutParams.init()
	this@radioGroup.layoutParams = layoutParams
	return this
}

fun <T: View> T.radioGroup(
		source: android.view.ViewGroup.MarginLayoutParams?,
		init: RadioGroup.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = RadioGroup.LayoutParams(source!!)
	layoutParams.init()
	this@radioGroup.layoutParams = layoutParams
	return this
}

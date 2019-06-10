package com.mh.anko.layout

import android.support.constraint.ConstraintLayout
import android.view.View

/**
 * Created by tinytitan on 16/10/10.
 * MARK:相对布局 - 扩展到函数上面,进行标准化设置
 */
private val defaultInit: Any.() -> Unit = {}

fun <T: View> T.constraintLayout(
		c: android.content.Context?,
		attrs: android.util.AttributeSet?,
		init: ConstraintLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = ConstraintLayout.LayoutParams(c!!, attrs!!)
	layoutParams.init()
	layoutParams.validate()
	this@constraintLayout.layoutParams = layoutParams
	return this
}

fun <T: View> T.constraintLayout(
		width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		init: ConstraintLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = ConstraintLayout.LayoutParams(width, height)
	layoutParams.init()
	layoutParams.validate()
	this@constraintLayout.layoutParams = layoutParams
	return this
}

fun <T: View> T.constraintLayout(
		source: android.view.ViewGroup.LayoutParams?,
		init: ConstraintLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = ConstraintLayout.LayoutParams(source!!)
	layoutParams.init()
	layoutParams.validate()
	this@constraintLayout.layoutParams = layoutParams
	return this
}

fun <T: View> T.constraintLayout(
		source: android.view.ViewGroup.MarginLayoutParams?,
		init: ConstraintLayout.LayoutParams.() -> Unit = defaultInit
): T {
	val layoutParams = ConstraintLayout.LayoutParams(source!!)
	layoutParams.init()
	layoutParams.validate()
	this@constraintLayout.layoutParams = layoutParams
	return this
}

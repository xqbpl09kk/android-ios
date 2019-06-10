package com.mh.anko

import android.app.*
import android.content.*
import android.graphics.Color
import android.graphics.drawable.*
import android.os.*
import android.support.v4.app.Fragment
import android.support.v4.content.*
import android.util.*
import android.view.*
import android.widget.*
import com.mh.anko.res.Icon
import com.mh.ca.flow.*
import com.mh.ext.*

/**
 * Created by tinytitan on 2017/6/20.
 * 扩展属性， 显示的设置 文本字号 为 dp
 */
fun TextView.dpSize(size: Float) {
	setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
}


// 基于 iOS 的设计稿件， 与 , 【12号 字体 设计期间 映射到 36】， 实际 dp 解析成 31.5,
// 需要添加调整参数， 这个在工程项目中， 可以重新设置， 调整， 注入上下文参数
object Design {
	
	// 这个一定要注入上下文， 否则解析出来的 尺寸全部是 0
	// 暴露给自定义组件进行， 模拟的上下文
	var context: Context
		get() = _context
		set(value) {}  // 不能设置的属性
	// 内部使用的上下文 == 只能由 CAConfig 通过 init 方式初始化 一次
	internal lateinit var _context: Context
	// var factor = 1.143f
	internal var factor = 1f
}

// 私有的内部扩展属性， 非常霸道
private var EditText._ddpSize: Float by FieldProperty { 0f }

// MARK:  只是为了兼容 iOS
fun EditText.setFontSize(size: Int) {
	this.ddpSize = size.toFloat()
}

// 为了后续的兼容 ，， 哈哈
fun EditText.setFontSize(size: Float) {
	this.ddpSize = size
}


// 设置 设计尺寸
var EditText.ddpSize: Float
	get() = this._ddpSize
	set(value) {
		this._ddpSize = value
		setTextSize(TypedValue.COMPLEX_UNIT_DIP, value * Design.factor)
	}

fun EditText.spSize(size: Float) {
	setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}


// 私有的内部扩展属性， 非常霸道
private var TextView._ddpSize: Float by FieldProperty { 0f }

// MARK:  只是为了兼容 iOS
fun TextView.setFontSize(size: Int) {
	this.ddpSize = size.toFloat()
}

// 为了后续的兼容 ，， 哈哈
fun TextView.setFontSize(size: Float) {
	this.ddpSize = size
}

var  TextView.numberOfLines : Int
	get() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return this.maxLines
		}else {
			return 1 // 默认 返回 1 行
		}
	}
	set(value) {
		if (value > 0) {
			this.maxLines = value
		}else {
		}
	}

// 设置 设计尺寸
var TextView.ddpSize: Float
	get() = this._ddpSize
	set(value) {
		this._ddpSize = value
		setTextSize(TypedValue.COMPLEX_UNIT_DIP, value * Design.factor)
	}

fun TextView.spSize(size: Float) {
	setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

// ==== ddp 辅助计算函数, 包括 CAFlowCollection && Layerouter ==========

fun Context.ddp(value: Int): Int = (value * resources.displayMetrics.density * Design.factor).toInt()
fun Context.ddp(value: Float): Int = (value * resources.displayMetrics.density * Design.factor).toInt()
fun Context.ddp(value: Double): Int = (value * resources.displayMetrics.density * Design.factor).toInt()

fun Activity.ddp(value: Int): Int = (value * resources.displayMetrics.density * Design.factor).toInt()
fun Activity.ddp(value: Float): Int = (value * resources.displayMetrics.density * Design.factor).toInt()

// 页面片段
fun Fragment.ddp(value: Int): Int  = context?.ddp(value) ?: 0
fun Fragment.ddp(value: Float): Int = context?.ddp(value) ?: 0

// 视图
inline fun View.ddp(value: Int): Int = context.ddp(value)
inline fun View.ddp(value: Float): Int = context.ddp(value)

// 动态集合视图(布局指令) == 关联到 collectionview
inline fun CAFlowCollection.ddp(value: Int): Int = containerView?.ddp(value) ?: 0
inline fun CAFlowCollection.ddp(value: Float): Int = containerView?.ddp(value) ?: 0

// 流动布局中，真正需要参数进行布局饿的地方
inline fun Layouter.ddp(value: Int): Int = adapter.containerView?.ddp(value) ?: 0
inline fun Layouter.ddp(value: Float): Int = adapter.containerView?.ddp(value) ?: 0


// ===================== DDP EDN  ===========================


fun View.px2ddp(px: Int): Float = (px.toFloat() / (resources.displayMetrics.density * Design.factor))
fun Context.px2ddp(px: Int): Double = px.toDouble() / resources.displayMetrics.density * Design.factor


// ============= ICON ================
fun Context.icon(init: Icon.() -> Unit): Icon {
	val result = Icon(this)
	result.init()
	return result
}

fun Activity.icon(init: Icon.() -> Unit): Icon {
	val result = Icon(this)
	result.init()
	return result
}

fun Fragment.icon(init: Icon.() -> Unit): Icon {
	val result = Icon(this.context)
	result.init()
	return result
}

fun View.icon(init: Icon.() -> Unit): Icon {
	val result = Icon(this.context)
	result.init()
	return result
}



// ============= Drawable   ==============


fun Context.drawable(id: Int) : Drawable?{
	return resources.getDrawable(id)
}
fun Activity.drawable(id: Int) : Drawable?{
	return resources.getDrawable(id)
}

fun Fragment.drawable(id: Int) : Drawable?{
	return context?.resources?.getDrawable(id)
}

fun View.drawable(id: Int) : Drawable?{
	return context?.resources?.getDrawable(id)
}


// ============= Drawable  END ==============


// ============= COLOR   ==============

// MARK: 获取颜色的外观
inline fun Context.color(id: Int): Int {
	return ContextCompat.getColor(this, id)
}

// MARK: 获取颜色的外观
inline fun Activity.color(id: Int): Int {
	return ContextCompat.getColor(this, id)
}

// MARK: 获取颜色的外观
inline fun Fragment.color(id: Int): Int {
	return context?.let { ContextCompat.getColor(it, id) } ?: Color.TRANSPARENT
}

inline fun View.color(id: Int): Int {
	return ContextCompat.getColor(context, id)
}



// ============= COLOR  END ==============

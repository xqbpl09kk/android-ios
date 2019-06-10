package com.mh.anko.uikit

import android.graphics.drawable.*
import android.os.*
import android.view.*
import com.mh.anko.*
import com.mh.ext.*
import java.lang.ref.*

/**
 * Created by tinytitan on 2017/6/26.
 * 扩展视图 通用的 属性设置， 兼容 iOS
 */

var ViewGroup.clipsToBounds: Boolean
	get() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			return this.clipChildren
		}else {
			return false
		}
	}
	set(value) {
		this.clipChildren = value
	}


// 带入 Layer attached 的 视图对象
class ViewLayer(val view: WeakReference<View>) : GradientDrawable() {
	
	// Kotlin 属性设置
	var backgroundColor: Int  = 0  // 默认都是透明颜色
		set(value) {
			field = value
			this.setColor(value)
		}
	
	
	// 圆角半径, 尺寸 相关的代码 全部需要转换成 ddp
	fun radii(radii: Array<Float>) {
		super.setCornerRadii(radii.map { it.ddp.toFloat() }.toFloatArray())
	}
	
	override fun setCornerRadius(radius: Float) {
		super.setCornerRadius(radius)
		val view = this.view.get()
		if ( view is UIImageView) {
			view.cornerRadius = radius // 自动设置了 图片的圆角， 相当于裁剪了， 霸气！！
		}
	}
	
	// MARK： 封装属性， 支撑整数传递, 都需要 转换成 ddp
	var radius: Int = 0
		set(value) {
			field = value
			this.cornerRadius = value.ddp.toFloat()
		}
	
	private var borderWidth: Int = 0
	
	fun borderWidth(width: Double) {
		this.borderWidth = width.ddp
		this.setStroke(this.borderWidth, this.borderColor)
	}
	
	fun borderWidth(width: Int) {
		this.borderWidth = width.ddp
		this.setStroke(this.borderWidth, this.borderColor)
	}
	
	var borderColor: Int = 0
		set(value) {
			field = value
			this.setStroke(borderWidth, value)
		}
	
}

// 扩展一个存储属性， 并初始化为 空即可, 使用了 layoer 就不能在 额外设置 background 了 否则嗝屁
var View.layer: ViewLayer by FieldProperty {
	val layer = ViewLayer(WeakReference(this))
	this.backgroundDrawable = layer // 设置背景图片为 Layer
	layer
}

// 显示隐藏 控件， Swift 中 只支持现实和吟唱， 不支持 动态的GONE 的概念
var View.isHidden: Boolean
	get() = this.visibility == View.INVISIBLE
	set(value) {
		this.visibility = when(value) {
			true -> View.INVISIBLE
			false -> View.VISIBLE
		}
	}


// 扩展 iconLeft 属性， 统一 button 的 状态处理， 这点很关键， 可以实现 按钮状态 的同步变化
//  这点对于 从语义上 统一 android 和 iOS 对状态的处理都很关键， 类似于 snapkit 的模拟





package com.mh.anko.uikit

import android.content.*
import android.graphics.*
import android.graphics.drawable.*
import android.view.*
import android.widget.*
import com.mh.anko.*
import com.mh.anko.res.*
import com.mh.anko.res.Icon
import com.mh.ca.*

/**
 * Created by tinytitan on 16/10/12.
 * MARK:  自定义的 Button, 往 UIButton上面靠,做扩展和迁移
 */
open class UIPrettyButton(context: Context): Button(context) {
	
	constructor() : this(UIKit._context) // 默认构造函数
	
	// 兼容 iOS 中的 UIPrettyButton
	var iconMargin: Int
		get() = compoundDrawablePadding.px2ddp.toInt()
		set(value) {
			compoundDrawablePadding = value.ddp
		}
	
	init {
		backgroundColor = Color.TRANSPARENT
		// 相当于设置了 两个方向上的 【对齐】 策略
		gravity = Gravity.CENTER
		iconMargin = 0  // 默认 图文紧贴， 很显然这个不符合规范， 肯定要调整的
		minimumHeight = 1
		minimumWidth = 1
		
		numberOfLines = 1
		padding = 0
	}
	
	
	var iconLeft: Drawable?
		get() = compoundDrawables[0]
		set(value) {
			setCompoundDrawables(value, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
		}
	var iconTop: Drawable?
		get() = compoundDrawables[1]
		set(value) {
			setCompoundDrawables(compoundDrawables[0], value, compoundDrawables[2], compoundDrawables[3])
		}
	
	var iconRight: Drawable?
		get() = compoundDrawables[2]
		set(value) {
			setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], value, compoundDrawables[3])
		}
	
	var iconBottom: Drawable?
		get() = compoundDrawables[3]
		set(value) {
			setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], value)
		}
	
	//  兼容 iOS 的语法
	var textColorState: ColorState
		get() = ColorState.zero
		set(value) {
			this.setTextColor(value.build())
		}
	
	// 可以通过图片来实现状态背景, 这个可以有， 但是要 点九图片， 不是很好对应， 还不如直接代码控制 == 图层属性！！
	var backgroundImageState: Icon
		get() = Icon.zero
		set(value) {
			this.backgroundDrawable = value
		}
	
	// 背景色 需要 跟 圆角 等结合使用才是王道， ？？描边怎么办？, 只能解决 简单场景 iOS 中 模拟不了那么多！！
	var backgroundColorState: ColorState
		get() = ColorState.zero
		set(value) {
			
			val radius = this.layer.radius.ddp // 兼容之前的设计
			this.backgroundDrawable = Icon().pipely { it ->
				if (value.normalColor != null) {
					// 自动生成 背景图片， 并附加之前设置的颜色， 只是尽量模拟， 其实是有差距的
					it.normalImage = GradientDrawable().pipely { it ->
						it.cornerRadius = radius.toFloat()
						it.setColor(value.normalColor!!)
					}
				}
				
				if (value.selectedColor != null) {
					// 自动生成 背景图片， 并附加之前设置的颜色， 只是尽量模拟， 其实是有差距的
					it.selectedImage = GradientDrawable().pipely { it ->
						it.cornerRadius = radius.toFloat()
						it.setColor(value.selectedColor!!)
					}
				}
				
				if (value.disabledColor != null) {
					// 自动生成 背景图片， 并附加之前设置的颜色， 只是尽量模拟， 其实是有差距的
					it.disabledImage = GradientDrawable().pipely { it ->
						it.cornerRadius = radius.toFloat()
						it.setColor(value.disabledColor!!)
					}
				}
				
				if (value.focusedColor != null) {
					// 自动生成 背景图片， 并附加之前设置的颜色， 只是尽量模拟， 其实是有差距的
					it.focusedImage = GradientDrawable().pipely { it ->
						it.cornerRadius = radius.toFloat()
						it.setColor(value.focusedColor!!)
					}
				}
			}
		}
	
	
	// 设置 水平 和 垂直 方向上面的对齐策略
	var contentVerticalAlignment: UIControlContentVerticalAlignment
			= UIControlContentVerticalAlignment.center
		set(value) {
			field = value
			updateGravity()
		}
	
	private fun updateGravity() {
		var gravity = when(contentHorizontalAlignment) {
			UIControlContentHorizontalAlignment.center -> Gravity.CENTER_HORIZONTAL
			UIControlContentHorizontalAlignment.left -> Gravity.LEFT
			UIControlContentHorizontalAlignment.right -> Gravity.RIGHT
			UIControlContentHorizontalAlignment.fill -> Gravity.FILL_HORIZONTAL
		}
		
		gravity = gravity or when(contentVerticalAlignment) {
			UIControlContentVerticalAlignment.center -> Gravity.CENTER_VERTICAL
			UIControlContentVerticalAlignment.top -> Gravity.TOP
			UIControlContentVerticalAlignment.bottom -> Gravity.BOTTOM
			UIControlContentVerticalAlignment.fill -> Gravity.FILL_VERTICAL
		}
		
		this.gravity = gravity //  给红心设置 重力效果
	}
	
	var contentHorizontalAlignment: UIControlContentHorizontalAlignment
			= UIControlContentHorizontalAlignment.center
		set(value) {
			field = value
			updateGravity()
		}
	
	
	
}


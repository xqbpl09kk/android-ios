package com.mh.anko.uikit

import android.content.*
import android.graphics.drawable.*
import android.view.*
import android.widget.*
import com.mh.anko.*

/**
 * Created by tinytitan on 16/10/12.
 * MARK: 目前纯粹就是为了 "兼容"iOS的风格
 */

class  UILabel(context: Context):TextView(context) {
	
	init {
		this.padding = 0
		this.gravity = Gravity.CENTER_VERTICAL // 默认垂直居中， 兼容iOS
	}
	
	constructor():this(UIKit.context) // 默认构造函数
	
	var font: UIFont
		get() = UIFont.zere
		set(value) {
			when (value.isFakeBold) {
				true -> {
					this.paint.isFakeBoldText = true
					this.ddpSize = value.size
				}
				false -> {
					this.paint.isFakeBoldText = false
					this.ddpSize = value.size
				}
			}
		}
	
	// 就是为了模拟 而已
	var alignment: NSTextAlignment
		get() = NSTextAlignment.left // 返回一个默认值即可, 肯定不会调用这这属性
		set(value) {
			val gravity = when (value) {
				NSTextAlignment.left -> Gravity.LEFT
				NSTextAlignment.right -> Gravity.RIGHT
				NSTextAlignment.center -> Gravity.CENTER
				else -> Gravity.LEFT
			}
			this.gravity = Gravity.CENTER_VERTICAL or gravity
		}
	
	// 扩展一些 基本属性
	var iconMargin: Int
		get() = compoundDrawablePadding
		set(value) {
			compoundDrawablePadding = value.ddp
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
}
package com.mh.anko.res

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import com.mh.anko.uikit.*

/**
 * Created by tinytitan on 2016/10/18.
 * MARK: 扩展StateListDrawable,实现 按钮 ICON的动态创建,创建者模式
 */
class Icon(val context: Context?): StateListDrawable() {
	constructor():this(UIKit.context) // 默认构造函数
	
	companion object {
		val zero = Icon()
		private val transparentDrawable = GradientDrawable()
	}
	private fun setImage(drawable: Drawable?, state: Array<UIControlState> = emptyArray()) {
		val stateSet = state.map(UIControlState::value).toIntArray()
		addState(stateSet, drawable)
	}
	// 设置尺寸
	fun setSize(width: Int, height: Int? = null): Icon {
		setBounds(0,0,width.ddp,height?.ddp ?: width.ddp)
		return this
	}

	// MARK: 获取资源图片
	private fun drawable(id: Int?) : Drawable?{
		if(id != null) {
			return context?.resources?.getDrawable(id)
		}
		return transparentDrawable
	}
	// MARK: 设置 各种状态下的图标
	fun setImage(id: Int? = null, state: Array<UIControlState> = emptyArray()): Icon {
		setImage(drawable(id), state)
		return this
	}

	// MARK: 快捷方式 - 设置选中状态图标， 做成属性的形式， 只能设置， 不能获取
	fun setSelectedImage(id: Int? = null): Icon {
		setImage(id, arrayOf(UIControlState.selected))
		return this
	}
	
	
	
	// 为后续 兼容做准备
	fun setSelectedImage(image: Drawable? = null): Icon {
		setImage(image, arrayOf(UIControlState.selected))
		return this
	}

	// MARK: 快捷方式 - 设置激活状态图标
	fun setFocusedImage(id: Int? = null): Icon {
		setImage(id, arrayOf(UIControlState.focused))
		return this
	}

	// MARK: 快捷方式 - 设置正常激活状态图标
	fun setNormalImage(id: Int? = null): Icon {
		setImage(id, arrayOf(UIControlState.enabled))
		return this
	}

	// MARK: 快捷方式 - 设置未激活状态图标
	fun setDisabledImage(id: Int? = null): Icon {
		setImage(id, arrayOf(UIControlState.disabled))
		return this
	}
	
	var normalImage: Drawable?
		get() = transparentDrawable
		set(value) {
			setImage(value, arrayOf(UIControlState.enabled))
		}
	
	var selectedImage: Drawable?
		get() = transparentDrawable
		set(value) {
			setImage(value, arrayOf(UIControlState.selected))
		}
	
	var focusedImage: Drawable?
		get() = transparentDrawable
		set(value) {
			setImage(value, arrayOf(UIControlState.focused))
		}
	
	var disabledImage: Drawable?
		get() = transparentDrawable
		set(value) {
			setImage(value, arrayOf(UIControlState.disabled))
		}
	
}
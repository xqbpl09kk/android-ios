package com.mh.anko.res

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import java.util.*

/**
 * Created by tinytitan on 2016/10/18.
 * MARK: ColorStateList,实现 一个 颜色 Selector的创建器
 */
class ColorState() {
	
	companion object {
		val zero = ColorState()
	}
	
	
	private val states: MutableList<IntArray> = ArrayList<IntArray>()
	private val colors: MutableList<Int> = ArrayList<Int>()

	// MARK: 设置 各种状态下的颜色
	private fun setColor(color: Int, state: Array<UIControlState> = emptyArray()): ColorState {
		val stateSet = state.map(UIControlState::value).toIntArray()
		states.add(stateSet)
		colors.add(color)
		return this
	}
	var selectedColor: Int? = null
	
	var normalColor: Int? = null
		
	var focusedColor: Int? = null
	
	var disabledColor: Int? = null

	fun build(): ColorStateList {
		
		if (selectedColor != null) {
			setColor(selectedColor!!, arrayOf(UIControlState.selected))
		}
		
		if (focusedColor != null) {
			setColor(focusedColor!!, arrayOf(UIControlState.focused))
		}
		
		if (disabledColor != null) {
			setColor(disabledColor!!, arrayOf(UIControlState.disabled))
		}
		
		// 一定要放到后面来处理
		if (normalColor != null) {
			setColor(normalColor!!, arrayOf(UIControlState.enabled))
			
		}
		
		return ColorStateList(states.toTypedArray(), colors.toIntArray())
	}
}
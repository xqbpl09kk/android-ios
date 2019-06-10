package com.mh.anko.uikit

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.mh.ca.*


class UISegmentedControl(context: Context, orientation: Int = LinearLayout.HORIZONTAL, radius: Int = 5) : RadioGroup(context) {
	
	constructor(orientation: Int = LinearLayout.HORIZONTAL, radius: Int = 5):this(UIKit.context, orientation, radius) // 默认构造函数
	
	internal enum class RadioButtonType {
		Left,
		Middle,
		Right
	}

	// =================  one piece =================


	// =================  params =================

	var mainColor: Int = 0

	var subColor: Int = 0

	private var cornerRadius: Float = 0.toFloat()

	var strokeWidth: Int = 0

	// =================  some values =================

	private var leftRbRadiusArray: FloatArray? = null

	private var rightRbRadiusArray: FloatArray? = null
	
	
	init {
		val resources = resources
		mainColor = resources.getColor(android.R.color.holo_blue_light)
		subColor = resources.getColor(android.R.color.white)
		
		// 设置圆角 == 这个要转换成 px 模式
		cornerRadius = radius.ddp.toFloat()

		// 默认的方向设置
		this.orientation = orientation
		if (orientation == LinearLayout.HORIZONTAL) {
			leftRbRadiusArray = floatArrayOf(cornerRadius, cornerRadius, 0f, 0f, 0f, 0f, cornerRadius, cornerRadius)
			rightRbRadiusArray = floatArrayOf(0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f)
		} else {
			leftRbRadiusArray = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f)
			rightRbRadiusArray = floatArrayOf(0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
		}
		strokeWidth = 2
	}
	
	// 一定要等到 child 添加完成后，才能使用这个方法
	fun beautifyRadioGroup() {

		val count = childCount
		
		if (count == 0) {
			return
		}
		if (count == 1) {
			Log.e("hehe", "You should use button instead of this")
			return
		}

		for (i in 0..count - 1 - 1) {
			val child = getChildAt(i)
			val initParams = child.layoutParams as LayoutParams
			if (orientation == LinearLayout.HORIZONTAL) {
				initParams.setMargins(0, 0, -strokeWidth, 0)     // smart
			} else {
				initParams.setMargins(0, 0, 0, -strokeWidth)
			}
			if (i == 0) {
				addSelectEffect(child, RadioButtonType.Left)
			} else {
				addSelectEffect(child, RadioButtonType.Middle)
			}
		}

		addSelectEffect(getChildAt(count - 1), RadioButtonType.Right)
	}

	private fun addSelectEffect(view: View, radioButtonType: RadioButtonType) {

		val radioButton = view as RadioButton
		addTextSelectEffect(radioButton)
		addBackgroundSelectEffect(radioButton, radioButtonType)
	}

	private fun addTextSelectEffect(radioButton: RadioButton) {

		val colorStateList = ColorStateList(
				arrayOf(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
				intArrayOf(mainColor, subColor)
		)
		radioButton.setTextColor(colorStateList)
	}

	private fun addBackgroundSelectEffect(radioButton: RadioButton, radioButtonType: RadioButtonType) {

		val checkedDrawable = GradientDrawable()
		checkedDrawable.setColor(mainColor)

		val uncheckedDrawable = GradientDrawable()
		uncheckedDrawable.setColor(subColor)
		uncheckedDrawable.setStroke(strokeWidth, mainColor)

		when (radioButtonType) {
			RadioButtonType.Left -> {
				checkedDrawable.setCornerRadii(leftRbRadiusArray)
				uncheckedDrawable.setCornerRadii(leftRbRadiusArray)
			}
			RadioButtonType.Right -> {
				checkedDrawable.setCornerRadii(rightRbRadiusArray)
				uncheckedDrawable.setCornerRadii(rightRbRadiusArray)
			}
		}

		val stateListDrawable = StateListDrawable()
		stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), checkedDrawable)
		stateListDrawable.addState(intArrayOf(-android.R.attr.state_checked), uncheckedDrawable)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			radioButton.background = stateListDrawable
		} else {
			radioButton.setBackgroundDrawable(stateListDrawable)
		}
	}
	
	// 设置分段选取数值发生变化的 监听器
	var valueChangeListener: ((value: Int) -> Unit)?
		get() = null
		set(listener) {
			// 转化成序号
			this.setOnCheckedChangeListener({group, id ->
				for (i in 0..(group.childCount -1)) {
					val child = group.getChildAt(i)
					if (child.id == id) {
						listener?.invoke(i)
						return@setOnCheckedChangeListener
					}
				}
			})
		}
	
	
	
}


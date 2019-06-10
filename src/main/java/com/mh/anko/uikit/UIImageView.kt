package com.mh.anko.uikit

import android.content.*
import android.widget.*
import com.mh.anko.uikit.roundedimageview.RoundedImageView

/**
 * Created by tinytitan on 16/10/12.
 * MARK: 目前纯粹就是为了 "兼容"iOS的风格， 为了进一步的兼容 swift，
 * 提供 上下文自动【统一】注入，查询的版本，非常的霸道！！！ ， 多态， 采用工厂模式封装即可
 */
class UIImageView(context: Context): RoundedImageView(context) {
	constructor():this(UIKit._context) // 默认构造函数
}

// 适配 android 的 scaleType 有些不常用的 适配不到
var ImageView.contentMode: UIViewContentMode
	get() {
		return when (scaleType) {
			ImageView.ScaleType.CENTER_CROP -> UIViewContentMode.scaleAspectFill
			ImageView.ScaleType.CENTER_INSIDE -> UIViewContentMode.center
			ImageView.ScaleType.FIT_CENTER -> UIViewContentMode.scaleAspectFit
			ImageView.ScaleType.FIT_XY -> UIViewContentMode.scaleToFill
			ImageView.ScaleType.FIT_END -> UIViewContentMode.right
			ImageView.ScaleType.FIT_START -> UIViewContentMode.left
			else -> UIViewContentMode.scaleAspectFill // 填充满
		}
	}
	set(value) {
		val type = when (value) {
			UIViewContentMode.scaleAspectFill -> ImageView.ScaleType.CENTER_CROP
			UIViewContentMode.center -> ImageView.ScaleType.CENTER_INSIDE
			UIViewContentMode.scaleAspectFit -> ImageView.ScaleType.FIT_CENTER
			UIViewContentMode.scaleToFill -> ImageView.ScaleType.FIT_XY
			UIViewContentMode.right -> ImageView.ScaleType.FIT_END
			UIViewContentMode.left -> ImageView.ScaleType.FIT_START
			else -> ImageView.ScaleType.CENTER_CROP // 填充满
		}
		scaleType = type
	}

// MARK:  兼容 iOS 的属性而已
var ImageView.clipsToBounds: Boolean
	get() = true
	set(value) {}
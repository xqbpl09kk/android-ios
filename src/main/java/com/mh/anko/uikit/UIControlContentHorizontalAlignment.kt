package com.mh.anko.uikit

/**
 * Created by tinytitan on 2017/7/7.
 * 模拟 按钮类 控件的 对齐 模式 == 单纯的文本对齐 已经满足不了 Button 了， 其实 androie 的抽象更合理
 * 但是 ANdroid 的API ，为了保持灵活性， 在 易用性上面 要打折扣！！
 */
enum class UIControlContentHorizontalAlignment {
	
	center,  // 这个是默认的属性和行为？
	
	left,
	
	right,
	
	fill
}
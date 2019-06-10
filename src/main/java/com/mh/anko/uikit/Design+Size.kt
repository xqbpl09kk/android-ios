package com.mh.anko.uikit

import com.mh.anko.*

/**
 * Created by tinytitan on 2017/6/26.
 * 兼容设计期间的概念， 直接 采用数字化 技术， 实现 尺寸的模拟，
 * 图片 + 颜色 + 字体， 尺寸 等都可以完全对应， 属性简单映射 ，最复杂的是 约束布局的 DSL 中立技术
 */


// swift 中颜色区分成两种， 而android中都是整数！！
var Int.cgColor
	get() = this
	set(value) {}


// 可以统一从 应用上下文中获取资源
var Int.ddp: Int
	get() =  Design.context.ddp(this)
	set(value) {}

var Int.px2ddp: Double
	get() =  Design.context.px2ddp(this)
	set(value) {}

var Double.ddp: Int
	get() =  Design.context.ddp(this)
	set(value) {}

var Float.ddp: Int
	get() =  Design.context.ddp(this)
	set(value) {}
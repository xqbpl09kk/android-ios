package com.mh.ca

import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * Created by tinytitan on 2017/3/12.
 * Kotlin 可以直接对各种类型进行扩展,这个就是为了兼容 iOS的Swift
 * ID 的处理有点诡异啊， 如何能够生成全局唯一的ID？ 有一个 10000 以内轮询取模的运算规则？，
 * 理论上碰撞的概率几乎为0
 */

private  var sequence: Int = 0
private  var SEQUENCE_BASE = 10000
private  var SEQUENCE_OFFSET = 100

// 线程安全的方法
internal @Synchronized fun nextSequence():Int {
	sequence += 1
	
	if (sequence > SEQUENCE_BASE) {
		sequence %= SEQUENCE_BASE
	}
	return sequence + SEQUENCE_OFFSET
}

fun<T: View, R: ViewGroup> R.child(view: T): T{
	// MARK: 自动设置 ID
	if (view.id != -1) {
		Log.d("ViewDSL", "手动设置了 view id: ${view.id}")
	} else {
		// view.id = this.childCount + 100 // 自己保证在约束布局范围内，局部唯一即可 ！！！
		view.id = nextSequence() //  自动生成ID
	}
	
	if (view.layoutParams == null) {
		this.addView(view) // 需要支持 更灵活的布局
		Log.d("ViewDSL", "添加子视图前没有进行布局，view： ${view.javaClass.canonicalName}")
	}else {
		this.addView(view, view.layoutParams)
	}
	
	return view
}


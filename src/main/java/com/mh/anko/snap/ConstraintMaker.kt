package com.mh.anko.snap

import android.support.constraint.ConstraintLayout
import android.view.View
import com.mh.anko.*
import com.mh.anko.layout.*

/**
 * Created by tinytitan on 2017/6/26.
 * 封装 约束的构建过程， 其实就是一个 Builder 模式, 初始化创建 约束布局
 */
data class ConstraintMaker(val view: View) {
	
	// MARK： 默认 根据内容自适应， 这点很关键
	val layout = ConstraintLayout.LayoutParams(anySize, anySize)
	init {
		this.view.layoutParams = layout
	}
	
	val left = ConstraintMakerExtendable(view,  ConstraintType.left)
	val top = ConstraintMakerExtendable(view,  ConstraintType.top)
	val right = ConstraintMakerExtendable(view,  ConstraintType.right)
	val bottom = ConstraintMakerExtendable(view,  ConstraintType.bottom)
	
	val leading = ConstraintMakerExtendable(view,  ConstraintType.leading)
	val trailing = ConstraintMakerExtendable(view,  ConstraintType.trailing)
	
	val width = ConstraintMakerExtendable(view,  ConstraintType.width)
	val height = ConstraintMakerExtendable(view,  ConstraintType.height)
	
	val centerX = ConstraintMakerExtendable(view,  ConstraintType.centerX)
	val centerY = ConstraintMakerExtendable(view,  ConstraintType.centerY)
	
	val lastBaseline = ConstraintMakerExtendable(view,  ConstraintType.lastBaseline)
	 
	val firstBaseline = ConstraintMakerExtendable(view,  ConstraintType.firstBaseline)
	 
	val leftMargin = ConstraintMakerExtendable(view,  ConstraintType.leftMargin)
	val rightMargin = ConstraintMakerExtendable(view,  ConstraintType.rightMargin)
	val topMargin = ConstraintMakerExtendable(view,  ConstraintType.topMargin)
	val bottomMargin = ConstraintMakerExtendable(view,  ConstraintType.bottomMargin)
	val leadingMargin = ConstraintMakerExtendable(view,  ConstraintType.leadingMargin)
	val trailingMargin = ConstraintMakerExtendable(view,  ConstraintType.trailingMargin)
	val centerXWithinMargins = ConstraintMakerExtendable(view,  ConstraintType.centerXWithinMargins)
	val centerYWithinMargins = ConstraintMakerExtendable(view,  ConstraintType.centerYWithinMargins)
	
	// aggregates
	val edges = ConstraintMakerExtendable(view,  ConstraintType.edges)
	val size = ConstraintMakerExtendable(view,  ConstraintType.size)
	val center = ConstraintMakerExtendable(view,  ConstraintType.center)
	val margins = ConstraintMakerExtendable(view,  ConstraintType.margins)
	
	val centerWithinMargins = ConstraintMakerExtendable(view,  ConstraintType.centerWithinMargins)
}
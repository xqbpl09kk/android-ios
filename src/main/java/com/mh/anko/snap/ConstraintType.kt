package com.mh.anko.snap

/**
 * Created by tinytitan on 2017/6/26.
 * 约束类型， 我们只做 单个纬度 的 约束建模， 不需要组合起来设置
 * == 就是 4个约束 全部离散， 当然可以做一些整合语法糖, 有些可以不支持， 这点看情况迁移
 */
enum class ConstraintType {
	
	none,
	left,
	top,
	right,
	bottom,
	leading,
	trailing,
	width,
	height,
	centerX,
	centerY,
	lastBaseline,
	
	firstBaseline,
	
	leftMargin,
	
	rightMargin,
	
	topMargin,
	
	bottomMargin,
	
	leadingMargin,
	
	trailingMargin,
	
	centerXWithinMargins,
	
	centerYWithinMargins,
	
	// aggregates
	
	edges,
	size,
	center,
	
	margins,
	
	centerWithinMargins
	
}
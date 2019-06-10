package com.mh.anko.uikit

import android.support.constraint.*
import android.view.*
import com.mh.anko.snap.*
import com.mh.ext.*

/**
 * Created by tinytitan on 2017/6/26.
 * 模拟 SnapKit 的 DSL， 语法， 实现 布局的 "兼容模式 == 这点非常霸道！！！"
 * 使得 迁移的改动最小化
 */

data class SnapKit(val view: View) {
	
	val maker: ConstraintMaker by lazy { ConstraintMaker(view) }
	
	val left: Constraint by lazy { Constraint(view,  ConstraintType.left) }  
	val top: Constraint by lazy { Constraint(view,  ConstraintType.top) }
	val right: Constraint by lazy { Constraint(view,  ConstraintType.right) }
	val bottom: Constraint by lazy { Constraint(view,  ConstraintType.bottom) }
	
	val leading: Constraint by lazy { Constraint(view,  ConstraintType.leading) }
	val trailing: Constraint by lazy { Constraint(view,  ConstraintType.trailing) }
	
	val width: Constraint by lazy { Constraint(view,  ConstraintType.width) }
	val height: Constraint by lazy { Constraint(view,  ConstraintType.height) }
	
	val centerX: Constraint by lazy { Constraint(view,  ConstraintType.centerX) }
	val centerY: Constraint by lazy { Constraint(view,  ConstraintType.centerY) }
	
	val lastBaseline: Constraint by lazy { Constraint(view,  ConstraintType.lastBaseline) }
	
	val firstBaseline: Constraint by lazy { Constraint(view,  ConstraintType.firstBaseline) }
	
	val leftMargin: Constraint by lazy { Constraint(view,  ConstraintType.leftMargin) }
	val rightMargin: Constraint by lazy { Constraint(view,  ConstraintType.rightMargin) }
	val topMargin: Constraint by lazy { Constraint(view,  ConstraintType.topMargin) }
	val bottomMargin: Constraint by lazy { Constraint(view,  ConstraintType.bottomMargin) }
	val leadingMargin: Constraint by lazy { Constraint(view,  ConstraintType.leadingMargin) }
	val trailingMargin: Constraint by lazy { Constraint(view,  ConstraintType.trailingMargin) }
	val centerXWithinMargins: Constraint by lazy { Constraint(view,  ConstraintType.centerXWithinMargins) }
	val centerYWithinMargins: Constraint by lazy { Constraint(view,  ConstraintType.centerYWithinMargins) }
	
	// aggregates
	val edges: Constraint by lazy { Constraint(view,  ConstraintType.edges) }
	val size: Constraint by lazy { Constraint(view,  ConstraintType.size) }
	val center: Constraint by lazy { Constraint(view,  ConstraintType.center) }
	val margins: Constraint by lazy { Constraint(view,  ConstraintType.margins) }
	
	val centerWithinMargins: Constraint by lazy { Constraint(view,  ConstraintType.centerWithinMargins) }
}

// 基本概念和逻辑 尽量 迁移 Swift 的 SnapKit AutoLayout DSL
var View.snp: SnapKit by FieldProperty {
	SnapKit(this)
}

// 扩展 约束布局的 DSL == 就是为了兼容 snpkit 语法，包装一下而已
fun SnapKit.makeConstraints(
		closure: (make: ConstraintMaker) -> Unit) {
	closure.invoke(this.maker)
	maker.layout.validate() // 一定需要 validate， 否则不生效， 这点很关键
	
	// 补丁， 保证先添加视图， 后续再设置布局也是可以的
	val parent = view.parent
	if (parent is ConstraintLayout) {
		parent.removeView(view)
		parent.addView(view, view.layoutParams)
	}
}
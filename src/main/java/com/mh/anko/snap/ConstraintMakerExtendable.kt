package com.mh.anko.snap

import android.view.*
import com.mh.anko.uikit.*
import com.mh.ca.*

/**
 * Created by tinytitan on 2017/7/2.
 * 可扩展的 动态约束构建器具, 可以初始化可变参数个约束
 */
class ConstraintMakerExtendable(val view: View, type: ConstraintType) {
	
	var snp: SnapKit
		get() = view.snp
		set(value) {}
	
	// 需要改成，列表集合， 可以对整体进行处理， 其实分两个层级处理 即可
	
	// 更具策略的不同，进行统一的处理,
	fun equalTo(value: Int, isPixelMode: Boolean = false): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.equalTo(value, isPixelMode) }
		return this
	}
	
	fun equalTo(value: Double, isPixelMode: Boolean = false): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.equalTo(value, isPixelMode) }
		return this
	}
	
	// 独有的， iOS 中需要打上补丁暴涨痛殴， 就是便是这个意思。。。
	fun wrapContent(): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.wrapContnt() }
		return this
	}
	
	fun greaterThanOrEqualTo(value: Int): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.greaterThanOrEqualTo(value) }
		return this
	}
	
	fun lessThanOrEqualTo(value: Int): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.lessThanOrEqualTo(value) }
		return this
	}
	
	
	// 与兄弟比较 == 对等的约束场景， 有些约束是无效的组合，
	// 代码里面最好直接标准出来 == DEBUG ！！！ == 开发期间， 直接 crash， 上线 DEBUG
	fun equalTo(brother: View): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.equalTo(brother) }
		return this
	}
	
	// 直接设置宽度等于父容器 == android 不支持 兄弟直接相等
	fun equalToSuperview(): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.equalToSuperview() }
		return this
	}
	
	// 模拟实现 对， 顶， 靠 的概念
	fun equalTo(other: Constraint): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.equalTo(other) }
		return this
	}
	
	// 后面是偏移量的设置， 或者修正调整, 要有对应的东西才能生效
	fun multipliedBy(amount: Double): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.multipliedBy(amount) }
		return this
	}
	
	fun dividedBy(amount: Double): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.dividedBy(amount) }
		return this
	}
	
	// 偏移指令 专门用来， 进行间距的调整， + padding 也可以 用来实现 偏移， 非常霸道！
	fun offset(amount: Int, isPixelMode: Boolean = false): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.offset(amount, isPixelMode) }
		return this
	}
	
	// 支持统一的 边距， 或者 Rect 四边 == 还是 迁移一个 过来， 实现 完全的对应， FUCK
	fun inset(amount: Int): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.inset(amount) }
		return this
	}
	
	fun inset(amount: UIEdgeInsetsMake): ConstraintMakerExtendable {
		this.constraints.forEach { it -> it.inset(amount) }
		return this
	}
	
	
	// 就是无用的，完全为了兼容 iOS 做的 FAKE == android 暂时没有很多的特性和约束仲裁机制
	fun priority(amount: Int): ConstraintMakerExtendable {
		//this.constraints.forEach { it -> it.priority(amount) }
		return this
	}
	
	
	private var constraints: MutableList<Constraint> = mutableListOf()
	
	init {
		this.constraints.add(Constraint(view, type))
	}
	
	// MARK: 局势添加 约束
	var left: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.left))
		}
		set(value) {}
	var top: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.top))
		}
		set(value) {}
	
	var right: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.right))
		}
		set(value) {}
	
	var bottom: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.bottom))
		}
		set(value) {}
	
	var leading: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.leading))
		}
		set(value) {}
	var trailing: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.trailing))
		}
		set(value) {}
	
	var width: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.width))
		}
		set(value) {}
	var height: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.height))
		}
		set(value) {}
	
	var centerX: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.centerX))
		}
		set(value) {}
	var centerY: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.centerY))
		}
		set(value) {}
	
	var lastBaseline: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.lastBaseline))
		}
		set(value) {}
	
	var firstBaseline: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.firstBaseline))
		}
		set(value) {}
	
	var leftMargin: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.leftMargin))
		}
		set(value) {}
	var rightMargin: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.rightMargin))
		}
		set(value) {}
	var topMargin: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.topMargin))
		}
		set(value) {}
	var bottomMargin: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.bottomMargin))
		}
		set(value) {}
	var leadingMargin: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.leadingMargin))
		}
		set(value) {}
	var trailingMargin: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.trailingMargin))
		}
		set(value) {}
	var centerXWithinMargins: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.centerXWithinMargins))
		}
		set(value) {}
	var centerYWithinMargins: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.centerYWithinMargins))
		}
		set(value) {}
	
	// aggregates
	var edges: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.edges))
		}
		set(value) {}
	var size: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.size))
		}
		set(value) {}
	var center: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.center))
		}
		set(value) {}
	var margins: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.margins))
		}
		set(value) {}
	
	var centerWithinMargins: ConstraintMakerExtendable
		get() = this.pipely { it ->
			it.constraints.add(Constraint(view, ConstraintType.centerWithinMargins))
		}
		set(value) {}


}
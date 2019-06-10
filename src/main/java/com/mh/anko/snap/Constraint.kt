package com.mh.anko.snap

import android.support.constraint.*
import android.view.*
import android.widget.*
import com.mh.anko.*
import com.mh.anko.layout.*
import com.mh.anko.uikit.*

/**
 * Created by tinytitan on 2017/6/26.
 * 直接 做约束， 这个爽翻天, 通过支持 链式操作， 实现非常屌的特性，
 * 没有使用策略模式， 就是直接一体化的 when， 简单明了！！！
 */
data class Constraint(val view: View, val type: ConstraintType) {
	
	companion object {
		internal val WIDTH_RATIO_POWER = 1000 // 宽高比的基准数值
	}
	
	var snp: SnapKit
		get() = view.snp
		set(value) {}
	
	var layout: ConstraintLayout.LayoutParams
		get() = snp.maker.layout
		set(value) {}
	
	// 只有文本视图会有这个约束， 转换成属性 + 约束组合模拟实现
	fun greaterThanOrEqualTo(value: Int): Constraint {
		val value = value.ddp
		when(view) {
			is TextView -> {
				when(type) {
					ConstraintType.width -> {
						layout.width = wrapContent
						view.minimumWidth = value
					}
					ConstraintType.height -> {
						layout.height = wrapContent
						view.minHeight = value
					}
				}
			}
		}
		return this
	}
	
	fun lessThanOrEqualTo(value: Int): Constraint {
		val value = value.ddp
		when(view) {
			is TextView -> {
				when(type) {
					ConstraintType.width -> {
						layout.width = wrapContent
						view.maxWidth = value
					}
					ConstraintType.height -> {
						layout.height = wrapContent
						view.maxHeight = value
					}
				}
			}
		}
		return this
	}
	
	// MRAK： Android 独有的 概念， iOS 会自动调整
	fun wrapContnt(): Constraint {
		when(type) {
			ConstraintType.width -> layout.width = wrapContent
			ConstraintType.height -> layout.height = wrapContent
		}
		
		return this
	}
	
	// 需要改成，列表集合， 可以对整体进行处理， 其实分两个层级处理 即可
	fun equalTo(value: Double, isPixelMode: Boolean = false): Constraint {
		val value: Int = when (isPixelMode) {
			true -> value.toInt()
			false -> value.ddp
		}
		this.equalTo(value, isPixelMode = true)
		return this
	}
	
	// 更具策略的不同，进行统一的处理,
	fun equalTo(value: Int, isPixelMode: Boolean = false): Constraint {
		val value = when (isPixelMode) {
			true -> value
			false -> value.ddp
		}
		when(type) {
			ConstraintType.width -> layout.width = value
			ConstraintType.height -> layout.height = value
			
			ConstraintType.leftMargin, ConstraintType.leadingMargin -> {
				layout.alignParentLeft()
				layout.leftMargin = value
			}
			ConstraintType.rightMargin, ConstraintType.trailingMargin -> {
				layout.alignParentRight()
				layout.rightMargin = -value
			}
			ConstraintType.topMargin -> {
				layout.alignParentTop()
				layout.topMargin = value
			}
			ConstraintType.bottomMargin -> {
				layout.alignParentBottom()
				layout.bottomMargin = -value
			}
			
			//  快捷方式： 隐式设置 对齐 Parent， 然后做 偏移
		// 对齐 父亲的个边角 左， 上，右，下
			ConstraintType.left, ConstraintType.leading -> {
				layout.alignParentLeft()
				layout.leftMargin = value
			}
			ConstraintType.top -> {
				layout.alignParentTop()
				layout.topMargin = value
			}
			ConstraintType.right, ConstraintType.trailing -> {
				layout.alignParentRight()
				layout.rightMargin = -value
			}
			ConstraintType.bottom -> {
				layout.alignParentBottom()
				layout.bottomMargin = -value
			}
			
		}
		
		return this
	}
	
	// 与兄弟比较 == 对等的约束场景， 有些约束是无效的组合，
	// 代码里面最好直接标准出来 == DEBUG ！！！ == 开发期间， 直接 crash， 上线 DEBUG
	fun equalTo(brother: View): Constraint {
		when (type) {
		// 对齐 父亲的个边角 左， 上，右，下
			ConstraintType.left, ConstraintType.leading -> {
				layout.sameLeft(brother)
			}
			ConstraintType.top -> {
				layout.sameTop(brother)
			}
			ConstraintType.right, ConstraintType.trailing -> {
				layout.sameRight(brother)
			}
			ConstraintType.bottom -> {
				layout.sameBottom(brother)
			}
		
		// 居中 是一种非常 有效的措施， 很牛逼的感觉
			ConstraintType.centerX -> {
				layout.centerHorizontallyOf(brother)
			}
			ConstraintType.centerY -> {
				layout.centerVerticallyOf(brother)
			}
			
			ConstraintType.center -> {
				layout.centerWith(brother)
			}
			
			ConstraintType.edges -> {
				layout.centerWith(brother)
			}
		}
		return this
	}
	
	// 直接设置宽度等于父容器 == android 不支持 兄弟直接相等
	fun equalToSuperview(): Constraint {
		when(type) {
			ConstraintType.width -> { // 这个相当于添加了好些个约束
				layout.width = anySize
				layout.matchConstraintDefaultWidth = 2 // percent 的意思
				layout.matchConstraintPercentWidth = 1f
			}
			ConstraintType.height -> {
				layout.width = anySize
				layout.matchConstraintDefaultHeight = 2 // percent 的意思
				layout.matchConstraintPercentHeight = 1f
			}
			
			// 对齐 父亲的个边角 左， 上，右，下
			ConstraintType.left, ConstraintType.leading -> {
				layout.alignParentLeft()
			}
			ConstraintType.top -> {
				layout.alignParentTop()
			}
			ConstraintType.right, ConstraintType.trailing -> {
				layout.alignParentRight()
			}
			ConstraintType.bottom -> {
				layout.alignParentBottom()
			}
			
			// 居中 是一种非常 有效的措施， 很牛逼的感觉
			ConstraintType.centerX -> {
				layout.centerHorizontally()
			}
			ConstraintType.centerY -> {
				layout.centerVertically()
			}
			
			ConstraintType.center -> {
				layout.centerInParent()
			}
			
			ConstraintType.edges -> {
				layout.centerInParent()
			}
			
		}
		return this
	}
	
	// 模拟实现 对， 顶， 靠 的概念
	fun equalTo(other: Constraint): Constraint {
		
		when(type) {
			
			ConstraintType.width -> {
				when(other.type) {
				// 这个就是设置宽高比先是 1比 1
					ConstraintType.height -> {
						// 依托的视图需要是同一个，否则无法等比例，没意义
						if (other.view == this.view) {
							layout.dimensionRatio = "1:1"  // == 指令重置为 1：1
						}
					}
				}
			}
			
			ConstraintType.height -> {
				when(other.type) {
					// 这个就是设置宽高比先是 1比 1
					ConstraintType.width -> {
						// 依托的视图需要是同一个，否则无法等比例，没意义
						if (other.view == this.view) {
							layout.dimensionRatio = "1:1"  // == 指令重置为 1：1
						}
					}
				}
			}
			
			ConstraintType.center -> {
				when(other.type) {
					// 只有中心点对齐中心点生效，否则无效
					ConstraintType.center -> {
						layout.centerWith(other.view)
					}
				}
			}
			
			ConstraintType.centerX -> {
				when(other.type) {
					ConstraintType.centerX -> {
						layout.centerHorizontallyOf(other.view)
					}
				}
			}
			
			ConstraintType.centerY -> {
				when(other.type) {
					ConstraintType.centerY -> {
						layout.centerVerticallyOf(other.view)
					}
				}
			}
			
			ConstraintType.edges -> {
				when(other.type) {
				// 只有中心点对齐中心点生效，否则无效
					ConstraintType.edges -> {
						layout.centerWith(other.view)
					}
				}
			}
			
			// 对， 顶，靠 场景
			
			ConstraintType.top -> {
				when(other.type) {
					ConstraintType.top -> {
						layout.sameTop(other.view)
					}
					ConstraintType.bottom -> {
						layout.below(other.view)
					}
				}
			}
			
			ConstraintType.bottom -> {
				when(other.type) {
					ConstraintType.bottom -> {
						layout.sameBottom(other.view)
					}
					ConstraintType.top -> {
						layout.above(other.view)
					}
				}
			}
			
			ConstraintType.left, ConstraintType.leading -> {
				when(other.type) {
					ConstraintType.left, ConstraintType.leading -> {
						layout.sameLeft(other.view)
					}
					ConstraintType.right, ConstraintType.trailing -> {
						layout.rightOf(other.view)
					}
				}
			}
			
			ConstraintType.right, ConstraintType.trailing -> {
				when(other.type) {
					ConstraintType.right, ConstraintType.trailing -> {
						layout.sameRight(other.view)
					}
					ConstraintType.left, ConstraintType.leading -> {
						layout.leftOf(other.view)
					}
				}
			}
			
		}
		return this
	}
	
	
	
	
	// 后面是偏移量的设置， 或者修正调整, 要有对应的东西才能生效
	fun multipliedBy(amount: Double): Constraint {
		
		// 目前只需要支持到 比例的调整
		when(type) {
			ConstraintType.height -> {
				if (layout.dimensionRatio != null) {
					layout.dimensionRatio = "$WIDTH_RATIO_POWER: ${(WIDTH_RATIO_POWER * amount).toInt()}"
				} else if (layout.height > 0) {
					layout.height = (layout.height * amount).toInt()
				} else if (layout.matchConstraintDefaultHeight == 2) { // 表示开启等比例了
					layout.matchConstraintPercentHeight = layout.matchConstraintPercentHeight * amount.toFloat()
				}
			}
			
			ConstraintType.width -> {
				
				if (layout.dimensionRatio != null) {
					layout.dimensionRatio = "${(WIDTH_RATIO_POWER * amount).toInt()}:$WIDTH_RATIO_POWER"
				}
					
				else if (layout.width >0) {
					layout.width = (layout.width * amount).toInt()
				} else if (layout.matchConstraintDefaultWidth == 2) { // 表示开启等比例了
					layout.matchConstraintPercentWidth = layout.matchConstraintPercentWidth * amount.toFloat()
				}
			}
		}
		return this
	}
	
	fun dividedBy(amount: Double): Constraint {
		return this
	}
	
	// 偏移指令 专门用来， 进行间距的调整， + padding 也可以 用来实现 偏移， 非常霸道！
	fun offset(amount: Int, isPixelMode: Boolean = false): Constraint {
		val amount = when (isPixelMode) {
			true -> amount
			false -> amount.ddp
		}
		
		when(type) {
			ConstraintType.top -> {
				layout.topMargin = amount
			}
			
			ConstraintType.left, ConstraintType.leading -> {
				layout.leftMargin = amount
			}
			
			// 为了兼容 iOS， 其实这个刚好要设置负数，才对应 正的 边距
			ConstraintType.bottom -> {
				layout.bottomMargin = -amount
			}
			
			ConstraintType.right, ConstraintType.trailing -> {
				layout.rightMargin = -amount
			}
			
			ConstraintType.centerX -> {
				if (amount > 0) { // 右偏， 左 Padding
					view.setPadding(amount * 2, view.paddingTop, view.paddingRight, view.paddingBottom)
				}else {
					view.setPadding(view.paddingLeft, view.paddingTop, -amount * 2, view.paddingBottom)
				}
			}
			
			ConstraintType.centerY -> {
				if (amount > 0) { // 下偏移， 上 Padding
					view.setPadding(view.paddingLeft, amount * 2, view.paddingRight, view.paddingBottom)
				} else {
					view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, -amount * 2)
				}
			}
		}
		return this
	}
	
	// 支持统一的 边距， 或者 Rect 四边 == 还是 迁移一个 过来， 实现 完全的对应， FUCK
	fun inset(amount: Int): Constraint {
		val amount = amount.ddp
		
		layout.topMargin = amount
		layout.leftMargin = amount
		layout.bottomMargin = amount
		layout.rightMargin = amount
		
		return this
	}
	
	// 支持统一的 边距， 或者 Rect 四边 == 还是 迁移一个 过来， 实现 完全的对应， FUCK
	fun inset(amount: UIEdgeInsetsMake): Constraint {
		layout.topMargin = amount.top.ddp
		layout.leftMargin = amount.left.ddp
		layout.bottomMargin = amount.bottom.ddp
		layout.rightMargin = amount.right.ddp
		
		return this
	}
	
	
	// 就是无用的，完全为了兼容 iOS 做的 FAKE == android 暂时没有很多的特性和约束仲裁机制
	fun priority(amount: Int): Constraint {
		return this
	}
}
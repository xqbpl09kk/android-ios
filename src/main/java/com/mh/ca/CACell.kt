package com.mh.ca

import android.content.*
import android.support.constraint.*
import com.mh.anko.*
import com.mh.anko.layout.*
import com.mh.anko.uikit.*
import com.mh.ca.recyclerview.view.*

/**
 * Created by tinytitan on 2017/3/12.
 * 基于卡片视图， 构造出 可以设置圆角的 UICollectionCell
 */
//open class CACell(context: Context)  : CardView(context), SelectionAware {
//	constructor():this(UIKit.context) // 默认构造函数
//
//	var contentView: ConstraintLayout
//	init {
//		layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
//		// 绘制 约束布局当作内部子容器
//		contentView = this.child(ConstraintLayout(context).pipely { it ->
//			it.frameLayout(width = matchParent, height = matchParent)
//		})
//
//		// 兼容 iOS 的风格
//		setCardBackgroundColor(Color.TRANSPARENT)  // 改成默认透明
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			elevation = 0f
//		}
//		cardElevation = 0f
//		maxCardElevation = 2f
//
//
//		loadView()
//		viewDidLoad()
//	}
//
//	open fun loadView() {}
//	open fun viewDidLoad() {}
//}

open class CACell(context: Context) : ConstraintLayout(context), SelectionAware {
	init {
		frameLayout(width = matchParent, height = matchParent)
		loadView()
		viewDidLoad()
	}
	
	constructor() : this(UIKit._context) // 默认构造函数
	
	open fun loadView() {}
	open fun viewDidLoad() {}
	
	// 兼容模式， 就是为了 hack swift 写法： iOS 平台 卡片一定有contentView
	open var contentView: ConstraintLayout
		get() = this
		set(value) {}
}
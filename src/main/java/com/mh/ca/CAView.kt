package com.mh.ca

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.ViewGroup
import com.mh.anko.layout.*
import com.mh.anko.matchParent
import com.mh.anko.uikit.UIKit

/**
 * Created by tinytitan on 2017/3/12.
 * 模拟出 iOS中 UIView的概念，并添加自定义的模版方法
 */
open class CAView(context: Context) : ConstraintLayout(context) {
	init {
		frameLayout(width = matchParent, height = matchParent)
		loadView()
		viewDidLoad()
	}
	
	constructor():this(UIKit._context) // 默认构造函数
	
	open fun loadView() {}
	open fun viewDidLoad() {}
	
	// 兼容模式， 就是为了 hack swift 写法： iOS 平台 卡片一定有contentView
	open var contentView: ConstraintLayout
		get() = this
		set(value) {}
}
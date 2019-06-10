package com.mh.ca

import android.content.Context
import android.view.ViewGroup
import com.mh.anko.uikit.UIPrettyButton
import com.mh.anko.matchParent
import com.mh.anko.uikit.UIKit
import com.mh.ca.recyclerview.view.SelectionAware

/**
 * Created by tinytitan on 2017/6/15.
 */
open class CAButtonCell(context: Context) : UIPrettyButton(context) , SelectionAware {
	init {
		layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
		loadView()
		viewDidLoad()
	}
	
	constructor():this(UIKit.context) // 默认构造函数
	
	open fun loadView() {}
	open fun viewDidLoad() {}
}
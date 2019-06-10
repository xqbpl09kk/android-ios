package com.mh.ca

import android.content.Context
import com.mh.anko.uikit.UIKit

/**
 * Created by tinytitan on 2017/6/12.
 * 只需要定义一个改变颜色的 LC, 颜色采用 整数即可
 */
class CAColorfullCell(context:Context): CACell(context) {
	
	constructor():this(UIKit.context) // 默认构造函数
	
	override fun viewDidLoad() {
		this.vip.presenter.loadContent { content ->
			if (content is Int) {
				this.setBackgroundColor(content)
			}else {
				// 设置错误的颜色，那么就只就是透明色
				this.setBackgroundColor(android.graphics.Color.TRANSPARENT)
			}
		}
	}
	
}
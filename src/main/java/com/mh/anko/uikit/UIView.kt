package com.mh.anko.uikit

import android.content.*
import android.widget.*

/**
 * Created by tinytitan on 2017/6/26.
 * 适配 iOS, 主要是考虑到 容器 在iOS 中也是 UIView， 真正的 约束 都改成 CAView ！！
 */
class UIView(context: Context): FrameLayout(context) {
	
	companion object {
		fun performWithoutAnimation(block: () -> Unit) {
			block.invoke()
		}
	}
	constructor():this(UIKit._context) // 默认构造函数
	
}
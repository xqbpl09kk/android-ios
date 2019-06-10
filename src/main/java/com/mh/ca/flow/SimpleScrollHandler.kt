package com.mh.ca.flow

import com.mh.anko.uikit.*

/**
 * Created by tinytitan on 2017/6/13.
 * 支持闭包风格的监听
 */
class SimpleScrollHandler(val handler: (position: CGPoint) -> Unit):
		CAScrollHandlerType {
	override fun handleScroll(position: CGPoint) {
		this.handler.invoke(position)
	}
}

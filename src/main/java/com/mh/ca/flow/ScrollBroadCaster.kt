package com.mh.ca.flow

import com.mh.anko.uikit.*

/**
 * Created by tinytitan on 2017/6/13.
 * MARK:  滚动广播器 ， 能够接受闭包注册，然后广播滚动事件，内部通过规则可以选择监听滚动状态
 */
class ScrollBroadCaster: CAScrollHandlerType {
	
	private var subHandlers: MutableList<CAScrollHandlerType> = mutableListOf()
	
	fun appendSubHandler(handler: CAScrollHandlerType?) {
		if (handler != null) {
			subHandlers.add(handler)
		}
	}
	
	fun appendSubHandler(handler: (position: CGPoint) -> Unit) {
		subHandlers.add(SimpleScrollHandler(handler))
	}
	override fun handleScroll(position: CGPoint) {
		for (handler in subHandlers) {
			handler.handleScroll(position)
		}
	}
}
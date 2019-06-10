package com.mh.ca.flow

import com.mh.anko.uikit.*

/**
 * Created by tinytitan on 2017/6/13.
 * MARK: 滚动处理器 + 通过这个协议 跟滚动广播器 【联动】
 */
public interface CAScrollHandlerType {
	fun handleScroll(position: CGPoint) : Unit
}
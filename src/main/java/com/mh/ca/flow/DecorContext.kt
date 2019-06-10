package com.mh.ca.flow

/**
 * Created by tinytitan on 2017/6/12.
 * MARK: 目前只对 ROW 进行装饰，简化场景
 */
class DecorContext(val row: Int, val isFirst: Boolean, val isLast: Boolean) {
	companion object {
		val KEY = "flo.vip.extra.decor"
	}
	public var isMiddle: Boolean
		get()  =  !isFirst  && !isLast
		set(value) {}
}
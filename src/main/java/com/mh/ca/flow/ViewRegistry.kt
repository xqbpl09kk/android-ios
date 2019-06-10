package com.mh.ca.flow

import kotlin.reflect.KClass

/**
 * Created by tinytitan on 2017/6/13.
 * 视图注册中心
 */
class ViewRegistry {
	
	private val viewMap: MutableMap<Int, KClass<out Any>>
			= HashMap<Int, KClass<out Any>>()
	
	// MARK： 注册视图，只能注册一次，并不能更改！！
	fun regiestView(type: Int, view: KClass<out Any>) {
		// 非法类型，注册不进去，就相当于 空类型
		if (type == Mapper.INVALID_TYPE) {return}
		if (!viewMap.containsKey(type)) {
			viewMap.put(type, view)
		}
	}
	
	fun getView(type: Int): KClass<out Any>? {
		return viewMap[type]
	}
}
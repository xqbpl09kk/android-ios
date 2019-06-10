package com.mh.ca.flow

import com.mh.ca.*
import kotlin.properties.*

/**
 * Created by tinytitan on 2017/6/12.
 * MARK: 策略对象，专注于策略的创建 == 隔离变化
 */
class Strategy {
	private var _adapter: CAFlowCollection by Delegates.notNull<CAFlowCollection>()
	// 计算属性
	var adapter: CAFlowCollection
		get() {return this._adapter}
		set(value) {}
	
	// 这个到时会产生 一个策略对象
	fun bindAdapter(adapter: CAFlowCollection) {
		this._adapter = adapter
	}
	// MARK:  如何保障策略只能设置一次
	private var _type: StrategyType? = null
	var type: StrategyType
		get() {
			val value = this._type
			if (value != null) {
				return value
			}else {
				// this.bindType(StrategyType.compound), 不绑定，但是返回一个默认值，即可
				return StrategyType.compound
			}
		}
		set(value) {}
	
	fun bindType(type: StrategyType) {
		if (this._type != null) {
			if (CAConfig.DEV_MODE) {
				//  开发模式直接 【开挂】
				assert(false, { print("flo: 重复绑定策略类型") })
			}
		} else {
			this._type = type
		}
	}
	
	// Singlton 模式的开关 == 是否可见
	var visiable: Boolean = true
	var disableReuse: Boolean = false // 不支持复用， 这样就不会回收，一直持有？
	
	// TAB 模式独有的开关
	public var tabIndex: Int = 0
	public var enableTabHeader: Boolean = true
	public var enableTabFooter: Boolean = false
}
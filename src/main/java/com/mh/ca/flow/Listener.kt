package com.mh.ca.flow

import kotlin.properties.Delegates

/**
 * Created by tinytitan on 2017/6/12.
 * MARK: 其实就是 Delegate，监听 UICV的 选取和滚动等行为状态的变化，类似 VIPER中的 Actor
 */
class Listener {
	
	private var _adapter: CAFlowCollection by Delegates.notNull<CAFlowCollection>()
	var adapter: CAFlowCollection
		get() {return this._adapter}
		set(value) {}
	fun bindAdapter(adapter: CAFlowCollection) {
		this._adapter = adapter
	}
	
	public var selectListener: ((section: Int, row: Int) -> Unit)? = null
	public var deselectListener: ((section: Int, row: Int) -> Unit)? = null
	
	
	public var allowsMultipleSelection: Boolean = false
	public var allowsSelection: Boolean = false
	
}
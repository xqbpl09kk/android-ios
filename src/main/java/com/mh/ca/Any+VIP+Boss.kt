package com.mh.ca

import com.mh.ext.FieldProperty

/**
 * Created by tinytitan on 2017/6/12.
 * 给任意对象扩展 vip 属性， 并支持 boss 操作
 */

private var Any._vip: CAViper by FieldProperty { CAViper() }

 // MARK： 这样就原声扩展了一个 非 Optinal 类型的属性
var Any.vip: CAViper
	get() {
		if (this._vip == null){
			val vip = CAViper()
			this._vip = vip
		}
		return this._vip!!
	}
	set(value) {}

fun<W: Any, B: Any> W.boss(boss: B): W{
	
	if (this == boss) {
		if (CAConfig.DEV_MODE) {
			throw AssertionError("自己不能向自己汇报")
		}
		return this
	}
	this.vip.parent = boss.vip
	return this
}
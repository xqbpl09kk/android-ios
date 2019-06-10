package com.mh.ca.flow

/**
 * Created by tinytitan on 2017/6/12.
 * MARK: 策略【当前层级】
 */
enum class StrategyType {
	list,
	repeatList,
	singleton,
	placeholder, // MARK: 就是静态的意思
	optinal,
	sequence,
	tab,
	compound,
	repeatCompound; // MARK: 专门应对复杂场景使用
}
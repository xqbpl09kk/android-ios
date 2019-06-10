package com.mh.anko.res

/**
 * Created by tinytitan on 2016/10/18.
 * MARK: 类型化包装内部结构数据 == 反向状态 通过 false 转换成 对应的 负数值来代表!!!
 */
enum class UIControlState(val value: Int) {
	aboveAnchor(android.R.attr.state_above_anchor),
	accelerated(android.R.attr.state_accelerated),
	activated(android.R.attr.state_activated),
	active(android.R.attr.state_active),
	checkable(android.R.attr.state_checkable),
	checked(android.R.attr.state_checked),
	dragCanAccept(android.R.attr.state_drag_can_accept),
	dragHovered(android.R.attr.state_drag_hovered),
	empty(android.R.attr.state_empty),

	enabled(android.R.attr.state_enabled),  // 激活状态 : 未激活是对应的负数!!!
	disabled(-android.R.attr.state_enabled), // 未激活状态

	expanded(android.R.attr.state_expanded),
	first(android.R.attr.state_first),

	focused(android.R.attr.state_focused), // iOS highlighted, 激活高亮状态

	hovered(android.R.attr.state_hovered),
	last(android.R.attr.state_last),
	longPressable(android.R.attr.state_long_pressable),
	middle(android.R.attr.state_middle),
	multiline(android.R.attr.state_multiline),

	pressed(android.R.attr.state_pressed),  // iOS highlighted, 激活高亮状态
	selected(android.R.attr.state_selected), // iOS tabSelected , 选取状态

	single(android.R.attr.state_single),
	window_focused(android.R.attr.state_window_focused)
}
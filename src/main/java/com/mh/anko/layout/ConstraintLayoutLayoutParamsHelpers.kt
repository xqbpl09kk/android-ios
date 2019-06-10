/*
 * Copyright 2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")
package com.mh.anko.layout

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintLayout.LayoutParams
import android.util.Log
import android.view.View

// MARK: 定义两个全局变量，anySize代表 0dp， parentId 以备后用
val anySize: Int = 0
val parentId: Int = 0

// MARK: layout_constraintTop_toTopOf — 期望视图的上边对齐另一个视图的上边。
/**
 * Set the current View top attribute the same as for [view].
 */
inline fun LayoutParams.sameTop(view: View): Unit  { topToTop = view.id }

/**
 * Set the current View top attribute the same as for View with a given [id].
 */
inline fun LayoutParams.sameTop(id: Int): Unit { topToTop = id }

/**
 * Align the current View's top edge with its parent's top edge.
 */
inline fun LayoutParams.alignParentTop(): Unit  { topToTop = LayoutParams.PARENT_ID }

// MARK: layout_constraintTop_toBottomOf — 期望视图的上边对齐另一个视图的底边。

/**
 * Place the current View below [view].
 * It is an alias for [below].
 */
inline fun LayoutParams.bottomOf(view: View): Unit { topToBottom = view.id }

/**
 * Place the current View below [view].
 */
inline fun LayoutParams.below(view: View): Unit { topToBottom = view.id }

/**
 * Place the current View below the View with a given [id].
 */
inline fun LayoutParams.below(id: Int): Unit { topToBottom = id }

/**
 * Place the current View below the View with a given [id].
 * It is an alias for [below].
 */
inline fun LayoutParams.bottomOf(id: Int): Unit { topToBottom = id }

// MARK: layout_constraintBottom_toTopOf — 期望视图的下边对齐另一个视图的上边

/**
 * Place the current View above [view].
 * It is an alias for [above].
 */
inline fun ConstraintLayout.LayoutParams.topOf(view: View): Unit { bottomToTop = view.id }

/**
 * Place the current View above [view].
 */
inline fun LayoutParams.above(view: View): Unit  { bottomToTop = view.id }

/**
 * Place the current View above the View with a given [id].
 * It is an alias for [above].
 */
inline fun LayoutParams.topOf(id: Int): Unit  { bottomToTop = id }

/**
 * Place the current View above the View with a given [id].
 */
inline fun LayoutParams.above(id: Int): Unit { bottomToTop = id }


// MARK: layout_constraintBottom_toBottomOf — 期望视图的底边对齐另一个视图的底边。
/**
 * Set the current View bottom attribute the same as for View with a given [id].
 */
inline fun LayoutParams.sameBottom(id: Int): Unit { bottomToBottom = id }
/**
 * Set the current View bottom attribute the same as for [view].
 */
inline fun LayoutParams.sameBottom(view: View): Unit  { bottomToBottom = view.id }

/**
 * Align the current View's bottom edge with its parent's bottom edge.
 */
inline fun LayoutParams.alignParentBottom(): Unit { bottomToBottom = LayoutParams.PARENT_ID }

// MARK: layout_constraintLeft_toLeftOf — 期望视图的左边对齐另一个视图的左边。

/**
 * Set the current View left attribute the same as for [view].
 */
inline fun LayoutParams.sameLeft(view: View): Unit  { leftToLeft = view.id }
/**
 * Set the current View left attribute the same as for View with a given [id].
 */
inline fun LayoutParams.sameLeft(id: Int): Unit { leftToLeft = id }

/**
 * Align the current View's left edge with its parent's left edge.
 */
inline fun LayoutParams.alignParentLeft(): Unit { leftToLeft = LayoutParams.PARENT_ID }

// MARK:layout_constraintLeft_toRightOf — 期望视图的左边对齐另一个视图的右边。
/**
 * Place the current View to the right of [view].
 */
inline fun LayoutParams.rightOf(view: View): Unit  { leftToRight = view.id }
/**
 * Place the current View to the left of the View with a given [id].
 */
inline fun LayoutParams.rightOf(id: Int): Unit { leftToRight = id }

// MARK:layout_constraintRight_toLeftOf — 期望视图的右边对齐另一个视图的左边。

/**
 * Place the current View to the left of [view].
 */
inline fun LayoutParams.leftOf(view: View): Unit { rightToLeft = view.id }
/**
 * Place the current View to the left of the View with a given [id].
 */
inline fun LayoutParams.leftOf(id: Int): Unit { rightToLeft = id }

// MARK: layout_constraintRight_toRightOf — 期望视图的右边对齐另一个视图的右边。

/**
 * Set the current View right attribute the same as for [view].
 */
inline fun LayoutParams.sameRight(view: View): Unit  { rightToRight = view.id }

/**
 * Set the current View right attribute the same as for View with a given [id].
 */
inline fun LayoutParams.sameRight(id: Int): Unit { rightToRight = id }

/**
 * Align the current View's right edge with its parent's right edge.
 */
inline fun LayoutParams.alignParentRight(): Unit { rightToRight = LayoutParams.PARENT_ID }

// MARK: layout_constraintStart_toStartOf — 期望视图的右边对齐另一个视图的右边。

/**
 * Align the current View's start edge with another child's start edge.
 */
inline fun LayoutParams.alignStart(id: Int): Unit  { startToStart = id }

/**
 * Align the current View's start edge with another child's start edge.
 */
inline fun LayoutParams.alignStart(view: View): Unit  { startToStart = view.id }

/**
 * Align the current View's start edge with its parent's start edge.
 */
inline fun LayoutParams.alignParentStart(): Unit  { startToStart = LayoutParams.PARENT_ID }


// MARK: layout_constraintEnd_toEndOf — 期望视图的右边对齐另一个视图的右边。

/**
 * Align the current View's end edge with another child's end edge.
 */
inline fun LayoutParams.alignEnd(id: Int): Unit  { endToEnd = id }

/**
 * Align the current View's end edge with another child's end edge.
 */
inline fun LayoutParams.alignEnd(view: View): Unit  { endToEnd = view.id }
/**
 * Align the current View's end edge with its parent's end edge.
 */
inline fun LayoutParams.alignParentEnd(): Unit { endToEnd = LayoutParams.PARENT_ID }


// MARK: 同时成对左右对齐，就会变成水平居中对齐，非常灵活的机制

/**
 * Center the child horizontally in its parent.
 */
inline fun LayoutParams.centerHorizontally(): Unit {
	leftToLeft = LayoutParams.PARENT_ID
	rightToRight = LayoutParams.PARENT_ID
}

/**
 * Align the current View's centerX with view with a given [id].
 */
inline fun LayoutParams.centerHorizontallyOf(id: Int): Unit {
	leftToLeft = id
	rightToRight = id
}

/**
 * Align the current View's centerX with  view
 */
fun LayoutParams.centerHorizontallyOf(view: View, offset: Int = 0): Unit {
	leftToLeft = view.id
	rightToRight = view.id

	if(offset == 0) { return }
	verticalBias = calculateHorizontalBias(offset, width, view.layoutParams.width)

	Log.d("verticalBias", "offset: $offset, width: $width, targetWidth: ${view.layoutParams.width}")
}

// MARK: 计算水平偏移量
private  fun calculateHorizontalBias(offset: Int, width: Int, targetWidth: Int): Float {
	if(width <= 0 || targetWidth <= 0 ){return 0.5f}
	if(width > targetWidth) {return 0.5f}
	val diff = targetWidth - width
	val targetValue = diff/2 + offset
	var result = targetValue.toFloat()/diff
	if(result < 0) {
		return 0f
	}else {
		return result
	}
}

// MARK: 同时成对上下对齐，就会变成垂直居中对齐，非常灵活的机制

/**
 * Center the child vertically in its parent.
 */
inline fun LayoutParams.centerVertically(): Unit {
	topToTop = LayoutParams.PARENT_ID
	bottomToBottom = LayoutParams.PARENT_ID
}

/**
 * Align the current View's centerY with view with a given [id].
 */
inline fun LayoutParams.centerVerticallyOf(id: Int): Unit {
	topToTop = id
	bottomToBottom = id
}

/**
 * Align the current View's centerY with  view
 */
fun ConstraintLayout.LayoutParams.centerVerticallyOf(view: View, offset: Int = 0): Unit {
	topToTop = view.id
	bottomToBottom = view.id
	if(offset == 0) { return }
	verticalBias = calculateVerticalBias(offset, height, view.layoutParams.height)

	Log.d("verticalBias", "offset: $offset, heighr: $height, targetHeight: ${view.layoutParams.height}")
}

// MARK: 计算垂直偏移量
private  fun calculateVerticalBias(offset: Int, height: Int, targetHeight: Int): Float {
	if(height <= 0 || targetHeight <= 0 ){return 0.5f}
	if(height > targetHeight) {return 0.5f}
	val diff = targetHeight - height
	val targetValue = diff/2 + offset
	var result = targetValue.toFloat()/diff
	if(result < 0) {
		return 0f
	}else {
		return result
	}
}

// MARK： 四个方向都对齐， 就是中心点对齐 - 牛呆
/**
 * Center the child horizontally and vertically in its parent.
 */
inline fun LayoutParams.centerInParent(): Unit {
	leftToLeft = LayoutParams.PARENT_ID
	rightToRight = LayoutParams.PARENT_ID
	topToTop = LayoutParams.PARENT_ID
	bottomToBottom = LayoutParams.PARENT_ID
}

/**
 * Center the current View horizontally and vertically with view with given [id].
 */
inline fun LayoutParams.centerWith(id: Int): Unit {
	leftToLeft = id
	rightToRight = id
	topToTop = id
	bottomToBottom = id
}

/**
 * Center the current View horizontally and vertically with [view] .
 */
inline fun LayoutParams.centerWith(view: View): Unit {
	leftToLeft = view.id
	rightToRight = view.id
	topToTop = view.id
	bottomToBottom = view.id
}

/**
 * Positions the baseline of this view on the baseline of the given anchor [view].
 */
inline fun LayoutParams.baselineOf(view: View): Unit  { baselineToBaseline = view.id }

/**
 * Positions the baseline of this view on the baseline of the anchor View with a given [id].
 */
inline fun LayoutParams.baselineOf(id: Int): Unit { baselineToBaseline = id }

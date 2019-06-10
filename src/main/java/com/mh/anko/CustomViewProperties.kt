package com.mh.anko

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mh.anko.uikit.*

var View.backgroundDrawable: Drawable?
	get() = background
	set(value) = setBackgroundDrawable(value)

var View.leftPadding: Int
	get() = paddingLeft.px2ddp.toInt()
	set(value) = setPadding(value.ddp, paddingTop, paddingRight, paddingBottom)

var View.topPadding: Int
	get() = paddingTop.px2ddp.toInt()
	set(value) = setPadding(paddingLeft, value.ddp, paddingRight, paddingBottom)

var View.rightPadding: Int
	get() = paddingRight.px2ddp.toInt()
	set(value) = setPadding(paddingLeft, paddingTop, value.ddp, paddingBottom)

var View.bottomPadding: Int
	get() = paddingBottom.px2ddp.toInt()
	set(value) = setPadding(paddingLeft, paddingTop, paddingRight, value.ddp)


var View.horizontalPadding: Int
	get() = throw PropertyWithoutGetterException("horizontalPadding")
	set(value) = setPadding(value.ddp, paddingTop, value.ddp, paddingBottom)

@Deprecated("Use verticalPadding instead", ReplaceWith("verticalPadding"))
var View.paddingVertical: Int
	get() = throw PropertyWithoutGetterException("paddingVertical")
	set(value) = setPadding(paddingLeft, value.ddp, paddingRight, value.ddp)

var View.verticalPadding: Int
	get() = throw PropertyWithoutGetterException("verticalPadding")
	set(value) = setPadding(paddingLeft, value.ddp, paddingRight, value.ddp)

var View.padding: Int
	get() = throw PropertyWithoutGetterException("padding")
	set(value) {
		val value = value.ddp
		setPadding(value, value, value, value)
	}

var TextView.isSelectable: Boolean
	get() = isTextSelectable
	set(value) = setTextIsSelectable(value)

var TextView.textSizeDimen: Int
	get() = throw PropertyWithoutGetterException("textSizeDimen")
	set(value) = setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(value))

var ImageView.image: Drawable?
	get() = drawable
	set(value) = setImageDrawable(value)


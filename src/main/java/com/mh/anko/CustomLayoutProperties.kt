package com.mh.anko

/**
 * Created by tinytitan on 2017/6/14.
 */
import android.view.ViewGroup
import com.mh.anko.AnkoException

val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT
val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT

var ViewGroup.MarginLayoutParams.verticalMargin: Int
	get() = throw AnkoException("'ViewGroup.MarginLayoutParams.verticalMargin' property does not have a getter")
	set(v) {
		topMargin = v
		bottomMargin = v
	}

var ViewGroup.MarginLayoutParams.horizontalMargin: Int
	get() = throw AnkoException("'ViewGroup.MarginLayoutParams.horizontalMargin' property does not have a getter")
	set(v) {
		leftMargin = v; rightMargin = v
	}

var ViewGroup.MarginLayoutParams.margin: Int
	get() = throw AnkoException("'ViewGroup.MarginLayoutParams.margin' property does not have a getter")
	set(v) {
		leftMargin = v
		rightMargin = v
		topMargin = v
		bottomMargin = v
	}

package com.mh.anko

var android.view.View.backgroundColor: Int
	get() = throw AnkoException("'android.view.View.backgroundColor' property does not have a getter")
	set(v) = setBackgroundColor(v)

var android.view.View.backgroundResource: Int
	get() = throw AnkoException("'android.view.View.backgroundResource' property does not have a getter")
	set(v) = setBackgroundResource(v)

var android.view.View.minimumHeight: Int
	get() = throw AnkoException("'android.view.View.minimumHeight' property does not have a getter")
	set(v) {
		minimumHeight = v
	}

var android.view.View.minimumWidth: Int
	get() = throw AnkoException("'android.view.View.minimumWidth' property does not have a getter")
	set(v) {
		minimumWidth = v
	}

var android.widget.TextView.enabled: Boolean
	get() = throw AnkoException("'android.widget.TextView.enabled' property does not have a getter")
	set(v) {
		isEnabled = v
	}

var android.widget.TextView.textColor: Int
	get() = throw AnkoException("'android.widget.TextView.textColor' property does not have a getter")
	set(v) = setTextColor(v)

var android.widget.TextView.highlightColor: Int
	get() = throw AnkoException("'android.widget.TextView.highlightColor' property does not have a getter")
	set(v) {
		highlightColor = v
	}

var android.widget.TextView.hintTextColor: Int
	get() = throw AnkoException("'android.widget.TextView.hintTextColor' property does not have a getter")
	set(v) = setHintTextColor(v)

var android.widget.TextView.linkTextColor: Int
	get() = throw AnkoException("'android.widget.TextView.linkTextColor' property does not have a getter")
	set(v) = setLinkTextColor(v)

var android.widget.TextView.minLines: Int
	get() = throw AnkoException("'android.widget.TextView.minLines' property does not have a getter")
	set(v) {
		minLines = v
	}

var android.widget.TextView.maxLines: Int
	get() = throw AnkoException("'android.widget.TextView.maxLines' property does not have a getter")
	set(v) {
		maxLines = v
	}

var android.widget.TextView.lines: Int
	get() = throw AnkoException("'android.widget.TextView.lines' property does not have a getter")
	set(v) = setLines(v)

var android.widget.TextView.minEms: Int
	get() = throw AnkoException("'android.widget.TextView.minEms' property does not have a getter")
	set(v) {
		minEms = v
	}

var android.widget.TextView.maxEms: Int
	get() = throw AnkoException("'android.widget.TextView.maxEms' property does not have a getter")
	set(v) {
		maxEms = v
	}

var android.widget.TextView.singleLine: Boolean
	get() = throw AnkoException("'android.widget.TextView.singleLine' property does not have a getter")
	set(v) = setSingleLine(v)

var android.widget.TextView.marqueeRepeatLimit: Int
	get() = throw AnkoException("'android.widget.TextView.marqueeRepeatLimit' property does not have a getter")
	set(v) {
		marqueeRepeatLimit = v
	}

var android.widget.TextView.cursorVisible: Boolean
	get() = throw AnkoException("'android.widget.TextView.cursorVisible' property does not have a getter")
	set(v) {
		isCursorVisible = v
	}

var android.widget.ImageView.imageResource: Int
	get() = throw AnkoException("'android.widget.ImageView.imageResource' property does not have a getter")
	set(v) = setImageResource(v)

var android.widget.ImageView.imageURI: android.net.Uri?
	get() = throw AnkoException("'android.widget.ImageView.imageURI' property does not have a getter")
	set(v) = setImageURI(v)

var android.widget.ImageView.imageBitmap: android.graphics.Bitmap?
	get() = throw AnkoException("'android.widget.ImageView.imageBitmap' property does not have a getter")
	set(v) = setImageBitmap(v)

var android.widget.RelativeLayout.gravity: Int
	get() = throw AnkoException("'android.widget.RelativeLayout.gravity' property does not have a getter")
	set(v) {
		gravity = v
	}

var android.widget.RelativeLayout.horizontalGravity: Int
	get() = throw AnkoException("'android.widget.RelativeLayout.horizontalGravity' property does not have a getter")
	set(v) = setHorizontalGravity(v)

var android.widget.RelativeLayout.verticalGravity: Int
	get() = throw AnkoException("'android.widget.RelativeLayout.verticalGravity' property does not have a getter")
	set(v) = setVerticalGravity(v)

var android.widget.LinearLayout.dividerDrawable: android.graphics.drawable.Drawable?
	get() = throw AnkoException("'android.widget.LinearLayout.dividerDrawable' property does not have a getter")
	set(v) {
		dividerDrawable = v
	}

var android.widget.LinearLayout.gravity: Int
	get() = throw AnkoException("'android.widget.LinearLayout.gravity' property does not have a getter")
	set(v) {
		gravity = v
	}

var android.widget.LinearLayout.horizontalGravity: Int
	get() = throw AnkoException("'android.widget.LinearLayout.horizontalGravity' property does not have a getter")
	set(v) = setHorizontalGravity(v)

var android.widget.LinearLayout.verticalGravity: Int
	get() = throw AnkoException("'android.widget.LinearLayout.verticalGravity' property does not have a getter")
	set(v) = setVerticalGravity(v)

var android.widget.Gallery.gravity: Int
	get() = throw AnkoException("'android.widget.Gallery.gravity' property does not have a getter")
	set(v) = setGravity(v)

var android.widget.Spinner.gravity: Int
	get() = throw AnkoException("'android.widget.Spinner.gravity' property does not have a getter")
	set(v) {
		gravity = v
	}

var android.widget.GridView.gravity: Int
	get() = throw AnkoException("'android.widget.GridView.gravity' property does not have a getter")
	set(v) {
		gravity = v
	}

var android.widget.AbsListView.selectorResource: Int
	get() = throw AnkoException("'android.widget.AbsListView.selectorResource' property does not have a getter")
	set(v) = setSelector(v)

var android.widget.TextView.hintResource: Int
	get() = throw AnkoException("'android.widget.TextView.hintResource' property does not have a getter")
	set(v) = setHint(v)

var android.widget.TextView.textResource: Int
	get() = throw AnkoException("'android.widget.TextView.textResource' property does not have a getter")
	set(v) = setText(v)


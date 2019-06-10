package com.mh.anko.uikit

import android.content.*
import android.graphics.*
import android.graphics.drawable.*
import android.widget.*
import com.mh.anko.*

/**
 * Created by tinytitan on 16/10/12.
 * MARK:  自定义的 ImageButton, 往 ImageButton上面靠,做扩展和迁移
 */
class UIImageButton(context: Context): ImageButton(context) {
	constructor():this(UIKit._context) // 默认构造函数
	init {
		setBackgroundColor(Color.TRANSPARENT)
		
		minimumHeight = 20.ddp
		minimumWidth = 20.ddp
		
		setPadding(0,0,0,0)
	}
	
	
	fun setImage(drawable: Drawable) {
		image = drawable
	}
}


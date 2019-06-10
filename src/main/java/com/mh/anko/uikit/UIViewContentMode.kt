package com.mh.anko.uikit

/**
 * Created by tinytitan on 2017/6/26.
 * 模拟 iOS 的变成风格
 */
enum class UIViewContentMode {
	
	scaleToFill,
	
	scaleAspectFit, // contents scaled to fit with fixed aspect. remainder is transparent
	
	scaleAspectFill, // contents scaled to fill with fixed aspect. some portion of content may be clipped.
	
	redraw, // redraw on bounds change (calls -setNeedsDisplay)
	
	center, // contents remain same size. positioned adjusted.
	
	top,
	
	bottom,
	
	left,
	
	right,
	
	topLeft,
	
	topRight,
	
	bottomLeft,
	
	bottomRight
}
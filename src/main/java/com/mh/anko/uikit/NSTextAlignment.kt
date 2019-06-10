package com.mh.anko.uikit

/**
 * Created by tinytitan on 2017/6/26.
 * 实现文本对齐的 HACK， 分散对齐 这种 模式也是很关键的，
 * 尤其是有大段的文本的场景下， 这点非常关键
 * 后续 还要 模拟 省略号 这种玩意 。。。。。 == ellipsize
 */
enum class NSTextAlignment {
	left, // Visually left aligned
	
	center, // Visually centered
	
	right, // Visually right aligned
	
	/* !TARGET_OS_IPHONE */
	// Visually right aligned
	// Visually centered
	
	justified, // Fully-justified. The last line in a paragraph is natural-aligned.
	
	natural // Indicates the default alignment for script
}
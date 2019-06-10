package com.mh.anko.uikit

/**
 * Created by tinytitan on 2017/6/26.
 * 兼容 iOS 的字体设置风格
 */
class UIFont(val size: Float, val isFakeBold: Boolean = false )  {
	
	companion object Factory {
		val zere = UIFont(size = 0f)
		
		fun systemFont(size: Float): UIFont {
			return UIFont(size)
		}
		fun boldSystemFont(size: Float): UIFont {
			return UIFont(size, isFakeBold = true)
		}
		
		fun systemFont(size: Int): UIFont {
			return UIFont(size.toFloat())
		}
		fun boldSystemFont(size: Int): UIFont {
			return UIFont(size.toFloat(), isFakeBold = true)
		}
	}
}
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mh.anko.uikit

import android.content.*
import android.content.res.*
import android.graphics.*
import android.graphics.drawable.*
import android.support.v4.graphics.drawable.*
import com.mh.anko.*


/**
 * Drawable which delegates all calls to it's wrapped [Drawable].
 * 兼容 iOS 中 UIImage的 概念
 * @hide
 */
class UIImage(val named: String? = null, val context: Context? = null) : Drawable(), Drawable.Callback {
	
	// 架构需要初始化 UIKit == 目前只需要 图片模拟！！！
	companion object {
		
		// 获取 ID
		fun getBitmapByName(name: String): Int? {
			val appInfo = context.applicationInfo
			return context.resources.getIdentifier(name, "drawable", appInfo.packageName)
		}
		
		// 暴露给自定义组件进行， 模拟的上下文
		var context: Context
			get() = _context
			set(value) {}  // 不能设置的属性
		// 内部使用的上下文 == 只能由 CAConfig 通过 init 方式初始化 一次
		internal lateinit var _context: Context// 通用的上下文， 应用启动的时候需要初始化好这个玩意！！
	}
	
	private var wrappedDrawable: Drawable? = null
		set(drawable) {
			if (wrappedDrawable != null) {
				wrappedDrawable!!.callback = null
			}
			
			field = drawable
			
			if (drawable != null) {
				drawable.callback = this
			}
		}
	
	init {
		
		var realContext = context
		if (realContext == null) {
			realContext = Companion.context
		}
		
		// 没有上下文， 就是空图片
		if (realContext == null || named == null) {
			wrappedDrawable = ColorDrawable(Color.TRANSPARENT)
		} else {
			
			val id = getBitmapByName(named)
			
			if (id == null || id == 0) {
				// wrappedDrawable = ColorDrawable(Color.TRANSPARENT)
				// 开发测试环境，换成一定的色彩代替
				wrappedDrawable = ColorDrawable(Color.RED.withAlphaComponent(0.5))
			} else {
				val realDrawable = realContext?.resources?.getDrawable(id)
				if (realDrawable != null) {
					wrappedDrawable = realDrawable
				} else {
					// wrappedDrawable = ColorDrawable(Color.TRANSPARENT)
					// 开发测试环境，换成一定的色彩代替
					wrappedDrawable = ColorDrawable(Color.RED.withAlphaComponent(0.5))
				}
			}
			
		}
	}
	
	override fun draw(canvas: Canvas) {
		wrappedDrawable!!.draw(canvas)
	}
	
	override fun onBoundsChange(bounds: Rect) {
		wrappedDrawable!!.bounds = bounds
	}
	
	override fun setChangingConfigurations(configs: Int) {
		wrappedDrawable!!.changingConfigurations = configs
	}
	
	override fun getChangingConfigurations(): Int {
		return wrappedDrawable!!.changingConfigurations
	}
	
	override fun setDither(dither: Boolean) {
		wrappedDrawable!!.setDither(dither)
	}
	
	override fun setFilterBitmap(filter: Boolean) {
		wrappedDrawable!!.isFilterBitmap = filter
	}
	
	override fun setAlpha(alpha: Int) {
		wrappedDrawable!!.alpha = alpha
	}
	
	override fun setColorFilter(cf: ColorFilter?) {
		wrappedDrawable!!.colorFilter = cf
	}
	
	override fun isStateful(): Boolean {
		return wrappedDrawable!!.isStateful
	}
	
	override fun setState(stateSet: IntArray): Boolean {
		return wrappedDrawable!!.setState(stateSet)
	}
	
	override fun getState(): IntArray {
		return wrappedDrawable!!.state
	}
	
	override fun jumpToCurrentState() {
		DrawableCompat.jumpToCurrentState(wrappedDrawable!!)
	}
	
	override fun getCurrent(): Drawable {
		return wrappedDrawable!!.current
	}
	
	override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
		return super.setVisible(visible, restart) || wrappedDrawable!!.setVisible(visible, restart)
	}
	
	override fun getOpacity(): Int {
		return wrappedDrawable!!.opacity
	}
	
	override fun getTransparentRegion(): Region? {
		return wrappedDrawable!!.transparentRegion
	}
	
	override fun getIntrinsicWidth(): Int {
		return wrappedDrawable!!.intrinsicWidth
	}
	
	override fun getIntrinsicHeight(): Int {
		return wrappedDrawable!!.intrinsicHeight
	}
	
	override fun getMinimumWidth(): Int {
		return wrappedDrawable!!.minimumWidth
	}
	
	override fun getMinimumHeight(): Int {
		return wrappedDrawable!!.minimumHeight
	}
	
	override fun getPadding(padding: Rect): Boolean {
		return wrappedDrawable!!.getPadding(padding)
	}
	
	/**
	 * {@inheritDoc}
	 */
	override fun invalidateDrawable(who: Drawable) {
		invalidateSelf()
	}
	
	/**
	 * {@inheritDoc}
	 */
	override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
		scheduleSelf(what, `when`)
	}
	
	/**
	 * {@inheritDoc}
	 */
	override fun unscheduleDrawable(who: Drawable, what: Runnable) {
		unscheduleSelf(what)
	}
	
	override fun onLevelChange(level: Int): Boolean {
		return wrappedDrawable!!.setLevel(level)
	}
	
	override fun setAutoMirrored(mirrored: Boolean) {
		DrawableCompat.setAutoMirrored(wrappedDrawable!!, mirrored)
	}
	
	override fun isAutoMirrored(): Boolean {
		return DrawableCompat.isAutoMirrored(wrappedDrawable!!)
	}
	
	override fun setTint(tint: Int) {
		DrawableCompat.setTint(wrappedDrawable!!, tint)
	}
	
	override fun setTintList(tint: ColorStateList?) {
		DrawableCompat.setTintList(wrappedDrawable!!, tint)
	}
	
	override fun setTintMode(tintMode: PorterDuff.Mode) {
		DrawableCompat.setTintMode(wrappedDrawable!!, tintMode)
	}
	
	override fun setHotspot(x: Float, y: Float) {
		DrawableCompat.setHotspot(wrappedDrawable!!, x, y)
	}
	
	override fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) {
		DrawableCompat.setHotspotBounds(wrappedDrawable!!, left, top, right, bottom)
	}
}

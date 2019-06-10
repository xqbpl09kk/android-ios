package com.mh.ca.flow

import android.graphics.Point
import com.mh.anko.uikit.*
import kotlin.properties.Delegates

/**
 * Created by tinytitan on 2017/6/12.
 * MARK: 布局管理器，本质就是 绘制 【坐标】（x,y,w,h）， 布局 支持 两种尺寸的设置， 常规模式 + 像素模式
 */
class Layouter {
	
	private var _adapter: CAFlowCollection by Delegates.notNull<CAFlowCollection>()
	var adapter: CAFlowCollection
		get() {return this._adapter}
		set(value) {}
	fun bindAdapter(adapter: CAFlowCollection) {
		this._adapter = adapter
	}
	
	// MARK：实际存储的 是 像素模式的 间距， 但是外部不支持 像素模式
	private var _minimumLineSpacing: Int = 0
	
	var minimumLineSpacing: Int
		get() = this._minimumLineSpacing
		set(value) {
			this._minimumLineSpacing = value.ddp // 自动转换成 ddp 模式
		}
	
	
	private var _minimumInteritemSpacing: Int = 0
	
	var minimumInteritemSpacing: Int
		get() = this._minimumInteritemSpacing
		set(value) {
			this._minimumInteritemSpacing = value.ddp // 自动转换成 ddp 模式
		}
	
	private var _sectionInset: UIEdgeInsetsMake = UIEdgeInsetsMake(0,0,0,0)
	
	var sectionInset: UIEdgeInsetsMake
		get() = this._sectionInset
		set(value) {
			this._sectionInset = UIEdgeInsetsMake(value.top.ddp, value.left.ddp,
					value.bottom.ddp, value.right.ddp)
		}
	
	// MARK:  当行数为空时，同时强行返回 zero 的边距 == 对视图美化很有帮助
	var sectionInsetZeroWhenNil: Boolean = false
	
	// MARK: 确保为正数
	private var _columnCount: Int = 1
	var columnCount: Int
		get() = this._columnCount
		set(value){
			if (value > 0) { this._columnCount = value }
		}
	
	
	// MARK: 定高方案，最终的打底方案
	private var _rowHeight: Int = 0
	var rowHeight: Int
		get() = this._rowHeight
		set(value) {
			this._rowHeight = value.ddp
		}
	
	private var _headerHeight: Int = 0
	
	var headerHeight: Int
		get() = this._headerHeight
		set(value) {
			this._headerHeight = value.ddp
		}
	
	private var _footerHeight: Int = 0
	
	var footerHeight: Int
		get() = this._footerHeight
		set(value) {
			this._footerHeight = value.ddp
		}
	
	// MARK: 这个需要进行转换
	var rowSizeParser: ((width: Double, section: Int, row: Int)-> CGSize)? = null
	// MARK: 这个会动态 解析出
	var rowHeightParser: ((width: Double, section: Int, row: Int)->Double)? = null
	var headerHeightParser: ((width: Double, section: Int)->Double)? = null
	var footerHeightParser: ((width: Double, section: Int)->Double)? = null
	
}
package com.mh.anko.uikit

import android.content.*
import android.support.v7.widget.*
import android.view.*
import com.mh.anko.*
import com.mh.ca.*
import com.mh.ca.flow.*
import com.mh.ca.recyclerview.adapter.*
import com.mh.ca.recyclerview.decoration.*
import com.mh.ca.recyclerview.layouts.*
import com.tonicartos.superslim.*

/**
 * 支持分页选项， 更好的嵌套滚动， 原生的对于 轮播的支持【嵌入到交互里面】
 */
open class UICollectionView (context: Context) : RecyclerView(context) {
	
	init {
		this.clipToPadding = false
	}
	
	
	var showsHorizontalScrollIndicator: Boolean
		get() = this.isHorizontalScrollBarEnabled
		set(value) {
			this.isHorizontalScrollBarEnabled = value
		}
	
	var showsVerticalScrollIndicator: Boolean
		get() = this.isVerticalScrollBarEnabled
		set(value) {
			this.isVerticalFadingEdgeEnabled = value
		}
	
	// 只能设置一次根视图！
	private var onceOver: Boolean = false
	
	fun child(strategyType: StrategyType): CAFlowCollection {
		
		if (this.onceOver &&  CAConfig.DEV_MODE)  {
			assert(false, { print("集合重复设置顶部 Root FlowCollection") })
		}
		this.flo.strategy(strategyType)
		this.onceOver = true
		return this.flo
	}
	
	// 分场景进行设置， 可以完美的模拟， 各种场景和外观， 统一上提 API
	companion object {
		// 这个就是网格布局的风格，先设置好 控件， 然后进行子元素的布局！！！
		fun flow(direction: UICollectionViewScrollDirection = UICollectionViewScrollDirection.vertical) :  UICollectionView {
			return collectionView(LayoutType.grid, direction == UICollectionViewScrollDirection.horizontal)
		}
		
		// 带刷新  功能的 集合视图， 其实， 布局 和 后续的参数设计是分开的， 然后 内部的布局 自动完成了， 牛逼 【android 还是很灵活的】
		fun refreshFlow(): UIRefreshControl {
			return UIRefreshControl().pipely { it ->
				it.child(collectionView(LayoutType.grid, isHorizontal = false).pipely { it ->
					// 设置包装好的CollectionView的内部布局
					it.layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
				})
			}
		}
		
		// 设置 水平布局
		fun horizontalFlow() :  UICollectionView {
			return collectionView(LayoutType.grid, isHorizontal = true)
		}
		
		fun pagedFlow(direction: UICollectionViewScrollDirection = UICollectionViewScrollDirection.horizontal) :  UIPagedCollectionView {
			return collectionView(LayoutType.grid, direction == UICollectionViewScrollDirection.horizontal, pageEnable = true) as UIPagedCollectionView
		}
		
		// 内部实现原理， 其实对应关系， 可能会发生变化， 但是 DSL 会规定支持的"共享功能集合"
		private fun collectionView(
				layoutType: LayoutType = LayoutType.grid,
				isHorizontal:Boolean = false, pageEnable: Boolean = false): UICollectionView {
			
			var collectionView = when(pageEnable) {
				true -> UIPagedCollectionView()
				false -> UICollectionView()
			}
			
			// 装配默认的数据源适配器
			val adapter = SectionedRecyclerViewDataSourceAdapter(collectionView.flo, collectionView)
			collectionView.adapter = adapter
			
			// 启用不同的 布局方式
			when (layoutType) {
				LayoutType.slim -> 	collectionView.layoutManager = LayoutManager(collectionView.context)
				LayoutType.grid -> {
					val layoutManager = GridLayoutManager(collectionView.context, GridLayoutSpanSizeLookupAdapter.MAX_SPAN_SIZE)
					// 内部底层的解析器
					val spanSizeLookup = GridLayoutSpanSizeLookupAdapter(adapter, collectionView.flo, layoutManager)
					layoutManager.spanSizeLookup = spanSizeLookup
					collectionView.layoutManager = layoutManager
					// 默认是纵向布局！！！
					if(isHorizontal){
						layoutManager.orientation = GridLayoutManager.HORIZONTAL
					}else {
						layoutManager.orientation = GridLayoutManager.VERTICAL
					}
				}
				LayoutType.flow -> {
					collectionView.layoutManager = FlowLayoutManager()
				}
			}
			// 添加间隔装饰
			val itemDecoration = RecyclerViewItemDecorationAdapter(adapter, collectionView.flo, isHorizontal)
			
			collectionView.addItemDecoration(itemDecoration)
			
			// 设置滚动监听的转发和 offset的模拟
			collectionView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)
					// 转发滚动请求到广播中
					val position = collectionView.getContentOffset()
					collectionView.flo.scrollBroadCaster.handleScroll(position)
				}
			})
			return collectionView
		}
	}
	
	
	constructor():this(UIKit._context) // 默认构造函数
	
	// ===========  来源于 BetterRecyclerView ===========
	private val INVALID_POINTER = -1
	private var scrollPointerId = INVALID_POINTER
	private var initialTouchX = 0
	private var initialTouchY = 0
	private var touchSlop = 0
	
	// 设置滚动系数
	override fun setScrollingTouchSlop(slopConstant: Int) {
		super.setScrollingTouchSlop(slopConstant)
		
		val vc = android.view.ViewConfiguration.get(context)
		when (slopConstant) {
			TOUCH_SLOP_DEFAULT -> touchSlop = vc.scaledTouchSlop
			TOUCH_SLOP_PAGING -> touchSlop = android.support.v4.view.ViewConfigurationCompat.getScaledPagingTouchSlop(vc)
		}
	}
	
	override fun onInterceptTouchEvent(e: android.view.MotionEvent?): Boolean {
		if(e == null) {
			return false
		}
		
		val action = android.support.v4.view.MotionEventCompat.getActionMasked(e)
		val actionIndex = android.support.v4.view.MotionEventCompat.getActionIndex(e);
		
		when (action) {
			android.view.MotionEvent.ACTION_DOWN -> {
				scrollPointerId = android.support.v4.view.MotionEventCompat.getPointerId(e, 0)
				initialTouchX = Math.round(e.x + 0.5f)
				initialTouchY = Math.round(e.y + 0.5f)
				return super.onInterceptTouchEvent(e)
			}
			
			android.view.MotionEvent.ACTION_POINTER_DOWN -> {
				scrollPointerId = android.support.v4.view.MotionEventCompat.getPointerId(e, actionIndex)
				initialTouchX = Math.round(android.support.v4.view.MotionEventCompat.getX(e, actionIndex) + 0.5f)
				initialTouchY = Math.round(android.support.v4.view.MotionEventCompat.getY(e, actionIndex) + 0.5f)
				return super.onInterceptTouchEvent(e)
			}
			
			android.view.MotionEvent.ACTION_MOVE -> {
				val index = android.support.v4.view.MotionEventCompat.findPointerIndex(e, scrollPointerId)
				if(index < 0) {
					return false
				}
				
				val x = Math.round(android.support.v4.view.MotionEventCompat.getX(e, index) + 0.5f)
				val y = Math.round(android.support.v4.view.MotionEventCompat.getY(e, index) + 0.5f)
				if(scrollState != SCROLL_STATE_DRAGGING) {
					val dx = x - initialTouchX
					val dy = y - initialTouchY
					var startScroll = false;
					if(layoutManager.canScrollHorizontally() && Math.abs(dx) > touchSlop && (layoutManager.canScrollVertically() || Math.abs(dx) > Math.abs(dy))) {
						startScroll = true
					}
					if(layoutManager.canScrollVertically() && Math.abs(dy) > touchSlop && (layoutManager.canScrollHorizontally() || Math.abs(dy) > Math.abs(dx))) {
						startScroll = true
					}
					return startScroll && super.onInterceptTouchEvent(e)
				}
				
				return super.onInterceptTouchEvent(e)
			}
			
			else -> {
				return super.onInterceptTouchEvent(e)
			}
		}
	}
	
	// ===========  来源于 BetterRecyclerView ============
	init {
		
		
		// 默认不出现 遮罩阴影 == iOS 风格
		overScrollMode = android.view.View.OVER_SCROLL_NEVER
		
		// 获取缩放系数
		val vc = android.view.ViewConfiguration.get(context)
		touchSlop = vc.scaledTouchSlop
	}
}


// 下拉刷新的场景 只需要支持简单的 集合视图， 不需要分页的功能
var UIRefreshControl.collectionView: UICollectionView
	get()  {
		
		// 意思更清晰
		(0..(this.childCount - 1))
				.map { getChildAt(it) }
				.filterIsInstance<UICollectionView>()
				.forEach { return it }
		
		return UICollectionView() // 不应该到这里
	}
	set(value) {}


// MARK： 这个就是一个套路，，， 一定要做准讯 统一的 API 实现 UI 的统一绘制， 牛逼
var UICollectionView.refreshControl: UIRefreshControl
	get() {
		return this.parent as? UIRefreshControl ?: UIRefreshControl()
	}
	set(value) {}
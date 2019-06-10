package com.mh.anko.uikit

/**
 * 支持分页选项， 更好的嵌套滚动， 原生的对于 轮播的支持【嵌入到交互里面】
 */
class UIPagedCollectionView (context: android.content.Context) : UICollectionView(context) {
	
	constructor():this(UIKit.context) // 默认构造函数
	
	/**
	 * Set interval for one slide in milliseconds.

	 * @param slideInterval integer
	 */
	var slideInterval = com.mh.anko.uikit.UIPagedCollectionView.Companion.DEFAULT_SLIDE_INTERVAL
		set(slideInterval) {
			field = slideInterval
			playCarousel()
		}
	private var swipeTimer: java.util.Timer? = null
	private var swipeTask: com.mh.anko.uikit.UIPagedCollectionView.SwipeTask? = null

	var autoPlay = false
	var disableAutoPlayOnUserInteraction = true

	/**
	 * Set interval for one slide in milliseconds.

	 * @param slideInterval integer
	 */
	fun reSetSlideInterval(slideInterval: Int) {
		this.slideInterval = slideInterval
		playCarousel()
	}

	private fun stopScrollTimer() {

		if (null != swipeTimer) {
			swipeTimer!!.cancel()
		}

		if (null != swipeTask) {
			swipeTask!!.cancel()
		}
	}


	private fun resetScrollTimer() {

		stopScrollTimer()

		swipeTask = SwipeTask()
		swipeTimer = java.util.Timer()

	}

	/**
	 * Starts auto scrolling if
	 */
	fun playCarousel() {

		resetScrollTimer()

		if (autoPlay && this.slideInterval > 0 && adapter != null && adapter.itemCount > 1) {

			swipeTimer!!.schedule(swipeTask, this.slideInterval.toLong(), this.slideInterval.toLong())
		}
	}

	/**
	 * Pause auto scrolling unless user interacts provided autoPlay is enabled.
	 */
	fun pauseCarousel() {

		resetScrollTimer()
	}

	/**
	 * Stops auto scrolling.
	 */
	fun stopCarousel() {

		this.autoPlay = false
	}

	// 轮播任务
	private inner class SwipeTask : java.util.TimerTask() {
		override fun run() {
			this@UIPagedCollectionView.post {
				if (this@UIPagedCollectionView.horizontalPagingEnable) {
					this@UIPagedCollectionView.scrollToNextPageX()
				} else if (this@UIPagedCollectionView.verticalPagingEnable) {

				}
			}
		}
	}

	// 做资源清理，比如Timer计时器资源
	override fun onDetachedFromWindow() {
		android.util.Log.d(com.mh.anko.uikit.UIPagedCollectionView.Companion.TAG, "脱离窗体，进行清理")
		stopScrollTimer()
		super.onDetachedFromWindow()
	}

	// 抛出速度的减速因子
	private val mFlingFactor = 0.15f


	private var shortestDistanceX: Int = 0 // 超过此距离的滑动才有效
	private var slideDistanceX = 0 // 滑动的距离
	private var scrollX = 0f // X轴当前的位置

	// 页面序号从1开始，这个比较奇怪？
	private var currentPageX = 1 // 当前页

	// 采用闭包写法
	var horizontalPageChangedListener: ((currentPage: Int) -> Unit)? = null
	

	// 是否激活分页
	var horizontalPagingEnable = false

	private var shortestDistanceY: Int = 0 // 超过此距离的滑动才有效
	private var slideDistanceY = 0 // 滑动的距离
	private var scrollY = 0f // X轴当前的位置

	// 页面序号从1开始，这个比较奇怪？
	private var currentPageY = 1 // 当前页

	var verticalPageChangedListener: ((currentPage: Int) -> Void)? = null

	// 是否激活分页
	var verticalPagingEnable = false


	/*
	 * 0: 停止滚动且手指移开; 1: 开始滚动; 2: 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
	 */
	private var _scrollState = 0 // 滚动状态
	
	override fun onMeasure(widthSpec: Int, heightSpec: Int) {
		super.onMeasure(widthSpec, heightSpec)
		shortestDistanceX = measuredWidth / 3
		shortestDistanceY = measuredHeight / 3
	}

	// 拦截时间对交互进行切换处理
	override fun onTouchEvent(event: android.view.MotionEvent): Boolean {

		if (event.action == android.view.MotionEvent.ACTION_DOWN || event.action == android.view.MotionEvent.ACTION_MOVE) {

			android.util.Log.d(com.mh.anko.uikit.UIPagedCollectionView.Companion.TAG, "DOWN")
			//  切换到手动滚动状态
			if (autoPlay && disableAutoPlayOnUserInteraction) {
				// 暂时终止Timer
				android.util.Log.d(com.mh.anko.uikit.UIPagedCollectionView.Companion.TAG, "停止Timer")
				pauseCarousel()
			}
		}
		if (event.action == android.view.MotionEvent.ACTION_UP) {
			android.util.Log.d(com.mh.anko.uikit.UIPagedCollectionView.Companion.TAG, "UP")

			if (autoPlay && disableAutoPlayOnUserInteraction) {
				android.util.Log.d(com.mh.anko.uikit.UIPagedCollectionView.Companion.TAG, "重启Timer")
				playCarousel()
			}
		}

		return super.onTouchEvent(event)
	}

	override fun onScrollStateChanged(state: Int) {

		if (horizontalPagingEnable) {
			handleHorizontalPaging(state)
		}

		if (verticalPagingEnable) {
			handleVerticalPaging(state)
		}

		super.onScrollStateChanged(state)
	}

	override fun fling(velocityX: Int, velocityY: Int): Boolean {


		val flinging = super.fling((velocityX * mFlingFactor).toInt(), (velocityY * mFlingFactor).toInt())
		if (flinging) {

			// 速度降低下拉： 刚好抛到 边界上！！！
			if (horizontalPagingEnable) {
				adjustPositionX(velocityX)
			}

			if (verticalPagingEnable) {
				adjustPositionY(velocityY)
			}
		}
		return flinging
	}

	//  调整位置
	private fun adjustPositionX(velocityX: Int) {
		if (velocityX < 0) {
			// 计算出页面
			if (currentPageX > 1) {
				currentPageX -= 1
			}
		} else {
			currentPageX += 1
		}

		// 最后一页 怎么抛掷都不能继续前进了！！
		if (currentPageX > adapter.itemCount) {
			currentPageX = adapter.itemCount
		}

		scrollToTargetPageX()

	}

	// 滚动到目标页面
	private fun scrollToTargetPageX() {
		// 执行自动滚动
		smoothScrollBy(((currentPageX - 1) * width - scrollX).toInt(), 0)
		// 通知页面变化
		horizontalPageChangedListener?.invoke(currentPageX - 1)
		
		slideDistanceX = 0
	}

	// 滚动到目标页面
	private fun scrollToTargetPageY() {
		// 执行自动滚动
		smoothScrollBy(((currentPageY - 1) * height - scrollY).toInt(), 0)
		verticalPageChangedListener?.invoke(currentPageX - 1)
		slideDistanceY = 0
	}

	// 滚动到下一个位置
	fun scrollToNextPageX() {
		if (horizontalPagingEnable) {
			currentPageX += 1

			if (currentPageX > adapter.itemCount) {
				currentPageX = 1
			}
		}
		scrollToTargetPageX()
	}

	// 滚动到下一个位置
	fun scrollToNextPageY() {
		if (verticalPagingEnable) {
			currentPageY += 1

			if (currentPageY > adapter.itemCount) {
				currentPageY = 1
			}
		}
		scrollToTargetPageY()
	}


	//  调整位置
	private fun adjustPositionY(velocityY: Int) {
		if (velocityY < 0) {
			// 计算出页面
			if (currentPageY > 1) {
				currentPageY -= 1
			}
		} else {
			currentPageY += 1
		}

		// 最后一页 怎么抛掷都不能继续前进了！！
		if (currentPageY > adapter.itemCount) {
			currentPageY = adapter.itemCount
		}

		scrollToTargetPageY()

	}

	// 根据状态变化进行滚动调整
	private fun handleHorizontalPaging(state: Int) {
		when (state) {
			1 -> _scrollState = 1
			2 -> _scrollState = 2
			0 -> {
				if (slideDistanceX == 0) {
					return
				}
				_scrollState = 0
				if (slideDistanceX < 0) { // 上页
					currentPageX = Math.ceil((scrollX / width).toDouble()).toInt()
					if (currentPageX * width - scrollX < shortestDistanceX) {
						currentPageX += 1
					}
				} else { // 下页
					currentPageX = Math.ceil((scrollX / width).toDouble()).toInt() + 1

					if (currentPageX <= adapter.itemCount) {
						if (scrollX - (currentPageX - 2) * width < shortestDistanceX) {
							// 如果这一页滑出距离不足，则定位到前一页
							currentPageX -= 1
						}
					} else {
						currentPageX = adapter.itemCount
					}

				}
				// 执行自动滚动
				smoothScrollBy(((currentPageX - 1) * width - scrollX).toInt(), 0)

				// 通知页面变化
				if (horizontalPageChangedListener != null) {
					horizontalPageChangedListener!!.invoke(currentPageX - 1)
				}
				slideDistanceX = 0
			}
		}
	}


	// 根据状态变化进行滚动调整
	private fun handleVerticalPaging(state: Int) {
		when (state) {
			2 -> _scrollState = 2
			1 -> _scrollState = 1
			0 -> {
				if (slideDistanceY == 0) {
					return
				}
				_scrollState = 0
				if (slideDistanceY < 0) { // 上页
					currentPageY = Math.ceil((scrollY / height).toDouble()).toInt()
					if (currentPageY * height - scrollY < shortestDistanceY) {
						currentPageY += 1
					}
				} else { // 下页
					currentPageY = Math.ceil((scrollY / height).toDouble()).toInt() + 1

					if (currentPageY <= adapter.itemCount) {
						if (scrollY - (currentPageY - 2) * height < shortestDistanceY) {
							// 如果这一页滑出距离不足，则定位到前一页
							currentPageY -= 1
						}
					} else {
						currentPageY = adapter.itemCount
					}

				}
				// 执行自动滚动
				smoothScrollBy(((currentPageY - 1) * height - scrollY).toInt(), 0)
				// 修改指示器选中项
				//mIndicatorView.setSelectedPage(currentPageX - 1);

				if (verticalPageChangedListener != null) {
					verticalPageChangedListener!!.invoke(currentPageX - 1)
				}

				slideDistanceY = 0
			}
		}
	}

	override fun onScrolled(dx: Int, dy: Int) {
		scrollX += dx.toFloat()
		if (_scrollState == 1) {
			slideDistanceX += dx
		}
		// 自己添加的
		scrollY += dy.toFloat()
		if (_scrollState == 1) {
			slideDistanceY += dy
		}
		super.onScrolled(dx, dy)
	}

	companion object {

		private val TAG = "PageRecyclerView"

		private val DEFAULT_SLIDE_INTERVAL = 3500
	}
}
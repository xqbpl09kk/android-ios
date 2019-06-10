package com.mh.ca

import android.support.v7.widget.*
import com.mh.anko.uikit.*
import com.mh.ca.flow.*
import com.mh.ca.recyclerview.adapter.*
import com.mh.ca.recyclerview.view.*
import com.mh.ext.*
import com.tonicartos.superslim.*

/**
 * Created by tinytitan on 2017/6/13.
 * 扩展 recyclerView 直接添加 flo 属性， 并设置相关的 外观函数
 */

// 扩展一个 私有的 _flo 属性
private var RecyclerView._flo: CAFlowCollection by FieldProperty {
	CAFlowCollection().pipely { it ->
		it.containerView = this
	}
}

var RecyclerView.flo: CAFlowCollection
	get() {
		if (this._flo == null) {
			val flow = CAFlowCollection()
			flow.containerView = this
			this._flo = flow
		}
		return this._flo!!
	}
	set(value) {}


// 目前支持 3种 布局类型
enum class LayoutType {
	slim,
	flow,
	grid
}

// MARK:  只有在 UICV本身 添加监听才有意义
public fun RecyclerView.appendSubHandler( handler: CAScrollHandlerType?) {
	this.flo.scrollBroadCaster.appendSubHandler(handler)
}

public fun RecyclerView.appendSubHandler(handler: (offset: CGPoint) -> Unit) {
	this.flo.scrollBroadCaster.appendSubHandler(handler)
}


// MARK： 手动选取设置
fun RecyclerView.selectItem(section: Int, row: Int) {
	(this.adapter as? SectionedRecyclerViewDataSourceAdapter)?.selectItem(section, row)
}

public var CAFlowCollection.collectionView: RecyclerView?
	get() = this.containerView as? RecyclerView ?: null
	set(value) {}


// MARK: 总是进行全局的更新
fun CAFlowCollection.reloadData(): Unit {
	this.collectionView?.adapter?.notifyDataSetChanged()
}

fun RecyclerView.reloadData(): Unit {
	this.adapter.notifyDataSetChanged()
}

// 计算逻辑需要

// MARK: 这个是专门用来给 涵盖的 section 列表使用的 == 需要根据 type 进行 branch 处理
fun CAFlowCollection.reloadFlow(payload: CAPayload? = null): Unit {
	// 这个肯定是不为空的, 并且是配套的内部类型
	val adapter = this.collectionView?.adapter
			as? SectionedRecyclerViewDataSourceAdapter?: return
	
	when(this.strategy.type) {
		StrategyType.singleton -> {
			if (!this.strategy.visiable) return
			
			val first = adapter.getStartItemPositionForSection(this.sectionStart)
			
			adapter.notifyItemChanged(first + this.rowStart, payload)
		}
	}
}

fun CAFlowCollection.reloadCollection(): Unit {
	// 这个肯定是不为空的, 并且是配套的内部类型
	val adapter = this.collectionView?.adapter
			as? SectionedRecyclerViewDataSourceAdapter ?: return
	val first = adapter.getFirstPositionForSection(this.sectionStart)
	var totalCount = adapter.itemCount
	if (adapter.hasFooterInSection(this.sectionStart)) {
		totalCount += 1
	}
	if (adapter.hasHeaderInSection(this.sectionStart)) {
		totalCount += 1
	}
	adapter.notifyItemRangeChanged(first, totalCount)
}

fun CAFlowCollection.reloadSingleton(payload: CAPayload? = null): Unit {
	this.reloadFlow(payload)
}


// MARK: 这个是专门用来给 涵盖的 section 列表使用的 == 需要根据 type 进行 branch 处理
fun CAFlowCollection.deleteItem(position: Int): Unit {
	// 这个肯定是不为空的, 并且是配套的内部类型
	val adapter = this.collectionView?.adapter
			as? SectionedRecyclerViewDataSourceAdapter?: return
	
	// 只有 列表需要支持 删除操作 == 这个很强大 哈哈
	when(this.strategy.type) {
		StrategyType.list -> {
			if (position < 0 || position > this.rowCount -1) {
				return // 非法的位置
			}
			// 初始位置
			val startItem = adapter.getStartItemPositionForSection(this.sectionStart)
			//  要加上偏移量
			val deleteIndex = startItem + position + this.rowStart
			// 打补丁 == 如果是第一个元素删除 就是全部刷新， 垃圾啊
			if (deleteIndex == 0) {
				reloadData()
				return
			}
			
			// reloadData()
			
			val count = adapter.itemCount
			adapter.notifyItemRemoved(deleteIndex)
			
			// 连带刷新 后面的 组件
			if (count - 1 > deleteIndex){
				adapter.notifyItemRangeChanged(deleteIndex, count -1 - deleteIndex)
			}
		}
	}
}

// MARK: 这个是专门用来给 涵盖的 section 列表使用的 == 需要根据 type 进行 branch 处理
fun CAFlowCollection.reloadItem(position: Int): Unit {
	// 这个肯定是不为空的, 并且是配套的内部类型
	val adapter = this.collectionView?.adapter
			as? SectionedRecyclerViewDataSourceAdapter ?: return
	
	// 只有 列表需要支持 删除操作 == 这个很强大 哈哈
	when (this.strategy.type) {
		StrategyType.list -> {
			if (position < 0 || position > this.rowCount - 1) {
				return // 非法的位置
			}
			val startItem = adapter.getStartItemPositionForSection(this.sectionStart)
			adapter.notifyItemChanged(startItem + position)
		}
	}
}

// MARK: DSL， 大多数都是往后面添加元素， 非常直观 == 业务高度定制
fun CAFlowCollection.appendItem(count: Int): Unit {
	// 这个肯定是不为空的, 并且是配套的内部类型
	val adapter = this.collectionView?.adapter
			as? SectionedRecyclerViewDataSourceAdapter?: return
	
	// 只有 列表需要支持 删除操作 == 这个很强大 哈哈
	when(this.strategy.type) {
		StrategyType.list -> {
			
			val totalCount = adapter.itemCount
			// 初始位置
			val startItem = adapter.getStartItemPositionForSection(this.sectionStart)
			val remainCount = totalCount - startItem - rowCount
			val rowCount = this.rowCount
			adapter.notifyItemRangeInserted(startItem + rowCount, count)
			
			// 尾部多出来的数量, 一般添加后由 HINT 减少， 所以放到后面 调整计算更新量！
			if (remainCount > 0) {
				adapter.notifyItemRangeChanged(startItem + rowCount + count, remainCount)
			}
		}
	}
}

// MARK: DSL， 大多数都是往后面添加元素， 非常直观 == 业务高度定制
fun CAFlowCollection.insertItems(index: Int, count: Int): Unit {
	// 这个肯定是不为空的, 并且是配套的内部类型
	val adapter = this.collectionView?.adapter
			as? SectionedRecyclerViewDataSourceAdapter ?: return
	
	// 只有 列表需要支持 删除操作 == 这个很强大 哈哈
	when (this.strategy.type) {
		StrategyType.list -> {
			
			val totalCount = adapter.itemCount
			// 初始位置
			val startItem = adapter.getStartItemPositionForSection(this.sectionStart)
			val remainCount = totalCount - startItem - rowCount
			val rowCount = this.rowCount
			
			if (index < 0 || index > rowCount) {
				return
			}
			
			adapter.notifyItemRangeInserted(startItem + index, count)
			
			// 尾部多出来的数量, 一般添加后由 HINT 减少， 所以放到后面 调整计算更新量！
			if (remainCount > 0) {
				adapter.notifyItemRangeChanged(startItem + rowCount + count, remainCount)
			}
		}
	}
}



// 获取 RecyclerView 的偏移量

// MARK: 相当于iOS中的contentOffset，只不过只能检测第一个元素可视的情况 以及边界值
fun RecyclerView.getContentOffset(): CGPoint {
	val layoutManager = this.layoutManager
	
	when(layoutManager) {
		is LayoutManager -> {
			val firstItem = layoutManager.findFirstVisibleItemPosition()
			val lastItem = layoutManager.findLastVisibleItemPosition()
			//  MMRK: 嵌套在XRecyclerView中所以第一个要 + 1
			var targetItem = 0
			// 已经滚过去了，代表top已经很大了
			if(targetItem < firstItem) {
				return CGPoint(Int.MAX_VALUE, Int.MAX_VALUE)
			}
			// 代表下拉的好狠，都不见了
			if(targetItem > lastItem) {
				return CGPoint(Int.MIN_VALUE, Int.MIN_VALUE)
			}
			val n = targetItem - firstItem
			val top = this.getChildAt(n).top
			val left = this.getChildAt(n).left
			// MARK: iOS中滚出去的offset跟第一个元素的top坐标刚好是反的！！
			return CGPoint(-left.px2ddp.toInt(), -top.px2ddp.toInt())  // 自动转换成 点位
		}
		is LinearLayoutManager -> {
			val firstItem = layoutManager.findFirstVisibleItemPosition()
			val lastItem = layoutManager.findLastVisibleItemPosition()
			//  MMRK: 嵌套在XRecyclerView中所以第一个要 + 1
			var targetItem = 0
			// 已经滚过去了，代表top已经很大了
			if(targetItem < firstItem) {
				return CGPoint(Int.MAX_VALUE, Int.MAX_VALUE)
			}
			// 代表下拉的好狠，都不见了
			if(targetItem > lastItem) {
				return CGPoint(Int.MIN_VALUE, Int.MIN_VALUE)
			}
			val n = targetItem - firstItem
			val top = this.getChildAt(n).top
			val left = this.getChildAt(n).left
			// MARK: iOS中滚出去的offset跟第一个元素的top坐标刚好是反的！！
			return CGPoint(-left.px2ddp.toInt(), -top.px2ddp.toInt())  // 自动转换成 点位
		}
		is GridLayoutManager -> {
			val firstItem = layoutManager.findFirstVisibleItemPosition()
			val lastItem = layoutManager.findLastVisibleItemPosition()
			//  MMRK: 嵌套在XRecyclerView中所以第一个要 + 1
			var targetItem = 0
			// 已经滚过去了，代表top已经很大了
			if(targetItem < firstItem) {
				return CGPoint(Int.MAX_VALUE, Int.MAX_VALUE)
			}
			// 代表下拉的好狠，都不见了
			if(targetItem > lastItem) {
				return CGPoint(Int.MIN_VALUE, Int.MIN_VALUE)
			}
			val n = targetItem - firstItem
			val top = this.getChildAt(n).top
			val left = this.getChildAt(n).left
			// MARK: iOS中滚出去的offset跟第一个元素的top坐标刚好是反的！！
			return CGPoint(-left.px2ddp.toInt(), -top.px2ddp.toInt())  // 自动转换成 点位
		}
		
		is StaggeredGridLayoutManager -> {
			
			// 瀑布流 最多三列
			val firstItems = IntArray(2)
			layoutManager.findFirstCompletelyVisibleItemPositions(firstItems)
			val firstItem = Math.min(firstItems[0], firstItems[1])
			
			val lastItems = IntArray(2)
			layoutManager.findLastCompletelyVisibleItemPositions(lastItems)
			val lastItem = Math.max(lastItems[0], lastItems[1])
			
			
			//  MMRK: 嵌套在XRecyclerView中所以第一个要 + 1
			var targetItem = 0
			// 已经滚过去了，代表top已经很大了
			// 已经滚过去了，代表top已经很大了
			if(targetItem < firstItem) {
				return CGPoint(Int.MAX_VALUE, Int.MAX_VALUE)
			}
			// 代表下拉的好狠，都不见了
			if(targetItem > lastItem) {
				return CGPoint(Int.MIN_VALUE, Int.MIN_VALUE)
			}
			val n = targetItem - firstItem
			val top = this.getChildAt(n).top
			val left = this.getChildAt(n).left
			// MARK: iOS中滚出去的offset跟第一个元素的top坐标刚好是反的！！
			return CGPoint(-left.px2ddp.toInt(), -top.px2ddp.toInt())  // 自动转换成 点位
		}
		else -> {
			return CGPoint(0,0)
		}
	}
}






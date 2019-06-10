package com.mh.ca.flow

import com.mh.ca.*
import kotlin.properties.*
import kotlin.reflect.*

/**
 * Created by tinytitan on 2017/6/12.
 * MARK: 专注于 数据到视图的映射
 */
class Mapper {
	
	companion object {
		val INVALID_TYPE: Int = -1
		
		// 权重 配比 == 占位 10 进制 《取模 + 整除 即可算 Type》
		val SECTION_BASE = 100000
		val TAG_BASE = 10000
		val HEADER_BASE = 1000
		val FOOTER_BASE = 100
		
		// ITEM 可以占据 2个位置
		val ITEM_BASE = 1
		
		// 判定是否头尾部卡片
		fun isSectionHeaderViewType(viewType: Int): Boolean {
			val header = viewType / HEADER_BASE
			val mod = header % 10
			return mod > 0
		}
		
		fun isSectionFooterViewType(viewType: Int): Boolean {
			val footer = viewType / FOOTER_BASE
			val mod = footer % 10
			return mod > 0
		}
	}
	
	private var _adapter: CAFlowCollection by Delegates.notNull<CAFlowCollection>()
	var adapter: CAFlowCollection
		get() {return this._adapter}
		set(value) {}
	fun bindAdapter(adapter: CAFlowCollection) {
		this._adapter = adapter
	}
	
	
	
	// 是否支持Stickey头部 == 这个是 Android 特有的补丁
	public var allowsStickeyHeader: Boolean = false
	// 头部是否 overlay【悬浮】的方式展现，默认 inline
	public var  isOverlayHeader: Boolean = false
	
	// MARK:  是否开启头尾部的广播 == 也就是 ROW也能收到 头部数据，这个对于【单卡压缩】有效
	public var enableRowHeader: Boolean = false
	public var enableRowFooter: Boolean = false
	
	// MARK:  是否开启附加数据,区分三种场景，需要单独设置
	public var enableHeaderExtra: Boolean = false
	public var enableFooterExtra: Boolean = false
	public var enableRowExtra: Boolean = false
	
	// MARK: 是否包装装饰上下文数据: 目前只支持 ROW，更简单
	public var enableRowDecor: Boolean = false
	
	// MARK: 是否开启滚动感知, 当然 CELL 要对应实现 对应接口
	public var enableHeaderScrollAware: Boolean = false
	public var enableFooterScrollAware: Boolean = false
	public var enableRowScrollAware: Boolean = false
	
	// MARK: 映射的目标 CELL
	public var rowViews: Array<KClass<out Any>> = emptyArray()
	public var headerViews: Array<KClass<out Any>> = emptyArray()
	public var footerViews: Array<KClass<out Any>> = emptyArray()
	
	public var rowViewParser: ((rowData: Any?) -> KClass<out Any>?)? = null
	public var headerViewParser: ((headerData: Any?) -> KClass<out Any>?)? = null
	public var footerViewParser: ((footerData: Any?) -> KClass<out Any>?)? = null
	
	
	// 获取 卡片 类型， 并 根据 单卡的 配置， 设置是否复用！！！
	fun getItemType(viewKlass: KClass<out Any>): Int {
		val index = rowViews.indexOf(viewKlass)
		if (index < 0 || index > 98) return INVALID_TYPE
		
		var type = (adapter.sectionStart + 1) * SECTION_BASE + (adapter.tag + 1) * TAG_BASE
		type += (index + 1) * ITEM_BASE
		
		// 不允许复用， 这个对于轮播 + 图片上传 等很关键
		if (adapter.strategy.type == StrategyType.singleton && adapter.strategy.disableReuse) {
			adapter.collectionView?.recycledViewPool?.setMaxRecycledViews(type, 0)
		}
		
		return type
	}
	
	fun getFooterType(viewKlass: KClass<out Any>): Int {
		val index = footerViews.indexOf(viewKlass)
		if (index < 0 || index > 8) return INVALID_TYPE
		
		var type = (adapter.sectionStart + 1) * SECTION_BASE + (adapter.tag + 1) * TAG_BASE
		type += (index + 1) * FOOTER_BASE
		
		return type
	}
	
	fun getHeaderType(viewKlass: KClass<out Any>): Int {
		val index = headerViews.indexOf(viewKlass)
		if (index < 0 || index > 8) return INVALID_TYPE
		
		var type = (adapter.sectionStart + 1) * SECTION_BASE + (adapter.tag + 1) * TAG_BASE
		type += (index + 1) * HEADER_BASE
		
		return type
	}
	
	// MARK: 根据数据内容获取行数，这个规则比较明确 == repeated 类型的, 之前数据获取逻辑压根不在这里
	fun rowCount(data: Any?) : Int {
		when (adapter.strategy.type) {
			// 可以才有 行
			StrategyType.singleton -> {
				if( adapter.strategy.visiable) {
					return 1
				}else {
					return 0
				}
			}
			StrategyType.optinal -> if(data == null) {return 0} else {return 1}
			StrategyType.list, StrategyType.repeatList -> {
				val items = data as? List<*> ?: return 0
				return items.count()
			}
			StrategyType.placeholder -> return rowViews.count()
			else -> return 0
		}
	}
	
	// MARK: 根据数据内容，获取对应行的 数据 == 与 行数解析其实有对应关系
	fun rowData(data: Any?, row: Int) : Any? {
		when(adapter.strategy.type) {
			StrategyType.singleton, StrategyType.optinal -> return data
			
			StrategyType.list, StrategyType.repeatList, StrategyType.placeholder -> {
				val items = data as? List<*> ?: return null
				
				if(row >= adapter.rowStart && row < adapter.rowStart + items.count()) {
					return items[row + adapter.rowStart]
				}else {
					return null
				}
			}
			else -> return null
		}
	}
	
	// MARK：  根据数据拿到视图 == 建立数据 与 视图的绑定关系，默认实现就是 第一个 CELL
	fun rowView(rowData: Any?) : KClass<out Any>? {
		if (rowViewParser != null) { return rowViewParser?.invoke(rowData) }
		if (rowViews.count() > 0) { return rowViews[0] }
		return null
	}
	
	// MARK: 统一注册到 注册中心里面， 类似于iOS注册到 UICollectionView 中道理一样
	
	
	// MARK： 内部判断是否有 头部 + 尾部 == Android 独有的补丁
	fun hasHeaderView(): Boolean {
		if (adapter.strategy.type == StrategyType.singleton) {
			return headerViews.count() > 0 && adapter.strategy.visiable
		}
		return headerViews.count() > 0
	}
	fun hasFooterView(): Boolean {
		// 不可见， 那么都不可见
		if (adapter.strategy.type == StrategyType.singleton) {
			return headerViews.count() > 0 && adapter.strategy.visiable
		}
		return footerViews.count() > 0
	}
	
	fun headerView(headerData: Any?) : KClass<out Any>? {
		if (headerViewParser != null) { return headerViewParser?.invoke(headerData) }
		if (headerViews.count() > 0) { return headerViews[0] }
		return null
	}
	
	fun footerView(footerData: Any?) : KClass<out Any>? {
		if (footerViewParser != null) { return footerViewParser?.invoke(footerData) }
		if (footerViews.count() > 0) { return footerViews[0] }
		return null
	}
}
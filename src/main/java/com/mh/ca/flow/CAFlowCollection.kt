package com.mh.ca.flow

import android.view.*
import com.mh.anko.*
import com.mh.ca.*

/**
 * Created by tinytitan on 2017/6/12.
 * 平台透明的方式，对集合视图进行建模，然后将模型 bind 到 UICollectionView上面
 */
class CAFlowCollection(var registry: ViewRegistry = ViewRegistry()) {
	
	val strategy: Strategy = Strategy()
	val mapper: Mapper = Mapper()
	val layouter: Layouter = Layouter()
	val listener: Listener = Listener()
	val scrollBroadCaster: ScrollBroadCaster = ScrollBroadCaster()
	
	var containerView: View? = null
	
	init {
		strategy.bindAdapter(this)
		mapper.bindAdapter(this)
		layouter.bindAdapter(this)
		listener.bindAdapter(this)
	}
	
	// 最复杂的 视图映射解析逻辑， 放到flow中来处理
	
	// MARK:  子组件，【组合模式】
	private var _subAdapters: MutableList<CAFlowCollection> = mutableListOf()
	var subAdapters: MutableList<CAFlowCollection>
		get() = this._subAdapters
		set(value) {}
	
	fun strategy(type: StrategyType) : CAFlowCollection {
		this.strategy.bindType(type)
		return this
	}
	// MARK:  动态产生 一个 儿子， 添加子节点 也需要这样，所以不按照套路 根本开不进去
	public fun child(child: CAFlowCollection): CAFlowCollection {
		child.containerView = this.containerView
		child.registry = this.registry
		this.addSubAdapter(child)
		return child
	}
	
	// MARK:  动态产生 一个 儿子， 添加子节点 也需要这样，所以不按照套路 根本开不进去
	public fun child(type: StrategyType): CAFlowCollection {
		val child = CAFlowCollection(this.registry)
		child.containerView = this.containerView
		child.strategy.bindType(type)
		this.addSubAdapter(child)
		return child
	}
	
	// MARK: 如果不符合规则，则添加不进去， 并且策略制定后不能修改 + 添加的时候就能知道 TAG
	private fun addSubAdapter(adapter: CAFlowCollection) {
		when ( this.strategy.type) {
			StrategyType.repeatCompound -> {
				when(adapter.strategy.type) {
					StrategyType.repeatCompound, StrategyType.compound ->
						if (CAConfig.DEV_MODE) {
							assert(false, { print("flo: invalid add child")})
						}
					else -> {
						this._subAdapters.add(adapter)
					}
				}
			}
			StrategyType.compound -> {
				when(adapter.strategy.type) {
					StrategyType.repeatCompound, StrategyType.compound,
					StrategyType.repeatList ->
						if (CAConfig.DEV_MODE) {
							assert(false, { print("flo: invalid add child")})
						}
					else -> {
						this._subAdapters.add(adapter)
					}
				}
			}
			
			StrategyType.tab -> {
				when(adapter.strategy.type) {
					StrategyType.repeatCompound, StrategyType.compound,
					StrategyType.repeatList, StrategyType.tab ->
						if (CAConfig.DEV_MODE) {
							assert(false, { print("flo: invalid add child")})
						}
					else -> {
						// 自动带上 TAG
						adapter._tag = this._subAdapters.count()
						this._subAdapters.add(adapter)
					}
				}
			}
			StrategyType.sequence -> {
				when(adapter.strategy.type) {
					StrategyType.repeatCompound, StrategyType.compound,
					StrategyType.repeatList, StrategyType.tab, StrategyType.sequence ->
						if (CAConfig.DEV_MODE) {
							assert(false, { print("flo: invalid add child")})
						}
					else -> {
						// 自动带上 TAG
						adapter._tag = this._subAdapters.count()
						this._subAdapters.add(adapter)
					}
				}
			}
			else ->
				if (CAConfig.DEV_MODE) {
					assert(false, { print("flo: invalid add child")})
				}
		}
	}
	
	// MARK: 实现 SECTION 定位，这些都是动态 【定位】，所以实现为计算属性
	private var _sectionCount: Int = 0
	public var sectionCount: Int
		get() = this._sectionCount
		set(value) {}
	private var _sectionStart: Int = 0
	public var sectionStart: Int
		get() = this._sectionStart
		set(value) {}
	
	// 在同一个段落中，用来区分段落的 编号 == 用来执行 viewKlass 到 viewType的映射
	private var _tag: Int = 0
	public var tag: Int
		get() = this._tag
		set(value) {}
	
	// 完美的线性编程思维方式
	private fun containsSection(section: Int) : Boolean {
		if (section < this.sectionStart)  { return false }
		if (section >= this.sectionStart + this.sectionCount ) { return false }
		return true
	}
	
	// MARK: calculateSectionCount .这个才是真正给外部调用的 函数， 其他都是只读的 属性
	fun calculateSectionCount() : Int {
		when(strategy.type) {
			StrategyType.repeatCompound -> { // 这个通常是最外层的 【策略】···
				var count = 0
				for (adapter in _subAdapters) {
					adapter._sectionStart = this._sectionStart + count
					count += adapter.calculateSectionCount()
				}
				this._sectionCount = count
				return count
			}
			StrategyType.compound -> {
				for (index in _subAdapters.indices) {
					_subAdapters[index]._sectionStart = this._sectionStart + index
					_subAdapters[index].calculateSectionCount()
				}
				this._sectionCount = _subAdapters.count()
				return this._sectionCount
			}
			
			StrategyType.tab, StrategyType.sequence -> {
				for (adapter in _subAdapters) {
					adapter._sectionStart = this._sectionStart
					// 传递计算逻辑还是要的
					adapter.calculateSectionCount()
				}
				this._sectionCount = 1
				return this._sectionCount
			}
			
			StrategyType.repeatList -> {
				// 转换成列表， 否则会挂！！！
				val items = this.vip.content?.checkList<List<*>>()
				
				if (items != null) {
					this._sectionCount = items.count()
					return this._sectionCount
				}else {
					this._sectionCount = 0
					return this._sectionCount
				}
			}
			else -> {
				this._sectionCount = 1
				return this._sectionCount
			}
		}
	}
	
	// MARK: 这个一定是一个查找的过程， 二叉树类似的东西，其实这个要分场景 == 头，尾，内容
	fun sectionAdapter(section: Int, position: PositionType): CAFlowCollection? {
		when ( strategy.type) {
			StrategyType.repeatCompound -> {
				if(!this.containsSection(section))   { return null}
				// 循环查找，直到匹配到合适的 【儿子】则结束
				return _subAdapters
						.firstOrNull { it.containsSection(section) }
						?.sectionAdapter(section, position)
			}
			
			StrategyType.compound -> {
				if(!this.containsSection(section))   { return null}
				// 循环查找，直到匹配到合适的 【儿子】则结束
				return _subAdapters[section].sectionAdapter(section, position)
			}
			
			StrategyType.tab -> {
				if (position == PositionType.header && strategy.enableTabHeader) {return this}
				if (position == PositionType.footer && strategy.enableTabFooter) {return this}
				if (strategy.tabIndex < 0 || strategy.tabIndex >= _subAdapters.count()) else { return null}
				// 已经嵌套到节点了， 再调用也就是返回自身，没必要了
				return _subAdapters[strategy.tabIndex]
			}
			StrategyType.sequence -> return this
			else -> return this
		}
	}
	
	
	// MARK: 实现 ROW 定位，必须的 2个属性
	private var _rowStart: Int = 0
	public var rowStart: Int
		get() = this._rowStart
		set(value) {}
	
	private var _rowCount: Int = 0
	public var rowCount: Int
		get() = this._rowCount
		set(value) {}
	
	//  完美的线性编程思维方式
	private fun containsRow(row: Int): Boolean {
		if (row < this.rowStart ) { return false }
		if (row >= this.rowStart + this.rowCount ) { return false }
		return true
	}
	
	// 找到 【行集位置 position == .row】 SECTION 之后就需要，计算 该行集中的的行数， 通常很简单，但是有的比较复杂 ,比如 sequence 和 repeatedList，就比较麻烦， 需要段落级别的 数据映射和解析
	// section, 参数是专门传递给 repeated 场景用的，他会横跨多个section
	fun calculateRowCount(section: Int) : Int {
		when (this.strategy.type ) {
			StrategyType.sequence -> {
				var count = 0
				for (adapter in _subAdapters) {
					adapter._rowStart = this._rowStart + count
					count += adapter.calculateRowCount(section)
				}
				this._rowCount = count
				return this._rowCount
			}
			
			StrategyType.repeatList, StrategyType.list, StrategyType.optinal,
			StrategyType.placeholder, StrategyType.singleton -> {
				val data = sectionData(section, PositionType.row)
				this._rowCount = this.mapper.rowCount(data)
				return this._rowCount
			}
			else -> return 0 // 不应该到这里
		}
	}
	
	// MARK: 这是一个 【外观】专门用来获取内部数据，用错了位置返回 nil
	public fun rowData(section: Int, row: Int) : Any? {
		when (this.strategy.type) {
			StrategyType.list, StrategyType.repeatList, StrategyType.optinal,
			StrategyType.singleton, StrategyType.placeholder -> {
				val sectionData = this.sectionData(section, PositionType.row)
				return this.mapper.rowData(sectionData, row)
			}
			else -> return null
		}
	}
	
	// MARK:  解析出 SECTION 对应的数据: 只有 【具体的】类有数据，到了 sectionAdapter级别
	fun sectionData(section: Int, position: PositionType) : Any? {
		when (this.strategy.type) {
			StrategyType.repeatList -> {
				if (position == PositionType.header) {
					val items = this.vip.header as? List<*> ?: return null
					if (section < sectionStart || section >= sectionStart + items.count() ) {
						return null
					}
					return items[section - sectionStart]
				}
				
				if (position == PositionType.footer) {
					val items = this.vip.footer as? List<*> ?: return null
					if (section < sectionStart || section >= sectionStart + items.count() ) {
						return null
					}
					return items[section - sectionStart]
				}
				// 复杂的 内容格式解析
				val  items = this.vip.content?.checkList<List<*>>() ?: return null
				if (section < sectionStart || section >= sectionStart + items.count() ) {
					return null
				}
				return items[section - sectionStart]
			}
			
			StrategyType.list, StrategyType.optinal,
			StrategyType.placeholder, StrategyType.singleton -> {
				if (position == PositionType.header) {return this.vip.header }
				if (position == PositionType.footer) {return this.vip.footer }
				return this.vip.content
			}
			
			StrategyType.tab -> {
				if (position == PositionType.header && strategy.enableTabHeader) {
					return this.vip.header
				}
				if (position == PositionType.footer && strategy.enableTabFooter) {
					return this.vip.footer
				}
				return null
			}
			
			StrategyType.sequence -> {
				if (position == PositionType.header) {
					return this.vip.header
				}
				if (position == PositionType.footer) {
					return this.vip.footer
				}
				return null
			}
			else -> return null // 不应该到这里
		}
	}
	
	// MARK: 定位到具体的 ROW 上面 == 这个时候肯定是 ，在已经定位到的 SECTION上面进行行选取
	fun rowAdapter(row: Int, position: PositionType) : CAFlowCollection? {
		when ( strategy.type) {
			StrategyType.repeatCompound, StrategyType.compound -> {
				// 永远不能到这里， 一定是在 【SECTION】独立的部分使用这个函数
				return null
			}
			StrategyType.tab -> {
				if (position == PositionType.header && strategy.enableTabHeader) {return this}
				if (position == PositionType.footer && strategy.enableTabFooter) {return this}
				return null
			}
			
			StrategyType.sequence -> { // MARK: 行拼接模式
				if (position == PositionType.header || position == PositionType.footer) {
					return this
				}
				return _subAdapters
						.firstOrNull { it.containsRow(row) }
						?.rowAdapter(row, position)
			}
			else -> return this
		}
	}
}
package com.mh.ca.recyclerview.adapter

import android.content.*
import android.support.v7.widget.*
import android.util.*
import android.view.*
import android.widget.*
import com.mh.anko.*
import com.mh.anko.uikit.*
import com.mh.ca.*
import com.mh.ca.flow.*
import com.mh.ca.recyclerview.layouts.*
import com.mh.ca.recyclerview.sectioned.*
import com.mh.ca.recyclerview.view.*
import com.mh.ca.utils.*
import com.tonicartos.superslim.*
import kotlin.reflect.*

/**
 * Created by tinytitan on 16/5/7.
 * 尝试兼容XRecyclerView！！！： 传递Recyclerview对象进行判断。
 */
internal  class SectionedRecyclerViewDataSourceAdapter(
        val flowCollection: CAFlowCollection,
        val recyclerView: RecyclerView): SectionedRecyclerViewAdapter() {
    companion object {
        val TAG = "DataSourceAdapter"
    }

    data class IndexPath(val section: Int, val row:Int)
    // 盗版iOS中的设计思路: 这个是内部实现
    class ViewHolder(itemView: View, val adapter:SectionedRecyclerViewDataSourceAdapter? = null) :
            RecyclerView.ViewHolder(itemView) {
        var position: IndexPath? = null // 这个等待绑定的时候注入进来
        init {
            // 设置adapter 对应的是Item， 头部和尾部不设置： 一般都不会处理这两个事件！！
            if(adapter != null) {
                this.itemView.setOnClickListener {
                    handleSelection()
                }
            }
        }

        fun handleSelection(){
            if(position == null){
                // 代码不应该执行这个逻辑： 到这里就错了
                return
            }
            // MARK： 如果DSL对象不存在，返回
            val flowCollection = adapter?.flowCollection ?: return
            val sectionFlow = flowCollection.sectionAdapter(position!!.section, PositionType.row) ?: return
	        val rowFlow = sectionFlow.rowAdapter(position!!.row, PositionType.row) ?: return
	        
            if(sectionFlow.listener.allowsMultipleSelection){
	            adapter.handleMultipleSelection(rowFlow, itemView,this.adapter!!, position!!.section, position!!.row)
                return
            }
            // 单选
            if(sectionFlow.listener.allowsSelection){
	            adapter.handleSingleSelection(rowFlow, itemView,this.adapter!!, position!!.section, position!!.row)
                return
            }
            //  没有选取概念， 只有点击的概念 == 是否需要继续实现行选取的逻辑？？
	        rowFlow.listener.selectListener?.invoke(position!!.section, position!!.row)
        }
    }

    // 手动选取特定的Item： 一般用于设置默认值 == 一般也需要同步监听状态 == 这样更加的统一和灵活
    fun selectItem(section: Int, row: Int) {
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row) ?: return
	    val rowFlow = sectionFlow.rowAdapter(row, PositionType.row) ?: return
	
	    // 查询到 视图
	    val itemPosition = getStartItemPositionForSection(section)
	    var selectionPosition = itemPosition + row
	    
	    //  查询到具体的子视图 == 可能还没出现，但是不影响功能！！
	    val selectedView = recyclerView.getChildAt(selectionPosition)
	
	    if(sectionFlow.listener.allowsMultipleSelection){
		    handleMultipleSelection(rowFlow, selectedView,this, section, row)
		    return
	    }
	    // 单选
	    if(sectionFlow.listener.allowsSelection){
		    handleSingleSelection(rowFlow, selectedView,this, section, row)
		    return
	    }
	    rowFlow.listener.selectListener?.invoke(section, row)
    }
	
	// 移植过来更准确的选取思路
	fun handleMultipleSelection(
			rowFlow: CAFlowCollection,
			itemView: View?,
			adapter: SectionedRecyclerViewDataSourceAdapter,
	        section: Int, row: Int){
		
		// 选中后再次选取： 那么就是取消选取了
		if(adapter.isSelected(section, row)){
			if(itemView is SelectionAware) {
				itemView.onSelection(false)
			}
			rowFlow.listener.deselectListener?.invoke(section, row)
			adapter.setUnselected(section, row)
		}else {
			if(itemView is SelectionAware) {
				itemView.onSelection(true)
			}
			rowFlow.listener.selectListener?.invoke(section, row)
			adapter.setSelected(section, row)
		}
	}
	
	// 单选场景
	fun handleSingleSelection(
			rowFlow: CAFlowCollection,
			itemView: View?,
			adapter: SectionedRecyclerViewDataSourceAdapter,
			section: Int, row: Int){
		// 已经选取了同一个Item
		val selectedPosition = adapter!!.selectedItemPositionForSection[section]
		val itemPosition = adapter!!.getStartItemPositionForSection(section)
		// 重复点击的效果
		if(row == selectedPosition) { return }
		
		// 记录已经选取的卡片位置
		adapter!!.selectedItemPositionForSection[section] = row
		
		//  实现选取感知接口
		if(itemView is SelectionAware) {
			itemView.onSelection(true)
		}
		
		rowFlow.listener.selectListener?.invoke(section, row)
		// 之前已经选取过
		if( selectedPosition != -1) {
			var deselectionPosition = itemPosition + selectedPosition
			
			// 采用直接操作的方式进行点选
			val deselectedView = adapter!!.recyclerView.getChildAt(deselectionPosition)
			//  实现选取感知接口
			if(deselectedView is SelectionAware) {
				deselectedView.onSelection(false)
			}
			rowFlow.listener.deselectListener?.invoke(section, row)
		}
	}



    override val sectionCount: Int
        get() = flowCollection.calculateSectionCount()

    override fun getItemCountForSection(section: Int): Int {
	    val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
        return sectionFlow?.calculateRowCount(section) ?: 0
    }

    override  fun getSectionItemViewType(section: Int, position: Int): Int {
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
                ?: return Mapper.INVALID_TYPE
        val rowFlow = sectionFlow.rowAdapter(position, PositionType.row)
                ?: return Mapper.INVALID_TYPE
        val rowdata = rowFlow.rowData(section, position)
        val rowView = rowFlow.mapper.rowView(rowdata) ?: return Mapper.INVALID_TYPE
        
        val type =  rowFlow.mapper.getItemType(rowView)
        rowFlow.registry.regiestView(type, rowView)
	    
        return type
    }

    override fun getSectionFooterViewType(section: Int): Int {
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.footer)
                ?: return Mapper.INVALID_TYPE
        val footerData = sectionFlow.sectionData(section, PositionType.footer)
        val footerView = sectionFlow.mapper.footerView(footerData) ?: return Mapper.INVALID_TYPE
    
        val type =  sectionFlow.mapper.getFooterType(footerView)
        sectionFlow.registry.regiestView(type, footerView)
        return type
    }

    override fun getSectionHeaderViewType(section: Int): Int {
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.header)
                ?: return Mapper.INVALID_TYPE
        val headerData = sectionFlow.sectionData(section, PositionType.header)
        val headerView = sectionFlow.mapper.headerView(headerData)
                ?: return Mapper.INVALID_TYPE
        val type =  sectionFlow.mapper.getHeaderType(headerView)
        sectionFlow.registry.regiestView(type, headerView)
        return type
    }

    // 头部 + 尾部 视图的 映射 也需要支持 可解析风格，
    // 这点非常重要 【暂时不用，但对完备性很关键】
    override  fun isSectionHeaderViewType(viewType: Int): Boolean {
        return Mapper.isSectionHeaderViewType(viewType)
    }

    override fun isSectionFooterViewType(viewType: Int): Boolean {
        return Mapper.isSectionFooterViewType(viewType)
    }

    // 根据是否设置了 视图来判断是否有头部和尾部
    override fun hasFooterInSection(section: Int): Boolean {
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.footer)
                ?: return false
        return sectionFlow.mapper.hasFooterView()
    }

    override fun hasHeaderInSection(section: Int): Boolean {
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.header)
                ?: return false
        return sectionFlow.mapper.hasHeaderView()
    }

    // 一定有一个中心注册机制， 实现 view ==> Type 的映射， 然后反向解析出 view
    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewKlass = flowCollection.registry.getView(viewType)
                ?: return ViewHolder(View(parent.context), this) // 使用默认的 空视图
        return createViewHolder(viewKlass, parent.context)
    }

    override fun onCreateSectionFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewKlass = flowCollection.registry.getView(viewType)
                ?: return ViewHolder(View(parent.context), this) // 使用默认的 空视图
        return createViewHolder(viewKlass, parent.context)
    }
    // 通过反射创建VH
    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewKlass = flowCollection.registry.getView(viewType)
                ?: return ViewHolder(View(parent.context), this) // 使用默认的 空视图
        return createViewHolder(viewKlass, parent.context)
    }

    // 创建视图并装配
    private  fun createViewHolder(klass: KClass<out Any>, context: Context): RecyclerView.ViewHolder{
        var contentView = ReflectUtils.createInstanceFor(klass.java)
	
	    if (contentView == null) {
		    contentView =  ReflectUtils.createInstanceFor(klass.java, Context::class.java, context)// 降级到 使用默认的构造函数，，，碉堡
	    }
	    
        if (contentView is View) {
            return ViewHolder(contentView, this)
        }else {
            return ViewHolder(View(context), this) // 使用默认的 空视图
        }
        
    }

    override fun onBindSectionHeaderViewHolder(holder: RecyclerView.ViewHolder, section: Int) {
	
	    val sectionFlow = flowCollection.sectionAdapter(section, PositionType.header)
			    ?: return
	    
	    val headerData = sectionFlow.sectionData(section, PositionType.header)
	    val cell = holder.itemView
	
	    cell.vip.context.content = headerData
	    cell.boss(sectionFlow) // 向 flow 进行汇报
	
	    if (sectionFlow.mapper.enableHeaderExtra) {
		    cell.vip.context.receiveExtra(sectionFlow.vip.context)
	    }
	
	    if (sectionFlow.mapper.enableHeaderScrollAware) {
		    if (cell is CAScrollHandlerType) {
			    flowCollection.scrollBroadCaster.appendSubHandler(cell)
		    }
	    }
	
	    // 临时hack一下布局，设置宽度为固定值
	    if(recyclerView.layoutManager is GridLayoutManager) {
		    // 判断布局方向
		    if((recyclerView.layoutManager as GridLayoutManager).orientation == GridLayoutManager.HORIZONTAL) {
			    // 默认就是自适应
			    var width = RelativeLayout.LayoutParams.WRAP_CONTENT
			    var height = RelativeLayout.LayoutParams.MATCH_PARENT
			
			    // 也可以直接设置 高度 不需要计算
			    if (sectionFlow.layouter.headerHeight > 0) {
				    width = sectionFlow.layouter.headerHeight
			    }
			
			    val heaerHeightParser = sectionFlow.layouter.headerHeightParser
			    if(heaerHeightParser != null) {
				    // 直接占据 所有的布局尺寸
				    val layoutHeight = contentHeightForRecyclerView(recyclerView)
				    width = heaerHeightParser.invoke(layoutHeight.px2ddp, section).ddp
			    }
			
			    val lp = RelativeLayout.LayoutParams(width, height)
			    holder.itemView.layoutParams = lp
		    }else {
			    // 默认就是自适应
			    var width = RelativeLayout.LayoutParams.MATCH_PARENT
			    var height = RelativeLayout.LayoutParams.WRAP_CONTENT
			
			    // 也可以直接设置 高度 不需要计算
			    if (sectionFlow.layouter.headerHeight > 0) {
				    height = sectionFlow.layouter.headerHeight
			    }
			
			    val headerheightParser = sectionFlow.layouter.headerHeightParser
			    if(headerheightParser != null) {
				    // val width = holder.itemView.measuredWidth
				    val layoutWidth = contentWidthForRecyclerView(recyclerView)
				    Log.d(TAG, "Grid计算进来的宽度：" + width)
				    height = headerheightParser.invoke(layoutWidth.px2ddp, section).ddp
			    }
			    
			    val lp = RelativeLayout.LayoutParams(width, height)
			    holder.itemView.layoutParams = lp
		    }
	    }

        // hack 布局: 如果不是SuperSLim 布局那么就不不需要额外处理！！
        if(recyclerView.layoutManager !is LayoutManager ) {
            return
        }

        //写死Sticky 头部测试下！！！
        val  lp = GridSLM.LayoutParams.from(holder.itemView.layoutParams)

        // 头部丰富多彩的展现形式！！！
        lp.isHeader = true

        // 控制Header的展示
        if(sectionFlow.mapper.allowsStickeyHeader){
            lp.headerDisplay =  GridSLM.LayoutParams.HEADER_STICKY or GridSLM.LayoutParams.HEADER_INLINE
            // 是否Overlay
            if(sectionFlow.mapper.isOverlayHeader){
                lp.headerDisplay = lp.headerDisplay or GridSLM.LayoutParams.HEADER_OVERLAY
            }else {
                lp.headerDisplay = lp.headerDisplay or GridSLM.LayoutParams.HEADER_INLINE
            }
        }else {
            // 是否Overlay
            if(sectionFlow.mapper.isOverlayHeader){
                lp.headerDisplay = GridSLM.LayoutParams.HEADER_OVERLAY
            }else {
                lp.headerDisplay = GridSLM.LayoutParams.HEADER_INLINE
            }
        }

        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp.setSlm(GridSLM.ID)

        Log.d(TAG, "段内的第一个元素" + getFirstPositionForSection(section))

        var firstPosition = getFirstPositionForSection(section)
        
        lp.firstPosition = firstPosition
        lp.numColumns = sectionFlow.layouter.columnCount
        // 打补丁支持自定义高度解析： 这个很关键因为很多复杂视图就可以通过公式设置高度了！！！
        // 默认就是自适应
        var height = GridSLM.LayoutParams.WRAP_CONTENT
        val headerHeight = sectionFlow.layouter.headerHeight
        if(headerHeight > 0) {
            height = headerHeight
        }
        lp.height = height
        holder.itemView.layoutParams = lp
    }

    override fun onBindSectionFooterViewHolder(holder: RecyclerView.ViewHolder, section: Int) {
	
	
	    val sectionFlow = flowCollection.sectionAdapter(section, PositionType.footer)
			    ?: return
	    val footerData = sectionFlow.sectionData(section, PositionType.footer)
	    
	    val cell = holder.itemView
	
	    cell.vip.context.content = footerData
	    cell.boss(sectionFlow) // 向 flow 进行汇报
	
	    if (sectionFlow.mapper.enableFooterExtra) {
		    cell.vip.context.receiveExtra(sectionFlow.vip.context)
	    }
	
	    if (sectionFlow.mapper.enableFooterScrollAware) {
		    if (cell is CAScrollHandlerType) {
			    flowCollection.scrollBroadCaster.appendSubHandler(cell)
		    }
	    }
	
	    // 临时hack一下布局，设置宽度为固定值
	    if(recyclerView.layoutManager is GridLayoutManager) {
		    // 判断布局方向
		    if((recyclerView.layoutManager as GridLayoutManager).orientation == GridLayoutManager.HORIZONTAL) {
			    // 默认就是自适应
			    var width = RelativeLayout.LayoutParams.WRAP_CONTENT
			    var height = RelativeLayout.LayoutParams.MATCH_PARENT
			
			    // 也可以直接设置 高度 不需要计算
			    if (sectionFlow.layouter.footerHeight > 0) {
				    width = sectionFlow.layouter.footerHeight
			    }
			
			    val footerHeightParser = sectionFlow.layouter.footerHeightParser
			    if(footerHeightParser != null) {
				    // 直接占据 所有的布局尺寸
				    val layoutHeight = contentHeightForRecyclerView(recyclerView)
				    width = footerHeightParser.invoke(layoutHeight.px2ddp, section).ddp
			    }
			
			    val lp = RelativeLayout.LayoutParams(width, height)
			    holder.itemView.layoutParams = lp
		    }else {
			    // 默认就是自适应
			    var width = RelativeLayout.LayoutParams.MATCH_PARENT
			    var height = RelativeLayout.LayoutParams.WRAP_CONTENT
			
			    // 也可以直接设置 高度 不需要计算
			    if (sectionFlow.layouter.footerHeight > 0) {
				    height = sectionFlow.layouter.footerHeight
			    }
			
			    val footerHeightParser = sectionFlow.layouter.footerHeightParser
			    if(footerHeightParser != null) {
				    // val width = holder.itemView.measuredWidth
				    val layoutWidth = contentWidthForRecyclerView(recyclerView)
				    Log.d(TAG, "Grid计算进来的宽度：" + width)
				    height = footerHeightParser.invoke(layoutWidth.px2ddp, section).ddp
			    }
			
			    val lp = RelativeLayout.LayoutParams(width, height)
			    holder.itemView.layoutParams = lp
		    }
	    }
	    
	    

        // hack 布局: 如果不是SuperSLim 布局那么就不不需要额外处理！！
        if(recyclerView.layoutManager !is LayoutManager ) {
            return
        }
	
	    // 尾部footer 只有 一种模式 是否可以采用 线性布局？？？
	    val  lp = GridSLM.LayoutParams.from(holder.itemView.layoutParams)
	
	    // 貌似目前 footer 的处理 有 BUG，， 垃圾的很啊！！！
	    lp.width = recyclerView.measuredWidth
	    lp.setSlm(GridSLM.ID) // 强制改成线性布局即可， 非常牛逼的体验
	
	    Log.d(TAG, "段内的第一个元素" + getFirstPositionForSection(section))
	
	    var firstPosition = getFirstPositionForSection(section)
	    
	    lp.firstPosition = firstPosition
	    // 打补丁支持自定义高度解析： 这个很关键因为很多复杂视图就可以通过公式设置高度了！！！
	    // 默认就是自适应
	    var height = GridSLM.LayoutParams.WRAP_CONTENT
	    val footerHeight = sectionFlow.layouter.footerHeight
	    if(footerHeight > 0) {
		    height = footerHeight
	    }
	    lp.height = height
	    holder.itemView.layoutParams = lp
    }
	
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
		
		// 暂时不支持 头部和尾部的局部刷新 == 没必要
		if (isSectionHeaderPosition(position) || isSectionFooterPosition(position) ) {
			onBindViewHolder(holder, position)
			return
		}
		
		// 为空 直接全部刷新
		if (payloads?.isEmpty() ?: true) {
			onBindViewHolder(holder, position)
			return
		}
		
		// 尝试解析道 CAPayload 对象， 这个是关键
		val payload = payloads?.get(0) as? CAPayload
		
		if (payload == null) {
			onBindViewHolder(holder, position)
			return
		}
		
		// 执行 payload 指令
		Log.d("PayLoad", "$payload") // 只接受 CAPayload 指令， 否则会出现不可用的情况
		val section = sectionForPosition[position]
		val index = positionWithinSection[position]
		onBindItemViewHolderWithPayload(holder, section, index, payload)
	}
	
	// MARK:  局部刷新框架 == 目前只支持， HACK 掉 content 的部分， 使用 payload 来代替
	fun onBindItemViewHolderWithPayload(
			holder: RecyclerView.ViewHolder,
			section: Int, position: Int, payload: CAPayload) {
		
		// 处理选取操作 == 内部创建和使用的是同一个 viewHolder
		val viewHolder = holder as ViewHolder //  强制转换
		
		// 设置当前位置
		viewHolder.position = IndexPath(section, position)
		
		val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
				?: return
		val rowFlow = sectionFlow.rowAdapter(position, PositionType.row) ?: return
		val rowData = rowFlow.rowData(section, position)
		
		val cell = holder.itemView
		// MARK:  动态组装 CELL
		// cell.vip.containerView.content = rowData // 换成 payload 指令
		cell.vip.payload(payload.key, payload.value)
		
		// 目前这个搞法， 也就是起到更新布局的效果了
		initItemLayout(holder.itemView, section, position, rowFlow)
	}
		
		// 装配模型
    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, section: Int, position: Int) {

        // 处理选取操作 == 内部创建和使用的是同一个 viewHolder
        val viewHolder = holder as ViewHolder //  强制转换

        // 设置当前位置
        viewHolder.position = IndexPath(section, position)
        
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
                ?: return
	    
        // 更新视图状态！！！
        if(holder.itemView is SelectionAware){
            val selectionAware = holder.itemView as SelectionAware

            if(sectionFlow.listener.allowsMultipleSelection){
                val selected = this.isSelected(section, position)
                selectionAware.onSelection(selected)
            }
            if(sectionFlow.listener.allowsSelection){
                val selected = selectedItemPositionForSection[section] == position
                selectionAware.onSelection(selected)
            }
        }
	
	    val rowFlow = sectionFlow.rowAdapter(position, PositionType.row) ?: return
	    val rowData = rowFlow.rowData(section, position)
	    
	    val cell = holder.itemView
	    // MARK:  动态组装 CELL
	    cell.vip.context.content = rowData
	    cell.vip.context.position = position
	    cell.boss(rowFlow)
	
	    if (rowFlow.mapper.enableRowHeader) {
		    cell.vip.context.header = rowFlow.vip.context.header
	    }
	    if (rowFlow.mapper.enableRowFooter) {
		    cell.vip.context.footer = rowFlow.vip.context.footer
	    }
	
	    if (rowFlow.mapper.enableRowExtra) {
		    cell.vip.context.receiveExtra(rowFlow.vip.context)
	    }
	
	    if (rowFlow.mapper.enableRowDecor) {
		    // MARK： 这个貌似有点商榷，需要验证下
		    val row = position - sectionFlow.rowStart
		    val isFirst: Boolean = row == 0
		    val isLast: Boolean = row == (sectionFlow.rowCount - 1)
		    val context = DecorContext(row, isFirst, isLast)
		    cell.vip.context.extra(DecorContext.KEY, context)
	    }
	
	    if (rowFlow.mapper.enableRowScrollAware) {
		    if (cell is CAScrollHandlerType) {
			    flowCollection.scrollBroadCaster.appendSubHandler(cell)
		    }
	    }
			
		initItemLayout(holder.itemView, section, position, rowFlow)
    }
	
	// 初始化 布局参数
	fun initItemLayout(
			itemView: View,
			section: Int, position: Int,
			rowFlow: CAFlowCollection) {
		// 临时hack一下布局，设置宽度为固定值
		if(recyclerView.layoutManager is GridLayoutManager) {
			// 判断布局方向
			if((recyclerView.layoutManager as GridLayoutManager).orientation == GridLayoutManager.HORIZONTAL) {
				// 默认就是自适应
				var width = RelativeLayout.LayoutParams.WRAP_CONTENT
				var height = RelativeLayout.LayoutParams.MATCH_PARENT
				// 也可以直接设置 高度 不需要计算
				if (rowFlow.layouter.rowHeight > 0) {
					width = rowFlow.layouter.rowHeight
				}
				
				val cellHeightParser = rowFlow.layouter.rowHeightParser
				if(cellHeightParser != null) {
					// val height = holder.itemView.measuredHeight
					val layoutHeight = layoutHeightForRecyclerView(recyclerView, section, position)
					val columnCount = rowFlow.layouter.columnCount
					val height = layoutHeight / columnCount
					Log.d(TAG, "Grid计算进来的高度：" + height)
					width = cellHeightParser.invoke(height.px2ddp, section, position).ddp
				}
				
				// MARK： 手动设置了 尺寸 == 这个优先级最高
				val cellSizeParser = rowFlow.layouter.rowSizeParser
				if(cellSizeParser != null) {
					val layoutHeight = layoutHeightForRecyclerView(recyclerView, section, position)
					val size = cellSizeParser.invoke(layoutHeight.px2ddp, section, position)
					width = size.width.ddp
					height = size.height.ddp
				}
				
				val lp = RelativeLayout.LayoutParams(width, height)
				itemView.layoutParams = lp
			}else {
				// 默认就是自适应
				var width = RelativeLayout.LayoutParams.MATCH_PARENT
				var height = RelativeLayout.LayoutParams.WRAP_CONTENT
				
				// 也可以直接设置 高度 不需要计算
				if (rowFlow.layouter.rowHeight > 0) {
					height = rowFlow.layouter.rowHeight
				}
				
				val cellHeightParser = rowFlow.layouter.rowHeightParser
				if(cellHeightParser != null) {
					// val width = holder.itemView.measuredWidth
					val layoutWidth = layoutWidthForRecyclerView(recyclerView, section, position)
					val columnCount = rowFlow.layouter.columnCount
					val width = layoutWidth / columnCount
					Log.d(TAG, "Grid计算进来的宽度：" + width)
					height = cellHeightParser.invoke(width.px2ddp, section, position).ddp
				}
				
				// MARK： 手动设置了 尺寸 == 这个优先级最高
				val cellSizeParser = rowFlow.layouter.rowSizeParser
				if(cellSizeParser != null) {
					val layoutWidth = layoutWidthForRecyclerView(recyclerView, section, position)
					val size = cellSizeParser.invoke(layoutWidth.px2ddp, section, position)
					width = size.width.ddp
					height = size.height.ddp
				}
				
				val lp = RelativeLayout.LayoutParams(width, height)
				itemView.layoutParams = lp
			}
		}
		
		// 临时hack一下布局，设置宽度为固定值
		if(recyclerView.layoutManager is FlowLayoutManager) {
			
			val lp =  FlowLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			// MARK： 手动设置了 尺寸
			val cellSizeParser = rowFlow.layouter.rowSizeParser
			if(cellSizeParser != null) {
				val layoutWidth = layoutWidthForRecyclerView(recyclerView, section, position)
				val size = cellSizeParser.invoke(layoutWidth.px2ddp, section, position)
				lp.width = size.width.ddp
				lp.height = size.height.ddp
			}
			// MARK:  直接设置 布局间距，而且只是边距设置
			lp.verticalMargin = rowFlow.layouter.minimumLineSpacing / 2
			lp.horizontalMargin = rowFlow.layouter.minimumInteritemSpacing / 2
			
			// 左上角对齐的方式
			lp.gravity = Gravity.LEFT or Gravity.FILL_VERTICAL or Gravity.TOP
			lp.weight = 0.0f
			itemView.layoutParams = lp
			Log.d(TAG, "FlowLayoutManager， section: $section, row: $position")
		}
		
		// hack 布局: 如果不是SuperSLim 布局那么就不不需要额外处理！！
		if(recyclerView.layoutManager !is LayoutManager ) {
			return
		}
		
		val  lp = GridSLM.LayoutParams.from(itemView.layoutParams)
		
		lp.setSlm(GridSLM.ID)
		
		Log.d(TAG, "列数" + rowFlow.layouter.columnCount)
		lp.numColumns = rowFlow.layouter.columnCount
		//lp.columnWidth = 250
		var firstPosition = getFirstPositionForSection(section)
		
		lp.firstPosition = firstPosition
		
		// 打补丁支持自定义高度解析： 这个很关键因为很多复杂视图就可以通过公式设置高度了！！！
		// 默认就是自适应 == 貌似 只能通过 父容器计算出高度 不能用还没布局子视图的宽度和高度传递，传进去是0
		var height = GridSLM.LayoutParams.WRAP_CONTENT
		// 也可以直接设置 高度 不需要计算
		if (rowFlow.layouter.rowHeight > 0) {
			height = rowFlow.layouter.rowHeight
		}
		val cellHeightParser = rowFlow.layouter.rowHeightParser
		if(cellHeightParser != null) {
			// val width = holder.itemView.measuredWidth
			val layoutWidth = layoutWidthForRecyclerView(recyclerView, section, position)
			val columnCount = rowFlow.layouter.columnCount
			val width = layoutWidth / columnCount
			Log.d(TAG, "Super计算进来的宽度：" + width)
			height = cellHeightParser.invoke(width.px2ddp, section, position).ddp
		}
		Log.d(TAG, "设置高度： " + height)
		lp.height = height
		itemView.layoutParams = lp
	}
	
	// 计算内容区域的宽度： 减去视图的边距
	fun contentWidthForRecyclerView(recyclerView: RecyclerView): Int{
		return recyclerView.measuredWidth - recyclerView.paddingLeft - recyclerView.paddingRight
	}
	// 计算内容区域的高度： 减去视图的边距
	fun contentHeightForRecyclerView(recyclerView: RecyclerView): Int{
		return recyclerView.measuredHeight - recyclerView.paddingTop - recyclerView.paddingBottom
	}
	// 计算排版区域的宽度： 减去Section的边距以及列之间的间距
	fun layoutWidthForRecyclerView(recyclerView: RecyclerView, section: Int, row: Int): Int{
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row) ?: return 0
		val sectionInset = sectionFlow.layouter.sectionInset
		return contentWidthForRecyclerView(recyclerView) - sectionInset.left - sectionInset.right -
				(sectionFlow.layouter.columnCount - 1) * sectionFlow.layouter.minimumInteritemSpacing
	}
	// 计算排版区域的高度： 减去Section的边距以及列之间的间距
	fun layoutHeightForRecyclerView(recyclerView: RecyclerView, section: Int, row: Int): Int{
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row) ?: return 0
        val sectionInset = sectionFlow.layouter.sectionInset
		return contentHeightForRecyclerView(recyclerView) - sectionInset.top - sectionInset.bottom -
				(sectionFlow.layouter.columnCount - 1) * sectionFlow.layouter.minimumInteritemSpacing
	}
}
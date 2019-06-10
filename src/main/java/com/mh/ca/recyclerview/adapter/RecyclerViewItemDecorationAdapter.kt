package com.mh.ca.recyclerview.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.mh.anko.*
import com.mh.anko.uikit.UIEdgeInsetsMake
import com.mh.ca.flow.CAFlowCollection
import com.mh.ca.flow.PositionType
import com.mh.ca.recyclerview.sectioned.SectionedRecyclerViewAdapter

/**
 *  模拟iOS中的间隔设置方式
 */
internal class RecyclerViewItemDecorationAdapter(
        val adapter: SectionedRecyclerViewAdapter,
        val flowCollection: CAFlowCollection,
        val isHorizontal:Boolean = false)
: RecyclerView.ItemDecoration() {

	companion object {
		val TAG = "ItemDecorationAdapter"
	}

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        var position = parent.getChildAdapterPosition(view) // item position

        // 后面补充的Item不装饰
        if(position >= adapter.itemCount) {
            return
        }
		// MARK： 获取容器内容宽度， 为后续做准备
	    val contentWidth = parent.measuredWidth - parent.paddingLeft - parent.paddingRight
		Log.d(TAG, "内容宽度：$contentWidth")


        if(isHorizontal) {
            getItemOffsetsHorizental(outRect, position)
        }else {
            getItemOffsets(outRect, position, contentWidth)
        }

    }


    // 水平布局
    fun getItemOffsetsHorizental(outRect: Rect, position: Int) {

        val section = adapter.getSectionForPosition(position)
        
        guard(section >= 0) {return} // 确保获取到了正确的 段落编号
        
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
        val sectionInset = sectionFlow?.layouter?.sectionInset ?: UIEdgeInsetsMake(0,0,0,0)
        //  头部： 专职处理 段 上下边距
        if (adapter.isSectionHeaderPosition(position) ) {
            //outRect.bottom = sectionInset.top
            return
        }
        // 尾部： 专职处理 段 上下边距
        if (adapter.isSectionFooterPosition(position)) {
            //outRect.top = sectionInset.bottom
            return
        }

        // 设置列表间距： 列表一定是columnCount统一的，这点不需保证！！！

        val index = adapter.getPositionWithinSection(position)
        val itemCount = adapter.getItemCountForSection(section) // 获取数量
        val minimumInteritemSpacing = sectionFlow?.layouter?.minimumInteritemSpacing ?: 0
        val minimumLineSpacing = sectionFlow?.layouter?.minimumLineSpacing ?: 0
        //  每个 Item 都可以设置不同的列数： 这样就能算出当前大致有多少个卡片！！，列表一般都是一致的，只有详情可能不一致
        val columnCount = sectionFlow?.layouter?.columnCount ?: 1

        val  column = index % columnCount; // item column
        val  row = index / columnCount // item row
        // 总行数: 横向就是总列数
        val  rowCount = itemCount / columnCount +  if(itemCount % columnCount == 0) 0 else 1


        // 内部最中间的条目的默认上下边距
        outRect.left = minimumLineSpacing / 2
        outRect.right = minimumLineSpacing / 2
        // 最顶部的Item
        if(index < columnCount) {
            // 顶部边距: 横向的所以左侧就相当于段落的 top
            outRect.left =  sectionInset.left
        }
        // 最后一行
        if(row == rowCount - 1) {
            // 底部边距
            outRect.right = sectionInset.right
        }
        // 只有一列 好办
        if(columnCount == 1){
            outRect.top = sectionInset.top
            outRect.bottom = sectionInset.bottom
            return
        }
        val total = sectionInset.top + sectionInset.bottom + (columnCount - 1) * minimumInteritemSpacing
        val avg = total / columnCount
        // 终极规律出现了： 归纳法啊
        outRect.top = (minimumInteritemSpacing - avg) * column + sectionInset.top
        outRect.bottom = avg - (minimumInteritemSpacing - avg) * column - sectionInset.top
    }

    // 这个才是真正的实现业务逻辑的方法！！！
    fun getItemOffsets(outRect: Rect, position: Int, contentWidth: Int) {

        val section = adapter.getSectionForPosition(position)
    
        guard(section >= 0) {return} // 确保获取到了正确的 段落编号
    
    
        val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
        val sectionInset = sectionFlow?.layouter?.sectionInset ?: UIEdgeInsetsMake(0,0,0,0)

        //  头部： 专职处理 段 上下边距
        if (adapter.isSectionHeaderPosition(position) ) {
            //outRect.bottom = sectionInset.top
            return
        }
        // 尾部： 专职处理 段 上下边距
        if (adapter.isSectionFooterPosition(position)) {
            //outRect.top = sectionInset.bottom
            return
        }

        // 设置列表间距： 列表一定是columnCount统一的，这点不需保证！！！
        val index = adapter.getPositionWithinSection(position)
        val itemCount = adapter.getItemCountForSection(section) // 获取数量
        val minimumInteritemSpacing = sectionFlow?.layouter?.minimumInteritemSpacing ?: 0
        val minimumLineSpacing = sectionFlow?.layouter?.minimumLineSpacing ?: 0
        //  每个 Item 都可以设置不同的列数： 这样就能算出当前大致有多少个卡片！！，列表一般都是一致的，只有详情可能不一致
        val columnCount = sectionFlow?.layouter?.columnCount ?: 1

        val  column = index % columnCount; // item column
        val  row = index / columnCount // item row
        // 总行数: 横向就是总列数
        val  rowCount = itemCount / columnCount +  if(itemCount % columnCount == 0) 0 else 1


        // 内部最中间的条目的默认上下边距
        outRect.top = minimumLineSpacing / 2
        outRect.bottom = minimumLineSpacing / 2
        // 最顶部的Item
        if(index < columnCount) {
            outRect.top =  sectionInset.top
        }
        // 最后一行
        if(row == rowCount - 1) {
            // 底部边距
            outRect.bottom = sectionInset.bottom
        }
        // 只有一列 好办
        if(columnCount == 1){
            outRect.left = sectionInset.left
            outRect.right = sectionInset.right
            return
        }
        val total = sectionInset.left + sectionInset.right + (columnCount - 1) * minimumInteritemSpacing
        val avg = total / columnCount
        // 终极规律出现了： 归纳法啊
        outRect.left = (minimumInteritemSpacing - avg) * column + sectionInset.left
        outRect.right = avg - (minimumInteritemSpacing - avg) * column - sectionInset.left
    }
}
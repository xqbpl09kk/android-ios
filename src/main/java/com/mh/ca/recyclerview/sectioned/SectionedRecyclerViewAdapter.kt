/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mh.ca.recyclerview.sectioned

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import kotlin.properties.Delegates

/**
 * lai
 * An extension to RecyclerView.Adapter to provide sections with headers and footers to a
 * RecyclerView. Each section can have an arbitrary number of items.
 */
internal abstract class SectionedRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val TAG = "SectionedAdapter"

        protected val TYPE_SECTION_HEADER = -7986
        protected val TYPE_SECTION_FOOTER = -8976
        protected val TYPE_ITEM = -9876
    }
    
    protected var sectionForPosition: IntArray by Delegates.notNull<IntArray>()
    protected var positionWithinSection: IntArray by Delegates.notNull<IntArray>()
    private var isHeader: BooleanArray by Delegates.notNull<BooleanArray>()
    private var isFooter: BooleanArray by Delegates.notNull<BooleanArray>()
    private var count = 0
    // 局部位置到全局位置的映射：
    private var startItemPositionForSection: IntArray by Delegates.notNull<IntArray>()

    // 段落的第一个位置
    private var firstPositionForSection: IntArray by Delegates.notNull<IntArray>()

    // 段落的第一个位置
    private var footerPositionForSection: IntArray by Delegates.notNull<IntArray>()

    // 选中的Item

    protected  var selectedItemPositionForSection: IntArray by Delegates.notNull<IntArray>()

    //  多选支持：
    private var isSelected: BooleanArray by Delegates.notNull<BooleanArray>()

    init {
        registerAdapterDataObserver(SectionDataObserver())
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setupIndices()
    }

    /**
     * Returns the sum of number of items for each section plus headers and footers if they
     * are provided.
     */
    override fun getItemCount(): Int {
        return count
    }

    internal fun setupIndices() {
        
        Log.d("Adapter", "更新索引开始， count: $count")
        
        count = countItems()
        allocateAuxiliaryArrays(count)
        precomputeIndices()
    
        Log.d("Adapter", "更新索引结束， count: $count")
    }

    // 内部私有的 计数 逻辑
    private fun countItems(): Int {
        val sectionCount = this.sectionCount
        startItemPositionForSection = IntArray(sectionCount)
        firstPositionForSection = IntArray(sectionCount)
        footerPositionForSection = IntArray(sectionCount)
        selectedItemPositionForSection = IntArray(sectionCount)

        var count = 0
        val sections = sectionCount

        for (i in 0..sections - 1) {
            firstPositionForSection[i] = count           // 第一个
            count += if (hasHeaderInSection(i)) 1 else 0 // 头部判定
            startItemPositionForSection[i] = count
            selectedItemPositionForSection[i] = -1 //  默认没选中
            count += getItemCountForSection(i)

            if (hasFooterInSection(i)) {
                footerPositionForSection[i] = count
                count += 1
            }else {
                footerPositionForSection[i] = -1
            }

        }

        Log.d(TAG, "段尾列表：" + footerPositionForSection.asList().toString() )
        Log.d(TAG, "段首列表：" + firstPositionForSection.asList().toString() )

        return count
    }

    private fun precomputeIndices() {
        val sections = sectionCount
        var index = 0

        for (i in 0..sections - 1) {
            if (hasHeaderInSection(i)) {
                setPrecomputedItem(index, true, false, i, 0)
                index++
            }

            for (j in 0..getItemCountForSection(i) - 1) {
                setPrecomputedItem(index, false, false, i, j)
                index++
            }

            if (hasFooterInSection(i)) {
                setPrecomputedItem(index, false, true, i, 0)
                index++
            }
        }
    }

    private fun allocateAuxiliaryArrays(count: Int) {
        sectionForPosition = IntArray(count)
        positionWithinSection = IntArray(count)
        isHeader = BooleanArray(count)
        isFooter = BooleanArray(count)

        isSelected = BooleanArray(count)
    }

    private fun setPrecomputedItem(index: Int, isHeader: Boolean, isFooter: Boolean, section: Int, position: Int) {
        this.isHeader[index] = isHeader
        this.isFooter[index] = isFooter
        sectionForPosition[index] = section
        positionWithinSection[index] = position
        // 默认都没选中
        isSelected[index] = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder

        if (isSectionHeaderViewType(viewType)) {
            viewHolder = onCreateSectionHeaderViewHolder(parent, viewType)!! //  一定是配对出现的
        } else if (isSectionFooterViewType(viewType)) {
            viewHolder = onCreateSectionFooterViewHolder(parent, viewType)!!
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType)
        }

        return viewHolder
    }
    

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = sectionForPosition[position]
        val index = positionWithinSection[position]

        if (isSectionHeaderPosition(position)) {
            onBindSectionHeaderViewHolder(holder, section)
        } else if (isSectionFooterPosition(position)) {
            onBindSectionFooterViewHolder(holder, section)
        } else {
            onBindItemViewHolder(holder, section, index)
        }

    }

    override fun getItemViewType(position: Int): Int {

        val section = sectionForPosition[position]
        val index = positionWithinSection[position]

        if (isSectionHeaderPosition(position)) {
            return getSectionHeaderViewType(section)
        } else if (isSectionFooterPosition(position)) {
            return getSectionFooterViewType(section)
        } else {
            return getSectionItemViewType(section, index)
        }

    }

    open protected fun getSectionHeaderViewType(section: Int): Int {
        return TYPE_SECTION_HEADER
    }

    open protected fun getSectionFooterViewType(section: Int): Int {
        return TYPE_SECTION_FOOTER
    }

    open protected fun getSectionItemViewType(section: Int, position: Int): Int {
        return TYPE_ITEM
    }

    /**
     *  判断是否是头部
     */
    fun isSectionHeaderPosition(position: Int): Boolean {
        return isHeader[position]
    }

    /**
     *  判断Item 是否选中
     */
    fun isSelected(section: Int, row: Int): Boolean {
        val position = startItemPositionForSection[section] + row
        return isSelected[position]
    }

    fun setSelected(section: Int, row: Int) {
        val position = startItemPositionForSection[section] + row
        isSelected[position] = true
    }

    fun setUnselected(section: Int, row: Int) {
        val position = startItemPositionForSection[section] + row
        isSelected[position] = false
    }

    /**
     *  获取段内第一个Item的位置
     */
    fun getStartItemPositionForSection(section: Int): Int {
        return startItemPositionForSection[section]
    }

    /**
     *  获取段内的以一个元素的起始位置
     */
    fun getFirstPositionForSection(section: Int): Int {
        return firstPositionForSection[section]
    }

    /**
     *  获取段落尾部的位置
     */
    fun getFooterPositionForSection(section: Int): Int {
        return footerPositionForSection[section]
    }

    /**
     *  获取对应的区间编号
     */
    fun getSectionForPosition(position: Int): Int {
    
        Log.d("Adapter", "size: ${sectionForPosition.size}, count: ${sectionForPosition.count()}, position: $position ")
    
    
        if (position > sectionForPosition.size - 1 || position < 0){
            
            Log.d("Adapter", "拦截成功 size: ${sectionForPosition.size}, position: $position ")
            return -1 // 已经删除了， 没有了
        }
        
        return sectionForPosition[position]
    }

    /**
     *  获取在区间内部的编号
     */
    fun getPositionWithinSection(position: Int): Int {
        return positionWithinSection[position]
    }

    /**
     * 判断是否是尾部
     */
    fun isSectionFooterPosition(position: Int): Boolean {
        return isFooter[position]
    }

    open protected fun isSectionHeaderViewType(viewType: Int): Boolean {
        return viewType == TYPE_SECTION_HEADER
    }

    open protected fun isSectionFooterViewType(viewType: Int): Boolean {
        return viewType == TYPE_SECTION_FOOTER
    }

    /**
     * Returns the number of sections in the RecyclerView
     */
    protected abstract val sectionCount: Int

    /**
     * Returns the number of items for a given section
     */
    abstract fun getItemCountForSection(section: Int): Int

    /**
     * 是否有Section Header 视图，
     */
    abstract fun hasFooterInSection(section: Int): Boolean

    /**
     * 是否有Section Header 视图 这个是改良版本的，自己添加的
     */
     abstract fun hasHeaderInSection(section: Int): Boolean

    /**
     * Creates a ViewHolder of class H for a Header
     */
    abstract fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * Creates a ViewHolder of class F for a Footer
     */
    protected abstract fun onCreateSectionFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * Creates a ViewHolder of class VH for an Item
     */
    protected abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * Binds data to the header view of a given section
     */
    abstract fun onBindSectionHeaderViewHolder(holder: RecyclerView.ViewHolder, section: Int)

    /**
     * Binds data to the footer view of a given section
     */
    protected abstract fun onBindSectionFooterViewHolder(holder: RecyclerView.ViewHolder, section: Int)

    /**
     * Binds data to the item view for a given position within a section
     */
    protected abstract fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, section: Int, position: Int)

    internal inner class SectionDataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            setupIndices()
        }
    
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            setupIndices()
        }
        
    
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            setupIndices()
        }
    }


}

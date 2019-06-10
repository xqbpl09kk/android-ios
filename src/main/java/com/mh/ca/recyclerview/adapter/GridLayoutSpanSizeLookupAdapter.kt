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
package com.mh.ca.recyclerview.adapter

import android.support.v7.widget.GridLayoutManager
import com.mh.ca.flow.CAFlowCollection
import com.mh.ca.flow.PositionType
import com.mh.ca.recyclerview.sectioned.SectionedRecyclerViewAdapter

/**
 * 布局管理器包装
 */
internal class GridLayoutSpanSizeLookupAdapter(
        val adapter: SectionedRecyclerViewAdapter,
        val flowCollection: CAFlowCollection,
        var layoutManager: GridLayoutManager) : GridLayoutManager.SpanSizeLookup() {

    init {
        this.layoutManager.spanSizeLookup = this // 将自身设置成查找器
        this.layoutManager.spanCount = MAX_SPAN_SIZE //  只要配合使用就强制设定为这个
    }
    companion object {
        // 这个最大公约数支持 1 - 7 之内的整除 使用这个类的前提是讲spanCount设置成这个大小
        val MAX_SPAN_SIZE = 420
    }
    override fun getSpanSize(position: Int): Int {

        if (adapter.isSectionHeaderPosition(position) || adapter.isSectionFooterPosition(position)) {
            return MAX_SPAN_SIZE
        } else {
            val section = adapter.getSectionForPosition(position)
            val row = adapter.getPositionWithinSection(position)
    
            var columnCount: Int = 1
            // MARK： iOS 移植
            val sectionFlow = flowCollection.sectionAdapter(section, PositionType.row)
            val rowFlow = sectionFlow?.rowAdapter(row, PositionType.row)
            if (rowFlow != null) {
                columnCount = rowFlow.layouter.columnCount
            }
            return MAX_SPAN_SIZE / columnCount
        }
    }
}

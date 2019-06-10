package com.mh.anko.uikit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.mh.anko.ddp
import com.mh.anko.backgroundDrawable
import com.mh.anko.guard
import com.mh.anko.uikit.UIKit
import java.util.*

/**
 * 页码指示器类，获得此类实例后，可通过[UIPageControl.initIndicator]方法初始化指示器
 */
class UIPageControl(context: Context) : LinearLayout(context) {
    
    constructor():this(UIKit.context) // 默认构造函数
    
    //  这两个一定要到 设置 数量之前调用
    var dotSize = 8 // 指示器的大小（dp）
    var margins = 6 // 指示器间距（dp）
    
    private var indicatorViews: MutableList<View> = mutableListOf() // 减少 NPE
    private val normalDrawable: GradientDrawable = GradientDrawable()
    private val selectedDrawable: GradientDrawable = GradientDrawable()
    
    init {
        gravity = Gravity.CENTER
        orientation = LinearLayout.HORIZONTAL
        
        dotSize = ddp(dotSize)
        margins = ddp(margins)
        
        normalDrawable.setColor(Color.WHITE)
        normalDrawable.shape = GradientDrawable.OVAL
        
        selectedDrawable.setColor(Color.RED)
        selectedDrawable.shape = GradientDrawable.OVAL
    }
    
    var pageIndicatorTintColor: Int
        get() = Color.TRANSPARENT
        set(value) {
            normalDrawable.setColor(value)
        }
    
    var currentPageIndicatorTintColor: Int
        get() = Color.TRANSPARENT
        set(value) {
            selectedDrawable.setColor(value)
        }
    
    
    var numberOfPages: Int
        get() = indicatorViews.count()
        set(value) {
            
            // 重复设置， 没有必要
            guard(value != indicatorViews.count()) {return}
            
            indicatorViews.clear()
            removeAllViews()
    
            val params = LayoutParams(dotSize, dotSize)
            params.setMargins(margins, margins, margins, margins)
    
            for (i in 0..value - 1) {
                val view = View(context)
                view.backgroundDrawable = normalDrawable
                addView(view, params)
                indicatorViews!!.add(view)
            }
            // 第一个默认选中
            if (indicatorViews.size > 0) {
                indicatorViews[0].backgroundDrawable = selectedDrawable
            }
        }
    
    var currentPage: Int = 0
        get() = indicatorViews.count()
        set(value) {
            field = value
            for (i in indicatorViews.indices) {
                if (i == value) {
                    indicatorViews[i].backgroundDrawable = selectedDrawable
                } else {
                    indicatorViews[i].backgroundDrawable = normalDrawable
                }
            }
        }
}
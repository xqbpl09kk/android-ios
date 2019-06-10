package com.mh.ca.recyclerview.view

/**
 * Created by tinytitan on 16/5/22.
 * 模拟CollectionViewCell中backgroundView的概念
 */
interface SelectionAware {
    fun onSelection(selected: Boolean) {
    }
}
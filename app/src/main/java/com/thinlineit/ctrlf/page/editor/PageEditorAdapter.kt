package com.thinlineit.ctrlf.page.editor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageEditorAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private var fragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
    }
}

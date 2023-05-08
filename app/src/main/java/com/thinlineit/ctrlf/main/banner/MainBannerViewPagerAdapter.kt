package com.thinlineit.ctrlf.main.banner

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thinlineit.ctrlf.util.BindingFragmentStateAdapter

class MainBannerViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity), BindingFragmentStateAdapter<List<Fragment>> {
    private var fragmentList = emptyList<Fragment>()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Fragment>) {
        fragmentList = data
        notifyDataSetChanged()
    }
}

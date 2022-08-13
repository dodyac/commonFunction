package com.acxdev.commonFunction.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var listFragmentTabView = mutableListOf<Pair<String, Fragment>>()
    private var listFragment = mutableListOf<Fragment>()
    private var isSetupWithTabView = false

    override fun getItemCount(): Int {
        return if (isSetupWithTabView) listFragmentTabView.size else listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (isSetupWithTabView) listFragmentTabView[position].second else listFragment[position]
    }

    fun set(listFragment: MutableList<Fragment>) {
        this.listFragment = listFragment
    }

    fun sets(vararg fragments: Fragment) {
        fragments.forEach {
            this.listFragment.add(it)
        }
    }

    fun setWithTab(
        listFragment: MutableList<Pair<String, Fragment>>,
        tabLayout: TabLayout,
        viewPager2: ViewPager2
    ) {
        this.listFragmentTabView = listFragment
        isSetupWithTabView = true
        TabLayoutMediator(tabLayout, viewPager2) { tab, pos ->
            tab.text = listFragment[pos].first
        }.attach()
    }
}
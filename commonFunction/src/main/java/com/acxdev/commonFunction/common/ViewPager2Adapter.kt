package com.acxdev.commonFunction.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val listFragmentTabView = mutableListOf<Pair<String, Fragment>>()
    private val listFragment = mutableListOf<Fragment>()
    private var isSetupWithTabView = false

    override fun getItemCount(): Int {
        return if (isSetupWithTabView) {
            listFragmentTabView.size
        } else {
            listFragment.size
        }
    }

    override fun createFragment(position: Int): Fragment {
        return if (isSetupWithTabView) {
            listFragmentTabView[position].second
        } else {
            listFragment[position]
        }
    }

    fun set(fragments: MutableList<Fragment>) {
        listFragment.clear()
        listFragment.addAll(fragments)
    }

    fun sets(vararg fragments: Fragment) {
        listFragment.clear()
        fragments.forEach {
            listFragment.add(it)
        }
    }

    fun setWithTab(
        listFragment: MutableList<Pair<String, Fragment>>,
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        isUserInputEnabled: Boolean = false
    ) {
        listFragmentTabView.clear()
        listFragmentTabView.addAll(listFragment)
        viewPager2.apply {
            adapter = this@ViewPager2Adapter
            this.isUserInputEnabled = isUserInputEnabled
            offscreenPageLimit = listFragment.size
        }
        isSetupWithTabView = true

        TabLayoutMediator(tabLayout, viewPager2) { tab, pos ->
            tab.text = listFragment[pos].first
        }.attach()
    }
}
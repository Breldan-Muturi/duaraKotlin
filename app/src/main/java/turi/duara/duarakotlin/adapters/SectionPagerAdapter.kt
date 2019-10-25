package turi.duara.duarakotlin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import turi.duara.duarakotlin.fragments.ChatsFragment
import turi.duara.duarakotlin.fragments.UsersFragment

class SectionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return UsersFragment()
            }
            1 ->{
                return ChatsFragment()
            }
        }
        return null!!
    }

    override fun getCount(): Int {
        return 2
    }
}
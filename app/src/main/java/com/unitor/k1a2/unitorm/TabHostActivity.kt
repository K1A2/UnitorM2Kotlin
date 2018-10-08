package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.R.attr.path
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import com.unitor.k1a2.unitorm.file.FileIO
import com.unitor.k1a2.unitorm.file.sharedpreference.PreferenceKey
import com.unitor.k1a2.unitorm.file.sharedpreference.SharedPreferenceIO
import kotlinx.android.synthetic.main.activity_tabhost.*
import java.util.*


class TabHostActivity: AppCompatActivity() {

    private lateinit var toolbarV: Toolbar
    private lateinit var tablayout:TabLayout
    private lateinit var viewPager:ViewPager

    private lateinit var sharedPreferenceIO: SharedPreferenceIO
    private lateinit var fileIO:FileIO
    private lateinit var path:String
    private lateinit var title:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tabhost)

        sharedPreferenceIO = SharedPreferenceIO(this, PreferenceKey.KEY_REPOSITORY_INFO)
        fileIO = FileIO(this)
        path = sharedPreferenceIO.getString(PreferenceKey.KEY_INFO_PATH, "")!!
        title = sharedPreferenceIO.getString(PreferenceKey.KEY_INFO_TITLE, "")!!

        if (path.equals("")) {
            if (title.equals("")) {
                //타이틀 없으면 랜덤으로 조합
                val random = Random()
                val stringBuffer = StringBuffer()
                for (i in 0..6) {
                    if (random.nextBoolean()) {
                        stringBuffer.append((random.nextInt(26) as Int + 97).toChar())
                    } else {
                        stringBuffer.append(random.nextInt(10))
                    }
                }
                title = stringBuffer.toString()
                sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_TITLE, title)
            }
            path = fileIO.getDefaultPath() + title + "/"
            sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_PATH, path)
        }

        toolbarV = toolbar
        toolbarV.title = ""
        setSupportActionBar(toolbarV)
        tablayout = tabs
        viewPager = container

        val infoTab = tablayout.newTab()
        infoTab.text = "Info"
        tablayout.addTab(infoTab)

        val soundTab = tablayout.newTab()
        soundTab.text = "KeySound"
        tablayout.addTab(soundTab)

        val ledTab = tablayout.newTab()
        ledTab.text = "KeyLED"
        tablayout.addTab(ledTab)

        val tabPagerAdapter = TabPagerAdapter(supportFragmentManager)
        viewPager.adapter = tabPagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
//                if (menu_save != null) {
//                    when (position) {
//                        0 -> menu_save.setTitle(getString(R.string.save_info))
//
//                        1 -> menu_save.setTitle(getString(R.string.save_keySound))
//
//                        2 -> menu_save.setTitle(getString(R.string.save_KeyLED))
//                    }
//                }
                viewPager.currentItem = position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private inner class TabPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    return InfoFragment()
                }

                1 -> {
                    return KeySoundFragment()
                }

                2 -> {
                    return KeyLEDFragment()
                }

                else -> return null
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.unitor.k1a2.unitorm.file.FileIO
import com.unitor.k1a2.unitorm.file.sharedpreference.PreferenceKey
import com.unitor.k1a2.unitorm.file.sharedpreference.SharedPreferenceIO
import com.unitor.k1a2.unitorm.listener.OnSaveListener
import kotlinx.android.synthetic.main.activity_tabhost.*
import java.util.*
import android.os.AsyncTask
import android.media.AudioManager
import android.media.SoundPool
import java.io.File
import java.io.FileFilter
import android.app.ProgressDialog
import kotlin.collections.ArrayList
import android.widget.Toast
import android.content.Intent


class TabHostActivity: AppCompatActivity() {

    private lateinit var toolbarV: Toolbar
    private lateinit var tablayout:TabLayout
    private lateinit var viewPager:ViewPager
    private lateinit var menu_save:MenuItem

    private var soundPool: SoundPool? = null
    private var isUnload: Boolean = false
    private var array_sounds = ArrayList<Array<Any>>()
    private lateinit var sharedPreferenceIO: SharedPreferenceIO
    private lateinit var onSaveListener: OnSaveListener
    private lateinit var onSaveListener2: OnSaveListener
    private lateinit var onSaveListener3: OnSaveListener
    private lateinit var fileIO:FileIO
    private lateinit var path:String
    private lateinit var title:String
    private var backKeyPress: Long = 0

    fun setSaveListener(listener: OnSaveListener) {
        onSaveListener = listener
    }

    fun setSaveListener2(listener2: OnSaveListener) {
        onSaveListener2 = listener2
    }

    fun setSaveListener3(listener3: OnSaveListener) {
        onSaveListener3 = listener3
    }

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

        soundLoad(path + "sounds/")

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
                if (menu_save != null) {
                    when (position) {
                        0 -> menu_save.title = getString(R.string.save_info)

                        1 -> menu_save.title = getString(R.string.save_keySound)

                        2 -> menu_save.title = getString(R.string.save_KeyLED)
                    }
                }
                viewPager.currentItem = position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    //메뉴생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        menu_save = menu!!.findItem(R.id.menu_save)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.menu_save -> {
                    when (tablayout.selectedTabPosition) {
                        0 -> {
                            onSaveListener.onSave(ActivityKey.KEY_INT_INFO)
                        }
                        1 -> {
                            onSaveListener2.onSave(ActivityKey.KEY_INT_SOUND)
                        }
                        2 -> {
                            onSaveListener3.onSave(ActivityKey.KEY_INT_LED)
                        }
                    }
                    return true
                }
                R.id.menu_saveall -> {
                    return true
                }
                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backKeyPress < 2000) {
            soundUnLoad()
//            isKilledSelf = true//스스로 종료시
//            sharedPKill.setBoolean(PreferenceKey.KEY_KILL_DIED, PreferenceKey.KEY_KILL_SELF)
            startActivity(Intent(this@TabHostActivity, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@TabHostActivity, getString(R.string.toast_back), Toast.LENGTH_SHORT).show()
            backKeyPress = System.currentTimeMillis()
        }
    }

    //파일 로딩
    fun soundLoad(file: String) {
        soundUnLoad()
        try {
            val sound_list = File(file).listFiles(object : FileFilter {
                override fun accept(file: File): Boolean {
                    return file.isFile() && (file.getName().endsWith(".wav") || file.getName().endsWith(".mp3"))
                }
            })
            if (sound_list == null) {
                soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
            } else {
                soundPool = SoundPool(sound_list!!.size, AudioManager.STREAM_MUSIC, 0)
            }
            if (sound_list != null) {
                LoadSound().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sound_list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e.message != null) fileIO.showErr(e.message!!)
        }

    }

    //사운드 언로딘
    fun soundUnLoad() {
        if (soundPool != null) {
            isUnload = true
            for (o in array_sounds) {
                soundPool!!.unload(o[1] as Int)
            }
            soundPool!!.release()
            array_sounds = ArrayList<Array<Any>>()
            isUnload = false
        }
    }

    override fun onPause() {
        super.onPause()
        soundUnLoad()
    }

    //사운드 로딩
    private inner class LoadSound : AsyncTask<Array<File>?, Any, String?>() {

        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            progressDialog = ProgressDialog(this@TabHostActivity)
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setTitle(getString(R.string.async_load_sound_title))
            progressDialog!!.show()
        }

        override fun doInBackground(vararg files: Array<File>?): String? {
            while (isUnload) {
                synchronized(this) {
                    try {
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }

            if (files[0]!! != null) {
                progressDialog!!.max = files[0]!!.size

                for (i in 0 until files[0]!!.size) {
                    val load = soundPool!!.load(files[0]!![i].absolutePath, 0)
                    val name = files[0]!![i].name
                    array_sounds.add(arrayOf(name, load))
                    publishProgress(i, files[0]!![i].absolutePath)
                }
//            for (i:File in files[0] .. files[files.size-1]) {
//                val load = soundPool.load(i.absolutePath, 0)
//                val name = i.name
//                array_sounds.add(arrayOf(name, load))
//                publishProgress(l, i.absolutePath)
//                l++
//            }
            }

            return null
        }

        override fun onProgressUpdate(vararg values: Any) {
            progressDialog!!.progress = values[0] as Int
            progressDialog!!.setMessage(values[1] as String)
        }

        override fun onPostExecute(s: String?) {
            progressDialog!!.dismiss()
        }
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
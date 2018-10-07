package com.unitor.k1a2.unitorm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.Toast
import com.unitor.k1a2.unitorm.file.FileIO
import com.unitor.k1a2.unitorm.view.Recycler.FileListAdapter
import com.unitor.k1a2.unitorm.view.Recycler.FileListItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recycler_Unipack : RecyclerView

    private val recyclerAdapter: FileListAdapter = FileListAdapter()
    private lateinit var fileio: FileIO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (supportActionBar != null) supportActionBar!!.hide()

        fileio = FileIO(this)
        recycler_Unipack = List_Unipack
        recycler_Unipack.layoutManager = LinearLayoutManager(this)
        recycler_Unipack.itemAnimator = DefaultItemAnimator()

        recycler_Unipack.adapter = recyclerAdapter

        showUnipack()
    }

    private fun showUnipack() {
        recyclerAdapter.clearItem()
        val arrayUnipack = fileio.getUnipacks()
        if (arrayUnipack != null) {
            for (unipackInfo in arrayUnipack) {
                if (unipackInfo != null) {
                    val uni: FileListItem = FileListItem()
                    uni.fname = unipackInfo[0]
                    uni.fproducer = unipackInfo[1]
                    uni.fchain = unipackInfo[3]
                    uni.fpath = unipackInfo[2]
                    recyclerAdapter.addItem(uni)
                }
            }
        }
        recycler_Unipack.adapter = recyclerAdapter
    }
}

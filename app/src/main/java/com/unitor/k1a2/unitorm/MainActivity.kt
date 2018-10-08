package com.unitor.k1a2.unitorm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
import com.unitor.k1a2.unitorm.file.FileIO
import com.unitor.k1a2.unitorm.view.Recycler.UnipackListAdapter
import com.unitor.k1a2.unitorm.view.Recycler.UnipackListItem
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.os.AsyncTask
import com.unitor.k1a2.unitorm.file.asynctask.UnzipFile
import com.unitor.k1a2.unitorm.file.sharedpreference.PreferenceKey
import com.unitor.k1a2.unitorm.file.sharedpreference.SharedPreferenceIO
import com.unitor.k1a2.unitorm.view.Recycler.listener.RecyclerItemClickListener
import com.unitor.k1a2.unitorm.view.dialog.DialogKey
import com.unitor.k1a2.unitorm.view.dialog.FileExDialog
import com.unitor.k1a2.unitorm.file.FileKey
import com.unitor.k1a2.unitorm.file.asynctask.DeleteFile




class MainActivity : AppCompatActivity(), FileExDialog.OnUnipackSelectListener {

    private lateinit var recycler_Unipack : RecyclerView
    private lateinit var floating_new: FloatingActionButton
    private lateinit var floating_import: FloatingActionButton
    private lateinit var floating_setting: FloatingActionButton

    private val recyclerAdapter: UnipackListAdapter = UnipackListAdapter()
    private lateinit var sharedPreferenceIO: SharedPreferenceIO
    private lateinit var fileio: FileIO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (supportActionBar != null) supportActionBar!!.hide()

        fileio = FileIO(this)
        recycler_Unipack = List_Unipack
        floating_new = fab_new
        floating_import = fab_import
        floating_setting = fab_setting

        recycler_Unipack.layoutManager = LinearLayoutManager(this)
        recycler_Unipack.itemAnimator = DefaultItemAnimator()

        recycler_Unipack.adapter = recyclerAdapter

        floating_new.setOnClickListener(fabListener)
        floating_setting.setOnClickListener(fabListener)
        floating_import.setOnClickListener(fabListener)

        showUnipack()

        recycler_Unipack.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_Unipack, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val item: UnipackListItem = recyclerAdapter.getItem(position)
                startEdit(item.fname!!, item.fproducer!!, item.fchain!!, item.fpath!!)
            }

            override fun onLongItemClicked(view: View?, position: Int) {
                val unipackListItem = recyclerAdapter.getItem(position)
                val delete = AlertDialog.Builder(this@MainActivity)
                delete.setTitle(String.format(getString(R.string.alert_title_dunipack), unipackListItem.fname))
                delete.setMessage(String.format(getString(R.string.alert_message_dunipack), unipackListItem.fname))
                delete.setNegativeButton(getString(R.string.alert_button_dcancel), null)
                delete.setPositiveButton(getString(R.string.alert_button_dok)) { dialogInterface, i ->
                    val deleteFile = DeleteFile(this@MainActivity)
                    deleteFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, FileKey.KEY_DELETE_UNIPACK, unipackListItem.fpath, String.format(getString(R.string.asynk_delete_title), unipackListItem.fname), recycler_Unipack, recyclerAdapter, position)
                }
                delete.show()
            }

        }))
    }

    override fun onResume() {
        super.onResume()
        showUnipack()
    }

    private fun showUnipack() {
        recyclerAdapter.clearItem()
        val arrayUnipack = fileio.getUnipacks()
        if (arrayUnipack != null) {
            for (unipackInfo in arrayUnipack) {
                if (unipackInfo != null) {
                    val uni: UnipackListItem = UnipackListItem()
                    uni.fname = unipackInfo[0]
                    uni.fproducer = unipackInfo[1]
                    uni.fchain = unipackInfo[2]
                    uni.fpath = unipackInfo[3]
                    recyclerAdapter.addItem(uni)
                }
            }
        }
        recycler_Unipack.adapter = recyclerAdapter
    }

    private fun startEdit(string_title: String, string_producer: String, string_chain: String, string_path: String) {
        sharedPreferenceIO = SharedPreferenceIO(this@MainActivity, PreferenceKey.KEY_REPOSITORY_INFO)
        sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_TITLE, string_title)
        sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_PRODUCER, string_producer)
        sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_CHAIN, string_chain)
        sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_PATH, string_path)
        val intent = Intent(this@MainActivity, TabHostActivity::class.java)
        intent.putExtra("KILL", false)
        startActivity(intent)
        finish()
    }

    override fun onUnipackSelect(name: String, path: String) {
        val unzipFile: UnzipFile = UnzipFile(this)
        unzipFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, name, path, fileio.getDefaultPath() + "unipackProject/" + name +"/", recyclerAdapter)
    }

    private val fabListener = View.OnClickListener { p0 ->
        if (p0 != null) {
            when(p0.id) {
                R.id.fab_new -> {
                    val layout: LinearLayout = View.inflate(this@MainActivity, R.layout.dialog_newpack, null) as LinearLayout
                    val p: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    p.setView(layout)
                    p.setTitle(getString(R.string.dialog_title_newUnipack))
                    p.setPositiveButton(getString(R.string.dialog_Make)) { dialog, which ->
                        val edit_title: EditText  = layout.findViewById(R.id.Edit_Title_new) as EditText
                        val edit_producer: EditText  = layout.findViewById(R.id.Edit_Producer_new) as EditText
                        val edit_chain: EditText  = layout.findViewById(R.id.Edit_Chain_new) as EditText

                        val string_title: String = edit_title.text.toString()
                        val string_producer: String  = edit_producer.text.toString()
                        val string_chain: String = edit_chain.text.toString();
                        val string_path: String = fileio.getDefaultPath() + "unipackProject/" + string_title + "/"

                        if ((string_title.isEmpty() || string_title == "")||(string_producer.isEmpty() || string_producer == "")||(string_chain.isEmpty() || string_chain == "")) {
                            Toast.makeText(this@MainActivity, getString(R.string.toast_newUnipack_null), Toast.LENGTH_LONG).show()
                        } else {
                            try {
                                fileio.mkNewUnipack(string_title, string_producer, string_chain, string_path)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                if (e.message != null) fileio.showErr(e.message!!)
                            }
                            startEdit(string_title, string_producer, string_chain, string_path)
                        }
                    }
                    p.show()
                }
                R.id.fab_import -> {
                    val args = Bundle()
                    args.putInt(DialogKey.KEY_BUNDLE_TYPE, DialogKey.KEY_BUNDLE_TYPE_UNIPACK)
                    val dia = FileExDialog()
                    dia.arguments = args
                    dia.show(supportFragmentManager, DialogKey.KEY_MAIN_FILE)
                }
                R.id.fab_setting -> {
                    Toast.makeText(this@MainActivity, "setting", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

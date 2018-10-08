package com.unitor.k1a2.unitorm.view.dialog

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.unitor.k1a2.unitorm.R
import com.unitor.k1a2.unitorm.view.Recycler.SelectFileAdapter
import com.unitor.k1a2.unitorm.view.dialog.FileExDialog.OnFileSelectListener
import com.unitor.k1a2.unitorm.view.dialog.FileExDialog.OnUnipackSelectListener
import kotlinx.android.synthetic.main.dialog_fileex.*
import org.w3c.dom.Text
import java.nio.file.Files.isDirectory
import android.widget.AbsListView
import android.text.method.TextKeyListener.clear
import com.unitor.k1a2.unitorm.file.FileIO
import com.unitor.k1a2.unitorm.view.Recycler.SelectFileItem
import java.io.File
import java.io.FileFilter
import java.util.*
import android.util.DisplayMetrics
import android.os.AsyncTask
import com.unitor.k1a2.unitorm.MainActivity
import android.content.DialogInterface
import com.unitor.k1a2.unitorm.view.Recycler.UnipackListItem
import com.unitor.k1a2.unitorm.view.Recycler.listener.RecyclerItemClickListener




class FileExDialog: DialogFragment() {

    private lateinit var button_add:Button
    private lateinit var text_path:TextView
    private lateinit var text_title:TextView
    private lateinit var recycler_files:RecyclerView

    private lateinit var fileIO: FileIO
    private lateinit var bundle: Bundle
    private lateinit var activityA: Activity
    private lateinit var selectFileAdapter: SelectFileAdapter
    private var type: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root:View = inflater.inflate(R.layout.dialog_fileex, container, false)

        fileIO = FileIO(this!!.context!!)

        button_add = root.findViewById(R.id.dialog_add)
        text_path = root.findViewById(R.id.text_file_path)
        text_title = root.findViewById(R.id.text_sum)
        recycler_files = root.findViewById(R.id.recycle_files)

        if (arguments != null) {
            bundle = arguments!!
            type = bundle.getInt(DialogKey.KEY_BUNDLE_TYPE)
        }

        recycler_files.layoutManager = LinearLayoutManager(this.context)
        recycler_files.itemAnimator = DefaultItemAnimator()

        selectFileAdapter = SelectFileAdapter(type)

        recycler_files.adapter = selectFileAdapter

        if (type == DialogKey.KEY_BUNDLE_TYPE_UNIPACK) {
            onUnipackSelectListener = activityA as OnUnipackSelectListener
            text_title.text = getString(R.string.dialog_title_unipack)
            button_add.visibility = View.GONE
        } else if (type == DialogKey.KEY_BUNDEL_TYPE_FILES) {
            onFileSelectListener = activityA as OnFileSelectListener
            text_title.text = getString(R.string.dialog_title_file)
        }

        recycler_files.addOnItemTouchListener(RecyclerItemClickListener(this.context!!, recycler_files, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val item: SelectFileItem = selectFileAdapter.getItem(position)
                val name = item.titlef
                val  path = item.pathf

                if (name.equals(".../")) {
                    setFileList(type, File(text_path.text.toString()).parentFile.absolutePath + "/")
                } else if (File(path).isDirectory) {
                    setFileList(type, path!!)
                } else {
                    if (name!!.endsWith(".zip")) {
                        onUnipackSelectListener.onUnipackSelect(name, path!!)
                        dismiss()
                    } else if (name.endsWith(".wav")||name.endsWith(".mp3")) {

                    }
                }
            }

            override fun onLongItemClicked(view: View?, position: Int) {

            }
        }))

        setFileList(type, fileIO.getDefaultPath())

        return root
    }

    override fun onStart() {
        super.onStart()
        if (context != null) {
            val dm = context!!.resources.displayMetrics
            val width = dm.widthPixels
            val height = dm.heightPixels
            dialog.window.setLayout(width*95/100, height*95/100);
        }
    }

    private fun setFileList(type: Int, path: String) {
        var list: Array<File>? = null

        text_path.text = path
        selectFileAdapter.clearItem()

        if (type == DialogKey.KEY_BUNDLE_TYPE_UNIPACK) {
            //recycler_files.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
            list = File(path).listFiles { file -> file.isDirectory() || (file.isFile() && file.getName().endsWith(".zip")) }
        } else if (type == DialogKey.KEY_BUNDEL_TYPE_FILES) {
            //list_File.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
            list = File(path).listFiles { file -> file.isDirectory() || (file.isFile() && (file.getName().endsWith(".wav") || file.getName().endsWith(".mp3"))) }
        }
        Arrays.sort(list) { file, t1 -> file.getName().compareTo(t1.getName(), true) }

        if (path != fileIO.getDefaultPath()) {
            val a = SelectFileItem()
            a.iconf = context!!.resources.getDrawable(android.support.design.R.drawable.navigation_empty_icon)
            a.titlef = ".../"
            a.pathf = ""
            selectFileAdapter.addItem(a)
        }

        for (file in list!!) {
            if (file.isDirectory()) {
                val a = SelectFileItem()
                a.iconf = context!!.resources.getDrawable(R.drawable.round_folder_black_48)
                a.titlef = file.name
                a.pathf = file.absolutePath + "/"
                selectFileAdapter.addItem(a)
            } else {
                if (file.getName().endsWith(".zip")) {
                    val a = SelectFileItem()
                    a.iconf = context!!.resources.getDrawable(R.drawable.round_folder_open_black_48)
                    a.titlef = file.name
                    a.pathf = file.absolutePath + "/"
                    selectFileAdapter.addItem(a)
                } else if (file.getName().endsWith(".wav") || file.getName().endsWith(".mp3")) {
                    val a = SelectFileItem()
                    a.iconf = context!!.resources.getDrawable(R.drawable.round_music_note_black_48)
                    a.titlef = file.name
                    a.pathf = file.absolutePath + "/"
                    selectFileAdapter.addItem(a)
                }
            }
        }
    }

    lateinit var onUnipackSelectListener: OnUnipackSelectListener
    lateinit var onFileSelectListener: OnFileSelectListener

    interface OnUnipackSelectListener {
        fun onUnipackSelect(name: String, path: String)
    }

    interface OnFileSelectListener {
        fun onFileSelect(files: ArrayList<Array<String>>)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        activityA = activity
    }
}
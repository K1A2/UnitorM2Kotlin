package com.unitor.k1a2.unitorm.file.asynctask

import com.unitor.k1a2.unitorm.view.Recycler.UnipackListAdapter
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import com.unitor.k1a2.unitorm.file.FileKey
import java.io.File


class DeleteFile(private val context: Context) : AsyncTask<Any, String, Array<Any>>() {
    private var progressDialog: ProgressDialog? = null
    private var unipackListAdapter: UnipackListAdapter? = null
    private var position: Int = 0

    override fun onPreExecute() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
    }

    override fun doInBackground(vararg objects: Any): Array<Any> {
        val key = objects[0] as String

        if (key == FileKey.KEY_DELETE_UNIPACK) {
            val title = objects[2] as String
            val path = objects[1] as String
            unipackListAdapter = objects[4] as UnipackListAdapter
            position = objects[5] as Int
            publishProgress(key, title)
            delete(path)
        }

        return arrayOf(key, position)
    }

    override fun onProgressUpdate(vararg values: String) {
        if (values[0] == FileKey.KEY_DELETE_UNIPACK) {
            progressDialog!!.setTitle(values[1])
            progressDialog!!.show()
        }
    }

    override fun onPostExecute(objects: Array<Any>) {
        progressDialog!!.dismiss()
        if (objects[0] as String == FileKey.KEY_DELETE_UNIPACK) {
            unipackListAdapter!!.removeItem(objects[1] as Int)
        }
    }

    private fun delete(path: String) {
        val d = File(path)
        if (d.exists()) {
            val childFileList = d.listFiles()
            for (childFile in childFileList) {
                if (childFile.isDirectory()) {
                    delete(childFile.getPath())
                } else {
                    childFile.delete()
                }
            }
            d.delete()
        }
    }
}
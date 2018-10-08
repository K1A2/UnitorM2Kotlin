package com.unitor.k1a2.unitorm.file.asynctask

import android.os.AsyncTask
import android.content.DialogInterface
import com.unitor.k1a2.unitorm.R.string.alert_ok
import com.unitor.k1a2.unitorm.R.string.dialog_unzip_failM
import com.unitor.k1a2.unitorm.R.string.dialog_unzip_failT
import com.unitor.k1a2.unitorm.view.Recycler.UnipackListItem
import com.unitor.k1a2.unitorm.file.FileIO
import com.unitor.k1a2.unitorm.R.string.dialog_unzip_sucM
import com.unitor.k1a2.unitorm.R.string.dialog_unzip_sucT
import android.app.ProgressDialog
import android.content.Context
import com.unitor.k1a2.unitorm.R
import com.unitor.k1a2.unitorm.R.string.async_unzip_message
import com.unitor.k1a2.unitorm.R.string.async_unzip_title
import java.nio.file.Files.isDirectory
import com.unitor.k1a2.unitorm.view.Recycler.UnipackListAdapter
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class UnzipFile(private val context: Context) : AsyncTask<Any, String, Boolean>() {
    private var progressDialog: ProgressDialog? = null
    private var name: String? = null
    private var path: String? = null
    private var target: String? = null
    private var finish: String? = null
    private var adapter: UnipackListAdapter? = null

    override fun onPreExecute() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    override fun doInBackground(vararg objects: Any): Boolean? {
        name = objects[0] as String
        path = objects[1] as String
        target = objects[2] as String
        adapter = objects[3] as UnipackListAdapter
        publishProgress("start", name)

        try {
            val fileInputStream = FileInputStream(path)
            val zipInputStream = ZipInputStream(fileInputStream)
            var zipEntry: ZipEntry? = null

            var targetFile: File? = null
            do {
                zipEntry = zipInputStream.getNextEntry()
                if (zipEntry == null) {
                    break
                }
                val filenameTounzip = zipEntry!!.getName()
                targetFile = File(target, filenameTounzip)

                if (zipEntry!!.isDirectory()) {
                    val pathF = File(targetFile!!.getAbsolutePath())
                    pathF.mkdirs()
                } else {
                    val pathF = File(targetFile!!.getParent())
                    pathF.mkdirs()
                    Unzip(zipInputStream, targetFile)
                }
            } while (true)
//            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//                val filenameTounzip = zipEntry!!.getName()
//                targetFile = File(target, filenameTounzip)
//
//                if (zipEntry!!.isDirectory()) {
//                    val pathF = File(targetFile!!.getAbsolutePath())
//                    pathF.mkdirs()
//                } else {
//                    val pathF = File(targetFile!!.getParent())
//                    pathF.mkdirs()
//                    Unzip(zipInputStream, targetFile)
//                }
//            }

            fileInputStream.close()
            zipInputStream.close()
            if (targetFile != null) finish = targetFile!!.getAbsolutePath()
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            publishProgress(e.message)
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            publishProgress(e.message)
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            publishProgress(e.message)
            return false
        }

    }

    override fun onProgressUpdate(vararg values: String) {
        if (values[0] == "start") {
            progressDialog!!.setTitle(context.getString(R.string.async_unzip_title))
            progressDialog!!.setMessage(String.format(context.getString(R.string.async_unzip_message), values[1]))
            progressDialog!!.show()
        } else {
            FileIO(context).showErr(values[0])
        }
    }

    override fun onPostExecute(b: Boolean?) {
        progressDialog!!.dismiss()
        progressDialog = ProgressDialog(context)
        if (b == true) {
            progressDialog!!.setTitle(String.format(context.getString(R.string.dialog_unzip_sucT), name))
            progressDialog!!.setMessage(String.format(context.getString(R.string.dialog_unzip_sucM), name))
            val s = FileIO(context).getUnipackInfo(File(target!! + "info"), target!!)
            val item = UnipackListItem()
            item.fname = s!![0]
            item.fproducer = s!![1]
            item.fchain = s!![2]
            item.fpath = s!![3]
            adapter!!.addItem(item)
        } else {
            progressDialog!!.setTitle(String.format(context.getString(R.string.dialog_unzip_failT), name))
            progressDialog!!.setMessage(String.format(context.getString(R.string.dialog_unzip_failM), name))
        }
        progressDialog!!.setButton(context.getString(R.string.alert_ok)) { dialogInterface, i -> progressDialog!!.dismiss() }
        progressDialog!!.show()
    }

    @Throws(IOException::class)
    private fun Unzip(zipInputStream: ZipInputStream, targetFile: File): File {
        var fileOutputStream: FileOutputStream? = null

        val BUFFER_SIZE = 1024 * 2

        try {
            fileOutputStream = FileOutputStream(targetFile)

            val buffer = ByteArray(BUFFER_SIZE)
            var len = 0
            try {
                do {
                    len = zipInputStream.read(buffer)
                    if (len == -1) {
                        break
                    }
                    fileOutputStream!!.write(buffer, 0, len)
                } while (true)
//                while ((len = zipInputStream.read(buffer)) != -1) {
//                    fileOutputStream!!.write(buffer, 0, len)
//                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                fileOutputStream!!.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return targetFile
    }
}
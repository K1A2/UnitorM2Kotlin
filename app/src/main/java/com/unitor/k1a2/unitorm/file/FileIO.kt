package com.unitor.k1a2.unitorm.file

import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import java.nio.file.Files.exists
import java.nio.file.Files.isDirectory
import android.os.Environment.getExternalStorageDirectory
import com.unitor.k1a2.unitorm.FileKey
import com.unitor.k1a2.unitorm.R
import java.io.*
import java.util.ArrayList


class FileIO(private val context: Context) : ContextWrapper(context) {

    private val defaultpath: String = Environment.getExternalStorageDirectory().absolutePath + "/"


    /**유니팩 관련**/
    fun getUnipacks(): ArrayList<Array<String>?>? {
        var unipackProjectF: File = File(defaultpath + "unipackProject/")

        try {
            isExists(unipackProjectF, FileKey.KEY_DIRECTORY_INT)
            isExists(File(defaultpath + "unipackProject/.nomedia"), FileKey.KEY_FILE_INT)
        } catch (e:Exception) {
            e.printStackTrace()
            if (e.message != null) showErr(e.message!!)
            return null
        }

        val unipackList = unipackProjectF.listFiles(FileFilter { file -> file.isDirectory })


        val unipackInfo: ArrayList<Array<String>?>? = ArrayList()

        for (arrayUnipack in unipackList) {
            val path = arrayUnipack.absolutePath
            if (unipackInfo != null) {
                unipackInfo.add(getUnipackInfo(File(path + "/info"), path + "/"))
            }
        }

        return unipackInfo
    }

    //새 유팩생성
    @Throws(Exception::class)
    fun mkNewUnipack(Title: String, Producer: String, Chain: String, path: String) {
        var path = path
        val file = File(path)
        isExists(file, FileKey.KEY_DIRECTORY_INT)

        path += "info"
        mkInfo(Title, Producer, Chain, path)
    }

    //인포 생성, 저장
    @Throws(Exception::class)
    fun mkInfo(Title: String, Producer: String, Chain: String, path: String) {
        val file = File(path)
        isExists(file, FileKey.KEY_FILE_INT)

        val printWriter = PrintWriter(file)
        printWriter.printf(FileKey.KEY_INFO_CONTENT, Title, Producer, Chain)
        printWriter.close()
    }

    //유니팩 인포 가져옴
    fun getUnipackInfo(unipack: File, path: String): Array<String>? {
        if (unipack.exists()) {
            try {
                val arrayInfo = getTextFile(unipack)

                val info: Array<String> = Array(4) { i ->  ""}
                if (arrayInfo != null) {
                    for (`in` in arrayInfo) {
                        when {
                            `in`.startsWith(FileKey.KEY_INFO_TITLE) -> info[0] = `in`.replace(FileKey.KEY_INFO_TITLE, "")
                            `in`.startsWith(FileKey.KEY_INFO_PRODUCER) -> info[1] = `in`.replace(FileKey.KEY_INFO_PRODUCER, "")
                            `in`.startsWith(FileKey.KEY_INFO_CHAIN) -> info[2] = `in`.replace(FileKey.KEY_INFO_CHAIN, "")
                        }
                    }
                }
                info[3] = path

                return info
            } catch (e: Exception) {
                e.printStackTrace()
                if (e.message != null) showErr(e.message!!)
                return null
            }

        } else {
            return null
        }
    }

    /**그밖에것들 */
    fun getDefaultPath(): String {
        return defaultpath
    }

    //파일 유무, 없으면 생성
    @Throws(Exception::class)
    fun isExists(file: File, i: Int) {
        if (!file.exists()) {
            if (i == FileKey.KEY_FILE_INT) {
                file.createNewFile()
            } else if (i == FileKey.KEY_DIRECTORY_INT) {
                file.mkdirs()
            }
        }
    }

    //파일내용 가져옴
    @Throws(Exception::class)
    fun getTextFile(file: File): ArrayList<String>? {
        if (file.exists()) {
            val arrayFile = ArrayList<String>()
            val bufferedReader = BufferedReader(FileReader(file))
            var line: String?

            do {
                line = bufferedReader.readLine()
                if (line == null) {
                    break
                }
                if (!line.isEmpty()) {
                    arrayFile.add(line)
                }
            } while (true)
            bufferedReader.close()

            return arrayFile
        } else {
            return null
        }
    }

    //에러출력
    fun showErr(e: String) {
        val alertErr = AlertDialog.Builder(context)
        alertErr.setTitle(getString(R.string.alert_err))
        alertErr.setMessage(e)
        alertErr.setPositiveButton(getString(R.string.alert_ok), null)
        alertErr.show()
    }
}
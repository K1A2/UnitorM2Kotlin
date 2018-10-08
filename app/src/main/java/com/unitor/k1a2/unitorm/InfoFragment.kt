package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.EditText
import com.unitor.k1a2.unitorm.file.sharedpreference.PreferenceKey
import com.unitor.k1a2.unitorm.file.sharedpreference.SharedPreferenceIO
import com.unitor.k1a2.unitorm.file.FileIO
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.unitor.k1a2.unitorm.listener.SaveListener


class InfoFragment: Fragment(), SaveListener {

    private var a:Int = 0
    private lateinit var edit_Title: EditText
    private lateinit var edit_Producer: EditText
    private lateinit var edit_Chain: EditText
    private lateinit var text_Info: TextView

    private lateinit var sharedPreferenceIO: SharedPreferenceIO
    private lateinit var fileIO: FileIO
    private var listInfo: ArrayList<String>? = null
    private lateinit var title: String
    private lateinit var producer: String
    private lateinit var chain: String
    private lateinit var path: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root:View = inflater.inflate(R.layout.fragment_info, container, false)

        (activity as TabHostActivity).setSaveListener(this)

        edit_Title = root!!.findViewById(R.id.Edit_Title)
        edit_Chain = root!!.findViewById(R.id.Edit_Chain)
        edit_Producer = root!!.findViewById(R.id.Edit_Producer)
        text_Info = root!!.findViewById(R.id.Text_info)

        sharedPreferenceIO = SharedPreferenceIO(context!!, PreferenceKey.KEY_REPOSITORY_INFO)
        listInfo = ArrayList<String>()
        fileIO = FileIO(context!!)

        title = sharedPreferenceIO.getString(PreferenceKey.KEY_INFO_TITLE, "")!!
        path = sharedPreferenceIO.getString(PreferenceKey.KEY_INFO_PATH, "")!!
        producer = sharedPreferenceIO.getString(PreferenceKey.KEY_INFO_PRODUCER, "")!!
        chain = sharedPreferenceIO.getString(PreferenceKey.KEY_INFO_CHAIN, "")!!

        setText_Info()

        edit_Title.setText(title)
        edit_Producer.setText(producer)
        edit_Chain.setText(chain)

        //에딧 택스트 편집
        edit_Title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                title = editable.toString()
                sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_TITLE, title)
            }
        })
        edit_Producer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                producer = editable.toString()
                sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_PRODUCER, producer)
            }
        })
        edit_Chain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                chain = editable.toString()
                sharedPreferenceIO.setString(PreferenceKey.KEY_INFO_CHAIN, chain)
            }
        })

        return root
    }

    override fun onSave(key: Int) {
        saveInfo()
    }

    private fun saveInfo() {
        if (title.equals("") || producer.equals("") || chain.equals("")) {
            Toast.makeText(context, getString(R.string.toast_newUnipack_null), Toast.LENGTH_LONG).show()
        } else {
            try {
                fileIO.mkInfo(title, producer, chain, path + "info")
                Toast.makeText(context, getString(R.string.toast_save_succeed), Toast.LENGTH_SHORT).show()
                setText_Info()
            } catch (e: Exception) {
                e.printStackTrace()
                if (e.message != null) fileIO.showErr(e.message!!)
                Toast.makeText(context, getString(R.string.toast_save_fail), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setText_Info() {
        //info내용 가져옴
        try {
            listInfo = fileIO.getInfo(path)
        } catch (e: Exception) {
            e.printStackTrace()
            listInfo = null
            if (e.message != null) fileIO.showErr(e.message!!)
        }

        //info내용 출력
        if (listInfo != null) {
            val stringBuilder = StringBuilder()
            for (content in listInfo!!) {
                stringBuilder.append(content + "\n")
            }
            text_Info.text = stringBuilder
        }
    }
}
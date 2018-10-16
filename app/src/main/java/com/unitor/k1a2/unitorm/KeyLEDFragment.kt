package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.unitor.k1a2.unitorm.listener.OnSaveListener

class KeyLEDFragment: Fragment(), OnSaveListener {

    private var a:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root:View = inflater.inflate(R.layout.fragment_keyled, container, false)

        (activity as TabHostActivity).setSaveListener3(this)

        return root
    }

    override fun onSave(key: Int) {
        Toast.makeText(context!!, "SaveLED"+a.toString(), Toast.LENGTH_SHORT).show()
        a++
    }
}
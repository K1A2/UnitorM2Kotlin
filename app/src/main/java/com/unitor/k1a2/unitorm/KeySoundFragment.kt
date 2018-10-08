package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.unitor.k1a2.unitorm.listener.SaveListener

class KeySoundFragment: Fragment(), SaveListener {

    private var a:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root:View = inflater.inflate(R.layout.fragment_keysound, container, false)

        (activity as TabHostActivity).setSaveListener2(this)

        return root
    }

    override fun onSave(key: Int) {
        Toast.makeText(context!!, "SaveSound"+a.toString(), Toast.LENGTH_SHORT).show()
        a++
    }
}
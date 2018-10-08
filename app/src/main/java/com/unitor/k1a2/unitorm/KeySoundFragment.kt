package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class KeySoundFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root:View = inflater.inflate(R.layout.fragment_keysound, container, false)
        return root
    }
}
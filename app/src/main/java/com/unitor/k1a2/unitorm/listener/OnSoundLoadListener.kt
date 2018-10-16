package com.unitor.k1a2.unitorm.listener

import java.io.File

interface OnSoundLoadListener {
    fun onLoad(files: ArrayList<File>)
}
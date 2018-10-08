package com.unitor.k1a2.unitorm.view

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.LinearLayout
import com.unitor.k1a2.unitorm.R
import java.util.jar.Attributes


class CheckView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), Checkable {

    private var checkBox: CheckBox? = null

    override fun setChecked(b: Boolean) {
        checkBox = findViewById(R.id.Fragdial_file_isSelect) as CheckBox
        if (checkBox!!.isChecked != b) {
            checkBox!!.isChecked = b
        }
    }

    override fun isChecked(): Boolean {
        checkBox = findViewById(R.id.Fragdial_file_isSelect) as CheckBox
        return checkBox!!.isChecked
    }

    override fun toggle() {
        checkBox = findViewById(R.id.Fragdial_file_isSelect) as CheckBox
        isChecked = if (checkBox!!.isChecked) false else true
    }
}
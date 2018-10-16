package com.unitor.k1a2.unitorm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unitor.k1a2.unitorm.listener.OnSaveListener
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.ViewTreeObserver
import android.widget.*


class KeySoundFragment: Fragment(), OnSaveListener {

    private lateinit var linear_buttons:LinearLayout
    private lateinit var imagebtn_add:ImageButton
    private lateinit var text_current:TextView
    private lateinit var recycle_sounds:RecyclerView
    private lateinit var spinnera_chain:Spinner
    private lateinit var check_isdelete:CheckBox
    private lateinit var text_content:TextView
    private lateinit var radio_edit:RadioButton
    private lateinit var radio_test:RadioButton
    private lateinit var rgroup_chain:RadioGroup

    private var a:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root:View = inflater.inflate(R.layout.fragment_keysound, container, false)

        linear_buttons = root.findViewById(R.id.Layout_Btns)
        imagebtn_add = root.findViewById(R.id.Button_Image_add)//add sound
        text_current = root.findViewById(R.id.Text_current_sound)//text_current
        recycle_sounds = root.findViewById(R.id.List_KeySound)//sound list
        spinnera_chain = root.findViewById(R.id.Spinner_chain)//chain selecting
        check_isdelete = root.findViewById(R.id.Check_deleteSound)//check delete
        text_content = root.findViewById(R.id.text_content)//show keysound
        radio_edit = root.findViewById(R.id.Radio_Edit)
        radio_test = root.findViewById(R.id.Radio_Test)
        rgroup_chain = root.findViewById(R.id.RadioG_chain_S)

        (activity as TabHostActivity).setSaveListener2(this)
        Toast.makeText(context!!, "Sound", Toast.LENGTH_SHORT).show()

        return root
    }

    //버튼 크기조절
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewTreeObserver = linear_buttons.getViewTreeObserver()
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linear_buttons.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                } else {
                    linear_buttons.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                }
                val height = linear_buttons.getHeight()
                val params = RelativeLayout.LayoutParams(height, height)
                params.addRule(RelativeLayout.CENTER_HORIZONTAL)
                linear_buttons.setLayoutParams(params)
            }
        })
    }

    override fun onSave(key: Int) {
        Toast.makeText(context!!, "SaveSound"+a.toString(), Toast.LENGTH_SHORT).show()
        a++
    }
}
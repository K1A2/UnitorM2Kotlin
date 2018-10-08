package com.unitor.k1a2.unitorm.view.Recycler

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import com.unitor.k1a2.unitorm.view.dialog.DialogKey
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.unitor.k1a2.unitorm.R


//class SelectFileAdapter(private val type: Int) : BaseAdapter() {
//
//    private val listViewList = ArrayList<SelectFileItem>()
//    private var icon: Drawable? = null
//    private var title: String? = null
//    private var path: String? = null
//
//    override fun getCount(): Int {
//        return listViewList.size
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var convertView = convertView
//        val context = parent.context
//
//        if (convertView == null) {
//            when (type) {
//                DialogKey.KEY_BUNDLE_TYPE_UNIPACK -> {
//                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                    convertView = inflater.inflate(R.layout.view_list_select_u, parent, false)
//                }
//
//                DialogKey.KEY_BUNDEL_TYPE_FILES -> {
//                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                    convertView = inflater.inflate(R.layout.view_list_select_f, parent, false)
//                }
//            }
//        }
//
//        val titleView = convertView!!.findViewById(R.id.FragDial_file_name_select) as TextView
//        val pathView = convertView!!.findViewById(R.id.FragDial_file_path_select) as TextView
//        val iconView = convertView!!.findViewById(R.id.FragDial_file_Image_select) as ImageView
//
//        val listItem = listViewList[position]
//
//        title = listItem.titlef
//        path = listItem.pathf
//        icon = listItem.iconf
//
//        titleView.text = title
//        pathView.text = path
//        iconView.setImageDrawable(icon)
//
//        return convertView
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItem(position: Int): Any {
//        return listViewList[position]
//    }
//
//    fun addItem(title: String, path: String, icon: Drawable) {
//        val listItem = SelectFileItem()
//
//        listItem.titlef = title
//        listItem.pathf = path
//        listItem.iconf = icon
//
//        listViewList.add(listItem)
//    }
//
//    fun remove(position: Int) {
//        listViewList.removeAt(position)
//        DataChange()
//    }
//
//    fun clear() {
//        listViewList.clear()
//        DataChange()
//    }
//
//    fun DataChange() {
//        this.notifyDataSetChanged()
//    }
//}

class SelectFileAdapter(private val type:Int) : RecyclerView.Adapter<SelectFileAdapter.ViewHolder>() {

    private val selectFileAdapter: MutableList<SelectFileItem>

    init {
        selectFileAdapter = ArrayList<SelectFileItem>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        when (type) {
                DialogKey.KEY_BUNDLE_TYPE_UNIPACK -> {
                    return ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_list_select_u, parent, false))
                }

                DialogKey.KEY_BUNDEL_TYPE_FILES -> {
                    return ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_list_select_f, parent, false))
                }
            }
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_list_select_f, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = selectFileAdapter[position]
        holder.text_title.text = item.titlef
        holder.text_path.text = item.pathf
        holder.image_icon.setImageDrawable(item.iconf)
    }

    override fun getItemCount(): Int {
        return selectFileAdapter.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image_icon: ImageView = view.findViewById(R.id.FragDial_file_Image_select) as ImageView
        var text_title: TextView = view.findViewById(R.id.FragDial_file_name_select) as TextView
        var text_path: TextView = view.findViewById(R.id.FragDial_file_path_select) as TextView

    }

    fun removeItem(position: Int) {
        try {
            selectFileAdapter.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }

    fun addItem(item: SelectFileItem) {
        selectFileAdapter.add(item)
        notifyItemInserted(selectFileAdapter.size)
    }

    fun getItem(position: Int): SelectFileItem {
        return selectFileAdapter[position]
    }

    fun clearItem() {
        val count = selectFileAdapter.size
        selectFileAdapter.clear()
        notifyItemRangeRemoved(0, count)
    }
}
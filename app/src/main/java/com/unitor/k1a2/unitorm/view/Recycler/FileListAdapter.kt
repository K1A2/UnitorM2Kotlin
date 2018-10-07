package com.unitor.k1a2.unitorm.view.Recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.unitor.k1a2.unitorm.R

class FileListAdapter : RecyclerView.Adapter<FileListAdapter.ViewHolder>() {

    private val fileExplorerItems: MutableList<FileListItem>

    init {
        fileExplorerItems = ArrayList<FileListItem>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_list_unipack, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = fileExplorerItems[position]
        holder.textproducer.setText(item.fproducer)
        holder.textName.setText(item.fname)
        holder.textPath.setText(item.fpath)
        holder.textChain.setText(item.fchain)
    }

    override fun getItemCount(): Int {
        return fileExplorerItems.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textproducer: TextView
        var textName: TextView
        var textPath: TextView
        var textChain: TextView

        init {

            textproducer = view.findViewById(R.id.list_unipack_producer)
            textName = view.findViewById(R.id.list_unipack_title)
            textPath = view.findViewById(R.id.list_unipack_path)
            textChain = view.findViewById(R.id.list_unipack_chain)
        }
    }

    fun removeItem(position: Int) {
        try {
            fileExplorerItems.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }

    fun addItem(item: FileListItem) {
        fileExplorerItems.add(item)
        notifyItemInserted(fileExplorerItems.size)
}

    fun getItem(position: Int): FileListItem {
        return fileExplorerItems[position]
    }

    fun clearItem() {
        val count = fileExplorerItems.size
        fileExplorerItems.clear()
        notifyItemRangeRemoved(0, count)
    }
}
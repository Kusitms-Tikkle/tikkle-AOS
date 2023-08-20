package com.team7.tikkle.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.TodoResult

class MemoAdapter(
    private val context: Context, private val text: List<TodoResult>)
    : BaseAdapter(), AdapterView.OnItemSelectedListener {

    override fun getCount(): Int {
        return text.size
    }

    override fun getItem(position: Int): Any {
        return text[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = text[position].title

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = text[position]
        GlobalApplication.prefs.setString("memo", selectedItem.id.toString())
        Log.d("메모", selectedItem.id.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
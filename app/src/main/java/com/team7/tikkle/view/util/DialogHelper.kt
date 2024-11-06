package com.team7.tikkle

import android.app.Dialog
import android.content.Context
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.team7.tikkle.R

class DialogHelper(private val context: Context) {
    private var dialog: Dialog? = null
    
    fun showDialog(onDelete: () -> Unit, onCancel: () -> Unit) {
        dialog = Dialog(context).apply {
            setContentView(R.layout.dialog_memo_back_new)
            findViewById<ConstraintLayout>(R.id.btn_delete).setOnClickListener {
                dismiss()
                onDelete()
            }
            findViewById<ConstraintLayout>(R.id.btn_undo).setOnClickListener {
                dismiss()
                onCancel()
            }
            findViewById<ImageButton>(R.id.btn_exit).setOnClickListener {
                dismiss()
            }
            show()
        }
    }
    
    fun dismissDialog() {
        dialog?.dismiss()
    }
}


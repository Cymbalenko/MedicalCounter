package com.example.medicalcounter.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.medicalcounter.R

class MyDialogFragment : DialogFragment() {
    var positive:Boolean=false
    private lateinit var dialog:AlertDialog.Builder
    private lateinit var dialogs:AlertDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.delete_where))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes)) { dialog, id ->
                    positive=true
                }
                .setNegativeButton(getString(R.string.no),
                    DialogInterface.OnClickListener { dialog, id ->
                        positive=false
                    })
            builder
        } ?: throw IllegalStateException("Activity cannot be null")
        dialogs=dialog.create()
         return dialogs
    }
    fun getBuild(): AlertDialog.Builder {
        return dialog
    }

}
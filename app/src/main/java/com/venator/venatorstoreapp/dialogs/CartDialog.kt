package com.venator.venatorstoreapp.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.venator.venatorstoreapp.R

class CartDialog(private val message:String) : DialogFragment() {

    interface CartDialogListener {
        fun onDialogResult(result: Boolean)
    }

    lateinit var listener: CartDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Подтвердите покупку")
            .setMessage(message)
            .setIcon(R.drawable.ic_question_answer)
            .setPositiveButton("Да") { _, i ->
                listener.onDialogResult(true)
            }
            .setNegativeButton("Нет") { _, i ->
                listener.onDialogResult(false)
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as CartDialogListener
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
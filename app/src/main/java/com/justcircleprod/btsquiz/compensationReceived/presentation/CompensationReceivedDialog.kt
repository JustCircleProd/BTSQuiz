package com.justcircleprod.btsquiz.compensationReceived.presentation

import android.animation.LayoutTransition
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.DialogCompensationReceivedBinding

class CompensationReceivedDialog : DialogFragment() {

    companion object {
        private const val COMPENSATION_NAME_ARGUMENT = "COMPENSATION"

        fun newInstance(compensation: Int): CompensationReceivedDialog {
            return CompensationReceivedDialog()
                .apply {
                    arguments = Bundle().apply {
                        putInt(COMPENSATION_NAME_ARGUMENT, compensation)
                    }
                }
        }
    }

    private lateinit var binding: DialogCompensationReceivedBinding

    private var compensation: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compensation = arguments?.getInt(COMPENSATION_NAME_ARGUMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogCompensationReceivedBinding.inflate(layoutInflater)

        enableAnimations()
        binding.cancelBtn.setOnClickListener { dismiss() }
        if (compensation != null) {
            binding.compensation.text = compensation.toString()
        }
        setOnSubmitClickListeners()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setOnSubmitClickListeners() {
        binding.submitBtn.setOnClickListener {
            dismiss()
        }
    }
}
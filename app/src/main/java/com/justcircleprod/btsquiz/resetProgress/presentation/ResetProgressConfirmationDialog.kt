package com.justcircleprod.btsquiz.resetProgress.presentation

import android.animation.LayoutTransition
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.DialogResetProgressConfirmationBinding

class ResetProgressConfirmationDialog : DialogFragment() {
    private lateinit var binding: DialogResetProgressConfirmationBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogResetProgressConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        setOnBtnClickListeners()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setOnBtnClickListeners() {
        binding.cancelBtn.setOnClickListener { dismiss() }

        binding.resetProgressBtn.setOnClickListener {
            (activity as ResetProgressConfirmationDialogCallback).onResetProgressBtnClicked()

            binding.title.text = getString(R.string.progress_was_reset_dialog_title)
            binding.hint.text = getString(R.string.progress_was_reset)
            binding.resetProgressBtn.visibility = View.GONE
            binding.submitProgressWasResetBtn.visibility = View.VISIBLE
        }

        binding.submitProgressWasResetBtn.setOnClickListener {
            dismiss()
        }
    }
}
package com.justcircleprod.btsquiz.ui.settings

import android.animation.LayoutTransition
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.DialogResetPassedQuestionsConfirmationBinding

class ResetPassedQuestionsConfirmationDialog : DialogFragment() {
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private lateinit var binding: DialogResetPassedQuestionsConfirmationBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogResetPassedQuestionsConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        binding.cancelBtn.setOnClickListener { dismiss() }
        setOnSubmitClickListeners()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setOnSubmitClickListeners() {
        binding.submitBtn.setOnClickListener {
            binding.hint.visibility = View.GONE
            binding.submitBtn.visibility = View.GONE
            binding.resetProgress.visibility = View.VISIBLE

            settingsViewModel.deleteAllPassedQuestions(requireContext())

            dismiss()
        }
    }
}
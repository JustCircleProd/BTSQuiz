package com.justcircleprod.btsquiz.resetProgress.presentation

import android.animation.LayoutTransition
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.DialogResetProgressConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetProgressConfirmationDialog : DialogFragment() {

    private lateinit var binding: DialogResetProgressConfirmationBinding
    private val viewModel by viewModels<ResetProgressConfirmationViewModel>()

    // to set up the collectors again, but not to execute code in them
    private var isProgressResetCollectorStopped: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogResetProgressConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        setOnBtnClickListeners()
        setIsProgressResetCollector()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    override fun onStop() {
        super.onStop()

        isProgressResetCollectorStopped = true
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setIsProgressResetCollector() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isProgressReset.collect {
                    if (isProgressResetCollectorStopped) {
                        isProgressResetCollectorStopped = false
                        return@collect
                    }

                    if (it) {
                        binding.title.text = getString(R.string.progress_was_reset_dialog_title)
                        binding.hint.text = getString(R.string.progress_was_reset)
                        binding.resetProgressConfirmationButtons.visibility = View.GONE
                        binding.submitProgressWasResetBtn.visibility = View.VISIBLE
                    } else {
                        binding.title.text = getString(R.string.reset_progress_dialog_title)
                        binding.hint.text = getString(R.string.reset_confirmation_question)
                        binding.submitProgressWasResetBtn.visibility = View.GONE
                        binding.resetProgressConfirmationButtons.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setOnBtnClickListeners() {
        binding.titleCancelBtn.setOnClickListener { dialog?.cancel() }
        binding.cancelBtn.setOnClickListener { dialog?.cancel() }

        binding.resetProgressBtn.setOnClickListener {
            viewModel.resetProgress()
        }

        binding.submitProgressWasResetBtn.setOnClickListener {
            dismiss()
        }
    }
}
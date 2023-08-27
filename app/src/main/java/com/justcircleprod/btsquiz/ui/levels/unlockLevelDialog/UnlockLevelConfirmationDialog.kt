package com.justcircleprod.btsquiz.ui.levels.unlockLevelDialog

import android.animation.LayoutTransition
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.DialogUnlockLevelConfirmationBinding

class UnlockLevelConfirmationDialog : DialogFragment() {
    companion object {
        private const val LEVEL_ID_NAME_ARGUMENT = "LEVEL_ID"
        private const val LEVEL_PRICE_NAME_ARGUMENT = "LEVEL_PRICE"

        fun newInstance(levelId: Int, levelPrice: Int): UnlockLevelConfirmationDialog {
            return UnlockLevelConfirmationDialog().apply {
                arguments = Bundle().apply {
                    putInt(LEVEL_ID_NAME_ARGUMENT, levelId)
                    putInt(LEVEL_PRICE_NAME_ARGUMENT, levelPrice)
                }
            }
        }
    }

    private lateinit var binding: DialogUnlockLevelConfirmationBinding

    private var levelId: Int? = null
    private var levelPrice: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        levelId = arguments?.getInt(LEVEL_ID_NAME_ARGUMENT)
        levelPrice = arguments?.getInt(LEVEL_PRICE_NAME_ARGUMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogUnlockLevelConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        binding.cancelBtn.setOnClickListener { dismiss() }
        binding.cancelBtn2.setOnClickListener { dismiss() }
        setOnSubmitClickListeners()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setOnSubmitClickListeners() {
        binding.submitBtn.setOnClickListener {
            if (levelId != null && levelPrice != null) {
                (activity as UnlockLevelConfirmationDialogCallback).onUnlockButtonClicked(
                    levelId!!,
                    levelPrice!!
                )
            }

            dismiss()
        }
    }
}
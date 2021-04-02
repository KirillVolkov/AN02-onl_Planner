package io.techmeskills.an02onl_plannerapp.screen.add_new

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentAddNewBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class AddNewFragment : NavigationFragment<FragmentAddNewBinding>(R.layout.fragment_add_new) {

    override val viewBinding: FragmentAddNewBinding by viewBinding()
    private val viewModel: AddNewViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.confirm.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                setFragmentResult(ADD_NEW_RESULT, Bundle().apply {
                    putString(TEXT, viewBinding.etNote.text.toString())
                    putString(DATE, viewBinding.etDate.text.toString())
                })
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), " Please, enter your note", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

    companion object {
        const val ADD_NEW_RESULT = "ADD_NEW_RESULT"
        const val TEXT = "TEXT"
        const val DATE = "DATE"
    }
}
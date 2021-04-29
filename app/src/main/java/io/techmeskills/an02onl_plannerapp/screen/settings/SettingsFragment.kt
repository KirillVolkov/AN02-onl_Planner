package io.techmeskills.an02onl_plannerapp.screen.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentSettingsBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : NavigationFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override val viewBinding: FragmentSettingsBinding by viewBinding()

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.settingLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigateSafe(SettingsFragmentDirections.toLoginFragment())
        }

        viewBinding.settingCloud.setOnClickListener {
            showCloudDialog()
        }

        viewBinding.settingDeleteUser.setOnClickListener {
            showDeleteDialog()
        }

        viewModel.progressLiveData.observe(this.viewLifecycleOwner) { success ->
            viewBinding.progressIndicator.isVisible = false
            val cloudResult = if (success) R.string.cloud_success else R.string.cloud_failed
            Toast.makeText(requireContext(), cloudResult, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun showCloudDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.cloud_request_title)
            .setMessage(R.string.pick_action)
            .setPositiveButton(R.string.action_import) { dialog, _ ->
                viewBinding.progressIndicator.isVisible = true
                viewModel.importNotes()
                dialog.cancel()
            }.setNegativeButton(R.string.action_export) { dialog, _ ->
                viewBinding.progressIndicator.isVisible = true
                viewModel.exportNotes()
                dialog.cancel()
            }.show()
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.cloud_request_title)
            .setMessage(R.string.are_you_sure)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                viewModel.deleteUser()
                dialog.cancel()
                findNavController().navigateSafe(SettingsFragmentDirections.toLoginFragment())
            }.setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(marginTop = top)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }
}
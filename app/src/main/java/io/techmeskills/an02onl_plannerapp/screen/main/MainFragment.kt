package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.CalendarView
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    private val adapter = NotesRecyclerViewAdapter(
        onClick = ::onItemClick,
        onDelete = ::onItemDelete
    )

    val dayFormatter = SimpleDateFormat("dd EEE", Locale.getDefault())

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            when {
                dy > 0 && viewBinding.fabAdd.translationY == 0f -> {
                    viewBinding.fabAdd.animate().translationY(100f).alpha(0f).start()
                }
                dy < 0 && viewBinding.fabAdd.translationY != 0f -> {
                    viewBinding.fabAdd.animate().translationY(0f).alpha(1f).start()
                }
            }
        }
    }

    private fun onItemClick(note: Note) {
        findNavController().navigateSafe(MainFragmentDirections.toNoteDetails(note))
    }

    private fun onItemDelete(note: Note) {
        viewModel.deleteNote(note)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter

        viewModel.notesLiveData.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.userNameLiveData.observe(this.viewLifecycleOwner) {
            viewBinding.collapsingToolbarLayout.title = it.title
        }

        viewBinding.ivSettings.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toSettings())
        }

        viewBinding.fabAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNoteDetails(null))
        }

        viewBinding.recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        viewBinding.recyclerView.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(marginTop = top)
        viewBinding.fabAdd.setVerticalMargin(marginBottom = bottom)
        viewBinding.recyclerView.setPadding(
            0,
            0,
            0,
            bottom
        )
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}
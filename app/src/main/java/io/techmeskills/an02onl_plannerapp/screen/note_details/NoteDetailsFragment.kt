package io.techmeskills.an02onl_plannerapp.screen.note_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentNoteDetailsBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailsFragment :
    NavigationFragment<FragmentNoteDetailsBinding>(R.layout.fragment_note_details) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override val viewBinding: FragmentNoteDetailsBinding by viewBinding()

    private val viewModel: NoteDetailsViewModel by viewModel()

    private val args: NoteDetailsFragmentArgs by navArgs()

    private var selectedDate: Date = Date()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.confirm.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {

                args.note?.let { //если note != null, то это обновление заметки
                    viewModel.updateNote(
                        Note(
                            id = it.id, //при обновлении надо указать id, чтобы база знала что обновлять
                            title = viewBinding.etNote.text.toString(),
                            date = dateFormatter.format(selectedDate),
                            userName = it.userName,
                            alarmEnabled = viewBinding.alarmSwitch.isChecked
                        )
                    )

                } ?: kotlin.run { //если note == null, то это новая заметка, и мы ее добавляем
                    viewModel.addNewNote(
                        Note( //при добавлении id можно не указывать
                            title = viewBinding.etNote.text.toString(),
                            date = dateFormatter.format(selectedDate),
                            alarmEnabled = viewBinding.alarmSwitch.isChecked,
                            userName = ""
                        )
                    )
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), " Please, enter your note", Toast.LENGTH_LONG)
                    .show()
            }
        }

        args.note?.let { note ->
            viewBinding.alarmSwitch.isChecked = note.alarmEnabled
            viewBinding.etNote.setText(note.title)
            selectedDate = dateFormatter.parse(note.date) ?: Date()
            viewBinding.calendarView.selectDate(Calendar.getInstance().apply {
                this.time = selectedDate
            })
        }

        viewBinding.calendarView.addOnDateChangedListener { displayed, date ->
            selectedDate = date
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(marginTop = top)
        viewBinding.confirm.setVerticalMargin(marginBottom = bottom * 3 / 2)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}
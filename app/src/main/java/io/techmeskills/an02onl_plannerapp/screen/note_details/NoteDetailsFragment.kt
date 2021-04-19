package io.techmeskills.an02onl_plannerapp.screen.note_details

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
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

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override val viewBinding: FragmentNoteDetailsBinding by viewBinding()

    private val viewModel: NoteDetailsViewModel by viewModel()

    private val args: NoteDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.confirm.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {

                args.note?.let { //если note != null, то это обновление заметки
                    viewModel.updateNote(
                        Note(
                            id = it.id, //при обновлении надо указать id, чтобы база знала что обновлять
                            title = viewBinding.etNote.text.toString(),
                            date = dateFormatter.format(viewBinding.tvDate.getSelectedDate()),
                            userId = it.userId
                        )
                    )

                } ?: kotlin.run { //если note == null, то это новая заметка, и мы ее добавляем
                    viewModel.addNewNote(
                        Note( //при добавлении id можно не указывать
                            title = viewBinding.etNote.text.toString(),
                            date = dateFormatter.format(viewBinding.tvDate.getSelectedDate())
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
            viewBinding.etNote.setText(note.title)
            viewBinding.tvDate.setSelectedDate(note.date)
        }
    }

    private fun DatePicker.getSelectedDate(): Date {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.YEAR, this.year)
        calendar.set(Calendar.MONTH, this.month)
        calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun DatePicker.setSelectedDate(date: String?) {
        date?.let {
            dateFormatter.parse(it)?.let { date ->
                val calendar = Calendar.getInstance(Locale.getDefault())
                calendar.time = date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                this.updateDate(year, month, day)
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(marginTop = top)
        viewBinding.confirm.setVerticalMargin(marginBottom = bottom * 11 / 10)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}
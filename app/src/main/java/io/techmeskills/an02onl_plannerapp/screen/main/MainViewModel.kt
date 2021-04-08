package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class MainViewModel : CoroutineViewModel() {

    val notesLiveData = MutableLiveData(
        listOf(
            Note(0, "Помыть посуду"),
            Note(1, "Забрать пальто из химчистки", "23.03.2021"),
            Note(2, "Позвонить Ибрагиму"),
            Note(3, "Заказать перламутровые пуговицы"),
            Note(4, "Избить соседа за шум ночью"),
            Note(5, "Выпить на неделе с Володей", "22.03.2021"),
            Note(6, "Починить кран"),
            Note(7, "Выбить ковры перед весной"),
            Note(8, "Заклеить сапог жене"),
            Note(9, "Купить картошки"),
            Note(10, "Скачать кино в самолёт", "25.03.2021")
        )
    )

    fun addNote(note: Note) {

        launch {
            val list = notesLiveData.value!!.toMutableList()
            list.add(0, Note((list.maxBy { it.id }?.id ?: 0L) + 1L, note.title, note.date))
            notesLiveData.postValue(list)
        }

    }

    fun editNote(note: Note) {
        launch {
            val list = notesLiveData.value!!.toMutableList()
            val pos = list.indexOfFirst { it.id == note.id }
            list.removeAt(pos)
            list.add(pos, note)
            notesLiveData.postValue(list)
        }
    }

    fun deleteNote(pos: Int) {
        launch {
            val list = notesLiveData.value!!.toMutableList()
            list.removeAt(pos)
            notesLiveData.postValue(list)
        }
    }
}

class Note(
    val id: Long,
    val title: String,
    val date: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}





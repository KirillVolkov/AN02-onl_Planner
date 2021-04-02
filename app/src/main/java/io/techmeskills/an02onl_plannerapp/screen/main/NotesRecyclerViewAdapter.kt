package io.techmeskills.an02onl_plannerapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R

class NotesRecyclerViewAdapter(private val items: List<Note>) :
    RecyclerView.Adapter<NotesRecyclerViewAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
    )


    override fun onBindViewHolder(holder: NotesRecyclerViewAdapter.NoteViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = items.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

        fun bind() {
            val item = items[adapterPosition]
            tvTitle.text = item.title
            tvDate.text = item.date
        }
    }
}


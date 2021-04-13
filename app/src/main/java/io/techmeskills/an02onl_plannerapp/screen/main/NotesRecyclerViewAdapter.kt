package io.techmeskills.an02onl_plannerapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.models.Note

class NotesRecyclerViewAdapter(
    private val onClick: (Note) -> Unit,
    private val onDelete: (Note) -> Unit,
    private val onAddNew: () -> Unit
) : ListAdapter<Note, RecyclerView.ViewHolder>(NoteAdapterDiffCallback()) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ADD_NEW_VIEW_TYPE -> AddNewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item_add, parent, false),
            onAddNew
        )
        else -> NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false),
            ::onItemClick,
            ::onItemDelete
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AddNewNote -> ADD_NEW_VIEW_TYPE
            else -> NOTE_VIEW_TYPE
        }
    }

    private fun onItemClick(position: Int) {
        onClick(getItem(position))
    }

    private fun onItemDelete(position: Int) {
        onDelete(getItem(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> holder.bind(getItem(position))
            else -> (holder as AddNewViewHolder).bind()
        }
    }

    inner class NoteViewHolder(
        itemView: View,
        private val onItemClick: (Int) -> Unit,
        private val onItemDelete: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val ivDelete = itemView.findViewById<ImageView>(R.id.ivRemove)

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }

            ivDelete.setOnClickListener {
                onItemDelete(adapterPosition)
            }
        }

        fun bind(item: Note) {
            tvTitle.text = item.title
            tvDate.text = item.date
        }
    }

    inner class AddNewViewHolder(
        itemView: View,
        private val onItemClick: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick()
            }
        }

        fun bind() = Unit
    }

    companion object {
        const val ADD_NEW_VIEW_TYPE = 1382
        const val NOTE_VIEW_TYPE = 2832
    }
}

class NoteAdapterDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.date == newItem.date && oldItem.title == newItem.title
    }
}


package com.gitz.simplenotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.gitz.simplenotes.model.Note
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitz.simplenotes.R

class NoteAdapter(private val onItemClicked: (Note) -> Unit): ListAdapter<Note, NoteAdapter.NoteViewHolder>(NotesComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val noteTitleTextView: TextView = itemView.findViewById(R.id.noteTitleTextView)
        private val noteContentTextView: TextView = itemView.findViewById(R.id.noteContentTextView)

        fun bind(note: Note) {
            noteTitleTextView.text = note.title
            noteContentTextView.text = note.content
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote)
        holder.itemView.setOnClickListener {
            onItemClicked(currentNote)
        }
    }

    class NotesComparator: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

}
package com.thinlineit.ctrlf.notes

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemNoteBinding
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class NotesAdapter(
    private val noteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindingRecyclerViewAdapter<List<Note>> {
    private var noteList: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        NoteViewHolder.from(parent)

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NoteViewHolder).bind(
            noteList[position],
            noteClickListener,
            position
        )
    }

    class NoteViewHolder(private val dataBinding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(
            note: Note,
            clickListener: (Int) -> Unit,
            position: Int
        ) {
            val noteDesignArray = dataBinding.root.resources.obtainTypedArray(R.array.notes)
            val noteResourceId = noteDesignArray.getResourceId(position % NOTE_DESIGN_NUM, 0)
            dataBinding.apply {
                noteItemImage.setImageResource(noteResourceId)
                if (!note.isApproved) {
                    noteItemImage.setColorFilter(R.color.light_gray, PorterDuff.Mode.DARKEN)
                }
                this.note = note
                root.setOnClickListener {
                    clickListener(note.id)
                }
            }
            noteDesignArray.recycle()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataBinding = ListItemNoteBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return NoteViewHolder(dataBinding)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Note>) {
        noteList = data
        notifyDataSetChanged()
    }

    companion object {
        private const val NOTE_DESIGN_NUM = 15
    }
}

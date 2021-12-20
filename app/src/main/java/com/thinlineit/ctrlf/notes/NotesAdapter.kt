package com.thinlineit.ctrlf.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemNoteBinding
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class NotesAdapter(
    private val addNoteClickListener: () -> Unit,
    private val noteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindingRecyclerViewAdapter<List<Note>> {
    private var noteList: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ADD_NOTE) {
            AddViewHolder.from(parent)
        } else {
            NoteViewHolder.from(parent)
        }
    }

    override fun getItemCount(): Int = (noteList.size + 1)

    override fun getItemViewType(position: Int): Int = if (position == 0) ADD_NOTE else NOTE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ADD_NOTE) {
            (holder as AddViewHolder).bind(
                addNoteClickListener
            )
        } else {
            (holder as NoteViewHolder).bind(
                noteList[position - 1],
                noteClickListener,
                position
            )
        }
    }

    class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(addNoteClickListener: () -> Unit) {
            itemView.setOnClickListener {
                addNoteClickListener()
            }
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val itemView =
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_add_note, parent, false
                    )
                return AddViewHolder(itemView)
            }
        }
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

        private const val ADD_NOTE = 0
        private const val NOTE = 1
    }
}

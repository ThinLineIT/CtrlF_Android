package com.thinlineit.ctrlf.main

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemMainNoteBinding
import com.thinlineit.ctrlf.entity.Note
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class MainNoteAdapter(private val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindingRecyclerViewAdapter<List<Note>> {
    var noteList: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MainNoteViewHolder.from(parent)

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val noteDao = noteList[position]
        (holder as MainNoteViewHolder).bind(noteDao, clickListener, position)
    }

    class MainNoteViewHolder(private val dataBinding: ListItemMainNoteBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(
            note: Note,
            clickListener: (Int) -> Unit,
            position: Int
        ) {
            val noteDesignArray = dataBinding.root.resources.obtainTypedArray(R.array.notes)
            val noteResourceId =
                noteDesignArray.getResourceId(position % NOTEDESIGN_NUM, 0)
            dataBinding.apply {
                noteItemImage.setImageResource(noteResourceId)
                if (!note.isApproved) {
                    noteItemImage.setColorFilter(R.color.light_gray, PorterDuff.Mode.DARKEN)
                } else {
                    noteItemImage.colorFilter = null
                }
                this.note = note
                root.setOnClickListener {
                    clickListener(note.id)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataBinding = ListItemMainNoteBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return MainNoteViewHolder(dataBinding)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Note>) {
        noteList = data
        notifyDataSetChanged()
    }

    companion object {
        const val NOTEDESIGN_NUM = 15
    }
}

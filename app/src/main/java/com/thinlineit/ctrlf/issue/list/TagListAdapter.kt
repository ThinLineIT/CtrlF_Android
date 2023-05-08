package com.thinlineit.ctrlf.issue.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.databinding.ListItemTagBinding
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

// Issue List 에서 Tag 기능 구현 시 사용되는 리사이클러뷰 어댑터입니다.
class TagListAdapter(private val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<TagListAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<String>> {
    private var tagList = emptyList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun getItemCount(): Int = tagList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tagString = tagList[position]
        holder.bind(
            tagString,
            clickListener,
            position
        )
    }

    class ViewHolder(private val dataBinding: ListItemTagBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(tagString: String, clickListener: (Int) -> Unit, position: Int) {
            dataBinding.apply {
                tag = tagString
                root.setOnClickListener {
                    clickListener(position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataBinding = ListItemTagBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(dataBinding)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<String>) {
        tagList = data
        notifyDataSetChanged()
    }
}

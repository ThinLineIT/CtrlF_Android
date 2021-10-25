package com.thinlineit.ctrlf.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemMainIssueBinding
import com.thinlineit.ctrlf.issue.Issue
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class MainIssueAdapter(private val clickListener: (Issue) -> Unit) :
    RecyclerView.Adapter<MainIssueAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<Issue>> {
    private var issueList = emptyList<Issue>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun getItemCount(): Int = issueList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val issueDao = issueList[position]
        holder.bind(issueDao, clickListener, position)
    }

    class ViewHolder(private val dataBinding: ListItemMainIssueBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(issue: Issue, clickListener: (Issue) -> Unit, position: Int) {
            val resourceId: Int = when (position % 3) {
                1 -> R.drawable.icon_issue_prelude
                2 -> R.drawable.icon_issue_bluechalk
                else -> R.drawable.icon_issue_lightgreen
            }
            dataBinding.apply {
                mainIssueItemImage.setImageResource(resourceId)
                this.issue = issue
                root.setOnClickListener {
                    clickListener(issue)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataBinding = ListItemMainIssueBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(dataBinding)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Issue>) {
        issueList = data
        notifyDataSetChanged()
    }
}

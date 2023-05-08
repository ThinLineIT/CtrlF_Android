package com.thinlineit.ctrlf.page.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemTopicTitleBinding
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class TopicTitleListAdapter(private val clickListener: (Topic) -> Unit) :
    RecyclerView.Adapter<TopicTitleListAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<Topic>> {
    var topicList = emptyList<Topic>()

    private lateinit var swipeBtnClickListener: SwipeButtonClickListener

    fun setSwipeBtnClickListener(swipeBtnClickListener: SwipeButtonClickListener) {
        this.swipeBtnClickListener = swipeBtnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun getItemCount(): Int = topicList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topicDao = topicList[position]
        holder.bind(topicDao, clickListener, swipeBtnClickListener)
    }

    class ViewHolder(private val dataBinding: ListItemTopicTitleBinding) :
        RecyclerView.ViewHolder(dataBinding.root), SwipeInterface {
        fun bind(
            topic: Topic,
            clickListener: (Topic) -> Unit,
            mSwipeBtnClickListener: SwipeButtonClickListener
        ) {
            val resourceId = when (topic.isApproved) {
                true -> R.color.white
                else -> R.color.gray
            }
            dataBinding.apply {
                this.topic = topic
                swipeTopicListView.setBackgroundResource(resourceId)
                root.setOnClickListener {
                    clickListener(topic)
                }
                topicTitleUpdateBtn.setOnClickListener {
                    mSwipeBtnClickListener.onUpdate(topic.id)
                }
                topicTitleDeleteButton.setOnClickListener {
                    mSwipeBtnClickListener.onDelete(topic.id)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataBinding = DataBindingUtil.inflate<ListItemTopicTitleBinding>(
                    layoutInflater,
                    R.layout.list_item_topic_title,
                    parent,
                    false
                )
                return ViewHolder(dataBinding)
            }
        }

        override fun getSwipeWidth(): Int = dataBinding.topicTitleDeleteButton.width
        override fun getSwipeLayout(): LinearLayout = dataBinding.swipeTopicListView
        override fun getUpdateButton(): TextView = dataBinding.topicTitleUpdateBtn
        override fun getDeleteButton(): TextView = dataBinding.topicTitleDeleteButton
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Topic>) {
        topicList = data
        notifyDataSetChanged()
    }
}

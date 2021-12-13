package com.thinlineit.ctrlf.page.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemTopicTitleBinding
import com.thinlineit.ctrlf.entity.Topic
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class TopicTitleListAdapter(private val clickListener: (Topic) -> Unit) :
    RecyclerView.Adapter<TopicTitleListAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<Topic>>,
    ListButtonInterface {
    var topicList = emptyList<Topic>()

    interface SwipeBtnClickListener {
        fun onUpdate(topicId: Int)
    }

    private lateinit var topicTitleListSwipeBtnClickListener: SwipeBtnClickListener

    fun setSwipeBtnClickListener(swipeBtnClickListener: SwipeBtnClickListener) {
        topicTitleListSwipeBtnClickListener = swipeBtnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun getItemCount(): Int = topicList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topicDao = topicList[position]
        holder.bind(topicDao, clickListener, topicTitleListSwipeBtnClickListener)
    }

    // TODO: 준비중입니다 토스트 메세지 -> 다이얼로그
    override fun onDelete(context: Context) {
        TopicFragmentDialog(context).topicDialog(context)
    }

    // TODO: 준비중입니다 토스트 메세지 -> 다이얼로그
    override fun onModify(context: Context) {
        TopicFragmentDialog(context).topicDialog(context)
    }

    class ViewHolder(private val dataBinding: ListItemTopicTitleBinding) :
        RecyclerView.ViewHolder(dataBinding.root), SwipeInterface {
        fun bind(
            topic: Topic,
            clickListener: (Topic) -> Unit,
            mSwipeBtnClickListener: SwipeBtnClickListener
        ) {
            dataBinding.topic = topic
            dataBinding.root.setOnClickListener {
                clickListener(topic)
            }
            dataBinding.topicTitleUpdateBtn.setOnClickListener {
                mSwipeBtnClickListener.onUpdate(topic.id)
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Topic>) {
        topicList = data
        notifyDataSetChanged()
    }
}

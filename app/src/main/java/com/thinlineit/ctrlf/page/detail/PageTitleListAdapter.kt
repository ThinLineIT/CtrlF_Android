package com.thinlineit.ctrlf.page.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ListItemPageTitleBinding
import com.thinlineit.ctrlf.entity.Page
import com.thinlineit.ctrlf.util.BindingRecyclerViewAdapter

class PageTitleListAdapter(private val clickListener: (Page) -> Unit) :
    RecyclerView.Adapter<PageTitleListAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<Page>> {
    private lateinit var swipeButtonClickListener: SwipeButtonClickListener
    private var pageList = emptyList<Page>()

    fun setSwipeButtonClickListener(swipeButtonClickListener: SwipeButtonClickListener) {
        this.swipeButtonClickListener = swipeButtonClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun getItemCount(): Int = pageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pageDao = pageList[position]
        holder.bind(pageDao, clickListener, swipeButtonClickListener)
    }

    class ViewHolder(private val dataBinding: ListItemPageTitleBinding) :
        RecyclerView.ViewHolder(dataBinding.root), SwipeInterface {
        fun bind(
            page: Page,
            clickListener: (Page) -> Unit,
            mSwipeButtonClickListener: SwipeButtonClickListener
        ) {
            val resourceId = when (page.isApproved) {
                true -> R.color.white
                else -> R.color.gray
            }
            dataBinding.apply {
                swipePageListView.setBackgroundResource(resourceId)
                this.page = page
                root.setOnClickListener {
                    clickListener(page)
                }
                pageTitleDeleteButton.setOnClickListener {
                    mSwipeButtonClickListener.onDelete(page.id)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataBinding = DataBindingUtil.inflate<ListItemPageTitleBinding>(
                    layoutInflater,
                    R.layout.list_item_page_title,
                    parent,
                    false
                )
                return ViewHolder(dataBinding)
            }
        }

        override fun getSwipeWidth(): Int = dataBinding.pageTitleDeleteButton.width
        override fun getSwipeLayout(): LinearLayout = dataBinding.swipePageListView
        override fun getUpdateButton(): TextView = dataBinding.pageTitleUpdateButton
        override fun getDeleteButton(): TextView = dataBinding.pageTitleDeleteButton
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<Page>) {
        pageList = data
        notifyDataSetChanged()
    }
}

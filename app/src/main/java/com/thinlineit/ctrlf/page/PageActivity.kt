package com.thinlineit.ctrlf.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityPageBinding
import com.thinlineit.ctrlf.registration.signout.LogoutActivity
import com.thinlineit.ctrlf.util.LoadingDialog
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.activity_page.*

class PageActivity : AppCompatActivity() {
    private val binding: ActivityPageBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_page
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getIntExtra(NOTE_ID, 0)
        val viewModelFactory = PageViewModelFactory(noteId)
        val pageViewModel = ViewModelProvider(this, viewModelFactory).get(PageViewModel::class.java)
        binding.apply {
            this.pageViewModel = pageViewModel
            lifecycleOwner = this@PageActivity
        }
        pageViewModel.openSlidingPane.observe(this) {
            if (it == true && slidingPaneLayout.isSlideable) {
                slidingPaneLayout.open()
                pageViewModel.closeSliding()
            }
        }

        val loadingDialog = LoadingDialog(this)

        pageViewModel.isLoading.observe(
            this,
            Observer {
                if (it) loadingDialog.show()
                else loadingDialog.dismiss()
            }
        )

        setSupportActionBar(pageActivityToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // TODO: Do not use deprecated methods.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        dpWidth = if (outMetrics.widthPixels > 1080) (outMetrics.widthPixels / density) / 6
        else (outMetrics.widthPixels / density) / 3

        // floating button for 0.2.1 version
        binding.fabButton.setOnClickListener {
            val intent = Intent(this, PageEditorActivity::class.java)
            intent.putExtra(PAGE_INFO, pageViewModel.pageInfo.value)
            intent.putExtra(TOPIC_INFO, pageViewModel.topicTitleTop.value)

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.userCircleBtn -> {
            val logout = Intent(this, LogoutActivity::class.java)
            startActivity(logout)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (slidingPaneLayout.isOpen && slidingPaneLayout.isSlideable) {
            slidingPaneLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val NOTE_ID = "noteId"
        var dpWidth by Delegates.notNull<Float>()
        const val PAGE_INFO = "pageInfo"
        const val TOPIC_INFO = "topicTitle"

        fun start(context: Context) {
            val intent = Intent(context, PageActivity::class.java)
            context.startActivity(intent)
        }
    }
}

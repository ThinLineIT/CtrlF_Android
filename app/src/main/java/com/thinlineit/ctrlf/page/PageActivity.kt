package com.thinlineit.ctrlf.page

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.databinding.ActivityPageBinding
import com.thinlineit.ctrlf.registration.signout.LogoutActivity
import com.thinlineit.ctrlf.util.LoadingDialog
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.activity_page.bookMarkButton
import kotlinx.android.synthetic.main.activity_page.editButton
import kotlinx.android.synthetic.main.activity_page.fabButton
import kotlinx.android.synthetic.main.activity_page.fabChildButtonList
import kotlinx.android.synthetic.main.activity_page.pageActivityToolBar
import kotlinx.android.synthetic.main.activity_page.relatedIssueButton
import kotlinx.android.synthetic.main.activity_page.shareButton
import kotlinx.android.synthetic.main.activity_page.slidingPaneLayout

class PageActivity : AppCompatActivity() {
    val pageViewModel by lazy {
        val noteId = intent.getIntExtra(NOTE_ID, 0)
        ViewModelProvider(this, PageViewModelFactory(noteId)).get(PageViewModel::class.java)
    }
    private val floatingMenuUIController by lazy {
        FloatingMenuUIController(this, pageViewModel.isFabOpen, fabButton, fabChildButtonList)
    }
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

        initObserver()
        initButton()
        setSupportActionBar(pageActivityToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // TODO: Do not use deprecated methods.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        dpWidth = if (outMetrics.widthPixels > 1080) (outMetrics.widthPixels / density) / 6
        else (outMetrics.widthPixels / density) / 3
    }

    private fun initButton() {
        fabButton.setOnClickListener {
            pageViewModel.toggleFab()
        }

        shareButton.setOnClickListener {
            // TODO: copy the uri on clipboard
            Toast.makeText(this, "해당 서비스는 준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        bookMarkButton.setOnClickListener {
            // TODO: save this page as bookmark
            Toast.makeText(this, "해당 서비스는 준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        relatedIssueButton.setOnClickListener {
            // TODO: go to issue detail
            Toast.makeText(this, "해당 서비스는 준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        editButton.setOnClickListener {
            // TODO: go to edit mode
            Toast.makeText(this, "해당 서비스는 준비중입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObserver() {
        pageViewModel.isRightPaneOpen.observe(this) {
            if (it == true && slidingPaneLayout.isSlideable) {
                slidingPaneLayout.open()
            }
        }

        val loadingDialog = LoadingDialog(this)

        pageViewModel.isLoading.observe(this) {
            if (it) loadingDialog.show()
            else loadingDialog.dismiss()
        }

        pageViewModel.isFabOpen.observe(this) {
            floatingMenuUIController.toggleFabButtons(it)
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
        const val PAGEINFO = "pageInfo"
    }
}

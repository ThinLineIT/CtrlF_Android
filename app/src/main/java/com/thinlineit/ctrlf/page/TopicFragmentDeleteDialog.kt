package com.thinlineit.ctrlf.page

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.thinlineit.ctrlf.R
import kotlinx.android.synthetic.main.fragment_topic_title_dialog.*

class TopicFragmentDeleteDialog(context: Context) {
    // TODO: 추후에 디자인 나오면 해당 레이아웃으로 적용
    private val dialog = Dialog(context)
    fun topicDialog() {
        dialog.setContentView(R.layout.fragment_topic_title_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.topicCorrectDeleteDialog.setText("토픽 삭제 요청")
        dialog.editTextDialog.setHint("삭제 요청 사유")
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
}

package com.thinlineit.ctrlf.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.thinlineit.ctrlf.R
import kotlinx.android.synthetic.main.progress_loading.*

class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_loading)
        Glide
            .with(context)
            .asGif()
            .load(R.drawable.gif_loading_logo)
            .into(progressView)

        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

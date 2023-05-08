package com.thinlineit.ctrlf.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.thinlineit.ctrlf.R

abstract class RegistrationBaseFragment<T : ViewDataBinding>(
    @LayoutRes private val resId: Int
) : Fragment(resId) {
    lateinit var anim: Animation
    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, resId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        anim = AnimationUtils.loadAnimation(context, R.anim.shake_animation)
        return binding.root
    }
}

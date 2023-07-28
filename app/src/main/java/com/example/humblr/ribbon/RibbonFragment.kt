package com.example.humblr.ribbon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.humblr.R
import com.example.humblr.presentation.MainActivity


class RibbonFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ribbon, container, false)
    }

}
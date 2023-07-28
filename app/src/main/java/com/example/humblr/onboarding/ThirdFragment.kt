package com.example.humblr.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.humblr.R
import com.example.humblr.databinding.FragmentThirdBinding
import com.example.humblr.enty.MySharedPreferences
import com.example.humblr.presentation.MainActivity

class ThirdFragment : Fragment() {


    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ready = binding.ready
        val back = binding.back
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        ready.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToSignInFragment())
            MySharedPreferences().onBoardingIsFinished(requireContext())
        }

        back.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
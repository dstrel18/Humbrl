package com.example.humblr.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.humblr.R
import com.example.humblr.databinding.FragmentSecondBinding
import com.example.humblr.enty.MySharedPreferences
import com.example.humblr.presentation.MainActivity
import com.example.humblr.utils.FINISHED
import com.example.humblr.utils.ONBOARDING

class SecondFragment : Fragment() {


    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val next = binding.next
        val back = binding.back
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        val ready = binding.ready
        ready.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToSignInFragment())
            MySharedPreferences().onBoardingIsFinished(requireContext())
        }
        next.setOnClickListener {
            viewPager?.currentItem = 2
        }
        back.setOnClickListener {
            viewPager?.currentItem = 0
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun onBoardingIsFinished() {
        val sharedPreferences =
            requireActivity().getSharedPreferences(ONBOARDING, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(FINISHED, true)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
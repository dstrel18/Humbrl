package com.example.humblr.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.humblr.adapter.ViewPagerAdapter
import com.example.humblr.databinding.FragmentOnboardingBinding
import com.example.humblr.presentation.MainActivity

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = arrayListOf(
            FirstFragment(), SecondFragment(), ThirdFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList, requireActivity().supportFragmentManager, lifecycle
        )
        val dotsIndicator = binding.dotsIndicator
        val viewPager = binding.viewPager
        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
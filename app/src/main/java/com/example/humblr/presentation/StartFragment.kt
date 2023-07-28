package com.example.humblr.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.humblr.R
import com.example.humblr.databinding.FragmentSignInBinding
import com.example.humblr.databinding.FragmentStartBinding
import com.example.humblr.enty.MySharedPreferences
import com.example.humblr.signIn.SignInFragmentDirections
import com.example.humblr.signIn.TokenStorage
import com.example.humblr.utils.TAG_T
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val sharedPreferences = MySharedPreferences()
    private val viewModel: StartViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(false)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({

            val toOnboardingFragment =
                StartFragmentDirections.actionStartFragmentToOnBoardingFragment()
            val toAuthFragment = StartFragmentDirections.actionStartFragmentToSignInFragment()
            val toRibbonFragment = StartFragmentDirections.actionStartFragmentToRibbonFragment()

            if (!sharedPreferences.onBoardingIsFinishedBoolean(requireContext())) {
                findNavController()
                    .navigate(toOnboardingFragment)
            } else findNavController().navigate(toAuthFragment)
//           viewModel.setNavigation(this)

        }, 1000)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
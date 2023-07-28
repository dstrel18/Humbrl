package com.example.humblr.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.humblr.enty.MySharedPreferences
import com.example.humblr.signIn.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val sharedPreferences: MySharedPreferences) :
    ViewModel() {

    fun setNavigation(fragment: Fragment) {
        val toOnboardingFragment =
            StartFragmentDirections.actionStartFragmentToOnBoardingFragment()
        val toAuthFragment = StartFragmentDirections.actionStartFragmentToSignInFragment()
        val toRibbonFragment = StartFragmentDirections.actionStartFragmentToRibbonFragment()

        if (!sharedPreferences.onBoardingIsFinishedBoolean(fragment.requireContext())) fragment.findNavController()
            .navigate(toOnboardingFragment)
        else {
            if (sharedPreferences.getToken(fragment.requireContext()) != null) {
                fragment.findNavController()
                    .navigate(toRibbonFragment)
                TokenStorage.accessToken = sharedPreferences.getToken(fragment.requireContext())
            } else fragment.findNavController().navigate(toAuthFragment)
        }
    }

}
package com.example.humblr.signIn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.humblr.databinding.FragmentSignInBinding
import com.example.humblr.enty.MySharedPreferences
import com.example.humblr.presentation.MainActivity
import com.example.humblr.utils.CALL
import com.example.humblr.utils.CODE
import com.example.humblr.utils.TAG_T
import com.example.humblr.utils.launchAndCollectIn

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private val sharedPreferences = MySharedPreferences()
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(false)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireActivity().intent != null) handleDeepLink(requireActivity().intent)

        binding.bottomSignIn.setOnClickListener {
            openBrowser()
            binding.progressBar.isVisible = true
        }

        viewModel.token.observe(viewLifecycleOwner) {
            sharedPreferences.saveToken(requireContext(), it)
        }
    }

    private fun handleDeepLink(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        val deepLinkUrl = intent.data ?: return
        if (deepLinkUrl.queryParameterNames.contains(CODE)) {
            val authCode = deepLinkUrl.getQueryParameter(CODE) ?: return
            binding.progressBar.isVisible = true
            viewModel.getToken(authCode)

            viewModel.authSuccessFlow.launchAndCollectIn(viewLifecycleOwner) {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToRibbonFragment())
                (activity as MainActivity).showBottomMenu(true)
            }
        }
    }

    private fun openBrowser() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CALL))
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
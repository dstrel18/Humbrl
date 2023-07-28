package com.example.humblr.profile

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.humblr.R
import com.example.humblr.data.profile.ProfileDto
import com.example.humblr.databinding.FragmentUserBinding
import com.example.humblr.utils.TAG_T
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSubscribedAll()
        setLogoutButton()

        viewModel.getProfile()
        viewModel.profile.observe(viewLifecycleOwner) {
            showUserInfo(it)
        }

        binding.bottomFriends.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToFriendsFragment())
        }

        binding.bottomClear.setOnClickListener {

            viewModel.listSaved.observe(viewLifecycleOwner) { it ->
                it.data.children.forEach {
                    viewModel.unSave(it.data.name)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showUserInfo(userInfo: ProfileDto) {
        binding.nameProfile.text = userInfo.more_infos?.title
        val text = "@${userInfo.name}"
        binding.userName.text = text
        binding.subreddits.text =
            "${getString(R.string.Subreddits)} ${userInfo.more_infos?.subscribers}"
        Glide.with(this).load(userInfo.snoovatar_img).circleCrop().into(binding.imageProfile)
    }


    private fun setLogoutButton() {
        binding.exit.setOnClickListener {
            setAlertDialog(
                getString(R.string.logout_text), getString(
                    R.string.logout_message
                ), viewModel.logout(this)
            )
        }
    }

    private fun setAlertDialog(title: String, message: String, viewModel: Unit) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.yes_text) { _, _ ->
                viewModel
            }
            .setNegativeButton(R.string.no_text) { _, _ ->
                dialog.create().hide()
            }
        dialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

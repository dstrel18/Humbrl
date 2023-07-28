package com.example.humblr.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.humblr.adapter.FriendsListAdapter
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.databinding.FragmentLisfFriendsBinding
import com.example.humblr.utils.TAG_T
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : Fragment() {


    private var _binding: FragmentLisfFriendsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    private val adapter = FriendsListAdapter { item -> onClick(item) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLisfFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageList.adapter = adapter
        binding.imageList.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        viewModel.getFriends()





        viewModel.friends.observe(viewLifecycleOwner) {

            adapter.setData(it)

        }
    }


    private fun onClick(clicked: ClickedUserProfileDto) {
        val name = clicked.data.name
        findNavController().navigate(
            FriendsFragmentDirections.actionFriendsFragmentToUserProfilFragment(
                name.toString()
            )
        )
        viewModel.list.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
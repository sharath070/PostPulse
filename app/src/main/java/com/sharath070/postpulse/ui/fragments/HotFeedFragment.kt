package com.sharath070.postpulse.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharath070.postpulse.R
import com.sharath070.postpulse.databinding.FragmentHotFeedBinding
import com.sharath070.postpulse.model.galleryTags.PostAndPosition
import com.sharath070.postpulse.repository.Resource
import com.sharath070.postpulse.ui.activities.MainActivity
import com.sharath070.postpulse.ui.activities.MediaViewActivity
import com.sharath070.postpulse.ui.adapters.PostsItemAdapter
import com.sharath070.postpulse.ui.viewModel.PostsViewModel


class HotFeedFragment : Fragment() {

    private lateinit var binding: FragmentHotFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hot_feed, container, false)
        return binding.root
    }

    private lateinit var viewModel: PostsViewModel

    private lateinit var postsItemAdapter: PostsItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()


        postsItemAdapter.setOnPostClickListener { position, post ->
            val post = PostAndPosition(position, post)

            val intent = Intent(requireContext(), MediaViewActivity::class.java)
            intent.putExtra("postData", post)

            startActivity(intent)
        }

        postsItemAdapter.setOnItemClickListener {
            val args = WebViewFragmentArgs(it)
            findNavController().navigate(R.id.action_hotFeedFragment_to_webViewFragment, args.toBundle())
        }



        viewModel.hotPosts.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    // hideProgressBar()
                    response.data?.let {
                        postsItemAdapter.submitList(it.data)
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error in Response", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    // showProgressBar()
                }
            }
        }


    }

    private fun setupRecyclerView() {
        postsItemAdapter = PostsItemAdapter(requireContext())
        binding.rvPosts.apply {
            adapter = postsItemAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }




}
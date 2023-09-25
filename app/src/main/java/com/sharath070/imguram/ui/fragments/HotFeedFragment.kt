package com.sharath070.imguram.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.FragmentHotFeedBinding
import com.sharath070.imguram.repository.Resource
import com.sharath070.imguram.ui.activities.MainActivity
import com.sharath070.imguram.ui.adapters.PostsItemAdapter
import com.sharath070.imguram.ui.viewModel.PostsViewModel


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

        postsItemAdapter.setOnPostClickListener {
            Log.i("@@@@", it.link.toString())

//            val args = WebViewFragmentArgs(it)
//            findNavController().navigate(R.id.action_hotFeedFragment_to_webViewFragment, args.toBundle())
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
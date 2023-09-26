package com.sharath070.imguram.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.FragmentTopFeedBinding
import com.sharath070.imguram.model.galleryTags.Data
import com.sharath070.imguram.model.galleryTags.PostAndPosition
import com.sharath070.imguram.repository.Resource
import com.sharath070.imguram.ui.activities.MainActivity
import com.sharath070.imguram.ui.activities.MediaViewActivity
import com.sharath070.imguram.ui.adapters.PostsItemAdapter
import com.sharath070.imguram.ui.viewModel.PostsViewModel


class TopFeedFragment : Fragment() {


    private lateinit var binding: FragmentTopFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_feed, container, false)
        return binding.root
    }


    private lateinit var viewModel: PostsViewModel
    private lateinit var postsItemAdapter: PostsItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        /*postsItemAdapter.setOnItemClickListener {
            Log.i("@@@@", it.images?.firstOrNull()?.description.toString())

            val args = WebViewFragmentArgs(it)
            findNavController().navigate(R.id.action_topFeedFragment_to_webViewFragment, args.toBundle())
        }*/

        viewModel.topPosts.observe(viewLifecycleOwner) { response ->

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
        binding.rvTopPosts.apply {
            adapter = postsItemAdapter
            layoutManager = LinearLayoutManager(activity)
        }

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


    }

    override fun onResume() {
        super.onResume()

        viewModel.networkManager.observe(viewLifecycleOwner){
            if (it == true){
                viewModel.getTopPosts()
            }
        }
    }


}
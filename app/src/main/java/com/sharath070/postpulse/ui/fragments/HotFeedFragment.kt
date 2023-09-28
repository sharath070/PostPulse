package com.sharath070.postpulse.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sharath070.postpulse.R
import com.sharath070.postpulse.databinding.FragmentHotFeedBinding
import com.sharath070.postpulse.model.galleryTags.PostAndPosition
import com.sharath070.postpulse.repository.Resource
import com.sharath070.postpulse.ui.activities.MainActivity
import com.sharath070.postpulse.ui.activities.MediaViewActivity
import com.sharath070.postpulse.ui.adapters.PostsItemAdapter
import com.sharath070.postpulse.ui.viewModel.BottomSheetDialogViewModel
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
    private lateinit var bottomSheetViewModel: BottomSheetDialogViewModel

    private lateinit var postsItemAdapter: PostsItemAdapter

    private var refreshFromPagination = false
    private var refreshFromSwipe = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        bottomSheetViewModel = (activity as MainActivity).bottomSheetDialogViewModel

        binding.toolbar.inflateMenu(R.menu.toolbar_menu)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> {
                    showBottomSheet()
                    true
                }

                else -> false
            }
        }

        setupRecyclerView()

        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.hotFeedFragment) {
                binding.rvPosts.scrollToPosition(250)
            }
        }
        postsItemAdapter.setOnPostClickListener { position, post ->
            val post = PostAndPosition(position, post)

            val intent = Intent(requireContext(), MediaViewActivity::class.java)
            intent.putExtra("postData", post)

            startActivity(intent)
        }

        postsItemAdapter.setOnItemClickListener {
            val args = WebViewFragmentArgs(it)
            findNavController().navigate(
                R.id.action_hotFeedFragment_to_webViewFragment,
                args.toBundle()
            )
        }



        showFeed()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getHotPosts()
            findNavController().popBackStack()
            findNavController().navigate(R.id.hotFeedFragment)
            refreshFromSwipe = true
            showFeed()
            binding.swipeToRefresh.isRefreshing = false
        }


    }

    private fun showFeed() {
        viewModel.hotPosts.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        postsItemAdapter.submitList(it.data)
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error in Response", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        if (refreshFromPagination){
            binding.progressBar4.visibility = View.VISIBLE
            refreshFromPagination = false
        }
        else if (refreshFromSwipe){
            refreshFromSwipe = false
        }
        else{
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar2.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.progressBar2.visibility = View.GONE
        binding.progressBar4.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        postsItemAdapter = PostsItemAdapter(requireContext(), viewModel)
        binding.rvPosts.apply {
            adapter = postsItemAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HotFeedFragment.scrollListener)
        }

    }

    private var isLoading = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1) && !isLoading){
                isLoading = true
                viewModel.getHotPosts()
                isLoading = false
                recyclerView.requestLayout()
                refreshFromPagination = true
            }
        }
    }


    private fun showBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout)

        val showViral = bottomSheetDialog.findViewById<LinearLayout>(R.id.llViral)
        val showRaising = bottomSheetDialog.findViewById<LinearLayout>(R.id.llRaising)

        val tvViral = bottomSheetDialog.findViewById<TextView>(R.id.tvViral)
        val tvRaising = bottomSheetDialog.findViewById<TextView>(R.id.tvRaising)

        val viralTick = bottomSheetDialog.findViewById<ImageView>(R.id.ivViralTick)
        val raisingTick = bottomSheetDialog.findViewById<ImageView>(R.id.ivRaisingTick)

        bottomSheetViewModel.selectedFilterForHotPosts.observe(viewLifecycleOwner) { filter ->
            viewModel.filterHotPosts = filter
            when (filter) {
                "viral" -> {
                    viralTick?.visibility = View.VISIBLE
                    raisingTick?.visibility = View.INVISIBLE

                    tvViral?.setTextColor(Color.WHITE)
                    tvRaising?.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.textDisabled)
                    )

                    val viralDrawable = tvViral?.compoundDrawables?.get(0)!!
                    androidx.core.graphics.drawable.DrawableCompat.setTint(
                        viralDrawable,
                        Color.WHITE
                    )

                    val raisingDrawable = tvRaising?.compoundDrawables?.get(0)!!
                    androidx.core.graphics.drawable.DrawableCompat.setTint(
                        raisingDrawable,
                        ContextCompat.getColor(requireContext(), R.color.textDisabled)
                    )

                }

                "Raising" -> {
                    viralTick?.visibility = View.INVISIBLE
                    raisingTick?.visibility = View.VISIBLE

                    tvRaising?.setTextColor(Color.WHITE)
                    tvViral?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.textDisabled
                        )
                    )

                    val raisingDrawable = tvRaising?.compoundDrawables?.get(0)!!
                    androidx.core.graphics.drawable.DrawableCompat.setTint(
                        raisingDrawable,
                        Color.WHITE
                    )

                    val viralDrawable = tvViral?.compoundDrawables?.get(0)!!
                    androidx.core.graphics.drawable.DrawableCompat.setTint(
                        viralDrawable,
                        ContextCompat.getColor(requireContext(), R.color.textDisabled)
                    )
                }
            }
        }

        showViral?.setOnClickListener {

            bottomSheetViewModel.hotPostsViralSelected()
            viewModel.getHotPosts()
            findNavController().popBackStack()
            findNavController().navigate(R.id.hotFeedFragment)

            bottomSheetDialog.dismiss()

        }



        showRaising?.setOnClickListener {

            bottomSheetViewModel.hotPostsRaisingSelected()
            viewModel.getHotPosts()
            findNavController().popBackStack()
            findNavController().navigate(R.id.hotFeedFragment)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}
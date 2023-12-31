package com.sharath070.postpulse.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sharath070.postpulse.R
import com.sharath070.postpulse.databinding.ActivityMainBinding
import com.sharath070.postpulse.db.PostDatabase
import com.sharath070.postpulse.repository.PostsRepository
import com.sharath070.postpulse.ui.viewModel.BottomSheetDialogViewModel
import com.sharath070.postpulse.ui.viewModel.PostsViewModel
import com.sharath070.postpulse.ui.viewModel.PostsViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: PostsViewModel
    lateinit var bottomSheetDialogViewModel: BottomSheetDialogViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val postsRepository = PostsRepository(PostDatabase.getDatabase(this))
        val viewModelProviderFactory = PostsViewModelFactory(applicationContext, postsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[PostsViewModel::class.java]

        bottomSheetDialogViewModel = ViewModelProvider(this)[BottomSheetDialogViewModel::class.java]

       viewModel.networkManager.observe(this){
           if (it == true){
               binding.fragmentContainerView.visibility = View.VISIBLE
               binding.imageView.visibility = View.GONE
               binding.tvNoSignal.visibility = View.GONE

               val navHostFragment =
                   supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
               val navController = navHostFragment.navController

               binding.bottomNavigationView.setupWithNavController(navController)

           }
           else{
               if (binding.fragmentContainerView.visibility == View.GONE){
                   binding.imageView.visibility = View.VISIBLE
                   binding.tvNoSignal.visibility = View.VISIBLE
               }
           }
       }

    }
}
package com.example.beautybell.presentation.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.beautybell.databinding.ActivityDetailBinding
import com.example.beautybell.domain.detail.entity.DetailEntity
import com.example.beautybell.presentation.common.extension.makeGone
import com.example.beautybell.presentation.common.extension.makeVisible
import com.example.beautybell.service.NavigationService
import com.example.beautybell.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val extraId by lazy { intent.getStringExtra(NavigationService.EXTRA_ID) }
    private val navigator by lazy { NavigationService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()
        fetchDetailArtisan()
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val mAdapter = DetailServiceAdapter(mutableListOf())
        binding.recyclerviewDetailService.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@DetailActivity, 2)
        }
    }

    private fun fetchDetailArtisan(){
        extraId?.let { viewModel.getArtisanById(extraId!!) }
    }

    @SuppressLint("ResourceAsColor")
    private fun handleDetail(detailEntity: DetailEntity){

        binding.textviewItemServiceName.text = detailEntity.name
        binding.textviewDetailDescription.text = detailEntity.description
        Glide.with(this).load(detailEntity.avatar).fitCenter()
            .into(binding.imageviewDetailUser)
        binding.ratingbarDetailArtisan.rating = detailEntity.rating.toFloat()

        binding.recyclerviewDetailService.adapter?.let {
            if(it is DetailServiceAdapter){
                it.updateList(detailEntity.services)
            }
        }
    }

    private fun observe(){
        observeState()
        observeProduct()
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun observeProduct(){
        viewModel.detail.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { product ->
                product?.let { handleDetail(it) }
            }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: DetailFragmentState){
        when(state){
            is DetailFragmentState.Init -> Unit
            is DetailFragmentState.ShowToast -> showToast(state.message)
            is DetailFragmentState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.linearlayoutDetail1.makeGone()
            binding.linearlayoutDetail2.makeGone()
            binding.shimmerLoadingDetail.makeVisible()
        }else{
            binding.linearlayoutDetail1.makeVisible()
            binding.linearlayoutDetail2.makeVisible()
            binding.shimmerLoadingDetail.makeGone()
        }
    }

    override fun onBackPressed() {
        navigator.startArtisanActivity(this)
    }
}
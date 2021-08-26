package com.example.beautybell.presentation.artisan

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beautybell.R
import com.example.beautybell.databinding.FragmentArtisanBinding
import com.example.beautybell.domain.artisan.entity.ArtisanEntity
import com.example.beautybell.presentation.common.extension.makeGone
import com.example.beautybell.presentation.common.extension.makeVisible
import com.example.beautybell.presentation.common.extension.showToast
import com.example.beautybell.service.NavigationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ArtisanFragment : Fragment(R.layout.fragment_artisan) {

    private var _binding: FragmentArtisanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArtisanViewModel by viewModels()
    private val navigator by lazy { NavigationService() }
    private var originalList = listOf<ArtisanEntity>()
    private var filteredList = mutableListOf<ArtisanEntity>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentArtisanBinding.bind(view)
        setupRecyclerView()
        observe()
        fetchArtisans()
    }

    private fun getListAdapter(): ArtisanAdapter? = binding.recyclerviewArtisan.adapter as? ArtisanAdapter


    private fun fetchArtisans(){
        viewModel.fetchAllArtisan()
    }

    private fun setupRecyclerView(){

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val keyword = p0.toString()
                if (keyword.isBlank()) {
                    binding.imageviewClearSearch.makeGone()
                    handleArtisans(originalList.toMutableList())
                } else {
                    binding.imageviewClearSearch.makeVisible()
                    filteredList = originalList.toMutableList()
                    filteredList
                        .filter { it.name.contains(keyword, ignoreCase = true) }
                        .toMutableList()
                        .let {
                            getListAdapter()?.updateList(it)
                        }
                }
            }
            override fun afterTextChanged(p0: Editable?) = Unit
        })

        binding.imageviewClearSearch.setOnClickListener {
            binding.editTextSearch.text.clear()
        }

        val mAdapter = ArtisanAdapter(mutableListOf(), requireContext())
        mAdapter.setItemTapListener(object : ArtisanAdapter.OnItemTap{
            override fun onTap(artisan: ArtisanEntity) {
                navigator.startDetailArtisanActivity(requireActivity(), artisan.id)
            }
        })
        binding.recyclerviewArtisan.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun handleArtisans(products: List<ArtisanEntity>){
        originalList = products
        binding.recyclerviewArtisan.adapter?.let {
            if(it is ArtisanAdapter){
                it.updateList(originalList)
            }
        }
    }

    private fun observeState(){
        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProducts(){
        viewModel.mArtisans
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { artisans ->
                handleArtisans(artisans)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observe(){
        observeState()
        observeProducts()
    }

    private fun handleState(state: ArtisanFragmenState){
        when(state){
            is ArtisanFragmenState.IsLoading -> handleLoading(state.isLoading)
            is ArtisanFragmenState.ShowToast -> requireActivity().showToast(state.message)
            is ArtisanFragmenState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.shimmerLoadingArtisans.makeVisible()
        }else{
            binding.shimmerLoadingArtisans.makeGone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
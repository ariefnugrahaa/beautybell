package com.example.beautybell.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.example.beautybell.R
import com.example.beautybell.databinding.FragmentProfileBinding
import com.example.beautybell.infra.utils.SharedPrefs
import com.example.beautybell.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        setProfile()
    }

    private fun setProfile() {
        binding.textviewProfileName.text = sharedPrefs.getName()
        binding.textviewProfileEmail.text = sharedPrefs.getEmail()
        binding.textviewProfileBirth.text = sharedPrefs.getBirth()
        if (sharedPrefs.getAvatar() !== "") {
            Glide.with(this).load(sharedPrefs.getAvatar()).circleCrop()
                .into(binding.imageviewDetailUser)
        }
    }
}
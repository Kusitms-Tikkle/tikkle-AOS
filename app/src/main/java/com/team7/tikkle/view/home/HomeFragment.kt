package com.team7.tikkle.view.home

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.team7.tikkle.R
import com.team7.tikkle.core.base.BaseFragment
import com.team7.tikkle.databinding.FragmentHomeBinding

/**
 * challenge 참여 유무에 따라 이동하는 Fragment.
 * @author chaehyun
 * @since 2023.12.07
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by viewModels<HomeViewModel>()
    
    override fun setup() {
        observeViewModel()
        homeViewModel.checkHomeChallengeExistence { existence ->
            val fragment = if (existence) HomeExistenceFragment() else HomeNoneExistenceFragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.home_fragment, fragment)
                commit()
            }
        }
    }
    
    private fun observeViewModel() {
        homeViewModel.errMsg.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

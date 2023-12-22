package com.team7.tikkle.view.memo

import com.team7.tikkle.R
import com.team7.tikkle.core.base.BaseFragment
import com.team7.tikkle.databinding.FragmentMemoFinishBinding

/**
 * 메모 작성 완료시 보여지는 Fragment.
 * @author yujeong -> chaehyun
 * @since 2023.12.07
 */
class MemoFinishFragment : BaseFragment<FragmentMemoFinishBinding>(R.layout.fragment_memo_finish) {
    
    override fun onPause() {
        super.onPause()
    }
    
    override fun setup() {
        binding?.btnGo?.setOnClickListener {
            val memoListFragment = MemoListFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            
            fragmentTransaction?.replace(R.id.main_frm, memoListFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
    }
}

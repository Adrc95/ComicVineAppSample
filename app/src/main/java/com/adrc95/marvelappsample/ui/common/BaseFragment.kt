package com.adrc95.marvelappsample.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VBinding : ViewDataBinding> : Fragment() {

    private var _binding: VBinding? = null

    protected val binding: VBinding get() = _binding!!

    abstract val bindView: (LayoutInflater, ViewGroup?, Boolean) -> VBinding

    protected abstract val viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            //setVariable(BR.viewModel, viewModel)
        }
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

package com.ycagri.buxassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ycagri.buxassignment.databinding.FragmentProductDetailBinding
import com.ycagri.buxassignment.di.Injectable
import com.ycagri.buxassignment.util.autoCleared
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProductDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productViewModel: ProductViewModel by activityViewModels { viewModelFactory }

    var binding by autoCleared<FragmentProductDetailBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()

        binding.btnSubscribe.setOnClickListener {
            productViewModel.subscribeProduct()
        }
    }

    private fun initBinding() {
        binding.product = productViewModel.product
        binding.closingPrice = productViewModel.closingPrice
        binding.currentPrice = productViewModel.currentPrice
        binding.dayRangeLow = productViewModel.dayRangeLow
        binding.dayRangeHigh = productViewModel.dayRangeHigh
        binding.yearRangeLow = productViewModel.yearRangeLow
        binding.yearRangeHigh = productViewModel.yearRangeHigh
        binding.subscription = productViewModel.subscribed
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
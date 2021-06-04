package com.ycagri.buxassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.ycagri.buxassignment.databinding.FragmentProductListBinding
import com.ycagri.buxassignment.di.Injectable
import com.ycagri.buxassignment.ui.adapter.ProductsAdapter
import com.ycagri.buxassignment.util.AppExecutors
import com.ycagri.buxassignment.util.Status
import com.ycagri.buxassignment.util.autoCleared
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductListFragment : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productViewModel: ProductViewModel by activityViewModels { viewModelFactory }

    private val refreshCallback =
        SwipeRefreshLayout.OnRefreshListener { productViewModel.refresh() }

    private var binding by autoCleared<FragmentProductListBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initRecyclerView()
        productViewModel.refresh()
    }

    private fun initBindings() {
        binding.refreshCallback = refreshCallback
        binding.refreshing = productViewModel.refreshing
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initRecyclerView() {
        binding.rvProducts.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val adapter = ProductsAdapter(appExecutors) {
            productViewModel.selectProduct(it.securityId)
        }
        binding.rvProducts.adapter = adapter

        productViewModel.products.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    adapter.submitList(it.data)
                }
                else -> {
                    it.message?.let { message ->
                        Snackbar.make(binding.clProducts, message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
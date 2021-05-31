package com.ycagri.buxassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.ycagri.buxassignment.R
import com.ycagri.buxassignment.di.Injectable
import com.ycagri.buxassignment.ui.adapter.ProductsAdapter
import com.ycagri.buxassignment.util.AppExecutors
import com.ycagri.buxassignment.util.Status
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productsRV = view.findViewById<RecyclerView>(R.id.rv_products)
        productsRV.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val adapter = ProductsAdapter(appExecutors, {

        })
        productsRV.adapter = adapter

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.srl_products)
        val coordinatorLayout = view.findViewById<CoordinatorLayout>(R.id.cl_products)

        productViewModel.products.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    swipeRefresh.isRefreshing = true
                }
                Status.SUCCESS -> {
                    adapter.submitList(it.data)
                    swipeRefresh.isRefreshing = false
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                    it.message?.let { message ->
                        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
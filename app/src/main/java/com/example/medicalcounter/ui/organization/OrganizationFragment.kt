package com.example.medicalcounter.ui.organization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicalcounter.R
import com.example.medicalcounter.databinding.FragmentOrganizationBinding
import com.example.medicalcounter.ui.base.BaseViewBindingFragment
import com.example.medicalcounter.ui.organization.adapter.OrganizationAdapter
import com.example.medicalcounter.ui.organization.common.OrganizationUiEvent

class OrganizationFragment  : BaseViewBindingFragment<FragmentOrganizationBinding>() {

    companion object {
        fun newInstance() = OrganizationFragment ()
    }

    private val viewModel: OrganizationViewModel by viewModels()
    private val adapter = OrganizationAdapter { _period, i ->

        if(i==1){
            createOrUpdateOrganization(false,_period.id)
        }else if (i==2){
            viewModel.OnClickListenerDialPhone(_period)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        viewModel.organizationFilter.observe(viewLifecycleOwner) { organization ->
            adapter.submitList(organization)
            adapter.notifyDataSetChanged()
        }

        binding.imageButtonOrganizationAdd.setOnClickListener {
            createOrUpdateOrganization(true )
        }
        viewModel.getAll()
        setupSearchView()
    }
    override fun initViewBinding(view: View): FragmentOrganizationBinding {
        return FragmentOrganizationBinding.bind(view)
    }
    private fun setUpRecyclerView(view: View) {
        val recyclerView = binding.recyclerViewOrganizations
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    private fun handleUiEvent(uiEvent: OrganizationUiEvent) {
        when (uiEvent)  {
        }
    }
    private fun createOrUpdateOrganization(isNew: Boolean ,id:Int=-1){
        try {
            val bundle = Bundle()
            bundle.putString("id", id.toString())
            bundle.putString("isNew", isNew.toString())
            view?.let{
                it.findNavController() .navigate(R.id.navigation_organization_add_update, bundle)
            }
        }catch (e: Exception){
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupSearchView() {
        binding.searchOrganization.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    viewModel.filterString=it
                    viewModel.filter(it)
                    Log.e("MainViewModeltoken_", "onQueryTextChange "+p0 )
                }
                if(p0==null || p0.length==0){
                    viewModel.filterReady()
                    viewModel.filterString= ""
                    Log.e("MainViewModeltoken_", "filterReady "+p0 )
                }
                return false
            }
        })
    }
}
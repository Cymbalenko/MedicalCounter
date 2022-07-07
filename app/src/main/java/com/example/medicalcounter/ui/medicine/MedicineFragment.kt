package com.example.medicalcounter.ui.medicine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicalcounter.R
import com.example.medicalcounter.databinding.FragmentMedicineAddUpdateBinding
import com.example.medicalcounter.databinding.FragmentMedicineBinding
import com.example.medicalcounter.ui.base.BaseViewBindingFragment
import com.example.medicalcounter.ui.medicine.adapter.MedicineAdapter
import com.example.medicalcounter.ui.medicine.common.MedicineUiEvent

class MedicineFragment : BaseViewBindingFragment<FragmentMedicineBinding>() {

    companion object {
        fun newInstance() = MedicineFragment ()
    }

    private val viewModel: MedicineViewModel by viewModels()
    private val adapter = MedicineAdapter { medicine, i ->

        if(i==1){
            createOrUpdateMedicine(false,medicine.id)
        }else if (i==2){

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        viewModel.medicineFilter.observe(viewLifecycleOwner) { medicine ->
            adapter.submitList(medicine)
            adapter.notifyDataSetChanged()
        }
        binding.imageViewMedicineAdd.setOnClickListener {
            createOrUpdateMedicine(true )
        }
        viewModel.getAll()
        setupSearchView()
    }
    override fun initViewBinding(view: View): FragmentMedicineBinding {
        return FragmentMedicineBinding.bind(view)
    }
    private fun setUpRecyclerView(view: View) {
        val recyclerView = binding.recyclerViewMedicine
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    private fun handleUiEvent(uiEvent: MedicineUiEvent) {
        when (uiEvent)  {
        }
    }
    private fun createOrUpdateMedicine(isNew: Boolean ,id:Int=-1){
        try {
            val bundle = Bundle()
            bundle.putString("id", id.toString())
            bundle.putString("isNew", isNew.toString())
            view?.let{
                Log.e("MainViewModeltoken_", "Success\n"  +bundle)
                it.findNavController() .navigate(R.id.navigation_medicine_add_update, bundle)
            }
        }catch (e: Exception){
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupSearchView() {
        binding.searchMedical.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
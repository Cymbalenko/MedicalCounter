package com.example.medicalcounter.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicalcounter.R
import com.example.medicalcounter.databinding.FragmentHomeBinding
import com.example.medicalcounter.ui.base.BaseViewBindingFragment
import com.example.medicalcounter.ui.home.adapter.HomeAdapter
import com.example.medicalcounter.ui.home.common.HomeUiEvent
import com.example.medicalcounter.util.observeEvent
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseViewBindingFragment<FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = HomeAdapter { counter, i ->

        if (i == 1) {
            counter.coun+=1
           viewModel.updateCounters(counter)
        } else if (i == 2) {
            val alertDialog: AlertDialog? = activity?.let { it ->
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle(getString(R.string.minus))
                    setMessage(getString(R.string.minus_where))
                    setCancelable(true)
                    setPositiveButton(getString(R.string.yes)) { dialog, id ->
                        counter.coun-=1
                        viewModel.updateCounters(counter)
                    }
                    setNegativeButton(getString(R.string.no),
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                }
                builder.create()
            }
            alertDialog?.show()
        }

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        viewModel.initial=false
        Log.e("selectSort", "initial\n"  + viewModel.initial)
        val formaterYear = SimpleDateFormat("y")
        val formaterMounth = SimpleDateFormat("M")
        val date = Date( )
        viewModel.selectMounth=formaterMounth.format(date).toInt()-1
        viewModel.selectYear=formaterYear.format(date).toInt()
        Log.e("insertCounter", "init\n" )
        loadSpinners()
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.year_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            binding.spinnerYear.setSelection(adapter.getPosition(viewModel.selectYear.toString()))
        }
        binding.spinnerMounth.setSelection(viewModel.selectMounth)

        viewModel.showEvent.observeEvent(this) { actualOrdersUiEvent ->
            handleUiEvent(actualOrdersUiEvent)
        }
        viewModel.countersFilter.observe(viewLifecycleOwner) { orders ->
            adapter.submitList(orders)
            adapter.notifyDataSetChanged()
        }
        setupSearchView()
        viewModel.sorting()
    }

    override fun initViewBinding(view: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(view)
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerView = binding.recyclerViewHome
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    private fun handleUiEvent(uiEvent: HomeUiEvent) {
        when (uiEvent) {
            is HomeUiEvent.Update->{
                    viewModel.filter(viewModel.filterString)
            }
            is HomeUiEvent.Confirm->{
                Log.e("insertCounter","HomeUiEvent.Confirm-")
                /*if(viewModel.selectMounth!=-1 && !viewModel.initial){
                    ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.year_array,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        binding.spinnerYear.setSelection(adapter.getPosition(viewModel.selectYear.toString()))
                    }
                    binding.spinnerMounth.setSelection(viewModel.selectMounth-1)
                }*/
            }
        }
    }

    fun loadSpinners() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.mounth_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerMounth.adapter = adapter
           // binding.spinnerMounth.setSelection(viewModel.selectMounth-1)
            binding.spinnerMounth.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("loadAddCounters", "loadAddCounters sss\n" + parent)
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.selectMounth = position
                        Log.e("insertCounter", "loadAddCounters\n" + viewModel.selectMounth)
                        Log.e("insertCounter", "viewModel.initial\n" + viewModel.initial)
                        if(viewModel.initial){
                            Log.e("insertCounter", "if(viewModel.initial){\n" + viewModel.selectMounth)
                             viewModel.loadLastCounter()
                        }
                    }
                }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.year_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerYear.adapter = adapter
            binding.spinnerYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("loadAddCounters", "spinnerYear ssss\n" + parent)
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        parent?.let { viewModel.selectYear=it.selectedItem.toString().toInt() }
                        Log.e("insertCounter", "spinnerYear\n" + viewModel.selectYear)
                        Log.e("insertCounter", "viewModel.initial\n" + viewModel.initial)
                        if(viewModel.initial) {
                            Log.e("insertCounter", "viewModel.initial\n" + viewModel.selectYear)
                            viewModel.loadLastCounter()
                        }
                    }
                }
        }


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerSort.adapter = adapter
            binding.spinnerSort.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.e("loadAddCounters", "spinnerYear ssss\n" + parent)
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.selectSort=position+1
                        viewModel.sorting()
                    }
                }
        }
        viewModel.loadLastCounter()
    }
    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
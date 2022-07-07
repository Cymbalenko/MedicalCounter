package com.example.medicalcounter.ui.medicineAddUpdate

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.medicalcounter.R
import com.example.medicalcounter.databinding.FragmentMedicineAddUpdateBinding
import com.example.medicalcounter.model.db.dataClass.Medicine
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.ui.MyDialogFragment
import com.example.medicalcounter.ui.base.BaseViewBindingFragment
import com.example.medicalcounter.ui.medicine.common.MedicineUiEvent
import com.example.medicalcounter.ui.organization.common.OrganizationUiEvent
import com.example.medicalcounter.util.observeEvent

class MedicineAddUpdateFragment  : BaseViewBindingFragment<FragmentMedicineAddUpdateBinding>() {

    private val viewModel: MedicineAddUpdateViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.pause) {
            view?.let{
                it.findNavController().popBackStack()
            }
        }
        arguments?.let{
            it.getString("id")?.let { g->
                viewModel.medicineId =  g.toInt()
            }
            it.getString("isNew")?.let { g->
                viewModel.isNew =  g.toBoolean()
            }
        }
        viewModel.loadMedicine()

        binding.imageButtonDeleteMedicine.visibility= View.INVISIBLE
        binding.imageButtonSaveMedicine.visibility= View.INVISIBLE


        binding.textViewOrganizationTitleMedicineError.visibility= View.VISIBLE
        binding.textViewOrganizationTitleMedicine.visibility= View.INVISIBLE


        binding.editTextTextPersonNameMedicine.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("aaaaaaa",""+Int)
                if(binding.editTextTextPersonNameMedicine.text.count()<3){
                    binding.imageButtonSaveMedicine.visibility= View.INVISIBLE
                }else{
                    binding.imageButtonSaveMedicine.visibility= View.VISIBLE
                }
            }
        })

        binding.imageButtonSaveMedicine.setOnClickListener{
            val text = binding.editTextTextPersonNameMedicine.text
            if(viewModel.isNew){

                var item = Medicine( title =  text.toString(),viewModel.selectPositionId,viewModel.selectPositionName)
                viewModel.createMedicine(item)
                view?.let{
                    it.findNavController().popBackStack()
                }

            }else{
                var item = viewModel.medicine.value
                item?.let {
                    item.title=text.toString()
                    item.organizationName=viewModel.selectPositionName
                    item.organizationId=viewModel.selectPositionId
                    viewModel.createMedicine(item)
                    view?.let{
                        it.findNavController().popBackStack()
                    }
                }

            }

        }
        binding.imageButtonLastMedicine.setOnClickListener{
            view.let{
                view.let{
                    it.findNavController().popBackStack()
                }
            }
        }
        viewModel.showEvent.observeEvent(this) { event ->
            handleUiEvent(event)
        }
        binding.imageButtonDeleteMedicine.setOnClickListener {
            if(!viewModel.isNew){

                val alertDialog: android.app.AlertDialog? = activity?.let { it ->
                    val builder = android.app.AlertDialog.Builder(it)
                    builder.apply {
                        setTitle(getString(R.string.delete))
                        setMessage(getString(R.string.delete_where))
                        setCancelable(true)
                        setPositiveButton(getString(R.string.yes)) { dialog, id ->
                            viewModel.deleteMedicine( )
                            view?.let{
                                view?.let{
                                    it.findNavController().popBackStack()
                                }
                            }
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
        viewModel.getAllOrganization()



    }

    override fun onPause() {
        viewModel.pause=true
        super.onPause()
    }

    override fun onStop() {
        viewModel.pause=true
        super.onStop()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                view?.let{
                    it.findNavController().popBackStack()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun handleUiEvent(uiEvent: MedicineUiEvent) {
        when (uiEvent) {
            is MedicineUiEvent.Confirm ->{
                Log.e("MainViewModeltoken_", "viewModel.organization\n" + viewModel.medicine.value?.title)
                viewModel.medicine.value?.let {
                    binding.editTextTextPersonNameMedicine.setText(it.title)
                    binding.textViewOrganizationTitleMedicine.setText(it.organizationName)
                    binding.imageButtonDeleteMedicine.visibility= View.VISIBLE
                    binding.textViewOrganizationTitleMedicineError.visibility= View.INVISIBLE
                    binding.textViewOrganizationTitleMedicine.visibility= View.VISIBLE
                }
            }
            is MedicineUiEvent.Loading ->{
                setupListView()
                setupSearchView()
            }
            is MedicineUiEvent.Error  ->{
                Log.e("Error", "Error\n"  )
                Toast.makeText(context, uiEvent.errorStringId, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medicine_add_update, container, false)
    }
    override fun initViewBinding(view: View): FragmentMedicineAddUpdateBinding {
        return FragmentMedicineAddUpdateBinding.bind(view)
    }
    private fun setupListView() {
        context?.let{
            adapter = ArrayAdapter(it, android.R.layout.simple_list_item_1 ,viewModel.organizationList)
            binding.listView.adapter = adapter
        }
    }
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val isMatchFound = viewModel.organizationList.contains(p0)
                val msg = if (isMatchFound) "Found: $p0" else getString(R.string.no_match)
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                Log.e("MainViewModeltoken_", "onQueryTextChange "+p0 )
                return false
            }
        })
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            var name=parent.adapter.getItem(position).toString()
            Log.e("MainViewModeltoken_", "parent.get "+parent.adapter.getItem(position) )
            viewModel.organization?.value?.let { list ->
                var item = list.singleOrNull  { it.title==name }
                item?.let {
                    viewModel.selectPositionId=it.id
                    viewModel.selectPositionName=it.title

                    binding.textViewOrganizationTitleMedicineError.visibility= View.INVISIBLE
                    binding.textViewOrganizationTitleMedicine.visibility= View.VISIBLE
                    binding.textViewOrganizationTitleMedicine.setText(it.title)
                }
            }
        }
    }

}
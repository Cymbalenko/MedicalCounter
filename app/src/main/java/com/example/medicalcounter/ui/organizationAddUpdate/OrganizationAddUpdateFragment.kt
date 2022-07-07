package com.example.medicalcounter.ui.organizationAddUpdate

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.medicalcounter.R
import com.example.medicalcounter.databinding.FragmentOrganizationAddUpdateBinding
import com.example.medicalcounter.model.db.dataClass.Organization
import com.example.medicalcounter.placeholder.PhoneTextFormatter
import com.example.medicalcounter.ui.MyDialogFragment
import com.example.medicalcounter.ui.base.BaseViewBindingFragment
import com.example.medicalcounter.ui.organization.common.OrganizationUiEvent
import com.example.medicalcounter.ui.organizationAddUpdate.common.OrganizationAddUpdateUiEvent
import com.example.medicalcounter.util.Event
import com.example.medicalcounter.util.observeEvent


class OrganizationAddUpdateFragment : BaseViewBindingFragment<FragmentOrganizationAddUpdateBinding>() {

    private val viewModel: OrganizationAddUpdateViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.pause) {
            view?.let{
                it.findNavController().popBackStack()
            }
        }
        viewModel.showEvent.observeEvent(this) { actualOrdersUiEvent ->
            handleUiEvent(actualOrdersUiEvent)
        }
        arguments?.let{
            it.getString("id")?.let { g->
                viewModel.organizationId =  g.toInt()
            }
            it.getString("isNew")?.let { g->
                viewModel.isNew =  g.toBoolean()
            }
        }

        binding.imageButtonDelete.visibility= INVISIBLE
        binding.imageButtonSave.visibility= INVISIBLE
         binding.editTextPhone.addTextChangedListener(PhoneTextFormatter(binding.editTextPhone, "(###)###-####"))
        binding.editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("aaaaaaa",""+Int)
                if(binding.editTextTextPersonName.text.count()<3){
                    binding.imageButtonSave.visibility=INVISIBLE
                }else{
                    binding.imageButtonSave.visibility=VISIBLE
                }
            }
        })
        viewModel.loadOrganization()
        binding.imageButtonSave.setOnClickListener{
            if(viewModel.isNew){
                var item = Organization( binding.editTextTextPersonName.text.toString(),0, binding.editTextAgent.text.toString(),binding.editTextPhone.text.toString())
                Log.e("MainViewModeltoken", "Success\n" + item)
                viewModel.createOrganization(item)
                view?.let{
                    it.findNavController().popBackStack()
                }
            }else{
                var item = viewModel.organization.value
                item?.let {
                    item.title=binding.editTextTextPersonName.text.toString()
                    if(binding.editTextAgent.text.toString().count()>0){
                        item.agent=binding.editTextAgent.text.toString()
                    }
                    if(binding.editTextPhone.text.toString().count()>0){
                        item.phone=binding.editTextPhone.text.toString()
                    }
                    Log.e("MainViewModeltoken", "Success\n" + item)
                    viewModel.createOrganization(item)
                    view?.let{
                        it.findNavController().popBackStack()
                    }
                }

            }

        }
        binding.imageButtonLast.setOnClickListener{
            view?.let{
                view?.let{
                    it.findNavController().popBackStack()
                }
            }
        }

        binding.imageButtonDelete.setOnClickListener{
            Log.e("imageButtonDelete", "viewModel.isNew\n" + viewModel.isNew)
            if(!viewModel.isNew){
                val alertDialog: AlertDialog? = activity?.let { it ->
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                         setTitle(getString(R.string.delete))
                         setMessage(getString(R.string.delete_where))
                         setCancelable(true)
                         setPositiveButton(getString(R.string.yes)) { dialog, id ->
                             viewModel.deleteOrganization( )
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
    override fun onPause() {
        viewModel.pause=true
        super.onPause()
    }

    override fun onStop() {
        viewModel.pause=true
        super.onStop()
    }
    private fun handleUiEvent(event: OrganizationAddUpdateUiEvent ) {

            when (event) {
                is OrganizationAddUpdateUiEvent.Confirm -> {
                    Log.e("deleteOrganization", "Confirm\n")
                    viewModel.organization.value?.let {
                        binding.editTextTextPersonName.setText(it.title)
                        binding.editTextAgent.setText(it.agent)
                        binding.editTextPhone.setText(it.phone)
                        binding.imageButtonDelete.visibility= VISIBLE
                    }
                }
                is OrganizationAddUpdateUiEvent.Error -> {
                    Log.e("deleteOrganization", "Error\n")
                    Toast.makeText(context, R.string.organization_delete_error, Toast.LENGTH_LONG)
                        .show()
                }
                is OrganizationAddUpdateUiEvent.Loading -> Log.e("deleteOrganization", "Loading\n")
                is OrganizationAddUpdateUiEvent.Update -> Log.e("deleteOrganization", "Update\n")
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organization_add_update, container, false)
    }
    override fun initViewBinding(view: View): FragmentOrganizationAddUpdateBinding {
        return FragmentOrganizationAddUpdateBinding.bind(view)
    }
}
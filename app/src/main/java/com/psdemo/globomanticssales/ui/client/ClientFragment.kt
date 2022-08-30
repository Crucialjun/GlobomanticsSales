package com.psdemo.globomanticssales.ui.client

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.psdemo.globomanticssales.R
import com.psdemo.globomanticssales.buildPdf
import com.psdemo.globomanticssales.getFiles
import com.psdemo.globomanticssales.proposalExists
import kotlinx.android.synthetic.main.fragment_client.*
import java.util.*

class ClientFragment : Fragment(), FilesAdapter.OnClickListener {
    private var clientId = 0
    private var adapter = FilesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val clientViewModel = ViewModelProvider(this).get(ClientViewModel::class.java)
        listFiles.layoutManager = LinearLayoutManager(activity)
        listFiles.adapter = adapter
        arguments?.let { bundle ->
            val passedArguments = ClientFragmentArgs.fromBundle(bundle)
            clientViewModel.getClient(passedArguments.clientId)
                .observe(viewLifecycleOwner, Observer { client ->
                    name.text = client.name
                    order.text = client.order
                    terms.text = client.terms
                    clientId= client.id

                    val calendar = Calendar.getInstance()
                    val dateFormat = DateFormat.getDateFormat(view.context)
                    calendar.timeInMillis = client.date
                    date.text = dateFormat.format(calendar.time)

                    adapter.setFiles(requireContext().getFiles(clientId))

                    if(requireContext().proposalExists(clientId)){
                        btnProposal.visibility = View.INVISIBLE
                    }else{
                        btnProposal.setOnClickListener {

                            requireContext().buildPdf(client)
                            it.visibility = View.INVISIBLE
                            adapter.setFiles(requireContext().getFiles(clientId))
                        }
                    }
                })
        }



    }

    override fun onClick(id: Int) {

    }
}



package com.minutecodes.satelspeed.activities.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minutecodes.satelspeed.R
import com.minutecodes.satelspeed.activities.adapters.HistoryRecyclerAdapter
import com.minutecodes.satelspeed.activities.interfaces.java.RecyclerItemClickListener
import com.minutecodes.satelspeed.activities.viewmodel.DataViewModel
import com.minutecodes.satelspeed.activities.viewmodel.factory.DataViewModelFactory

class HistoryFragment : Fragment() {
    private lateinit var historyAdapter: HistoryRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataViewModel: DataViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        historyAdapter = HistoryRecyclerAdapter(RecyclerItemClickListener {

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.history_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.speedHistoryRecyclerView)
        initRecyclerView()
        initViewModel()
        loadHistoryRecord()
    }

    private fun initRecyclerView() {
        with(recyclerView) {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initViewModel() {
        dataViewModel = ViewModelProvider(
            requireActivity(),
            DataViewModelFactory(requireActivity().application)
        )[DataViewModel::class.java]

//        with(ViewModelProvider(requireActivity())) {
//            dataViewModel = get(DataViewModel(requireActivity().application)::class.java)
//        }
    }

    private fun loadHistoryRecord() {
        dataViewModel.historyRecord()?.observe(viewLifecycleOwner, Observer {
            historyAdapter.submitList(it)
        })
    }

}
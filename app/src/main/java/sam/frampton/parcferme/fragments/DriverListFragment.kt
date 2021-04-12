package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import sam.frampton.parcferme.adapters.DriverAdapter
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.databinding.FragmentDriverListBinding
import sam.frampton.parcferme.viewmodels.DriverListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class DriverListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val driverListViewModel: DriverListViewModel by viewModels()
    private lateinit var binding: FragmentDriverListBinding
    private lateinit var driverAdapter: DriverAdapter
    private var driverList: LiveData<List<Driver>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverListBinding.inflate(layoutInflater)
        initialiseRecyclerView()
        initialiseSpinner()
        return binding.root
    }

    private fun initialiseRecyclerView() {
        driverAdapter = DriverAdapter {
            val directions =
                DriverListFragmentDirections.actionDriverListFragmentToDriverDetailFragment(it)
            findNavController().navigate(directions)
        }
        binding.rvDriverListDrivers.adapter = driverAdapter
    }

    private fun initialiseSpinner() {
        val seasonList = ArrayList<Int>()
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, seasonList)
        binding.spDriverListSeason.adapter = spinnerAdapter
        binding.spDriverListSeason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val season = parent.getItemAtPosition(position) as Int
                    driverList?.removeObservers(viewLifecycleOwner)
                    driverList = driverListViewModel.getDrivers(season)
                    driverList?.observe(viewLifecycleOwner) { driverAdapter.submitList(it) }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) {
                seasonList.clear()
                seasonList.addAll(seasons)
                spinnerAdapter.notifyDataSetChanged()
            }
        }
    }
}
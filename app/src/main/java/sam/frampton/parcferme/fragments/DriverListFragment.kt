package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import sam.frampton.parcferme.adapters.DriverAdapter
import sam.frampton.parcferme.databinding.FragmentDriverListBinding
import sam.frampton.parcferme.viewmodels.DriverListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class DriverListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val driverListViewModel: DriverListViewModel by activityViewModels()
    private lateinit var binding: FragmentDriverListBinding

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
        val driverAdapter = DriverAdapter { driver ->
            val directions = DriverListFragmentDirections
                .actionDriverListFragmentToDriverDetailFragment(
                    driver,
                    "${driver.givenName} ${driver.familyName}"
                )
            findNavController().navigate(directions)
        }
        binding.rvDriverListDrivers.adapter = driverAdapter
        driverListViewModel.driverList.observe(viewLifecycleOwner) {
            driverAdapter.submitList(it)
        }
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
                    if (driverListViewModel.season != season) {
                        driverListViewModel.setSeason(season)
                        driverListViewModel.refreshDrivers(false)
                    }
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
                driverListViewModel.season?.let {
                    binding.spDriverListSeason.setSelection(seasons.indexOf(it))
                }
            }
        }
    }
}
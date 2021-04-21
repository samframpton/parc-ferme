package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.DriverAdapter
import sam.frampton.parcferme.databinding.FragmentDriverListBinding
import sam.frampton.parcferme.viewmodels.DriverListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class DriverListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val driverListViewModel: DriverListViewModel by activityViewModels()
    private lateinit var binding: FragmentDriverListBinding
    private lateinit var driverAdapter: DriverAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverListBinding.inflate(layoutInflater)
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        driverAdapter = DriverAdapter { driver ->
            val directions = DriverListFragmentDirections
                .actionDriverListFragmentToDriverDetailFragment(
                    driver,
                    getString(R.string.driver_full_name, driver.givenName, driver.familyName)
                )
            findNavController().navigate(directions)
        }
        binding.rvDriverListDrivers.adapter = driverAdapter
    }

    private fun setupChips() {
        binding.chipDriverListSeason.setOnClickListener {
            val directions = DriverListFragmentDirections
                .actionDriverListFragmentToSeasonDialogFragment()
            findNavController().navigate(directions)
        }
    }

    private fun setupObservers() {
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) {
                setSeason(driverListViewModel.season ?: seasons.first())
            }
        }

        driverListViewModel.driverList.observe(viewLifecycleOwner) {
            driverAdapter.submitList(it)
        }

        findNavController().getBackStackEntry(R.id.driverListFragment).savedStateHandle
            .getLiveData<Int>(SeasonDialogFragment.SEASON_KEY).observe(viewLifecycleOwner) {
                setSeason(it)
            }
    }

    private fun setSeason(season: Int) {
        binding.chipDriverListSeason.text = season.toString()
        if (driverListViewModel.season != season) {
            driverListViewModel.setSeason(season)
            driverListViewModel.refreshDrivers(false)
        }
    }
}
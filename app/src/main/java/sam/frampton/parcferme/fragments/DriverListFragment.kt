package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.DriverAdapter
import sam.frampton.parcferme.databinding.FragmentDriverListBinding
import sam.frampton.parcferme.viewmodels.DriverListViewModel
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class DriverListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val driverListViewModel: DriverListViewModel by activityViewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
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
                    driver.getTitle(requireContext())
                )
            findNavController().navigate(directions)
        }
        binding.rvDriverList.adapter = driverAdapter
        binding.rvDriverList.setOrientedLayoutManager()
        binding.swipeRefreshDriverList.setOnRefreshListener { refresh() }
    }

    private fun setupChips() {
        binding.chipDriverListSeason.setOnClickListener {
            val seasons = seasonViewModel.seasons.value
            if (!seasons.isNullOrEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.season)
                    .setItems(seasons.map { it.toString() }.toTypedArray()) { _, which ->
                        setSeason(seasons[which])
                    }
                    .show()
            }
        }
    }

    private fun setupObservers() {
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) setSeason(driverListViewModel.season ?: seasons.first())
        }
        seasonViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }
        driverListViewModel.driverList.observe(viewLifecycleOwner, driverAdapter::submitList)
        driverListViewModel.refreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                driverListViewModel.clearRefreshResult()
            }
        }
        driverListViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }
        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                refresh()
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }

    private fun refresh() {
        seasonViewModel.refreshSeasons(true)
        driverListViewModel.refreshDrivers(true)
    }

    private fun setSeason(season: Int) {
        binding.chipDriverListSeason.text = season.toString()
        if (driverListViewModel.season != season) {
            driverListViewModel.setSeason(season)
            driverListViewModel.refreshDrivers(false)
        }
    }

    private fun isRefreshing() {
        binding.swipeRefreshDriverList.isRefreshing =
            seasonViewModel.isRefreshing.value == true
                    || driverListViewModel.isRefreshing.value == true
    }
}
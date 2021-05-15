package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.RaceAdapter
import sam.frampton.parcferme.databinding.FragmentRaceListBinding
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.RaceListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class RaceListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val raceListViewModel: RaceListViewModel by activityViewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val args: RaceListFragmentArgs by navArgs()
    private lateinit var binding: FragmentRaceListBinding
    private lateinit var raceAdapter: RaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRaceListBinding.inflate(layoutInflater)
        seasonViewModel.refreshSeasons(false)
        setSeason(args.season)
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        raceAdapter = RaceAdapter { race ->
            val directions = RaceListFragmentDirections
                .actionRaceListFragmentToRaceDetailFragment(race, race.raceName)
            findNavController().navigate(directions)
        }
        binding.rvRaceList.adapter = raceAdapter
        binding.rvRaceList.setOrientedLayoutManager()
        binding.swipeRefreshRaceList.setOnRefreshListener { refresh() }
        binding.swipeRefreshRaceList.setColor()
    }

    private fun setupChips() {
        binding.chipRaceListSeason.setOnClickListener {
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
            if (seasons.isNotEmpty()) setSeason(raceListViewModel.season ?: seasons.first())
        }
        seasonViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }
        raceListViewModel.raceList.observe(viewLifecycleOwner, raceAdapter::submitList)
        raceListViewModel.refreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                raceListViewModel.clearRefreshResult()
            }
        }
        raceListViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }
        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                refresh()
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }

    private fun refresh() {
        seasonViewModel.refreshSeasons(true)
        raceListViewModel.refreshRaces(true)
    }

    private fun setSeason(season: Int) {
        if (season != -1) {
            binding.chipRaceListSeason.text = season.toString()
            if (raceListViewModel.season != season) {
                raceListViewModel.setSeason(season)
                raceListViewModel.refreshRaces(false)
            }
        }
    }

    private fun isRefreshing() {
        binding.swipeRefreshRaceList.isRefreshing =
            seasonViewModel.isRefreshing.value == true
                    || raceListViewModel.isRefreshing.value == true
    }
}
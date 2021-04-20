package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.RaceAdapter
import sam.frampton.parcferme.databinding.FragmentRaceListBinding
import sam.frampton.parcferme.viewmodels.RaceListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class RaceListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val raceListViewModel: RaceListViewModel by activityViewModels()
    private lateinit var binding: FragmentRaceListBinding
    private lateinit var raceAdapter: RaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRaceListBinding.inflate(layoutInflater)
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
        binding.rvRaceListRaces.adapter = raceAdapter
    }

    private fun setupChips() {
        binding.chipRaceListSeason.setOnClickListener {
            val directions = RaceListFragmentDirections
                .actionRaceListFragmentToSeasonDialogFragment()
            findNavController().navigate(directions)
        }
    }

    private fun setupObservers() {
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) {
                setSeason(raceListViewModel.season ?: seasons.first())
            }
        }

        raceListViewModel.raceList.observe(viewLifecycleOwner) {
            raceAdapter.submitList(it)
        }

        findNavController().getBackStackEntry(R.id.raceListFragment).savedStateHandle
            .getLiveData<Int>(SeasonDialogFragment.SEASON_KEY).observe(viewLifecycleOwner) {
                setSeason(it)
            }
    }

    private fun setSeason(season: Int) {
        binding.chipRaceListSeason.text = season.toString()
        if (raceListViewModel.season != season) {
            raceListViewModel.setSeason(season)
            raceListViewModel.refreshRaces(false)
        }
    }
}
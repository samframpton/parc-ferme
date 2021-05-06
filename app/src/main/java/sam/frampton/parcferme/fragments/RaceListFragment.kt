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
        binding.rvRaceList.adapter = raceAdapter
        binding.rvRaceList.setOrientedLayoutManager()
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
            if (seasons.isNotEmpty()) {
                setSeason(raceListViewModel.season ?: seasons.first())
            }
        }
        raceListViewModel.raceList.observe(viewLifecycleOwner) {
            raceAdapter.submitList(it)
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
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
import sam.frampton.parcferme.adapters.RaceAdapter
import sam.frampton.parcferme.databinding.FragmentRaceListBinding
import sam.frampton.parcferme.viewmodels.RaceListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class RaceListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val raceListViewModel: RaceListViewModel by activityViewModels()
    private lateinit var binding: FragmentRaceListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRaceListBinding.inflate(layoutInflater)
        initialiseRecyclerView()
        initialiseSpinner()
        return binding.root
    }

    private fun initialiseRecyclerView() {
        val raceAdapter = RaceAdapter { race ->
            val directions = RaceListFragmentDirections
                .actionRaceListFragmentToRaceDetailFragment(
                    race,
                    race.raceName
                )
            findNavController().navigate(directions)
        }
        binding.rvRaceListRaces.adapter = raceAdapter
        raceListViewModel.raceList.observe(viewLifecycleOwner) {
            raceAdapter.submitList(it)
        }
    }

    private fun initialiseSpinner() {
        val seasonList = ArrayList<Int>()
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, seasonList)
        binding.spRaceListSeason.adapter = spinnerAdapter
        binding.spRaceListSeason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val season = parent.getItemAtPosition(position) as Int
                    if (raceListViewModel.season != season) {
                        raceListViewModel.setSeason(season)
                        raceListViewModel.refreshRaces(false)
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
                raceListViewModel.season?.let {
                    binding.spRaceListSeason.setSelection(seasons.indexOf(it))
                }
            }
        }
    }
}
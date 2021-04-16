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
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.adapters.ConstructorStandingAdapter
import sam.frampton.parcferme.adapters.DriverStandingAdapter
import sam.frampton.parcferme.databinding.FragmentStandingListBinding
import sam.frampton.parcferme.fragments.StandingListFragment.StandingType.*
import sam.frampton.parcferme.viewmodels.SeasonViewModel
import sam.frampton.parcferme.viewmodels.StandingListViewModel

class StandingListFragment : Fragment() {

    enum class StandingType { DRIVER, CONSTRUCTOR, DEFAULT }

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val standingListViewModel: StandingListViewModel by activityViewModels()
    private val args: StandingListFragmentArgs by navArgs()
    private lateinit var binding: FragmentStandingListBinding
    private lateinit var driverAdapter: DriverStandingAdapter
    private lateinit var constructorAdapter: ConstructorStandingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStandingListBinding.inflate(layoutInflater)
        initialiseRecyclerView()
        initialiseSeasonSpinner()
        initialiseTypeSpinner()
        return binding.root
    }

    private fun initialiseRecyclerView() {
        driverAdapter = DriverStandingAdapter { standing ->
            val directions = StandingListFragmentDirections
                .actionStandingListFragmentToDriverDetailFragment(
                    standing.driver,
                    "${standing.driver.givenName} ${standing.driver.familyName}"
                )
            findNavController().navigate(directions)
        }
        standingListViewModel.driverStandingList.observe(viewLifecycleOwner) {
            driverAdapter.submitList(it)
        }
        constructorAdapter = ConstructorStandingAdapter { standing ->
            val directions = StandingListFragmentDirections
                .actionStandingListFragmentToConstructorDetailFragment(
                    standing.constructor,
                    standing.constructor.name
                )
            findNavController().navigate(directions)
        }
        standingListViewModel.constructorStandingList.observe(viewLifecycleOwner) {
            constructorAdapter.submitList(it)
        }
    }

    private fun initialiseSeasonSpinner() {
        val seasonList = ArrayList<Int>()
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, seasonList)
        binding.spStandingListSeason.adapter = spinnerAdapter
        binding.spStandingListSeason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val season = parent.getItemAtPosition(position) as Int
                    if (standingListViewModel.season != season) {
                        setSeason(season)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        if (args.season != -1) {
            setSeason(args.season)
        }
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) {
                seasonList.clear()
                seasonList.addAll(seasons)
                standingListViewModel.season?.let {
                    binding.spStandingListSeason.setSelection(seasons.indexOf(it))
                }
                spinnerAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setSeason(season: Int) {
        standingListViewModel.setSeason(season)
        standingListViewModel.refreshDriverStandings(false)
        standingListViewModel.refreshConstructorStandings(false)
    }

    private fun initialiseTypeSpinner() {
        if (args.standingType == CONSTRUCTOR ||
            (args.standingType == DEFAULT && standingListViewModel.standingType == CONSTRUCTOR)
        ) {
            binding.spStandingListType.setSelection(1)
        }
        binding.spStandingListType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            standingListViewModel.standingType = DRIVER
                            binding.rvStandingListStandings.adapter = driverAdapter
                        }
                        1 -> {
                            standingListViewModel.standingType = CONSTRUCTOR
                            binding.rvStandingListStandings.adapter = constructorAdapter
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }
}
package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.R
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
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        driverAdapter = DriverStandingAdapter { standing ->
            val driver = standing.driver
            val directions = StandingListFragmentDirections
                .actionStandingListFragmentToDriverDetailFragment(
                    driver,
                    getString(R.string.driver_full_name, driver.givenName, driver.familyName)
                )
            findNavController().navigate(directions)
        }
        constructorAdapter = ConstructorStandingAdapter { standing ->
            val directions = StandingListFragmentDirections
                .actionStandingListFragmentToConstructorDetailFragment(
                    standing.constructor,
                    standing.constructor.name
                )
            findNavController().navigate(directions)
        }
    }

    private fun setupChips() {
        binding.chipStandingListSeason.setOnClickListener {
            val directions = StandingListFragmentDirections
                .actionStandingListFragmentToSeasonDialogFragment()
            findNavController().navigate(directions)
        }
        if (args.standingType == CONSTRUCTOR ||
            (args.standingType == DEFAULT && standingListViewModel.standingType == CONSTRUCTOR)
        ) {
            setStandingType(CONSTRUCTOR)
            binding.chipGroupStandingListType.check(R.id.chip_standing_list_constructors)
        } else {
            setStandingType(DRIVER)
        }
        binding.chipGroupStandingListType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_standing_list_drivers -> {
                    setStandingType(DRIVER)
                }
                R.id.chip_standing_list_constructors -> {
                    setStandingType(CONSTRUCTOR)
                }
            }
        }
    }

    private fun setStandingType(standingType: StandingType) {
        standingListViewModel.standingType = standingType
        when (standingType) {
            DRIVER, DEFAULT -> {
                binding.rvStandingListStandings.adapter = driverAdapter
            }
            CONSTRUCTOR -> {
                binding.rvStandingListStandings.adapter = constructorAdapter
            }
        }
    }

    private fun setupObservers() {
        if (args.season != -1) {
            setSeason(args.season)
        }
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) {
                setSeason(standingListViewModel.season ?: seasons.first())
            }
        }

        standingListViewModel.driverStandingList.observe(viewLifecycleOwner) {
            driverAdapter.submitList(it)
        }

        standingListViewModel.constructorStandingList.observe(viewLifecycleOwner) {
            constructorAdapter.submitList(it)
        }

        findNavController().getBackStackEntry(R.id.standingListFragment).savedStateHandle
            .getLiveData<Int>(SeasonDialogFragment.SEASON_KEY).observe(viewLifecycleOwner) {
                setSeason(it)
            }
    }

    private fun setSeason(season: Int) {
        binding.chipStandingListSeason.text = season.toString()
        if (standingListViewModel.season != season) {
            standingListViewModel.setSeason(season)
            standingListViewModel.refreshDriverStandings(false)
            standingListViewModel.refreshConstructorStandings(false)
        }
    }
}
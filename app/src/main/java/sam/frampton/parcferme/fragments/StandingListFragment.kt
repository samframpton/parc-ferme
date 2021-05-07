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
import sam.frampton.parcferme.adapters.ConstructorStandingAdapter
import sam.frampton.parcferme.adapters.DriverStandingAdapter
import sam.frampton.parcferme.databinding.FragmentStandingListBinding
import sam.frampton.parcferme.fragments.StandingListFragment.StandingType.*
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel
import sam.frampton.parcferme.viewmodels.StandingListViewModel

class StandingListFragment : Fragment() {

    enum class StandingType { DRIVER, CONSTRUCTOR, DEFAULT }

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val standingListViewModel: StandingListViewModel by activityViewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
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
        setSeason(args.season)
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        driverAdapter = DriverStandingAdapter { standing ->
            val directions = StandingListFragmentDirections
                .actionStandingListFragmentToDriverDetailFragment(
                    standing.driver,
                    standing.driver.getTitle(requireContext())
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
        binding.rvStandingList.setOrientedLayoutManager()
        binding.swipeRefreshStandingList.setOnRefreshListener { refresh() }
    }

    private fun setupChips() {
        binding.chipStandingListSeason.setOnClickListener {
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
                binding.rvStandingList.adapter = driverAdapter
            }
            CONSTRUCTOR -> {
                binding.rvStandingList.adapter = constructorAdapter
            }
        }
    }

    private fun setupObservers() {
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) setSeason(standingListViewModel.season ?: seasons.first())
        }
        seasonViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }

        standingListViewModel.driverStandingList.observe(
            viewLifecycleOwner,
            driverAdapter::submitList
        )
        standingListViewModel.driverStandingRefreshResult
            .observe(viewLifecycleOwner) { refreshResult ->
                refreshResult?.let {
                    mainActivityViewModel.setRefreshResult(refreshResult)
                    standingListViewModel.clearDriverStandingRefreshResult()
                }
            }
        standingListViewModel.driverStandingIsRefreshing.observe(viewLifecycleOwner) {
            isRefreshing()
        }

        standingListViewModel.constructorStandingList.observe(
            viewLifecycleOwner,
            constructorAdapter::submitList
        )
        standingListViewModel.constructorStandingRefreshResult
            .observe(viewLifecycleOwner) { refreshResult ->
                refreshResult?.let {
                    mainActivityViewModel.setRefreshResult(refreshResult)
                    standingListViewModel.clearConstructorStandingRefreshResult()
                }
            }
        standingListViewModel.constructorStandingIsRefreshing.observe(viewLifecycleOwner) {
            isRefreshing()
        }

        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                refresh()
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }

    private fun refresh() {
        seasonViewModel.refreshSeasons(true)
        standingListViewModel.refreshDriverStandings(true)
        standingListViewModel.refreshConstructorStandings(true)
    }

    private fun setSeason(season: Int) {
        if (season != -1) {
            binding.chipStandingListSeason.text = season.toString()
            if (standingListViewModel.season != season) {
                standingListViewModel.setSeason(season)
                standingListViewModel.refreshDriverStandings(false)
                standingListViewModel.refreshConstructorStandings(false)
            }
        }
    }

    private fun isRefreshing() {
        binding.swipeRefreshStandingList.isRefreshing =
            seasonViewModel.isRefreshing.value == true
                    || standingListViewModel.driverStandingIsRefreshing.value == true
                    || standingListViewModel.constructorStandingIsRefreshing.value == true
    }
}
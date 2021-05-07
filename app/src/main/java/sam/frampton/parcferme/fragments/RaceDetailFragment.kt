package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.QualifyingResultAdapter
import sam.frampton.parcferme.adapters.RaceResultAdapter
import sam.frampton.parcferme.databinding.FragmentRaceDetailBinding
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.RaceDetailViewModel

class RaceDetailFragment : Fragment() {

    private val raceDetailViewModel: RaceDetailViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val args: RaceDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentRaceDetailBinding
    private lateinit var raceResultAdapter: RaceResultAdapter
    private lateinit var qualifyingResultAdapter: QualifyingResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRaceDetailBinding.inflate(layoutInflater)
        setRace()
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setRace() {
        binding.race = args.race
        raceDetailViewModel.setRace(args.race)
        refresh(false)
    }

    private fun setupRecyclerView() {
        raceResultAdapter = RaceResultAdapter { result ->
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(
                    result.driver,
                    result.driver.getTitle(requireContext())
                )
            findNavController().navigate(action)
        }
        qualifyingResultAdapter = QualifyingResultAdapter { result ->
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(
                    result.driver,
                    result.driver.getTitle(requireContext())
                )
            findNavController().navigate(action)
        }
        binding.rvRaceDetail.adapter = raceResultAdapter
        binding.rvRaceDetail.setOrientedLayoutManager()
        binding.swipeRefreshRaceDetail.setOnRefreshListener { refresh(true) }
        binding.swipeRefreshRaceDetail.setColor()
    }

    private fun setupChips() {
        binding.chipGroupRaceDetail.setOnCheckedChangeListener { _, checkedId ->
            binding.rvRaceDetail.adapter =
                when (checkedId) {
                    R.id.chip_race_detail_race -> raceResultAdapter
                    R.id.chip_race_detail_qualifying -> qualifyingResultAdapter
                    else -> raceResultAdapter
                }
        }
    }

    private fun setupObservers() {
        raceDetailViewModel.raceResultList.observe(
            viewLifecycleOwner,
            raceResultAdapter::submitList
        )
        raceDetailViewModel.raceResultRefreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                raceDetailViewModel.clearRaceResultRefreshResult()
            }
        }
        raceDetailViewModel.raceResultIsRefreshing.observe(viewLifecycleOwner) { isRefreshing() }

        raceDetailViewModel.qualifyingResultList.observe(
            viewLifecycleOwner,
            qualifyingResultAdapter::submitList
        )
        raceDetailViewModel.qualifyingResultRefreshResult
            .observe(viewLifecycleOwner) { refreshResult ->
                refreshResult?.let {
                    mainActivityViewModel.setRefreshResult(refreshResult)
                    raceDetailViewModel.clearQualifyingResultRefreshResult()
                }
            }
        raceDetailViewModel.qualifyingResultIsRefreshing.observe(viewLifecycleOwner) {
            isRefreshing()
        }

        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                refresh(true)
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }

    private fun refresh(force: Boolean) {
        raceDetailViewModel.refreshRaceResults(force)
        raceDetailViewModel.refreshQualifyingResults(force)
    }

    private fun isRefreshing() {
        binding.swipeRefreshRaceDetail.isRefreshing =
            raceDetailViewModel.raceResultIsRefreshing.value == true
                    || raceDetailViewModel.qualifyingResultIsRefreshing.value == true
    }
}
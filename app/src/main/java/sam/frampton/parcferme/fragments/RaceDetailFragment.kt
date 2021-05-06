package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.QualifyingResultAdapter
import sam.frampton.parcferme.adapters.RaceResultAdapter
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.databinding.FragmentRaceDetailBinding
import sam.frampton.parcferme.viewmodels.RaceDetailViewModel

class RaceDetailFragment : Fragment() {

    private val raceDetailViewModel: RaceDetailViewModel by viewModels()
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
        binding.race = args.race
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        raceResultAdapter = RaceResultAdapter { result ->
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(
                    result.driver,
                    getDriverTitle(result.driver)
                )
            findNavController().navigate(action)
        }
        qualifyingResultAdapter = QualifyingResultAdapter { result ->
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(
                    result.driver,
                    getDriverTitle(result.driver)
                )
            findNavController().navigate(action)
        }
        binding.rvRaceDetail.adapter = raceResultAdapter
        binding.rvRaceDetail.setOrientedLayoutManager()
    }

    private fun getDriverTitle(driver: Driver): String {
        return driver.permanentNumber?.let {
            getString(R.string.driver_full_name_and_number, it, driver.givenName, driver.familyName)
        } ?: getString(R.string.driver_full_name, driver.givenName, driver.familyName)
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
        raceDetailViewModel.getRaceResults(args.race.season, args.race.round)
            .observe(viewLifecycleOwner, raceResultAdapter::submitList)
        raceDetailViewModel.getQualifyingResults(args.race.season, args.race.round)
            .observe(viewLifecycleOwner, qualifyingResultAdapter::submitList)
    }
}
package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.adapters.QualifyingResultAdapter
import sam.frampton.parcferme.adapters.RaceResultAdapter
import sam.frampton.parcferme.data.QualifyingResult
import sam.frampton.parcferme.data.RaceResult
import sam.frampton.parcferme.databinding.FragmentRaceDetailBinding
import sam.frampton.parcferme.viewmodels.RaceDetailViewModel

class RaceDetailFragment : Fragment() {

    private val raceDetailViewModel: RaceDetailViewModel by viewModels()
    private val args: RaceDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentRaceDetailBinding
    private lateinit var raceResultAdapter: RaceResultAdapter
    private lateinit var qualifyingResultAdapter: QualifyingResultAdapter
    private var raceResultList: LiveData<List<RaceResult>>? = null
    private var qualifyingResultList: LiveData<List<QualifyingResult>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRaceDetailBinding.inflate(layoutInflater)
        binding.race = args.race
        initialiseRecyclerView()
        initialiseSpinner()
        return binding.root
    }

    private fun initialiseRecyclerView() {
        raceResultAdapter = RaceResultAdapter {
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(it.driver)
            findNavController().navigate(action)
        }
        qualifyingResultAdapter = QualifyingResultAdapter {
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(it.driver)
            findNavController().navigate(action)
        }
        binding.rvRaceDetailResults.adapter = raceResultAdapter
    }

    private fun initialiseSpinner() {
        binding.spRaceDetailResultType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            binding.rvRaceDetailResults.adapter = raceResultAdapter
                            if (raceResultList == null) {
                                raceResultList = raceDetailViewModel.getRaceResults(
                                    args.race.season,
                                    args.race.round
                                )
                                raceResultList?.observe(viewLifecycleOwner) {
                                    raceResultAdapter.submitList(
                                        it
                                    )
                                }
                            }
                        }
                        1 -> {
                            binding.rvRaceDetailResults.adapter = qualifyingResultAdapter
                            if (qualifyingResultList == null) {
                                qualifyingResultList = raceDetailViewModel.getQualifyingResults(
                                    args.race.season,
                                    args.race.round
                                )
                                qualifyingResultList?.observe(viewLifecycleOwner) {
                                    qualifyingResultAdapter.submitList(
                                        it
                                    )
                                }
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }
}
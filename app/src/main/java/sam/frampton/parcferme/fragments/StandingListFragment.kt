package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import sam.frampton.parcferme.adapters.ConstructorStandingAdapter
import sam.frampton.parcferme.adapters.DriverStandingAdapter
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.databinding.FragmentStandingListBinding
import sam.frampton.parcferme.viewmodels.SeasonViewModel
import sam.frampton.parcferme.viewmodels.StandingListViewModel

class StandingListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val standingListViewModel: StandingListViewModel by viewModels()
    private lateinit var binding: FragmentStandingListBinding
    private lateinit var driverAdapter: DriverStandingAdapter
    private lateinit var constructorAdapter: ConstructorStandingAdapter
    private var driverStandingList: LiveData<List<DriverStanding>>? = null
    private var constructorStandingList: LiveData<List<ConstructorStanding>>? = null

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
        driverAdapter = DriverStandingAdapter {
            TODO()
        }
        constructorAdapter = ConstructorStandingAdapter {
            TODO()
        }
        binding.rvStandingListStandings.adapter = driverAdapter
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
                    driverStandingList?.removeObservers(viewLifecycleOwner)
                    driverStandingList = standingListViewModel.getDriverStandings(season)
                    driverStandingList?.observe(viewLifecycleOwner) {
                        driverAdapter.submitList(it)
                    }
                    constructorStandingList?.removeObservers(viewLifecycleOwner)
                    constructorStandingList = standingListViewModel.getConstructorStandings(season)
                    constructorStandingList?.observe(viewLifecycleOwner) {
                        constructorAdapter.submitList(it)
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
            }
        }
    }

    private fun initialiseTypeSpinner() {
        binding.spStandingListType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> binding.rvStandingListStandings.adapter = driverAdapter
                        1 -> binding.rvStandingListStandings.adapter = constructorAdapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }
}
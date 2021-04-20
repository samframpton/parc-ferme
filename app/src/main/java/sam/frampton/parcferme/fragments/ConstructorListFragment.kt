package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.ConstructorAdapter
import sam.frampton.parcferme.databinding.FragmentConstructorListBinding
import sam.frampton.parcferme.viewmodels.ConstructorListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class ConstructorListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val constructorListViewModel: ConstructorListViewModel by activityViewModels()
    private lateinit var binding: FragmentConstructorListBinding
    private lateinit var constructorAdapter: ConstructorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConstructorListBinding.inflate(layoutInflater)
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        constructorAdapter = ConstructorAdapter { constructor ->
            val directions = ConstructorListFragmentDirections
                .actionConstructorListFragmentToConstructorDetailFragment(
                    constructor,
                    constructor.name
                )
            findNavController().navigate(directions)
        }
        binding.rvConstructorListConstructors.adapter = constructorAdapter
    }

    private fun setupChips() {
        binding.chipConstructorListSeason.setOnClickListener {
            val directions = ConstructorListFragmentDirections
                .actionConstructorListFragmentToSeasonDialogFragment()
            findNavController().navigate(directions)
        }
    }

    private fun setupObservers() {
        seasonViewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            if (seasons.isNotEmpty()) {
                setSeason(constructorListViewModel.season ?: seasons.first())
            }
        }

        constructorListViewModel.constructorList.observe(viewLifecycleOwner) {
            constructorAdapter.submitList(it)
        }

        findNavController().getBackStackEntry(R.id.constructorListFragment).savedStateHandle
            .getLiveData<Int>(SeasonDialogFragment.SEASON_KEY).observe(viewLifecycleOwner) {
                setSeason(it)
            }
    }

    private fun setSeason(season: Int) {
        binding.chipConstructorListSeason.text = season.toString()
        if (constructorListViewModel.season != season) {
            constructorListViewModel.setSeason(season)
            constructorListViewModel.refreshConstructors(false)
        }
    }
}
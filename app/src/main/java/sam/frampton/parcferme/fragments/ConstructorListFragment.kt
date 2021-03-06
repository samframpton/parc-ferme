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
import sam.frampton.parcferme.adapters.ConstructorAdapter
import sam.frampton.parcferme.databinding.FragmentConstructorListBinding
import sam.frampton.parcferme.viewmodels.ConstructorListViewModel
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class ConstructorListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val constructorListViewModel: ConstructorListViewModel by activityViewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentConstructorListBinding
    private lateinit var constructorAdapter: ConstructorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConstructorListBinding.inflate(layoutInflater)
        seasonViewModel.refreshSeasons(false)
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
        binding.rvConstructorList.adapter = constructorAdapter
        binding.rvConstructorList.setOrientedLayoutManager()
        binding.swipeRefreshConstructorList.setOnRefreshListener { refresh() }
        binding.swipeRefreshConstructorList.setColor()
    }

    private fun setupChips() {
        binding.chipConstructorListSeason.setOnClickListener {
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
            if (seasons.isNotEmpty()) setSeason(constructorListViewModel.season ?: seasons.first())
        }
        seasonViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }
        constructorListViewModel.constructorList.observe(
            viewLifecycleOwner,
            constructorAdapter::submitList
        )
        constructorListViewModel.refreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                constructorListViewModel.clearRefreshResult()
            }
        }
        constructorListViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing() }
        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                refresh()
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }

    private fun refresh() {
        seasonViewModel.refreshSeasons(true)
        constructorListViewModel.refreshConstructors(true)
    }

    private fun setSeason(season: Int) {
        binding.chipConstructorListSeason.text = season.toString()
        if (constructorListViewModel.season != season) {
            constructorListViewModel.setSeason(season)
            constructorListViewModel.refreshConstructors(false)
        }
    }

    private fun isRefreshing() {
        binding.swipeRefreshConstructorList.isRefreshing =
            seasonViewModel.isRefreshing.value == true
                    || constructorListViewModel.isRefreshing.value == true
    }
}
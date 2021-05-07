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
import sam.frampton.parcferme.adapters.ConstructorDetailStandingAdapter
import sam.frampton.parcferme.databinding.FragmentConstructorDetailBinding
import sam.frampton.parcferme.viewmodels.ConstructorDetailViewModel
import sam.frampton.parcferme.viewmodels.MainActivityViewModel

class ConstructorDetailFragment : Fragment() {

    private val constructorDetailViewModel: ConstructorDetailViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val args: ConstructorDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentConstructorDetailBinding
    private lateinit var constructorAdapter: ConstructorDetailStandingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConstructorDetailBinding.inflate(layoutInflater)
        setConstructor()
        setupRecyclerView()
        setupObservers()
        return binding.root
    }

    private fun setConstructor() {
        binding.constructor = args.constructor
        constructorDetailViewModel.setConstructor(args.constructor)
        constructorDetailViewModel.refreshConstructorStandings(false)
    }

    private fun setupRecyclerView() {
        constructorAdapter = ConstructorDetailStandingAdapter {
            val action = ConstructorDetailFragmentDirections
                .actionConstructorDetailFragmentToStandingListFragment(
                    StandingListFragment.StandingType.CONSTRUCTOR,
                    it.season
                )
            findNavController().navigate(action)
        }
        binding.rvConstructorDetail.adapter = constructorAdapter
        binding.rvConstructorDetail.setOrientedLayoutManager()
        binding.swipeRefreshConstructorDetail.setOnRefreshListener {
            constructorDetailViewModel.refreshConstructorStandings(true)
        }
    }

    private fun setupObservers() {
        constructorDetailViewModel.standingList.observe(viewLifecycleOwner) { standings ->
            if (standings.isNotEmpty()) {
                constructorAdapter.submitList(standings)
                binding.constructorStandings = standings
                binding.executePendingBindings()
            }
        }
        constructorDetailViewModel.refreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                constructorDetailViewModel.clearRefreshResult()
            }
        }
        constructorDetailViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshConstructorDetail.isRefreshing = isRefreshing == true
        }
        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                constructorDetailViewModel.refreshConstructorStandings(true)
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }
}
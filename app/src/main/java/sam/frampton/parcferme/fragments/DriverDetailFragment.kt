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
import sam.frampton.parcferme.adapters.DriverDetailStandingAdapter
import sam.frampton.parcferme.data.StandingType
import sam.frampton.parcferme.databinding.FragmentDriverDetailBinding
import sam.frampton.parcferme.viewmodels.DriverDetailViewModel
import sam.frampton.parcferme.viewmodels.MainActivityViewModel

class DriverDetailFragment : Fragment() {

    private val driverDetailViewModel: DriverDetailViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val args: DriverDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDriverDetailBinding
    private lateinit var driverAdapter: DriverDetailStandingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverDetailBinding.inflate(layoutInflater)
        setDriver()
        setupRecyclerView()
        setupObservers()
        return binding.root
    }

    private fun setDriver() {
        binding.driver = args.driver
        driverDetailViewModel.setDriver(args.driver)
        driverDetailViewModel.refreshDriverStandings(false)
    }

    private fun setupRecyclerView() {
        driverAdapter = DriverDetailStandingAdapter { standing ->
            val action =
                DriverDetailFragmentDirections.actionDriverDetailFragmentToStandingListFragment(
                    StandingType.DRIVER,
                    standing.season
                )
            findNavController().navigate(action)
        }
        binding.rvDriverDetail.adapter = driverAdapter
        binding.rvDriverDetail.setOrientedLayoutManager()
        binding.swipeRefreshDriverDetail.setOnRefreshListener {
            driverDetailViewModel.refreshDriverStandings(true)
        }
        binding.swipeRefreshDriverDetail.setColor()
    }

    private fun setupObservers() {
        driverDetailViewModel.standingList.observe(viewLifecycleOwner) { standings ->
            if (standings.isNotEmpty()) {
                driverAdapter.submitList(standings)
                binding.driverStandings = standings
                binding.executePendingBindings()
            }
        }
        driverDetailViewModel.refreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                driverDetailViewModel.clearRefreshResult()
            }
        }
        driverDetailViewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshDriverDetail.isRefreshing = isRefreshing == true
        }
        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                driverDetailViewModel.refreshDriverStandings(true)
                mainActivityViewModel.clearMenuRefresh()
            }
        }
    }
}
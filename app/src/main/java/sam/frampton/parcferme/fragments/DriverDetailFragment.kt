package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.adapters.DriverDetailStandingAdapter
import sam.frampton.parcferme.databinding.FragmentDriverDetailBinding
import sam.frampton.parcferme.viewmodels.DriverDetailViewModel

class DriverDetailFragment : Fragment() {

    private val driverDetailViewModel: DriverDetailViewModel by viewModels()
    private val args: DriverDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDriverDetailBinding
    private lateinit var driverAdapter: DriverDetailStandingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverDetailBinding.inflate(layoutInflater)
        binding.driver = args.driver
        setupRecyclerView()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        driverAdapter = DriverDetailStandingAdapter {
            val action =
                DriverDetailFragmentDirections.actionDriverDetailFragmentToStandingListFragment(
                    StandingListFragment.StandingType.DRIVER,
                    it.season
                )
            findNavController().navigate(action)
        }
        binding.rvDriverDetail.adapter = driverAdapter
        binding.rvDriverDetail.setOrientedLayoutManager()
    }

    private fun setupObservers() {
        driverDetailViewModel.getDriverStandings(args.driver.driverId).observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                driverAdapter.submitList(it)
                binding.driverStandings = it
                binding.executePendingBindings()
            }
        }
    }
}
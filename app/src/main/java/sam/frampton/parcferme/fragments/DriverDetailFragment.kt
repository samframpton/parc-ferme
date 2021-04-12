package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.adapters.DriverDetailStandingAdapter
import sam.frampton.parcferme.databinding.FragmentDriverDetailBinding
import sam.frampton.parcferme.viewmodels.DriverDetailViewModel

class DriverDetailFragment : Fragment() {

    private val driverDetailViewModel: DriverDetailViewModel by viewModels()
    private val args: DriverDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDriverDetailBinding.inflate(layoutInflater)
        binding.driver = args.driver
        val adapter = DriverDetailStandingAdapter {
            TODO()
        }
        binding.rvDriverDetailStandings.adapter = adapter
        driverDetailViewModel.getDriverStandings(args.driver.driverId).observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }
}
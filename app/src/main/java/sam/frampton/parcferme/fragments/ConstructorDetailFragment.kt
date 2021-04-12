package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.frampton.parcferme.adapters.ConstructorDetailStandingAdapter
import sam.frampton.parcferme.databinding.FragmentConstructorDetailBinding
import sam.frampton.parcferme.viewmodels.ConstructorDetailViewModel

class ConstructorDetailFragment : Fragment() {

    private val constructorDetailViewModel: ConstructorDetailViewModel by viewModels()
    private val args: ConstructorDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentConstructorDetailBinding.inflate(layoutInflater)
        binding.constructor = args.constructor
        val adapter = ConstructorDetailStandingAdapter {
            val action = ConstructorDetailFragmentDirections
                .actionConstructorDetailFragmentToStandingListFragment(
                    StandingListFragment.StandingType.CONSTRUCTOR,
                    it.season
                )
            findNavController().navigate(action)
        }
        binding.rvConstructorDetailStandings.adapter = adapter
        constructorDetailViewModel.getConstructorStandings(args.constructor)
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        return binding.root
    }
}
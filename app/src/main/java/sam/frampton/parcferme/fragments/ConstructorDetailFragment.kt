package sam.frampton.parcferme.fragments

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.ConstructorDetailStandingAdapter
import sam.frampton.parcferme.databinding.FragmentConstructorDetailBinding
import sam.frampton.parcferme.viewmodels.ConstructorDetailViewModel


class ConstructorDetailFragment : Fragment() {

    private val constructorDetailViewModel: ConstructorDetailViewModel by viewModels()
    private val args: ConstructorDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentConstructorDetailBinding
    private lateinit var constructorAdapter: ConstructorDetailStandingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConstructorDetailBinding.inflate(layoutInflater)
        binding.constructor = args.constructor
        setupRecyclerView()
        setupObservers()
        return binding.root
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
        val dividerAttrs = intArrayOf(android.R.attr.listDivider)
        val styledAttrs = requireContext().obtainStyledAttributes(dividerAttrs)
        val divider = styledAttrs.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.margin_large)
        val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
        styledAttrs.recycle()
        val dividerDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerDecoration.setDrawable(insetDivider)
        binding.rvConstructorDetail.addItemDecoration(dividerDecoration)
    }

    private fun setupObservers() {
        constructorDetailViewModel.getConstructorStandings(args.constructor)
            .observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    constructorAdapter.submitList(it)
                    binding.constructorStandings = it
                    binding.executePendingBindings()
                }
            }
    }
}
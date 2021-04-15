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
import androidx.navigation.fragment.findNavController
import sam.frampton.parcferme.adapters.ConstructorAdapter
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.databinding.FragmentConstructorListBinding
import sam.frampton.parcferme.viewmodels.ConstructorListViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class ConstructorListFragment : Fragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val constructorListViewModel: ConstructorListViewModel by viewModels()
    private lateinit var binding: FragmentConstructorListBinding
    private lateinit var constructorAdapter: ConstructorAdapter
    private var constructorList: LiveData<List<Constructor>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConstructorListBinding.inflate(layoutInflater)
        initialiseRecyclerView()
        initialiseSpinner()
        return binding.root
    }

    private fun initialiseRecyclerView() {
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

    private fun initialiseSpinner() {
        val seasonList = ArrayList<Int>()
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, seasonList)
        binding.spConstructorListSeason.adapter = spinnerAdapter
        binding.spConstructorListSeason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val season = parent.getItemAtPosition(position) as Int
                    constructorList?.removeObservers(viewLifecycleOwner)
                    constructorList = constructorListViewModel.getConstructors(season)
                    constructorList?.observe(viewLifecycleOwner) {
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
}
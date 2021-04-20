package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sam.frampton.parcferme.adapters.SeasonAdapter
import sam.frampton.parcferme.databinding.FragmentSeasonDialogBinding
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class SeasonDialogFragment : BottomSheetDialogFragment() {

    private val seasonViewModel: SeasonViewModel by activityViewModels()

    companion object {
        const val SEASON_KEY = "season"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSeasonDialogBinding.inflate(layoutInflater)
        val seasonAdapter = SeasonAdapter {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(SEASON_KEY, it)
            dismiss()
        }
        binding.rvSeasonDialog.adapter = seasonAdapter
        seasonViewModel.seasons.observe(viewLifecycleOwner) {
            seasonAdapter.submitList(it)
        }
        return binding.root
    }
}
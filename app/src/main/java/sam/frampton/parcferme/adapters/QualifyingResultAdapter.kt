package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.QualifyingResult
import sam.frampton.parcferme.databinding.ListItemQualifyingResultBinding

class QualifyingResultAdapter(private val onClick: (QualifyingResult) -> Unit) :
    ListAdapter<QualifyingResult, QualifyingResultAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemQualifyingResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var qualifyingResult: QualifyingResult? = null

        init {
            binding.root.setOnClickListener { qualifyingResult?.let { onClick(it) } }
        }

        fun bind(qualifyingResult: QualifyingResult) {
            this.qualifyingResult = qualifyingResult
            binding.qualifyingResult = qualifyingResult
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<QualifyingResult>() {
        override fun areItemsTheSame(oldItem: QualifyingResult, newItem: QualifyingResult):
                Boolean {
            return oldItem.position == newItem.position
        }

        override fun areContentsTheSame(oldItem: QualifyingResult, newItem: QualifyingResult):
                Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemQualifyingResultBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
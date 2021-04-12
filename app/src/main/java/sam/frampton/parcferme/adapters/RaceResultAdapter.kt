package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.RaceResult
import sam.frampton.parcferme.databinding.ListItemRaceResultBinding

class RaceResultAdapter(private val onClick: (RaceResult) -> Unit) :
    ListAdapter<RaceResult, RaceResultAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemRaceResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var raceResult: RaceResult? = null

        init {
            binding.root.setOnClickListener { raceResult?.let { onClick(it) } }
        }

        fun bind(raceResult: RaceResult) {
            this.raceResult = raceResult
            binding.raceResult = raceResult
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<RaceResult>() {
        override fun areItemsTheSame(oldItem: RaceResult, newItem: RaceResult): Boolean {
            return oldItem.position == newItem.position
        }

        override fun areContentsTheSame(oldItem: RaceResult, newItem: RaceResult): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemRaceResultBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
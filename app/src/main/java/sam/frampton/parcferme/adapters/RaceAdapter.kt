package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.Race
import sam.frampton.parcferme.databinding.ListItemRaceBinding

class RaceAdapter(private val onClick: (Race) -> Unit) :
    ListAdapter<Race, RaceAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemRaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var race: Race? = null

        init {
            binding.root.setOnClickListener { race?.let { onClick(it) } }
        }

        fun bind(race: Race) {
            this.race = race
            binding.race = race
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Race>() {
        override fun areItemsTheSame(oldItem: Race, newItem: Race): Boolean {
            return oldItem.round == newItem.round
        }

        override fun areContentsTheSame(oldItem: Race, newItem: Race): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemRaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
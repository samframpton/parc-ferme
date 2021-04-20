package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.databinding.ListItemSeasonBinding

class SeasonAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<Int, SeasonAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemSeasonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var season: Int? = null

        init {
            binding.root.setOnClickListener { season?.let { onClick(it) } }
        }

        fun bind(season: Int) {
            this.season = season
            binding.season = season.toString()
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonAdapter.ViewHolder {
        val binding = ListItemSeasonBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeasonAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
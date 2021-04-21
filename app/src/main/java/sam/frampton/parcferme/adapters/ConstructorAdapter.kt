package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.databinding.ListItemConstructorBinding

class ConstructorAdapter(private val onClick: (Constructor) -> Unit) :
    ListAdapter<Constructor, ConstructorAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemConstructorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var constructor: Constructor? = null

        init {
            binding.root.setOnClickListener { constructor?.let { onClick(it) } }
        }

        fun bind(constructor: Constructor) {
            this.constructor = constructor
            binding.constructor = constructor
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Constructor>() {
        override fun areItemsTheSame(oldItem: Constructor, newItem: Constructor): Boolean {
            return oldItem.constructorId == newItem.constructorId
        }

        override fun areContentsTheSame(oldItem: Constructor, newItem: Constructor): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemConstructorBinding.inflate(
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
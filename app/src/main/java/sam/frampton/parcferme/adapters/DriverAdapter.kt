package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.databinding.ListItemDriverBinding

class DriverAdapter(private val onClick: (Driver) -> Unit) :
    ListAdapter<Driver, DriverAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var driver: Driver? = null

        init {
            binding.root.setOnClickListener { driver?.let { onClick(it) } }
        }

        fun bind(driver: Driver) {
            this.driver = driver
            binding.driver = driver
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Driver>() {
        override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
            return oldItem.driverId == newItem.driverId
        }

        override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDriverBinding.inflate(
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
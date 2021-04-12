package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.databinding.ListItemDriverSeasonStandingBinding

class DriverSeasonStandingAdapter(private val onClick: (DriverStanding) -> Unit) :
    ListAdapter<DriverStanding, DriverSeasonStandingAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: ListItemDriverSeasonStandingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var driverStanding: DriverStanding? = null

        init {
            binding.root.setOnClickListener { driverStanding?.let { onClick(it) } }
        }

        fun bind(driverStanding: DriverStanding) {
            this.driverStanding = driverStanding
            binding.driverStanding = driverStanding
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DriverStanding>() {
        override fun areItemsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem.driver.driverId == newItem.driver.driverId
        }

        override fun areContentsTheSame(oldItem: DriverStanding, newItem: DriverStanding): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemDriverSeasonStandingBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
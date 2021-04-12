package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.databinding.ListItemConstructorStandingBinding

class ConstructorStandingAdapter(private val onClick: (ConstructorStanding) -> Unit) :
    ListAdapter<ConstructorStanding, ConstructorStandingAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemConstructorStandingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var constructorStanding: ConstructorStanding? = null

        init {
            binding.root.setOnClickListener { constructorStanding?.let { onClick(it) } }
        }

        fun bind(constructorStanding: ConstructorStanding) {
            this.constructorStanding = constructorStanding
            binding.constructorStanding = constructorStanding
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ConstructorStanding>() {
        override fun areItemsTheSame(
            oldItem: ConstructorStanding,
            newItem: ConstructorStanding
        ): Boolean {
            return oldItem.constructor.constructorId == newItem.constructor.constructorId
        }

        override fun areContentsTheSame(
            oldItem: ConstructorStanding,
            newItem: ConstructorStanding
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemConstructorStandingBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
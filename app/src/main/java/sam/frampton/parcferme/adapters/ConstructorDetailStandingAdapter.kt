package sam.frampton.parcferme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.databinding.ListItemConstructorDetailStandingBinding

class ConstructorDetailStandingAdapter(private val onClick: (ConstructorStanding) -> Unit) :
    ListAdapter<ConstructorStanding, ConstructorDetailStandingAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemConstructorDetailStandingBinding) :
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
        ): Boolean = oldItem.season == newItem.season

        override fun areContentsTheSame(
            oldItem: ConstructorStanding,
            newItem: ConstructorStanding
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemConstructorDetailStandingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}
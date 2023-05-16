package app.agk.countriesinformation.countryinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.agk.countriesinformation.databinding.CountryItemInfoBinding

interface CountryItemClickListener {
    fun navigateToDetailView(countryName: String)
}

class DiffUtils : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}

class CountryInfoAdapter(val countryItemClickListener : CountryItemClickListener) : ListAdapter<String, CountryInfoAdapter.CountryInfoViewHolder>(DiffUtils()) {

    inner class CountryInfoViewHolder(
        private val binding : CountryItemInfoBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val item = getItem(layoutPosition)
                countryItemClickListener.navigateToDetailView(item)
            }
        }

        fun updateUI(name: String){
            binding.countryName.setText(name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryInfoViewHolder {
        val binding = CountryItemInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return CountryInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryInfoViewHolder, position: Int) {
        val item = getItem(position)
        holder.updateUI(item)
    }
}
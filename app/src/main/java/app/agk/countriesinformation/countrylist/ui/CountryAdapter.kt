package app.agk.countriesinformation.countrylist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.agk.countriesinformation.databinding.CountryItemInfoBinding

interface CountryItemClickListener {
    fun navigateToDetailView(countryName: String)
}

class CountryDiffUtil : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}

class CountryAdapter(private val countryItemClickListener : CountryItemClickListener)
    : ListAdapter<String, CountryAdapter.CountryViewHolder>(CountryDiffUtil()) {

    inner class CountryViewHolder(
        private val binding : CountryItemInfoBinding): ViewHolder(binding.root){

        @VisibleForTesting
        private lateinit var countryName: String

        init {
            binding.root.setOnClickListener {
                countryItemClickListener.navigateToDetailView(countryName)
            }
        }

        fun updateUI(name: String){
            countryName = name
            binding.countryName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountryItemInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val item = getItem(position)
        holder.updateUI(item)
    }
}
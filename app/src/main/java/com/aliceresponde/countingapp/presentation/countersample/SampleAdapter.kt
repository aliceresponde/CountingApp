package com.aliceresponde.countingapp.presentation.countersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.SampleItemBinding

class SampleAdapter(private val items: List<String>, val listener: (String) -> Unit) :
    RecyclerView.Adapter<SampleAdapter.SampleViewHolder>() {

    inner class SampleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = SampleItemBinding.bind(view)
        fun onBind(item: String) {
            binding.item.text = item
            binding.item.setOnClickListener { listener.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.sample_item, parent, false)
        return SampleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        holder.onBind(items[position])
    }
}

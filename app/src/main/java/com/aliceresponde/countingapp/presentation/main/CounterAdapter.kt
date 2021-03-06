package com.aliceresponde.countingapp.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliceresponde.countingapp.R
import com.aliceresponde.countingapp.databinding.CounterItemBinding
import com.aliceresponde.countingapp.domain.model.Counter
import java.util.*
import kotlin.collections.ArrayList

class CounterAdapter(
    private var data: MutableList<Counter> = mutableListOf(),
    private val selectedItems: MutableList<Counter> = mutableListOf(),
    private val callback: CounterAdapterListeners
) :
    RecyclerView.Adapter<CounterAdapter.CounterViewHolder>(), Filterable {

    private val differCallback = object : DiffUtil.ItemCallback<Counter>() {
        override fun areItemsTheSame(oldItem: Counter, newItem: Counter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Counter, newItem: Counter): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)
    var filteredList = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.counter_item, parent, false)
        return CounterViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    inner class CounterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CounterItemBinding.bind(view)
        fun onBind(counter: Counter) {
            binding.apply {
                title.text = counter.title

                if (isSelected(counter)) {
                    itemView.setBackgroundResource(R.drawable.selected_counter_background)
                    selectedIcon.visibility = VISIBLE
                    amountGroup.visibility = GONE
                } else {
                    amountGroup.visibility = VISIBLE
                    selectedIcon.visibility = GONE
                    itemView.setBackgroundResource(0)
                }

                amountTextView.text = counter.count.toString()
                decreaseView.setColorFilter(
                    if (counter.count == 0)
                        ContextCompat.getColor(itemView.context, R.color.grayColor)
                    else
                        ContextCompat.getColor(itemView.context, R.color.orangeColor)
                )

                increaseView.setOnClickListener {
                    callback.onIncreaseCounter(counter, adapterPosition)
                }

                decreaseView.setOnClickListener {
                    callback.onDecreaseCounterClicked(counter, adapterPosition)
                }

                root.setOnLongClickListener {
                    callback.onSelectedItem(counter, adapterPosition)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    private fun isSelected(counter: Counter) =
        selectedItems.contains(counter)

    fun update(counters: List<Counter>) {
        filteredList = counters.toMutableList()
        differ.submitList(counters)
        data = counters.toMutableList()
    }

    fun selectCounter(selectedCounters: List<Counter>) {
        selectedItems.clear()
        selectedItems.addAll(selectedCounters)
        notifyDataSetChanged()
    }

    fun removeSelectedCounters() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                filteredList = if (constraint.isNullOrEmpty()) data
                else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    data.filter {
                        it.title.toLowerCase(Locale.ROOT).contains(filterPattern)
                    }.toMutableList()
                }
                val results = FilterResults()
                results.values = filteredList
                callback.onFiltered(filteredList)
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                differ.submitList(results?.values as MutableList<Counter>?)
            }
        }
    }
}

interface CounterAdapterListeners {
    fun onIncreaseCounter(counter: Counter, position: Int)
    fun onDecreaseCounterClicked(counter: Counter, position: Int)
    fun onSelectedItem(counter: Counter, position: Int)
    fun onFiltered(list: List<Counter>)
}
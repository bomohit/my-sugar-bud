package com.bit.mysugarbud.ui.diets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.R

class FoodChoices(private val foodChoice: MutableList<FoodChoice>) :
    RecyclerView.Adapter<FoodChoices.ViewHolder>() {
    class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val Name : TextView = itemView.findViewById(R.id.diet_row_name)
        val info : TextView = itemView.findViewById(R.id.diet_row_info)
        val protein : TextView = itemView.findViewById(R.id.diet_row_prot)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diet_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodChoice[position]
        holder.Name.text = food.name
        holder.info.text = food.info
        holder.protein.text = food.protein

    }

    override fun getItemCount() = foodChoice.size

}

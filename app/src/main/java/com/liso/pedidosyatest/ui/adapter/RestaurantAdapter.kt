package com.liso.pedidosyatest.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.liso.pedidosyatest.model.Restaurant

class RestaurantAdapter(var restaurants: Restaurant) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){

    fun addRestaurants(newRestaurants: Restaurant) {
        restaurants.data.addAll(newRestaurants.data)
        notifyDataSetChanged()
    }

    fun updateRestaurants(newRestaurants: Restaurant) {
        restaurants = newRestaurants
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(container.context).inflate(android.R.layout.simple_list_item_1, container, false)

        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurantName = restaurants.data[position].name

        holder.textView.text = restaurantName
    }

    override fun getItemCount() = restaurants.data.size

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
}
package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ItemResidentialBinding
import com.jeuxdevelopers.estatepie.databinding.ItemVehicleBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse

class VehiclesAdapter(
    private val onItemClicked: (GetVehicleResponse.Data.Vehicle) -> Unit,
    private val onMenuItemClicked: (GetVehicleResponse.Data.Vehicle, Int) -> Unit,
) :
    ListAdapter<GetVehicleResponse.Data.Vehicle, VehiclesAdapter.AdsGridViewHolder>(DiffCallback) {

    inner class AdsGridViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GetVehicleResponse.Data.Vehicle) {

            binding.root.setOnClickListener { onItemClicked(item) }

            binding.root.setOnLongClickListener{
                onMenuItemClicked(item, adapterPosition)
                true
            }
            binding.tvVehicleName.text = item.vehicle_type
            binding.tvModel.text = item.model
            binding.tvColor.text = binding.root.resources.getString(R.string.color_txt) + item.color
            binding.tvLicensePlate.text = binding.root.resources.getString(R.string.licence_plate) + item.licence_plate

            if(item.vehicle_images.size > 0){

                Glide.with(binding.root).load(item.vehicle_images[0].image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_house_gray)
                    .error(R.drawable.ic_house_gray)
                    .dontAnimate()
                    .into(binding.houseImage)
            }else{

                binding.houseImage.setPadding(35,35,35,35)
            }

        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<GetVehicleResponse.Data.Vehicle>() {
        override fun areItemsTheSame(oldItem: GetVehicleResponse.Data.Vehicle, newItem: GetVehicleResponse.Data.Vehicle) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: GetVehicleResponse.Data.Vehicle, newItem: GetVehicleResponse.Data.Vehicle) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsGridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return AdsGridViewHolder(
            ItemVehicleBinding.bind(view)
        )
    }

    override fun onBindViewHolder(holder: AdsGridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}
package com.example.dream_ias.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dream_ias.databinding.WalletItemBinding

class WalletAdapter(val con: Context) : RecyclerView.Adapter<WalletAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: WalletItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(WalletItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 6
    }
}
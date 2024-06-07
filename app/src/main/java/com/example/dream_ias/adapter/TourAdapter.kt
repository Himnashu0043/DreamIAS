package com.example.dream_ias.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.dream_ias.databinding.TourItemBinding
import com.example.dream_ias.model.TourModel

class TourAdapter(private val con: Context, private val data_list: ArrayList<TourModel>) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: TourItemBinding = TourItemBinding.inflate(
            LayoutInflater.from(
                con
            ), container, false
        )

        binding.ivImg.setImageResource(data_list[position].img)


        container.addView(binding.root, 0)
        return binding.root
    }

    override fun getCount(): Int {
        return data_list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
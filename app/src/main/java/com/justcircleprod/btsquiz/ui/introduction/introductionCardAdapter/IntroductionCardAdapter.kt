package com.justcircleprod.btsquiz.ui.introduction.introductionCardAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.justcircleprod.btsquiz.databinding.IntroductionCardItemBinding

class IntroductionCardAdapter(
    private val cardResources: List<IntroductionCardResources>,
    private val onNextBtnClicked: () -> Unit,
    private val onPlayBtnClicked: () -> Unit
) :
    RecyclerView.Adapter<IntroductionCardAdapter.PagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            IntroductionCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = cardResources.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PagerViewHolder(private val binding: IntroductionCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val context = binding.root.context

            binding.title.text = context.getString(cardResources[position].title)

            if (adapterPosition == 1) {
                binding.titleIconCoins.visibility = View.VISIBLE
            } else {
                binding.titleIconCoins.visibility = View.GONE
            }

            binding.text.text = context.getString(cardResources[position].text)

            Glide
                .with(binding.root.context)
                .load(cardResources[position].gif)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.gif)

            if (adapterPosition == 2) {
                binding.nextBtn.visibility = View.GONE
                binding.playBtn.visibility = View.VISIBLE
            } else {
                binding.nextBtn.visibility = View.VISIBLE
                binding.playBtn.visibility = View.GONE
            }

            binding.nextBtn.setOnClickListener {
                onNextBtnClicked()
            }

            binding.playBtn.setOnClickListener {
                onPlayBtnClicked()
            }
        }
    }
}
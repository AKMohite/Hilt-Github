package com.ak.githilt.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.githilt.R
import com.ak.githilt.databinding.LoadStateFooterBinding

class RepositoryLoadStateAdapter(val retry : () -> Unit): LoadStateAdapter<RepositoryLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LoadStateFooterBinding>(inflater, R.layout.load_state_footer, parent, false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.footerRetryBtn.setOnClickListener {
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState) {
            binding.apply {
                footerProgressBar.isVisible = loadState is LoadState.Loading
                footerRetryBtn.isVisible = loadState !is LoadState.Loading
                footerErrorText.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}
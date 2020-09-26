package com.ak.githilt.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.githilt.R
import com.ak.githilt.databinding.RepoItemBinding
import com.ak.githilt.model.Repo

class RepositoryAdapter: RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val repoList: MutableList<Repo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RepoItemBinding>(inflater, R.layout.repo_item, parent, false)

        return RepositoryViewHolder(binding)
    }

    override fun getItemCount(): Int = repoList.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.binding.apply {
            repo = repoList[position]
        }
    }

    fun updateRepoList(repoItems: List<Repo>){
        val previousSize = repoList.size
        repoList.addAll(repoItems)
        notifyItemRangeChanged(previousSize, repoItems.size)
    }

    class RepositoryViewHolder(val binding: RepoItemBinding): RecyclerView.ViewHolder(binding.root)

}
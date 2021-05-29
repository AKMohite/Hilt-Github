package com.ak.githilt.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.githilt.R
import com.ak.githilt.databinding.RepoItemBinding
import com.ak.githilt.model.Repo

class RepositoryAdapter: PagingDataAdapter<Repo, RepositoryAdapter.RepositoryViewHolder>(REPOSITORY_COMPARATOR) {

    companion object{
        private val REPOSITORY_COMPARATOR = object : DiffUtil.ItemCallback<Repo>(){
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem

        }
    }

    private val repoList: MutableList<Repo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RepoItemBinding>(inflater, R.layout.repo_item, parent, false)

        return RepositoryViewHolder(binding)
    }

//    override fun getItemCount(): Int = repoList.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.binding.apply {
            repo = getItem(position)
        }
    }

    fun addNewRepoList(repoItems: List<Repo>){
        val previousSize = repoList.size
        repoList.addAll(repoItems)
        notifyItemRangeChanged(previousSize, repoItems.size)
    }

    class RepositoryViewHolder(val binding: RepoItemBinding): RecyclerView.ViewHolder(binding.root)

}
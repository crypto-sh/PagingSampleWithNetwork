package com.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.paging.databinding.RepoViewItemBinding
import com.paging.model.RepoSearch

typealias clickRepo = (RepoSearch) -> Unit

class ReposAdapter(private val callback : clickRepo) : PagedListAdapter<RepoSearch, ReposAdapter.RepoViewHolder>(REPO_COMPARE) {

    companion object {
        private var REPO_COMPARE = object : DiffUtil.ItemCallback<RepoSearch>(){
            override fun areItemsTheSame(old : RepoSearch, new : RepoSearch): Boolean = old.fullName == new.fullName
            override fun areContentsTheSame(p0: RepoSearch, p1: RepoSearch): Boolean  = p0 == p1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = RepoViewItemBinding.inflate(inflater)
        return RepoViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position : Int) {
        val item = getItem(position)
        if (item != null)
            holder.bindData(item,callback)
    }

    class RepoViewHolder(private val repoView : RepoViewItemBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(repoView.root) {
        fun bindData(repo : RepoSearch, callback : clickRepo){
            repoView.repo = repo
            repoView.constraintLayout.setOnClickListener {
                callback(repo)
            }
        }
    }
}
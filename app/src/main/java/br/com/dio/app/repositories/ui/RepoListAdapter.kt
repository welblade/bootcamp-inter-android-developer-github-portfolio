package br.com.dio.app.repositories.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.databinding.ItemRepoBinding
import com.bumptech.glide.Glide

class RepoListAdapter: ListAdapter<Repo, RepoListAdapter.ViewHolder>(DiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val item: ItemRepoBinding): RecyclerView.ViewHolder(item.root) {
        fun bind(repo :Repo){
            item.tvRepoName.text = repo.name
            item.tvRepoDescription.text = repo.description
            item.tvRepoLanguage.text = repo.language
            item.cpStar.text = repo.stargazersCount.toString()
            Glide.with(item.root.context)
                .load(repo.owner.avatarURL)
                .into(item.ivOwner)
        }
    }
    class DiffCallBack : DiffUtil.ItemCallback<Repo>(){
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
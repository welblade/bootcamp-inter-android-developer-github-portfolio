package br.com.dio.app.repositories.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.databinding.ItemRepoBinding
import com.bumptech.glide.Glide

class RepoListAdapter: ListAdapter<Repo, RepoListAdapter.ViewHolder>(DiffCallBack()) {
    var onRemovePositionZeroItemListener: (Boolean)-> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if(holder.adapterPosition == 0){
            onRemovePositionZeroItemListener(true)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if(holder.adapterPosition == 1){
            onRemovePositionZeroItemListener(false)
        }
    }

    class ViewHolder(private val item: ItemRepoBinding): RecyclerView.ViewHolder(item.root) {
        fun bind(repo :Repo){
            item.tvRepoName.text = repo.name
            item.tvRepoDescription.text = repo.description
            item.tvRepoLanguage.text = repo.language
            item.cpStar.text = repo.stargazersCount.toString()
            val url = "https://ui-avatars.com/api/?background=random&name=${repo.name}"
            Glide.with(item.root.context)
                .load(Uri.parse(url))
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
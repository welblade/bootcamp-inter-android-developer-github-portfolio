package br.com.dio.app.repositories.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.animation.doOnEnd
import br.com.dio.app.repositories.R
import br.com.dio.app.repositories.core.createDialog
import br.com.dio.app.repositories.core.createProgressDialog
import br.com.dio.app.repositories.core.format
import br.com.dio.app.repositories.core.hideSoftKeyboard
import br.com.dio.app.repositories.data.model.Owner
import br.com.dio.app.repositories.databinding.ActivityMainBinding
import br.com.dio.app.repositories.presentation.MainViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val mainViewModel: MainViewModel by viewModel()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dialog by lazy { createProgressDialog()}
    private val adapter = RepoListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.rvRepoList.adapter = adapter
        bindUserProfile()
        setListeners()
        mainViewModel.repos.observe(this) {
            when(it){
                is MainViewModel.State.Error -> {
                    dialog.dismiss()
                }
                MainViewModel.State.Loading -> {
                    dialog.show()
                }
                is MainViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(it.list)
                }
            }
        }
    }
    private fun bindUserProfile(){
        mainViewModel.owner.observe(this) {
            when(it){
                is MainViewModel.OwnerState.Error -> {
                    createDialog {
                        setMessage(getString(R.string.error, it.error.message))
                    }.show()
                    dialog.dismiss()
                }
                MainViewModel.OwnerState.Loading -> {
                    dialog.show()
                }
                is MainViewModel.OwnerState.Success -> {
                    val owner = it.owner
                    dialog.dismiss()
                    binding.toolbar.collapseActionView()
                    binding.layoutEmptyState.root.visibility = View.GONE
                    setOwnerAvatar(owner.avatarURL)
                    binding.layoutProfile.tvOwnerName.text = owner.name
                    binding.layoutProfileCompact.tvOwnerName.text = owner.name
                    setOwnerBio(owner.bio)
                    binding.layoutProfile.tvOwnerLogin.text = getString(R.string.login, owner.login)
                    binding.layoutProfileCompact.tvOwnerLogin.text = getString(R.string.login, owner.login)
                    setOwnerCreatedAt(owner.createdAt)
                    setOwnerLocation(owner.location)
                    setOwnerCompany(owner.company)
                    binding.layoutProfile.tvOwnerHireable.visibility =
                        if(owner.hireable) View.VISIBLE else View.GONE
                    setOwnerTwitter(owner.twitterUsername)
                    binding.layoutProfile.contactButtonsSeparator.visibility =
                        if(ownerHasTwitterAndEmail(owner)) View.VISIBLE else View.GONE
                    setOwnerEmail(owner.email)
                    binding.layoutProfile.tvOwnerFollowers.text = getString(R.string.n_followers, owner.followers)
                    binding.layoutProfileCompact.tvOwnerFollowers.text = getString(R.string.n_followers, owner.followers)
                    binding.layoutProfile.tvOwnerFollowing.text = getString(R.string.n_following, owner.following)
                    binding.layoutProfileCompact.tvOwnerRepositories.text = getString(R.string.n_repositories, owner.publicRepos)
                }
            }
        }
    }

    private fun ownerHasTwitterAndEmail(owner: Owner) =
        !(owner.email.isNullOrBlank() || owner.twitterUsername.isNullOrBlank())

    private fun setOwnerCreatedAt(date: Date) {
        binding.layoutProfile.tvOwnerCreatedAt.text = getString(
            R.string.member_since,
            date.format("MMM"),
            date.format("dd"),
            date.format("yyyy"),
        )
    }

    private fun setOwnerLocation(ownerLocation: String?) {
        if (ownerLocation.isNullOrBlank()) {
            binding.layoutProfile.tvOwnerLocation.visibility = View.GONE
            binding.layoutProfile.ivOwnerLocation.visibility = View.GONE
        } else {
            binding.layoutProfile.tvOwnerLocation.visibility = View.VISIBLE
            binding.layoutProfile.ivOwnerLocation.visibility = View.VISIBLE
            binding.layoutProfile.tvOwnerLocation.text = ownerLocation
        }
    }

    private fun setOwnerCompany(ownerCompany: String?) {
        if (ownerCompany.isNullOrBlank()) {
            binding.layoutProfile.locationSeparator.visibility = View.GONE
            binding.layoutProfile.tvOwnerCompany.visibility = View.GONE
            binding.layoutProfile.ivOwnerCompany.visibility = View.GONE
        } else {
            binding.layoutProfile.locationSeparator.visibility = View.VISIBLE
            binding.layoutProfile.tvOwnerCompany.visibility = View.VISIBLE
            binding.layoutProfile.ivOwnerCompany.visibility = View.VISIBLE
            binding.layoutProfile.tvOwnerCompany.text = ownerCompany
        }
    }

    private fun setOwnerTwitter(ownerTwitterUsername: String?) {
        if (ownerTwitterUsername.isNullOrBlank()) {
            binding.layoutProfile.ivTwitter.visibility = View.GONE
        } else {
            binding.layoutProfile.ivTwitter.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/${ownerTwitterUsername}")
                    )
                    startActivity(browserIntent)
                }
            }
        }
    }

    private fun setOwnerEmail(ownerEmail: String?) {
        if (ownerEmail.isNullOrBlank()) {
            binding.layoutProfile.ivMail.visibility = View.GONE
        } else {
            binding.layoutProfile.ivMail.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    val mailIntent = Intent(
                        Intent.ACTION_SENDTO
                    )
                    mailIntent.data = Uri.parse("mailto:${ownerEmail}")
                    startActivity(Intent.createChooser(mailIntent, getString(R.string.send_mail)))
                }
            }
        }
    }

    private fun setOwnerBio(ownerBio: String?) {
        if (ownerBio.isNullOrBlank()) {
            binding.layoutProfile.tvOwnerBio.visibility = View.GONE
            binding.layoutProfileCompact.tvOwnerBio.visibility = View.GONE
        } else {
            binding.layoutProfile.tvOwnerBio.visibility = View.VISIBLE
            binding.layoutProfile.tvOwnerBio.text = ownerBio
            binding.layoutProfileCompact.tvOwnerBio.visibility = View.VISIBLE
            binding.layoutProfileCompact.tvOwnerBio.text = ownerBio
        }
    }

    private fun setOwnerAvatar(avatarURL: String) {
        Glide.with(binding.root.context)
            .load(Uri.parse(avatarURL))
            .into(binding.layoutProfile.ivOwnerAvatar)
        Glide.with(binding.root.context)
            .load(Uri.parse(avatarURL))
            .into(binding.layoutProfileCompact.ivOwnerAvatar)
    }

    private fun setListeners() {
        adapter.onRemovePositionZeroItemListener = { isPositionZeroItemRemovedFromList ->
            val profileView = binding.layoutProfile.layoutProfileRoot
            if(isPositionZeroItemRemovedFromList){
                ObjectAnimator.ofPropertyValuesHolder(
                    profileView,
                    PropertyValuesHolder.ofFloat("scaleX", 0.5f),
                    PropertyValuesHolder.ofFloat("scaleY", 0.5f),
                    PropertyValuesHolder.ofFloat("translationY", -32f),
                    PropertyValuesHolder.ofFloat("translationX", -128f)
                ).apply {
                    duration = 100
                    start()
                    doOnEnd {
                        profileView.visibility = View.GONE
                    }
                }
                //binding.layoutProfile.layoutProfileRoot.visibility = View.GONE
                binding.layoutProfileCompact.layoutProfileCompactRoot.visibility = View.VISIBLE
            }else{
                binding.layoutProfileCompact.layoutProfileCompactRoot.visibility = View.GONE
                binding.layoutProfile.layoutProfileRoot.visibility = View.VISIBLE
                ObjectAnimator.ofPropertyValuesHolder(
                    profileView,
                    PropertyValuesHolder.ofFloat("scaleX", 1f),
                    PropertyValuesHolder.ofFloat("scaleY", 1f),
                    PropertyValuesHolder.ofFloat("translationY", 0f),
                    PropertyValuesHolder.ofFloat("translationX", 0f)
                ).apply {
                    duration = 100
                    start()
                }

            }
        }
        binding.layoutEmptyState.btSearch.setOnClickListener {
            val menuItem = binding.toolbar.menu.findItem(R.id.action_search)
            menuItem.expandActionView()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.root.hideSoftKeyboard()
        query?.let { mainViewModel.getRepoList(it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange: $newText")
        return false
    }

    companion object{
        const val TAG = "OnQueryTextListener"
    }

}
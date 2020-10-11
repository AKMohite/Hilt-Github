package com.ak.githilt.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.githilt.R
import com.ak.githilt.databinding.FragmentHomePageBinding
import com.ak.githilt.model.Repo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomePageFragment: Fragment() {

    lateinit var binding: FragmentHomePageBinding

    private val viewModel: RepositoryViewModel by viewModels()

    private val repoAdapter = RepositoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()

        setHasOptionsMenu(true)
    }

    private fun initViews() {
        binding.apply {
            repoList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = repoAdapter.withLoadStateFooter(
                    footer = RepositoryLoadStateAdapter { repoAdapter.retry() }
                )
            }

            retryButton.setOnClickListener { repoAdapter.retry() }
        }

        repoAdapter.addLoadStateListener { loadState: CombinedLoadStates ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                repoList.isVisible = loadState.source.refresh is LoadState.NotLoading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                errorText.isVisible = loadState.source.refresh is LoadState.Error

//                empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && repoAdapter.itemCount < 1){
                    repoList.isVisible = false
                    errorText.isVisible = true
                    errorText.text = "No repos found"
                } else{
                    errorText.isVisible = false
                }
            }
        }
    }

    private fun subscribeObservers(){
        /*viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<List<Repo>> -> {
                    displayProgressBar(false)
                    loadData(dataState.data)
                }

                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }

                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })*/

        viewModel.repos.observe(viewLifecycleOwner, Observer { repoPagingData: PagingData<Repo> ->
            repoAdapter.submitData(viewLifecycleOwner.lifecycle, repoPagingData)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_home, menu)

        val searchBtn = menu.findItem(R.id.menu_search)
        val searchView = searchBtn.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    binding.repoList.scrollToPosition(0)
                    viewModel.searchRepo(query)
                    searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun displayError(message: String?){
        Toast.makeText(context, message ?: "Unknown Error", Toast.LENGTH_LONG).show()
    }

    private fun displayProgressBar(isVisible: Boolean){
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadData(repos: List<Repo>){

        repoAdapter.addNewRepoList(repos)
    }
}
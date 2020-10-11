package com.ak.githilt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
//        viewModel.setStateEvent(RepoStateEvent.GetRepoEvents("android", 1)) // TODO pagination
    }

    private fun initViews() {
        repo_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = repoAdapter
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
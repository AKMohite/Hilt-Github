package com.ak.githilt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ak.githilt.R
import com.ak.githilt.model.Repo
import com.ak.githilt.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    private val viewModel: RepositoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeObservers()
        viewModel.setStateEvent(RepoStateEvent.GetRepoEvents)
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer { dataState ->
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
        })
    }

    private fun displayError(message: String?){
        if (message != null)
            text.text = message
        else
            text.text = "Unknown Error"
    }

    private fun displayProgressBar(isVisible: Boolean){
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadData(repos: List<Repo>){
        val sb = StringBuilder("")
        for (repo in repos){
            sb.append("${repo.id} ==> ${repo.fullName}\n")
        }
        text.text = sb.toString()
    }
}

package com.paging.ui


import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.paging.R
import com.paging.adapter.ReposAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var adapter = ReposAdapter {
        Toast.makeText(this@MainActivity,it.fullName,Toast.LENGTH_LONG).show()
    }

    private lateinit var viewModel : MainActivityViewModel

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"
    }

    override fun onCreate(bundle : Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this,MainActivityViewModel.Factory()).get(MainActivityViewModel::class.java)
        initRecycler()
        val query = bundle?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchRepositoeies(query)
        initSearch(query)
    }

    fun initRecycler(){
        list.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )
        list.adapter        = adapter
        viewModel.network.observe(this, Observer {

        })
        viewModel.data.observe(this, Observer {
            adapter.submitList(it!!)
        })
    }
    private fun initSearch(query : String) {
        search_repo.setText(query)
        search_repo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        search_repo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        search_repo.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                viewModel.searchRepositoeies(it.toString())
                adapter.submitList(null)
            }
        }
    }
}





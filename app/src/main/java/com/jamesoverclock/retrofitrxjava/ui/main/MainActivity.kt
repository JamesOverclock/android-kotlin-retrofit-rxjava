package com.jamesoverclock.retrofitrxjava.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.SearchView
import com.jamesoverclock.retrofitrxjava.R
import com.jamesoverclock.retrofitrxjava.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    val presenter: MainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listUsers.layoutManager = LinearLayoutManager(this)
        listUsers.itemAnimator = DefaultItemAnimator()

        searchUsers.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                presenter.searchUser(text)
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                return false
            }
        })
    }

    override fun setAdapterData(items: List<User>) {
        listUsers.adapter = MainAdapter(this, items)
    }

}

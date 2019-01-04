package com.jamesoverclock.retrofitrxjava.ui.main

import com.jamesoverclock.retrofitrxjava.models.User

interface MainView {

    fun setAdapterData(items: List<User>)
}
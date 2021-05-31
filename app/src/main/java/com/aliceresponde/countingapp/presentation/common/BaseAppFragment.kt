package com.aliceresponde.countingapp.presentation.common

import androidx.fragment.app.Fragment
import com.aliceresponde.countingapp.data.remote.NetworkConnection
import javax.inject.Inject

open class BaseAppFragment(layoutId: Int) : Fragment(layoutId), BaseFragment {
    @Inject
    lateinit var networkConnection: NetworkConnection
}
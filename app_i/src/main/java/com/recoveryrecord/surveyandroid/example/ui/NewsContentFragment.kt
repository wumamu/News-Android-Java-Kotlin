package com.recoveryrecord.surveyandroid.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.sqlite.DragObj
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj
import com.recoveryrecord.surveyandroid.example.util.SimpleGestureListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsContentFragment : Fragment(), SimpleGestureListener {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mParam3: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParam1 = it.getString(Constants.TRIGGER_BY_KEY, "")
            mParam2 = it.getString(Constants.NEWS_ID_KEY, "")
            mParam3 = it.getString(Constants.NEWS_MEDIA_KEY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.empty_fragment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): EmptyFragment {
            return EmptyFragment()
        }
    }

    override fun onSwipe(
        direction: Int,
        flingObj: FlingObj,
    ) {
        TODO("Not yet implemented")
    }

    override fun onOnePoint(dragObj: DragObj) {
        TODO("Not yet implemented")
    }
}

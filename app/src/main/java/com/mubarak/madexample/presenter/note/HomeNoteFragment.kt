package com.mubarak.madexample.presenter.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentHomeNoteBinding

class HomeNoteFragment : Fragment() {

    private lateinit var binding: FragmentHomeNoteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeNoteBinding.inflate(
            inflater,container,false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolBarHome.setNavigationOnClickListener {
            val drawerLayout =requireActivity().findViewById<DrawerLayout>(R.id.main_drawer_layout)
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}
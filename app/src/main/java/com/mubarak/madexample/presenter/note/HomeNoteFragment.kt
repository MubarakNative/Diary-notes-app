package com.mubarak.madexample.presenter.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mubarak.madexample.R
import com.mubarak.madexample.adapter.HomeNoteItemAdapter
import com.mubarak.madexample.databinding.FragmentHomeNoteBinding
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeNoteFragment : Fragment() {

    private lateinit var binding: FragmentHomeNoteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val rootView = inflater.inflate(
            R.layout.fragment_home_note,
            container,
            false
        )
        binding = FragmentHomeNoteBinding.bind(
          rootView
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpHomeRecyclerView()

         val homeViewModel: HomeNoteViewModel by viewModels()

        val homeAdapter by lazy {
            HomeNoteItemAdapter()
        }

        binding.homeNoteList.adapter = homeAdapter
        homeAdapter.submitList(homeViewModel.getAllNotes.value)

        binding.toolBarHome.setNavigationOnClickListener {
            requireView().openNavDrawer(requireActivity())
        }

    }

    private fun setUpHomeRecyclerView(){
        binding.apply {
            homeNoteList.layoutManager = LinearLayoutManager(
                requireContext()
            )
            homeNoteList.setHasFixedSize(true)
        }

    }

    private fun setUpFab(){

    }

}
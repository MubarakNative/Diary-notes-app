package com.mubarak.madexample.presenter.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mubarak.madexample.R
import com.mubarak.madexample.adapter.HomeNoteItemAdapter
import com.mubarak.madexample.data.Note
import com.mubarak.madexample.databinding.FragmentHomeNoteBinding
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeNoteFragment : Fragment() {

    private lateinit var binding: FragmentHomeNoteBinding
    private val homeViewModel: HomeNoteViewModel by viewModels()
     var draggedNoteId: Int?= null
    lateinit var draggedNote:Note
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
        ).apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeAdapter by lazy {
            HomeNoteItemAdapter()
        }
        setUpHomeRecyclerView()
        navigateToAddEditFragment()

       /* homeViewModel.getAllNote.observe(viewLifecycleOwner) {
            homeAdapter.submitList(it)
            binding.homeNoteList.adapter = homeAdapter
        }*/
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.getAllNote.collect{
                    homeAdapter.submitList(it)
                    binding.homeNoteList.adapter = homeAdapter
                }
            }
        }


        binding.toolBarHome.setNavigationOnClickListener {
            requireView().openNavDrawer(requireActivity())
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // draggedNoteId = homeAdapter.currentList[viewHolder.adapterPosition].id

                draggedNote = homeAdapter.currentList[viewHolder.adapterPosition]
             //   homeViewModel.deleteNoteById(draggedNoteId!!)
                homeViewModel.deleteNote(draggedNote)

                /*Snackbar.make(binding.root,"Note deleted",Snackbar.LENGTH_SHORT).setAction(
                 ""
                ){

                }.show() // test not to call this in this.*/

            }

        }).attachToRecyclerView(binding.homeNoteList)

        homeViewModel.noteDeletedEvent.observe(viewLifecycleOwner){ event ->
            event?.getContentIfNotHandled()?.let {
                Snackbar.make(requireView(),it,Snackbar.LENGTH_SHORT).setAction(
                    "UNDO"
                ){
                    homeViewModel.undoDeletedNote(draggedNote)
                }.show()
            }
        }

    }

    private fun setUpHomeRecyclerView() {
        binding.apply {
            homeNoteList.layoutManager = LinearLayoutManager(
                requireContext()
            )
            homeNoteList.setHasFixedSize(true)
        }
    }

    private fun navigateToAddEditFragment() {
        binding.fabCreateNote.setOnClickListener {
            val action = HomeNoteFragmentDirections.actionHomeNoteFragmentToActionNoteFragment(
                null,
                null,
                0
            )
            findNavController().navigate(action)
        }
    }

}
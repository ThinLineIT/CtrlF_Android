package com.thinlineit.ctrlf.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thinlineit.ctrlf.databinding.FragmentNotesBinding
import com.thinlineit.ctrlf.util.LoadingDialog

class NotesFragment : Fragment() {
    private val noteViewModel by viewModels<NotesViewModel>()
    private val noteAdapter = NotesAdapter { noteId ->
        this.findNavController().navigate(
            NotesFragmentDirections.actionNotesFragmentToPageActivity(noteId)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNotesBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.noteViewModel = this@NotesFragment.noteViewModel
            lifecycleOwner = this@NotesFragment
            noteListRecyclerView.adapter = noteAdapter
            addNoteBtn.setOnClickListener {
                NoteCreateClickListener(
                    requireContext(),
                    this@NotesFragment.noteViewModel
                ).onCreateClick()
            }
        }

        noteViewModel.alertLiveData.observe(viewLifecycleOwner) {
            // TODO: Check if response body is empty
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            Log.e("loadNote Exception", it)
        }
        val loadingDialog = LoadingDialog(this.requireContext())

        noteViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.show()
            else loadingDialog.dismiss()
        }
        return binding.root
    }
}

package com.gitz.simplenotes.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gitz.simplenotes.R
import com.gitz.simplenotes.adapter.NoteAdapter
import com.gitz.simplenotes.model.Note
import com.gitz.simplenotes.model.Resource
import com.gitz.simplenotes.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var refreshQuoteButton: ImageButton
    private lateinit var progressBarQuote: ProgressBar
    private lateinit var noteAdapter: NoteAdapter

    private lateinit var quoteTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        addNoteButton = findViewById(R.id.addNoteButton)
        quoteTextView = findViewById(R.id.quoteTextView)
        refreshQuoteButton = findViewById(R.id.refreshQuoteButton)
        progressBarQuote = findViewById(R.id.progressBarQuote)

        setupRecyclerView()

        setupItemTouchHelper()

        noteViewModel.fetchRandomQuote()

        setupObserver()

        addNoteButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            addEditNoteLauncher.launch(intent)
        }

        refreshQuoteButton.setOnClickListener {
            noteViewModel.fetchRandomQuote()
        }

    }

    private fun setupObserver() {
        noteViewModel.allNotes.observe(this) { notes ->
            notes?.let {
                noteAdapter.submitList(notes)
            }
        }

        noteViewModel.quote.observe(this) { quoteResponse ->
            when (quoteResponse) {
                is Resource.Loading -> {
                    progressBarQuote.visibility = View.VISIBLE
                    quoteTextView.visibility = View.GONE
                }

                is Resource.Success -> {
                    progressBarQuote.visibility = View.GONE
                    quoteTextView.visibility = View.VISIBLE
                    val quoteData = quoteResponse.data

                    if (quoteData != null) {
                        val quoteText = quoteData.quote ?: "No quote"
                        val authorText = quoteData.author ?: "No author"
                        quoteTextView.text = "\"$quoteText\" - $authorText"
                    } else {
                        quoteTextView.text = "Quote data is missing"
                    }

                }

                is Resource.Empty -> {
                    progressBarQuote.visibility = View.GONE
                    quoteTextView.visibility = View.VISIBLE
                    quoteTextView.text = "No quote"
                }

                is Resource.Error -> {
                    progressBarQuote.visibility = View.GONE
                    quoteTextView.visibility = View.VISIBLE
                    quoteTextView.text = "Failed to load quote."
                }

            }
        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val noteToDelete = noteAdapter.currentList[position]
                noteViewModel.delete(noteToDelete)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(notesRecyclerView)
    }


    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onItemClicked = object : (Note) -> Unit {
                override fun invoke(selectedNote: Note) {
                    val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                    intent.putExtra(AddEditNoteActivity.EXTRA_ID, selectedNote.id)
                    intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, selectedNote.title)
                    intent.putExtra(AddEditNoteActivity.EXTRA_CONTENT, selectedNote.content)
                    addEditNoteLauncher.launch(intent)
                }
            }
        )
        notesRecyclerView.adapter = noteAdapter
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private val addEditNoteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Succesfully added note", Toast.LENGTH_SHORT).show()
            }
        }
}
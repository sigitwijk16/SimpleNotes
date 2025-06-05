package com.gitz.simplenotes.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.gitz.simplenotes.R
import com.gitz.simplenotes.model.Note
import com.gitz.simplenotes.viewmodel.NoteViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "com.gits.simplenotes.EXTRA_ID"
        const val EXTRA_TITLE = "com.gitz.simplenotes.EXTRA_TITLE"
        const val EXTRA_CONTENT = "com.gitz.simplenotes.EXTRA_CONTENT"
    }

    private lateinit var noteTitleEditText: TextInputEditText
    private lateinit var noteContentEditText: TextInputEditText
    private lateinit var saveNoteButton: Button

    private val noteViewModel: NoteViewModel by viewModels()
    private var currentNoteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        noteTitleEditText = findViewById(R.id.noteTitleEditText)
        noteContentEditText = findViewById(R.id.noteContentEditText)
        saveNoteButton = findViewById(R.id.saveNoteButton)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            currentNoteId = intent.getIntExtra(EXTRA_ID, -1)
            noteTitleEditText.setText(intent.getStringExtra(EXTRA_TITLE))
            noteContentEditText.setText(intent.getStringExtra(EXTRA_CONTENT))
        } else {
            title = "Add Note"
        }

        saveNoteButton.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = noteTitleEditText.text.toString().trim()
        val content = noteContentEditText.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please insert a title and content", Toast.LENGTH_SHORT).show()
            return
        }

        val note = Note(id = if (currentNoteId != -1) currentNoteId else 0, title = title, content = content)

        if (currentNoteId == -1) {
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else {
            noteViewModel.update(note)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
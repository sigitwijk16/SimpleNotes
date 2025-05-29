package com.gitz.simplenotes.repository

import androidx.lifecycle.LiveData
import com.gitz.simplenotes.model.Note

interface NoteRepository {
    fun getAllNotes(): LiveData<List<Note>>
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    fun getNoteById(noteId: Int): LiveData<Note>
}
package com.gitz.simplenotes.repository

import androidx.lifecycle.LiveData
import com.gitz.simplenotes.data.local.NoteDao
import com.gitz.simplenotes.model.Note
import javax.inject.Inject

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    override suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    override suspend fun update(note: Note) {
        noteDao.update(note)
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    override fun getNoteById(noteId: Int): LiveData<Note> {
        return noteDao.getNoteById(noteId)
    }
}
package com.gitz.simplenotes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gitz.simplenotes.model.Note
import com.gitz.simplenotes.model.QuoteResponseItem
import com.gitz.simplenotes.model.Resource
import com.gitz.simplenotes.repository.NoteRepository
import com.gitz.simplenotes.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val quoteRepository: QuoteRepository
): ViewModel() {

    val allNotes: LiveData<List<Note>>

    private val _quote = MutableLiveData<Resource<QuoteResponseItem?>>()
    val quote: LiveData<Resource<QuoteResponseItem?>> = _quote

    init {
        allNotes = noteRepository.getAllNotes()
        fetchRandomQuote()
    }

    fun fetchRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            _quote.postValue(Resource.Loading())
            try {
                val response = quoteRepository.getRandomQuote()
                Log.d("Api Response", response.toString())
                if (response.isSuccessful) {
                    val quoteItemList: List<QuoteResponseItem>? = response.body()

                    if (quoteItemList != null && quoteItemList.isNotEmpty()) {
                        val firstQuoteItem = quoteItemList[0]
                        _quote.postValue(Resource.Success(firstQuoteItem))
                    } else {
                        _quote.postValue(Resource.Empty(null))
                    }
                } else {
                    _quote.postValue(Resource.Error("Response status unsuccessful"))
                }
            } catch (e: Exception) {
                _quote.postValue(Resource.Error("Error with exception: ${e}"))
            }
        }
    }

    fun insert(note: Note) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        noteRepository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        noteRepository.delete(note)
    }

    fun getNoteById(noteId: Int): LiveData<Note> {
        return noteRepository.getNoteById(noteId)
    }
}
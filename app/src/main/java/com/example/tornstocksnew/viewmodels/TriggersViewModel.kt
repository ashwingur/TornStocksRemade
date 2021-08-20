package com.example.tornstocksnew.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TriggersViewModel @Inject constructor(val repository: Repository): ViewModel() {

    fun getAllTriggers(): LiveData<List<Trigger>> {
        return repository.getAllTriggers()
    }

    fun deleteTrigger(trigger: Trigger) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteTrigger(trigger)
            }
        }
    }
}
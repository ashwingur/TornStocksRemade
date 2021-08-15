package com.example.tornstocksnew.utils

import com.example.tornstocksnew.models.Trigger

interface TriggerCreator {
    fun createTrigger(): Trigger?
}
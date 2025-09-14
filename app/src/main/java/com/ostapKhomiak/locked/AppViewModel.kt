package com.ostapKhomiak.locked


import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AppViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _isLocked = MutableStateFlow(sharedPreferences.getBoolean("isLocked", false))
    private val _lastAccess = MutableStateFlow(sharedPreferences.getString("lastAccess", null))

    val isLocked: StateFlow<Boolean> = _isLocked
    val lastAccess: StateFlow<String?> = _lastAccess

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleLock() {
        val newLockedState = !_isLocked.value
        val newLastAccess = formatLastAccess(System.currentTimeMillis())

        _isLocked.value = newLockedState
        _lastAccess.value = newLastAccess

        viewModelScope.launch {
            with(sharedPreferences.edit()) {
                putBoolean("isLocked", newLockedState)
                putString("lastAccess", newLastAccess)
                apply()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")
        .withZone(ZoneId.systemDefault())


    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatLastAccess(timestamp: Long): String {
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }
}

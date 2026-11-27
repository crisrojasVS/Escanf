package com.cristo.escanf.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristo.escanf.features.admin.domain.entities.ManagedUser
import com.cristo.escanf.features.admin.domain.usescases.GetAdminStatsUseCase
import com.cristo.escanf.features.admin.domain.usescases.GetAllScansUseCase
import com.cristo.escanf.features.admin.domain.usescases.GetUsersUseCase
import com.cristo.escanf.features.admin.domain.usescases.SetUserBlockedUseCase
import com.cristo.escanf.features.admin.presentation.screens.AdminUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getStats: GetAdminStatsUseCase,
    private val getUsers: GetUsersUseCase,
    private val setUserBlocked: SetUserBlockedUseCase,
    private val getAllScans: GetAllScansUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUIState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStats()
    }

    // ---------- Pestañas ----------

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
        reload(index)
    }

    fun refreshCurrent() = reload(_uiState.value.selectedTab)

    private fun reload(tab: Int) {
        when (tab) {
            0 -> loadStats()
            1 -> loadUsers()
            else -> loadScans()
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    // ---------- Resumen ----------

    fun loadStats() {
        viewModelScope.launch {
            if (_uiState.value.stats == null) {
                _uiState.update { it.copy(statsLoading = true, statsError = null) }
            }
            getStats().fold(
                onSuccess = { stats ->
                    _uiState.update { it.copy(statsLoading = false, stats = stats, statsError = null) }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (it.stats == null) it.copy(statsLoading = false, statsError = error.message)
                        else it.copy(statsLoading = false, message = error.message)
                    }
                }
            )
        }
    }

    // ---------- Usuarios ----------

    fun onSearchChange(value: String) {
        _uiState.update { it.copy(search = value) }
    }

    fun searchUsers() = loadUsers(showSpinner = true)

    fun loadUsers(showSpinner: Boolean = _uiState.value.users.isEmpty()) {
        viewModelScope.launch {
            if (showSpinner) {
                _uiState.update { it.copy(usersLoading = true, usersError = null) }
            }
            getUsers(_uiState.value.search.trim(), 1).fold(
                onSuccess = { page ->
                    _uiState.update {
                        it.copy(
                            usersLoading = false,
                            users = page.items,
                            usersPage = 1,
                            usersHasMore = page.hasMore,
                            usersError = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (it.users.isEmpty()) it.copy(usersLoading = false, usersError = error.message)
                        else it.copy(usersLoading = false, message = error.message)
                    }
                }
            )
        }
    }

    fun loadMoreUsers() {
        val current = _uiState.value
        if (current.usersLoadingMore || !current.usersHasMore) return

        _uiState.update { it.copy(usersLoadingMore = true) }
        viewModelScope.launch {
            getUsers(current.search.trim(), current.usersPage + 1).fold(
                onSuccess = { page ->
                    _uiState.update {
                        it.copy(
                            usersLoadingMore = false,
                            users = (it.users + page.items).distinctBy { user -> user.id },
                            usersPage = page.page,
                            usersHasMore = page.hasMore
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(usersLoadingMore = false, message = error.message) }
                }
            )
        }
    }

    fun toggleBlock(user: ManagedUser) {
        if (user.id in _uiState.value.busyUserIds) return

        _uiState.update { it.copy(busyUserIds = it.busyUserIds + user.id) }
        viewModelScope.launch {
            setUserBlocked(user.id, !user.isBlocked).fold(
                onSuccess = { blocked ->
                    _uiState.update { state ->
                        state.copy(
                            busyUserIds = state.busyUserIds - user.id,
                            users = state.users.map {
                                if (it.id == user.id) it.copy(isBlocked = blocked) else it
                            }
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(busyUserIds = it.busyUserIds - user.id, message = error.message)
                    }
                }
            )
        }
    }

    // ---------- Escaneos de todos ----------

    fun loadScans() {
        viewModelScope.launch {
            if (_uiState.value.scans.isEmpty()) {
                _uiState.update { it.copy(scansLoading = true, scansError = null) }
            }
            getAllScans(1).fold(
                onSuccess = { page ->
                    _uiState.update {
                        it.copy(
                            scansLoading = false,
                            scans = page.items,
                            scansPage = 1,
                            scansHasMore = page.hasMore,
                            scansError = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (it.scans.isEmpty()) it.copy(scansLoading = false, scansError = error.message)
                        else it.copy(scansLoading = false, message = error.message)
                    }
                }
            )
        }
    }

    fun loadMoreScans() {
        val current = _uiState.value
        if (current.scansLoadingMore || !current.scansHasMore) return

        _uiState.update { it.copy(scansLoadingMore = true) }
        viewModelScope.launch {
            getAllScans(current.scansPage + 1).fold(
                onSuccess = { page ->
                    _uiState.update {
                        it.copy(
                            scansLoadingMore = false,
                            scans = (it.scans + page.items).distinctBy { scan -> scan.record.id },
                            scansPage = page.page,
                            scansHasMore = page.hasMore
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(scansLoadingMore = false, message = error.message) }
                }
            )
        }
    }
}
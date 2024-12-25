package com.example.kplogbook.ui.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kplogbook.data.UserRepository
import com.example.kplogbook.data.request.CreateGroupRequest
import com.example.kplogbook.data.response.DataMember
import com.example.kplogbook.data.response.DataUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateGroupState(
    val currentUser: DataUser? = null,
    val groupName: String = "",
    val availableMembers: List<DataMember> = emptyList(),
    val selectedMemberIds: Set<Int> = emptySet(),
    val selectedMembers: List<DataMember> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CreateGroupViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateGroupState())
    val state: StateFlow<CreateGroupState> = _state.asStateFlow()

    init {
        loadCurrentUser()
        loadAvailableMembers()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser()
                _state.update {
                    it.copy(
                        currentUser = user.data,
                        // Remove current user from available members if they're already in the list
                        availableMembers = it.availableMembers.filter { member ->
                            member.id != user.data.id
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("CreateGroupViewModel", "Failed to load current user", e)
                _state.update {
                    it.copy(error = "Failed to load user: ${e.message}")
                }
            }
        }
    }

    private fun loadAvailableMembers() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val members = userRepository.getAvailableMembers().data
                // Filter out current user from available members
                val filteredMembers = _state.value.currentUser?.let { currentUser ->
                    members.filter { it.id != currentUser.id }
                } ?: members

                _state.update {
                    it.copy(
                        availableMembers = filteredMembers,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Failed to load members: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateGroupName(name: String) {
        _state.update { it.copy(groupName = name) }
    }

    fun addMember(memberId: Int) {
        _state.update { currentState ->
            val newSelectedIds = currentState.selectedMemberIds + memberId
            val newSelectedMembers = currentState.availableMembers.filter {
                newSelectedIds.contains(it.id)
            }
            currentState.copy(
                selectedMemberIds = newSelectedIds,
                selectedMembers = newSelectedMembers
            )
        }
    }

    fun removeMember(memberId: Int) {
        _state.update { currentState ->
            val newSelectedIds = currentState.selectedMemberIds - memberId
            val newSelectedMembers = currentState.availableMembers.filter {
                newSelectedIds.contains(it.id)
            }
            currentState.copy(
                selectedMemberIds = newSelectedIds,
                selectedMembers = newSelectedMembers
            )
        }
    }

    fun createGroup(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                // Include current user's ID in the member list
                val allMemberIds = state.value.currentUser?.let { currentUser ->
                    listOf(currentUser.id) + state.value.selectedMemberIds.toList()
                } ?: state.value.selectedMemberIds.toList()

                val request = CreateGroupRequest(
                    name = state.value.groupName,
                    memberIds = allMemberIds
                )

                userRepository.createGroup(request)
                _state.update { it.copy(isLoading = false) }
                onComplete(true)
            } catch (e: Exception) {
                Log.e("CreateGroupViewModel", "Failed to create group", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to create group: ${e.message}"
                    )
                }
                onComplete(false)
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
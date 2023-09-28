package com.jeuxdevelopers.estatepie.ui.fragments.inbox.chat

import androidx.lifecycle.ViewModel
import com.jeuxdevelopers.estatepie.models.chat.inbox.InboxModel
import com.jeuxdevelopers.estatepie.models.chat.message.MessageModel
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var chatList: MutableList<MessageModel> = mutableListOf()

    var messageModel = MessageModel()

    @Inject
    lateinit var tokenManager: TokenManager
    private val _dashboardUiState = MutableStateFlow(false)

    val dashboardUiState = _dashboardUiState.asStateFlow()

//    init {
//        viewModelScope.launch {
//            delay(500)
//            _dashboardUiState.value = true
//        }
//    }

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

}
package com.example.firstaiappcation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


data class ChatGptScreenState(
    val userQuestion: String = "",
    val gptResponse: String = ""
)
class ChatGPTViewModel: ViewModel() {
    var _uistate by mutableStateOf(ChatGptScreenState())

    init {
        val openai = OpenAI(
            token = BuildConfig.apiKey,
            timeout = Timeout(socket = 60.seconds),
            // additional configurations...
        )

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = "You are a helpful assistant!"
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = "Hello!"
                )
            )
        )

        viewModelScope.launch {
            val completion = openai.chatCompletion(chatCompletionRequest)
            //or, as flow
            val completions: Flow<ChatCompletionChunk> =
                openai.chatCompletions(chatCompletionRequest)
            completions.collectLatest {
                _uistate = ChatGptScreenState(gptResponse = it.choices.first().toString())
            }
        }
    }
}
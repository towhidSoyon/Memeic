package com.towhid.memeic.meme_gallery.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towhid.memeic.api.apis.PostApi
import com.towhid.memeic.api.models.Post
import com.towhid.memeic.core.presentation.MemeTemplate
import com.towhid.memeic.meme_editor.presentation.MemeText
import com.towhid.memeic.meme_editor.presentation.TextBoxInteractionState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MemeGalleryViewModel(
    private val postApi: PostApi
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MemeGalleryState())

    private val _actions: Channel<MemeGalleryEvent> = Channel(Channel.BUFFERED)
    val actions: Flow<MemeGalleryEvent> get() = _actions.receiveAsFlow()
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MemeGalleryState()
        )

    init {
        onAction(MemeGalleryAction.OnGetPost)
    }

    fun onAction(action: MemeGalleryAction) {
        when (action) {
            MemeGalleryAction.OnGetPost -> {
                viewModelScope.launch {
                    val post = postApi.postsGet().body()
                    _state.update {
                        it.copy(
                            post = post.take(10)
                        )
                    }
                    println(post)
                    Napier.d(tag = "check", message = "post.size.toString()")
                }
            }

            is MemeGalleryAction.OnMemeSelected -> {
                _actions.trySend(MemeGalleryEvent.OnMemeTemplateSelected(action.memeTemplate))
            }
        }
    }
    sealed interface MemeGalleryEvent{
        data class OnMemeTemplateSelected(val memeTemplate: MemeTemplate) : MemeGalleryEvent
    }
}

sealed interface MemeGalleryAction {
    data object OnGetPost : MemeGalleryAction
    data class OnMemeSelected(val memeTemplate: MemeTemplate): MemeGalleryAction
}


data class MemeGalleryState(
    val post: List<Post> = listOf()
)




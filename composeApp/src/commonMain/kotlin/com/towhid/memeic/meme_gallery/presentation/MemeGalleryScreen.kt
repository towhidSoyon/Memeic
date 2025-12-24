package com.towhid.memeic.meme_gallery.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.towhid.memeic.api.models.Post
import com.towhid.memeic.core.presentation.MemeTemplate
import com.towhid.memeic.core.presentation.memeTemplates
import com.towhid.memeic.meme_editor.presentation.util.observeAsActions
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MemeGallery(
    onMemeTemplateSelected: (MemeTemplate) -> Unit
) {
    val viewModel: MemeGalleryViewModel = koinViewModel<MemeGalleryViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.actions.observeAsActions { event ->
        when (event) {
            is MemeGalleryViewModel.MemeGalleryEvent.OnMemeTemplateSelected -> {
                onMemeTemplateSelected(event.memeTemplate)
            }
        }
    }

    MemeGalleryScreen(
        state = state,
        onMemeTemplateSelected = onMemeTemplateSelected,
        onAction = viewModel::onAction
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeGalleryScreen(
    state: MemeGalleryState,
    onMemeTemplateSelected: (MemeTemplate) -> Unit,
    onAction: (MemeGalleryAction) -> Unit,
) {
    val allItems = state.post.map { it as Any } + memeTemplates.map { it as Any }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MEMEIC"
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(
                start = innerPadding.calculateLeftPadding(LayoutDirection.Ltr) + 8.dp,
                end = innerPadding.calculateRightPadding(LayoutDirection.Ltr) + 8.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 8.dp,
            )
        ) {

            items(allItems) { item ->
                when (item) {
                    is Post -> {
                        Card(
                            modifier = Modifier,
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    item.title.take(10), style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    item.body, style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    maxLines = 5,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    is MemeTemplate -> {
                        Card(
                            onClick = { onMemeTemplateSelected(item) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Image(
                                painter = painterResource(item.drawable),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            /*items(
                items = memeTemplates,
                key = { it.id }
            ) { memeTemplate ->
                Card(
                    onClick = {
                        onMemeTemplateSelected(memeTemplate)
                    },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Image(
                        painter = painterResource(memeTemplate.drawable),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = null
                    )
                }
            }*/
        }
    }
}
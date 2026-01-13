package com.towhid.memeic.meme_gallery.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import greet


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

private const val HORIZONTAL_GRID_COUNT = 5
private const val VERTICAL_GRID_COUNT = 6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeGalleryScreen(
    state: MemeGalleryState,
    onMemeTemplateSelected: (MemeTemplate) -> Unit,
    onAction: (MemeGalleryAction) -> Unit,
) {
    val allItems = state.post.map { it as Any } + memeTemplates.map { it as Any }
    val earningsData = listOf(
        EarningsPoint("Dec 1", 300f),
        EarningsPoint("Dec 6", 320f),
        EarningsPoint("Dec 12", 420f),
        EarningsPoint("Dec 18", 50f),
        EarningsPoint("Dec 18", 250f),
        EarningsPoint("Dec 18", 150f),
        EarningsPoint("Dec 18", 350f),
        EarningsPoint("Dec 18", 450f),
        EarningsPoint("Dec 18", 50f),
        EarningsPoint("Dec 24", 450f),
        EarningsPoint("Dec 31", 300f)
    )



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
                            modifier = Modifier.fillMaxWidth().clickable{
                                    greet("hello soyon")
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Earnings",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                EarningsLineChart(
                                    data = earningsData,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
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

data class EarningsPoint(
    val label: String,
    val value: Float
)

@Composable
fun EarningsLineChart(
    data: List<EarningsPoint>,
    modifier: Modifier = Modifier
) {

    val maxValue = 500f
    val lineColor = Color(0xFF2F80FF)
    val gridColor = Color(0xFFE6EDF7)

    Canvas(modifier = modifier) {

        val width = size.width
        val height = size.height

        val spaceBetween = width / (data.size - 1)
        val heightRatio = height / maxValue

        repeat(HORIZONTAL_GRID_COUNT + 1) { index ->
            val y = height / HORIZONTAL_GRID_COUNT * index
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }

        // --- Vertical Grid (FIXED) ---
        data.indices.forEach { index ->
            val x = index * spaceBetween
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
        }

        // --- Chart Points ---
        val points = data.mapIndexed { index, point ->
            Offset(
                x = index * spaceBetween,
                y = height - (point.value * heightRatio)
            )
        }

        // --- Line Path ---
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                quadraticBezierTo(
                    (points[i - 1].x + points[i].x) / 2,
                    points[i - 1].y,
                    points[i].x,
                    points[i].y
                )
            }
        }

        // --- Fill Area ---
        val fillPath = Path().apply {
            addPath(linePath)
            lineTo(points.last().x, height)
            lineTo(points.first().x, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.25f),
                    Color.Transparent
                )
            )
        )

        // --- Draw Line ---
        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )

        // --- Data Point Dots ---
        points.forEach {
            drawCircle(
                color = Color.White,
                radius = 8f,
                center = it
            )
            drawCircle(
                color = lineColor,
                radius = 5f,
                center = it
            )
        }
    }
}



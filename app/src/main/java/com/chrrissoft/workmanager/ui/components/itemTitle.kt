package com.chrrissoft.workmanager.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ItemTitle(
    text: String,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
            ) { onClick() }
            .padding(horizontal = 8.dp)
            .clip(shapes.medium)
            .fillMaxWidth()
            .background(colorScheme.onPrimary)
            .padding(10.dp),
    ) {
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = text,
            style = typography.titleMedium.copy(color = colorScheme.primary),
            modifier = Modifier.weight(2.3f)
        )
        if (actions!=null) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,

                modifier = Modifier.weight(1f),
            ) {
                actions()
            }
        }
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        shape = shapes.medium,
        colors = cardColors(containerColor = colorScheme.primaryContainer),
        border = if (selected) BorderStroke(1.dp, colorScheme.primary) else null,
        modifier = modifier
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
            ) { onClick() }
            .padding(horizontal = 8.dp)
    ) {
        Spacer(Modifier.height(15.dp))
        content()
        Spacer(Modifier.height(10.dp))
    }
}

package com.chrrissoft.workmanager.info.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal

@Composable
fun SelectableForObserve(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .clickable { onClick(!checked) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onClick(it) }
        )
        Text(
            text,
            fontWeight = if (checked) Medium else Normal,
            color = if (checked) colorScheme.primary else colorScheme.secondary,
        )
    }
}

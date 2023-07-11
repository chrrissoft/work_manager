package com.chrrissoft.workmanager.ui.theme

import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
val appBarColors
    @Composable get() = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = colorScheme.primaryContainer,
        titleContentColor = colorScheme.onPrimaryContainer,
        actionIconContentColor = colorScheme.onPrimaryContainer,
        navigationIconContentColor = colorScheme.onPrimaryContainer,
    )

val navigationBarItemColors
    @Composable get() = NavigationBarItemDefaults.colors(
        selectedIconColor = colorScheme.onPrimary,
        selectedTextColor = colorScheme.primary,
        indicatorColor = colorScheme.primary,
        unselectedIconColor = colorScheme.primary.copy(.5f),
        unselectedTextColor = colorScheme.primary.copy(.5f),
    )

val textFieldColors
    @Composable get() = TextFieldDefaults.colors(
        focusedTextColor = colorScheme.primary,
        unfocusedTextColor = colorScheme.primary,
        focusedContainerColor = colorScheme.onPrimary,
        unfocusedContainerColor = colorScheme.onPrimary,
        cursorColor = colorScheme.primary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedPlaceholderColor = colorScheme.primary.copy(.6f),
        unfocusedPlaceholderColor = colorScheme.primary.copy(.6f),
    )

@Composable
fun alertDialogColors(
    containerColor: Color = colorScheme.primaryContainer,
    iconContentColor: Color = colorScheme.primary,
    titleContentColor: Color = colorScheme.primary,
    textContentColor: Color = colorScheme.primary,
): AlertDialogColors {
    return AlertDialogColors(
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
    )
}

data class AlertDialogColors constructor(
    val containerColor: Color,
    val iconContentColor: Color,
    val titleContentColor: Color,
    val textContentColor: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
val inputChipsColors
    @Composable get() = InputChipDefaults.inputChipColors(
        containerColor = colorScheme.primaryContainer,
        labelColor = colorScheme.primary,
        leadingIconColor = colorScheme.primary,
        trailingIconColor = colorScheme.primary,
        disabledContainerColor = colorScheme.errorContainer,
        disabledLabelColor = colorScheme.errorContainer,
        disabledLeadingIconColor = colorScheme.errorContainer,
        disabledTrailingIconColor = colorScheme.errorContainer,
        selectedContainerColor = colorScheme.primary,
        disabledSelectedContainerColor = colorScheme.errorContainer,
        selectedLabelColor = colorScheme.onPrimary,
        selectedLeadingIconColor = colorScheme.onPrimary,
        selectedTrailingIconColor = colorScheme.onPrimary,
        )
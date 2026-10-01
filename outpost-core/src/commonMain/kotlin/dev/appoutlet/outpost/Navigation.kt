package dev.appoutlet.outpost

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun Navigation() {
    Text(modifier = Modifier.testTag("Navigation"), text = "Outpost")
}
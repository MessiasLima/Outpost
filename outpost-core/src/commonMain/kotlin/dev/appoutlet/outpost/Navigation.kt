package dev.appoutlet.outpost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.appoutlet.outpost.core.SampleInterface
import org.koin.compose.koinInject

@Composable
internal fun Navigation() {
    val sampleInterface = koinInject<SampleInterface>()

    Column(modifier = Modifier.safeDrawingPadding()) {
        Text(modifier = Modifier.testTag("Navigation"), text = "Outpost")

        Button(onClick = {}) {
            Text("Button ${sampleInterface.variable}")
        }

        FilledTonalButton(onClick = {}) {
            Text("FilledTonalButton")
        }
    }
}

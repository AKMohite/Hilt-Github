package app.mak.gitstar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import app.mak.gitstar.core.designsystem.GitStarTheme
import app.mak.gitstar.features.discover.presentation.DiscoverRoot

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitStarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiscoverRoot(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

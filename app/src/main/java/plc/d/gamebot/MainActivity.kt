package plc.d.gamebot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import plc.d.gamebot.screens.Ui
import plc.d.gamebot.ui.theme.GameBotTheme


class MainActivity : ComponentActivity() {
    // Top-level viewmodel to pass through app
    private val viewModel: GameViewModel by viewModels { GameViewModel.Factory }

    // Permission handler
    // TODO: support for more modern APIs
    private val getPermit = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.i("DEBUG", "${it.key}: ${it.value}")
        }
    }

    // Stylized font for use in titles, etc.
    private val botFont = FontFamily(
        Font(R.font.orbitron_regular, FontWeight.Normal),
        Font(R.font.orbitron_bold, FontWeight.Bold),
        Font(R.font.orbitron_medium, FontWeight.Medium),
        Font(R.font.orbitron_semibold, FontWeight.SemiBold),
        Font(R.font.orbitron_extrabold, FontWeight.ExtraBold),
        Font(R.font.orbitron_black, FontWeight.Black)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameBotTheme (darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Ui(viewModel, getPermit, botFont) {
                        finish()
                    }
                }
            }
        }
    }
}
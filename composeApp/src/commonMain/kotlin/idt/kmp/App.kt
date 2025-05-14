package idt.kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import qr.composeapp.generated.resources.Res
import qr.composeapp.generated.resources.compose_multiplatform
import qrgenerator.QRCodeImage

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            val scope = rememberCoroutineScope()
            val generatedQRCode = remember { mutableStateOf<ImageBitmap?>(null) }

            QRCodeImage(
                url = "https://www.google.com/",
                contentScale = ContentScale.Fit,
                contentDescription = "QR Code",
                modifier = Modifier
                    .size(150.dp),
                onSuccess = { qrImage ->
                    println("Noob Shit")
                }
            )


        }
    }
}
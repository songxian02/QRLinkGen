package idt.kmp

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import qrgenerator.QRCodeImage

// Navy Blue Theme Colors
private val NavyBlue = Color(0xFF1A237E)
private val LightNavy = Color(0xFF3F51B5)
private val NavyAccent = Color(0xFF303F9F)
private val LightBlue = Color(0xFFE8EAF6)

@Composable
@Preview
fun App() {
    val navyTheme = darkColors(
        primary = NavyBlue,
        primaryVariant = NavyAccent,
        secondary = LightNavy,
        background = Color(0xFF0A0E27),
        surface = Color(0xFF1E2147),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(colors = navyTheme) {
        var userInput by remember { mutableStateOf("") }
        var shouldGenerateQR by remember { mutableStateOf(false) }
        var isGenerating by remember { mutableStateOf(false) }

        // Animation states
        val buttonScale by animateFloatAsState(
            targetValue = if (userInput.isNotBlank()) 1f else 0.95f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0E27),
                            Color(0xFF1A237E),
                            Color(0xFF0A0E27)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                // Animated Header
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(1000)
                    ) + fadeIn(animationSpec = tween(1000))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "QR Code Generator",
                            style = MaterialTheme.typography.h4.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Create QR codes instantly",
                            style = MaterialTheme.typography.subtitle1,
                            color = LightBlue
                        )
                    }
                }

                // Input Field Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF1E2147),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            label = { Text("Enter URL or text", color = LightBlue) },
                            placeholder = { Text("https://example.com", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                focusedBorderColor = LightNavy,
                                unfocusedBorderColor = NavyAccent,
                                cursorColor = LightNavy
                            ),
                            trailingIcon = {
                                if (userInput.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            userInput = ""
                                            shouldGenerateQR = false
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Clear,
                                            contentDescription = "Clear",
                                            tint = LightBlue
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                // Generate Button with Animation
                Button(
                    onClick = {
                        if (!isGenerating) {
                            isGenerating = true
                            shouldGenerateQR = userInput.isNotBlank()
                            // Simulate generation delay for better UX
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(500)
                                isGenerating = false
                            }
                        }
                    },
                    enabled = userInput.isNotBlank() && !isGenerating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(buttonScale),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LightNavy,
                        contentColor = Color.White,
                        disabledBackgroundColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(28.dp),
                    elevation = ButtonDefaults.elevation(8.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = if (isGenerating) "Generating..." else "Generate QR Code",
                        style = MaterialTheme.typography.button.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // QR Code Display with Animation
                AnimatedVisibility(
                    visible = shouldGenerateQR && userInput.isNotBlank(),
                    enter = scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { /* Add functionality like save/share */ },
                        backgroundColor = Color.White,
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            QRCodeImage(
                                url = userInput,
                                contentScale = ContentScale.Fit,
                                contentDescription = "Generated QR Code",
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(8.dp),
                                onSuccess = { qrImage ->
                                    println("QR Code generated successfully for: $userInput")
                                }
                            )
                        }
                    }
                }

                // URL Display
                AnimatedVisibility(
                    visible = shouldGenerateQR && userInput.isNotBlank(),
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(600)
                    ) + fadeIn()
                ) {
                    Card(
                        backgroundColor = Color(0xFF1E2147),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "QR Code for: $userInput",
                            style = MaterialTheme.typography.body2.copy(
                                color = LightBlue,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
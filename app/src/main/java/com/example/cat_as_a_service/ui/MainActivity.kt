package com.example.cat_as_a_service.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.cat_as_a_service.R
import com.example.cat_as_a_service.network.NetworkConstants
import com.example.cat_as_a_service.ui.theme.CuteCatsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuteCatsTheme {
                // A surface container using the 'background' color from the theme
                CuteCatsScreen(context = this, hiltViewModel())
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CuteCatsScreen(context: Context, viewModel: MainViewModel) {

    val randomNumber = remember {
        mutableStateOf((0..100000).random())
    }
    val isImageLoaded = remember {
        mutableStateOf(false)
    }
    val isSelected = remember {
        mutableStateOf(false)
    }

    var currentBitmap: Bitmap? by remember { mutableStateOf(null) }

    if (!isImageLoaded.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.fillMaxSize()) {
            val request = ImageRequest.Builder(context)
                .data("${NetworkConstants.BASE_URL}${NetworkConstants.RANDOM_CAT}?${randomNumber.value}")
                .crossfade(true)
                .listener { request, result ->
                    currentBitmap = result.drawable.toBitmap()
                }
                .build()

            AsyncImage(
                model = request,
                contentDescription = "Cat picture",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(330.dp),
                contentScale = ContentScale.Crop,
                onState = {
                    when (it) {
                        is AsyncImagePainter.State.Loading -> {
                            isImageLoaded.value = false
                        }
                        is AsyncImagePainter.State.Success -> {
                            isImageLoaded.value = true
                        }
                    }
                }
            )

            if (isImageLoaded.value) {

                val infiniteTransition = rememberInfiniteTransition()
                val scale1 = infiniteTransition.animateFloat(
                    0.7f,
                    0.8f,
                    // No offset for the 1st animation
                    infiniteRepeatable(tween(600), RepeatMode.Reverse)
                )
                Image(
                    painter = painterResource(id = R.drawable.cat_paw_icon),
                    modifier = Modifier
                        .padding()
                        .align(Alignment.BottomCenter)
                        .size(200.dp)
                        .graphicsLayer {
                            scaleX = scale1.value
                            scaleY = scale1.value
                        }
                        .clickable {
                            randomNumber.value = (0..100000).random()
                            isSelected.value = false
                            Log.e("@@@", "here ${randomNumber.value}")
                        },
                    contentDescription = "fetch new cat")

//                Button(
//                    modifier = Modifier
//                        .padding(vertical = 24.dp)
//                        .align(Alignment.BottomCenter)
//                        .offset(y = 60.dp),
//                    onClick = {
//                        saveImageToDownloadFolder("Cat_${randomNumber.value}.png",currentBitmap!!, context)
//                    }
//                ) {
//                    Text("Download Cat")
//                }
            }

        }
    }
}

//TODO move to ViewModel at least
fun saveImageToDownloadFolder(imageFile: String, ibitmap: Bitmap, context: Context) {
    try {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            imageFile
        )
        val outputStream: OutputStream = FileOutputStream(filePath)
        ibitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Toast.makeText(
            context,
            imageFile + "Sucessfully saved in Download Folder",
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error while saving a cat ${e}",
            Toast.LENGTH_SHORT
        ).show()
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CuteCatsTheme {
        Greeting(name = "Grinya")
        //CuteCatsScreen()
    }
}
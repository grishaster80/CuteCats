package com.example.cat_as_a_service.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.cat_as_a_service.MyApplication
import com.example.cat_as_a_service.network.NetworkConstants
import com.example.cat_as_a_service.repository.CatRepository
import com.example.cat_as_a_service.ui.theme.CuteCatsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

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
        Box(Modifier.size(500.dp)) {
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
                Button(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.BottomCenter),
                    onClick = {
                        randomNumber.value = (0..100000).random()
                        isSelected.value = false
                        Log.e("@@@", "here ${randomNumber.value}")
                    }
                ) {
                    Text("Next Cat")
                }
                Button(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 60.dp),
                    onClick = {
                        saveImageToDownloadFolder("Cat_${randomNumber.value}.png",currentBitmap!!, context)
                    }
                ) {
                    Text("Download Cat")
                }
                IconButton(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = 80.dp, x = -28.dp),
                    onClick = {
                        isSelected.value = !isSelected.value
                        viewModel.saveCatImage(currentBitmap)
                    }) {
                    if (isSelected.value) {
                        Icon(
                            Icons.Filled.Favorite,
                            tint = Color.Red,
                            contentDescription = null,
                            modifier = Modifier
                                .height(36.dp)
                                .width(36.dp)
                        )
                    } else {
                        Icon(
                            Icons.Filled.Favorite,
                            tint = Color.DarkGray,
                            contentDescription = null,
                            modifier = Modifier
                                .height(36.dp)
                                .width(36.dp)
                        )
                    }
                }
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
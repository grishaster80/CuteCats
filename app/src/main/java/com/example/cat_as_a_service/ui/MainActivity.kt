package com.example.cat_as_a_service.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.cat_as_a_service.R
import com.example.cat_as_a_service.network.NetworkConstants
import com.example.cat_as_a_service.ui.theme.CuteCatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuteCatsTheme {
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
                    0.65f,
                    0.85f,
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
            }

        }
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
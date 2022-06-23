package com.example.cat_as_a_service.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.cat_as_a_service.MyApplication
import com.example.cat_as_a_service.R
import com.example.cat_as_a_service.network.NetworkConstants
import com.example.cat_as_a_service.ui.theme.CuteCatsTheme
import javax.inject.Inject
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.subComponentSample().create().inject(this)
        super.onCreate(savedInstanceState)
        mainViewModel.test()
        setContent {
            CuteCatsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CuteCatsScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun CuteCatsScreen() {

    val randomNumber = remember {
        mutableStateOf(0)
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
        Box(Modifier.size(500.dp)) {
            AsyncImage(
                model = "${NetworkConstants.BASE_URL}${NetworkConstants.RANDOM_CAT}?${randomNumber.value}",
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
                    Text("Load Cat")
                }
                IconButton(modifier = Modifier.align(Alignment.TopEnd).offset(y=80.dp, x = -28.dp),
                    onClick = {
                        isSelected.value = !isSelected.value
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CuteCatsTheme {
        Greeting(name = "Grinya")
        //CuteCatsScreen()
    }
}
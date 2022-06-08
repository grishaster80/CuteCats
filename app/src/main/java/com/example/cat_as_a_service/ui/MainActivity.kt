package com.example.cat_as_a_service.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
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
        if (isImageLoaded.value) {
            IconButton(onClick = { Log.e("@@@","test")}) {
                Icon(
                    painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                )
            }
        }
        AsyncImage(
            model = "${NetworkConstants.BASE_URL}${NetworkConstants.RANDOM_CAT}?${randomNumber.value}",
            contentDescription = "Cat picture",
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
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = {
                randomNumber.value = (0..100000).random()
                Log.e("@@@", "here ${randomNumber.value}")
            }
        ) {
            Text("Load Cat")
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
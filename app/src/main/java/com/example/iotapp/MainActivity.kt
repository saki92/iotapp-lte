package com.example.iotapp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iotapp.ui.theme.NamecardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NamecardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingImage("Hello", modifier = Modifier)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var smsButtonPressed by remember { mutableStateOf(false) }
    var msisdnValid by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus() //hide keyboard when tapped outside
            })
        }
    ) {
        Text(
            text = name,
            fontSize = 50.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Enter Device's MSISDN Number",
            modifier = Modifier.padding(16.dp)
        )
        var msisdn by remember { mutableStateOf("") }
        var msisdnLen = 10
        OutlinedTextField(
            value = msisdn,
            onValueChange = {
                if (it.length <= msisdnLen) {
                    msisdn = it
                }
                msisdnValid = msisdn.length == msisdnLen
            },
            label = { Text("10 Digit Phone Number") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }), // hide keyboard when done is clicked
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

        )
        Button(
            onClick = {
                smsButtonPressed = true
                focusManager.clearFocus()
            }, // hide keyboard
            modifier = Modifier.padding(20.dp),
            enabled = !smsButtonPressed && msisdnValid
        ) {
            Text(text = if (smsButtonPressed) "Wait" else "Send SMS")
        }

        val countDownTime = 5000L
        val countDownInterval = 1000L
        val timeLeft = remember { mutableStateOf(countDownTime) }
        if (smsButtonPressed) {
            val timer = object : CountDownTimer(countDownTime, countDownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft.value = millisUntilFinished / countDownInterval
                }

                override fun onFinish() {
                    smsButtonPressed = false
                }
            }
            Text(
                text = "${timeLeft.value.toString()} s",
                modifier = Modifier.padding(16.dp)
            )
            DisposableEffect(key1 = "key") {
                timer.start()
                onDispose {
                    timer.cancel()
                }
            }
        }
    }
}

@Composable
fun GreetingImage(message: String, modifier: Modifier = Modifier) {
    val image = painterResource(id = R.drawable.brakou)
    Image(
        painter = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.75F
    )
    Greeting(name = message, modifier = Modifier
        .fillMaxSize()
        .padding(16.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    NamecardTheme {
        GreetingImage("Hello", modifier = Modifier)
    }
}
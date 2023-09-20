package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                TipCalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorApp() {
    var billAmount by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(15) }
    var showTipAmount by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    // Calculate tip and total within the composable function
    val tipAmount by remember { derivedStateOf { calculateTip(billAmount, tipPercentage) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text (
            text = "Tip Calculator",
            color = Color.White,
            fontSize = 32.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = billAmount,
            onValueChange = {
                billAmount = it
            },
            label = { Text(
                text = "Bill Amount",
                color = Color.White
            ) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White,
                textColor = Color.White,
                unfocusedBorderColor = Color(0xFFb300b3),
                focusedBorderColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = tipPercentage.toFloat(),
            onValueChange = {
                tipPercentage = it.toInt()
            },
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFb300b3),
                activeTrackColor = Color(0xFFb300b3)
            ),
            valueRange = 0f..30f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Text(text = "Tip Percentage: $tipPercentage%",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier
                .padding(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        if (showTipAmount) {
            Text(
                text = "Tip Amount: $${"%.2f".format(tipAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )

            Button(
                onClick = {
                    showTipAmount = false
                    billAmount = ""
                    tipPercentage = 15
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Reset")
            }
        } else {
            Button(
                onClick = {
                    showTipAmount = true
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Calculate Tip")
            }
        }
    }
}

fun calculateTip(billAmount: String, tipPercentage: Int): Double {
    val bill = billAmount.toDoubleOrNull() ?: 0.0
    return bill * (tipPercentage / 100.0)
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorAppPreview() {
    TipCalculatorTheme {
        TipCalculatorApp()
    }
}

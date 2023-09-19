package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = billAmount,
            onValueChange = {
                billAmount = it
            },
            label = { Text("Bill Amount") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
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
            valueRange = 0f..30f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Text(text = "Tip Percentage: $tipPercentage%", fontSize = 16.sp, modifier = Modifier.padding(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        if (showTipAmount) {
            Text(
                text = "Tip Amount: $${"%.2f".format(tipAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
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

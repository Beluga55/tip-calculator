package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var billAmountError by remember { mutableStateOf(false) }

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
            style = TextStyle (
                color = Color.White,
                fontSize = 32.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            ),
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = billAmount,
            onValueChange = {
                if(it.matches(Regex("""^\d*\.?\d*$"""))) {
                    billAmount = it
                    billAmountError = false
                }
                else {
                    billAmountError = true
                }
            },
            label = { Text(
                text = "Bill Amount",
                style = TextStyle (
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                ),
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
            isError = billAmountError,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        if(billAmountError) {
            Text(
                text = "Invalid input. Please Enter A Valid Number.",
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                ),
                modifier = Modifier
                    .padding(8.dp)
            )
        }

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
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            ),
            modifier = Modifier
                .padding(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        if (showTipAmount) {
            Text(
                text = "Tip Amount: $${"%.2f".format(tipAmount)}",
                modifier = Modifier.padding(8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                ),
            )

            Button(
                onClick = {
                    showTipAmount = false
                    billAmount = ""
                    tipPercentage = 15
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFFb300b3)
                )
            ) {
                Text(
                    text = "Reset",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                    ),
                )
            }
        } else {
            Button(
                onClick = {
                    showTipAmount = true
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFFb300b3)
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Calculate Tip",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                    ),
                )
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

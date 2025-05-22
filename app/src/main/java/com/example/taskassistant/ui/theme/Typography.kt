package com.example.taskassistant.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.taskassistant.R

val Inter_Regular = FontFamily(


    Font(R.font.inter_regular, FontWeight.Normal)
)

val AppTypography = Typography (
    displayLarge = TextStyle(
        fontFamily = Inter_Regular,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    )
)

    // Customize more styles as needed


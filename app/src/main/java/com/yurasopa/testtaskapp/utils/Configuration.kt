package com.yurasopa.testtaskapp.utils


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yurasopa.testtaskapp.R

class Typography() {

    companion object {
        private val fontFamily = FontFamily(
            Font(R.font.nunito_sans_regular, FontWeight.Normal)
        )

        val heading1 = androidx.compose.ui.text.TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal, // Regular 400
            fontSize = 20.sp,
            lineHeight = 24.sp
        )

        val body1 = androidx.compose.ui.text.TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal, // Regular 400
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        val body2 = androidx.compose.ui.text.TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal, // Regular 400
            fontSize = 18.sp,
            lineHeight = 24.sp
        )

        val body3 = androidx.compose.ui.text.TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal, // Regular 400
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }

}

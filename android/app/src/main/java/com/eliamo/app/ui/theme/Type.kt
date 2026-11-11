package com.eliamo.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 系统字体栈；标题走大字号 + 重字重，数据数字加大，制造「报表」反差
val EliaoTypography = Typography(
    displayLarge = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 40.sp, color = Ink),
    headlineLarge = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 30.sp, color = Ink),
    titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink),
    titleMedium = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink),
    bodyLarge = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, color = Ink),
    bodyMedium = TextStyle(fontSize = 13.sp, color = InkSoft),
    bodySmall = TextStyle(fontSize = 12.sp, color = InkFaint),
    labelSmall = TextStyle(fontSize = 11.sp, color = InkFaint)
)

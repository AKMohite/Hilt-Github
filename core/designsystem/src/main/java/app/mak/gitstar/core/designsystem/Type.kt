package app.mak.gitstar.core.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val SansCode = FontFamily(
    Font(R.font.google_sans_code_regular, FontWeight.Normal),
    Font(R.font.google_sans_code_medium, FontWeight.Medium),
    Font(R.font.google_sans_code_semi_bold, FontWeight.SemiBold),
    Font(R.font.google_sans_code_bold, FontWeight.Bold),
    Font(R.font.google_sans_code_extra_bold, FontWeight.ExtraBold)
)

internal val Typography = Typography(
    displayLarge = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.ExtraBold, fontSize = 57.sp),
    displaySmall = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Bold, fontSize = 36.sp),
    headlineLarge = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Bold, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
    headlineSmall = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = SansCode, fontWeight = FontWeight.Medium, fontSize = 11.sp)
)
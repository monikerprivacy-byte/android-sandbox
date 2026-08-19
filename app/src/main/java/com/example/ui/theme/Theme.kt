package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BinanceDarkColorScheme = darkColorScheme(
  primary = BinanceGold,
  onPrimary = Color.Black,
  primaryContainer = Color(0xFF2E3133),
  onPrimaryContainer = BinanceGoldLight,
  secondary = BinanceGreen,
  onSecondary = Color.Black,
  secondaryContainer = BinanceGreenBg,
  onSecondaryContainer = BinanceGreen,
  tertiary = BinanceRed,
  onTertiary = Color.White,
  tertiaryContainer = BinanceRedBg,
  onTertiaryContainer = BinanceRed,
  background = BinanceBackground,
  onBackground = TextPrimary,
  surface = BinanceSurface,
  onSurface = TextPrimary,
  surfaceVariant = BinanceSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = BinanceCardBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark editorial theme for high-contrast crypto analytics
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = BinanceDarkColorScheme,
    typography = Typography,
    content = content
  )
}

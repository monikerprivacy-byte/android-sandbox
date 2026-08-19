package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OptionChainSummary
import com.example.ui.theme.BinanceCardBorder
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.BinanceGreen
import com.example.ui.theme.BinanceGreenBg
import com.example.ui.theme.BinanceRed
import com.example.ui.theme.BinanceRedBg
import com.example.ui.theme.BinanceSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun PCRSummaryCard(
    summary: OptionChainSummary,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600

    val totalAtmOi = (summary.atm5CallOi + summary.atm5PutOi).coerceAtLeast(1.0)
    val putRatio = (summary.atm5PutOi / totalAtmOi).toFloat().coerceIn(0.05f, 0.95f)
    val callRatio = 1f - putRatio

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BinanceSurface)
            .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
            .padding(if (isCompact) 10.dp else 14.dp)
            .testTag("pcr_summary_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ATM ±5 STRIKES PCR FOCUS",
                    color = TextMuted,
                    fontSize = if (isCompact) 9.sp else 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = if (summary.atm5Pcr >= 1.0) "BULLISH PUT WRITING" else "BEARISH CALL RESISTANCE",
                    color = if (summary.atm5Pcr >= 1.0) BinanceGreen else BinanceRed,
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (summary.atm5Pcr >= 1.0) BinanceGreenBg else BinanceRedBg)
                    .border(1.dp, if (summary.atm5Pcr >= 1.0) BinanceGreen else BinanceRed, RoundedCornerShape(6.dp))
                    .padding(horizontal = if (isCompact) 6.dp else 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "PCR " + String.format(Locale.US, "%.2f", summary.atm5Pcr),
                    color = if (summary.atm5Pcr >= 1.0) BinanceGreen else BinanceRed,
                    fontSize = if (isCompact) 11.sp else 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        // Call vs Put Open Interest Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF181C20))
        ) {
            // Put OI (Green)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(putRatio)
                    .background(BinanceGreen)
            )
            // Call OI (Red)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(callRatio)
                    .background(BinanceRed)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Open Interest Numbers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BinanceGreen)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Puts OI: " + String.format(Locale.US, "%.0f", summary.atm5PutOi),
                    color = BinanceGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Calls OI: " + String.format(Locale.US, "%.0f", summary.atm5CallOi),
                    color = BinanceRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BinanceRed)
                )
            }
        }
    }
}

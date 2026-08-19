package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OptionChainSummary
import com.example.ui.theme.BinanceCardBorder
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.BinanceGreen
import com.example.ui.theme.BinanceSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun RVolPowerCard(
    summary: OptionChainSummary,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BinanceSurface)
            .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
            .padding(if (isCompact) 10.dp else 14.dp)
            .testTag("rvol_power_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RELATIVE VOLUME (rVol)",
                color = TextMuted,
                fontSize = if (isCompact) 9.sp else 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )

            // Power status tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(summary.rVolStatus.badgeColorHex))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = summary.rVolStatus.label,
                    color = Color.Black,
                    fontSize = if (isCompact) 8.sp else 9.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = String.format(Locale.US, "%.1fx", summary.rVolMultiplier),
                color = TextPrimary,
                fontSize = if (isCompact) 26.sp else 38.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Monospace,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "vs Past 24h Avg Volume",
                color = TextSecondary,
                fontSize = if (isCompact) 10.sp else 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        // Animated Histogram for past vs current volume surge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            summary.hourlyVolumeHistory.forEachIndexed { index, vol ->
                val maxVol = (summary.hourlyVolumeHistory.maxOrNull() ?: 1.0).coerceAtLeast(0.1)
                val targetHeightFraction = (vol / maxVol).toFloat().coerceIn(0.15f, 1.0f)
                val animatedHeight by animateFloatAsState(
                    targetValue = targetHeightFraction,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                    label = "rVolBar_$index"
                )

                val isHighSurge = index >= summary.hourlyVolumeHistory.size - 3 || vol >= 2.0

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(animatedHeight)
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                        .background(if (isHighSurge) BinanceGold else Color(0xFF2E3133))
                )
            }
        }
    }
}

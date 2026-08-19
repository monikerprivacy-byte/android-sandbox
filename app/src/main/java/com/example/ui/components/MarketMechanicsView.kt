package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EffortVsResultItem
import com.example.data.model.OptionChainSummary
import com.example.ui.theme.BinanceCardBorder
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.BinanceGreen
import com.example.ui.theme.BinanceRed
import com.example.ui.theme.BinanceSurface
import com.example.ui.theme.BinanceSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun MarketMechanicsView(
    summary: OptionChainSummary,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("market_mechanics_view")
    ) {
        // --- 1. STRONGEST S/R CLUSTERS SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(if (isCompact) 10.dp else 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "SR Clusters",
                    tint = BinanceGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "STRONGEST S/R CLUSTERS (MECHANICS)",
                    color = BinanceGold,
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isCompact) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SupportClusterCard(summary = summary, isCompact = true)
                    ResistanceClusterCard(summary = summary, isCompact = true)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        SupportClusterCard(summary = summary, isCompact = false)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ResistanceClusterCard(summary = summary, isCompact = false)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 2. EFFORT VS RESULTS SECTION (5m, 1h, 4h) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(if (isCompact) 10.dp else 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                    contentDescription = "Effort vs Results",
                    tint = BinanceGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "EFFORT VS RESULTS ANALYSIS (5m • 1h • 4h)",
                    color = BinanceGold,
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            summary.effortVsResultsList.forEach { item ->
                EffortVsResultCard(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 3. OPTIONS INSTITUTIONAL LEVELS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurfaceVariant)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(if (isCompact) 10.dp else 14.dp)
        ) {
            Text(
                text = "OPTIONS INSTITUTIONAL LEVELS",
                color = TextPrimary,
                fontSize = if (isCompact) 10.sp else 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    Text(text = "Max Pain", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(
                        text = "$" + String.format(Locale.US, "%,.0f", summary.maxPainPrice),
                        color = TextPrimary,
                        fontSize = if (isCompact) 13.sp else 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Delta Neutral", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(
                        text = "$" + String.format(Locale.US, "%,.1f", summary.deltaNeutralPrice),
                        color = TextPrimary,
                        fontSize = if (isCompact) 13.sp else 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "Gamma Flip", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(
                        text = "$" + String.format(Locale.US, "%,.0f", summary.gammaFlipPrice),
                        color = TextPrimary,
                        fontSize = if (isCompact) 13.sp else 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 4. MARKET MECHANICS LEARNING INSIGHTS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(if (isCompact) 10.dp else 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Insights",
                    tint = BinanceGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MARKET MECHANICS LEARNING ENGINE",
                    color = BinanceGold,
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            summary.mechanicsInsights.forEach { insight ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "• ",
                        color = BinanceGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = insight,
                        color = TextSecondary,
                        fontSize = if (isCompact) 10.sp else 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SupportClusterCard(summary: OptionChainSummary, isCompact: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BinanceSurfaceVariant)
            .border(1.dp, BinanceGreen, RoundedCornerShape(12.dp))
            .padding(if (isCompact) 10.dp else 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SUPPORT CLUSTER",
                color = BinanceGreen,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x1F00C087))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "STRONG",
                    color = BinanceGreen,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = summary.srCluster.supportClusterRange,
            color = TextPrimary,
            fontSize = if (isCompact) 13.sp else 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Put OI: ${summary.srCluster.supportOiVolume} BTC",
            color = TextSecondary,
            fontSize = if (isCompact) 9.sp else 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun ResistanceClusterCard(summary: OptionChainSummary, isCompact: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BinanceSurfaceVariant)
            .border(1.dp, BinanceRed, RoundedCornerShape(12.dp))
            .padding(if (isCompact) 10.dp else 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RESISTANCE CLUSTER",
                color = BinanceRed,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x1FF6465D))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "STRONG",
                    color = BinanceRed,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = summary.srCluster.resistanceClusterRange,
            color = TextPrimary,
            fontSize = if (isCompact) 13.sp else 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Call OI: ${summary.srCluster.resistanceOiVolume} BTC",
            color = TextSecondary,
            fontSize = if (isCompact) 9.sp else 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun EffortVsResultCard(item: EffortVsResultItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BinanceSurfaceVariant)
            .border(1.dp, BinanceCardBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BinanceGold)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.timeframe,
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.directionalEffort,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(item.statusBadgeColorHex).copy(alpha = 0.15f))
                    .border(1.dp, Color(item.statusBadgeColorHex), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = item.status,
                    color = Color(item.statusBadgeColorHex),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Volume Effort: ${item.effortVolume} BTC",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Price Result: ${if (item.priceResultPercent >= 0) "+${item.priceResultPercent}%" else "${item.priceResultPercent}%"}",
                color = if (item.priceResultPercent >= 0) BinanceGreen else BinanceRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

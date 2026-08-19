package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.BinanceSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun OptionChainTable(
    summary: OptionChainSummary,
    filterAtm5: Boolean,
    onToggleFilterAtm5: (Boolean) -> Unit,
    selectedStrike: Double?,
    onSelectStrike: (Double?) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600

    val displayedStrikes = if (filterAtm5) {
        summary.strikes.filter { it.isAtmRange }
    } else {
        summary.strikes
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BinanceSurfaceVariant)
            .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
            .testTag("option_chain_table")
    ) {
        // Table Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 10.dp else 14.dp, vertical = if (isCompact) 8.dp else 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "OPTION CHAIN & STRIKE PCR",
                    color = BinanceGold,
                    fontSize = if (isCompact) 10.sp else 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.3.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${summary.symbol} • ${summary.expiryDate}",
                    color = TextMuted,
                    fontSize = if (isCompact) 9.sp else 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // ATM ±5 Switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ATM ±5",
                    color = if (filterAtm5) BinanceGold else TextMuted,
                    fontSize = if (isCompact) 9.sp else 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                    checked = filterAtm5,
                    onCheckedChange = onToggleFilterAtm5,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = BinanceGold,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = BinanceSurface
                    ),
                    modifier = Modifier
                        .height(20.dp)
                        .testTag("atm5_filter_switch")
                )
            }
        }

        // Columns Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BinanceSurface)
                .padding(horizontal = if (isCompact) 8.dp else 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CALL OI / VOL",
                color = BinanceRed,
                fontSize = if (isCompact) 9.sp else 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1.1f),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "PCR",
                color = TextMuted,
                fontSize = if (isCompact) 9.sp else 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = "STRIKE",
                color = BinanceGold,
                fontSize = if (isCompact) 9.sp else 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(0.9f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = "PUT OI / VOL",
                color = BinanceGreen,
                fontSize = if (isCompact) 9.sp else 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1.1f),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Standard Column iteration
        Column(modifier = Modifier.fillMaxWidth()) {
            displayedStrikes.forEach { strike ->
                val isSelected = selectedStrike == strike.strikePrice
                val isAtmRow = strike.isAtm

                val pcrColor = when {
                    strike.pcrOi >= 1.1 -> BinanceGreen
                    strike.pcrOi <= 0.9 -> BinanceRed
                    else -> TextSecondary
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when {
                                isAtmRow -> Color(0x1AFFD700)
                                isSelected -> Color(0x222E3133)
                                else -> Color.Transparent
                            }
                        )
                        .clickable {
                            if (isSelected) onSelectStrike(null) else onSelectStrike(strike.strikePrice)
                        }
                        .padding(horizontal = if (isCompact) 8.dp else 14.dp, vertical = if (isCompact) 7.dp else 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Call OI
                        Column(modifier = Modifier.weight(1.1f), horizontalAlignment = Alignment.Start) {
                            Text(
                                text = String.format(Locale.US, "%.0f", strike.callOi),
                                color = TextPrimary,
                                fontSize = if (isCompact) 11.sp else 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                            Text(
                                text = "V: " + String.format(Locale.US, "%.1f", strike.callVol),
                                color = TextMuted,
                                fontSize = if (isCompact) 8.sp else 9.sp,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                        }

                        // Strike PCR Badge
                        Box(
                            modifier = Modifier
                                .weight(0.7f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (strike.pcrOi >= 1.0) BinanceGreenBg else BinanceRedBg)
                                .padding(vertical = 2.dp, horizontal = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.2f", strike.pcrOi),
                                color = pcrColor,
                                fontSize = if (isCompact) 10.sp else 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                        }

                        // Strike Price & ATM Indicator
                        Row(
                            modifier = Modifier.weight(0.9f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.0f", strike.strikePrice),
                                color = if (isAtmRow) BinanceGold else TextPrimary,
                                fontSize = if (isCompact) 11.sp else 13.sp,
                                fontWeight = if (isAtmRow) FontWeight.Black else FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                            if (isAtmRow) {
                                Spacer(modifier = Modifier.width(2.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(BinanceGold)
                                        .padding(horizontal = 2.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "ATM",
                                        color = Color.Black,
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }

                        // Put OI
                        Column(modifier = Modifier.weight(1.1f), horizontalAlignment = Alignment.End) {
                            Text(
                                text = String.format(Locale.US, "%.0f", strike.putOi),
                                color = TextPrimary,
                                fontSize = if (isCompact) 11.sp else 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                            Text(
                                text = "V: " + String.format(Locale.US, "%.1f", strike.putVol),
                                color = TextMuted,
                                fontSize = if (isCompact) 8.sp else 9.sp,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                        }
                    }

                    // Strike Detail Expanded View
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BinanceSurface)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "STRIKE ${strike.strikePrice.toInt()} GREEKS & MARKET DEPTH",
                                color = BinanceGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Call IV: ${strike.callIv}% | Delta: ${strike.callDelta}",
                                    color = BinanceRed,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Put IV: ${strike.putIv}% | Delta: ${strike.putDelta}",
                                    color = BinanceGreen,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(BinanceCardBorder.copy(alpha = 0.5f))
                )
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OptionChainSummary
import com.example.ui.theme.BinanceBackground
import com.example.ui.theme.BinanceCardBorder
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.BinanceGreen
import com.example.ui.theme.BinanceRed
import com.example.ui.theme.BinanceSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun HeaderSection(
    summary: OptionChainSummary,
    availableSymbols: List<String>,
    availableExpiries: List<String>,
    onSelectSymbol: (String) -> Unit,
    onSelectExpiry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var symbolMenuExpanded by remember { mutableStateOf(false) }
    var expiryMenuExpanded by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BinanceBackground)
            .padding(horizontal = if (isCompact) 10.dp else 16.dp, vertical = if (isCompact) 8.dp else 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Symbol Switcher
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { symbolMenuExpanded = true }
                        .padding(vertical = 2.dp, horizontal = 2.dp)
                        .testTag("symbol_selector")
                ) {
                    Text(
                        text = summary.symbol,
                        color = BinanceGold,
                        fontSize = if (isCompact) 20.sp else 28.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = (-0.5).sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Symbol",
                        tint = BinanceGold,
                        modifier = Modifier.size(if (isCompact) 22.dp else 28.dp)
                    )
                }

                DropdownMenu(
                    expanded = symbolMenuExpanded,
                    onDismissRequest = { symbolMenuExpanded = false },
                    modifier = Modifier.background(BinanceSurface)
                ) {
                    availableSymbols.forEach { symbol ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = symbol,
                                    color = if (symbol == summary.symbol) BinanceGold else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            onClick = {
                                onSelectSymbol(symbol)
                                symbolMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Expiry & Live Status Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Expiry selector
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(BinanceSurface)
                            .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                            .clickable { expiryMenuExpanded = true }
                            .padding(horizontal = if (isCompact) 8.dp else 10.dp, vertical = if (isCompact) 4.dp else 5.dp)
                            .testTag("expiry_selector")
                    ) {
                        Text(
                            text = summary.expiryDate,
                            color = TextSecondary,
                            fontSize = if (isCompact) 10.sp else 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Expiry",
                            tint = TextSecondary,
                            modifier = Modifier.size(if (isCompact) 14.dp else 16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expiryMenuExpanded,
                        onDismissRequest = { expiryMenuExpanded = false },
                        modifier = Modifier.background(BinanceSurface)
                    ) {
                        availableExpiries.forEach { exp ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = exp,
                                        color = if (exp == summary.expiryDate) BinanceGold else TextPrimary,
                                        fontFamily = FontFamily.Monospace
                                    )
                                },
                                onClick = {
                                    onSelectExpiry(exp)
                                    expiryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // WS Live Dot
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 8.dp else 10.dp)
                        .clip(CircleShape)
                        .background(
                            if (summary.isWsConnected) BinanceGreen else BinanceRed
                        )
                        .alpha(if (summary.isWsConnected) pulseAlpha else 1.0f)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Spot Price & PCR Quick Stat
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$" + String.format(Locale.US, "%,.2f", summary.spotPrice),
                        color = TextPrimary,
                        fontSize = if (isCompact) 17.sp else 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (summary.priceChangePercent >= 0) "+${summary.priceChangePercent}%" else "${summary.priceChangePercent}%",
                        color = if (summary.priceChangePercent >= 0) BinanceGreen else BinanceRed,
                        fontSize = if (isCompact) 11.sp else 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
                Text(
                    text = "MARKET MECHANICS • LIVE WS",
                    color = TextMuted,
                    fontSize = if (isCompact) 8.sp else 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Quick ATM ±5 PCR Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2E3133))
                    .border(1.dp, BinanceCardBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = if (isCompact) 8.dp else 12.dp, vertical = if (isCompact) 4.dp else 6.dp)
                    .testTag("atm5_pcr_pill")
            ) {
                Text(
                    text = "ATM ±5 PCR",
                    color = TextSecondary,
                    fontSize = if (isCompact) 9.sp else 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.3.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = String.format(Locale.US, "%.2f", summary.atm5Pcr),
                    color = if (summary.atm5Pcr >= 1.0) BinanceGreen else BinanceRed,
                    fontSize = if (isCompact) 12.sp else 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}


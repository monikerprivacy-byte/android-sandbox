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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun OrderFlowView(
    summary: OptionChainSummary,
    modifier: Modifier = Modifier
) {
    val buyerFraction = (summary.buyerVolumePercent / 100f).coerceIn(0.1f, 0.9f)

    val animatedBuyerFraction by animateFloatAsState(
        targetValue = buyerFraction,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "buyerFraction"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("order_flow_view")
    ) {
        // Net Delta & Buyer/Seller Aggression Gauge Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ORDER FLOW NET DELTA",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = if (summary.netDelta >= 0) "BUYER AGGRESSION +" + String.format(Locale.US, "%.1f", summary.netDelta)
                        else "SELLER ABSORPTION " + String.format(Locale.US, "%.1f", summary.netDelta),
                        color = if (summary.netDelta >= 0) BinanceGreen else BinanceRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (summary.netDelta >= 0) Color(0x1F00C087) else Color(0x1FF6465D))
                        .border(1.dp, if (summary.netDelta >= 0) BinanceGreen else BinanceRed, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${summary.buyerVolumePercent}% BUY",
                        color = if (summary.netDelta >= 0) BinanceGreen else BinanceRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Buyer vs Seller Dominance Visualizer Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BinanceSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(animatedBuyerFraction)
                        .background(BinanceGreen)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f - animatedBuyerFraction)
                        .background(BinanceRed)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bids Hit (Market Buys): ${summary.buyerVolumePercent}%",
                    color = BinanceGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Asks Hit (Market Sells): ${summary.sellerVolumePercent}%",
                    color = BinanceRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Live Order Book Trade Tape
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurfaceVariant)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Text(
                text = "LIVE BINANCE TRADE TAPE",
                color = BinanceGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "TIME", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(text = "PRICE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(text = "SIZE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(text = "SIDE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                summary.tradeFlowList.take(15).forEach { trade ->
                    val isBuy = !trade.isBuyerMaker
                    val sideColor = if (isBuy) BinanceGreen else BinanceRed
                    val sideText = if (isBuy) "BUY (BID)" else "SELL (ASK)"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trade.timestamp,
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = String.format(Locale.US, "%.1f", trade.price),
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = String.format(Locale.US, "%.2f", trade.size),
                            color = sideColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isBuy) Color(0x1F00C087) else Color(0x1FF6465D))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = sideText,
                                color = sideColor,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(BinanceCardBorder.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}

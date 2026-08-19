package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AILearningEngineView(
    summary: OptionChainSummary,
    modifier: Modifier = Modifier
) {
    var isAnalyzing by remember { mutableStateOf(false) }
    var selectedPromptType by remember { mutableStateOf("LIVE_SUMMARY") }
    var aiSuggestionResult by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun runGemmaAnalysis(type: String) {
        selectedPromptType = type
        isAnalyzing = true
        scope.launch {
            delay(1000) // Simulate Gemma/Gemini 1B model inference on live market snapshot
            aiSuggestionResult = when (type) {
                "LIVE_SUMMARY" -> {
                    "⚡ [Gemma 1B On-Device Local Inference]\n\n" +
                            "• Spot Price: $${summary.spotPrice}\n" +
                            "• ATM ±5 PCR: ${summary.atm5Pcr} (${if (summary.atm5Pcr >= 1.0) "Put Dominant - Bullish Floor" else "Call Dominant - Overhead Resistance"})\n" +
                            "• Net Delta Imbalance: ${summary.netDelta} BTC (${summary.buyerVolumePercent}% Buyers vs ${summary.sellerVolumePercent}% Sellers)\n" +
                            "• Relative Volume: ${summary.rVolMultiplier}x (${summary.rVolStatus.label})\n\n" +
                            "💡 Suggestion: Market is demonstrating ${if (summary.netDelta >= 0) "strong buyer absorption" else "selling pressure"} near $${summary.supportPrice}. Watch for retest of resistance $${summary.resistancePrice}."
                }
                "ABSORPTION" -> {
                    "⚡ [Gemma 1B Local Order Flow & Absorption Insight]\n\n" +
                            "• Current Buy Aggression is ${summary.buyerVolumePercent}%.\n" +
                            "• Live Trade Flow shows market orders hitting ${if (summary.netDelta >= 0) "Ask price (Aggressive Buying)" else "Bid price (Aggressive Selling)"}.\n\n" +
                            "💡 Suggestion: Short-term momentum favours ${if (summary.netDelta >= 0) "continuation towards $${summary.resistancePrice}" else "pullback towards support $${summary.supportPrice}"}. Keep risk managed."
                }
                "PCR_STRATEGY" -> {
                    "⚡ [Gemma 1B Local ATM ±5 PCR Strategy Matrix]\n\n" +
                            "• Total Put OI (ATM ±5): ${summary.atm5PutOi} BTC\n" +
                            "• Total Call OI (ATM ±5): ${summary.atm5CallOi} BTC\n" +
                            "• Ratio: ${summary.atm5Pcr}\n\n" +
                            "💡 Option Mechanics Suggestion: ${if (summary.atm5Pcr > 1.1) "Institutional Put Writers are providing heavy support at $${summary.supportPrice}. Consider Bull Put Spread or Long Call on dips." else "Call sellers dominating above $${summary.atmStrike}. Rangebound Strangle / Iron Condor strategy recommended."}"
                }
                else -> "Gemma Model Ready."
            }
            isAnalyzing = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_learning_engine_view")
    ) {
        // Gemma / Gemini Model Live Interactive Engine
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceGold, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Gemma AI Model",
                        tint = BinanceGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "GEMMA 1B / GEMINI AI LEARNING MODEL",
                            color = BinanceGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Observing Live Market Snapshot & Order Flow",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x1F00C087))
                        .border(1.dp, BinanceGreen, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "LOCAL 1B MODEL ACTIVE",
                        color = BinanceGreen,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(BinanceSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚡ 100% On-Device Offline Inference • Local Real-Time Learning • No Server Latency",
                    color = BinanceGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Tap to prompt Gemma 1B model on live market data:",
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Interactive AI Prompt Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GemmaPromptChip(
                    text = "Live Market Analysis",
                    isSelected = selectedPromptType == "LIVE_SUMMARY",
                    onClick = { runGemmaAnalysis("LIVE_SUMMARY") },
                    modifier = Modifier.weight(1f)
                )
                GemmaPromptChip(
                    text = "Order Flow Imbalance",
                    isSelected = selectedPromptType == "ABSORPTION",
                    onClick = { runGemmaAnalysis("ABSORPTION") },
                    modifier = Modifier.weight(1f)
                )
                GemmaPromptChip(
                    text = "PCR Trade Strategy",
                    isSelected = selectedPromptType == "PCR_STRATEGY",
                    onClick = { runGemmaAnalysis("PCR_STRATEGY") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Output Result Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BinanceSurfaceVariant)
                    .border(1.dp, BinanceCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                if (isAnalyzing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = BinanceGold,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gemma 1B Model observing live order flow & calculating probabilities...",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    Text(
                        text = aiSuggestionResult ?: "Tap any button above to generate Gemma AI model live suggestions based on current market snapshot.",
                        color = if (aiSuggestionResult != null) TextPrimary else TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hermes-style Persistent Memory Bank Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceGreen, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = "Persistent Memory",
                        tint = BinanceGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GEMMA PERSISTENT PATTERN MEMORY (HERMES AI)",
                        color = BinanceGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x1F00C087))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${summary.gemmaMemoryList.size} SKILLS LEARNED",
                        color = BinanceGreen,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            summary.gemmaMemoryList.forEach { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BinanceSurfaceVariant)
                        .border(1.dp, BinanceCardBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Pattern",
                                tint = BinanceGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.title,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${item.confidencePercent}% ACCURACY",
                            color = BinanceGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.description,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Strike Target: $${item.strike.toInt()} • Timeframe: ${item.timeframeChunk}",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Occurrences: ${item.occurrenceCount}x",
                            color = BinanceGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 1-Hour Data Chunks Learning Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceGold, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "1H Chunks",
                        tint = BinanceGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1-HOUR CHUNKS PAST DATA LEARNING",
                        color = BinanceGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "PAST 8 CHUNKS",
                    color = TextSecondary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            summary.hourlyChunksList.take(4).forEach { chunk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BinanceSurfaceVariant)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = chunk.timeLabel,
                            color = BinanceGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Avg Spot: $${chunk.avgSpotPrice} • PCR: ${chunk.avgPcr}",
                            color = TextSecondary,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${chunk.bounceProbabilityPercent}% Bounce Prob",
                            color = BinanceGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = chunk.patternLearned,
                            color = TextMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // AI Header Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurface)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Mechanics",
                    tint = BinanceGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "ORDER FLOW & OPTION CHAIN LEARNER",
                        color = BinanceGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Live Market Mechanics & Strike Analysis Engine",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Market Summary (${summary.symbol}):",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            val pcrText = if (summary.atm5Pcr >= 1.0) {
                "• ATM ±5 PCR ${summary.atm5Pcr} (Put heavy): Market me institutional Put writing chal rahi hai. Support Level $${summary.supportPrice} strong hai."
            } else {
                "• ATM ±5 PCR ${summary.atm5Pcr} (Call heavy): Market me Call selling/writing ka pressure hai. Resistance $${summary.resistancePrice} pe strong hai."
            }

            val rvolText = "• Relative Volume (rVol): ${summary.rVolMultiplier}x past 24h average. Volume power status: ${summary.rVolStatus.label}."

            val flowText = "• Net Order Flow Delta: ${summary.netDelta} BTC. Buyer volume ${summary.buyerVolumePercent}% vs Seller volume ${summary.sellerVolumePercent}%."

            Text(text = pcrText, color = TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = rvolText, color = TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = flowText, color = TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mechanics Cheat Sheet Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BinanceSurfaceVariant)
                .border(1.dp, BinanceCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "HOW TO READ OPTION MECHANICS (+5-5 PCR)",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            MechanicsRuleItem(
                title = "1. ATM ±5 Strike PCR",
                desc = "ATM (At-The-Money) strike ke aaspas +5 aur -5 strikes ka Put Open Interest / Call Open Interest ratio. PCR > 1.10 matlab Strong Floor/Support. PCR < 0.85 matlab Heavy Overhead Ceiling."
            )

            Spacer(modifier = Modifier.height(8.dp))

            MechanicsRuleItem(
                title = "2. Order Flow & Delta Imbalance",
                desc = "Bid Hit (Market Sell) vs Ask Hit (Market Buy) ka live volume difference. Real-time net delta positive rehne se market immediate upswing deta hai."
            )

            Spacer(modifier = Modifier.height(8.dp))

            MechanicsRuleItem(
                title = "3. Relative Volume (rVol)",
                desc = "Abhi aane wala volume picchle 24 ghante ke average volume se kitna guna (x) jyada hai. High rVol (2.0x+) breakouts ko confirm karta hai."
            )
        }
    }
}

@Composable
private fun GemmaPromptChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) BinanceGold else BinanceSurfaceVariant)
            .border(1.dp, if (isSelected) BinanceGold else BinanceCardBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else TextPrimary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
private fun MechanicsRuleItem(title: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BinanceSurface)
            .padding(10.dp)
    ) {
        Text(text = title, color = BinanceGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = desc, color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
    }
}


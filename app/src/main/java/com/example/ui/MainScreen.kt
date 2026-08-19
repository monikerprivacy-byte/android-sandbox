package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AILearningEngineView
import com.example.ui.components.HeaderSection
import com.example.ui.components.MarketMechanicsView
import com.example.ui.components.OptionChainTable
import com.example.ui.components.OrderFlowView
import com.example.ui.components.PCRSummaryCard
import com.example.ui.components.RVolPowerCard
import com.example.ui.theme.BinanceBackground
import com.example.ui.theme.BinanceCardBorder
import com.example.ui.theme.BinanceGold
import com.example.ui.theme.BinanceSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NavigationTab
import com.example.ui.viewmodel.OptionChainViewModel

import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun MainScreen(
    viewModel: OptionChainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val chainSummary by viewModel.chainState.collectAsStateWithLifecycle()
    val currentTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val showOnlyAtm5 by viewModel.showOnlyAtm5.collectAsStateWithLifecycle()
    val selectedStrikeDetail by viewModel.selectedStrikeForDetail.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600
    val horizontalPadding = if (isCompact) 8.dp else 16.dp

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BinanceBackground,
        bottomBar = {
            BottomNavBar(
                selectedTab = currentTab,
                onSelectTab = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BinanceBackground)
        ) {
            HeaderSection(
                summary = chainSummary,
                availableSymbols = viewModel.availableSymbols,
                availableExpiries = viewModel.availableExpiries,
                onSelectSymbol = { viewModel.selectSymbol(it) },
                onSelectExpiry = { viewModel.selectExpiry(it) }
            )

            // Gemma 1B Local AI Popup Suggestion Banner across all screens
            com.example.ui.components.GemmaGlobalPopupBanner(
                alert = chainSummary.gemmaAlert,
                onOpenAiTab = { viewModel.selectTab(NavigationTab.AI_LEARNING) }
            )

            AnimatedContent(
                targetState = currentTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { targetTab ->
                when (targetTab) {
                    NavigationTab.OPTION_CHAIN -> {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = horizontalPadding)
                                .verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            PCRSummaryCard(summary = chainSummary)
                            Spacer(modifier = Modifier.height(10.dp))
                            OptionChainTable(
                                summary = chainSummary,
                                filterAtm5 = showOnlyAtm5,
                                onToggleFilterAtm5 = { viewModel.toggleAtm5Filter() },
                                selectedStrike = selectedStrikeDetail,
                                onSelectStrike = { viewModel.selectStrikeForDetail(it) }
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    NavigationTab.ORDER_FLOW -> {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = horizontalPadding)
                                .verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            RVolPowerCard(summary = chainSummary)
                            Spacer(modifier = Modifier.height(10.dp))
                            OrderFlowView(summary = chainSummary)
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    NavigationTab.MARKET_MECHANICS -> {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = horizontalPadding)
                                .verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            MarketMechanicsView(summary = chainSummary)
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    NavigationTab.AI_LEARNING -> {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = horizontalPadding)
                                .verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            AILearningEngineView(summary = chainSummary)
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BottomNavBar(
    selectedTab: NavigationTab,
    onSelectTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BinanceSurfaceVariant)
            .border(1.dp, BinanceCardBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val iconColor = if (isSelected) BinanceGold else TextMuted
            val textColor = if (isSelected) BinanceGold else TextMuted

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSelectTab(tab) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("nav_tab_${tab.name.lowercase()}")
            ) {
                Icon(
                    imageVector = when (tab) {
                        NavigationTab.OPTION_CHAIN -> Icons.Default.Leaderboard
                        NavigationTab.ORDER_FLOW -> Icons.Default.Analytics
                        NavigationTab.MARKET_MECHANICS -> Icons.AutoMirrored.Filled.ShowChart
                        NavigationTab.AI_LEARNING -> Icons.Default.Psychology
                    },
                    contentDescription = tab.label,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = when (tab) {
                        NavigationTab.OPTION_CHAIN -> "Chain (+5-5)"
                        NavigationTab.ORDER_FLOW -> "Order Flow"
                        NavigationTab.MARKET_MECHANICS -> "Mechanics"
                        NavigationTab.AI_LEARNING -> "AI Engine"
                    },
                    color = textColor,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                    letterSpacing = 0.2.sp
                )
            }
        }
    }
}

@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.jwoglom.wearx2.presentation.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.jwoglom.pumpx2.pump.messages.Message
import com.jwoglom.pumpx2.pump.messages.calculator.BolusCalcCondition
import com.jwoglom.pumpx2.pump.messages.calculator.BolusCalcDecision
import com.jwoglom.pumpx2.pump.messages.calculator.BolusCalcUnits
import com.jwoglom.pumpx2.pump.messages.calculator.BolusCalculatorBuilder
import com.jwoglom.pumpx2.pump.messages.calculator.BolusParameters
import com.jwoglom.pumpx2.pump.messages.models.InsulinUnit
import com.jwoglom.pumpx2.pump.messages.request.control.BolusPermissionRequest
import com.jwoglom.pumpx2.pump.messages.request.control.CancelBolusRequest
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BolusCalcDataSnapshotRequest
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CurrentBolusStatusRequest
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.LastBGRequest
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.TimeSinceResetRequest
import com.jwoglom.pumpx2.pump.messages.response.control.BolusPermissionResponse
import com.jwoglom.pumpx2.pump.messages.response.control.CancelBolusResponse
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BolusCalcDataSnapshotResponse
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentBolusStatusResponse
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.LastBGResponse
import com.jwoglom.wearx2.LocalDataStore
import com.jwoglom.wearx2.R
import com.jwoglom.wearx2.presentation.DataStore
import com.jwoglom.wearx2.presentation.screens.setUpPreviewState
import com.jwoglom.wearx2.presentation.theme.WearX2Theme
import com.jwoglom.wearx2.shared.presentation.LifecycleStateObserver
import com.jwoglom.wearx2.shared.util.SendType
import com.jwoglom.wearx2.shared.util.firstLetterCapitalized
import com.jwoglom.wearx2.shared.util.oneDecimalPlace
import com.jwoglom.wearx2.shared.util.snakeCaseToSpace
import com.jwoglom.wearx2.shared.util.twoDecimalPlaces
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@Composable
fun BolusWindow(
    navController: NavHostController? = null,
    sendMessage: (String, ByteArray) -> Unit,
    sendPumpCommands: (SendType, List<Message>) -> Unit,
    modifier: Modifier = Modifier,
) {

//    var showPermissionCheckDialog by remember { mutableStateOf(false) }
//    var showConfirmDialog by remember { mutableStateOf(false) }
//    var showInProgressDialog by remember { mutableStateOf(false) }
//    var showCancelledDialog by remember { mutableStateOf(false) }
//    var showCancellingDialog by remember { mutableStateOf(false) }
//    var showApprovedDialog by remember { mutableStateOf(false) }
//
//    var bolusUnitsUserInput by remember { mutableStateOf<Double?>(null) }
//    var bolusCarbsGramsUserInput by remember { mutableStateOf<Int?>(null) }
//    var bolusBgMgdlUserInput by remember { mutableStateOf<Int?>(null) }
//
//    fun resetDialogs() {
//        showPermissionCheckDialog = false
//        showConfirmDialog = false
//        showInProgressDialog = false
//        showCancelledDialog = false
//        showCancellingDialog = false
//        showApprovedDialog = false
//    }
//
//    val refreshScope = rememberCoroutineScope()
//    var refreshing by remember { mutableStateOf(true) }
//
//    val dataStore = LocalDataStore.current
//
//    fun runBolusCalculator(
//        dataSnapshot: BolusCalcDataSnapshotResponse?,
//        lastBG: LastBGResponse?,
//        bolusUnitsUserInput: Double?,
//        bolusCarbsGramsUserInput: Int?,
//        bolusBgMgdlUserInput: Int?
//    ): BolusCalculatorBuilder {
//        Timber.i("runBolusCalculator: INPUT units=$bolusUnitsUserInput carbs=$bolusCarbsGramsUserInput bg=$bolusBgMgdlUserInput dataSnapshot=$dataSnapshot lastBG=$lastBG")
//        val bolusCalc = BolusCalculatorBuilder()
//        if (dataSnapshot != null) {
//            bolusCalc.onBolusCalcDataSnapshotResponse(dataSnapshot)
//        }
//        if (lastBG != null) {
//            bolusCalc.onLastBGResponse(lastBG)
//        }
//
//        bolusCalc.setInsulinUnits(bolusUnitsUserInput)
//        bolusCalc.setCarbsValueGrams(bolusCarbsGramsUserInput)
//        bolusCalc.setGlucoseMgdl(bolusBgMgdlUserInput)
//        return bolusCalc
//    }
//
//    fun bolusCalcDecision(bolusCalc: BolusCalculatorBuilder?): BolusCalcDecision? {
//        val decision = bolusCalc?.build()?.parse()
//        Timber.i("bolusCalcDecision: OUTPUT units=${decision?.units} conditions=${decision?.conditions}")
//        return decision
//    }
//
//    fun bolusCalcParameters(bolusCalc: BolusCalculatorBuilder?): Pair<BolusParameters, BolusCalcUnits> {
//        val decision = bolusCalc?.build()?.parse()
//        return Pair(
//            BolusParameters(
//                decision?.units?.total,
//                bolusCalc?.carbsValueGrams?.orElse(0),
//                bolusCalc?.glucoseMgdl?.orElse(0)
//            ),
//            decision!!.units!!)
//    }
//
//    val commands = listOf(
//        BolusCalcDataSnapshotRequest(),
//        LastBGRequest(),
//        TimeSinceResetRequest(),
//    )
//
//    val baseFields = listOf(
//        dataStore.bolusCalcDataSnapshot,
//        dataStore.bolusCalcLastBG
//    )
//
//    val calculatedFields = listOf(
//        dataStore.bolusCalculatorBuilder,
//        dataStore.bolusCurrentParameters
//    )
//
//    @Synchronized
//    fun waitForLoaded() = refreshScope.launch {
//        var sinceLastFetchTime = 0
//        while (true) {
//            val nullBaseFields = baseFields.filter { field -> field.value == null }.toSet()
//            if (nullBaseFields.isEmpty()) {
//                break
//            }
//
//            Timber.i("BolusScreen loading: remaining ${nullBaseFields.size}: ${baseFields.map { it.value }}")
//            if (sinceLastFetchTime >= 2500) {
//                Timber.i("BolusScreen loading re-fetching")
//                // for safety reasons, NEVER CACHE.
//                sendPumpCommands(SendType.STANDARD, commands)
//                sinceLastFetchTime = 0
//            }
//
//            withContext(Dispatchers.IO) {
//                Thread.sleep(250)
//            }
//            sinceLastFetchTime += 250
//        }
//        Timber.i("BolusScreen base loading done: ${baseFields.map { it.value }}")
//        refreshing = false
//    }
//
//    fun refresh() = refreshScope.launch {
//        refreshing = true
//
//        baseFields.forEach { field -> field.value = null }
//        calculatedFields.forEach { field -> field.value = null }
//        sendPumpCommands(SendType.BUST_CACHE, commands)
//    }
//
//    val state = rememberPullRefreshState(refreshing, ::refresh)
//
//    LifecycleStateObserver(lifecycleOwner = LocalLifecycleOwner.current, onStop = {
//    }) {
//        sendPumpCommands(SendType.BUST_CACHE, commands)
//    }
//
//    LaunchedEffect(refreshing) {
//        waitForLoaded()
//    }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .pullRefresh(state)
//    ) {
//        PullRefreshIndicator(
//            refreshing, state,
//            Modifier
//                .align(Alignment.TopCenter)
//                .zIndex(10f)
//        )
//
//        val bolusCalcDataSnapshot = dataStore.bolusCalcDataSnapshot.observeAsState()
//        val bolusCalcLastBG = dataStore.bolusCalcLastBG.observeAsState()
//
//        LaunchedEffect(
//            bolusCalcDataSnapshot.value,
//            bolusCalcLastBG.value,
//            bolusUnitsUserInput,
//            bolusCarbsGramsUserInput,
//            bolusBgMgdlUserInput
//        ) {
//            dataStore.bolusCalculatorBuilder.value = runBolusCalculator(
//                bolusCalcDataSnapshot.value,
//                bolusCalcLastBG.value,
//                bolusUnitsUserInput,
//                bolusCarbsGramsUserInput,
//                bolusBgMgdlUserInput
//            )
//            dataStore.bolusCurrentParameters.value =
//                bolusCalcParameters(dataStore.bolusCalculatorBuilder.value).first
//
//            dataStore.bolusUnitsDisplayedText.value = when (bolusUnitsUserInput) {
//                null -> when (dataStore.bolusCurrentParameters.value) {
//                    null -> "None Entered"
//                    else -> "Calculated: ${twoDecimalPlaces(dataStore.bolusCurrentParameters.value!!.units)}"
//                }
//                else -> "Entered: ${oneDecimalPlace(bolusUnitsUserInput!!)}"
//            }
//
//            val autofilledBg = dataStore.bolusCalculatorBuilder.value?.glucoseMgdl?.orElse(null)
//            dataStore.bolusBGDisplayedText.value = when {
//                bolusBgMgdlUserInput != null -> "Entered: $bolusBgMgdlUserInput"
//                autofilledBg != null -> "From CGM: $autofilledBg"
//                else -> "Not Entered"
//            }
//        }
//
//        LazyColumn {
//            item {
//                val bolusUnitsDisplayedText = dataStore.bolusUnitsDisplayedText.observeAsState()
//
//                OutlinedTextField(
//                    value =
//                )
//
//                Chip(
//                    onClick = {
//                        if (dataStore.bolusCurrentParameters.value != null) {
//                            onClickUnits(dataStore.bolusCurrentParameters.value!!.units)
//                        } else {
//                            onClickUnits(null)
//                        }
//                    },
//                    label = {
//                        Text(
//                            "Units",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    },
//                    secondaryLabel = {
//                        Text(
//                            text = "${bolusUnitsDisplayedText.value}",
//                        )
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 35.dp)
//                )
//            }
//
//            item {
//                Chip(
//                    onClick = onClickCarbs,
//                    label = {
//                        Text(
//                            "Carbs (g)",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    },
//                    secondaryLabel = {
//                        Text(
//                            text = when (bolusCarbsGramsUserInput) {
//                                null -> "Not Entered"
//                                else -> "$bolusCarbsGramsUserInput"
//                            }
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            item {
//                val bolusBGDisplayedText = dataStore.bolusBGDisplayedText.observeAsState()
//
//                Chip(
//                    onClick = onClickBG,
//                    label = {
//                        Text(
//                            "BG (mg/dL)",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    },
//                    secondaryLabel = {
//                        Text(
//                            text = "${bolusBGDisplayedText.value}",
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            fun performPermissionCheck() {
//                showPermissionCheckDialog = true
//                sendPumpCommands(SendType.BUST_CACHE, listOf(BolusPermissionRequest()))
//            }
//
//            item {
//                CompactButton(
//                    onClick = {
//                        dataStore.bolusFinalConditions.value =
//                            bolusCalcDecision(dataStore.bolusCalculatorBuilder.value)?.conditions
//
//                        val pair = bolusCalcParameters(dataStore.bolusCalculatorBuilder.value)
//                        dataStore.bolusFinalParameters.value = pair.first
//                        dataStore.bolusFinalCalcUnits.value = pair.second
//                        performPermissionCheck()
//                    },
//                    enabled = true
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Check,
//                        contentDescription = "continue",
//                        modifier = Modifier
//                            .size(ButtonDefaults.SmallIconSize)
//                            .wrapContentSize(align = Alignment.Center),
//                    )
//                }
//            }
//
//            fun sortConditions(set: Set<BolusCalcCondition>?): List<BolusCalcCondition> {
//                if (set == null) {
//                    return emptyList()
//                }
//
//                return set.sortedWith(
//                    compareBy(
//                        { it.javaClass == BolusCalcCondition.FailedSanityCheck::class.java },
//                        { it.javaClass == BolusCalcCondition.FailedPrecondition::class.java },
//                        { it.javaClass == BolusCalcCondition.WaitingOnPrecondition::class.java },
//                        { it.javaClass == BolusCalcCondition.Decision::class.java },
//                        { it.javaClass == BolusCalcCondition.NonActionDecision::class.java },
//                    )
//                )
//            }
//
//            items(5) { index ->
//                val bolusCalculatorBuilder = dataStore.bolusCalculatorBuilder.observeAsState()
//                val conditions = sortConditions(bolusCalculatorBuilder.value?.conditions)
//
//                if (index < conditions.size) {
//                    LineTextDescription(
//                        labelText = firstLetterCapitalized(conditions[index].msg),
//                        fontSize = 12.sp,
//                    )
//                } else {
//                    Spacer(modifier = Modifier.height(0.dp))
//                }
//            }
//        }
//
//        val scrollState = rememberScalingLazyListState()
//
//        Dialog(
//            showDialog = showPermissionCheckDialog,
//            onDismissRequest = {
//                showPermissionCheckDialog = false
//            },
//            scrollState = scrollState
//        ) {
//            val bolusFinalParameters = dataStore.bolusFinalParameters.observeAsState()
//
//            IndeterminateProgressIndicator(text = bolusFinalParameters.value?.units?.let { "Requesting permission" }
//                ?: "Invalid request!")
//        }
//
//        val bolusPermissionResponse = dataStore.bolusPermissionResponse.observeAsState()
//
//        LaunchedEffect(bolusPermissionResponse.value) {
//            if (bolusPermissionResponse.value != null && showPermissionCheckDialog) {
//                showConfirmDialog = true
//                showPermissionCheckDialog = false
//            }
//        }
//
//        fun sendBolusRequestToPhone(bolusParameters: BolusParameters?, unitBreakdown: BolusCalcUnits?) {
//            if (bolusParameters == null || dataStore.bolusPermissionResponse.value == null || dataStore.bolusCalcDataSnapshot.value == null || unitBreakdown == null) {
//                Timber.w("sendBolusRequestToPhone: null parameters")
//                return
//            }
//
//            val bolusId = dataStore.bolusPermissionResponse.value!!.bolusId
//            val iobUnits = InsulinUnit.from1000To1(dataStore.bolusCalcDataSnapshot.value!!.iob)
//
//            Timber.i("sendBolusRequestToPhone: sending bolus request to phone: bolusId=$bolusId bolusParameters=$bolusParameters unitBreakdown=$unitBreakdown iobUnits=$iobUnits")
//            sendPhoneBolusRequest(bolusId, bolusParameters, unitBreakdown, iobUnits)
//        }
//
//        Dialog(
//            showDialog = showConfirmDialog,
//            onDismissRequest = {
//                showConfirmDialog = false
//            },
//            scrollState = scrollState
//        ) {
//            val bolusFinalParameters = dataStore.bolusFinalParameters.observeAsState()
//
//            Alert(
//                title = {
//                    Text(
//                        text = bolusFinalParameters.value?.units?.let { "${twoDecimalPlaces(it)}u Bolus" }
//                            ?: "",
//                        textAlign = TextAlign.Center,
//                        color = MaterialTheme.colors.onBackground
//                    )
//                },
//                negativeButton = {
//                    Button(
//                        onClick = {
//                            showConfirmDialog = false
//                            dataStore.bolusFinalConditions.value = null
//                            dataStore.bolusFinalParameters.value = null
//                        },
//                        colors = ButtonDefaults.secondaryButtonColors()
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Clear,
//                            contentDescription = "Do not deliver bolus"
//                        )
//                    }
//                },
//                positiveButton = {
//                    bolusFinalParameters.value?.let { finalParameters ->
//                        bolusPermissionResponse.value?.let { permissionResponse ->
//                            if (permissionResponse.status == 0 && permissionResponse.nackReason == BolusPermissionResponse.NackReason.PERMISSION_GRANTED && finalParameters.units >= 0.05) {
//                                Button(
//                                    onClick = {
//                                        if (permissionResponse.status == 0 && permissionResponse.nackReason == BolusPermissionResponse.NackReason.PERMISSION_GRANTED && finalParameters.units >= 0.05) {
//                                            showConfirmDialog = false
//                                            showInProgressDialog = true
//                                            sendBolusRequestToPhone(dataStore.bolusFinalParameters.value, dataStore.bolusFinalCalcUnits.value)
//                                        }
//                                    },
//                                    colors = ButtonDefaults.primaryButtonColors()
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Filled.Check,
//                                        contentDescription = "Deliver bolus"
//                                    )
//                                }
//                            }
//                        }
//                    }
//                },
//                icon = {
//                    Image(
//                        painterResource(R.drawable.bolus_icon),
//                        "Bolus icon",
//                        Modifier.size(24.dp)
//                    )
//                },
//                scrollState = scrollState,
//            ) {
//                Text(
//                    text = bolusPermissionResponse.value?.let {
//                        when {
//                            bolusFinalParameters.value == null || bolusFinalParameters.value?.units == null -> ""
//                            bolusFinalParameters.value?.units!! < 0.05 -> "Insulin amount too small."
//                            it.status == 0 -> "Do you want to deliver the bolus?"
//                            else -> "Cannot deliver bolus: ${it.nackReason}"
//                        }
//                    } ?: "",
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.body2,
//                    color = MaterialTheme.colors.onBackground
//                )
//            }
//        }
//
//        Dialog(
//            showDialog = showCancellingDialog,
//            onDismissRequest = {
//                showCancellingDialog = false
//            },
//            scrollState = scrollState
//        ) {
//            val bolusCancelResponse = dataStore.bolusCancelResponse.observeAsState()
//
//            LaunchedEffect(bolusCancelResponse.value) {
//                if (bolusCancelResponse.value != null) {
//                    showCancelledDialog = true
//                }
//            }
//
//            IndeterminateProgressIndicator(text = "The bolus is being cancelled..")
//        }
//
//        Dialog(
//            showDialog = showCancelledDialog,
//            onDismissRequest = {
//                showCancelledDialog = false
//                resetBolusDataStoreState(dataStore)
//                onClickLanding()
//            },
//            scrollState = scrollState
//        ) {
//            val bolusCancelResponse = dataStore.bolusCancelResponse.observeAsState()
//            val bolusInitiateResponse = dataStore.bolusInitiateResponse.observeAsState()
//
//            Alert(
//                title = {
//                    Text(
//                        text = when (bolusCancelResponse.value?.status) {
//                            CancelBolusResponse.CancelStatus.SUCCESS ->
//                                "The bolus was cancelled."
//                            CancelBolusResponse.CancelStatus.FAILED ->
//                                when (bolusInitiateResponse.value) {
//                                    null -> "A bolus request was not sent to the pump, so there is nothing to cancel."
//                                    else -> "The bolus could not be cancelled: ${
//                                        snakeCaseToSpace(
//                                            bolusCancelResponse.value?.reason.toString()
//                                        )
//                                    }"
//                                }
//                            else -> "Please check your pump to confirm whether the bolus was cancelled."
//                        },
//                        textAlign = TextAlign.Center,
//                        color = MaterialTheme.colors.onBackground
//                    )
//                },
//                negativeButton = {
//                    Button(
//                        onClick = {
//                            onClickLanding()
//                            resetBolusDataStoreState(dataStore)
//                        },
//                        colors = ButtonDefaults.secondaryButtonColors(),
//                        modifier = Modifier.fillMaxWidth()
//
//                    ) {
//                        Text("OK")
//                    }
//                },
//                positiveButton = {},
//                icon = {
//                    Image(
//                        painterResource(R.drawable.bolus_icon),
//                        "Bolus icon",
//                        Modifier.size(24.dp)
//                    )
//                },
//                scrollState = scrollState,
//            )
//        }
//
//        fun cancelBolus() {
//            fun performCancel() {
//                bolusPermissionResponse.value?.bolusId?.let { bolusId ->
//                    sendPumpCommands(SendType.BUST_CACHE, listOf(CancelBolusRequest(bolusId)))
//                    showCancellingDialog = true
//                }
//            }
//            performCancel()
//            refreshScope.launch {
//                while (dataStore.bolusCancelResponse.value == null) {
//                    withContext(Dispatchers.IO) {
//                        Thread.sleep(250)
//                    }
//                    performCancel()
//                }
//                showCancellingDialog = false
//                showCancelledDialog = true
//                dataStore.bolusFinalConditions.value = null
//                dataStore.bolusFinalParameters.value = null
//                resetSavedBolusEnteredState()
//                sendPhoneBolusCancel()
//            }
//        }
//
//        Dialog(
//            showDialog = showInProgressDialog,
//            onDismissRequest = {
//                showInProgressDialog = false
//            },
//            scrollState = scrollState
//        ) {
//            val bolusFinalParameters = dataStore.bolusFinalParameters.observeAsState()
//            val bolusInitiateResponse = dataStore.bolusInitiateResponse.observeAsState()
//            val bolusCancelResponse = dataStore.bolusCancelResponse.observeAsState()
//
//            LaunchedEffect(bolusInitiateResponse.value) {
//                if (bolusInitiateResponse.value != null) {
//                    showApprovedDialog = true
//                }
//            }
//
//            LaunchedEffect(bolusCancelResponse.value) {
//                if (bolusCancelResponse.value != null) {
//                    showCancelledDialog = true
//                }
//            }
//
//            Alert(
//                title = {
//                    Text(
//                        text = when (bolusFinalParameters.value) {
//                            null -> ""
//                            else -> "${bolusFinalParameters.value?.units}u Bolus"
//                        },
//                        textAlign = TextAlign.Center,
//                        color = MaterialTheme.colors.onBackground
//                    )
//                },
//                negativeButton = {
//                    Button(
//                        onClick = {
//                            cancelBolus()
//                        },
//                        colors = ButtonDefaults.secondaryButtonColors(),
//                        modifier = Modifier.fillMaxWidth()
//
//                    ) {
//                        Text("Cancel")
//                    }
//                },
//                positiveButton = {},
//                scrollState = scrollState,
//                icon = {
//                    Image(
//                        painterResource(R.drawable.bolus_icon),
//                        "Bolus icon",
//                        Modifier.size(24.dp)
//                    )
//                }
//            ) {
//                Text(
//                    text = "A notification was sent to acknowledge the request.",
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.body2,
//                    color = MaterialTheme.colors.onBackground
//                )
//            }
//        }
//
//        Dialog(
//            showDialog = showApprovedDialog,
//            onDismissRequest = {
//                showApprovedDialog = false
//                resetBolusDataStoreState(dataStore)
//                resetSavedBolusEnteredState()
//                onClickLanding()
//            },
//            scrollState = scrollState
//        ) {
//            val bolusFinalParameters = dataStore.bolusFinalParameters.observeAsState()
//            val bolusInitiateResponse = dataStore.bolusInitiateResponse.observeAsState()
//            val bolusCurrentResponse = dataStore.bolusCurrentResponse.observeAsState()
//
//            Alert(
//                title = {
//                    Text(
//                        text = when {
//                            bolusInitiateResponse.value != null -> when {
//                                bolusInitiateResponse.value!!.wasBolusInitiated() -> "Bolus Initiated"
//                                else -> "Bolus Rejected by Pump"
//                            }
//                            else -> "Bolus Status Unknown"
//                        },
//                        textAlign = TextAlign.Center,
//                        color = MaterialTheme.colors.onBackground
//                    )
//                },
//                negativeButton = {
//                    Button(
//                        onClick = {
//                            cancelBolus()
//                        },
//                        colors = ButtonDefaults.secondaryButtonColors(),
//                        modifier = Modifier.fillMaxWidth()
//
//                    ) {
//                        Text("Cancel")
//                    }
//                },
//                positiveButton = {},
//                icon = {
//                    Image(
//                        painterResource(R.drawable.bolus_icon),
//                        "Bolus icon",
//                        Modifier.size(24.dp)
//                    )
//                },
//                scrollState = scrollState,
//            ) {
//                LaunchedEffect(Unit) {
//                    sendPumpCommands(SendType.BUST_CACHE, listOf(CurrentBolusStatusRequest()))
//                    refreshScope.launch {
//                        repeat(5) {
//                            Thread.sleep(1000)
//                            sendPumpCommands(
//                                SendType.BUST_CACHE,
//                                listOf(CurrentBolusStatusRequest())
//                            )
//                        }
//                    }
//                }
//
//                // When bolusCurrentResponse is updated, re-request it
//                LaunchedEffect(bolusCurrentResponse.value) {
//                    Timber.i("bolusCurrentResponse: ${bolusCurrentResponse.value}")
//                    // when a bolusId=0 is returned, the current bolus session has ended so the message
//                    // no longer contains any useful data.
//                    if (bolusCurrentResponse.value?.bolusId != 0) {
//                        sendPumpCommands(SendType.BUST_CACHE, listOf(CurrentBolusStatusRequest()))
//                        refreshScope.launch {
//                            repeat(5) {
//                                Thread.sleep(1000)
//                                sendPumpCommands(
//                                    SendType.BUST_CACHE,
//                                    listOf(CurrentBolusStatusRequest())
//                                )
//                            }
//                        }
//                    }
//                }
//
//                Text(
//                    text = when {
//                        bolusInitiateResponse.value != null -> when {
//                            bolusInitiateResponse.value!!.wasBolusInitiated() -> "The ${
//                                bolusFinalParameters.value?.let {
//                                    twoDecimalPlaces(
//                                        it.units
//                                    )
//                                }
//                            }u bolus ${
//                                when (bolusCurrentResponse.value) {
//                                    null -> "was requested."
//                                    else -> when (bolusCurrentResponse.value!!.status) {
//                                        CurrentBolusStatusResponse.CurrentBolusStatus.REQUESTING -> "is being prepared."
//                                        CurrentBolusStatusResponse.CurrentBolusStatus.DELIVERING -> "is being delivered."
//                                        else -> "was completed."
//                                    }
//                                }
//                            }"
//                            else -> "The bolus could not be delivered: ${
//                                bolusInitiateResponse.value?.let {
//                                    snakeCaseToSpace(
//                                        it.statusType.toString()
//                                    )
//                                }
//                            }"
//                        }
//                        else -> "The bolus status is unknown. Please check your pump to identify the status of the bolus."
//                    },
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.body2,
//                    color = MaterialTheme.colors.onBackground
//                )
//            }
//        }
//    }
}

fun resetBolusDataStoreState(dataStore: DataStore) {
    dataStore.bolusPermissionResponse.value = null
    dataStore.bolusCancelResponse.value = null
    dataStore.bolusInitiateResponse.value = null
    dataStore.bolusCalculatorBuilder.value = null
    dataStore.bolusCurrentParameters.value = null
    dataStore.bolusFinalParameters.value = null
    dataStore.bolusFinalConditions.value = null
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    WearX2Theme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            BolusWindow(
                sendMessage = {a, b -> },
                sendPumpCommands = {a, b -> },
            )
        }
    }
}
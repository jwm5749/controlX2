package com.jwoglom.wearx2.presentation

import androidx.lifecycle.MutableLiveData
import com.jwoglom.pumpx2.pump.messages.calculator.BolusCalcCondition
import com.jwoglom.pumpx2.pump.messages.calculator.BolusCalculatorBuilder
import com.jwoglom.pumpx2.pump.messages.calculator.BolusParameters
import com.jwoglom.pumpx2.pump.messages.response.control.BolusPermissionResponse
import com.jwoglom.pumpx2.pump.messages.response.control.CancelBolusResponse
import com.jwoglom.pumpx2.pump.messages.response.control.InitiateBolusResponse
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BolusCalcDataSnapshotResponse
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentBolusStatusResponse
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.LastBGResponse
import timber.log.Timber

class DataStore {
    val connectionStatus = MutableLiveData<String>()
    val batteryPercent = MutableLiveData<Int>()
    val iobUnits = MutableLiveData<Double>()
    val cartridgeRemainingUnits = MutableLiveData<Int>()
    val lastBolusStatus = MutableLiveData<String>()
    val controlIQStatus = MutableLiveData<String>()
    val controlIQMode = MutableLiveData<String>()
    val basalRate = MutableLiveData<String>()
    var basalStatus = MutableLiveData<String>()
    val cgmSessionState = MutableLiveData<String>()
    val cgmTransmitterStatus = MutableLiveData<String>()
    val cgmReading = MutableLiveData<Int>()
    val cgmDelta = MutableLiveData<Int>()
    val cgmStatusText = MutableLiveData<String>()
    val cgmHighLowState = MutableLiveData<String>()
    val cgmDeltaArrow = MutableLiveData<String>()
    val bolusCalcDataSnapshot = MutableLiveData<BolusCalcDataSnapshotResponse>()
    val bolusCalcLastBG = MutableLiveData<LastBGResponse>()
    val maxBolusAmount = MutableLiveData<Int>()

    var landingBasalDisplayedText = MutableLiveData<String>()
    var landingControlIQDisplayedText = MutableLiveData<String>()

    var bolusUnitsDisplayedText = MutableLiveData<String>()
    var bolusBGDisplayedText = MutableLiveData<String>()

    var bolusCalculatorBuilder = MutableLiveData<BolusCalculatorBuilder>()
    var bolusCurrentParameters = MutableLiveData<BolusParameters>()
    var bolusFinalParameters = MutableLiveData<BolusParameters>()
    var bolusFinalConditions = MutableLiveData<Set<BolusCalcCondition>>()

    var bolusPermissionResponse = MutableLiveData<BolusPermissionResponse>()
    var bolusInitiateResponse = MutableLiveData<InitiateBolusResponse>()
    var bolusCancelResponse = MutableLiveData<CancelBolusResponse>()
    var bolusCurrentResponse = MutableLiveData<CurrentBolusStatusResponse>()

    init {
        connectionStatus.observeForever { t -> Timber.i("DataStore.connectionStatus=$t") }
        batteryPercent.observeForever { t -> Timber.i("DataStore.batteryPercent=$t") }
        iobUnits.observeForever { t -> Timber.i("DataStore.iobUnits=$t") }
        cartridgeRemainingUnits.observeForever { t -> Timber.i("DataStore.cartridgeRemainingUnits=$t") }
        lastBolusStatus.observeForever { t -> Timber.i("DataStore.lastBolusStatus=$t") }
        controlIQStatus.observeForever { t -> Timber.i("DataStore.controlIQStatus=$t") }
        controlIQMode.observeForever { t -> Timber.i("DataStore.controlIQMode=$t") }
        basalRate.observeForever { t -> Timber.i("DataStore.basalRate=$t") }
        basalStatus.observeForever { t -> Timber.i("DataStore.basalStatus=$t") }
        cgmSessionState.observeForever { t -> Timber.i("DataStore.cgmSessionState=$t") }
        cgmTransmitterStatus.observeForever { t -> Timber.i("DataStore.cgmTransmitterStatus=$t") }
        cgmReading.observeForever { t -> Timber.i("DataStore.cgmLastReading=$t") }
        cgmDelta.observeForever { t -> Timber.i("DataStore.cgmDelta=$t") }
        cgmStatusText.observeForever { t -> Timber.i("DataStore.cgmStatusText=$t") }
        cgmHighLowState.observeForever { t -> Timber.i("DataStore.cgmHighLowState=$t") }
        cgmDeltaArrow.observeForever { t -> Timber.i("DataStore.cgmDeltaArrow=$t") }
        bolusCalcDataSnapshot.observeForever { t -> Timber.i("DataStore.bolusCalcDataSnapshot=$t") }
        bolusCalcLastBG.observeForever { t -> Timber.i("DataStore.bolusCalcLastBG=$t") }
        maxBolusAmount.observeForever { t -> Timber.i("DataStore.maxBolusAmount=$t") }

        landingBasalDisplayedText.observeForever { t -> Timber.i("DataStore.landingBasalDisplayedText=$t") }
        landingControlIQDisplayedText.observeForever { t -> Timber.i("DataStore.landingControlIQDisplayedText=$t") }

        bolusUnitsDisplayedText.observeForever { t -> Timber.i("DataStore.bolusUnitsDisplayedText=$t") }
        bolusBGDisplayedText.observeForever { t -> Timber.i("DataStore.bolusBGDisplayedText=$t") }

        bolusCalculatorBuilder.observeForever { t -> Timber.i("DataStore.bolusCalculatorBuilder=$t") }
        bolusCurrentParameters.observeForever { t -> Timber.i("DataStore.bolusCurrentParameters=$t") }
        bolusFinalParameters.observeForever { t -> Timber.i("DataStore.bolusFinalParameters=$t") }
        bolusFinalConditions.observeForever { t -> Timber.i("DataStore.bolusFinalConditions=$t") }

        bolusPermissionResponse.observeForever { t -> Timber.i("DataStore.bolusPermissionResponse=$t") }
        bolusInitiateResponse.observeForever { t -> Timber.i("DataStore.bolusInitiateResponse=$t") }
        bolusCancelResponse.observeForever { t -> Timber.i("DataStore.bolusCancelResponse=$t") }
        bolusCurrentResponse.observeForever { t -> Timber.i("DataStore.bolusCurrentResponse=$t") }
    }
}
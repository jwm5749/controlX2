package com.jwoglom.controlx2.shared

enum class CommServiceCodes {
    INIT_PUMP_COMM,
    CHECK_PUMP_CONNECTED,
    SEND_PUMP_PAIRING_MESSAGE,
    SEND_PUMP_COMMAND,
    SEND_PUMP_COMMANDS_BULK,
    SEND_PUMP_COMMANDS_BUST_CACHE_BULK,
    SEND_PUMP_COMMAND_BOLUS,
    CACHED_PUMP_COMMANDS_BULK,
    DEBUG_GET_MESSAGE_CACHE,
    DEBUG_GET_HISTORYLOG_CACHE,
    WRITE_CHARACTERISTIC_FAILED_CALLBACK,
    STOP_PUMP_COMM,

    INIT_PUMP_FINDER_COMM,
    STOP_PUMP_FINDER_COMM,
    CHECK_PUMP_FINDER_FOUND_PUMPS,
}
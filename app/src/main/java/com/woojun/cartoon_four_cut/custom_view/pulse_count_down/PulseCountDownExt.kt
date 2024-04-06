package com.woojun.cartoon_four_cut.custom_view.pulse_count_down


fun PulseCountDown.start(callback: () -> Unit = {}) = start(OnCountdownCompleted { callback() })
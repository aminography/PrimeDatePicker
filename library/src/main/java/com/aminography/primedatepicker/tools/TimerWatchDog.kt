package com.aminography.primedatepicker.tools

import java.util.*

/**
 * @author aminography
 */
class TimerWatchDog(private val delay: Long) {

    private var timer: Timer? = null

    fun refresh(job: () -> Unit) {
        timer?.cancel()

        val timerTask = object : TimerTask() {
            override fun run() {
                job.invoke()
            }
        }
        timer = Timer()
        timer?.schedule(timerTask, delay)
    }

}

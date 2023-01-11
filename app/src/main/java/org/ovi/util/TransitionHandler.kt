package org.ovi.util

import android.transition.ChangeBounds
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity

object TransitionHandler {

    val explodeTransition by lazy { Explode().apply { duration = 200 } }
     val fadeTransition by lazy { Fade().apply { duration = 200 } }
     val slideTransition by lazy { Slide(Gravity.END).apply { duration = 200 } }
     val slideTrans by lazy { Slide(Gravity.END)}
    private val changeBoundTransition by lazy { ChangeBounds().apply { duration = 200 } }

    private val animList by lazy {
        arrayListOf(
            slideTransition,
            explodeTransition,
            fadeTransition
        )
    }

    fun getRandomAnim() = animList[(0..2).random()]


}
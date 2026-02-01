package com.keresman.flatearth.framework

import android.view.View
import android.view.animation.AnimationUtils

fun View.applyAnimation(animationId: Int)
        = startAnimation(AnimationUtils.loadAnimation(context, animationId))


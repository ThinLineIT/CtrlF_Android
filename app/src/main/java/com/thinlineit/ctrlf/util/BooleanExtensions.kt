package com.thinlineit.ctrlf.util

/** Returns `false` if [this] boolean is `null`. Otherwise, returns `this` itself. */
fun Boolean?.nullToFalse(): Boolean = this == true

/** Returns `true` if [this] boolean is `null`. Otherwise, returns `this` itself. */
fun Boolean?.nullToTrue(): Boolean = this ?: true

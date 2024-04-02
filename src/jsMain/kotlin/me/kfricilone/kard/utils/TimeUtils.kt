/*
 * Copyright (c) 2022 Kyle Fricilone (https://kfricilone.me)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package me.kfricilone.kard.utils

import kotlin.js.Date
import kotlin.math.max

/**
 * Created by Kyle Fricilone on Jan 09, 2021.
 */
private val regex = Regex("\\B(?=(\\d{3})+(?!\\d))")

private const val MINUTE = 60000L
private const val HOUR = 3600000L
private const val DAY = 86400000L
private const val MONTH = 2629743000L
private const val YEAR = 31556926000L

internal fun calcElapsed(date: String): String {
    val now = Date.now()
    val from = Date.parse(date)

    val delta = now - from
    val time =
        when {
            delta >= YEAR -> format("year", delta / YEAR)
            delta >= MONTH -> format("month", delta / MONTH)
            delta >= DAY -> format("day", delta / DAY)
            delta >= HOUR -> format("hour", delta / HOUR)
            else -> format("minute", delta / MINUTE)
        }

    return "Updated $time ago"
}

private fun format(
    unit: String,
    amount: Double,
): String {
    val v = max(1.0, amount).toInt()
    var str = "$v $unit"
    if (v != 1) str += "s"
    return str
}

internal fun format(amount: Int): String = amount.toString().replace(regex, ",")

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
package me.kfricilone.kard

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import js.array.asList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.json.Json
import me.kfricilone.kard.api.GithubColor
import me.kfricilone.kard.api.Repo
import me.kfricilone.kard.components.Card
import me.kfricilone.kard.constants.BACKGROUND_COLOR
import me.kfricilone.kard.constants.BACKGROUND_COLOR_DARK
import me.kfricilone.kard.constants.BACKGROUND_COLOR_LIGHT
import me.kfricilone.kard.constants.BORDER_COLOR
import me.kfricilone.kard.constants.BORDER_COLOR_DARK
import me.kfricilone.kard.constants.BORDER_COLOR_LIGHT
import me.kfricilone.kard.constants.COLORS_URL
import me.kfricilone.kard.constants.FOREGROUND_COLOR
import me.kfricilone.kard.constants.FOREGROUND_COLOR_DARK
import me.kfricilone.kard.constants.FOREGROUND_COLOR_LIGHT
import me.kfricilone.kard.constants.GH_API_URL
import me.kfricilone.kard.constants.LINK_COLOR
import me.kfricilone.kard.constants.LINK_COLOR_DARK
import me.kfricilone.kard.constants.LINK_COLOR_LIGHT
import react.dom.render
import web.dom.Element
import web.dom.document
import web.html.HTMLElement

/**
 * Created by Kyle Fricilone on Jan 05, 2021.
 */

private val jsonClient =
    HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        BrowserUserAgent()
    }

@JsExport
@JsName("buildKards")
public fun buildKards(
    dark: Boolean = false,
    error: Boolean = false,
) {
    injectGhTheme(dark)
    injectGhCss()

    val urls = collect()

    val scope = MainScope() + CoroutineName("kard")
    scope.launch {
        val colors =
            jsonClient.get(COLORS_URL)
                .body<Map<String, GithubColor>>()

        val repos = fetchAll(urls)
        if (error) repos.forEach { it.onFailure { t -> console.error(t) } }
        repos.mapNotNull { it.getOrNull() }
            .forEach {
                render(it.first) {
                    child(Card::class) {
                        attrs {
                            repo = it.second
                            this.colors = colors
                        }
                    }
                }
            }
    }
}

@JsExport
@JsName("switchKardTheme")
public fun switchKardTheme(
    element: HTMLElement,
    dark: Boolean,
) {
    element.style.setProperty("--$FOREGROUND_COLOR", if (dark) FOREGROUND_COLOR_DARK else FOREGROUND_COLOR_LIGHT)
    element.style.setProperty("--$BACKGROUND_COLOR", if (dark) BACKGROUND_COLOR_DARK else BACKGROUND_COLOR_LIGHT)
    element.style.setProperty("--$BORDER_COLOR", if (dark) BORDER_COLOR_DARK else BORDER_COLOR_LIGHT)
    element.style.setProperty("--$LINK_COLOR", if (dark) LINK_COLOR_DARK else LINK_COLOR_LIGHT)
}

private fun collect(): List<Pair<Element, String>> {
    return document.getElementsByClassName("kard-main")
        .asList()
        .mapNotNull {
            val path = it.getAttribute("repo-path")
            if (path.isNullOrEmpty()) return@mapNotNull null
            Pair(it, "$path")
        }
}

private suspend fun fetchAll(requests: List<Pair<Element, String>>) =
    coroutineScope {
        requests.map { async { fetchSuspend(it) } }.awaitAll()
    }

private suspend fun fetchSuspend(request: Pair<Element, String>): Result<Pair<Element, Repo>> =
    runCatching {
        Pair(request.first, jsonClient.get(GH_API_URL + request.second).body<Repo>())
    }

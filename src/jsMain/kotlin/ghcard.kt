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

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import js.core.asList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.json.Json
import react.dom.render
import web.dom.Element
import web.dom.document
import web.html.HTMLElement

/**
 * Created by Kyle Fricilone on Jan 05, 2021.
 */
private const val COLORS_URL = "https://cdn.jsdelivr.net/gh/ozh/github-colors/colors.json"
private const val GH_API_URL = "https://api.github.com/repos/"

private val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    BrowserUserAgent()
}

@JsName("buildGhCards")
public fun buildGhCards(
    dark: Boolean = false,
    error: Boolean = false
) {
    injectGhTheme(dark)
    injectGhCss()

    val urls = collect()

    val scope = MainScope() + CoroutineName("kard")
    scope.launch {
        val colors = jsonClient.get(COLORS_URL)
            .body<Map<String, LangColor>>()

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

@JsName("switchGhTheme")
public fun switchGhTheme(element: HTMLElement, dark: Boolean) {
    element.style.setProperty("--$fgCol", if (dark) fgColDark else fgColLight)
    element.style.setProperty("--$bgCol", if (dark) bgColDark else bgColLight)
    element.style.setProperty("--$borderCol", if (dark) borderColDark else borderColLight)
    element.style.setProperty("--$linkCol", if (dark) linkColDark else linkColLight)
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

private suspend fun fetchAll(
    requests: List<Pair<Element, String>>
) = coroutineScope {
    requests.map { async { fetchSuspend(it) } }
        .awaitAll()
}

private suspend fun fetchSuspend(
    request: Pair<Element, String>
): Result<Pair<Element, Repo>> = runCatching {
    val repo = jsonClient.get(GH_API_URL + request.second).body<Repo>()
    Pair(request.first, repo)
}

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
package me.kfricilone.kard.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by Kyle Fricilone on Jan 11, 2021.
 */
class RepoTest {
    @Test
    fun test_license() =
        runTest {
            val repo = client.get(ANGULAR_URL).body<Repo>()
            assertNotNull(repo.license)
        }

    @Test
    fun test_forked() =
        runTest {
            val repo = client.get(SWOT_URL).body<Repo>()
            assertNotNull(repo.parent)
        }

    @Test
    fun test_null_license() =
        runTest {
            val repo = client.get(INTELLIJ_KOTLIN_URL).body<Repo>()
            assertNull(repo.license)
        }

    @Test
    fun test_null_lang() =
        runTest {
            val repo = client.get(BOWER_ANGULAR_LOADER).body<Repo>()
            assertNull(repo.language)
        }

    private companion object {
        private val json = Json { ignoreUnknownKeys = true }

        private val client =
            HttpClient {
                install(ContentNegotiation) {
                    json(json)
                }
                BrowserUserAgent()
            }

        private const val ANGULAR_URL = "https://api.github.com/repos/angular/angular"
        private const val SWOT_URL = "https://api.github.com/repos/JetBrains/swot"
        private const val INTELLIJ_KOTLIN_URL = "https://api.github.com/repos/JetBrains/intellij-kotlin"
        private const val BOWER_ANGULAR_LOADER = "https://api.github.com/repos/angular/bower-angular-loader"
    }
}

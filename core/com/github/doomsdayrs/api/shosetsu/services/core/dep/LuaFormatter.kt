package com.github.doomsdayrs.api.shosetsu.services.core.dep

import com.github.doomsdayrs.api.shosetsu.services.core.objects.Novel
import com.github.doomsdayrs.api.shosetsu.services.core.objects.NovelGenre
import com.github.doomsdayrs.api.shosetsu.services.core.objects.NovelPage
import org.jsoup.nodes.Document
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua.coerce
import org.luaj.vm2.lib.jse.CoerceLuaToJava

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
/**
 * shosetsu-extensions
 * 16 / 01 / 2020
 *
 * @author github.com/doomsdayrs
 */
class LuaFormatter(val luaObject: LuaValue) : ScrapeFormat(luaObject.get("getID").call().toint()) {


    override val genres: Array<NovelGenre>
        get() = CoerceLuaToJava.coerce(luaObject.get("genres").call(), Array<NovelGenre>::class.java) as Array<NovelGenre>

    override val imageURL: String
        get() = luaObject.get("getImageURL").call().toString()

    override val name: String
        get() = luaObject.get("getName").call().toString()


    override fun getLatestURL(page: Int): String {
        return luaObject.get("getLatestURL").call(LuaValue.valueOf(page)).toString()
    }

    override fun getNovelPassage(document: Document): String {
        return luaObject.get("getNovelPassage").call(coerce(document)).toString()
    }

    override fun getSearchString(query: String): String {
        return luaObject.get("getSearchString").call(query).toString()
    }

    override fun novelPageCombiner(url: String, increment: Int): String {
        val out = luaObject.get("novelPageCombiner").call(LuaValue.valueOf(url), LuaValue.valueOf(increment))
        return out.toString()
    }

    override fun parseLatest(document: Document): List<Novel> {
        val out = luaObject.get("parseLatest").call(coerce(document))
        return CoerceLuaToJava.coerce(out, ArrayList::class.java) as ArrayList<Novel>
    }

    override fun parseNovel(document: Document): NovelPage {
        val out = luaObject.get("parseNovel").call(coerce(document))
        return CoerceLuaToJava.coerce(out, NovelPage::class.java) as NovelPage
    }

    override fun parseNovel(document: Document, increment: Int): NovelPage {
        return parseNovel(document)
    }

    override fun parseSearch(document: Document): List<Novel> {
        val out = luaObject.get("parseSearch").call(coerce(document))
        return CoerceLuaToJava.coerce(out, ArrayList::class.java) as ArrayList<Novel>
    }
}
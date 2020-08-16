package app.shosetsu.lib

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.CoerceLuaToJava

/*
 * This file is part of shosetsu-kotlin-lib.
 * shosetsu-kotlin-lib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * shosetsu-kotlin-lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with shosetsu-kotlin-lib.  If not, see <https://www.gnu.org/licenses/>.
 */

/** Convenient kotlin function to convert a value to a jvm value */
inline fun <reified T> coerceLuaToJava(value: LuaValue): T = CoerceLuaToJava.coerce(value, T::class.java) as T

/** Converts a [Map] to a [LuaTable] */
fun Map<Int, *>.toLua(): LuaTable = LuaTable().also {
	this.forEach { (i, any) -> it[i] = CoerceJavaToLua.coerce(any) }
}


/** Converts a [Map] to a [LuaTable] */
fun Array<*>.toLua(): LuaTable = LuaTable().also {
	this.map { CoerceJavaToLua.coerce(it) }.forEachIndexed { i, v -> it[i] = v }
}

/** Converts [Array] of anything to [LuaTable] */
fun Array<*>.toLua(oneIndex: Boolean): LuaTable = LuaTable().also {
	this.map { CoerceJavaToLua.coerce(it) }.forEachIndexed { i, v -> it[if (oneIndex) i + 1 else i] = v }
}


fun Array<Filter<*>>.mapify(): Map<Int, Any> = HashMap<Int, Any>().apply hasMap@{
	this@mapify.forEach {
		when (val state = it.state) {
			is Map<*, *> -> this.putAll(state as Map<out Int, Any>)
			else -> this[it.id] = state!!
		}
	}
}

fun <T> Array<Filter<T>>.mapifyS(): Map<Int, T> = HashMap<Int, T>().apply hasMap@{
	this@mapifyS.forEach {
		when (val state = it.state) {
			is Map<*, *> -> this.putAll(state as Map<out Int, T>)
			else -> this[it.id] = state
		}
	}
}
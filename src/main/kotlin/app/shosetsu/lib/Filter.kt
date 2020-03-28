@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package app.shosetsu.lib

/*
 * This file is part of shosetsu-extensions.
 * shosetsu-extensions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * shosetsu-extensions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with shosetsu-extensions.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 */
/**
 * shosetsu-extensions
 * 31 / 01 / 2020
 *
 * @author github.com/doomsdayrs
 */
abstract class Filter<T>(val name: String, open var state: T) {
	override fun toString(): String = "Filter(name='$name', state=${if (state is Sequence<*>) state.toString() else state})"
}

class Header(name: String) : Filter<Unit>(name, Unit)
class Separator : Filter<Unit>("", Unit)

class TextFilter(name: String) : Filter<String>(name, "")

class SwitchFilter(name: String) : Filter<Boolean>(name, false)
class CheckboxFilter(name: String) : Filter<Boolean>(name, false)

class TriStateFilter(name: String) : Filter<Int>(name, STATE_IGNORED) {
	companion object {
		const val STATE_IGNORED = 0
		const val STATE_INCLUDE = 1
		const val STATE_EXCLUDE = 2
	}
}

class DropdownFilter(name: String, val choices: Array<String>) : Filter<Int>(name, 0)
class RadioGroupFilter(name: String, val choices: Array<String>) : Filter<Int>(name, 0)

class FilterList(name: String, val filters: Array<Filter<*>>) : Filter<Sequence<*>>(name, sequence { yieldAll(filters.map { it.state }) }) {
	override var state: Sequence<*>
		get() = filters.map { it.state }.asSequence()
		set(value) {}
}

class FilterGroup<I, T>(name: String, val filters: Array<I>) : Filter<Sequence<T>>(name, sequence { yieldAll(filters.map { it.state }) }) where I : Filter<T> {
	override var state: Sequence<T>
		get() = filters.map { it.state }.asSequence()
		set(value) {}
}

fun Array<Filter<*>>.values(): Array<*> = this.map { it.state }.toTypedArray()
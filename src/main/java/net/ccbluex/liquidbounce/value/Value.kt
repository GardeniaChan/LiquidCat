/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.value

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.minecraft.client.gui.FontRenderer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*
import kotlin.math.roundToInt

@SideOnly(Side.CLIENT)



abstract class Value<T>(val name: String, var value: T) {
    var textHovered: Boolean = false
    var opened = false



    private var displayableFunc: () -> Boolean = { true }

    fun displayable(func: () -> Boolean): Value<T> {
        displayableFunc = func
        return this
    }

    val displayable: Boolean
        get() = displayableFunc()


    fun set(newValue: T) {
        if (newValue == value) return

        val oldValue = get()

        try {
            onChange(oldValue, newValue)
            changeValue(newValue)
            onChanged(oldValue, newValue)
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
        } catch (e: Exception) {
            ClientUtils.getLogger().error("[ValueSystem ($name)]: ${e.javaClass.name} (${e.message}) [$oldValue >> $newValue]")
        }
    }

    fun get() = value

    open fun changeValue(value: T) {
        this.value = value
    }

    abstract fun toJson(): JsonElement?
    abstract fun fromJson(element: JsonElement)

    protected open fun onChange(oldValue: T, newValue: T) {}
    protected open fun onChanged(oldValue: T, newValue: T) {}

}

/**
 * Bool value represents a value with a boolean
 */
open class BoolValue(name: String, value: Boolean) : Value<Boolean>(name, value) {

    var anim = 0F


    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asBoolean || element.asString.equals("true", ignoreCase = true)
    }
    open fun toggle(){
        this.value = !this.value
    }
}

open class ColorValue(name : String, value: Int) : Value<Int>(name, value) {
    val minimum: Int = -10000000
    val maximum: Int = 1000000
    fun set(newValue: Number) {
        set(newValue.toInt())
    }
    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if(element.isJsonPrimitive)
            value = element.asInt
    }

}
/**
 * Text value represents a value with a string
 */


open class TextValue(name: String, value: String) : Value<String>(name, value) {

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asString
    }
    fun append(o: Any): TextValue {
        set(get() + o)
        return this
    }
}

/**
 * Integer value represents a value with a integer
 */
open class IntegerValue(name: String, value: Int, val minimum: Int = 0, val maximum: Int = Integer.MAX_VALUE)
    : Value<Int>(name, value) {

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asInt
    }

}

open class LongValue(name: String, value: Long, val minimum: Long = 0, val maximum: Long = Long.MAX_VALUE)
    : Value<Long>(name, value) {

    fun set(newValue: Number) {
        set(newValue.toLong())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asLong
    }

}

/**
 * Float value represents a value with a float
 */
open class FloatValue(name: String, value: Float, val minimum: Float = 0F, val maximum: Float = Float.MAX_VALUE)
    : Value<Float>(name, value) {

    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asFloat
    }

}


/**
 * Font value represents a value with a font
 */
class FontValue(valueName: String, value: FontRenderer) : Value<FontRenderer>(valueName, value) {

    override fun toJson(): JsonElement? {
        val fontDetails = Fonts.getFontDetails(value) ?: return null
        val valueObject = JsonObject()
        valueObject.addProperty("fontName", fontDetails[0] as String)
        valueObject.addProperty("fontSize", fontDetails[1] as Int)
        return valueObject
    }

    override fun fromJson(element: JsonElement) {
        if (!element.isJsonObject) return
        val valueObject = element.asJsonObject
        value = Fonts.getFontRenderer(valueObject["fontName"].asString, valueObject["fontSize"].asInt)
    }
}
open class NumberValue(name: String, value: Double, val minimum: Double = 0.0, val maximum: Double = Double.MAX_VALUE,val inc: Double/* = 1.0*/)
    : Value<Double>(name, value) {

    fun set(newValue: Number) {
        set(newValue.toDouble())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asDouble
    }
    open fun getDouble(): Double {
        return ((this.get() as Number).toDouble() / this.inc).roundToInt() * this.inc
    }

    fun append(o: Double): NumberValue {
        set(get() + o)
        return this
    }
}

/**
 * Block value represents a value with a block
 */
class BlockValue(name: String, value: Int) : IntegerValue(name, value, 1, 197)

/**
 * List value represents a selectable list of values
 */
open class ListValue(name: String, val values: Array<String>, value: String) : Value<String>(name, value) {



    @JvmField
    var openList = false

    init {
        this.value = value
    }

    operator fun contains(string: String?): Boolean {
        return Arrays.stream(values).anyMatch { s: String -> s.equals(string, ignoreCase = true) }
    }
    fun getModeListNumber(modeName: String) : Int {
        for(i in this.values.indices) {
            if(values[i] == modeName) {
                return i
            }
        }
        return 0
    }

    override fun changeValue(value: String) {
        for (element in values) {
            if (element.equals(value, ignoreCase = true)) {
                this.value = element
                break
            }
        }
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) changeValue(element.asString)
    }


}
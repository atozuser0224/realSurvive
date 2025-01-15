package io.papermc.paperweight.testplugin

import io.papermc.paperweight.testplugin.TestPlugin.Companion.instance
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

val setHomeKey = NamespacedKey(instance,"setHomeKey")
val setHomeAmountKey = NamespacedKey(instance,"setHomeAmountKey")
val setHomeDate = NamespacedKey(instance,"setHomeDate")





var Player.setHomeLocation : Location
  get(){
    val loc = Location(this.world , 0.0 , 0.0 , 0.0)
    val list = this.persistentDataContainer.get(setHomeKey, PersistentDataType.INTEGER_ARRAY)
    list?.let {
      loc.x = it[0].toDouble()
      loc.y = it[1].toDouble()
      loc.z = it[2].toDouble()
      return loc
    }?:return loc
  }
  set(value) {
    this.persistentDataContainer.set(setHomeKey, PersistentDataType.INTEGER_ARRAY, intArrayOf(value.x.toInt(),value.y.toInt(),value.z.toInt()))
  }

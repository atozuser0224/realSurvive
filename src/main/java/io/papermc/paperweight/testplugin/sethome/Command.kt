package io.papermc.paperweight.testplugin.sethome

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import io.papermc.paperweight.testplugin.*
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import java.time.LocalDate
import java.util.Date

fun setHomeCommand(plugin: TestPlugin){
  commandTree("set"){
    literalArgument("home"){
      playerExecutor { player, commandArguments ->
        player.setHomeLocation = player.location
        player.sendMessage("""
          $PREFIX set home 설정하였습니다.
          $PREFIX x y z : ${player.location.blockX} , ${player.location.blockY} , ${player.location.blockZ}
        """.trimIndent())
      }
    }
  }
  commandTree("home"){
    playerExecutor { player, commandArguments ->
      if (player.persistentDataContainer.has(setHomeAmountKey)){
        val amount = player.pdc.get(setHomeAmountKey, PersistentDataType.INTEGER)!!
        if (amount > 0){
          player.persistentDataContainer.set(setHomeAmountKey, PersistentDataType.INTEGER , amount - 1)
          player.teleportAsync(player.setHomeLocation)
          player.sendMessage("$PREFIX 성공적으로 set Home을 실행하였습니다 남은 횟수" +
            "${amount-1}")
          player.pdc.set(setHomeDate, PersistentDataType.STRING,DateManager.getCurrentDateAsString())
        }else if (DateManager.dateToInt(Date()) > DateManager.dateToInt(DateManager.parseStringToDate(player.pdc.get(setHomeDate, PersistentDataType.STRING)!!)!!)) {
          player.pdc.set(setHomeAmountKey, PersistentDataType.INTEGER,4)
          player.teleportAsync(player.setHomeLocation)
          player.sendMessage("$PREFIX 성공적으로 set Home을 실행하였습니다 남은 횟수" +
            "${4}")
        }else{
          player.sendMessage("$PREFIX 오늘의 제한을 모두 사용하셨습니다 내일 사용해주세요.")
        }
      }else{
        player.pdc.set(setHomeAmountKey, PersistentDataType.INTEGER,5)
      }
    }
  }
}

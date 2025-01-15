package io.papermc.paperweight.testplugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.nullness.qual.NonNull
import org.checkerframework.framework.qual.DefaultQualifier
import java.util.*

@DefaultQualifier(NonNull::class)
class TestPlugin : JavaPlugin(), Listener {

    companion object{
        lateinit var instance : Plugin
    }

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true)) // Load with verbose output
        commandTree("set"){
            literalArgument("home"){
                playerExecutor { player, commandArguments ->
                    player.setHomeLocation = player.location
                    player.sendMessage("""
          $PREFIX set home 설정하였습니다.
          $PREFIX ${player.location.blockX} , ${player.location.blockY} , ${player.location.blockZ}
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
                        player.sendMessage("$PREFIX 성공적으로 set Home을 실행하였습니다\n 남은 횟수" +
                                "${amount-1}")
                        player.pdc.set(setHomeDate, PersistentDataType.STRING,DateManager.getCurrentDateAsString())
                    }else if (DateManager.dateToInt(Date()) > DateManager.dateToInt(DateManager.parseStringToDate(player.pdc.get(setHomeDate, PersistentDataType.STRING)!!)!!)) {
                        player.pdc.set(setHomeAmountKey, PersistentDataType.INTEGER,9)
                        player.teleportAsync(player.setHomeLocation)
                        player.sendMessage("$PREFIX 성공적으로 set Home을 실행하였습니다\n 남은 횟수" +
                                " : ${9}")
                    }else{
                        player.sendMessage("$PREFIX 오늘의 제한을 모두 사용하셨습니다 내일 사용해주세요.")
                    }
                }else{
                    player.pdc.set(setHomeAmountKey, PersistentDataType.INTEGER,9)
                    player.teleportAsync(player.setHomeLocation)
                    player.sendMessage("$PREFIX 성공적으로 set Home을 실행하였습니다\n 남은 횟수" +
                            " : ${9}")                }
            }
        }
        commandTree("patch"){
            playerExecutor { player, commandArguments ->
                config.getList("patch")?.forEach {
                    player.sendMessage(it as String)
                }
            }
        }
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this,this)
        CommandAPI.onEnable()
        instance = this
        saveDefaultConfig()
        // Register commands, listeners etc.
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

    @EventHandler
    fun onJoin(e : PlayerJoinEvent){
        e.player.sendMessage("$PREFIX 패치노트를 확인하시려면 /patch를 사용해주세요.")
    }
}

package io.papermc.paperweight.testplugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.papermc.paperweight.testplugin.sethome.setHomeCommand
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.nullness.qual.NonNull
import org.checkerframework.framework.qual.DefaultQualifier

@DefaultQualifier(NonNull::class)
class TestPlugin : JavaPlugin(), Listener {

    companion object{
        lateinit var instance : Plugin
    }

    override fun onLoad() {
        instance = this
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true)) // Load with verbose output

        setHomeCommand(this)
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        // Register commands, listeners etc.
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

}

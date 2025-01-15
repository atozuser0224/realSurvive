package io.papermc.paperweight.testplugin

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

val PREFIX = "${ChatColor.AQUA}${ChatColor.BOLD} [ REAL SURVIVE ] ${ChatColor.RESET}"

val Player.pdc : PersistentDataContainer
  get() = this.persistentDataContainer

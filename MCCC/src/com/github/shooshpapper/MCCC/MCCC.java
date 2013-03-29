/**
 * MineCraft Chat Censor
 */

package com.github.shooshpapper.MCCC;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 * @author shooshpapper
 *
 */
public class MCCC extends JavaPlugin implements Listener {
	
	public static MCCC plugin;
	
    @Override
    public void onEnable() {
    	getServer().getPluginManager().registerEvents(this, this);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		if (message.contains("fuck")) {
			event.setMessage("****");
		}
	}
}
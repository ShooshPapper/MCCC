/**
 * MineCraft Chat Censor
 */

package com.github.shooshpapper.MCCC;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
//import java.text.Normalizer;



/**
 * @author shooshpapper
 *
 */
public class MCCC extends JavaPlugin implements Listener {
	
	public static MCCC plugin;
	
    @Override
    public void onEnable() {
    	saveDefaultConfig();
    	getServer().getPluginManager().registerEvents(this, this);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled()) {
			return;
		}
		String message = event.getMessage();
		event.setMessage(censor(message));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) {
			return;
		}
		String message = event.getMessage();
		if (message.startsWith("/me")) {
			event.setMessage(censor(message));
		}
		else if (message.startsWith("/tell")) {
			// TODO: Split first so target username isn't censored
			event.setMessage(censor(message));
		}
		else if (message.startsWith("/say")) {
			event.setMessage(censor(message));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		int i = 0;
		for (String line : event.getLines()) {
			event.setLine(i, censor(line));
			i++;
		}
	}
	
	private String censor(String message) {	
		// case mapping
		//        BAnanA --> banana
		String testMessage = message.toLowerCase();

		Boolean doReplace = false;
		for (String swear : this.getConfig().getStringList("swears")) {
			if (testMessage.contains(swear)) {
				doReplace = true;
				StringBuilder replacement = new StringBuilder();
				for (int i = 0; i < swear.length(); i++) {
					replacement.append("*");
				}
				testMessage = testMessage.replaceAll(swear, replacement.toString());
			}
		}
		if (doReplace) {
			StringBuilder newMessage = new StringBuilder(message);
			for (int i = 0; i < message.length(); i++) {
				if (testMessage.charAt(i) == '*') {
					newMessage.setCharAt(i, '*');
				}
			}
			return newMessage.toString();
		}
		else {
			return message;
		}
	}
}
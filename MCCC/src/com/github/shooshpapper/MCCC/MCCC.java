/**
 * MineCraft Chat Censor
 */
//Name of Plugin
package com.github.shooshpapper.MCCC;

//Imported things to make plugin run smoothly
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;




/**
 * @author shooshpapper
 *
 */

//Create class, Daddy File, (MCCC is an offspring of JavaPlugin) uses specific part of Daddy.
public class MCCC extends JavaPlugin implements Listener {
	
	public static MCCC plugin;
	
	//Overrides function, onEnable. Turns plugin on automatically as server is turned on.
    @Override
    public void onEnable() {
    	//Copies config.yml out of .jar
    	saveDefaultConfig();
    	//Allows events to function inside this plugin
    	getServer().getPluginManager().registerEvents(this, this);
    }
	
    //Tells bukkit to read events in order of Lowest to Highest priority
	@EventHandler(priority = EventPriority.HIGHEST)
	//Finds messages being sent
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		//Event can be cancelled by other/previous plugins
		if (event.isCancelled()) {
			return;
		}
		//Collects message
		String message = event.getMessage();
		//Changes message, if needed, and sends back out
		event.setMessage(censor(message));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	//Finds commands, i.e. /me /tell
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) {
			return;
		}
		String message = event.getMessage();
		//Checks for /me command
		if (message.startsWith("/me ")) {
			//Changes command, if needed, and sends it back
			event.setMessage(censor(message));
		}
		//Checks for /tell command
		else if (message.startsWith("/tell ")) {
			// TODO: Split first so target username isn't censored
			event.setMessage(censor(message));
		}
		//Checks for /say command (can only be done by admins?)
		else if (message.startsWith("/say ")) {
			event.setMessage(censor(message));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	//Finds signs when being placed/edited
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		//Sets code up for different lines on the sign
		int i = 0;
		//Finds strings 0-3 for each line on the sign
		for (String line : event.getLines()) {
			//Sets individual censored lines
			event.setLine(i, censor(line));
			i++;
		}
	}
	//Inputs and gives a 'modified' string
	private String censor(String message) {	
		//Turns message into lowercase to override case sensitivity
		String testMessage = message.toLowerCase();

		//Improves performance by skipping filters if no swears are found (in case no swears are found)
		Boolean doReplace = false;
		//Grabs config.yml as an array
		for (String swear : this.getConfig().getStringList("swears")) {
			//If a swear from config.yml is found...
			if (testMessage.contains(swear)) {
				//Does not skip filtering
				doReplace = true;
				//Provides capability to edit a string
				StringBuilder replacement = new StringBuilder();
				//Finds length of word and creates string of asterisks with the same length
				for (int i = 0; i < swear.length(); i++) {
					replacement.append("*");
				}
				//Replaces words with asterisk strings
				testMessage = testMessage.replaceAll(swear, replacement.toString());
			}
		}
		//If process wasn't skipped... (swear found)
		if (doReplace) {
			//Turns original (message)string into a modifiable string
			StringBuilder newMessage = new StringBuilder(message);
			//Finds where to asterisks in original (message)string
			for (int i = 0; i < message.length(); i++) {
				//If an asterisk is found...
				if (testMessage.charAt(i) == '*') {
					//Replaces the given character with an asterisk
					newMessage.setCharAt(i, '*');
				}
			}
			//Returns modified original (message)string
			return newMessage.toString();
		}
		//If process is skipped...
		else {
			//Returns unmodified message
			return message;
		}
	}
}
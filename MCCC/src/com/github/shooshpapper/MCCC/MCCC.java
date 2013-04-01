/**
 * MineCraft Chat Censor
 */

package com.github.shooshpapper.MCCC;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import java.text.Normalizer;



/**
 * @author shooshpapper
 *
 */
public class MCCC extends JavaPlugin implements Listener {
	
	public static MCCC plugin;
	
    @Override
    public void onEnable() {
    	plugin.getConfig().options().copyDefaults(true);
    	getServer().getPluginManager().registerEvents(this, this);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();

		// TODO: remove format codes
		//        §1f§2u§3c§4k --> fuck
		
		// TODO: if message contains non-alphanumerics, translate them
		//        ƒµ©K --> fuck
		// We can't normalize without going the whole way, because NFD can alter character count
		// String newMessage = Normalizer.normalize(message, Normalizer.Form.NFD);
		
		// TODO: multi-character glyphs
		//        fuc|< --> fuck
		
		// TODO: case mapping
		//        FUcK --> fuck
		String testMessage = message.toLowerCase();
		
		// TODO: space/separator removal
		//        f u c k --> fuck
		//        f.u.c.k --> fuck
		//        f█u█c█k --> fuck
		
		// TODO: non-dictionary word with alphabetic spacing
		//        aaFaaaUaaCaaKaa --> fuck
		
		// TODO: intact first and last character
		//        fcuk --> fuck
		
		// NOTE: This is actually a lie (somewhat) and called typoglycemia
		//        https://en.wikipedia.org/wiki/Typoglycemia
		//        http://www.mrc-cbu.cam.ac.uk/people/matt.davis/cmabridge/


		// TODO: pre/postfix removal
		//        assmonster --> ass
		//        unclefucker --> fuck
		//        fatniggerbumhole --> ?
		
		// TODO: multiline concatenation
		//        f
		//        u
		//        c
		//        k --> fuck
		
		// TODO: repeated character removal
		//        fffuuuuuuuucccckkk --> fuck
		
		// TODO: phonetic matching
		//        fuk --> fuck
		
		// TODO: grab fucks from a list/db
		
		// TODO: stemming
		//        fucking --> fuck
		
		for (String swear : plugin.getConfig().getStringList("swear")) {
			if (testMessage.contains(swear)) {
				StringBuilder replacement = new StringBuilder();
				for (int i = 0; i < swear.length(); i++) {
					replacement.append("*");
				}
				testMessage = testMessage.replaceAll(swear, replacement.toString());
			}
		}
		StringBuilder newMessage = new StringBuilder(message);
		for (int i = 0; i < message.length(); i++) {
			if (testMessage.charAt(i) == '*') {
				newMessage.setCharAt(i, '*');
			}
		}
		event.setMessage(newMessage.toString());
	}
}
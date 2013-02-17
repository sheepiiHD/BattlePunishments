package com.lducks.battlepunishments.commands.needhelp;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lducks.battlepunishments.commands.CustomCommandExecutor;
import com.lducks.battlepunishments.listeners.chat.ChatEditor;
import com.lducks.battlepunishments.util.BattlePerms;
import com.lducks.battlepunishments.util.BattleSettings;
import com.lducks.battlepunishments.util.battlelogs.BattleLog;

/**
 * 
 * @author lDucks
 *
 */

public class NeedHelpExecutor extends CustomCommandExecutor{
	
	/**
	 * Players who needhelp
	 */
	static HashMap<String, Long> timeout = new HashMap<String, Long>();
	
	@MCCommand(perm=BattlePerms.NEEDHELP, inGame=true)
	public void onNeedHelp(CommandSender sender, String[] args) {
		if(sender.hasPermission(BattlePerms.RESPOND)){
			sender.sendMessage(RED+"You ARE a staff member, you can't request help.");
			return;
		}

		Player p = (Player) sender;

		long time = System.currentTimeMillis();

		if(timeout.containsKey(p.getName())){
			time = timeout.get(p.getName());
		}

		if(time > System.currentTimeMillis()){
			sender.sendMessage(RED + "You can not use this yet!");
			return;
		}else
			timeout.remove(p.getName());

		sender.sendMessage(BLUE+"You notified the staff that you need help.");
		sender.sendMessage(BLUE+"You can not use this command again for 60 seconds.");
		sender.sendMessage(BLUE+"If you don't get a reply within 60 seconds, feel free to request help again.");

		timeout.put(p.getName(), System.currentTimeMillis() + BattleSettings.getNeedHelpTimeout());

		String reason = null;

		int amt = args.length;

		if(amt > 0){
			StringBuilder sb = new StringBuilder();
			for (int i=0;i< args.length;i++){
				if(i != amt){
					sb.append(args[i]+" ");
				}else{
					sb.append(args[i]);
				}
			}

			reason = ChatEditor.colorChat(sb.toString());
		}

		if(reason == null && !BattleSettings.requiresHelpReason()){
			sender.sendMessage(BLUE+"Next time you use this command please add a short reason as to why you need help!");
		}else if(reason == null && BattleSettings.requiresHelpReason()){
			sender.sendMessage(RED + "You are required to supply a reason as to why you need help.");
			return;
		}

		String duder = sender.getName();
		Player[] playerListVariable = Bukkit.getServer().getOnlinePlayers();
		
		if(BattleSettings.useBattleLog())
			BattleLog.addMessage(sender.getName() + " requested help!");
		
		for(Player op : playerListVariable){
			if(op.hasPermission(BattlePerms.RESPOND)){
				op.sendMessage(DARK_RED+""+ BOLD+"[ATTENTION] "+AQUA+ITALIC+duder+" has requested staff help!");
				op.playEffect(op.getLocation(), Effect.POTION_BREAK, 5);

				if(reason != null)
					op.sendMessage(DARK_RED+""+ BOLD+"[ATTENTION] "+AQUA+""+ITALIC+"Reason For Request: "+YELLOW+reason);
			}
		}
	}
}

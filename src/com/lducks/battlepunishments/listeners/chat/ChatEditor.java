package com.lducks.battlepunishments.listeners.chat;

/**
 * 
 * @author lDucks
 *
 */

public class ChatEditor {
	public static String colorChat(String msg) {
		return msg.replaceAll("&", Character.toString((char) 167));
	}
	
	public static String toChar(String msg) {
		return msg.replaceAll(Character.toString((char) 167), "&");
	}
}

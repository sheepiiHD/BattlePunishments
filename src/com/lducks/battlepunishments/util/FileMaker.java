package com.lducks.battlepunishments.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.lducks.battlepunishments.BattlePunishments;
import com.lducks.battlepunishments.debugging.DumpFile;

/**
 * 
 * @author lDucks
 *
 */

public class FileMaker {
	private static Logger log = Logger.getLogger("Minecraft");
	private static String path = BattlePunishments.getPath();
	public FileMaker(){

		new File(path).mkdir();
		new File(path+"/players").setWritable(true);

		final String[] directories = {"players","errors"};

		for(String dirname : directories)
			new File(path+"/"+dirname).mkdir();

		final String[] ymls = {"automessage.yml","blocklogger.yml","private.key","battlelog.txt"};

		for(String filename : ymls){
			File a = new File(path +"/"+filename);
			if(!a.exists()){
				try {
					a.createNewFile();
					log.info("[" + BattlePunishments.getPluginName() + "] "+path+"/"+filename+".yml created.");
				}catch (IOException e) {
					new DumpFile("create"+filename, e, "Error creating file "+filename);
				}
			}else{
				log.info("[" + BattlePunishments.getPluginName() + "] "+path+"/"+filename+".yml found.");
			}
		}
	}
}
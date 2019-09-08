package net.aionstudios.livecomply.web.form;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.JDCHeadElement;
import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.processor.ComputeSchedule;
import net.aionstudios.jdc.processor.ElementProcessor;
import net.aionstudios.jdc.processor.ProcessorSet;
import net.aionstudios.jdc.util.DatabaseUtils;
import net.aionstudios.jdc.util.SecurityUtils;

public class GenerateFormTokenProcessor extends ElementProcessor {

	public GenerateFormTokenProcessor(ProcessorSet set) {
		super("gentoken", set, ComputeSchedule.LIVE);
	}

	@Override
	public void generateContent(JDCHeadElement element, HttpExchange he, RequestVariables vars,
			Map<String, Object> pageVariables) {
		element.setAttribute("value", generateNewToken(vars.getPage(), vars.getRequestCookies().get("sessionID")));
	}
	
	private String findTokenQuery = "SELECT * FROM `livecomply`.`form_tokens` WHERE `token` = ?;";
	private String insertTokenQuery = "INSERT INTO `livecomply`.`form_tokens` (`token`, `page`, `sessionID`) VALUES (?, ?, ?);";
	
	private String generateNewToken(String page, String sessionID) {
		String tryToken = "";
		boolean tokenAvailable = false;
		while(!tokenAvailable) {
			tryToken = SecurityUtils.genToken(64);
			if(DatabaseUtils.prepareAndExecute(findTokenQuery, true, tryToken).get(0).getResults().isEmpty()) {
				tokenAvailable = true;
			}
		}
		DatabaseUtils.prepareAndExecute(insertTokenQuery, true, tryToken, page, sessionID);
		return tryToken;
	}

	@Override
	public void generateContent(JDCHeadElement element) {
		//empty
	}

}

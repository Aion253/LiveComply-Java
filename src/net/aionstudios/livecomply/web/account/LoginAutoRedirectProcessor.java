package net.aionstudios.livecomply.web.account;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.processor.Processor;
import net.aionstudios.jdc.processor.ProcessorSet;
import net.aionstudios.jdc.util.DatabaseUtils;

public class LoginAutoRedirectProcessor extends Processor {

	public LoginAutoRedirectProcessor(ProcessorSet set) {
		super("reqlog", set);
	}

	private String findSessionQuery = "SELECT * FROM `livecomply`.`user_sessions` WHERE `sessionID` = ?;";
	
	@Override
	public void compute(HttpExchange he, RequestVariables vars, Map<String, Object> pageVariables) {
		if(DatabaseUtils.prepareAndExecute(findSessionQuery, true, vars.getCookieManager().getRequestCookies().get("sessionID")).get(0).getResults().isEmpty()){
			vars.setRedirect("/signin.jdc");
		}
	}

}

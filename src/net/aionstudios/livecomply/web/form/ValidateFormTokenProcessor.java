package net.aionstudios.livecomply.web.form;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.processor.Processor;
import net.aionstudios.jdc.processor.ProcessorSet;
import net.aionstudios.jdc.util.DatabaseUtils;

public class ValidateFormTokenProcessor extends Processor {

	public ValidateFormTokenProcessor(ProcessorSet set) {
		super("validatetoken", set);
	}
	
	private String validTokenQuery = "SELECT * FROM `livecomply`.`form_tokens` WHERE `token` = ? AND `sessionID` = ? AND `page` = ?;";

	@Override
	public void compute(HttpExchange he, RequestVariables vars, Map<String, Object> pageVariables) {
		if(vars.getPost().containsKey("formToken")) {
			if(!DatabaseUtils.prepareAndExecute(validTokenQuery, true, vars.getPost().get("formToken"), vars.getRequestCookies().get("sessionID"), vars.getPage()).get(0).getResults().isEmpty()) {
				pageVariables.put("formValid", true);
				return;
			}
			pageVariables.put("formValid", false);
			return;
		}
	}

}

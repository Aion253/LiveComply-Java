package net.aionstudios.livecomply.web.session;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.JDCHeadElement;
import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.processor.ComputeSchedule;
import net.aionstudios.jdc.processor.ElementProcessor;
import net.aionstudios.jdc.processor.ProcessorSet;

public class PrintIDSessionProcessor extends ElementProcessor {

	public PrintIDSessionProcessor(ProcessorSet set) {
		super("sessionid", set, ComputeSchedule.LIVE);
	}

	@Override
	public void generateContent(JDCHeadElement element, HttpExchange he, RequestVariables vars,
			Map<String, Object> pageVariables) {
		element.setAttribute("id", "sessionID");
		element.setAttribute("data-sessionid", vars.getRequestCookies().get("sessionID"));
	}

	@Override
	public void generateContent(JDCHeadElement element) {
		
	}

}

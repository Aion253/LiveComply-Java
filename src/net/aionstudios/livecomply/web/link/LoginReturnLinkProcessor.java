package net.aionstudios.livecomply.web.link;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.JDCHeadElement;
import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.processor.ComputeSchedule;
import net.aionstudios.jdc.processor.ElementProcessor;
import net.aionstudios.jdc.processor.ProcessorSet;

public class LoginReturnLinkProcessor extends ElementProcessor {

	public LoginReturnLinkProcessor(ProcessorSet set) {
		super("loginreturn", set, ComputeSchedule.LIVE);
	}

	@Override
	public void generateContent(JDCHeadElement element, HttpExchange he, RequestVariables vars,
			Map<String, Object> pageVariables) {
		try {
			element.setAttribute("href", "/signin.jdc?pageReturn="+URLEncoder.encode(vars.getGet().containsKey("pageReturn") ? vars.getGet().get("pageReturn") : vars.getPage(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateContent(JDCHeadElement element) {
		
	}

}

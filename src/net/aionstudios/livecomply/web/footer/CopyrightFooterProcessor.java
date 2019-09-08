package net.aionstudios.livecomply.web.footer;

import java.time.Year;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.JDCHeadElement;
import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.processor.ComputeSchedule;
import net.aionstudios.jdc.processor.ElementProcessor;
import net.aionstudios.jdc.processor.ProcessorSet;

public class CopyrightFooterProcessor extends ElementProcessor {

	public CopyrightFooterProcessor(ProcessorSet set) {
		super("copyright", set, ComputeSchedule.LIVE);
	}

	@Override
	public void generateContent(JDCHeadElement element, HttpExchange he, RequestVariables vars,
			Map<String, Object> pageVariables) {
		element.setText("&copy; " + Year.now().getValue() + " Winter Roberts. All Rights Reserved.");
	}

	@Override
	public void generateContent(JDCHeadElement element) {
	}

}

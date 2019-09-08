package net.aionstudios.livecomply.web.errors;

import net.aionstudios.jdc.processor.ProcessorManager;
import net.aionstudios.jdc.processor.ProcessorSet;

public class ErrorsSet extends ProcessorSet {

	public ErrorsSet(ProcessorManager pm) {
		super("errors", pm);
	}

}

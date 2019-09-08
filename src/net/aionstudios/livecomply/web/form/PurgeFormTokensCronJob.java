package net.aionstudios.livecomply.web.form;

import net.aionstudios.jdc.cron.CronDateTime;
import net.aionstudios.jdc.cron.CronJob;
import net.aionstudios.jdc.util.DatabaseUtils;

public class PurgeFormTokensCronJob extends CronJob {

	public PurgeFormTokensCronJob() {
		super(new CronDateTime());
	}
	
	private String removeTokenAgeQuery = "DELETE FROM `livecomply`.`form_tokens` WHERE created < (NOW() - INTERVAL 29 MINUTE);";

	@Override
	public void run() {
		DatabaseUtils.prepareAndExecute(removeTokenAgeQuery, false);
	}

}

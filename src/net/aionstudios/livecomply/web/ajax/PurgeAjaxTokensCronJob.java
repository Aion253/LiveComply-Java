package net.aionstudios.livecomply.web.ajax;

import net.aionstudios.jdc.cron.CronDateTime;
import net.aionstudios.jdc.cron.CronJob;
import net.aionstudios.jdc.util.DatabaseUtils;

public class PurgeAjaxTokensCronJob extends CronJob {

	public PurgeAjaxTokensCronJob() {
		super(new CronDateTime());
	}
	
	private String removeTokenAgeQuery = "DELETE FROM `livecomply_ajax`.`ajax_tokens` WHERE created < (NOW() - INTERVAL 719 MINUTE);";

	@Override
	public void run() {
		DatabaseUtils.prepareAndExecute(removeTokenAgeQuery, false);
	}

}

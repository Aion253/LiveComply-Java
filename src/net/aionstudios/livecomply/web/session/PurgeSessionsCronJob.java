package net.aionstudios.livecomply.web.session;

import net.aionstudios.jdc.cron.CronDateTime;
import net.aionstudios.jdc.cron.CronJob;
import net.aionstudios.jdc.util.DatabaseUtils;

public class PurgeSessionsCronJob extends CronJob {

	public PurgeSessionsCronJob() {
		super(new CronDateTime());
	}
	
	private String removeSessionAgeQuery = "DELETE FROM `livecomply`.`web_sessions` WHERE last < (NOW() - INTERVAL 59 MINUTE);";
	private String removeDeadUserSession = "DELETE FROM `livecomply`.`user_sessions` WHERE NOT EXISTS(SELECT NULL FROM `hoh_front`.`web_sessions` WHERE `web_sessions`.`sessionID` = `user_sessions`.`sessionID`);";

	@Override
	public void run() {
		DatabaseUtils.prepareAndExecute(removeSessionAgeQuery, false);
		DatabaseUtils.prepareAndExecute(removeDeadUserSession, false);
	}

}

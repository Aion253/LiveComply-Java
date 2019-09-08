package net.aionstudios.livecomply.web.clock;

import net.aionstudios.jdc.cron.CronDateTime;
import net.aionstudios.jdc.cron.CronJob;
import net.aionstudios.jdc.util.DatabaseUtils;

public class NightlyClockPurgeCronJob extends CronJob {

	public NightlyClockPurgeCronJob(CronDateTime cdt) {
		super(cdt);
	}

	@Override
	public void run() {
		DatabaseUtils.prepareAndExecute("TRUNCATE `livecomply`.`clock_events`;", true);
	}

}

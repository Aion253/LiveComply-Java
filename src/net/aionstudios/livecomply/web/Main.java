package net.aionstudios.livecomply.web;

import net.aionstudios.jdc.JDC;
import net.aionstudios.jdc.cron.CronDateTime;
import net.aionstudios.jdc.cron.CronManager;
import net.aionstudios.livecomply.web.account.AccountSet;
import net.aionstudios.livecomply.web.account.LoginAutoRedirectProcessor;
import net.aionstudios.livecomply.web.account.LoginFormAccountProcessor;
import net.aionstudios.livecomply.web.ajax.AjaxSet;
import net.aionstudios.livecomply.web.ajax.GenerateAjaxTokenProcessor;
import net.aionstudios.livecomply.web.ajax.PurgeAjaxTokensCronJob;
import net.aionstudios.livecomply.web.clock.NightlyClockPurgeCronJob;
import net.aionstudios.livecomply.web.errors.ErrorsSet;
import net.aionstudios.livecomply.web.errors.NotFoundProcessor;
import net.aionstudios.livecomply.web.footer.CopyrightFooterProcessor;
import net.aionstudios.livecomply.web.footer.FooterSet;
import net.aionstudios.livecomply.web.form.FormSet;
import net.aionstudios.livecomply.web.form.GenerateFormTokenProcessor;
import net.aionstudios.livecomply.web.form.PurgeFormTokensCronJob;
import net.aionstudios.livecomply.web.form.ValidateFormTokenProcessor;
import net.aionstudios.livecomply.web.link.LinkSet;
import net.aionstudios.livecomply.web.link.LoginReturnLinkProcessor;
import net.aionstudios.livecomply.web.session.PrintIDSessionProcessor;
import net.aionstudios.livecomply.web.session.PurgeSessionsCronJob;
import net.aionstudios.livecomply.web.session.SessionSet;
import net.aionstudios.livecomply.web.session.ValidateSessionProcessor;

public class Main extends JDC {

	@Override
	public void initialize() {
		/*Sessions*/
		SessionSet ss = new SessionSet(getProcessorManager());
		ValidateSessionProcessor vss = new ValidateSessionProcessor(ss);
		PrintIDSessionProcessor psss = new PrintIDSessionProcessor(ss);
		
		/*Forms*/
		FormSet fs = new FormSet(getProcessorManager());
		GenerateFormTokenProcessor gtfs = new GenerateFormTokenProcessor(fs);
		ValidateFormTokenProcessor vtfs = new ValidateFormTokenProcessor(fs);
		
		/*Ajax*/
		AjaxSet as = new AjaxSet(getProcessorManager());
		GenerateAjaxTokenProcessor gtas = new GenerateAjaxTokenProcessor(as);
		
		/*Accounts*/
		AccountSet acs = new AccountSet(getProcessorManager());
		LoginFormAccountProcessor las = new LoginFormAccountProcessor(acs);
		LoginAutoRedirectProcessor larpas = new LoginAutoRedirectProcessor(acs);
		
		/*Errors*/
		ErrorsSet es = new ErrorsSet(getProcessorManager());
		NotFoundProcessor esnfp = new NotFoundProcessor(es);
		
		/*Links*/
		LinkSet ls = new LinkSet(getProcessorManager());
		LoginReturnLinkProcessor lrlpls = new LoginReturnLinkProcessor(ls);
		
		
		/*Footer*/
		FooterSet fts = new FooterSet(getProcessorManager());
		CopyrightFooterProcessor cfts = new CopyrightFooterProcessor(fts);
		
		/*Cron Jobs*/
		/*Cron*/
		CronDateTime nightly = new CronDateTime();
		nightly.setHourRange(0, 0);
		nightly.setMinuteRange(0, 0);
		nightly.setAllDayOfWeekRange();
		nightly.setAllDayOfMonthRange();
		nightly.setAllMonthRange();
		nightly.setAllYearRange();
		NightlyClockPurgeCronJob ncpcj = new NightlyClockPurgeCronJob(nightly);
		PurgeSessionsCronJob pscj = new PurgeSessionsCronJob();
		PurgeFormTokensCronJob pftcj = new PurgeFormTokensCronJob();
		PurgeAjaxTokensCronJob patcj = new PurgeAjaxTokensCronJob();
		CronManager.addJob(pscj);
		CronManager.addJob(ncpcj);
		CronManager.addJob(pftcj);
		CronManager.addJob(patcj);
		CronDateTime td = new CronDateTime();
		CronManager.startCron();
	}

}

package net.aionstudios.livecomply.web.account;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.jdc.content.JDCElement;
import net.aionstudios.jdc.content.JDCHeadElement;
import net.aionstudios.jdc.content.RequestVariables;
import net.aionstudios.jdc.database.QueryResults;
import net.aionstudios.jdc.processor.ComputeSchedule;
import net.aionstudios.jdc.processor.ElementProcessor;
import net.aionstudios.jdc.processor.ProcessorSet;
import net.aionstudios.jdc.util.DatabaseUtils;
import net.aionstudios.jdc.util.SecurityUtils;

public class LoginFormAccountProcessor extends ElementProcessor {

	public LoginFormAccountProcessor(ProcessorSet set) {
		super("loginform", set, ComputeSchedule.LIVE);
	}

	private String deleteSessionQuery = "DELETE FROM `livecomply`.`user_sessions` WHERE `sessionID`=?;";
	private String insertUserSessionQuery = "INSERT INTO `livecomply`.`user_sessions` (`sessionID`, `uid`) VALUES (?, ?);";
	
	@Override
	public void generateContent(JDCHeadElement e, HttpExchange he, RequestVariables vars,
			Map<String, Object> pageVariables) {
		String pageReturn = vars.getGet().containsKey("pageReturn") ? vars.getGet().get("pageReturn") : "/";
		
		String loginVal = vars.getPost().containsKey("login") ? vars.getPost().get("login") : "";
		
		String unVal = vars.getPost().containsKey("username") ? vars.getPost().get("username") : "";
		String pVal = vars.getPost().containsKey("password") ? vars.getPost().get("password") : "";
		
		JDCElement une = new JDCElement("input").setAttribute("type", "text").setAttribute("name", "username")
				.setAttribute("placeholder", "Username").setAttribute("value", unVal).setAttribute("autofocus", "");
		JDCElement unp = new JDCElement("p").setAttribute("name", "username").setAttribute("class", "error-text");
		
		JDCElement pwe = new JDCElement("input").setAttribute("type", "password").setAttribute("name", "password")
				.setAttribute("placeholder", "Password").setAttribute("value", pVal);
		JDCElement pwp = new JDCElement("p").setAttribute("name", "password").setAttribute("class", "error-text");
		
		JDCElement jhi = new JDCElement("input").setAttribute("type", "hidden").setAttribute("name", "login").setAttribute("value", "true");
		JDCElement fti = new JDCElement("input").setAttribute("type", "hidden").setAttribute("name", "formToken").setAttribute("value", generateNewToken(vars.getPage(), vars.getRequestCookies().get("sessionID")));
		
		JDCElement sbm = new JDCElement("input").setAttribute("type", "submit").setAttribute("name", "btnSubmit")
				.setAttribute("class", "button-hollow-fill button-right button-half").setAttribute("value", "Sign In");
		
		try {
			e.setAttribute("action", "/signin.jdc?pageReturn="+URLEncoder.encode(pageReturn, "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		if(loginVal.equals("true") && ((boolean) pageVariables.get("formValid")) == true) {
			QueryResults qr = getNecessaryInfo(unVal);
			String salt = (String) (!qr.getResults().isEmpty() && !qr.getResults().get(0).isEmpty() ? qr.getResults().get(0).get("salt") : null);
			if(salt!=null) {
				String uid = (String) qr.getResults().get(0).get("uid");
				String sessionID = vars.getCookieManager().getRequestCookie("sessionID");
				if(validateCredentials(unVal, pVal, salt)) {
					DatabaseUtils.prepareAndExecute(deleteSessionQuery, false, sessionID);
					DatabaseUtils.prepareAndExecute(insertUserSessionQuery, true, sessionID, uid);
					vars.setRedirect(pageReturn);
				} else {
					une.setAttribute("class", "error");
					pwe.setAttribute("class", "error");
					unp.setText("Username and password do not match");
					pwp.setText("Username and password do not match");
				}
			} else {
				une.setAttribute("class", "error");
				pwe.setAttribute("class", "error");
				unp.setText("Username and password do not match");
				pwp.setText("Username and password do not match");
			}
		}
		e.addChild(une).addChild(unp).addChild(pwe).addChild(pwp).addChild(jhi).addChild(fti).addChild(sbm);
	}
	
	private String getSaltQuery = "SELECT `salt`,`uid` FROM `livecomply`.`users` WHERE `username`=?;";
	
	private QueryResults getNecessaryInfo(String username) {
		return DatabaseUtils.prepareAndExecute(getSaltQuery, true, username).get(0);
	}
	
	private String validateCredentialsQuery = "SELECT * FROM `livecomply`.`users` WHERE `username`=? AND `passhash`=?;";
	
	private boolean validateCredentials(String username, String password, String salt) {
		String passHash = SecurityUtils.sha512PasswordHash(password, salt);
		return !DatabaseUtils.prepareAndExecute(validateCredentialsQuery, true, username, passHash).get(0).getResults().isEmpty();
	}
	
	private String findTokenQuery = "SELECT * FROM `livecomply`.`form_tokens` WHERE `token` = ?;";
	private String insertTokenQuery = "INSERT INTO `livecomply`.`form_tokens` (`token`, `page`, `sessionID`) VALUES (?, ?, ?);";
	
	private String generateNewToken(String page, String sessionID) {
		String tryToken = "";
		boolean tokenAvailable = false;
		while(!tokenAvailable) {
			tryToken = SecurityUtils.genToken(64);
			if(DatabaseUtils.prepareAndExecute(findTokenQuery, true, tryToken).get(0).getResults().isEmpty()) {
				tokenAvailable = true;
			}
		}
		DatabaseUtils.prepareAndExecute(insertTokenQuery, true, tryToken, page, sessionID);
		return tryToken;
	}

	@Override
	public void generateContent(JDCHeadElement element) {
		
	}

}

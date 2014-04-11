package org.myhnuhai.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by 马英虎 on 14-3-14.
 */
public class JetBrickSessionListener implements HttpSessionListener {
    private final static Logger logger = LoggerFactory.getLogger(JetBrickSessionListener.class);
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info(se.getSession().getAttribute("UUID") + "--Login");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info(se.getSession().getAttribute("UUID") + "--Logout");
    }
}

package org.myhnuhai.util;

import java.util.ResourceBundle;

/**
 * Created by 马英虎 on 14-3-15.
 */
public class ConfigUtil {

    private final static ResourceBundle resource = ResourceBundle.getBundle("config");

    /**
     * huoqu SessionInfo
     * @return
     */
    public static final String getSessionInfoName() {
        return resource.getString("sessionInfoName");
    }

    public static final String get(String key) {
        return resource.getString(key);
    }
}

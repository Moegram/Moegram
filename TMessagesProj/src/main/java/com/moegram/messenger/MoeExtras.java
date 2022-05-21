/*
 * This is the source code of Moegram 8.7.x.
 * It's licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Ivan Korolyov, 2022
 */

package com.moegram.messenger;

import android.content.res.Configuration;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;

public class MoeExtras {
    // Here is "date of forwarded messages"
    public static String showForwardDate(MessageObject date, CharSequence main) {
        return !MoeConfig.dateOfForwardedMsg ? main.toString() : main + " (" + LocaleController.formatDate(date.messageOwner.fwd_from.date) + ")";
    }

    // It's a notification icons colors :)
    public static int setNotificationColor() {
        Configuration configuration = ApplicationLoader.applicationContext.getResources().getConfiguration();
        int nightModeFlags = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        int daycolor = BuildVars.isBetaApp() ? 0xff789395 : 0xff7c99ac;
        int nightcolor = BuildVars.isBetaApp() ? 0xffb4cfb0 : 0xffd3dedc;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                return nightcolor;
            case Configuration.UI_MODE_NIGHT_NO:
                return daycolor;
        }
        return daycolor;
    }
}

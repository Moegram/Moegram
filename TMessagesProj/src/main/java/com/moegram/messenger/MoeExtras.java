/*
 * This is the source code of Moegram 8.7.x.
 * It's licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Ivan Korolev, 2022
 */

package com.moegram.messenger;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;

public class MoeExtras {
    // Date of forwarded messages
    public static String showForwardDate(MessageObject date, CharSequence main) {
        return !MoeConfig.dateOfForwardedMsg ? main.toString() : main + " (" + LocaleController.formatDate(date.messageOwner.fwd_from.date) + ")";
    }
}

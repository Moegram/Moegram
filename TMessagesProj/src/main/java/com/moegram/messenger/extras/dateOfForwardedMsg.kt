package com.moegram.messenger.extras

import org.telegram.messenger.LocaleController
import org.telegram.messenger.MessageObject

import com.moegram.messenger.MoeConfig

object DateOfForwardedMsg {
    @JvmStatic
    fun showForwardDate(obj: MessageObject, orig: CharSequence): String {
        return if (!MoeConfig.dateOfForwardedMsg) orig.toString()
               else "$orig • ${LocaleController.formatDate(obj.messageOwner.fwd_from.date.toLong())}"
    }
}
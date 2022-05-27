package com.moegram.messenger;

import android.app.Activity;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;

public class MoeConfig {

    private static final Object sync = new Object();

    public static boolean useSystemFonts;
    public static boolean blurForAllThemes;

    public static boolean newGroup;
    public static boolean newSecretChat;
    public static boolean newChannel;
    public static boolean contacts;
    public static boolean calls;
    public static boolean peopleNearby;
    public static boolean archivedChats;
    public static boolean savedMessages;
    public static boolean scanQr;
    public static boolean inviteFriends;
    public static boolean telegramFeatures;
    public static int eventType;

    public static boolean hideAllChats;
    public static boolean hidePhoneNumber;
    public static boolean disableRounding;
    public static boolean showID;
    public static boolean chatsOnTitle;
    public static boolean disableVibration;
    public static int forceTabletMode;

    public static float stickerSize = 14.0f;
    public static boolean hideStickerTime;
    public static boolean hideGroupStickers;
    public static boolean disableGreetingSticker;
    public static boolean unlimitedRecentStickers;

    public static boolean hideSendAsChannel;
    public static boolean hideKeyboardOnScroll;
    public static boolean disableReactions;
    public static boolean disableJumpToNextChannel;
    public static boolean archiveOnPull;
    public static boolean dateOfForwardedMsg;
    public static boolean showMessageID;

    public static boolean rearVideoMessages;
    public static boolean disableCamera;
    public static boolean pauseOnMinimize;
    public static boolean disablePlayback;
    public static boolean disableSideActions;

    public static final int TABLET_AUTO = 0;
    public static final int TABLET_ENABLE = 1;
    public static final int TABLET_DISABLE = 2;

    private static boolean configLoaded;

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            if (configLoaded) {
                return;
            }

            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);

            useSystemFonts = preferences.getBoolean("useSystemFonts", false);
            blurForAllThemes = preferences.getBoolean("blurForAllThemes", true);

            newGroup = preferences.getBoolean("newGroup", true);
            newSecretChat = preferences.getBoolean("newSecretChat", false);
            newChannel = preferences.getBoolean("newChannel", false);
            contacts = preferences.getBoolean("contacts", true);
            calls = preferences.getBoolean("calls", false);
            peopleNearby = preferences.getBoolean("peopleNearby", false);
            archivedChats = preferences.getBoolean("archivedChats", true);
            savedMessages = preferences.getBoolean("savedMessages", true);
            scanQr = preferences.getBoolean("scanQr", true);
            inviteFriends = preferences.getBoolean("inviteFriends", false);
            telegramFeatures = preferences.getBoolean("telegramFeatures", true);
            eventType = preferences.getInt("eventType", 0);

            hideAllChats = preferences.getBoolean("hideAllChats", false);
            hidePhoneNumber = preferences.getBoolean("hidePhoneNumber", false);
            disableRounding = preferences.getBoolean("disableRounding", false);
            showID = preferences.getBoolean("showID", false);
            chatsOnTitle = preferences.getBoolean("chatsOnTitle", true);
            disableVibration = preferences.getBoolean("disableVibration", false);
            forceTabletMode = preferences.getInt("forceTabletMode", TABLET_AUTO);

            stickerSize = preferences.getFloat("stickerSize", 14.0f);
            hideStickerTime = preferences.getBoolean("hideStickerTime", false);
            hideGroupStickers = preferences.getBoolean("hideGroupStickers", false);
            disableGreetingSticker = preferences.getBoolean("disableGreetingSticker", false);
            unlimitedRecentStickers = preferences.getBoolean("unlimitedRecentStickers", false);

            hideSendAsChannel = preferences.getBoolean("hideSendAsChannel", false);
            hideKeyboardOnScroll = preferences.getBoolean("hideKeyboardOnScroll", true);
            disableReactions = preferences.getBoolean("disableReactions", false);
            disableJumpToNextChannel = preferences.getBoolean("disableJumpToNextChannel", false);
            archiveOnPull = preferences.getBoolean("archiveOnPull", true);
            dateOfForwardedMsg = preferences.getBoolean("dateOfForwardedMsg", false);
            showMessageID = preferences.getBoolean("showMessageID", false);

            rearVideoMessages = preferences.getBoolean("rearVideoMessages", false);
            disableCamera = preferences.getBoolean("disableCamera", false);
            pauseOnMinimize = preferences.getBoolean("pauseOnMinimize", true);
            disablePlayback = preferences.getBoolean("disablePlayback", true);
            disableSideActions = preferences.getBoolean("disableSideActions", false);

            configLoaded = true;
        }
    }

    public static void toggleUseSystemFonts() {
        useSystemFonts = !useSystemFonts;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("useSystemFonts", useSystemFonts);
        editor.apply();
    }

    public static void toggleDisableVibration() {
        disableVibration = !disableVibration;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableVibration", disableVibration);
        editor.apply();
    }

    public static void toggleBlurForAllThemes() {
        blurForAllThemes = !blurForAllThemes;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("blurForAllThemes", blurForAllThemes);
        editor.apply();
    }

    public static void toggleHideAllChats() {
        hideAllChats = !hideAllChats;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("hideAllChats", hideAllChats);
        editor.apply();
    }

    public static void toggleHidePhoneNumber() {
        hidePhoneNumber = !hidePhoneNumber;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("hidePhoneNumber", hidePhoneNumber);
        editor.apply();
    }

    public static void toggleDisableRounding() {
        disableRounding = !disableRounding;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableRounding", disableRounding);
        editor.apply();
    }

    public static void toggleShowID() {
        showID = !showID;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showID", showID);
        editor.apply();
    }

    public static void toggleChatsOnTitle() {
        chatsOnTitle = !chatsOnTitle;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("chatsOnTitle", chatsOnTitle);
        editor.apply();
    }

    public static void setForceTabletMode(int mode) {
        forceTabletMode = mode;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("forceTabletMode", forceTabletMode);
        editor.apply();
    }

    public static void setStickerSize(float size) {
        stickerSize = size;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("stickerSize", stickerSize);
        editor.apply();
    }

    public static void toggleHideStickerTime() {
        hideStickerTime = !hideStickerTime;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("hideStickerTime", hideStickerTime);
        editor.apply();
    }

    public static void toggleHideGroupStickers() {
        hideGroupStickers = !hideGroupStickers;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("hideGroupStickers", hideGroupStickers);
        editor.apply();
    }

    public static void toggleUnlimitedRecentStickers() {
        unlimitedRecentStickers = !unlimitedRecentStickers;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("unlimitedRecentStickers", unlimitedRecentStickers);
        editor.apply();
    }

    public static void toggleHideSendAsChannel() {
        hideSendAsChannel = !hideSendAsChannel;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("hideSendAsChannel", hideSendAsChannel);
        editor.apply();
    }

    public static void toggleHideKeyboardOnScroll() {
        hideKeyboardOnScroll = !hideKeyboardOnScroll;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("hideKeyboardOnScroll", hideKeyboardOnScroll);
        editor.apply();
    }

    public static void toggleDisableReactions() {
        disableReactions = !disableReactions;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableReactions", disableReactions);
        editor.apply();
    }

    public static void toggleDisableGreetingSticker() {
        disableGreetingSticker = !disableGreetingSticker;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableGreetingSticker", disableGreetingSticker);
        editor.apply();
    }

    public static void toggleDisableJumpToNextChannel() {
        disableJumpToNextChannel = !disableJumpToNextChannel;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableJumpToNextChannel", disableJumpToNextChannel);
        editor.apply();
    }

    public static void toggleArchiveOnPull() {
        archiveOnPull = !archiveOnPull;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("archiveOnPull", archiveOnPull);
        editor.apply();
    }

    public static void toggleDateOfForwardedMsg() {
        dateOfForwardedMsg = !dateOfForwardedMsg;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("dateOfForwardedMsg", dateOfForwardedMsg);
        editor.apply();
    }

    public static void toggleShowMessageID() {
        showMessageID = !showMessageID;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showMessageID", showMessageID);
        editor.apply();
    }

    public static void toggleRearVideoMessages() {
        rearVideoMessages = !rearVideoMessages;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("rearVideoMessages", rearVideoMessages);
        editor.apply();
    }

    public static void toggleDisableCamera() {
        disableCamera = !disableCamera;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableCamera", disableCamera);
        editor.apply();
    }

    public static void togglePauseOnMinimize() {
        pauseOnMinimize = !pauseOnMinimize;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("pauseOnMinimize", pauseOnMinimize);
        editor.apply();
    }
    public static void toggleDisablePlayback() {
        disablePlayback = !disablePlayback;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disablePlayback", disablePlayback);
        editor.apply();
    }

    public static void toggleDisableSideActions() {
        disableSideActions = !disableSideActions;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("disableSideActions", disableSideActions);
        editor.apply();
    }

    public static void toggleNewGroup() {
        newGroup = !newGroup;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("newGroup", newGroup);
        editor.apply();
    }

    public static void toggleNewSecretChat() {
        newSecretChat = !newSecretChat;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("newSecretChat", newSecretChat);
        editor.apply();
    }

    public static void toggleNewChannel() {
        newChannel = !newChannel;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("newChannel", newChannel);
        editor.apply();
    }

    public static void toggleContacts() {
        contacts = !contacts;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("contacts", contacts);
        editor.apply();
    }

    public static void toggleCalls() {
        calls = !calls;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("calls", calls);
        editor.apply();
    }

    public static void togglePeopleNearby() {
        peopleNearby = !peopleNearby;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("peopleNearby", peopleNearby);
        editor.apply();
    }

    public static void toggleArchivedChats() {
        archivedChats = !archivedChats;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("archivedChats", archivedChats);
        editor.apply();
    }

    public static void toggleSavedMessages() {
        savedMessages = !savedMessages;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("savedMessages", savedMessages);
        editor.apply();
    }

    public static void toggleScanQr() {
        scanQr = !scanQr;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("scanQr", scanQr);
        editor.apply();
    }

    public static void toggleInviteFriends() {
        inviteFriends = !inviteFriends;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("inviteFriends", inviteFriends);
        editor.apply();
    }

    public static void toggleTelegramFeatures() {
        telegramFeatures = !telegramFeatures;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("telegramFeatures", telegramFeatures);
        editor.apply();
    }

    public static void setEventType(int type) {
        eventType = type;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("moeconfig", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("eventType", eventType);
        editor.apply();
    }
}

package com.exteragram.messenger.preferences;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UndoView;

import com.exteragram.messenger.ExteraConfig;

public class AppearancePreferencesEntry extends BaseFragment {

    private int rowCount;
    private ListAdapter listAdapter;
    private ValueAnimator statusBarColorAnimate;

    private int applicationHeaderRow;
    private int useSystemFontsRow;
    private int useSystemEmojiRow;
    private int transparentStatusBarRow;
    private int blurForAllThemesRow;
    private int applicationDividerRow;

    private int drawerHeaderRow;
    private int eventChooserRow;
    private int drawerSettingsRow;
    private int drawerDividerRow;

    private int generalHeaderRow;
    private int hideAllChatsRow;
    private int hidePhoneNumberRow;
    private int showIDRow;
    private int chatsOnTitleRow;
    private int disableVibrationRow;
    private int forceTabletModeRow;
    private int generalDividerRow;

    // Icons
    private int newGroupIcon;
    private int newSecretIcon;
    private int newChannelIcon;
    private int contactsIcon;
    private int callsIcon;
    private int archiveIcon;
    private int savedIcon;
    private int settingsIcon;
    private int scanQrIcon;
    private int inviteIcon;
    private int helpIcon;
    private int peopleNearbyIcon;

    private UndoView restartTooltip;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRowsId(true);
        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("Appearance", R.string.Appearance));
        actionBar.setAllowOverlayTitle(false);

        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        listAdapter = new ListAdapter(context);
        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);

        if (listView.getItemAnimator() != null) {
            ((DefaultItemAnimator) listView.getItemAnimator()).setDelayAnimations(false);
        }

        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position, x, y) -> {
            if (position == useSystemFontsRow) {
                ExteraConfig.toggleUseSystemFonts();
                AndroidUtilities.clearTypefaceCache();

                Parcelable recyclerViewState = null;
                if (listView.getLayoutManager() != null) {
                    recyclerViewState = listView.getLayoutManager().onSaveInstanceState();
                }

                AlertDialog progressDialog = new AlertDialog(context, 3);
                progressDialog.show();
                AndroidUtilities.runOnUIThread(progressDialog::dismiss, 2000);

                parentLayout.rebuildAllFragmentViews(true, true);
                listView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            } else if (position == useSystemEmojiRow) {
                SharedConfig.toggleUseSystemEmoji();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(SharedConfig.useSystemEmoji);
                }

                AlertDialog progressDialog = new AlertDialog(context, 3);
                progressDialog.show();
                AndroidUtilities.runOnUIThread(progressDialog::dismiss, 500);

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == transparentStatusBarRow) {
                SharedConfig.toggleNoStatusBar();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(SharedConfig.noStatusBar);
                }

                int color = Theme.getColor(Theme.key_actionBarDefault, null, true);
                int alpha = ColorUtils.calculateLuminance(color) > 0.7f ? 0x0f : 0x33;

                if (statusBarColorAnimate != null && statusBarColorAnimate.isRunning()) {
                    statusBarColorAnimate.end();
                }

                statusBarColorAnimate = SharedConfig.noStatusBar ? ValueAnimator.ofInt(alpha, 0) : ValueAnimator.ofInt(0, alpha);
                statusBarColorAnimate.setDuration(200);
                statusBarColorAnimate.addUpdateListener(animation -> getParentActivity().getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(0, (int) animation.getAnimatedValue())));
                statusBarColorAnimate.start();
            } else if (position == blurForAllThemesRow) {
                ExteraConfig.toggleBlurForAllThemes();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.blurForAllThemes);
                }
                restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
            } else if (position == eventChooserRow) {
                if (getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("DrawerIconPack", R.string.DrawerIconPack));
                builder.setItems(new CharSequence[]{
                        LocaleController.getString("Default", R.string.Default),
                        LocaleController.getString("NewYear", R.string.NewYear),
                        LocaleController.getString("ValentinesDay", R.string.ValentinesDay),
                        LocaleController.getString("Halloween", R.string.Halloween)
                }, (dialog, which) -> {
                    ExteraConfig.setEventType(which);
                    RecyclerView.ViewHolder holder = listView.findViewHolderForAdapterPosition(eventChooserRow);
                    if (holder != null) {
                        listAdapter.onBindViewHolder(holder, eventChooserRow);
                    }
                    Parcelable recyclerViewState = null;

                    if (listView.getLayoutManager() != null) {
                        recyclerViewState = listView.getLayoutManager().onSaveInstanceState();
                    }

                    AlertDialog progressDialog = new AlertDialog(context, 3);
                    progressDialog.show();
                    AndroidUtilities.runOnUIThread(progressDialog::dismiss, 500);

                    parentLayout.rebuildAllFragmentViews(true, true);
                    listView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                showDialog(builder.create());
            } else if (position == drawerSettingsRow) {
                presentFragment(new DrawerPreferencesEntry());
            } else if (position == hideAllChatsRow) {
                ExteraConfig.toggleHideAllChats();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.hideAllChats);
                }

                AlertDialog progressDialog = new AlertDialog(context, 3);
                progressDialog.show();
                AndroidUtilities.runOnUIThread(progressDialog::dismiss, 500);

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == hidePhoneNumberRow) {
                ExteraConfig.toggleHidePhoneNumber();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.hidePhoneNumber);
                }
                parentLayout.rebuildAllFragmentViews(false, false);
                getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
            } else if (position == showIDRow) {
                ExteraConfig.toggleShowID();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.showID);
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == chatsOnTitleRow) {
                ExteraConfig.toggleChatsOnTitle();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.chatsOnTitle);
                }

                AlertDialog progressDialog = new AlertDialog(context, 3);
                progressDialog.show();
                AndroidUtilities.runOnUIThread(progressDialog::dismiss, 500);

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == disableVibrationRow) {
                ExteraConfig.toggleDisableVibration();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.disableVibration);
                }
                restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
            } else if (position == forceTabletModeRow) {
                ExteraConfig.toggleForceTabletMode();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(ExteraConfig.forceTabletMode);
                }
                restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
            }
        });

        restartTooltip = new UndoView(context);
        restartTooltip.setInfoText(LocaleController.formatString("RestartRequired", R.string.RestartRequired));
        frameLayout.addView(restartTooltip, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT, 8, 0, 8, 8));
        return fragmentView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRowsId(boolean notify) {
        rowCount = 0;

        applicationHeaderRow = rowCount++;
        useSystemFontsRow = rowCount++;
        useSystemEmojiRow = rowCount++;
        transparentStatusBarRow = rowCount++;
        blurForAllThemesRow = rowCount++;
        applicationDividerRow = rowCount++;

        drawerHeaderRow = rowCount++;
        eventChooserRow = rowCount++;
        drawerSettingsRow = rowCount++;
        drawerDividerRow = rowCount++;

        generalHeaderRow = rowCount++;
        hideAllChatsRow = rowCount++;
        hidePhoneNumberRow = rowCount++;
        showIDRow = rowCount++;
        chatsOnTitleRow = rowCount++;
        disableVibrationRow = rowCount++;
        forceTabletModeRow = rowCount++;
        generalDividerRow = rowCount++;

        if (listAdapter != null && notify) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (ExteraConfig.eventType) {
                case 1:
                    newGroupIcon = R.drawable.menu_groups_ny;
                    newSecretIcon = R.drawable.menu_secret_ny;
                    newChannelIcon = R.drawable.menu_channel_ny;
                    contactsIcon = R.drawable.menu_contacts_ny;
                    callsIcon = R.drawable.menu_calls_ny;
                    archiveIcon = R.drawable.msg_archive;
                    savedIcon = R.drawable.menu_bookmarks_ny;
                    settingsIcon = R.drawable.menu_settings_ny;
                    archiveIcon = R.drawable.chats_archive;
                    scanQrIcon = R.drawable.msg_qrcode;
                    inviteIcon = R.drawable.menu_invite_ny;
                    helpIcon = R.drawable.menu_help_ny;
                    peopleNearbyIcon = R.drawable.menu_nearby_ny;
                    break;
                case 2:
                    newGroupIcon = R.drawable.menu_groups_14;
                    newSecretIcon = R.drawable.menu_secret_14;
                    newChannelIcon = R.drawable.menu_broadcast_14;
                    contactsIcon = R.drawable.menu_contacts_14;
                    callsIcon = R.drawable.menu_calls_14;
                    archiveIcon = R.drawable.msg_archive;
                    savedIcon = R.drawable.menu_bookmarks_14;
                    settingsIcon = R.drawable.menu_settings_14;
                    scanQrIcon = R.drawable.msg_qrcode;
                    inviteIcon = R.drawable.menu_secret_ny;
                    helpIcon = R.drawable.menu_help;
                    peopleNearbyIcon = R.drawable.menu_secret_14;
                    break;
                case 3:
                    newGroupIcon = R.drawable.menu_groups_hw;
                    newSecretIcon = R.drawable.menu_secret_hw;
                    newChannelIcon = R.drawable.menu_broadcast_hw;
                    contactsIcon = R.drawable.menu_contacts_hw;
                    callsIcon = R.drawable.menu_calls_hw;
                    archiveIcon = R.drawable.msg_archive;
                    savedIcon = R.drawable.menu_bookmarks_hw;
                    settingsIcon = R.drawable.menu_settings_hw;
                    scanQrIcon = R.drawable.msg_qrcode;
                    inviteIcon = R.drawable.menu_invite_hw;
                    helpIcon = R.drawable.menu_help_hw;
                    peopleNearbyIcon = R.drawable.menu_secret_hw;
                    break;
                default:
                    newGroupIcon = R.drawable.menu_groups;
                    newSecretIcon = R.drawable.menu_secret;
                    newChannelIcon = R.drawable.menu_broadcast;
                    contactsIcon = R.drawable.menu_contacts;
                    callsIcon = R.drawable.menu_calls;
                    archiveIcon = R.drawable.msg_archive;
                    savedIcon = R.drawable.menu_saved;
                    settingsIcon = R.drawable.menu_settings;
                    scanQrIcon = R.drawable.msg_qrcode;
                    inviteIcon = R.drawable.menu_invite;
                    helpIcon = R.drawable.menu_help;
                    peopleNearbyIcon = R.drawable.menu_nearby;
                    break;
            }

            switch (holder.getItemViewType()) {
                case 1:
                    holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == applicationHeaderRow) {
                        headerCell.setText(LocaleController.getString("Appearance", R.string.Appearance));
                    } else if (position == drawerHeaderRow){
                        headerCell.setText(LocaleController.getString("DrawerOptions", R.string.DrawerOptions));
                    } else if (position == generalHeaderRow) {
                        headerCell.setText(LocaleController.getString("General", R.string.General));
                    }
                    break;
                case 3:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == transparentStatusBarRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("TransparentStatusBar", R.string.TransparentStatusBar), SharedConfig.noStatusBar, true);
                    } else if (position == useSystemFontsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UseSystemFonts", R.string.UseSystemFonts), ExteraConfig.useSystemFonts, true);
                    } else if (position == useSystemEmojiRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UseSystemEmoji", R.string.UseSystemEmoji), SharedConfig.useSystemEmoji, true);
                    } else if (position == blurForAllThemesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("BlurForAllThemes", R.string.BlurForAllThemes), ExteraConfig.blurForAllThemes, false);
                    } else if (position == hideAllChatsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideAllChats", R.string.HideAllChats), ExteraConfig.hideAllChats, true);
                    } else if (position == hidePhoneNumberRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HidePhoneNumber", R.string.HidePhoneNumber), ExteraConfig.hidePhoneNumber, true);
                    } else if (position == showIDRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowID", R.string.ShowID), ExteraConfig.showID, true);
                    } else if (position == chatsOnTitleRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ChatsOnTitle", R.string.ChatsOnTitle), ExteraConfig.chatsOnTitle, true);
                    } else if (position == disableVibrationRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableVibration", R.string.DisableVibration), ExteraConfig.disableVibration, true);
                    } else if (position == forceTabletModeRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ForceTabletMode", R.string.ForceTabletMode), ExteraConfig.forceTabletMode, false);
                    }
                    break;
                case 4:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) holder.itemView;
                    if (position == eventChooserRow) {
                        String value;
                        if (ExteraConfig.eventType == 1) {
                            value = LocaleController.getString("NewYear", R.string.NewYear);
                        } else if (ExteraConfig.eventType == 2) {
                            value = LocaleController.getString("ValentinesDay", R.string.ValentinesDay);
                        } else if (ExteraConfig.eventType == 3) {
                            value = LocaleController.getString("Halloween", R.string.Halloween);
                        } else {
                            value = LocaleController.getString("Default", R.string.Default);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("DrawerIconPack", R.string.DrawerIconPack), value, true);
                    }
                    break;
                case 5:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == drawerSettingsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Drawer", R.string.Drawer), R.drawable.msg_list, false);
                    }
                    break;
            }
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 3 || type == 7;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 2:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 5:
                    view = new TextCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new ShadowSectionCell(mContext);
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == applicationDividerRow || position == drawerDividerRow || position == generalDividerRow) {
                return 1;
            } else if (position == applicationHeaderRow || position == drawerHeaderRow || position == generalHeaderRow) {
                return 2;
            } else if (position == useSystemFontsRow || position == useSystemEmojiRow || position == transparentStatusBarRow ||
                       position == blurForAllThemesRow || position == hideAllChatsRow || position == hidePhoneNumberRow ||
                       position == showIDRow || position == chatsOnTitleRow || position == disableVibrationRow ||
                       position == forceTabletModeRow) {
                return 3;
            } else if (position == eventChooserRow) {
                return 4;
            } else if (position == drawerSettingsRow) {
                return 5;
            }
            return 1;
        }
    }
}

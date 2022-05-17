package com.moegram.messenger.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UndoView;

import com.moegram.messenger.MoeConfig;
import com.moegram.messenger.preferences.cells.TextCheckWithIconCell;

public class DrawerPreferencesEntry extends BaseFragment {

    private int rowCount;
    private ListAdapter listAdapter;

    private int drawerHeaderRow;
    private int newGroupRow;
    private int newSecretChatRow;
    private int newChannelRow;
    private int contactsRow;
    private int callsRow;
    private int peopleNearbyRow;
    private int archivedChatsRow;
    private int savedMessagesRow;
    private int scanQrRow;
    private int inviteFriendsRow;
    private int telegramFeaturesRow;
    private int drawerDividerRow;

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
        updateRows();
        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("Drawer", R.string.Drawer));
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
            if (position == newGroupRow) {
                MoeConfig.toggleNewGroup();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.newGroup);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == newSecretChatRow) {
                MoeConfig.toggleNewSecretChat();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.newSecretChat);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == newChannelRow) {
                MoeConfig.toggleNewChannel();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.newChannel);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == contactsRow) {
                MoeConfig.toggleContacts();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.contacts);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == callsRow) {
                MoeConfig.toggleCalls();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.calls);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == peopleNearbyRow) {
                MoeConfig.togglePeopleNearby();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.peopleNearby);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == archivedChatsRow) {
                MoeConfig.toggleArchivedChats();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.archivedChats);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == savedMessagesRow) {
                MoeConfig.toggleSavedMessages();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.savedMessages);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == scanQrRow) {
                MoeConfig.toggleScanQr();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.scanQr);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == inviteFriendsRow) {
                MoeConfig.toggleInviteFriends();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.inviteFriends);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == telegramFeaturesRow) {
                MoeConfig.toggleTelegramFeatures();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(MoeConfig.telegramFeatures);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            }
        });
        restartTooltip = new UndoView(context);
        restartTooltip.setInfoText(LocaleController.formatString("RestartRequired", R.string.RestartRequired));
        frameLayout.addView(restartTooltip, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT, 8, 0, 8, 8));
        return fragmentView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRows() {
        rowCount = 0;
    
        drawerHeaderRow = rowCount++;
        newGroupRow = rowCount++;
        newSecretChatRow = rowCount++;
        newChannelRow = rowCount++;
        contactsRow = rowCount++;
        callsRow = rowCount++;
        peopleNearbyRow = rowCount++;
        archivedChatsRow = rowCount++;
        savedMessagesRow = rowCount++;
        scanQrRow = rowCount++;
        inviteFriendsRow = rowCount++;
        telegramFeaturesRow = rowCount++;
        drawerDividerRow = rowCount++;

        if (listAdapter != null) {
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
            switch (MoeConfig.eventType) {
                case 1:
                    newGroupIcon = R.drawable.menu_groups_ny;
                    newSecretIcon = R.drawable.menu_secret_ny;
                    newChannelIcon = R.drawable.menu_channel_ny;
                    contactsIcon = R.drawable.menu_contacts_ny;
                    callsIcon = R.drawable.menu_calls_ny;
                    savedIcon = R.drawable.menu_bookmarks_ny;
                    settingsIcon = R.drawable.menu_settings_ny;
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
                    savedIcon = R.drawable.menu_bookmarks_14;
                    settingsIcon = R.drawable.menu_settings_14;
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
                    savedIcon = R.drawable.menu_bookmarks_hw;
                    settingsIcon = R.drawable.menu_settings_hw;
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
                    savedIcon = R.drawable.menu_saved;
                    settingsIcon = R.drawable.menu_settings;
                    inviteIcon = R.drawable.menu_invite;
                    helpIcon = R.drawable.menu_help;
                    peopleNearbyIcon = R.drawable.menu_nearby;
                    break;
            }
            switch (holder.getItemViewType()) {
                case 1:
                    if (position == drawerDividerRow) {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    } else {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    }
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == drawerHeaderRow) {
                        headerCell.setText(LocaleController.getString("DrawerElements", R.string.DrawerElements));
                    }
                    break;
                case 3:
                    TextCheckWithIconCell textCheckWithIconCell = (TextCheckWithIconCell) holder.itemView;
                    textCheckWithIconCell.setEnabled(true, null);
                    if (position == newGroupRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("NewGroup", R.string.NewGroup), newGroupIcon, MoeConfig.newGroup, true);
                    } else if (position == newSecretChatRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("NewSecretChat", R.string.NewSecretChat), newSecretIcon, MoeConfig.newSecretChat, true);
                    } else if (position == newChannelRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("NewChannel", R.string.NewChannel), newChannelIcon, MoeConfig.newChannel, true);
                    } else if (position == contactsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("Contacts", R.string.Contacts), contactsIcon, MoeConfig.contacts, true);
                    } else if (position == callsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("Calls", R.string.Calls), callsIcon, MoeConfig.calls, true);
                    } else if (position == peopleNearbyRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("PeopleNearby", R.string.PeopleNearby), peopleNearbyIcon, MoeConfig.peopleNearby, true);
                    } else if (position == archivedChatsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("ArchivedChats", R.string.ArchivedChats), R.drawable.msg_archive, MoeConfig.archivedChats, true);
                    } else if (position == savedMessagesRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("SavedMessages", R.string.SavedMessages), savedIcon, MoeConfig.savedMessages, true);
                    } else if (position == scanQrRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient), R.drawable.msg_qrcode, MoeConfig.scanQr, true);
                    } else if (position == inviteFriendsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("InviteFriends", R.string.InviteFriends), inviteIcon, MoeConfig.inviteFriends, true);
                    } else if (position == telegramFeaturesRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("TelegramFeatures", R.string.TelegramFeatures), helpIcon, MoeConfig.telegramFeatures, false);
                    }
                    break;
            }
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
                    view = new TextCheckWithIconCell(mContext);
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
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == drawerDividerRow) {
                return 1;
            } else if (position == drawerHeaderRow) {
                return 2;
            } else if (position == newGroupRow || position == newSecretChatRow || position == newChannelRow ||
                       position == contactsRow || position == callsRow || position == peopleNearbyRow ||
                       position == archivedChatsRow || position == savedMessagesRow || position == scanQrRow ||
                       position == telegramFeaturesRow || position == inviteFriendsRow) {
                return 3;
            }
            return 1;
        }
    }
}

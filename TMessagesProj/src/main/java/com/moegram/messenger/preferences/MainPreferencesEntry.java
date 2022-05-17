package com.moegram.messenger.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

import com.moegram.messenger.preferences.cells.MoegramCell;

public class MainPreferencesEntry extends BaseFragment {
    private int rowCount;
    private ListAdapter listAdapter;

    private int MoegramRow;
    private int MoegramDividerRow;

    private int categoriesHeaderRow;
    private int appearanceRow;
    private int chatsRow;
    private int categoriesDividerRow;

    private int linksHeaderRow;
    private int sourceCodeRow;
    private int channelRow;
    private int groupRow;
    private int linksDividerRow;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("Preferences", R.string.Preferences));
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
            if (position == appearanceRow) {
                presentFragment(new AppearancePreferencesEntry());
            } else if (position == chatsRow) {
                presentFragment(new ChatsPreferencesEntry());
            } else if (position == channelRow) {
                MessagesController.getInstance(currentAccount).openByUserName(("Moegram"), this, 1);
            } else if (position == groupRow) {
                MessagesController.getInstance(currentAccount).openByUserName(("MoegramChat"), this, 1);
            } else if (position == sourceCodeRow) {
                Browser.openUrl(getParentActivity(), "https://github.com/Moegram/Moegram");
            }
        });
        return fragmentView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRows() {
        rowCount = 0;

        MoegramRow = rowCount++;
        MoegramDividerRow = rowCount++;

        categoriesHeaderRow = rowCount++;
        appearanceRow = rowCount++;
        chatsRow = rowCount++;
        categoriesDividerRow = rowCount++;

        linksHeaderRow = rowCount++;
        channelRow = rowCount++;
        groupRow = rowCount++;
        sourceCodeRow = rowCount++;
        linksDividerRow = rowCount++;

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
            switch (holder.getItemViewType()) {
                case 1:
                    if (position == MoegramDividerRow || position == categoriesDividerRow || position == linksDividerRow) {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    } else {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    }
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == categoriesHeaderRow) {
                        headerCell.setText(LocaleController.getString("Categories", R.string.Categories));
                    } else if (position == linksHeaderRow) {
                        headerCell.setText(LocaleController.getString("Links", R.string.Links));
                    }
                    break;
                case 3:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == sourceCodeRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("SourceCode", R.string.SourceCode), "Github", R.drawable.msg_report_spam, false);
                    } else if (position == channelRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("Channel", R.string.Channel), "@Moegram", R.drawable.msg_channel, true);
                    } else if (position == groupRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("Chat", R.string.Chat), "@MoegramChat", R.drawable.menu_groups, true);
                    } else if (position == appearanceRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Appearance", R.string.Appearance), R.drawable.msg_theme, true);
                    } else if (position == chatsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Chats", R.string.Chats), R.drawable.menu_chats, false);
                    }
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    break;
                case 4:
                    MoegramCell textDetailCell = (MoegramCell) holder.itemView;
                    textDetailCell.setMultilineDetail(true);
                    if (position == MoegramRow) {
                        if (BuildVars.isBetaApp()) {
                            textDetailCell.setTextAndValueAndIcon("Moegram β | v" + BuildVars.BUILD_VERSION_STRING, LocaleController.getString("MoegramDescription", R.string.MoegramDescription), R.drawable.ic_logo_foreground, false);
                        } else {
                            textDetailCell.setTextAndValueAndIcon("Moegram | v" + BuildVars.BUILD_VERSION_STRING, LocaleController.getString("MoegramDescription", R.string.MoegramDescription), R.drawable.ic_logo_foreground, false);
                        }
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
                    view = new TextCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new MoegramCell(mContext);
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
            return type == 3 || type == 4;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == MoegramDividerRow || position == categoriesDividerRow || position == linksDividerRow) {
                return 1;
            } else if (position == categoriesHeaderRow || position == linksHeaderRow) {
                return 2;
            } else if (position == appearanceRow || position == chatsRow ||
                       position == channelRow || position == groupRow || position == sourceCodeRow) {
                return 3;
            } else if (position == MoegramRow) {
                return 4;
            }
            return 1;
        }
    }
}

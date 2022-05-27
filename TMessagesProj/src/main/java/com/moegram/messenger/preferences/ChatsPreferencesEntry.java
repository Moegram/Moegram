package com.moegram.messenger.preferences;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.UndoView;

import com.moegram.messenger.MoeConfig;
import com.moegram.messenger.preferences.cells.StickerSizePreviewCell;

public class ChatsPreferencesEntry extends BaseFragment {

    private int rowCount;
    private ListAdapter listAdapter;

    private ActionBarMenuItem resetItem;
    private StickerSizeCell stickerSizeCell;

    private int stickerSizeHeaderRow;
    private int stickerSizeRow;
    private int stickerSizeDividerRow;

    private int stickersHeaderRow;
    private int hideStickerTimeRow;
    private int hideGroupStickersRow;
    private int disableGreetingStickerRow;
    private int unlimitedRecentStickersRow;
    private int stickersDividerRow;

    private int chatHeaderRow;
    private int hideSendAsChannelRow;
    private int hideKeyboardOnScrollRow;
    private int disableReactionsRow;
    private int disableJumpToNextChannelRow;
    private int archiveOnPullRow;
    private int dateOfForwardedMsgRow;
    private int showMessageIDRow;
    private int chatDividerRow;

    private int mediaHeaderRow;
    private int rearVideoMessagesRow;
    private int disableProximityEventsRow;
    private int pauseOnMinimizeRow;
    private int disablePlaybackRow;
    private int disableSideActionsRow;
    private int disableCameraRow;
    private int mediaDividerRow;

    private UndoView restartTooltip;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    private class StickerSizeCell extends FrameLayout {

        private final StickerSizePreviewCell messagesCell;
        private final SeekBarView sizeBar;
        private final int startStickerSize = 8;
        private final int endStickerSize = 20;

        private final TextPaint textPaint;
        private int lastWidth;

        public StickerSizeCell (Context context) {
            super(context);

            setWillNotDraw(false);

            textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextSize(AndroidUtilities.dp(16));

            sizeBar = new SeekBarView(context);
            sizeBar.setReportChanges(true);
            sizeBar.setDelegate(new SeekBarView.SeekBarViewDelegate() {
                @Override
                public void onSeekBarDrag(boolean stop, float progress) {
                    sizeBar.getSeekBarAccessibilityDelegate().postAccessibilityEventRunnable(StickerSizeCell.this);
                    MoeConfig.setStickerSize(startStickerSize + (endStickerSize - startStickerSize) * progress);
                    StickerSizeCell.this.invalidate();
                    if (resetItem.getVisibility() != VISIBLE) {
                        AndroidUtilities.updateViewVisibilityAnimated(resetItem, true, 0.5f, true);
                    }
                }

                @Override
                public void onSeekBarPressed(boolean pressed) {

                }
            });
            sizeBar.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
            addView(sizeBar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 38, Gravity.LEFT | Gravity.TOP, 9, 5, 43, 11));

            messagesCell = new StickerSizePreviewCell(context, parentLayout);
            messagesCell.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
            addView(messagesCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP, 0, 53, 0, 0));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            canvas.drawText(String.valueOf(Math.round(MoeConfig.stickerSize)), getMeasuredWidth() - AndroidUtilities.dp(39), AndroidUtilities.dp(28), textPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (lastWidth != width) {
                sizeBar.setProgress((MoeConfig.stickerSize - startStickerSize) / (float) (endStickerSize - startStickerSize));
                lastWidth = width;
            }
        }

        @Override
        public void invalidate() {
            super.invalidate();
            lastWidth = -1;
            messagesCell.invalidate();
            sizeBar.invalidate();
        }

        @Override
        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityEvent(this, event);
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, info);
        }

        @Override
        public boolean performAccessibilityAction(int action, Bundle arguments) {
            return super.performAccessibilityAction(action, arguments) || sizeBar.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, action, arguments);
        }
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("Chats", R.string.Chats));
        actionBar.setAllowOverlayTitle(false);

        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }

        ActionBarMenu menu = actionBar.createMenu();

        resetItem = menu.addItem(0, R.drawable.msg_reset);
        resetItem.setContentDescription(LocaleController.getString("Reset", R.string.Reset));
        resetItem.setVisibility(MoeConfig.stickerSize != 14.0f ? View.VISIBLE : View.GONE);
        resetItem.setTag(null);
        resetItem.setOnClickListener(v -> {
            AndroidUtilities.updateViewVisibilityAnimated(resetItem, false, 0.5f, true);
            ValueAnimator animator = ValueAnimator.ofFloat(MoeConfig.stickerSize, 14.0f);
            animator.setDuration(200);
            animator.addUpdateListener(valueAnimator -> {
                MoeConfig.setStickerSize((Float) valueAnimator.getAnimatedValue());
                stickerSizeCell.invalidate();
            });
            animator.start();
        });

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
            if (position == hideStickerTimeRow) {
                MoeConfig.toggleHideStickerTime();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.hideStickerTime);
                }
            } else if (position == hideGroupStickersRow) {
                MoeConfig.toggleHideGroupStickers();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.hideGroupStickers);
                }
            } else if (position == disableGreetingStickerRow) {
                MoeConfig.toggleDisableGreetingSticker();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disableGreetingSticker);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == unlimitedRecentStickersRow) {
                MoeConfig.toggleUnlimitedRecentStickers();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.unlimitedRecentStickers);
                }
            } else if (position == hideSendAsChannelRow) {
                MoeConfig.toggleHideSendAsChannel();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.hideSendAsChannel);
                }
            } else if (position == hideKeyboardOnScrollRow) {
                MoeConfig.toggleHideKeyboardOnScroll();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.hideKeyboardOnScroll);
                }
            } else if (position == disableReactionsRow) {
                MoeConfig.toggleDisableReactions();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disableReactions);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == disableJumpToNextChannelRow) {
                MoeConfig.toggleDisableJumpToNextChannel();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disableJumpToNextChannel);
                }

                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == archiveOnPullRow) {
                MoeConfig.toggleArchiveOnPull();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.archiveOnPull);
                }
            } else if (position == dateOfForwardedMsgRow) {
                MoeConfig.toggleDateOfForwardedMsg();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.dateOfForwardedMsg);
                }
            } else if (position == showMessageIDRow) {
                MoeConfig.toggleShowMessageID();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.showMessageID);
                }
            } else if (position == rearVideoMessagesRow) {
                MoeConfig.toggleRearVideoMessages();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.rearVideoMessages);
                }
            } else if (position == disableProximityEventsRow) {
                MoeConfig.toggleDisableProximityEvents();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disableProximityEvents);
                }
            } else if (position == pauseOnMinimizeRow) {
                MoeConfig.togglePauseOnMinimize();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.pauseOnMinimize);
                }
            } else if (position == disablePlaybackRow) {
                MoeConfig.toggleDisablePlayback();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disablePlayback);
                }
                restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
            } else if (position == disableSideActionsRow) {
                MoeConfig.toggleDisableSideActions();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disableSideActions);
                }
                restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
            } else if (position == disableCameraRow) {
                MoeConfig.toggleDisableCamera();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MoeConfig.disableCamera);
                }
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

        stickerSizeHeaderRow = rowCount++;
        stickerSizeRow = rowCount++;
        stickerSizeDividerRow = rowCount++;

        stickersHeaderRow = rowCount++;
        hideStickerTimeRow = rowCount++;
        hideGroupStickersRow = rowCount++;
        disableGreetingStickerRow = rowCount++;
        unlimitedRecentStickersRow = rowCount++;
        stickersDividerRow = rowCount++;

        chatHeaderRow = rowCount++;
        hideSendAsChannelRow = rowCount++;
        hideKeyboardOnScrollRow = rowCount++;
        disableReactionsRow = rowCount++;
        disableJumpToNextChannelRow = rowCount++;
        archiveOnPullRow = rowCount++;
        dateOfForwardedMsgRow = rowCount++;
        showMessageIDRow = rowCount++;
        chatDividerRow = rowCount++;

        mediaHeaderRow = rowCount++;
        rearVideoMessagesRow = rowCount++;
        disableProximityEventsRow = rowCount++;
        pauseOnMinimizeRow = rowCount++;
        disablePlaybackRow = rowCount++;
        disableSideActionsRow = rowCount++;
        disableCameraRow = rowCount++;
        mediaDividerRow = rowCount++;

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
                    if (position == stickerSizeDividerRow || position == stickersDividerRow || position == chatDividerRow ||
                        position == mediaDividerRow) {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    } else {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    }
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == stickerSizeHeaderRow) {
                        headerCell.setText(LocaleController.getString("StickerSize", R.string.StickerSize));
                    } else if (position == stickersHeaderRow) {
                        headerCell.setText(LocaleController.getString("AccDescrStickers", R.string.AccDescrStickers));
                    } else if (position == chatHeaderRow) {
                        headerCell.setText(LocaleController.getString("Chats", R.string.Chats));
                    } else if (position == mediaHeaderRow) {
                        headerCell.setText(LocaleController.getString("Media", R.string.Media));
                    }
                    break;
                case 3:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == hideStickerTimeRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("StickerTime", R.string.StickerTime), MoeConfig.hideStickerTime, true);
                    } else if (position == hideGroupStickersRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideGroupStickers", R.string.HideGroupStickers), MoeConfig.hideGroupStickers, true);
                    } else if (position == disableGreetingStickerRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableGreetingSticker", R.string.DisableGreetingSticker), MoeConfig.disableGreetingSticker, true);
                    } else if (position == unlimitedRecentStickersRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UnlimitedRecentStickers", R.string.UnlimitedRecentStickers), MoeConfig.unlimitedRecentStickers, true);
                    } else if (position == hideSendAsChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideSendAsChannel", R.string.HideSendAsChannel), MoeConfig.hideSendAsChannel, true);
                    } else if (position == hideKeyboardOnScrollRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideKeyboardOnScroll", R.string.HideKeyboardOnScroll), MoeConfig.hideKeyboardOnScroll, true);
                    } else if (position == disableReactionsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableReactions", R.string.DisableReactions), MoeConfig.disableReactions, true);
                    } else if (position == disableJumpToNextChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableJumpToNextChannel", R.string.DisableJumpToNextChannel), MoeConfig.disableJumpToNextChannel, true);
                    } else if (position == archiveOnPullRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ArchiveOnPull", R.string.ArchiveOnPull), MoeConfig.archiveOnPull, true);
                    } else if (position == dateOfForwardedMsgRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DateOfForwardedMsg", R.string.DateOfForwardedMsg), MoeConfig.dateOfForwardedMsg, true);
                    } else if (position == showMessageIDRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowMessageID", R.string.ShowMessageID), MoeConfig.showMessageID, false);
                    } else if (position == rearVideoMessagesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("RearVideoMessages", R.string.RearVideoMessages), MoeConfig.rearVideoMessages, true);
                    } else if (position == disableProximityEventsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableProximityEvents", R.string.DisableProximityEvents), MoeConfig.disableProximityEvents, true);
                    } else if (position == pauseOnMinimizeRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("PauseOnMinimize", R.string.PauseOnMinimize), LocaleController.getString("POMDescription", R.string.POMDescription), MoeConfig.pauseOnMinimize, true, true);
                    } else if (position == disablePlaybackRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("DisablePlayback", R.string.DisablePlayback), LocaleController.getString("DPDescription", R.string.DPDescription), MoeConfig.disablePlayback, true, true);
                    } else if (position == disableSideActionsRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("DisableSideActions", R.string.DisableSideActions), LocaleController.getString("DisableSideActionsDesc", R.string.DisableSideActionsDesc), MoeConfig.disableSideActions, true, false);
                    } else if (position == disableCameraRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableCamera", R.string.DisableCamera), MoeConfig.disableCamera, true);
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
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = stickerSizeCell = new StickerSizeCell(mContext);
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
            if (position == stickerSizeDividerRow || position == stickersDividerRow || position == chatDividerRow || position == mediaDividerRow) {
                return 1;
            } else if (position == stickerSizeHeaderRow || position == stickersHeaderRow || position == chatHeaderRow || position == mediaHeaderRow) {
                return 2;
            } else if (position == hideStickerTimeRow || position == hideGroupStickersRow || position == disableGreetingStickerRow ||
                    position == unlimitedRecentStickersRow || position == hideSendAsChannelRow || position == hideKeyboardOnScrollRow ||
                    position == disableReactionsRow || position == disableJumpToNextChannelRow || position == archiveOnPullRow ||
                    position == dateOfForwardedMsgRow || position == showMessageIDRow || position == rearVideoMessagesRow ||
                    position == disableProximityEventsRow || position == pauseOnMinimizeRow || position == disablePlaybackRow ||
                    position == disableSideActionsRow || position == disableCameraRow) {
                return 3;
            } else if (position == stickerSizeRow) {
                return 4;
            }
            return 1;
        }
    }
}

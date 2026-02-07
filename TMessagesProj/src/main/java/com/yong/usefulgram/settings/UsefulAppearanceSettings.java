package com.yong.usefulgram.settings;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;

import com.yong.usefulgram.UsefulConfig;
import com.yong.usefulgram.helpers.EmojiHelper;
import com.yong.usefulgram.helpers.PopupHelper;

public class UsefulAppearanceSettings extends BaseUsefulSettingsActivity implements NotificationCenter.NotificationCenterDelegate {

    private DrawerProfilePreviewCell profilePreviewCell;

    private int drawerRow;
    private int avatarAsDrawerBackgroundRow;
    private int avatarBackgroundBlurRow;
    private int avatarBackgroundDarkenRow;
    private int hidePhoneRow;
    private int drawer2Row;

    private int appearanceRow;
    private int emojiSetsRow;
    private int mediaPreviewRow;
    private int predictiveBackAnimationRow;
    private int appBarShadowRow;
    private int formatTimeWithSecondsRow;
    private int disableNumberRoundingRow;
    private int tabletModeRow;
    private int eventTypeRow;
    private int appearance2Row;

    private int foldersRow;
    private int hideAllTabRow;
    private int tabsTitleTypeRow;
    private int folders2Row;

    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        super.onFragmentDestroy();
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == hidePhoneRow) {
            UsefulConfig.toggleHidePhone();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.hidePhone);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
        } else if (position == tabletModeRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString(R.string.TabletModeAuto));
            types.add(UsefulConfig.TABLET_AUTO);
            arrayList.add(LocaleController.getString(R.string.Enable));
            types.add(UsefulConfig.TABLET_ENABLE);
            arrayList.add(LocaleController.getString(R.string.Disable));
            types.add(UsefulConfig.TABLET_DISABLE);
            PopupHelper.show(arrayList, LocaleController.getString(R.string.TabletMode), types.indexOf(UsefulConfig.tabletMode), getParentActivity(), view, i -> {
                UsefulConfig.setTabletMode(types.get(i));
                listAdapter.notifyItemChanged(tabletModeRow, PARTIAL);
                AndroidUtilities.resetTabletFlag();
                if (getParentActivity() instanceof LaunchActivity) {
                    ((LaunchActivity) getParentActivity()).invalidateTabletMode();
                }
            }, resourcesProvider);
        } else if (position == emojiSetsRow) {
            presentFragment(new UsefulEmojiSettingsActivity());
        } else if (position == eventTypeRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(LocaleController.getString(R.string.DependsOnDate));
            arrayList.add(LocaleController.getString(R.string.Christmas));
            arrayList.add(LocaleController.getString(R.string.Valentine));
            arrayList.add(LocaleController.getString(R.string.Halloween));
            PopupHelper.show(arrayList, LocaleController.getString(R.string.EventType), UsefulConfig.eventType, getParentActivity(), view, i -> {
                UsefulConfig.setEventType(i);
                listAdapter.notifyItemChanged(eventTypeRow, PARTIAL);
                getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
            }, resourcesProvider);
        } else if (position == avatarAsDrawerBackgroundRow) {
            UsefulConfig.toggleAvatarAsDrawerBackground();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.avatarAsDrawerBackground);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
            TransitionManager.beginDelayedTransition(profilePreviewCell);
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
            if (UsefulConfig.avatarAsDrawerBackground) {
                updateRows();
                listAdapter.notifyItemRangeInserted(avatarBackgroundBlurRow, 2);
            } else {
                listAdapter.notifyItemRangeRemoved(avatarBackgroundBlurRow, 2);
                updateRows();
            }
        } else if (position == avatarBackgroundBlurRow) {
            UsefulConfig.toggleAvatarBackgroundBlur();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.avatarBackgroundBlur);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
        } else if (position == avatarBackgroundDarkenRow) {
            UsefulConfig.toggleAvatarBackgroundDarken();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.avatarBackgroundDarken);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
        } else if (position == disableNumberRoundingRow) {
            UsefulConfig.toggleDisableNumberRounding();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.disableNumberRounding);
            }
        } else if (position == appBarShadowRow) {
            UsefulConfig.toggleDisableAppBarShadow();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.disableAppBarShadow);
            }
            parentLayout.setHeaderShadow(UsefulConfig.disableAppBarShadow ? null : parentLayout.getParentActivity().getDrawable(R.drawable.header_shadow).mutate());
            parentLayout.rebuildAllFragmentViews(false, false);
        } else if (position == mediaPreviewRow) {
            UsefulConfig.toggleMediaPreview();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.mediaPreview);
            }
        } else if (position == formatTimeWithSecondsRow) {
            UsefulConfig.toggleFormatTimeWithSeconds();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.formatTimeWithSeconds);
            }
            parentLayout.rebuildAllFragmentViews(false, false);
        } else if (position == hideAllTabRow) {
            UsefulConfig.toggleHideAllTab();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.hideAllTab);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
            getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
        } else if (position == tabsTitleTypeRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString(R.string.TabTitleTypeText));
            types.add(UsefulConfig.TITLE_TYPE_TEXT);
            arrayList.add(LocaleController.getString(R.string.TabTitleTypeIcon));
            types.add(UsefulConfig.TITLE_TYPE_ICON);
            arrayList.add(LocaleController.getString(R.string.TabTitleTypeMix));
            types.add(UsefulConfig.TITLE_TYPE_MIX);
            PopupHelper.show(arrayList, LocaleController.getString(R.string.TabTitleType), types.indexOf(UsefulConfig.tabsTitleType), getParentActivity(), view, i -> {
                UsefulConfig.setTabsTitleType(types.get(i));
                listAdapter.notifyItemChanged(tabsTitleTypeRow, PARTIAL);
                getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
            }, resourcesProvider);
        } else if (position == predictiveBackAnimationRow) {
            UsefulConfig.togglePredictiveBackAnimation();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.predictiveBackAnimation);
            }
            showRestartBulletin();
        }
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString(R.string.ChangeChannelNameColor2);
    }

    @Override
    protected String getKey() {
        return "a";
    }

    @Override
    protected void updateRows() {
        super.updateRows();

        drawerRow = addRow("drawer");
        avatarAsDrawerBackgroundRow = addRow("avatarAsDrawerBackground");
        if (UsefulConfig.avatarAsDrawerBackground) {
            avatarBackgroundBlurRow = addRow("avatarBackgroundBlur");
            avatarBackgroundDarkenRow = addRow("avatarBackgroundDarken");
        } else {
            avatarBackgroundBlurRow = -1;
            avatarBackgroundDarkenRow = -1;
        }
        hidePhoneRow = addRow("hidePhone");
        drawer2Row = addRow();

        appearanceRow = addRow("appearance");
        emojiSetsRow = addRow("emojiSets");
        mediaPreviewRow = addRow("mediaPreview");
        predictiveBackAnimationRow = addRow("predictiveBackAnimation");
        appBarShadowRow = addRow("appBarShadow");
        formatTimeWithSecondsRow = addRow("formatTimeWithSeconds");
        disableNumberRoundingRow = addRow("disableNumberRounding");
        eventTypeRow = addRow("eventType");
        tabletModeRow = addRow("tabletMode");
        appearance2Row = addRow();

        foldersRow = addRow("folders");
        hideAllTabRow = addRow("hideAllTab");
        tabsTitleTypeRow = addRow("tabsTitleType");
        folders2Row = addRow();
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.emojiLoaded && listAdapter != null) {
            listAdapter.notifyItemChanged(emojiSetsRow, PARTIAL);
        }
    }

    private class ListAdapter extends BaseListAdapter {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean partial, boolean divider) {
            switch (holder.getItemViewType()) {
                case TYPE_SETTINGS: {
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    if (position == eventTypeRow) {
                        String value = switch (UsefulConfig.eventType) {
                            case 1 -> LocaleController.getString(R.string.Christmas);
                            case 2 -> LocaleController.getString(R.string.Valentine);
                            case 3 -> LocaleController.getString(R.string.Halloween);
                            default -> LocaleController.getString(R.string.DependsOnDate);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.EventType), value, partial, divider);
                    } else if (position == tabsTitleTypeRow) {
                        String value = switch (UsefulConfig.tabsTitleType) {
                            case UsefulConfig.TITLE_TYPE_TEXT ->
                                    LocaleController.getString(R.string.TabTitleTypeText);
                            case UsefulConfig.TITLE_TYPE_ICON ->
                                    LocaleController.getString(R.string.TabTitleTypeIcon);
                            default -> LocaleController.getString(R.string.TabTitleTypeMix);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.TabTitleType), value, partial, divider);
                    } else if (position == tabletModeRow) {
                        String value = switch (UsefulConfig.tabletMode) {
                            case UsefulConfig.TABLET_AUTO ->
                                    LocaleController.getString(R.string.TabletModeAuto);
                            case UsefulConfig.TABLET_ENABLE ->
                                    LocaleController.getString(R.string.Enable);
                            default -> LocaleController.getString(R.string.Disable);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.TabletMode), value, partial, divider);
                    }
                    break;
                }
                case TYPE_CHECK: {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    textCell.setEnabled(true, null);
                    if (position == hidePhoneRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.HidePhone), UsefulConfig.hidePhone, divider);
                    } else if (position == avatarAsDrawerBackgroundRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.AvatarAsBackground), UsefulConfig.avatarAsDrawerBackground, divider);
                    } else if (position == disableNumberRoundingRow) {
                        textCell.setTextAndValueAndCheck(LocaleController.getString(R.string.DisableNumberRounding), "4.8K -> 4777", UsefulConfig.disableNumberRounding, divider, divider);
                    } else if (position == appBarShadowRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.DisableAppBarShadow), UsefulConfig.disableAppBarShadow, divider);
                    } else if (position == mediaPreviewRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.MediaPreview), UsefulConfig.mediaPreview, divider);
                    } else if (position == formatTimeWithSecondsRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.FormatWithSeconds), UsefulConfig.formatTimeWithSeconds, divider);
                    } else if (position == avatarBackgroundBlurRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.BlurAvatarBackground), UsefulConfig.avatarBackgroundBlur, divider);
                    } else if (position == avatarBackgroundDarkenRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.DarkenAvatarBackground), UsefulConfig.avatarBackgroundDarken, divider);
                    } else if (position == hideAllTabRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.HideAllTab), UsefulConfig.hideAllTab, divider);
                    } else if (position == predictiveBackAnimationRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.PredictiveBackAnimation), UsefulConfig.predictiveBackAnimation, divider);
                    }
                    break;
                }
                case TYPE_HEADER: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == appearanceRow) {
                        headerCell.setText(LocaleController.getString(R.string.ChangeChannelNameColor2));
                    } else if (position == foldersRow) {
                        headerCell.setText(LocaleController.getString(R.string.Filters));
                    }
                    break;
                }
                case TYPE_INFO_PRIVACY: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == folders2Row) {
                        cell.setText(LocaleController.getString(R.string.TabTitleTypeTip));
                    }
                    break;
                }
                case TYPE_EMOJI: {
                    EmojiSetCell emojiPackSetCell = (EmojiSetCell) holder.itemView;
                    if (position == emojiSetsRow) {
                        emojiPackSetCell.setData(EmojiHelper.getInstance().getCurrentEmojiPackInfo(), partial, divider);
                    }
                    break;
                }
                case Integer.MAX_VALUE: {
                    DrawerProfilePreviewCell cell = (DrawerProfilePreviewCell) holder.itemView;
                    cell.setUser(getUserConfig().getCurrentUser(), false);
                    break;
                }
            }
        }

        @Override
        public View createCustomView(int viewType) {
            if (viewType == Integer.MAX_VALUE) {
                return profilePreviewCell = new DrawerProfilePreviewCell(mContext);
            } else {
                return super.createCustomView(viewType);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == appearance2Row || position == drawer2Row) {
                return TYPE_SHADOW;
            } else if (position == eventTypeRow || position == tabsTitleTypeRow || position == tabletModeRow) {
                return TYPE_SETTINGS;
            } else if (position == hideAllTabRow ||
                    (position > emojiSetsRow && position <= disableNumberRoundingRow) ||
                    (position > drawerRow && position < drawer2Row)) {
                return TYPE_CHECK;
            } else if (position == appearanceRow || position == foldersRow) {
                return TYPE_HEADER;
            } else if (position == folders2Row) {
                return TYPE_INFO_PRIVACY;
            } else if (position == emojiSetsRow) {
                return TYPE_EMOJI;
            } else if (position == drawerRow) {
                return Integer.MAX_VALUE;
            }
            return TYPE_SETTINGS;
        }
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate cellDelegate = () -> {
            if (listView != null) {
                for (int i = 0; i < listView.getChildCount(); i++) {
                    View child = listView.getChildAt(i);
                    if (child instanceof DrawerProfileCell profileCell) {
                        profileCell.applyBackground(true);
                        profileCell.updateColors();
                    }
                }
            }
        };
        ArrayList<ThemeDescription> themeDescriptions = super.getThemeDescriptions();
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuName));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuPhone));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuPhoneCats));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chat_serviceBackground));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuTopShadow));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuTopShadowCats));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerProfileCell.class}, new String[]{"darkThemeView"}, null, null, null, Theme.key_chats_menuName));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{DrawerProfileCell.class}, null, null, cellDelegate, Theme.key_chats_menuTopBackgroundCats));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{DrawerProfileCell.class}, null, null, cellDelegate, Theme.key_chats_menuTopBackground));
        return themeDescriptions;
    }
}

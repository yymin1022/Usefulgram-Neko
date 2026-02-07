package com.yong.usefulgram.settings;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;

import java.util.ArrayList;
import java.util.Locale;

import app.nekogram.translator.DeepLTranslator;
import com.yong.usefulgram.UsefulConfig;
import com.yong.usefulgram.helpers.PopupHelper;
import com.yong.usefulgram.translator.Translator;
import com.yong.usefulgram.translator.TranslatorApps;

public class UsefulGeneralSettingsActivity extends BaseUsefulSettingsActivity {

    private int connectionRow;
    private int ipv6Row;
    private int connection2Row;

    private int translatorRow;
    private int showOriginalRow;
    private int translatorTypeRow;
    private int translatorExternalAppRow;
    private int deepLFormalityRow;
    private int translationProviderRow;
    private int translationTargetRow;
    private int doNotTranslateRow;
    private int autoTranslateRow;
    private int translator2Row;

    private int notificationRow;
    private int accentAsNotificationColorRow;
    private int silenceNonContactsRow;
    private int notification2Row;

    private int generalRow;
    private int hideStoriesRow;
    private int disabledInstantCameraRow;
    private int askBeforeCallRow;
    private int openArchiveOnPullRow;
    private int nameOrderRow;
    private int idTypeRow;
    private int general2Row;

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == ipv6Row) {
            UsefulConfig.toggleIPv6();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.preferIPv6);
            }
            for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
                if (UserConfig.getInstance(a).isClientActivated()) {
                    ConnectionsManager.getInstance(a).checkConnection();
                }
            }
        } else if (position == disabledInstantCameraRow) {
            UsefulConfig.toggleDisabledInstantCamera();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.disableInstantCamera);
            }
        } else if (position == nameOrderRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString(R.string.FirstLast));
            types.add(1);
            arrayList.add(LocaleController.getString(R.string.LastFirst));
            types.add(2);
            PopupHelper.show(arrayList, LocaleController.getString(R.string.NameOrder), types.indexOf(UsefulConfig.nameOrder), getParentActivity(), view, i -> {
                UsefulConfig.setNameOrder(types.get(i));
                listAdapter.notifyItemChanged(nameOrderRow, PARTIAL);
                parentLayout.rebuildAllFragmentViews(false, false);
            }, resourcesProvider);
        } else if (position == translationProviderRow) {
            final String oldProvider = UsefulConfig.translationProvider;
            Translator.showTranslationProviderSelector(getParentActivity(), view, param -> {
                if (param) {
                    listAdapter.notifyItemChanged(translationProviderRow, PARTIAL);
                } else {
                    listAdapter.notifyItemChanged(translationProviderRow, PARTIAL);
                    listAdapter.notifyItemChanged(translationTargetRow, PARTIAL);
                }
                if (!oldProvider.equals(UsefulConfig.translationProvider)) {
                    if (oldProvider.equals(Translator.PROVIDER_DEEPL)) {
                        listAdapter.notifyItemRemoved(deepLFormalityRow);
                        updateRows();
                    } else if (UsefulConfig.translationProvider.equals(Translator.PROVIDER_DEEPL)) {
                        updateRows();
                        listAdapter.notifyItemInserted(deepLFormalityRow);
                    }
                }
            }, resourcesProvider);
        } else if (position == translationTargetRow) {
            Translator.showTranslationTargetSelector(this, view, () -> {
                listAdapter.notifyItemChanged(translationTargetRow, PARTIAL);
                if (Translator.getRestrictedLanguages().size() == 1) {
                    listAdapter.notifyItemChanged(doNotTranslateRow, PARTIAL);
                }
            }, true, resourcesProvider);
        } else if (position == deepLFormalityRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString(R.string.DeepLFormalityDefault));
            types.add(DeepLTranslator.FORMALITY_DEFAULT);
            arrayList.add(LocaleController.getString(R.string.DeepLFormalityMore));
            types.add(DeepLTranslator.FORMALITY_MORE);
            arrayList.add(LocaleController.getString(R.string.DeepLFormalityLess));
            types.add(DeepLTranslator.FORMALITY_LESS);
            PopupHelper.show(arrayList, LocaleController.getString(R.string.DeepLFormality), types.indexOf(UsefulConfig.deepLFormality), getParentActivity(), view, i -> {
                UsefulConfig.setDeepLFormality(types.get(i));
                listAdapter.notifyItemChanged(deepLFormalityRow, PARTIAL);
            }, resourcesProvider);
        } else if (position == openArchiveOnPullRow) {
            UsefulConfig.toggleOpenArchiveOnPull();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.openArchiveOnPull);
            }
        } else if (position == askBeforeCallRow) {
            UsefulConfig.toggleAskBeforeCall();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.askBeforeCall);
            }
        } else if (position == idTypeRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString(R.string.IdTypeHidden));
            types.add(UsefulConfig.ID_TYPE_HIDDEN);
            arrayList.add(LocaleController.getString(R.string.IdTypeAPI));
            types.add(UsefulConfig.ID_TYPE_API);
            arrayList.add(LocaleController.getString(R.string.IdTypeBOTAPI));
            types.add(UsefulConfig.ID_TYPE_BOTAPI);
            PopupHelper.show(arrayList, LocaleController.getString(R.string.IdType), types.indexOf(UsefulConfig.idType), getParentActivity(), view, i -> {
                UsefulConfig.setIdType(types.get(i));
                listAdapter.notifyItemChanged(idTypeRow, PARTIAL);
                parentLayout.rebuildAllFragmentViews(false, false);
            }, resourcesProvider);
        } else if (position == accentAsNotificationColorRow) {
            UsefulConfig.toggleAccentAsNotificationColor();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.accentAsNotificationColor);
            }
        } else if (position == silenceNonContactsRow) {
            UsefulConfig.toggleSilenceNonContacts();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.silenceNonContacts);
            }
        } else if (position == translatorTypeRow) {
            int oldType = UsefulConfig.transType;
            Translator.showTranslatorTypeSelector(getParentActivity(), view, () -> {
                int newType = UsefulConfig.transType;
                listAdapter.notifyItemChanged(translatorTypeRow, PARTIAL);
                if (oldType != newType) {
                    int count = 4;
                    if (UsefulConfig.translationProvider.equals(Translator.PROVIDER_DEEPL)) {
                        count++;
                    }
                    if (oldType == UsefulConfig.TRANS_TYPE_NEKO || newType == UsefulConfig.TRANS_TYPE_NEKO) {
                        count++;
                    }
                    if (oldType == UsefulConfig.TRANS_TYPE_EXTERNAL) {
                        listAdapter.notifyItemRemoved(translatorExternalAppRow);
                        updateRows();
                        listAdapter.notifyItemRangeInserted(translationProviderRow, count);
                    } else if (newType == UsefulConfig.TRANS_TYPE_EXTERNAL) {
                        listAdapter.notifyItemRangeRemoved(translationProviderRow, count);
                        updateRows();
                        listAdapter.notifyItemInserted(translatorExternalAppRow);
                    } else if (oldType == UsefulConfig.TRANS_TYPE_NEKO) {
                        listAdapter.notifyItemRemoved(showOriginalRow);
                        updateRows();
                    } else if (newType == UsefulConfig.TRANS_TYPE_NEKO) {
                        updateRows();
                        listAdapter.notifyItemInserted(showOriginalRow);
                    }
                }
            }, resourcesProvider);
        } else if (position == doNotTranslateRow) {
            presentFragment(new UsefulLanguagesSelectActivity(UsefulLanguagesSelectActivity.TYPE_RESTRICTED, true));
        } else if (position == autoTranslateRow) {
            UsefulConfig.toggleAutoTranslate();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.autoTranslate);
            }
        } else if (position == showOriginalRow) {
            UsefulConfig.toggleShowOriginal();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.showOriginal);
            }
        } else if (position == hideStoriesRow) {
            UsefulConfig.toggleHideStories();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(UsefulConfig.hideStories);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.storiesEnabledUpdate);
        } else if (position == translatorExternalAppRow) {
            Translator.showTranslationProviderSelector(getParentActivity(), view, param -> {
                listAdapter.notifyItemChanged(translatorExternalAppRow, PARTIAL);
            }, resourcesProvider);
        }
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString(R.string.General);
    }

    @Override
    protected void updateRows() {
        super.updateRows();

        connectionRow = addRow("connection");
        ipv6Row = addRow("ipv6");
        connection2Row = addRow();

        translatorRow = addRow("translator");
        translatorTypeRow = addRow("translatorType");
        if (UsefulConfig.transType != UsefulConfig.TRANS_TYPE_EXTERNAL) {
            translatorExternalAppRow = -1;
            showOriginalRow = UsefulConfig.transType == UsefulConfig.TRANS_TYPE_NEKO ? addRow("showOriginalRow") : -1;
            translationProviderRow = addRow("translationProvider");
            deepLFormalityRow = UsefulConfig.translationProvider.equals(Translator.PROVIDER_DEEPL) ? addRow("deepLFormality") : -1;
            translationTargetRow = addRow("translationTarget");
            doNotTranslateRow = addRow("doNotTranslate");
            autoTranslateRow = addRow("autoTranslate");
        } else {
            translatorExternalAppRow = addRow("translatorExternalApp");
            showOriginalRow = -1;
            translationProviderRow = -1;
            deepLFormalityRow = -1;
            translationTargetRow = -1;
            doNotTranslateRow = -1;
            autoTranslateRow = -1;
        }
        translator2Row = addRow();

        notificationRow = addRow("notification");
        accentAsNotificationColorRow = addRow("accentAsNotificationColor");
        silenceNonContactsRow = addRow("silenceNonContacts");
        notification2Row = addRow();

        generalRow = addRow("general");
        hideStoriesRow = addRow("hideStories");
        disabledInstantCameraRow = addRow("disabledInstantCamera");
        askBeforeCallRow = addRow("askBeforeCall");
        openArchiveOnPullRow = addRow("openArchiveOnPull");
        nameOrderRow = addRow("nameOrder");
        idTypeRow = addRow("idType");
        general2Row = addRow();
    }

    @Override
    protected String getKey() {
        return "g";
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
                    textCell.setCanDisable(true);
                    if (position == nameOrderRow) {
                        String value = switch (UsefulConfig.nameOrder) {
                            case 2 -> LocaleController.getString(R.string.LastFirst);
                            default -> LocaleController.getString(R.string.FirstLast);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.NameOrder), value, partial, divider);
                    } else if (position == translationProviderRow) {
                        Pair<ArrayList<String>, ArrayList<String>> providers = Translator.getProviders();
                        ArrayList<String> names = providers.first;
                        ArrayList<String> types = providers.second;
                        if (names == null || types == null) {
                            return;
                        }
                        int index = types.indexOf(UsefulConfig.translationProvider);
                        if (index < 0) {
                            textCell.setTextAndValue(LocaleController.getString(R.string.TranslationProviderShort), "", partial, divider);
                        } else {
                            String value = names.get(index);
                            textCell.setTextAndValue(LocaleController.getString(R.string.TranslationProviderShort), value, partial, divider);
                        }
                    } else if (position == translationTargetRow) {
                        String language = UsefulConfig.translationTarget;
                        CharSequence value;
                        if (language.equals("app")) {
                            value = LocaleController.getString(R.string.TranslationTargetApp);
                        } else {
                            Locale locale = Locale.forLanguageTag(language);
                            if (!TextUtils.isEmpty(locale.getScript())) {
                                value = HtmlCompat.fromHtml(locale.getDisplayScript(), HtmlCompat.FROM_HTML_MODE_LEGACY);
                            } else {
                                value = locale.getDisplayName();
                            }
                        }
                        textCell.setTextAndValue(LocaleController.getString(R.string.TranslationTarget), value, partial, divider);
                    } else if (position == deepLFormalityRow) {
                        String value = switch (UsefulConfig.deepLFormality) {
                            case DeepLTranslator.FORMALITY_DEFAULT ->
                                    LocaleController.getString(R.string.DeepLFormalityDefault);
                            case DeepLTranslator.FORMALITY_MORE ->
                                    LocaleController.getString(R.string.DeepLFormalityMore);
                            default -> LocaleController.getString(R.string.DeepLFormalityLess);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.DeepLFormality), value, partial, divider);
                    } else if (position == idTypeRow) {
                        String value = switch (UsefulConfig.idType) {
                            case UsefulConfig.ID_TYPE_HIDDEN ->
                                    LocaleController.getString(R.string.IdTypeHidden);
                            case UsefulConfig.ID_TYPE_BOTAPI ->
                                    LocaleController.getString(R.string.IdTypeBOTAPI);
                            default -> LocaleController.getString(R.string.IdTypeAPI);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.IdType), value, partial, divider);
                    } else if (position == translatorTypeRow) {
                        String value = switch (UsefulConfig.transType) {
                            case UsefulConfig.TRANS_TYPE_TG ->
                                    LocaleController.getString(R.string.TranslatorTypeTG);
                            case UsefulConfig.TRANS_TYPE_EXTERNAL ->
                                    LocaleController.getString(R.string.TranslatorTypeExternal);
                            default -> LocaleController.getString(R.string.TranslatorTypeUseful);
                        };
                        textCell.setTextAndValue(LocaleController.getString(R.string.TranslatorType), value, partial, divider);
                    } else if (position == doNotTranslateRow) {
                        var langCodes = Translator.getRestrictedLanguages();
                        CharSequence value;
                        if (langCodes.size() == 1) {
                            Locale locale = Locale.forLanguageTag(langCodes.get(0));
                            if (!TextUtils.isEmpty(locale.getScript())) {
                                value = HtmlCompat.fromHtml(locale.getDisplayScript(), HtmlCompat.FROM_HTML_MODE_LEGACY);
                            } else {
                                value = locale.getDisplayName();
                            }
                        } else {
                            value = LocaleController.formatPluralString("Languages", langCodes.size());
                        }
                        textCell.setTextAndValue(LocaleController.getString(R.string.DoNotTranslate), value, partial, divider);
                    } else if (position == translatorExternalAppRow) {
                        var app = TranslatorApps.getTranslatorApp();
                        if (app != null) {
                            textCell.setTextAndValue(LocaleController.getString(R.string.TranslationProviderShort), app.title, partial, divider);
                        } else {
                            textCell.setTextAndValue(LocaleController.getString(R.string.TranslationProviderShort), "", partial, divider);
                        }
                    }
                    break;
                }
                case TYPE_CHECK: {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    textCell.setEnabled(true, null);
                    if (position == ipv6Row) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.PreferIPv6), UsefulConfig.preferIPv6, divider);
                    } else if (position == disabledInstantCameraRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.DisableInstantCamera), UsefulConfig.disableInstantCamera, divider);
                    } else if (position == openArchiveOnPullRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.OpenArchiveOnPull), UsefulConfig.openArchiveOnPull, divider);
                    } else if (position == askBeforeCallRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.AskBeforeCalling), UsefulConfig.askBeforeCall, divider);
                    } else if (position == accentAsNotificationColorRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.AccentAsNotificationColor), UsefulConfig.accentAsNotificationColor, divider);
                    } else if (position == silenceNonContactsRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.SilenceNonContacts), UsefulConfig.silenceNonContacts, divider);
                    } else if (position == autoTranslateRow) {
                        textCell.setTextAndValueAndCheck(LocaleController.getString(R.string.AutoTranslate), LocaleController.getString(R.string.AutoTranslateAbout), UsefulConfig.autoTranslate, true, divider);
                    } else if (position == showOriginalRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.TranslatorShowOriginal), UsefulConfig.showOriginal, divider);
                    } else if (position == hideStoriesRow) {
                        textCell.setTextAndCheck(LocaleController.getString(R.string.HideStories), UsefulConfig.hideStories, divider);
                    }
                    break;
                }
                case TYPE_HEADER: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == generalRow) {
                        headerCell.setText(LocaleController.getString(R.string.General));
                    } else if (position == connectionRow) {
                        headerCell.setText(LocaleController.getString(R.string.Connection));
                    } else if (position == notificationRow) {
                        headerCell.setText(LocaleController.getString(R.string.Notifications));
                    } else if (position == translatorRow) {
                        headerCell.setText(LocaleController.getString(R.string.Translator));
                    }
                    break;
                }
                case TYPE_INFO_PRIVACY: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == general2Row) {
                        cell.setText(LocaleController.getString(R.string.IdTypeAbout));
                    } else if (position == notification2Row) {
                        cell.setText(LocaleController.getString(R.string.SilenceNonContactsAbout));
                    } else if (position == translator2Row) {
                        cell.setText(LocaleController.getString(R.string.TranslateMessagesInfo1));
                    }
                    break;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == connection2Row) {
                return TYPE_SHADOW;
            } else if (position == nameOrderRow || position == idTypeRow || position == translatorTypeRow ||
                    position == translatorExternalAppRow ||
                    (position >= translationProviderRow && position <= doNotTranslateRow)) {
                return TYPE_SETTINGS;
            } else if (position == ipv6Row || position == autoTranslateRow ||
                    (position > generalRow && position < nameOrderRow) ||
                    (position > notificationRow && position < notification2Row) ||
                    position == showOriginalRow) {
                return TYPE_CHECK;
            } else if (position == generalRow || position == connectionRow || position == notificationRow ||
                    position == translatorRow) {
                return TYPE_HEADER;
            } else if (position == general2Row || position == notification2Row || position == translator2Row) {
                return TYPE_INFO_PRIVACY;
            }
            return TYPE_SETTINGS;
        }
    }
}

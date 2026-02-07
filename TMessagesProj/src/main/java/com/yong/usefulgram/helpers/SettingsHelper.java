package com.yong.usefulgram.helpers;

import android.net.Uri;
import android.text.TextUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;

import java.util.Locale;

import com.yong.usefulgram.settings.BaseUsefulSettingsActivity;
import com.yong.usefulgram.settings.UsefulAppearanceSettings;
import com.yong.usefulgram.settings.UsefulChatSettingsActivity;
import com.yong.usefulgram.settings.UsefulDonateActivity;
import com.yong.usefulgram.settings.UsefulEmojiSettingsActivity;
import com.yong.usefulgram.settings.UsefulExperimentalSettingsActivity;
import com.yong.usefulgram.settings.UsefulGeneralSettingsActivity;
import com.yong.usefulgram.settings.UsefulPasscodeSettingsActivity;
import com.yong.usefulgram.settings.UsefulSettingsActivity;

public class SettingsHelper {

    public static void processDeepLink(Uri uri, Callback callback, Runnable unknown, Browser.Progress progress) {
        if (uri == null) {
            unknown.run();
            return;
        }
        var segments = uri.getPathSegments();
        if (segments.isEmpty() || segments.size() > 2) {
            unknown.run();
            return;
        }
        BaseUsefulSettingsActivity fragment;
        if (segments.size() == 1) {
            var segment = segments.get(0).toLowerCase(Locale.US);
            switch (segment) {
                case "neko", "nekosettings" -> fragment = new UsefulSettingsActivity();
                case "update", "upgrade" -> {
                    LaunchActivity.instance.checkAppUpdate(true, progress);
                    return;
                }
                case "nya", "meow" -> {
                    LaunchActivity.instance.showBulletin(factory -> factory.createErrorBulletin(LocaleController.getString(R.string.Nya)));
                    return;
                }
                default -> {
                    unknown.run();
                    return;
                }
            }
        } else {
            var segment = segments.get(1);
            if (PasscodeHelper.getSettingsKey().equals(segment)) {
                fragment = new UsefulPasscodeSettingsActivity();
            } else {
                switch (segment.toLowerCase(Locale.US)) {
                    case "appearance":
                    case "a":
                        fragment = new UsefulAppearanceSettings();
                        break;
                    case "chat":
                    case "chats":
                    case "c":
                        fragment = new UsefulChatSettingsActivity();
                        break;
                    case "donate":
                    case "d":
                        fragment = new UsefulDonateActivity();
                        break;
                    case "experimental":
                    case "e":
                        fragment = new UsefulExperimentalSettingsActivity();
                        break;
                    case "emoji":
                        fragment = new UsefulEmojiSettingsActivity();
                        break;
                    case "general":
                    case "g":
                        fragment = new UsefulGeneralSettingsActivity();
                        break;
                    case "reportid":
                        SettingsHelper.copyReportId();
                        return;
                    case "update":
                        LaunchActivity.instance.checkAppUpdate(true, progress);
                        return;
                    default:
                        unknown.run();
                        return;
                }
            }
        }
        callback.presentFragment(fragment);
        var row = uri.getQueryParameter("r");
        if (TextUtils.isEmpty(row)) {
            row = uri.getQueryParameter("row");
        }
        if (!TextUtils.isEmpty(row)) {
            var rowFinal = row;
            AndroidUtilities.runOnUIThread(() -> fragment.scrollToRow(rowFinal, unknown));
        }
    }

    public static void copyReportId() {
        AndroidUtilities.addToClipboard(AnalyticsHelper.userId);
        BulletinFactory.global().createSimpleBulletin(R.raw.copy, LocaleController.getString(R.string.TextCopied), LocaleController.getString(R.string.CopyReportIdDescription)).show();
    }

    public interface Callback {
        void presentFragment(BaseFragment fragment);
    }
}

package com.tencent.shadow.core.manifest_parser;

import java.lang.String;

public class PluginManifest implements com.tencent.shadow.core.runtime.PluginManifest {
  private static final String applicationPackageName = "app.paradox.youtubeintegrated";

  private static final String applicationClassName = "com.google.android.apps.youtube.music.application.YouTubeMusicTikTokRoot_Application";

  private static final String appComponentFactory = "androidx.core.app.CoreComponentFactory";

  private static final int applicationTheme = 0x801506b2;

  private static final String[] usesPermissions = new String[] {"android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_WIFI_STATE", "android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT", "android.permission.INTERNET", "android.permission.NFC", "android.permission.READ_MEDIA_IMAGES", "android.permission.CAMERA", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "app.revanced.android.gms.permission.AD_ID", "android.permission.VIBRATE", "android.permission.WAKE_LOCK", "app.revanced.android.c2dm.permission.RECEIVE", "app.revanced.android.providers.gsf.permission.READ_GSERVICES", "android.permission.GET_ACCOUNTS", "android.permission.MANAGE_ACCOUNTS", "android.permission.USE_CREDENTIALS", "com.google.android.gms.permission.ACTIVITY_RECOGNITION", "android.permission.FOREGROUND_SERVICE", "android.permission.FOREGROUND_SERVICE_DATA_SYNC", "android.permission.RUN_USER_INITIATED_JOBS", "android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK", "android.permission.POST_NOTIFICATIONS", "app.paradox.youtubeintegrated.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION", "android.permission.RECEIVE_BOOT_COMPLETED", "com.android.vending.BILLING", "android.permission.REQUEST_COMPANION_START_FOREGROUND_SERVICES_FROM_BACKGROUND", "android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.QUERY_ALL_PACKAGES"};

  private static final com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo[] activities = new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo[] {
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.activities.MusicActivity", 0x801506bc ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD|android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN|android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_UI_MODE|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE|android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE ,android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING ,-1 ,2 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.activities.MusicPickerActivity", 0 ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.youtube.account.image.CropActivity", 0x801507c6 ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.audiopreview.AudioPreviewPlayerActivity", 0x8015003a ,android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE|android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.browser.BrowserActivity", 0x801506c1 ,android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE|android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.deeplink.MusicServiceDeepLinkActivity", 0x801506b5 ,0 ,android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING ,-1 ,0 ,".MusicServiceDeepLinkActivity"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.settings.SettingsCompatActivity", 0x801506c1 ,android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE|android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.apps.youtube.music.playlist.customthumbnail.CustomThumbnailCreationActivity", 0 ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN|android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE ,android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN ,android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.gms.common.api.GoogleApiActivity", android.R.style.Theme_Translucent_NoTitleBar ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.families.FamilyActivity", android.R.style.Theme_Translucent_NoTitleBar ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.notifications.entrypoints.systemtray.SystemTrayActivity", android.R.style.Theme_Translucent_NoTitleBar ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.parenttools.youtube.ParentToolsActivity", 0x801502bd ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD|android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN|android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_UI_MODE|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE|android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE ,0 ,android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.android.billingclient.api.ProxyBillingActivity", android.R.style.Theme_Translucent_NoTitleBar ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD|android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN|android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.android.billingclient.api.ProxyBillingActivityV2", android.R.style.Theme_Translucent_NoTitleBar ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD|android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN|android.content.pm.ActivityInfo.CONFIG_ORIENTATION|android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT|android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.social.licenses.LicenseMenuActivity", 0x801505d5 ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.social.licenses.LicenseActivity", 0x801505d5 ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.youtube.mdx.manualpairing.PairWithTvActivity", 0x80150290 ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.android.libraries.youtube.mdx.tvsignin.TvSignInActivity", 0x80150297 ,android.content.pm.ActivityInfo.CONFIG_KEYBOARD_HIDDEN ,0 ,android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.cardboard.sdk.QrCodeCaptureActivity", 0x801505e8 ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("com.google.cardboard.sdk.HeadsetDetectionActivity", 0 ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated"),
  new com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo("androidx.car.app.CarAppPermissionActivity", android.R.style.Theme_Translucent_NoTitleBar ,0 ,0 ,-1 ,0 ,"app.paradox.youtubeintegrated")
  };

  private static final com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo[] services = new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo[] {
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.youtube.player.background.service.BackgroundPlayerService", 2),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.youtube.offline.transfer.service.OfflineKeepAliveService", 1),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.apps.youtube.music.bluetooth.BluetoothA2dpService", 16),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.apps.youtube.music.mediabrowser.MusicBrowserService", 2),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.apps.youtube.music.mediabrowser.androidautoprojected.AndroidAutoCarAppServices", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.apps.youtube.music.notifications.FcmMessageListenerService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.phenotype.registration.PhenotypeMetadataHolderService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.apps.youtube.music.signals.update.HomePagePrefetchService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.firebase.components.ComponentDiscoveryService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.gms.measurement.AppMeasurementService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.gms.measurement.AppMeasurementJobService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.directboot.DirectBootHelperService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.mlkit.common.internal.MlKitComponentDiscoveryService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.notifications.entrypoints.scheduled.ScheduledTaskService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.notifications.platform.executor.impl.GnpExecutorApiService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.youtube.account.service.AccountsChangedJobIntentService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.youtube.mdx.mediaroute.service.RemotePlaybackControlsService", 2),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.libraries.youtube.offline.transfer.service.OfflineUserInitiatedDataTransferJobService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.apps.tiktok.concurrent.AndroidFuturesService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.apps.tiktok.concurrent.InternalForegroundService", 2048),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.firebase.messaging.FirebaseMessagingService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("android.net.http.MetaDataHolder", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("androidx.room.MultiInstanceInvalidationService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("androidx.work.impl.background.systemalarm.SystemAlarmService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("androidx.work.impl.background.systemjob.SystemJobService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("androidx.work.impl.foreground.SystemForegroundService", 0),
  new com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo("com.google.android.build.data.PropertiesServiceHolder", 0)
  };

  private static final com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo[] receivers = new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo[] {
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.offline.OfflineStorePackageReplacedReceiver", new String[]{"android.intent.action.MY_PACKAGE_REPLACED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.mediarouter.media.MediaTransferReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.signals.awareness.router.AwarenessRouterBroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.bluetooth.BluetoothA2dpReceiver", new String[]{"android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.media.session.MediaButtonReceiver", new String[]{"android.intent.action.MEDIA_BUTTON"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.mediabrowser.waze.MusicWazeBroadcastReceiver", new String[]{"com.waze.sdk.audio.ACTION_INIT"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.youtube.notification.NotificationInteractionBroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.player.widget.MusicWidgetProvider", new String[]{"android.appwidget.action.APPWIDGET_UPDATE", "com.google.android.apps.music.player.widget.FORCE_WIDGET_PROVIDER_REFRESH"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.player.widget.base.PendingIntentReceiver", new String[]{"com.google.android.youtube.music.pendingintent.controller_widget_play", "com.google.android.youtube.music.pendingintent.controller_widget_pause", "com.google.android.youtube.music.pendingintent.controller_widget_replay", "com.google.android.youtube.music.pendingintent.controller_widget_retry", "com.google.android.youtube.music.pendingintent.controller_widget_previous", "com.google.android.youtube.music.pendingintent.controller_widget_next", "com.google.android.youtube.music.pendingintent.controller_widget_dislike", "com.google.android.youtube.music.pendingintent.controller_widget_like", "com.google.android.youtube.music.pendingintent.controller_widget_launch_app", "com.google.android.youtube.music.pendingintent.controller_widget_request_data"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.player.widget.gm3.FreeformMusicWidgetProvider", new String[]{"android.appwidget.action.APPWIDGET_UPDATE", "com.google.android.apps.music.player.widget.FORCE_WIDGET_PROVIDER_REFRESH"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.player.widget.gm3.NowPlayingWidgetProvider", new String[]{"android.appwidget.action.APPWIDGET_UPDATE", "com.google.android.apps.music.player.widget.FORCE_WIDGET_PROVIDER_REFRESH"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.apps.youtube.music.player.widget.gm3.FlipWidgetProvider", new String[]{"android.appwidget.action.APPWIDGET_UPDATE", "com.google.android.apps.music.player.widget.FORCE_WIDGET_PROVIDER_REFRESH"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.gms.measurement.AppMeasurementReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.entrypoints.blockstatechanged.BlockStateChangedReceiver", new String[]{"android.app.action.APP_BLOCK_STATE_CHANGED", "android.app.action.NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED", "android.app.action.NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.entrypoints.systemtray.SystemTrayBroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.platform.entrypoints.accountchanged.AccountChangedReceiver", new String[]{"android.accounts.LOGIN_ACCOUNTS_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.platform.entrypoints.localechanged.LocaleChangedReceiver", new String[]{"android.intent.action.LOCALE_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.platform.entrypoints.restart.RestartReceiver", new String[]{"android.intent.action.BOOT_COMPLETED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.platform.entrypoints.timezonechanged.TimezoneChangedReceiver", new String[]{"android.intent.action.TIMEZONE_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.notifications.platform.entrypoints.update.UpdateReceiver", new String[]{"android.intent.action.MY_PACKAGE_REPLACED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.performance.primes.transmitter.LifeboatReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.phenotype.client.stable.AccountRemovedBroadcastReceiver", new String[]{"android.accounts.action.ACCOUNT_REMOVED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.phenotype.client.stable.PhenotypeUpdateBackgroundBroadcastReceiver", new String[]{"com.google.android.gms.phenotype.UPDATE"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.youtube.account.service.AccountsChangedReceiver", new String[]{"android.accounts.LOGIN_ACCOUNTS_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.youtube.mdx.background.MdxBackgroundPlaybackBroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.youtube.rendering.customtabs.CustomTabsReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.android.libraries.youtube.share.endpoint.ShareLoggingBroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("com.google.firebase.iid.FirebaseInstanceIdReceiver", new String[]{"app.revanced.android.c2dm.intent.RECEIVE"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.car.app.notification.CarAppNotificationBroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.utils.ForceStopRunnable$BroadcastReceiver", null),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.background.systemalarm.ConstraintProxy$BatteryChargingProxy", new String[]{"android.intent.action.ACTION_POWER_CONNECTED", "android.intent.action.ACTION_POWER_DISCONNECTED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.background.systemalarm.ConstraintProxy$BatteryNotLowProxy", new String[]{"android.intent.action.BATTERY_OKAY", "android.intent.action.BATTERY_LOW"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.background.systemalarm.ConstraintProxy$StorageNotLowProxy", new String[]{"android.intent.action.DEVICE_STORAGE_LOW", "android.intent.action.DEVICE_STORAGE_OK"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.background.systemalarm.ConstraintProxy$NetworkStateProxy", new String[]{"android.net.conn.CONNECTIVITY_CHANGE"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.background.systemalarm.RescheduleReceiver", new String[]{"android.intent.action.BOOT_COMPLETED", "android.intent.action.TIME_SET", "android.intent.action.TIMEZONE_CHANGED"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.background.systemalarm.ConstraintProxyUpdateReceiver", new String[]{"androidx.work.impl.background.systemalarm.UpdateProxies"}),
  new com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo("androidx.work.impl.diagnostics.DiagnosticsReceiver", new String[]{"androidx.work.diagnostics.REQUEST_DIAGNOSTICS"})
  };

  private static final com.tencent.shadow.core.runtime.PluginManifest.ProviderInfo[] providers = new com.tencent.shadow.core.runtime.PluginManifest.ProviderInfo[] {
  new com.tencent.shadow.core.runtime.PluginManifest.ProviderInfo("com.google.android.apps.youtube.music.util.MusicFileProviderCompat", "app.paradox.youtubeintegrated.fileprovider",true),
  new com.tencent.shadow.core.runtime.PluginManifest.ProviderInfo("com.google.firebase.provider.FirebaseInitProvider", "app.paradox.youtubeintegrated.firebaseinitprovider",false),
  new com.tencent.shadow.core.runtime.PluginManifest.ProviderInfo("com.google.mlkit.common.internal.MlKitInitProvider", "app.paradox.youtubeintegrated.mlkitinitprovider",false)
  };

  public final String getApplicationPackageName() {
    return applicationPackageName;
  }

  public final String getApplicationClassName() {
    return applicationClassName;
  }

  public final String getAppComponentFactory() {
    return appComponentFactory;
  }

  public final int getApplicationTheme() {
    return applicationTheme;
  }

  public final String[] getUsesPermissions() {
    return usesPermissions;
  }

  public final com.tencent.shadow.core.runtime.PluginManifest.ActivityInfo[] getActivities() {
    return activities;
  }

  public final com.tencent.shadow.core.runtime.PluginManifest.ServiceInfo[] getServices() {
    return services;
  }

  public final com.tencent.shadow.core.runtime.PluginManifest.ReceiverInfo[] getReceivers() {
    return receivers;
  }

  public final com.tencent.shadow.core.runtime.PluginManifest.ProviderInfo[] getProviders() {
    return providers;
  }
}

from pathlib import Path
import shutil,re
src=Path('/tmp/music-preserved-search');dst=Path('/tmp/music-bottom-search');dst.mkdir(exist_ok=True)
# Only rebuild the navigation/provider DEX. All other ZIP entries come directly from the confirmed working APK.
shutil.copytree(src/'smali_classes7',dst/'smali_classes7',dirs_exist_ok=True)
b=dst/'smali_classes7';n=b/'app/revanced/extension/music/patches/navigation/NavigationPatch.smali'
s=n.read_text().replace('# static fields','# static fields\n.field public static nativeSearchView:Landroid/view/View;')
a=s.index('.method public static filterAndReorderTabs');z=s.index('.end method',a);m=s[a:z];m=m.replace('.locals 7','.locals 8').replace('    move-object v2, v1','    move-object v7, v1\n\n    move-object v2, v1',1)
m=m.replace('    :cond_2\n    const-string v1, "FEmusic_explore"','''    :cond_2
    const-string v1, "FEmusic_home"
    invoke-virtual {v6, v1}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z
    move-result v1
    if-eqz v1, :not_home
    move-object v7, v5
    goto :goto_0
    :not_home
    const-string v1, "FEmusic_explore"''')
m=m.replace('    :cond_4\n    if-eqz v3', '''    :cond_4
    if-eqz v7, :no_search_tab
    invoke-virtual {v0, v7}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z
    :no_search_tab
    if-eqz v3''')
s=s[:a]+m+s[z:]
a=s.index('.method public static hideNavigationButton');i=s.index('    .line 59',a)
s=s[:i]+'''    sget-object v0, Lapp/revanced/extension/music/patches/navigation/NavigationPatch;->lastYTNavigationEnumName:Ljava/lang/String;
    const-string v1, "TAB_HOME"
    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
    move-result v0
    if-eqz v0, :not_search_tab
    const/4 v0, 0x0
    invoke-virtual {p0, v0}, Landroid/view/View;->setVisibility(I)V
    new-instance v0, Lapp/revanced/extension/music/patches/navigation/NavigationPatch$NavigationButton$$ExternalSyntheticLambda0;
    invoke-direct {v0}, Lapp/revanced/extension/music/patches/navigation/NavigationPatch$NavigationButton$$ExternalSyntheticLambda0;-><init>()V
    new-instance v1, Lapp/revanced/extension/music/patches/navigation/NavigationPatch$$ExternalSyntheticLambda1;
    invoke-direct {v1, v0}, Lapp/revanced/extension/music/patches/navigation/NavigationPatch$$ExternalSyntheticLambda1;-><init>(Ljava/lang/Runnable;)V
    invoke-virtual {p0, v1}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V
    return-void
    :not_search_tab
'''+s[i:]
a=s.index('.method public static replaceNavigationIcon');i=s.index('    .line 100',a)
s=s[:i]+'''    const v0, 0x7f0808c5
    if-eq p0, v0, :search_icon
    const v0, 0x7f080a10
    if-ne p0, v0, :not_home_icon
    :search_icon
    const p0, 0x7f080a65
    return p0
    :not_home_icon
'''+s[i:]
a=s.index('.method public static replaceNavigationLabel');i=s.index('    .line 114',a)
s=s[:i]+'''    sget-object v0, Lapp/revanced/extension/music/patches/navigation/NavigationPatch;->lastYTNavigationEnumName:Ljava/lang/String;
    const-string v1, "TAB_HOME"
    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
    move-result v0
    if-eqz v0, :not_search_label
    const-string v0, "Search"
    invoke-static {p0, v0}, Lapp/revanced/extension/shared/utils/Utils;->newSpanUsingStylingOfAnotherSpan(Landroid/text/Spanned;Ljava/lang/CharSequence;)Landroid/text/SpannableString;
    move-result-object p0
    return-object p0
    :not_search_label
'''+s[i:];n.write_text(s)
for name in ['jjf','jew']:
 p=b/(name+'.smali');s=p.read_text();needle='    invoke-interface {v2, v0}, Landroid/view/MenuItem;->setActionView(Landroid/view/View;)Landroid/view/MenuItem;';assert s.count(needle)==1
 s=s.replace(needle,needle+'\n    sput-object v0, Lapp/revanced/extension/music/patches/navigation/NavigationPatch;->nativeSearchView:Landroid/view/View;\n    const/4 v0, 0x0\n    invoke-interface {v2, v0}, Landroid/view/MenuItem;->setVisible(Z)Landroid/view/MenuItem;');p.write_text(s)
p=b/'app/revanced/extension/music/utils/ExtendedUtils.smali';s=p.read_text();a=s.index('.method public static openSearch()V');i=s.index('    .line 26',a)
s=s[:i]+'''    sget-object v0, Lapp/revanced/extension/music/patches/navigation/NavigationPatch;->nativeSearchView:Landroid/view/View;
    if-eqz v0, :shortcut_fallback
    invoke-virtual {v0}, Landroid/view/View;->performClick()Z
    return-void
    :shortcut_fallback
'''+s[i:];p.write_text(s)
print('Patched navigation, native search view retained, original search code untouched')

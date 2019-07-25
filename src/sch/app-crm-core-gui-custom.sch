
;; custom menu style for CRM 8
;;
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.menu.GlobalMenuStyleDefinitionHome.class) (let ((b (com.redknee.framework.xhome.menu.GlobalMenuStyleDefinition.))) (setBeanProperty b "Name" "TCB8LookNFeel")(setBeanProperty b "GlobalSetting" "_subOffsetLeft=20;
_menuCloseDelay=200;
_menuOpenDelay=200;
_subOffsetTop=-6;")(setBeanProperty b "MainMenuSetting" "alwaysvisible=1;
left=findPosX(gmobj('menu'));
top=findPosY(gmobj('menu'));
orientation=\"horizontal\";
style=mainMenuStyle;
followscroll='0';
menualign=\"left\";")(setBeanProperty b "StyleDefinition" (let ((c (java.util.ArrayList.))) (.add c (let ((b (com.redknee.framework.xhome.menu.MenuStyleDefinition.)))(setBeanProperty b "MenuKey" "menuStyle")(setBeanProperty b "StyleDefinition" "with(menuStyle=new mm_style()){
bordercolor=\"#000000\";
borderstyle=\"dotted solid\";
borderwidth=1;
align=\"left\";
itemheight=\"20\";
itemwidth=\"150\";
valign=\"top\";
subimage=\"./images/black_13x13_greyboxed.gif\";
subimageposition='right';
fontfamily=\"verdana\";
fontsize=\"100%\";
fontstyle=\"normal\";
fontweight=\"500\";
headerbgcolor=\"#555555\";
headercolor=\"#000000\";
onbgcolor=\"#4F8EB6\";
offbgcolor=\"#DCE9F0\";
offcolor=\"#000000\";
oncolor=\"#DDDDDD\";
padding=4;
pagebgcolor=\"#999999\";
pagecolor=\"#000099\";
separatorcolor=\"#999999\";
separatorsize=1;
subimagepadding=2;
overfilter=\"Fade(duration=0.2);Alpha(opacity=90);Shadow(color='#777777', Direction=135, Strength=5)\";
}") b))(.add c (let ((b (com.redknee.framework.xhome.menu.MenuStyleDefinition.)))(setBeanProperty b "MenuKey" "mainMenuStyle")(setBeanProperty b "StyleDefinition" "with(mainMenuStyle=new mm_style()){
bordercolor=\"#000033\";
borderstyle=\"dotted solid\";
borderwidth=1;
align=\"left\";
itemheight=\"20\";
itemwidth=\"120\";
valign=\"top\";
subimage=\"./images/black_13x13_greyboxed.gif\";
subimageposition=\"bottom\";
fontfamily=\"verdana\";
fontsize=\"100%\";
fontstyle=\"normal\";
fontweight=\"500\";
headerbgcolor=\"#ffffff\";
headercolor=\"#000000\";
offcolor=\"#000000\";
onbgcolor=\"#4F8EB6\";
offbgcolor=\"#DCE9F0\";
oncolor=\"#ffffff\";
padding=4;
pagebgcolor=\"#999999\";
pagecolor=\"#000099\";
separatorcolor=\"#999999\";
separatorsize=1;
subimagepadding=2;
}") b)) c))(setBeanProperty b "SubMenuDefinition" (let ((c (java.util.ArrayList.)))  c)) b)))

(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.xhome.menu.DefaultGlobalMenuStyle.class))) (setBeanProperty b "name" "TCB8LookNFeel") b))
public class Driver {
    public static void main(String[] args) {
        BaseFrame frame = new BaseFrame("Hooop!");
        SettingsMenu settingsMenu = new SettingsMenu();
        
        BaseFrame.addScreen(TitleScreen.SCREEN_NAME, new TitleScreen(settingsMenu));
        BaseFrame.addScreen(SettingsMenu.SCREEN_NAME, settingsMenu);
        
        BaseFrame.showScreen(TitleScreen.SCREEN_NAME);
        frame.setVisible(true);
    }
}
from kivy.app import App
from kivy.config import Config
from kivy.core.window import Window
from kivy.uix.screenmanager import ScreenManager, FadeTransition

# Configure window for landscape when timer is active
Config.set('graphics', 'fullscreen', 'auto')

# Set global background color
from theme import BG_PRIMARY
Window.clearcolor = BG_PRIMARY

from screens.splash_screen import SplashScreen
from screens.main_list_screen import MainListScreen
from screens.asana_list_config_screen import AsanaListConfigScreen
from screens.timer_screen import TimerScreen

class YogaAsanaTimerApp(App):
    def build(self):
        sm = ScreenManager(transition=FadeTransition(duration=0.3))
        
        # Create screens
        splash = SplashScreen(name='splash')
        main_list = MainListScreen(name='main_list')
        asana_config = AsanaListConfigScreen(name='asana_config')
        timer = TimerScreen(name='timer')
        
        sm.add_widget(splash)
        sm.add_widget(main_list)
        sm.add_widget(asana_config)
        sm.add_widget(timer)
        
        return sm

if __name__ == '__main__':
    YogaAsanaTimerApp().run()


from kivy.uix.screenmanager import Screen
from kivy.uix.label import Label
from kivy.uix.boxlayout import BoxLayout
from kivy.clock import Clock
from kivy.metrics import dp
from theme import TEXT_PRIMARY, ACCENT_PRIMARY


class SplashScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.build_ui()

    def build_ui(self):
        layout = BoxLayout(orientation='vertical')
        
        label = Label(
            text='🧘\nYoga Asana Timer',
            font_size=dp(48),
            color=TEXT_PRIMARY,
            halign='center'
        )
        
        layout.add_widget(label)
        self.add_widget(layout)

    def on_enter(self):
        # Show splash for 1.5 seconds then go to main list
        Clock.schedule_once(self.go_to_main, 1.5)

    def go_to_main(self, dt):
        self.manager.current = 'main_list'


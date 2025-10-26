from kivy.uix.screenmanager import Screen
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.boxlayout import BoxLayout
from kivy.clock import Clock
from kivy.metrics import dp
from theme import (ACCENT_PRIMARY, TEXT_PRIMARY,
                   BTN_PRIMARY, BTN_DANGER,
                   HEART_CHAKRA, THIRD_EYE_CHAKRA, TEXT_SECONDARY)

class TimerScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.current_list = None
        self.current_asana_index = 0
        self.remaining_time = 0
        self.event = None
        self.is_paused = False
        self.build_ui()
    
    def build_ui(self):
        layout = BoxLayout(orientation='vertical', padding=dp(30))
        
        # Timer display with chakra styling
        self.timer_label = Label(
            text='00',
            font_size=dp(150),
            size_hint_y=0.6,
            color=ACCENT_PRIMARY,
            bold=True
        )
        layout.add_widget(self.timer_label)
        
        # Asana name
        self.asana_label = Label(
            text='',
            font_size=dp(40),
            size_hint_y=0.1,
            color=TEXT_PRIMARY
        )
        layout.add_widget(self.asana_label)
        
        # Progress indicator
        progress_layout = BoxLayout(
            orientation='horizontal',
            size_hint_y=0.05,
            spacing=dp(5)
        )
        self.progress_layout = progress_layout
        layout.add_widget(progress_layout)
        
        # Controls
        controls_layout = BoxLayout(
            orientation='horizontal',
            size_hint_y=0.15,
            spacing=dp(20),
            padding=dp(20)
        )
        
        pause_resume_btn = Button(
            text='⏸',
            font_size=dp(50),
            size_hint_x=0.5,
            background_color=BTN_PRIMARY,
            color=TEXT_PRIMARY
        )
        pause_resume_btn.bind(on_press=self.toggle_pause)
        self.pause_btn = pause_resume_btn
        
        cancel_btn = Button(
            text='❌ Abbrechen',
            font_size=dp(24),
            size_hint_x=0.5,
            background_color=BTN_DANGER,
            color=TEXT_PRIMARY
        )
        cancel_btn.bind(on_press=self.cancel)
        
        controls_layout.add_widget(pause_resume_btn)
        controls_layout.add_widget(cancel_btn)
        layout.add_widget(controls_layout)
        
        # Info
        self.info_label = Label(
            text='',
            font_size=dp(20),
            size_hint_y=0.1,
            color=TEXT_SECONDARY
        )
        layout.add_widget(self.info_label)
        
        self.add_widget(layout)
    
    def start_list(self, asana_list):
        self.current_list = asana_list
        self.current_asana_index = 0
        self.is_paused = False
        self.update_progress_indicators()
        self.start_current_asana()
    
    def start_current_asana(self):
        if not self.current_list or self.current_asana_index >= len(self.current_list['asanas']):
            self.complete_list()
            return
        
        current_asana = self.current_list['asanas'][self.current_asana_index]
        self.remaining_time = current_asana['duration']
        self.asana_label.text = current_asana['name']
        self.update_display()
        
        if self.event:
            Clock.unschedule(self.event)
        
        self.event = Clock.schedule_interval(self.tick, 1.0)
        self.update_info()
    
    def tick(self, dt):
        if not self.is_paused:
            self.remaining_time -= 1
            self.update_display()
            
            if self.remaining_time <= 0:
                # Move to next asana
                self.current_asana_index += 1
                if self.event:
                    Clock.unschedule(self.event)
                
                if self.current_asana_index < len(self.current_list['asanas']):
                    # Brief pause before next asana
                    Clock.schedule_once(lambda x: self.start_current_asana(), 0.5)
                else:
                    self.complete_list()
    
    def update_display(self):
        minutes = self.remaining_time // 60
        seconds = self.remaining_time % 60
        self.timer_label.text = f"{minutes:02d}:{seconds:02d}"
        
        # Update progress indicators
        self.update_progress_indicators()
    
    def update_info(self):
        total = len(self.current_list['asanas'])
        current = self.current_asana_index + 1
        self.info_label.text = f"Asana {current}/{total}"
    
    def update_progress_indicators(self):
        self.progress_layout.clear_widgets()
        
        for i, asana in enumerate(self.current_list['asanas']):
            if i < self.current_asana_index:
                color = HEART_CHAKRA  # Green - completed
            elif i == self.current_asana_index:
                color = THIRD_EYE_CHAKRA  # Indigo - current
            else:
                # Gray - pending, mit reduzierter Transparenz
                color = (TEXT_SECONDARY[0], TEXT_SECONDARY[1], 
                        TEXT_SECONDARY[2], 0.3)
            
            dot = Label(text='●', font_size=dp(20), color=color)
            self.progress_layout.add_widget(dot)
    
    def toggle_pause(self, instance):
        self.is_paused = not self.is_paused
        if self.is_paused:
            self.pause_btn.text = '▶'
        else:
            self.pause_btn.text = '⏸'
    
    def cancel(self, instance):
        if self.event:
            Clock.unschedule(self.event)
        self.manager.current = 'main_list'
    
    def complete_list(self):
        self.asana_label.text = '✓ Abgeschlossen!'
        self.timer_label.text = '00:00'
        self.info_label.text = 'Alle Asanas abgeschlossen'
        Clock.schedule_once(lambda x: self.cancel(None), 2.0)


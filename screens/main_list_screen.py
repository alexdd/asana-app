from kivy.uix.screenmanager import Screen
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.scrollview import ScrollView
from kivy.uix.popup import Popup
from kivy.metrics import dp
from data_manager import data_manager
from theme import (TEXT_PRIMARY, BTN_PRIMARY, BTN_SECONDARY, BTN_DANGER,
                   ACCENT_PRIMARY, BG_SECONDARY)

class MainListScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.current_editing_id = None
        self.build_ui()
    
    def build_ui(self):
        layout = BoxLayout(orientation='vertical', padding=dp(20), spacing=dp(10))
        
        # Title
        title = Label(
            text='🧘 Yoga Asana Timer',
            size_hint_y=None,
            height=dp(60),
            font_size=dp(28),
            bold=True,
            color=TEXT_PRIMARY
        )
        layout.add_widget(title)
        
        # Scrollable list of Asana lists
        scroll = ScrollView()
        self.grid = GridLayout(
            cols=1,
            size_hint_y=None,
            spacing=dp(10),
            padding=dp(10)
        )
        self.grid.bind(minimum_height=self.grid.setter('height'))
        scroll.add_widget(self.grid)
        layout.add_widget(scroll)
        
        self.add_widget(layout)
        self.refresh_lists()
    
    def refresh_lists(self):
        self.grid.clear_widgets()
        lists = data_manager.get_asana_lists()
        
        if not lists:
            # Show placeholder
            placeholder = Button(
                text='[+] Neue Asana Liste erstellen',
                size_hint_y=None,
                height=dp(100),
                font_size=dp(20),
                background_color=BTN_PRIMARY,
                color=TEXT_PRIMARY
            )
            placeholder.bind(on_press=self.create_new_list)
            self.grid.add_widget(placeholder)
        else:
            # Add existing lists
            for asana_list in lists:
                btn = self.create_list_button(asana_list)
                self.grid.add_widget(btn)
            
            # Add create button at the end
            add_btn = Button(
                text='[+] Neue Liste',
                size_hint_y=None,
                height=dp(80),
                background_color=BTN_SECONDARY,
                color=TEXT_PRIMARY
            )
            add_btn.bind(on_press=self.create_new_list)
            self.grid.add_widget(add_btn)
    
    def create_list_button(self, asana_list):
        btn_layout = BoxLayout(
            size_hint_y=None,
            height=dp(100),
            padding=dp(15),
            spacing=dp(10),
            orientation='horizontal'
        )
        
        total_duration = sum(a['duration'] for a in asana_list['asanas'])
        main_btn = Button(
            text=f"{asana_list['name']}\n"
                 f"{len(asana_list['asanas'])} Asanas • "
                 f"{total_duration}s Gesamtzeit",
            size_hint_x=0.7,
            text_size=(None, None),
            halign='left',
            valign='middle',
            padding_x=dp(20),
            background_color=BG_SECONDARY,
            color=TEXT_PRIMARY
        )
        main_btn.bind(on_press=lambda x, lid=asana_list['id']: self.start_list(lid))
        
        # Edit button
        edit_btn = Button(
            text='✎',
            size_hint_x=0.15,
            font_size=dp(24),
            background_color=ACCENT_PRIMARY,
            color=TEXT_PRIMARY
        )
        edit_btn.bind(on_press=lambda x, lid=asana_list['id']: self.edit_list(lid))
        
        # Delete button
        delete_btn = Button(
            text='✕',
            size_hint_x=0.15,
            font_size=dp(24),
            background_color=BTN_DANGER,
            color=TEXT_PRIMARY
        )
        delete_btn.bind(on_press=lambda x, lid=asana_list['id']: self.delete_list(lid))
        
        btn_layout.add_widget(main_btn)
        btn_layout.add_widget(edit_btn)
        btn_layout.add_widget(delete_btn)
        
        return btn_layout
    
    def create_new_list(self, instance):
        self.current_editing_id = None
        self.manager.get_screen('asana_config').setup_new_list()
        self.manager.current = 'asana_config'
    
    def edit_list(self, list_id):
        self.current_editing_id = list_id
        asana_list = data_manager.get_asana_list(list_id)
        self.manager.get_screen('asana_config').setup_edit(asana_list)
        self.manager.current = 'asana_config'
    
    def delete_list(self, list_id):
        popup = Popup(
            title='Liste löschen?',
            content=Label(text='Möchten Sie diese Liste wirklich löschen?'),
            size_hint=(0.8, 0.4)
        )
        
        layout = BoxLayout(orientation='horizontal', padding=dp(10), spacing=dp(10))
        
        def confirm_delete():
            data_manager.delete_asana_list(list_id)
            self.refresh_lists()
            popup.dismiss()
        
        def cancel():
            popup.dismiss()
        
        cancel_button = Button(
            text='Abbrechen',
            on_press=lambda x: cancel(),
            background_color=BG_SECONDARY,
            color=TEXT_PRIMARY
        )
        delete_button = Button(
            text='Löschen',
            on_press=lambda x: confirm_delete(),
            background_color=BTN_DANGER,
            color=TEXT_PRIMARY
        )
        layout.add_widget(cancel_button)
        layout.add_widget(delete_button)
        
        popup.content.add_widget(layout)
        popup.open()
    
    def start_list(self, list_id):
        asana_list = data_manager.get_asana_list(list_id)
        self.manager.get_screen('timer').start_list(asana_list)
        self.manager.current = 'timer'


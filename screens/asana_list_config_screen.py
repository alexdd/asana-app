from kivy.uix.screenmanager import Screen
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.scrollview import ScrollView
from kivy.uix.textinput import TextInput
from kivy.metrics import dp
from data_manager import data_manager
from theme import TEXT_PRIMARY, BTN_PRIMARY, BTN_DANGER, BTN_SUCCESS

class AsanaListConfigScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.current_list_id = None
        self.build_ui()
    
    def build_ui(self):
        layout = BoxLayout(orientation='vertical', padding=dp(20), spacing=dp(10))
        
        # Title
        self.title_label = Label(
            text='Neue Asana Liste',
            size_hint_y=None,
            height=dp(50),
            font_size=dp(24),
            bold=True,
            color=TEXT_PRIMARY
        )
        layout.add_widget(self.title_label)
        
        # List name input
        list_name_layout = BoxLayout(size_hint_y=None, height=dp(50), spacing=dp(10))
        label = Label(text='Listenname:', size_hint_x=0.3, halign='right', color=TEXT_PRIMARY)
        list_name_layout.add_widget(label)
        self.list_name_input = TextInput(
            hint_text='z.B. Morgendliche Sonnengrüße',
            multiline=False,
            size_hint_x=0.7
        )
        list_name_layout.add_widget(self.list_name_input)
        layout.add_widget(list_name_layout)
        
        # Asanas scroll area
        asanas_label = Label(
            text='Asanas:',
            size_hint_y=None,
            height=dp(30),
            font_size=dp(18),
            halign='left',
            color=TEXT_PRIMARY
        )
        layout.add_widget(asanas_label)
        
        scroll = ScrollView()
        self.asana_grid = GridLayout(
            cols=1,
            size_hint_y=None,
            spacing=dp(10),
            padding=dp(10)
        )
        self.asana_grid.bind(minimum_height=self.asana_grid.setter('height'))
        scroll.add_widget(self.asana_grid)
        layout.add_widget(scroll)
        
        # Action buttons
        btn_layout = BoxLayout(size_hint_y=None, height=dp(60), spacing=dp(10))
        
        cancel_btn = Button(
            text='Zurück',
            size_hint_x=0.3,
            background_color=BTN_DANGER,
            color=TEXT_PRIMARY
        )
        cancel_btn.bind(on_press=lambda x: self.go_back())
        
        add_btn = Button(
            text='+ Asana',
            size_hint_x=0.35,
            background_color=BTN_PRIMARY,
            color=TEXT_PRIMARY
        )
        add_btn.bind(on_press=lambda x: self.add_asana_row())
        
        save_btn = Button(
            text='Speichern',
            size_hint_x=0.35,
            background_color=BTN_SUCCESS,
            color=TEXT_PRIMARY
        )
        save_btn.bind(on_press=lambda x: self.save_list())
        
        btn_layout.add_widget(cancel_btn)
        btn_layout.add_widget(add_btn)
        btn_layout.add_widget(save_btn)
        layout.add_widget(btn_layout)
        
        self.add_widget(layout)
    
    def setup_new_list(self):
        self.current_list_id = None
        self.title_label.text = 'Neue Asana Liste'
        self.list_name_input.text = ''
        self.asana_grid.clear_widgets()
        self.add_asana_row()
    
    def setup_edit(self, asana_list):
        self.current_list_id = asana_list['id']
        self.title_label.text = f'Bearbeite: {asana_list["name"]}'
        self.list_name_input.text = asana_list['name']
        self.asana_grid.clear_widgets()
        
        for asana in asana_list['asanas']:
            self.add_asana_row(asana['name'], asana['duration'])
    
    def add_asana_row(self, name='', duration=30):
        row = BoxLayout(size_hint_y=None, height=dp(50), spacing=dp(10))
        
        name_input = TextInput(
            text=name,
            hint_text='Asana Name',
            multiline=False,
            size_hint_x=0.4
        )
        
        duration_input = TextInput(
            text=str(duration),
            hint_text='Sekunden',
            multiline=False,
            size_hint_x=0.3,
            input_filter='int'
        )
        
        delete_btn = Button(
            text='✕',
            size_hint_x=0.1,
            background_color=BTN_DANGER,
            color=TEXT_PRIMARY
        )
        delete_btn.bind(on_press=lambda x: self.remove_asana_row(row))
        
        row.add_widget(name_input)
        row.add_widget(duration_input)
        sec_label = Label(text='s', size_hint_x=0.1, color=TEXT_PRIMARY)
        row.add_widget(sec_label)
        row.add_widget(delete_btn)
        
        self.asana_grid.add_widget(row)
    
    def remove_asana_row(self, row):
        self.asana_grid.remove_widget(row)
    
    def save_list(self):
        name = self.list_name_input.text.strip()
        if not name:
            # Show error
            return
        
        asanas = []
        for child in self.asana_grid.children:
            if isinstance(child, BoxLayout):
                inputs = [w for w in child.children if isinstance(w, TextInput)]
                if len(inputs) >= 2:
                    asana_name = inputs[1].text.strip()  # name input
                    duration_str = inputs[0].text.strip()  # duration input
                    if asana_name and duration_str:
                        try:
                            asanas.append({
                                'name': asana_name,
                                'duration': int(duration_str)
                            })
                        except ValueError:
                            pass
        
        if not asanas:
            # Show error
            return
        
        if self.current_list_id:
            data_manager.update_asana_list(self.current_list_id, name, asanas)
        else:
            data_manager.add_asana_list(name, asanas)
        
        self.go_back()
    
    def go_back(self):
        self.manager.current = 'main_list'
        self.manager.get_screen('main_list').refresh_lists()


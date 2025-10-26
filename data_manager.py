import json
from pathlib import Path

class DataManager:
    def __init__(self):
        self.data_dir = Path.home() / '.yoga_asana_timer'
        self.data_dir.mkdir(exist_ok=True)
        self.data_file = self.data_dir / 'asana_lists.json'
        self.load_data()
    
    def load_data(self):
        if self.data_file.exists():
            with open(self.data_file, 'r', encoding='utf-8') as f:
                try:
                    self.data = json.load(f)
                    if 'asana_lists' not in self.data:
                        self.data = {'asana_lists': []}
                except json.JSONDecodeError:
                    self.data = {'asana_lists': []}
        else:
            self._create_example_data()
    
    def _create_example_data(self):
        """Create initial example with real Asanas"""
        self.data = {
            'asana_lists': [
                {
                    'id': '1',
                    'name': 'Morgendliche Sonnengrüße',
                    'asanas': [
                        {'name': 'Tadasana', 'duration': 30},
                        {'name': 'Urdhva Hastasana', 'duration': 20},
                        {'name': 'Uttanasana', 'duration': 30},
                        {'name': 'Chaturanga Dandasana', 'duration': 15},
                        {'name': 'Adho Mukha Svanasana', 'duration': 30},
                        {'name': 'Virabhadrasana I', 'duration': 45},
                        {'name': 'Savasana', 'duration': 120}
                    ]
                },
                {
                    'id': '2',
                    'name': 'Hüftöffnende Sequenz',
                    'asanas': [
                        {'name': 'Baddha Konasana', 'duration': 90},
                        {'name': 'Gomukhasana', 'duration': 60},
                        {'name': 'Eka Pada Rajakapotasana', 'duration': 90},
                        {'name': 'Hip Opener', 'duration': 60},
                        {'name': 'Supine Twist', 'duration': 45}
                    ]
                }
            ]
        }
        self.save_data()
    
    def save_data(self):
        with open(self.data_file, 'w', encoding='utf-8') as f:
            json.dump(self.data, f, ensure_ascii=False, indent=2)
    
    def get_asana_lists(self):
        return self.data.get('asana_lists', [])
    
    def add_asana_list(self, name, asanas):
        new_id = str(len(self.data['asana_lists']) + 1)
        new_list = {
            'id': new_id,
            'name': name,
            'asanas': asanas
        }
        self.data['asana_lists'].append(new_list)
        self.save_data()
        return new_id
    
    def update_asana_list(self, list_id, name, asanas):
        for lst in self.data['asana_lists']:
            if lst['id'] == list_id:
                lst['name'] = name
                lst['asanas'] = asanas
                self.save_data()
                return True
        return False
    
    def delete_asana_list(self, list_id):
        self.data['asana_lists'] = [lst for lst in self.data['asana_lists'] if lst['id'] != list_id]
        self.save_data()
    
    def get_asana_list(self, list_id):
        for lst in self.data['asana_lists']:
            if lst['id'] == list_id:
                return lst
        return None

# Global data manager instance
data_manager = DataManager()


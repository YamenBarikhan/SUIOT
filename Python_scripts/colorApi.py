import requests
import sys
from get_state import get_state
import time

api_key = 'cad26e09-fbeb-4abc-92f9-0e08cde8972c'

headers = {
            'Content-Type': 'application/json',
            'Govee-API-Key': api_key,
        }

device = 'C6:A7:D4:AD:FC:DA:EC:6A'

model = 'H6009'

amount = 0

color_red = sys.argv[1]
color_green = sys.argv[2]
color_blue = sys.argv[3]

ssh_command = {}

def change_color(cmd):
    url = 'https://developer-api.govee.com/v1/devices/control'

    data = {
        'device': device,
        'model': model,
        'cmd': {
            'name': 'color',
            'value': {
                'r': int(color_red),
                'g': int(color_green),
                'b': int(color_blue)
            }
        }
    }
   #print(data)

    try:
        response = requests.put(url, headers=headers, json=data)
        
    except requests.exceptions.RequestException as e:
        print('Request error:', e)


change_color(color_red + color_green + color_blue)
get_state()

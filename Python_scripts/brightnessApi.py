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

brightness_cmd = sys.argv[1]

ssh_command = {}

def brightness_api(cmd):
    ssh_command['name'] = 'brightness'
    ssh_command['value'] = int(brightness_cmd)

    url = 'https://developer-api.govee.com/v1/devices/control'

    data = {
        'device': device,
        'model': model,
        'cmd': ssh_command
    }

    #print(data)

    try:
        response = requests.put(url, headers=headers, json=data)
        # print("Response content:", response.content)
        
    except requests.exceptions.RequestException as e:
        print('Request error:', e)

brightness_api(brightness_cmd)
get_state()

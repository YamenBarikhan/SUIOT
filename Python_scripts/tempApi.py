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

temp = sys.argv[1]

ssh_command = {}

def post_temp(cmd):
    ssh_command['name'] = 'colorTem'
    ssh_command['value'] = int(temp)

    url = 'https://developer-api.govee.com/v1/devices/control'

    data = {
        'device': device,
        'model': model,
        'cmd': ssh_command
    }

    # print(data)

    try:
        response = requests.put(url, headers=headers, json=data)
        # print("Response content:", response.content)
        
    except requests.exceptions.RequestException as e:
        print('Request error:', e)

post_temp(temp)
get_state()

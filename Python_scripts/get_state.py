import requests
import time

api_key = 'cad26e09-fbeb-4abc-92f9-0e08cde8972c'

headers = {
            'Content-Type': 'application/json',
            'Govee-API-Key': api_key,
        }


def get_state():
    info = []
    i = 0
    j = 0

    url = 'https://developer-api.govee.com/v1/devices/state?device=C6%3AA7%3AD4%3AAD%3AFC%3ADA%3AEC%3A6A&model=H6009'
    try:
        time.sleep(0.5)
        response = requests.get(url, headers=headers)
        data = response.json()
        if 'color' in data['data']['properties'][3]:
            on_off = data['data']['properties'][1]['powerState']
            brightness = data['data']['properties'][2]['brightness']
            color_r = data['data']['properties'][3]['color']['r']
            color_g = data['data']['properties'][3]['color']['g']
            color_b = data['data']['properties'][3]['color']['b']
            info.append(on_off)
            info.append(brightness)
            info.append(color_r)
            info.append(color_g)
            info.append(color_b)
            result_string = ', '.join(str(item) for item in info)
            print(1)


        elif 'colorTem' in data['data']['properties'][4]:
            on_off = data['data']['properties'][1]['powerState']
            brightness = data['data']['properties'][2]['brightness']
            color_temp = data['data']['properties'][4]['colorTem']
            info.append(on_off)
            info.append(brightness)
            info.append(color_temp)
            result_string = ', '.join(str(item) for item in info)
            print(2)

        print(result_string)
    except requests.exceptions.RequestException as e:
        print('Request error:', e)


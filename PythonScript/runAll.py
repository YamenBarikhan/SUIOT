import requests
from transcriptVoice import transcribe_file_v2
from transcriptVoice import get_list

audio_file_path = "E:\\IoT\\IoT\\stt.mp3"
project_id = "groovy-gearbox-407414"

transcribed_commands = transcribe_file_v2(project_id, audio_file_path)

filtered_commands = get_list('words')

api_key = 'cad26e09-fbeb-4abc-92f9-0e08cde8972c'

headers = {
            'Content-Type': 'application/json',
            'Govee-API-Key': api_key,
        }

device = 'C6:A7:D4:AD:FC:DA:EC:6A'

model = 'H6009'

amount = 0

def filter_words():

    turn = {}
    color = {}
    brightness = {}
    temp = {}
    if 'turn' in transcribed_commands:
        turn['name'] = 'turn'
    if 'change' and 'brightness' in transcribed_commands:
        brightness['name'] = 'brightness'
    if 'change' and 'color' in transcribed_commands:
        color['name'] = 'color'
    if 'change' and 'temperature' in transcribed_commands:
        temp['name'] = 'colorTem'


    for word in transcribed_commands:
        if word == "on":
            turn['value'] = 'on'
            post_normal_api(turn)
        elif word == 'off':
            turn['value'] = 'off'
            post_normal_api(turn)
        elif word == 'red':
            color['r'] = 255
            color['g'] = 0
            color['b'] = 0
            change_color(color)
        elif word == 'green':
            color['r'] = 0
            color['g'] = 255
            color['b'] = 0
            change_color(color)
        elif word == 'blue':
            color['r'] = 0
            color['g'] = 0
            color['b'] = 255
            change_color(color)
        elif word == 'white':
            color['r'] = 255
            color['g'] = 255
            color['b'] = 255
            change_color(color)
        elif word == '10%' or word == '20%' or word == '30%' or word == '40%' or word == '50%' or word == '60%' or word == '70%' or word == '80%' or word == '90%' or word == '100%':
            percentage = int(word[:-1])
            brightness['value'] = percentage
            post_normal_api(brightness)
        elif word == 'warm':
            temp['value'] = 2700
            post_normal_api(temp)
        elif word == 'normal':
            temp['value'] = 4600
            post_normal_api(temp)
        elif word == 'cool':
            temp['value'] = 6500
            post_normal_api(temp)


def change_color(cmd):
    url = 'https://developer-api.govee.com/v1/devices/control'

    data = {
        'device': device,
        'model': model,
        'cmd': {
            'name': 'color',
            'value': {
                'r': cmd['r'],
                'g': cmd['g'],
                'b': cmd['b']
            }
        }
    }


    try:
        response = requests.put(url, headers=headers, json=data)
        if response.status_code == 200:
            print("Begäran för att styra lampan lyckades")
        else:
            print(f"Misslyckades med att styra lampan med statuskod: {response.status_code}")

    except requests.exceptions.RequestException as e:
        print('Request error:', e)


def post_normal_api(cmd):
    url = 'https://developer-api.govee.com/v1/devices/control'

    data = {
        'device': device,
        'model': model,
        'cmd': cmd
    }

    # print(data)

    try:
        response = requests.put(url, headers=headers, json=data)
        # print("Response content:", response.content)
        if response.status_code == 200:
            print("Begäran för att styra lampan lyckades")
        else:
            print(f"Misslyckades med att styra lampan med statuskod: {response.status_code}")

    except requests.exceptions.RequestException as e:
        print('Request error:', e)

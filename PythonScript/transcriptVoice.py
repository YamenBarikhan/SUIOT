from google.cloud.speech_v2 import SpeechClient
from google.cloud.speech_v2.types import cloud_speech

filtered=[]
colors = ["red", "green", "blue", "white"]
brightness = ["10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%" , "100%"]
on_off=["on", "off"]
words=["turn", "on", "off","and", "change", "color", "increase", "decrease", "dim", "white", "red", "green", "blue", "brightness", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%" , "100%", "temperature", "warm","normal","cool"]
temp=["warm","normal","cool"]
resultlist=[]
added=[]

def get_list(listName):
    if listName == 'words':
        return words
    if listName == 'filtered':
        return filtered
    if listName == 'brightness':
        return brightness
    if listName == 'on_off':
        return on_off
    if listName == 'colors':
        return colors
    if listName == 'temp':
        return temp
    if listName == 'resultlist':
        return resultlist
    if listName == 'added':
        return added
def transcribe_file_v2(
    project_id: str,
    audio_file: str,
) -> cloud_speech.RecognizeResponse:

    client = SpeechClient()

    with open(audio_file, "rb") as f:
        content = f.read()

    config = cloud_speech.RecognitionConfig(
        auto_decoding_config=cloud_speech.AutoDetectDecodingConfig(),
        language_codes=["en-US"],
        model="long",
    )

    request = cloud_speech.RecognizeRequest(
        recognizer=f"projects/{project_id}/locations/global/recognizers/_",
        config=config,
        content=content,
    )


    response = client.recognize(request=request)

    for result in response.results:
        wordlist = result.alternatives[0].transcript.split()
        for wordd in wordlist:
            wordd = wordd.lower()
            resultlist.append(wordd)

    and_count = 0
    change_count = 0

    for word in resultlist:
        if word in words:
            if word == 'and' or word == 'change':
                if word == 'and' and and_count < 2:
                    and_count += 1
                    filtered.append(word)
                elif word == 'change' and change_count < 2:
                    change_count += 1
                    filtered.append(word)
                else:
                    continue
            elif word not in filtered:
                if word == 'green' or word == 'blue' or word == 'red' or word == 'white':
                    if any(color in added for color in colors):
                        continue
                    else:
                        filtered.append(word)
                        added.append(word)
                if word == 'cool' or word == 'warm' or word == 'normal':
                    if any(temperature in added for temperature in temp):
                        continue
                    else:
                        filtered.append(word)
                        added.append(word)
                if word == '10%' or word == '20%' or word == '30%' or word == '40%' or word == '50%' or word == '60%' or word == '70%' or word == '80%' or word == '90%' or word == '100%':
                    if any(bright in added for bright in brightness):
                        continue
                    else:
                        filtered.append(word)
                        added.append(word)
                if word == 'on' or word == 'off':
                    if any(action in added for action in on_off):
                        continue
                    else:
                        filtered.append(word)
                        added.append(word)
                if word == 'turn':
                    if word in filtered:
                        continue
                    else:
                        filtered.append(word)
                elif word not in filtered:
                    filtered.append(word)


    #print(resultlist)
    #print(filtered)
    return filtered


#project_id = "groovy-gearbox-407414"
#audio_file_path = "E:\\IoT\\IoT\\stt.mp3"
#transcribe_file_v2(project_id, audio_file_path)
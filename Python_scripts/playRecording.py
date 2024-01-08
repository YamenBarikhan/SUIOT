import pygame

pygame.init()
pygame.mixer.init()


sound = pygame.mixer.Sound("/home/noaalk03/IoT/stt.mp3")


sound.play()


while pygame.mixer.get_busy():
    pass

pygame.mixer.quit()

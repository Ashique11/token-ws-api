import pygame
import sys

if __name__ == "__main__":
    # Check if the correct number of arguments is provided
    if len(sys.argv) < 1:
        print("Usage: python myscript.py <arg1> <arg2>")
        sys.exit(1)
        
arg1 = sys.argv[1]

try:
    filename=arg1;
    pygame.init()
    pygame.mixer.music.load(filename)
    pygame.mixer.music.play()
    pygame.time.delay(500);
    pygame.event.wait()
except KeyboardInterrupt:
    pygame.mixer.music.stop()
finally:
    pygame.quit()

#define EEPROM_ADDR_START 0
#define EEPROM_ADDR_BRIGHTNESS 1
#define EEPROM_ADDR_MODE 2
#define EEPROM_ADDR_IMAGES (EEPROM_ADDR_MODE+1)
#define EEPROM_ADDR_ANIMATIONS (EEPROM_ADDR_IMAGES+sizeof(images))
#define EEPROM_ADDR_TEXT (EEPROM_ADDR_ANIMATIONS+sizeof(animations))
#define EEPROM_DATA_LEN (EEPROM_ADDR_TEXT+sizeof(textInfo))

// magic byte - by reading this we know that eeprom was really written by this sketch
#define EEPROM_MAGIC 48


// all data in RAM memory are initialized to their default values
// that's why we can perform initial save if EEPROM looks empty or is in different version
void eepromSetup() {
  if (EEPROM_DATA_LEN > EEPROM.length()) {
    Serial.println(F("-EEPROM data too large"));
    while (1);
  }

  uint8_t magic = EEPROM.read(EEPROM_ADDR_START);
  if (magic == EEPROM_MAGIC) {
    eepromLoad();
  } else {
    eepromSave();
  }
}

void eepromLoad() {
  EEPROM.get(EEPROM_ADDR_IMAGES, images);
  EEPROM.get(EEPROM_ADDR_ANIMATIONS, animations);
  EEPROM.get(EEPROM_ADDR_TEXT, textInfo);
  displaySetBrightness(EEPROM.read(EEPROM_ADDR_BRIGHTNESS));
  modeSet(EEPROM.read(EEPROM_ADDR_MODE));
}

void eepromSave() {
  EEPROM.write(EEPROM_ADDR_BRIGHTNESS, displayBrightness);
  EEPROM.write(EEPROM_ADDR_MODE, modeCurrent);
  EEPROM.put(EEPROM_ADDR_IMAGES, images);
  EEPROM.put(EEPROM_ADDR_ANIMATIONS, animations);
  EEPROM.put(EEPROM_ADDR_TEXT, textInfo);
  EEPROM.write(EEPROM_ADDR_START, EEPROM_MAGIC); // write magic byte last -> protection against reset while writing
}


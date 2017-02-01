#define EEPROM_ADDR_START 0
#define EEPROM_ADDR_BRIGHTNESS 1
#define EEPROM_ADDR_MODE 2
#define EEPROM_ADDR_CLOCK_MODE 3
#define EEPROM_ADDR_TRESHOLD 4
#define EEPROM_ADDR_TIME 6
#define EEPROM_DATA_LEN (EEPROM_ADDR_TIME+sizeof(timeCurrent))

// magic byte - by reading this we know that eeprom was really written by this sketch
#define EEPROM_MAGIC 115


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
  displaySetBrightness(EEPROM.read(EEPROM_ADDR_BRIGHTNESS));
  clockMode = EEPROM.read(EEPROM_ADDR_CLOCK_MODE);

  EEPROM.get(EEPROM_ADDR_TRESHOLD, timeTreshold);
  uint32_t t;
  EEPROM.get(EEPROM_ADDR_TIME, t);
  timeSet(t);

  modeSet(EEPROM.read(EEPROM_ADDR_MODE));
}

void eepromSave() {
  EEPROM.write(EEPROM_ADDR_BRIGHTNESS, displayBrightness);
  EEPROM.write(EEPROM_ADDR_MODE, modeCurrent);
  EEPROM.write(EEPROM_ADDR_CLOCK_MODE, clockMode);
  EEPROM.put(EEPROM_ADDR_TRESHOLD, timeTreshold);
  EEPROM.put(EEPROM_ADDR_TIME, timeCurrent);
  EEPROM.write(EEPROM_ADDR_START, EEPROM_MAGIC); // write magic byte last -> protection against reset while writing
}


#define EEPROM_ADDR_START 0
#define EEPROM_ADDR_TIME 1
#define EEPROM_DATA_LEN (EEPROM_ADDR_TIME+sizeof(currentTime))

// magic byte - by reading this we know that eeprom was really written by this sketch
#define EEPROM_MAGIC 112


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
  EEPROM.get(EEPROM_ADDR_TIME, currentTime);
}

void eepromSave() {
  EEPROM.put(EEPROM_ADDR_TIME, currentTime);
  EEPROM.write(EEPROM_ADDR_START, EEPROM_MAGIC); // write magic byte last -> protection against reset while writing
}


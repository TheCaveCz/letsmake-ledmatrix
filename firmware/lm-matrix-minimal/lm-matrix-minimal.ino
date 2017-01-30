//
//  LedMatrix
//  part of Let's make series
//
//  The Cave, 2017
//  https://thecave.cz
//
//  Licensed under MIT License (see LICENSE file for details)
//

// Pin definitions. If you change something in hardware, change it here too
#define PIN_DATA A2
#define PIN_CLK A0
#define PIN_LOAD A1
#define PIN_BUTTON 2

uint8_t smile1[] = {0b00111100, 0b01000010, 0b10101001, 0b10000101, 0b10000101, 0b10101001, 0b01000010, 0b00111100};
uint8_t smile2[] = {0b00111100, 0b01000010, 0b10100101, 0b10001001, 0b10001001, 0b10100101, 0b01000010, 0b00111100};

void setup() {
  pinMode(PIN_BUTTON, INPUT_PULLUP);

  pinMode(PIN_DATA, OUTPUT);
  pinMode(PIN_CLK, OUTPUT);
  pinMode(PIN_LOAD, OUTPUT);
  digitalWrite(PIN_LOAD, HIGH);

  displaySend(15, 0); // disable display test mode
  displaySend(11, 7); // set maximum scanlimit (8 rows)
  displaySend(9, 0); // disable BCD decoding (only for 7-seg displays)
  displaySetBrightness(8); // set initial brightness
  displaySend(12, 1); // enable display
}

void displaySend(uint8_t *data) {
  for (uint8_t i = 0; i < 8; i++) {
    displaySend(i + 1, data[i]);
  }
}

void loop() {
  if (digitalRead(PIN_BUTTON)) {
    displaySend(smile1);
  } else {
    displaySend(smile2);
  }
}

void displaySetBrightness(int8_t val) {
  displaySend(10, constrain(val, 0, 15));
}

// this is actually sending data to display
void displaySend(uint8_t command, uint8_t data) {
  digitalWrite(PIN_LOAD, LOW);
  shiftOut(PIN_DATA, PIN_CLK, MSBFIRST, command);
  shiftOut(PIN_DATA, PIN_CLK, MSBFIRST, data);
  digitalWrite(PIN_LOAD, HIGH);
}



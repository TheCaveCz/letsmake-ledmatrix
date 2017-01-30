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

uint8_t data[] = {0b00111100, 0b01000010, 0b10101001, 0b10000101, 0b10000101, 0b10101001, 0b01000010, 0b00111100};

void setup() {
  pinMode(PIN_DATA, OUTPUT);
  pinMode(PIN_CLK, OUTPUT);
  pinMode(PIN_LOAD, OUTPUT);
  digitalWrite(PIN_LOAD, HIGH);

  displaySend(15, 0); // disable display test mode
  displaySend(11, 7); // set maximum scanlimit (8 rows)
  displaySend(9, 0); // disable BCD decoding (only for 7-seg displays)
  displaySetBrightness(8); // set initial brightness
  displaySend(12, 1); // enable display

  for (uint8_t i = 0; i < 8; i++) {
    displaySend(i+1, data[i]);
  }
}

void loop() {
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



// Communication with MAX7219 that handles led array

// first column to be displayed (0-15)
uint8_t displayStart;
// circular buffer to hold column based data (each byte is one column, high order byte at the top)
uint8_t displayBuffer[16];
// brightness (0-15)
uint8_t displayBrightness;

void displaySetup() {
  pinMode(PIN_DATA, OUTPUT);
  pinMode(PIN_CLK, OUTPUT);
  pinMode(PIN_LOAD, OUTPUT);
  digitalWrite(PIN_LOAD, HIGH);

  // clear buffer
  memset(displayBuffer, 0, sizeof(displayBuffer));
  displayStart = 0;

  displaySend(15, 0); // disable display test mode
  displaySend(11, 7); // set maximum scanlimit (8 rows)
  displaySend(9, 0); // disable BCD decoding (only for 7-seg displays)
  displaySetBrightness(8); // set initial brightness

  displayRefresh(); // send buffer content to display
  displaySend(12, 1); // enable display
}

void displaySetBrightness(int8_t val) {
  displayBrightness = constrain(val, 0, 15);
  displaySend(10, displayBrightness);
}

// this is actually sending data to display
void displaySend(uint8_t command, uint8_t data) {
  digitalWrite(PIN_LOAD, LOW);
  shiftOut(PIN_DATA, PIN_CLK, MSBFIRST, command);
  shiftOut(PIN_DATA, PIN_CLK, MSBFIRST, data);
  digitalWrite(PIN_LOAD, HIGH);
}

// send buffer content starting with displayStart to display
void displayRefresh() {
  uint8_t pos = displayStart % 16;
  uint8_t row = 9;
  while (--row) {
    displaySend(row, displayBuffer[pos]);
    pos = (pos + 1) % 16;
  }
}


void setup() {
  commSetup();
  buttonSetup();
  displaySetup();
  timeSetup();
  textSetup();
  modeSetup();
  eepromSetup();
  Serial.println("+ready");
}

void loop() {
  commCheck();
  buttonCheck();
  timeLoop();
  modeLoop();
}



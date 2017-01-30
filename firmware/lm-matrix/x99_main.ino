void setup() {
  commSetup();
  buttonSetup();
  displaySetup();
  imagesSetup();
  animationSetup();
  textSetup();
  modeSetup();
  eepromSetup();
  Serial.println("+ready");
}

void loop() {
  commCheck();
  buttonCheck();
  modeLoop();
}



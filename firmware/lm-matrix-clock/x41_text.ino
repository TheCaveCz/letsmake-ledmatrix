#define MAX_TEXT_LEN 10
#define ANIMATION_FRAME_TIME 10

typedef struct {
  uint8_t len;
  char buffer[MAX_TEXT_LEN];
  uint8_t delayFrames;
} TextInfo;

TextInfo textInfo;
uint8_t textPos;
uint8_t textDisplayBufferPos;
uint32_t textLastFrameTime;


void textSetup() {
  memset(textInfo.buffer, 0, sizeof(textInfo.buffer));
  strcpy(textInfo.buffer, "00:00:00 ");
  textInfo.len = strlen(textInfo.buffer);
  textInfo.delayFrames = 10;

  textDisplayBufferPos = displayStart = 0;
  textPos = 0;
  textShowNext();
  textShowNext();
  displayRefresh();
  textLastFrameTime = millis();
}

void textLoop() {
  uint32_t now = millis();
  if (now - textLastFrameTime < textInfo.delayFrames * ANIMATION_FRAME_TIME) return;
  textLastFrameTime = now;

  if ((displayStart + 8) % 16 == textDisplayBufferPos) {
    textShowNext();
  }
  displayStart = (displayStart + 1) % 16;
  displayRefresh();
}

uint8_t textDrawChar(char code, uint8_t pos) {
  if (code < 32 || code > 126) code = '?';

  Character ch;
  memcpy_P(&ch, &textChars[code - 32], sizeof(Character));
  for (uint8_t i = 0; i < ch.cols; i++) {
    displayBuffer[pos] = ch.data[i];
    pos = (pos + 1) % 16;
  }
  displayBuffer[pos] = 0;
  pos = (pos + 1) % 16;
  return pos;
}

void textShowNext() {
  textDisplayBufferPos = textDrawChar(textInfo.buffer[textPos++], textDisplayBufferPos);
  if (textPos >= textInfo.len) textPos = 0;
}


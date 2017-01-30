#define MAX_TEXT_LEN 64

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
  strcpy(textInfo.buffer, "The Cave! ");
  textInfo.len = strlen(textInfo.buffer);
  textInfo.delayFrames = 8;
}

void textEnter() {
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

void textShow(const char * text, uint8_t delayTime, bool enter = true) {
  strncpy(textInfo.buffer, text, sizeof(textInfo.buffer));

  textInfo.len = strlen(text);
  if (textInfo.len == 0) {
    textInfo.len = 1;
    textInfo.buffer[0] = '_';
  }
  if (textInfo.len == 1) {
    textInfo.len = 2;
    textInfo.buffer[1] = ' ';
  }
  if (textInfo.len > MAX_TEXT_LEN) {
    textInfo.len = MAX_TEXT_LEN;
  }
  textInfo.delayFrames = delayTime;

  if (enter) textEnter();
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


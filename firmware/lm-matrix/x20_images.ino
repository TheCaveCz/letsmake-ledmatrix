// Image mode - stores images and displays them

#define MAX_IMAGES 64


typedef struct {
  uint8_t reserved: 7;
  bool ignored: 1; // used for finding suitable images to display

  uint8_t data[8]; // raw image data
} Image;

Image images[MAX_IMAGES];
uint8_t imageCurrent; // id of currently displayed image

const Image defaultImages[] PROGMEM = {
  {0, 0, {0b00111100, 0b01000010, 0b10101001, 0b10000101, 0b10000101, 0b10101001, 0b01000010, 0b00111100}},
  {0, 0, {0b00111100, 0b01000010, 0b10100101, 0b10001001, 0b10001001, 0b10100101, 0b01000010, 0b00111100}},
  {0, 0, {0b01111000, 0b11111100, 0b11111110, 0b01111111, 0b11111110, 0b11111100, 0b01111000, 0b00000000}},
  {0, 0, {0b10111010, 0b11010110, 0b11101110, 0b11110110, 0b11110110, 0b11101110, 0b11010110, 0b10111010}},
  {0, 0, {0b00110000, 0b01110111, 0b10111111, 0b11111011, 0b01110110, 0b00000111, 0b00000001, 0b00000010}},

  {0, 0, {0b00000000, 0b00000000, 0b00011101, 0b11011111, 0b11011111, 0b00011101, 0b00000000, 0b00000000}},
  {0, 1, {0b00000000, 0b00001000, 0b00010001, 0b11011111, 0b11011111, 0b00010001, 0b00001000, 0b00000000}},
  {0, 1, {0b00010000, 0b00010000, 0b00010001, 0b11011111, 0b11011111, 0b00010001, 0b00010000, 0b00010000}},
  {0, 1, {0b00000000, 0b00100000, 0b00010001, 0b11011111, 0b11011111, 0b00010001, 0b00100000, 0b00000000}},
};


void imagesSetup() {
  memset(images, 0xff, sizeof(images)); // for eeprom write - by setting image values to 0xff we are avoiding unnecessary rewrites of eeprom
  memcpy_P(images, defaultImages, sizeof(defaultImages)); // copy defaultImages to memory
  imageCurrent = 0;
}

void imageEnter() {
  // when entering image mode, redisplay current image
  imageShow(imageCurrent);
}

void imageButton() {
  // find and display next image that has ignored == false, or start with image at index 0
  while (1) {
    imageCurrent++;
    if (imageCurrent >= MAX_IMAGES) {
      imageCurrent = 0;
    }
    if (!images[imageCurrent].ignored || imageCurrent == 0) {
      imageShow(imageCurrent);
      return;
    }
  }
}

// display image with specified index
void imageShow(uint8_t newImg) {
  displayStart = 0;
  imageCopyToBuffer(newImg, false);
  displayRefresh();
}

void imageCopyToBuffer(uint8_t imageIndex, bool secondPage) {
  if (imageIndex < MAX_IMAGES) {
    memcpy(&displayBuffer[secondPage ? 8 : 0], &images[imageIndex].data[0], 8);
  } else {
    memset(&displayBuffer[secondPage ? 8 : 0], 0, 8);
  }
}


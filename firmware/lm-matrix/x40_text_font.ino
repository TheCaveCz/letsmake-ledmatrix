
typedef struct {
  uint8_t cols: 3;
  uint8_t reserved: 5;
  uint8_t data[7];
} Character;

const Character textChars[] PROGMEM = {
  {4, 0, {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}}, //32
  {4, 0, {0x60, 0xfa, 0xfa, 0x60, 0x00, 0x00, 0x00}}, //33
  {4, 0, {0x00, 0xc0, 0xc0, 0x00, 0xc0, 0xc0, 0x00}}, //34
  {7, 0, {0x28, 0xfe, 0xfe, 0x28, 0xfe, 0xfe, 0x28}}, //35
  {6, 0, {0x24, 0x74, 0xd6, 0xd6, 0x5c, 0x48, 0x00}}, //36
  {7, 0, {0x62, 0x66, 0x0c, 0x18, 0x30, 0x66, 0x46}}, //37
  {7, 0, {0x0c, 0x5e, 0xf2, 0xba, 0xec, 0x5e, 0x12}}, //38
  {3, 0, {0x20, 0xe0, 0xc0, 0x00, 0x00, 0x00, 0x00}}, //39
  {4, 0, {0x38, 0x7c, 0xc6, 0x82, 0x00, 0x00, 0x00}}, //40
  {4, 0, {0x82, 0xc6, 0x7c, 0x38, 0x00, 0x00, 0x00}}, //41
  {7, 0, {0x44, 0x6c, 0x38, 0xfe, 0x38, 0x6c, 0x44}}, //42
  {6, 0, {0x10, 0x10, 0x7c, 0x7c, 0x10, 0x10, 0x00}}, //43
  {3, 0, {0x01, 0x07, 0x06, 0x00, 0x00, 0x00, 0x00}}, //44
  {6, 0, {0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00}}, //45
  {2, 0, {0x00, 0x00, 0x06, 0x06, 0x00, 0x00, 0x00}}, //46
  {7, 0, {0x06, 0x0c, 0x18, 0x30, 0x60, 0xc0, 0x80}}, //47
  {7, 0, {0x7c, 0xfe, 0x8e, 0x9a, 0xb2, 0xfe, 0x7c}}, //48
  {6, 0, {0x02, 0x42, 0xfe, 0xfe, 0x02, 0x02, 0x00}}, //49
  {6, 0, {0x46, 0xce, 0x9a, 0x92, 0xf6, 0x66, 0x00}}, //50
  {6, 0, {0x44, 0xc6, 0x92, 0x92, 0xfe, 0x6c, 0x00}}, //51
  {7, 0, {0x18, 0x38, 0x68, 0xca, 0xfe, 0xfe, 0x0a}}, //52
  {6, 0, {0xe4, 0xe6, 0xa2, 0xa2, 0xbe, 0x9c, 0x00}}, //53
  {6, 0, {0x3c, 0x7e, 0xd2, 0x92, 0x9e, 0x0c, 0x00}}, //54
  {6, 0, {0xc0, 0xc0, 0x8e, 0x9e, 0xf0, 0xe0, 0x00}}, //55
  {6, 0, {0x6c, 0xfe, 0x92, 0x92, 0xfe, 0x6c, 0x00}}, //56
  {6, 0, {0x60, 0xf2, 0x92, 0x96, 0xfc, 0x78, 0x00}}, //57
  {2, 0, {0x00, 0x66, 0x66, 0x00, 0x00, 0x00, 0x00}}, //58
  {3, 0, {0x01, 0x67, 0x66, 0x00, 0x00, 0x00, 0x00}}, //59
  {5, 0, {0x10, 0x38, 0x6c, 0xc6, 0x82, 0x00, 0x00}}, //60
  {6, 0, {0x24, 0x24, 0x24, 0x24, 0x24, 0x24, 0x00}}, //61
  {5, 0, {0x82, 0xc6, 0x6c, 0x38, 0x10, 0x00, 0x00}}, //62
  {6, 0, {0x40, 0xc0, 0x8a, 0x9a, 0xf0, 0x60, 0x00}}, //63
  {7, 0, {0x7c, 0xfe, 0x82, 0xba, 0xba, 0xf8, 0x78}}, //64
  {6, 0, {0x3e, 0x7e, 0xc8, 0xc8, 0x7e, 0x3e, 0x00}}, //65
  {7, 0, {0x82, 0xfe, 0xfe, 0x92, 0x92, 0xfe, 0x6c}}, //66
  {7, 0, {0x38, 0x7c, 0xc6, 0x82, 0x82, 0xc6, 0x44}}, //67
  {7, 0, {0x82, 0xfe, 0xfe, 0x82, 0xc6, 0x7c, 0x38}}, //68
  {7, 0, {0x82, 0xfe, 0xfe, 0x92, 0xba, 0x82, 0xc6}}, //69
  {7, 0, {0x82, 0xfe, 0xfe, 0x92, 0xb8, 0x80, 0xc0}}, //70
  {7, 0, {0x38, 0x7c, 0xc6, 0x82, 0x8a, 0xce, 0x4e}}, //71
  {6, 0, {0xfe, 0xfe, 0x10, 0x10, 0xfe, 0xfe, 0x00}}, //72
  {4, 0, {0x82, 0xfe, 0xfe, 0x82, 0x00, 0x00, 0x00}}, //73
  {7, 0, {0x0c, 0x0e, 0x02, 0x82, 0xfe, 0xfc, 0x80}}, //74
  {7, 0, {0x82, 0xfe, 0xfe, 0x10, 0x38, 0xee, 0xc6}}, //75
  {7, 0, {0x82, 0xfe, 0xfe, 0x82, 0x02, 0x06, 0x0e}}, //76
  {7, 0, {0xfe, 0xfe, 0x70, 0x38, 0x70, 0xfe, 0xfe}}, //77
  {7, 0, {0xfe, 0xfe, 0x60, 0x30, 0x18, 0xfe, 0xfe}}, //78
  {7, 0, {0x38, 0x7c, 0xc6, 0x82, 0xc6, 0x7c, 0x38}}, //79
  {7, 0, {0x82, 0xfe, 0xfe, 0x92, 0x90, 0xf0, 0x60}}, //80
  {6, 0, {0x78, 0xfc, 0x84, 0x8e, 0xfe, 0x7a, 0x00}}, //81
  {7, 0, {0x82, 0xfe, 0xfe, 0x90, 0x98, 0xfe, 0x66}}, //82
  {6, 0, {0x64, 0xf6, 0xb2, 0x9a, 0xce, 0x4c, 0x00}}, //83
  {6, 0, {0xc0, 0x82, 0xfe, 0xfe, 0x82, 0xc0, 0x00}}, //84
  {6, 0, {0xfe, 0xfe, 0x02, 0x02, 0xfe, 0xfe, 0x00}}, //85
  {6, 0, {0xf8, 0xfc, 0x06, 0x06, 0xfc, 0xf8, 0x00}}, //86
  {7, 0, {0xfe, 0xfe, 0x0c, 0x18, 0x0c, 0xfe, 0xfe}}, //87
  {7, 0, {0xc2, 0xe6, 0x3c, 0x18, 0x3c, 0xe6, 0xc2}}, //88
  {6, 0, {0xe0, 0xf2, 0x1e, 0x1e, 0xf2, 0xe0, 0x00}}, //89
  {7, 0, {0xe2, 0xc6, 0x8e, 0x9a, 0xb2, 0xe6, 0xce}}, //90
  {4, 0, {0xfe, 0xfe, 0x82, 0x82, 0x00, 0x00, 0x00}}, //91
  {7, 0, {0x80, 0xc0, 0x60, 0x30, 0x18, 0x0c, 0x06}}, //92
  {4, 0, {0x82, 0x82, 0xfe, 0xfe, 0x00, 0x00, 0x00}}, //93
  {7, 0, {0x10, 0x30, 0x60, 0xc0, 0x60, 0x30, 0x10}}, //94
  {7, 0, {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01}}, //95
  {3, 0, {0x00, 0x00, 0xc0, 0xe0, 0x20, 0x00, 0x00}}, //96
  {7, 0, {0x04, 0x2e, 0x2a, 0x2a, 0x3c, 0x1e, 0x02}}, //97
  {7, 0, {0x82, 0xfe, 0xfc, 0x12, 0x12, 0x1e, 0x0c}}, //98
  {6, 0, {0x1c, 0x3e, 0x22, 0x22, 0x36, 0x14, 0x00}}, //99
  {7, 0, {0x0c, 0x1e, 0x12, 0x92, 0xfc, 0xfe, 0x02}}, //100
  {6, 0, {0x1c, 0x3e, 0x2a, 0x2a, 0x3a, 0x18, 0x00}}, //101
  {6, 0, {0x12, 0x7e, 0xfe, 0x92, 0xc0, 0x40, 0x00}}, //102
  {7, 0, {0x19, 0x3d, 0x25, 0x25, 0x1f, 0x3e, 0x20}}, //103
  {7, 0, {0x82, 0xfe, 0xfe, 0x10, 0x20, 0x3e, 0x1e}}, //104
  {4, 0, {0x22, 0xbe, 0xbe, 0x02, 0x00, 0x00, 0x00}}, //105
  {6, 0, {0x06, 0x07, 0x01, 0x01, 0xbf, 0xbe, 0x00}}, //106
  {7, 0, {0x82, 0xfe, 0xfe, 0x08, 0x1c, 0x36, 0x22}}, //107
  {4, 0, {0x82, 0xfe, 0xfe, 0x02, 0x00, 0x00, 0x00}}, //108
  {7, 0, {0x3e, 0x3e, 0x18, 0x1c, 0x38, 0x3e, 0x1e}}, //109
  {6, 0, {0x3e, 0x3e, 0x20, 0x20, 0x3e, 0x1e, 0x00}}, //110
  {6, 0, {0x1c, 0x3e, 0x22, 0x22, 0x3e, 0x1c, 0x00}}, //111
  {7, 0, {0x21, 0x3f, 0x1f, 0x25, 0x24, 0x3c, 0x18}}, //112
  {7, 0, {0x18, 0x3c, 0x24, 0x25, 0x1f, 0x3f, 0x21}}, //113
  {7, 0, {0x22, 0x3e, 0x1e, 0x32, 0x20, 0x38, 0x18}}, //114
  {6, 0, {0x12, 0x3a, 0x2a, 0x2a, 0x2e, 0x24, 0x00}}, //115
  {5, 0, {0x20, 0x7c, 0xfe, 0x22, 0x24, 0x00, 0x00}}, //116
  {7, 0, {0x3c, 0x3e, 0x02, 0x02, 0x3c, 0x3e, 0x02}}, //117
  {6, 0, {0x38, 0x3c, 0x06, 0x06, 0x3c, 0x38, 0x00}}, //118
  {7, 0, {0x3c, 0x3e, 0x0e, 0x1c, 0x0e, 0x3e, 0x3c}}, //119
  {7, 0, {0x22, 0x36, 0x1c, 0x08, 0x1c, 0x36, 0x22}}, //120
  {6, 0, {0x39, 0x3d, 0x05, 0x05, 0x3f, 0x3e, 0x00}}, //121
  {6, 0, {0x32, 0x26, 0x2e, 0x3a, 0x32, 0x26, 0x00}}, //122
  {6, 0, {0x10, 0x10, 0x7c, 0xee, 0x82, 0x82, 0x00}}, //123
  {2, 0, {0xfe, 0xfe, 0x00, 0x00, 0x00, 0x00, 0x00}}, //124
  {6, 0, {0x82, 0x82, 0xee, 0x7c, 0x10, 0x10, 0x00}}, //125
  {7, 0, {0x40, 0xc0, 0x80, 0xc0, 0x40, 0xc0, 0x80}}, //126
};
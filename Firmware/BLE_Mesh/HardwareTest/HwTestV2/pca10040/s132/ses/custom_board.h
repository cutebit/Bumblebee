/**
 * Copyright (c) 2014 - 2018, Nordic Semiconductor ASA
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form, except as embedded into a Nordic
 *    Semiconductor ASA integrated circuit in a product or a software update for
 *    such product, must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 * 
 * 3. Neither the name of Nordic Semiconductor ASA nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * 4. This software, with or without modification, must only be used with a
 *    Nordic Semiconductor ASA integrated circuit.
 * 
 * 5. Any software provided in binary form under this license must not be reverse
 *    engineered, decompiled, modified and/or disassembled.
 * 
 * THIS SOFTWARE IS PROVIDED BY NORDIC SEMICONDUCTOR ASA "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY, NONINFRINGEMENT, AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL NORDIC SEMICONDUCTOR ASA OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
#ifndef PCA10040_H
#define PCA10040_H

#ifdef __cplusplus
extern "C" {
#endif

#include "nrf_gpio.h"
#define BOARD_VERSION 3
#if BOARD_VERSION==1
// LEDs definitions for PCA10040
#define LEDS_NUMBER    1
#define LED_1          20
#define LED_START      20
#define LED_STOP       20
#define LEDS_ACTIVE_STATE 0
#define LEDS_INV_MASK  LEDS_MASK
#define BSP_LED_0           LED_1
#define LEDS_LIST {LED_1 }

#define GPIO_NOT_EXIST    0xff
#define GPIO_PIN_NUMBER   20
#define GPIO_PIN_1        GPIO_NOT_EXIST  //P2.1 GND
#define GPIO_PIN_2        26              //P2.2 SDA
#define GPIO_PIN_3        27              //P2.3 SCL
#define GPIO_PIN_4        2               //P2.4 AIN0
#define GPIO_PIN_5        3               //P2.5 AIN1
#define GPIO_PIN_6        9               //P2.6 P0.9
#define GPIO_PIN_7        10              //P2.7 P0.10
#define GPIO_PIN_8        7               //P2.8 P0.7
#define GPIO_PIN_9        6               //P2.9 P0.8
#define GPIO_PIN_10       GPIO_NOT_EXIST  //P2.10 3V3
#define GPIO_PIN_11       GPIO_NOT_EXIST  //P3.1 GND
#define GPIO_PIN_12       20              //P3.2 P0.20 //LED changing in the range 0-3.3V
#define GPIO_PIN_13       18              //P3.3 P0.18
#define GPIO_PIN_14       GPIO_NOT_EXIST  //P3.4 P0.13 //Reset Pin: Always hight 
#define GPIO_PIN_15       19              //P3.5 P0.19
#define GPIO_PIN_16       16              //P3.6 P0.16
#define GPIO_PIN_17       14              //P3.7 P0.14
#define GPIO_PIN_18       GPIO_NOT_EXIST  //P3.8 GND
#define GPIO_PIN_19       18              //P3.9 REPLAY_OUT //Set: 4.6V, clear 0v
#define GPIO_PIN_20       GPIO_NOT_EXIST  //P3.10 5V



//#define GPIO_PIN_LIST { GPIO_PIN_1, GPIO_PIN_2, GPIO_PIN_3, GPIO_PIN_4, GPIO_PIN_5, GPIO_PIN_6, GPIO_PIN_7, GPIO_PIN_8,  GPIO_PIN_9, GPIO_PIN_10 }
#define GPIO_PIN_LIST { GPIO_PIN_1, GPIO_PIN_2, GPIO_PIN_3, GPIO_PIN_4, GPIO_PIN_5, GPIO_PIN_6, GPIO_PIN_7, GPIO_PIN_8,  GPIO_PIN_9, GPIO_PIN_10, \
                        GPIO_PIN_11, GPIO_PIN_12, GPIO_PIN_13, GPIO_PIN_14, GPIO_PIN_15, GPIO_PIN_16, GPIO_PIN_17, GPIO_PIN_18,  GPIO_PIN_19, GPIO_PIN_20}


#define BUTTONS_NUMBER 1

#define BUTTON_START   13
#define BUTTON_1       13
#define BUTTON_STOP    13
#define BUTTON_PULL    NRF_GPIO_PIN_PULLUP

#define BUTTONS_ACTIVE_STATE 0

#define BUTTONS_LIST { BUTTON_1 }

#define BSP_BUTTON_0   BUTTON_1

#define RX_PIN_NUMBER  11
#define TX_PIN_NUMBER  12
#define CTS_PIN_NUMBER 7
#define RTS_PIN_NUMBER 5
#define HWFC           false
#elif BOARD_VERSION==3
//Version 3 board
//LED defination
#define LEDS_NUMBER       1
#define LED_1             20
#define LED_START         20
#define LED_STOP          20
#define LEDS_ACTIVE_STATE 0
#define LEDS_INV_MASK     LEDS_MASK
#define BSP_LED_0         LED_1
#define LEDS_LIST         {LED_1 }

//GPIO defination
#define GPIO_NOT_EXIST    0xff
#define GPIO_PIN_NUMBER   65
//P2
#define GPIO_PIN_1        GPIO_NOT_EXIST  //P2.1 GND
#define GPIO_PIN_2        GPIO_NOT_EXIST  //P2.2 GND
#define GPIO_PIN_3        26              //P2.3 SDA
#define GPIO_PIN_4        27              //P2.4 SCL
#define GPIO_PIN_5        GPIO_NOT_EXIST  //P2.5 3V3
#define GPIO_PIN_6        0               //P2.6 P0.0
#define GPIO_PIN_7        30              //P2.7 P0.30
#define GPIO_PIN_8        1               //P2.8 P0.1
#define GPIO_PIN_9        31              //P2.9 P0.31
#define GPIO_PIN_10       2               //P2.10 P0.2
#define GPIO_PIN_11       6               //P2.11 P0.6
#define GPIO_PIN_12       3               //P2.12 P0.3
#define GPIO_PIN_13       7               //P2.13 P0.7
#define GPIO_PIN_14       9               //P2.14 P0.9
#define GPIO_PIN_15       5               //P2.15 P0.5
#define GPIO_PIN_16       10              //P2.16 P0.10
#define GPIO_PIN_17       4               //P2.17 P0.4
#define GPIO_PIN_18       28              //P2.18 P0.28
#define GPIO_PIN_19       29              //P2.19 P0.29
#define GPIO_PIN_20       25              //P2.20 P0.25

//P3
#define GPIO_PIN_21        GPIO_NOT_EXIST  //P3.1 GND
#define GPIO_PIN_22        GPIO_NOT_EXIST  //P3.2 3V3
#define GPIO_PIN_23        20              //P3.3 P0.20 //LED changes in the range 0-3.3V
#define GPIO_PIN_24        24              //P3.4 P0.24
#define GPIO_PIN_25        18              //P3.5 P0.18
#define GPIO_PIN_26        23              //P3.6 P0.23
#define GPIO_PIN_27        GPIO_NOT_EXIST  //P3.7 P0.13
#define GPIO_PIN_28        GPIO_NOT_EXIST  //P3.8 GND
#define GPIO_PIN_29        19              //P3.9 P0.19
#define GPIO_PIN_30        GPIO_NOT_EXIST  //P3.10 GND
#define GPIO_PIN_31        16              //P3.11 P0.16
#define GPIO_PIN_32        GPIO_NOT_EXIST  //P3.12 GND
#define GPIO_PIN_33        14              //P3.13 P0.14
#define GPIO_PIN_34        8               //P3.14 P0.8
#define GPIO_PIN_35        22              //P3.15 P0.22
#define GPIO_PIN_36        17              //P3.16 P0.17
#define GPIO_PIN_37        GPIO_NOT_EXIST  //P3.17 GND
#define GPIO_PIN_38        GPIO_NOT_EXIST  //P3.18 GND
#define GPIO_PIN_39        GPIO_NOT_EXIST  //P3.19 5V
#define GPIO_PIN_40        GPIO_NOT_EXIST  //P3.20 5V

//P5
#define GPIO_PIN_41        GPIO_NOT_EXIST  //P5.1 3V3
#define GPIO_PIN_42        31              //P5.2 P0.31
#define GPIO_PIN_43        30              //P5.3 P0.30
#define GPIO_PIN_44        GPIO_NOT_EXIST  //P5.4 3V3
#define GPIO_PIN_45        GPIO_NOT_EXIST  //P5.5 RXD
#define GPIO_PIN_46        GPIO_NOT_EXIST  //P5.6 TXD
#define GPIO_PIN_47        GPIO_NOT_EXIST  //P5.7 GND
#define GPIO_PIN_48        GPIO_NOT_EXIST  //P5.8 NC
#define GPIO_PIN_49        GPIO_NOT_EXIST  //P5.9 NC
#define GPIO_PIN_50        15              //P5.10 P0.15 REPLAY_OUT //Set: 4.6V, clear: 0v for Q2
#define GPIO_PIN_51        18              //P5.11 P0.18 REPLAY_OUT //Set: 4.6V, clear: 0v for Q1
#define GPIO_PIN_52        GPIO_NOT_EXIST  //P5.12 P0.13 Button State //Press: 0, release 4.6V
#define GPIO_PIN_53        GPIO_NOT_EXIST  //P5.13 RESET //Reset Pin: Always hight 
#define GPIO_PIN_54        GPIO_NOT_EXIST  //P5.14 5V
#define GPIO_PIN_55        GPIO_NOT_EXIST  //P5.15 GND







//#define GPIO_PIN_LIST { GPIO_PIN_1, GPIO_PIN_2, GPIO_PIN_3, GPIO_PIN_4, GPIO_PIN_5, GPIO_PIN_6, GPIO_PIN_7, GPIO_PIN_8,  GPIO_PIN_9, GPIO_PIN_10 }
#define GPIO_PIN_LIST { GPIO_PIN_1, GPIO_PIN_2, GPIO_PIN_3, GPIO_PIN_4, GPIO_PIN_5, GPIO_PIN_6, GPIO_PIN_7, GPIO_PIN_8,  GPIO_PIN_9, GPIO_PIN_10, \
                        GPIO_PIN_11, GPIO_PIN_12, GPIO_PIN_13, GPIO_PIN_14, GPIO_PIN_15, GPIO_PIN_16, GPIO_PIN_17, GPIO_PIN_18,  GPIO_PIN_19, GPIO_PIN_20, \
                        GPIO_PIN_21, GPIO_PIN_22, GPIO_PIN_23, GPIO_PIN_24, GPIO_PIN_25, GPIO_PIN_26, GPIO_PIN_27, GPIO_PIN_28,  GPIO_PIN_29, GPIO_PIN_30, \
                        GPIO_PIN_31, GPIO_PIN_32, GPIO_PIN_33, GPIO_PIN_34, GPIO_PIN_35, GPIO_PIN_36, GPIO_PIN_37, GPIO_PIN_38,  GPIO_PIN_39, GPIO_PIN_40, \
                        GPIO_PIN_41, GPIO_PIN_42, GPIO_PIN_43, GPIO_PIN_44, GPIO_PIN_45, GPIO_PIN_46, GPIO_PIN_47, GPIO_PIN_48,  GPIO_PIN_49, GPIO_PIN_50, \
                        GPIO_PIN_51, GPIO_PIN_52, GPIO_PIN_53, GPIO_PIN_54, GPIO_PIN_55 }

#define BUTTONS_NUMBER 1

#define BUTTON_START   13
#define BUTTON_1       13
#define BUTTON_STOP    13
#define BUTTON_PULL    NRF_GPIO_PIN_PULLUP

#define BUTTONS_ACTIVE_STATE 0

#define BUTTONS_LIST { BUTTON_1 }

#define BSP_BUTTON_0   BUTTON_1

#define RX_PIN_NUMBER  11
#define TX_PIN_NUMBER  12
#define CTS_PIN_NUMBER 7
#define RTS_PIN_NUMBER 5
#define HWFC           false 
#endif //Board version

#define SPIS_MISO_PIN   28  // SPI MISO signal.
#define SPIS_CSN_PIN    12  // SPI CSN signal.
#define SPIS_MOSI_PIN   25  // SPI MOSI signal.
#define SPIS_SCK_PIN    29  // SPI SCK signal.

#define SPIM0_SCK_PIN   29  // SPI clock GPIO pin number.
#define SPIM0_MOSI_PIN  25  // SPI Master Out Slave In GPIO pin number.
#define SPIM0_MISO_PIN  28  // SPI Master In Slave Out GPIO pin number.
#define SPIM0_SS_PIN    12  // SPI Slave Select GPIO pin number.

#define SPIM1_SCK_PIN   2   // SPI clock GPIO pin number.
#define SPIM1_MOSI_PIN  3   // SPI Master Out Slave In GPIO pin number.
#define SPIM1_MISO_PIN  4   // SPI Master In Slave Out GPIO pin number.
#define SPIM1_SS_PIN    5   // SPI Slave Select GPIO pin number.

#define SPIM2_SCK_PIN   12  // SPI clock GPIO pin number.
#define SPIM2_MOSI_PIN  13  // SPI Master Out Slave In GPIO pin number.
#define SPIM2_MISO_PIN  14  // SPI Master In Slave Out GPIO pin number.
#define SPIM2_SS_PIN    15  // SPI Slave Select GPIO pin number.

// serialization APPLICATION board - temp. setup for running serialized MEMU tests
#define SER_APP_RX_PIN              23    // UART RX pin number.
#define SER_APP_TX_PIN              24    // UART TX pin number.
#define SER_APP_CTS_PIN             2     // UART Clear To Send pin number.
#define SER_APP_RTS_PIN             25    // UART Request To Send pin number.

#define SER_APP_SPIM0_SCK_PIN       27     // SPI clock GPIO pin number.
#define SER_APP_SPIM0_MOSI_PIN      2      // SPI Master Out Slave In GPIO pin number
#define SER_APP_SPIM0_MISO_PIN      26     // SPI Master In Slave Out GPIO pin number
#define SER_APP_SPIM0_SS_PIN        23     // SPI Slave Select GPIO pin number
#define SER_APP_SPIM0_RDY_PIN       25     // SPI READY GPIO pin number
#define SER_APP_SPIM0_REQ_PIN       24     // SPI REQUEST GPIO pin number

// serialization CONNECTIVITY board
#define SER_CON_RX_PIN              24    // UART RX pin number.
#define SER_CON_TX_PIN              23    // UART TX pin number.
#define SER_CON_CTS_PIN             25    // UART Clear To Send pin number. Not used if HWFC is set to false.
#define SER_CON_RTS_PIN             2     // UART Request To Send pin number. Not used if HWFC is set to false.


#define SER_CON_SPIS_SCK_PIN        27    // SPI SCK signal.
#define SER_CON_SPIS_MOSI_PIN       2     // SPI MOSI signal.
#define SER_CON_SPIS_MISO_PIN       26    // SPI MISO signal.
#define SER_CON_SPIS_CSN_PIN        23    // SPI CSN signal.
#define SER_CON_SPIS_RDY_PIN        25    // SPI READY GPIO pin number.
#define SER_CON_SPIS_REQ_PIN        24    // SPI REQUEST GPIO pin number.

#define SER_CONN_CHIP_RESET_PIN     11    // Pin used to reset connectivity chip


// Arduino board mappings
#define ARDUINO_SCL_PIN             27    // SCL signal pin
#define ARDUINO_SDA_PIN             26    // SDA signal pin
#define ARDUINO_AREF_PIN            2     // Aref pin
#define ARDUINO_13_PIN              25    // Digital pin 13
#define ARDUINO_12_PIN              24    // Digital pin 12
#define ARDUINO_11_PIN              23    // Digital pin 11
#define ARDUINO_10_PIN              22    // Digital pin 10
#define ARDUINO_9_PIN               20    // Digital pin 9
#define ARDUINO_8_PIN               19    // Digital pin 8

#define ARDUINO_7_PIN               18    // Digital pin 7
#define ARDUINO_6_PIN               17    // Digital pin 6
#define ARDUINO_5_PIN               16    // Digital pin 5
#define ARDUINO_4_PIN               15    // Digital pin 4
#define ARDUINO_3_PIN               14    // Digital pin 3
#define ARDUINO_2_PIN               13    // Digital pin 2
#define ARDUINO_1_PIN               12    // Digital pin 1
#define ARDUINO_0_PIN               11    // Digital pin 0

#define ARDUINO_A0_PIN              3     // Analog channel 0
#define ARDUINO_A1_PIN              4     // Analog channel 1
#define ARDUINO_A2_PIN              28    // Analog channel 2
#define ARDUINO_A3_PIN              29    // Analog channel 3
#define ARDUINO_A4_PIN              30    // Analog channel 4
#define ARDUINO_A5_PIN              31    // Analog channel 5


#ifdef __cplusplus
}
#endif

#endif // PCA10040_H

/* Copyright (c) 2010 - 2018, Nordic Semiconductor ASA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
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
 */

#ifndef CUSTOM_LEVEL_COMMON_H__
#define CUSTOM_LEVEL_COMMON_H__

#include <stdint.h>
#include "access.h"

/**
 * @defgroup CUSTOM_LEVEL_MODEL Custom Level model
 * This model implements the message based interface required to
 * set multiple bytes information on the server.
 *
 * This implementation of a Custom Level model can be used to set multiple value for the server
 * or script the information from client. The intention of this model
 * is to have a simple example model that can be used as a baseline for constructing
 * your own model.
 *
 *
 * @note When the server has a publish address set (as in the weather station example),
 * the server will publish its state to its publish address every time its state changes.
 *
 * For more information about creating models, see
 * @ref md_doc_getting_started_how_to_models.
 *
 * Model Identification
 * @par
 * Company ID: @ref CUSTOM_LEVEL_COMPANY_ID
 * @par
 * Custom Level Client Model ID: @ref CUSTOM_LEVEL_CLIENT_MODEL_ID
 * @par
 * Custom Level Server Model ID: @ref CUSTOM_LEVEL_SERVER_MODEL_ID
 *
 * List of supported messages:
 * @par
 * @copydoc CUSTOM_LEVEL_OPCODE_SET
 * @par
 * @copydoc CUSTOM_LEVEL_OPCODE_GET
 * @par
 * @copydoc CUSTOM_LEVEL_OPCODE_SET_UNRELIABLE
 * @par
 * @copydoc CUSTOM_LEVEL_OPCODE_STATUS
 *
 * @ingroup MESH_API_GROUP_VENDOR_MODELS
 * @{
 * @defgroup CUSTOM_LEVEL_COMMON Common Custom Level definitions
 * Types and definitions shared between the two Custom Level models.
 * @{
 */

/*lint -align_max(push) -align_max(1) */

/** Vendor specific company ID for Custom Level model */
#define CUSTOM_LEVEL_COMPANY_ID    (ACCESS_COMPANY_ID_NORDIC)

/** Custom Level opcodes. */
typedef enum
{
    CUSTOM_LEVEL_OPCODE_SET = 0xC1,            /**< Custom Level Acknowledged Set. */
    CUSTOM_LEVEL_OPCODE_GET = 0xC2,            /**< Custom Level Get. */
    CUSTOM_LEVEL_OPCODE_SET_UNRELIABLE = 0xC3, /**< Custom Level Set Unreliable. */
    CUSTOM_LEVEL_OPCODE_STATUS = 0xC4          /**< Custom Level Status. */
} custom_level_opcode_t;


typedef struct __attribute((packed))
{
    uint16_t temperature;    /**< temperature  information. */
    uint8_t battery_life;    /**< battery life time. */
    uint8_t humidity;       /**< humidity level. */
} custom_level_msg_set_t;

/** Message format for th Custom Level Set Unreliable message(from client to server). */
typedef struct __attribute((packed))
{
    uint8_t on_off; /**< State to set. */
    uint8_t tid;    /**< Transaction number. */

} custom_level_msg_set_unreliable_t;

/** Message format for the Custom Level Status message (from server to client). */
typedef struct __attribute((packed))
{
    uint8_t present_on_off; /**< Current state. */
} custom_level_msg_status_t;

/*lint -align_max(pop) */

/** @} end of CUSTOM_LEVEL_COMMON */
/** @} end of CUSTOM_LEVEL_MODEL */
#endif /* CUSTOM_LEVEL_COMMON_H__ */

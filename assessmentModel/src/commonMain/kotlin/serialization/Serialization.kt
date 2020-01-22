package org.sagebionetworks.assessmentmodel.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus

val defaultSerialzersModule = imageSerializersModule + buttonSerializersModule + resultSerializersModule

val jsonCoder = Json(context = defaultSerialzersModule)


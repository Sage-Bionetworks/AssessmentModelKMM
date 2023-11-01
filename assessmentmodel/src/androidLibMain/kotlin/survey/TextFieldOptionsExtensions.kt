package org.sagebionetworks.assessmentmodel.survey

import android.text.InputType

/**
 * Get the input type mask to use with Android TextView.inputType field.
 */
fun KeyboardOptions.inputTypeMask(): Int  {

    var mask = keyboardInputType()
    if (isSecureTextEntry) {
        mask = mask or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    mask = mask or capitalizationInputType()

    //TODO: figure out mapping autoCorrect and spellCheck to Android nbrown 03/17/20
    this.spellCheckingType
    this.autocorrectionType

    //TYPE_TEXT_FLAG_NO_SUGGESTIONS
    //TYPE_TEXT_FLAG_AUTO_CORRECT
    //TYPE_TEXT_FLAG_AUTO_COMPLETE

    return mask
}

/**
 * Helper method to convert [AutoCapitalizationType] to Android InputType.
 */
private fun KeyboardOptions.capitalizationInputType(): Int {
    when (autocapitalizationType) {
        AutoCapitalizationType.AllCharacters -> return InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        AutoCapitalizationType.Words -> return InputType.TYPE_TEXT_FLAG_CAP_WORDS
        AutoCapitalizationType.Sentences -> return InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        else -> return InputType.TYPE_NULL
    }
}

/**
 * Helper method to convert [KeyboardType] to Android InputType.
 */
private fun KeyboardOptions.keyboardInputType(): Int  {
    when (keyboardType) {
        KeyboardType.NumberPad -> return InputType.TYPE_CLASS_NUMBER
        KeyboardType.URL -> return InputType.TYPE_TEXT_VARIATION_URI
        KeyboardType.PhonePad -> return InputType.TYPE_CLASS_PHONE
        KeyboardType.NamePhonePad -> return InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        KeyboardType.EmailAddress -> return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        KeyboardType.DecimalPad -> return InputType.TYPE_NUMBER_FLAG_DECIMAL
        else -> return InputType.TYPE_CLASS_TEXT
    }
}
package org.hospitalmanagement.config.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [GermanPhoneValidator::class])
annotation class ValidGermanPhone(
    val message: String = "Telefonnummer muss eine gültige deutsche Nummer sein (z.B. +49 30 12345678 oder 030 12345678)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class GermanPhoneValidator : ConstraintValidator<ValidGermanPhone, String> {
    private val phonePattern = Regex("""^(\+49|0049|0)[1-9][0-9\s\-/\.]{5,14}$""")

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return phonePattern.matches(value)
    }
}

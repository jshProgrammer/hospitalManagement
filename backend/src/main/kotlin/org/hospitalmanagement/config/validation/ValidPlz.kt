package org.hospitalmanagement.config.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PlzValidator::class])
annotation class ValidPlz(
    val message: String = "PLZ muss genau 5 Ziffern enthalten (z.B. 80331)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PlzValidator : ConstraintValidator<ValidPlz, String> {
    private val plzPattern = Regex("^[0-9]{5}$")

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return plzPattern.matches(value)
    }
}

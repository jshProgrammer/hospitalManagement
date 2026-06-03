package org.hospitalmanagement.config.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [BirthdayValidator::class])
annotation class ValidBirthday(
    val message: String = "Geburtsdatum muss im Format YYYY-MM-DD vorliegen und in der Vergangenheit liegen",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class BirthdayValidator : ConstraintValidator<ValidBirthday, String> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE  // YYYY-MM-DD

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return try {
            val date = LocalDate.parse(value, formatter)
            date.isBefore(LocalDate.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }
}

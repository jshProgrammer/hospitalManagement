package org.hospitalmanagement.config.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NameValidator::class])
annotation class ValidName(
    val message: String = "Ungültige Zeichen. Erlaubt sind Buchstaben, Zahlen, Leerzeichen sowie - ' . /",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

// Allow-list gegen Injection-Versuche (XSS, Log-Injection).
// \p{L} deckt Unicode-Buchstaben ab (Umlaute, Akzente, etc.).
// Geblockt werden u.a.: < > " ; = { } ( ) was für Script-/SQL-Injection benötigt wird.
class NameValidator : ConstraintValidator<ValidName, String> {
    private val namePattern = Regex("""^[\p{L}0-9\s\-'./ ]{1,150}$""")

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return namePattern.matches(value)
    }
}

package io.github.syst3ms.tnil

import java.lang.IllegalStateException

val flatVowelForm = VOWEL_FORM.flatMap { it.split("/") }
val animateReferentDescriptions = listOf(
        listOf("monadic speaker (1m), \"I\"", "polyadic speaker (1p), \"we\"", "oneself in a hypothetical/timeless context", "all that I am, that makes me myself"),
        listOf("monadic addressee (2m), \"you (sg.)\"", "polyadic addressee (2p) \"you (pl.)\"", "the addressee in a hypothetical/timeless context", "all that you are, that makes you yourself"),
        listOf("monadic animate 3rd party (ma), \"he/she/they\"", "polyadic animate 3rd party (pa), \"they (pl.)\"", "impersonal animate (IPa), \"one\"", "all that (s)he/they are")
)
val inanimateReferentDescriptions = listOf(
        listOf("monadic inanimate 3rd party (mi), \"it\"", "polyadic inanimate 3rd party (pi), \"them/those\"", "impersonal inanimate (IPi), \"something\"", "all that it/they are"),
        listOf("monadic obviative (mObv)", "polyadic obviative (pObv)", "Nai, \"it\" as a generic concept", "Aai, \"it\" as an abstract referent"),
        listOf("monadic mixed animate+inanimate (mMx)", "polyadic mixed animate+inanimate (pMx)", "impersonal mixed animate+inanimate (IPx)", "everything and everyone, all about the world")
)
val scopes = listOf("{StmDom}", "{StmSub}", "{CaDom}", "{CaSub}", "{Form}", "{All}")

fun parseCaseAffixVowel(v: String, secondHalf: Boolean) : Case? {
    val i = VOWEL_FORM.indexOfFirst { it eq v }
    if (i == -1 || secondHalf && i % 9 == 7) // one of the unused values
        return null
    return if (secondHalf) {
        when {
            i % 9 == 8 -> Case.values()[36 + i - i/9 - 1] // We have to move back 1 for each 8th vowel form that's skipped
            else -> Case.values()[36 + i - i/9] // Likewise
        }
    } else {
        Case.values()[i]
    }
}

fun parseCd(c: String) : Pair<List<Precision>, Int> {
    val i = CD_CONSONANTS.indexOf(c.defaultForm())
    var flag = 0
    if ((i / 4) % 2 == 1)
        flag = flag or ALT_VF_FORM
    if ((i / 4) >= 2)
        flag = flag or SLOT_THREE_PRESENT
    return listOf<Precision>(Incorporation.values()[(i % 4) / 2], Version.values()[i % 2]) to flag
}

fun parseVnPatternOne(v: String, precision: Int, ignoreDefault: Boolean): String? {
    val i = VOWEL_FORM.indexOfFirst { it eq v }
    if (i == -1 || i in 36..71)
        return null
    return when {
        i < 9 -> Valence.values()[i % 9].toString(precision, ignoreDefault)
        i < 18 -> Phase.values()[i % 9].toString(precision, ignoreDefault)
        i < 27 -> effectString(precision, i % 9)
        else -> Level.values()[i % 9].toString(precision, false) + (if (i >= 72 && precision > 0) "(abs)" else if (i >= 72) "a" else "")
    }
}

fun effectString(precision: Int, effectIndex: Int): String? {
    val ben = Effect.BENEFICIAL.toString(precision)
    val det = Effect.DETRIMENTAL.toString(precision)
    val unk = Effect.UNKNOWN.toString(precision)
    return when (effectIndex) {
        0 -> "1/$ben"
        1 -> "2/$ben"
        2 -> "3/$ben"
        3 -> "all/$ben"
        4 -> unk
        5 -> "all/$det"
        6 -> "3/$det"
        7 -> "2/$det"
        8 -> "1/$det"
        else -> throw IllegalStateException()
    }
}

fun parseVk(s: String) : List<Precision>? = when {
    "a" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.OBSERVATIONAL)
    "ä" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.RECOLLECTIVE)
    "e" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.PURPORTIVE)
    "ë" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.REPORTIVE)
    "i" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.CONVENTIONAL)
    "ö" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.INFERENTIAL)
    "o" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.INTUITIVE)
    "ü" eq s -> listOf(Illocution.ASSERTIVE, Expectation.COGNITIVE, Validation.IMAGINARY)
    "u" eq s -> listOf(Illocution.PERFORMATIVE, Expectation.COGNITIVE)
    "ai" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.OBSERVATIONAL)
    "au" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.RECOLLECTIVE)
    "ei" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.PURPORTIVE)
    "eu" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.REPORTIVE)
    "ëi" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.CONVENTIONAL)
    "ou" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.INFERENTIAL)
    "oi" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.INTUITIVE)
    "iu" eq s -> listOf(Illocution.ASSERTIVE, Expectation.RESPONSIVE, Validation.IMAGINARY)
    "ui" eq s -> listOf(Illocution.PERFORMATIVE, Expectation.RESPONSIVE)
    "ia/oä" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.OBSERVATIONAL)
    "iä/uä" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.RECOLLECTIVE)
    "ie/oë" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.PURPORTIVE)
    "ië/uë" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.REPORTIVE)
    "ëu" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.CONVENTIONAL)
    "uö/iö" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.INFERENTIAL)
    "uo/io" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.INTUITIVE)
    "ue/eö" eq s -> listOf(Illocution.ASSERTIVE, Expectation.EXECUTIVE, Validation.IMAGINARY)
    "ua/aö" eq s -> listOf(Illocution.PERFORMATIVE, Expectation.EXECUTIVE)
    else -> null
}

fun parseVvSimple(s: String) : Pair<List<Precision>, Int>? {
    val i = SIMPLE_VV_FORMS.indexOf(s.defaultForm())
    if (i == -1)
        return null
    return listOf<Precision>(Context.values()[i % 8 / 2], Version.values()[i % 2]) to i / 8
}

fun parseVr(s: String): List<Precision>? {
    val i = VR_FORMS.indexOfFirst { it eq s }
    if (i == -1)
        return null
    return listOf(Function.values()[i % 8 / 4], Specification.values()[i % 4], Stem.values()[i / 8])
}

fun parsePersonalReference(s: String, final: Boolean = false): List<Precision>? = when (val r = s.defaultForm()) {
    "l" -> listOf(Referent.MONADIC_SPEAKER, Effect.NEUTRAL)
    "r" -> listOf(Referent.MONADIC_SPEAKER, Effect.BENEFICIAL)
    "ř" -> listOf(Referent.MONADIC_SPEAKER, Effect.DETRIMENTAL)
    "s" -> listOf(Referent.MONADIC_ADDRESSEE, Effect.NEUTRAL)
    "š" -> listOf(Referent.MONADIC_ADDRESSEE, Effect.BENEFICIAL)
    "ž" -> listOf(Referent.MONADIC_ADDRESSEE, Effect.DETRIMENTAL)
    "n" -> listOf(Referent.POLYADIC_ADDRESSEE, Effect.NEUTRAL)
    "t" -> listOf(Referent.POLYADIC_ADDRESSEE, Effect.BENEFICIAL)
    "d" -> listOf(Referent.POLYADIC_ADDRESSEE, Effect.DETRIMENTAL)
    "m" -> listOf(Referent.MONADIC_ANIMATE_THIRD_PARTY, Effect.NEUTRAL)
    "p" -> listOf(Referent.MONADIC_ANIMATE_THIRD_PARTY, Effect.BENEFICIAL)
    "b" -> listOf(Referent.MONADIC_ANIMATE_THIRD_PARTY, Effect.DETRIMENTAL)
    "ň" -> listOf(Referent.POLYADIC_ANIMATE_THIRD_PARTY, Effect.NEUTRAL)
    "k" -> listOf(Referent.POLYADIC_ANIMATE_THIRD_PARTY, Effect.BENEFICIAL)
    "g" -> listOf(Referent.POLYADIC_ANIMATE_THIRD_PARTY, Effect.DETRIMENTAL)
    "z" -> listOf(Referent.MONADIC_INANIMATE_THIRD_PARTY, Effect.NEUTRAL)
    "ţ" -> listOf(Referent.MONADIC_INANIMATE_THIRD_PARTY, Effect.BENEFICIAL)
    "ḑ" -> listOf(Referent.MONADIC_INANIMATE_THIRD_PARTY, Effect.DETRIMENTAL)
    "tļ" -> listOf(Referent.POLYADIC_INANIMATE_THIRD_PARTY, Effect.NEUTRAL)
    "f" -> listOf(Referent.POLYADIC_INANIMATE_THIRD_PARTY, Effect.BENEFICIAL)
    "v" -> listOf(Referent.POLYADIC_INANIMATE_THIRD_PARTY, Effect.DETRIMENTAL)
    "x" -> listOf(Referent.MIXED_THIRD_PARTY, Effect.NEUTRAL)
    "c" -> listOf(Referent.MIXED_THIRD_PARTY, Effect.BENEFICIAL)
    "ż" -> listOf(Referent.MIXED_THIRD_PARTY, Effect.DETRIMENTAL)
    "th" -> listOf(Referent.OBVIATIVE, Effect.NEUTRAL)
    "ph" -> listOf(Referent.OBVIATIVE, Effect.BENEFICIAL)
    "kh" -> listOf(Referent.OBVIATIVE, Effect.DETRIMENTAL)
    "tç" -> listOf(Referent.ANIMATE_IMPERSONAL, Effect.NEUTRAL)
    "pç" -> listOf(Referent.ANIMATE_IMPERSONAL, Effect.BENEFICIAL)
    "kç" -> listOf(Referent.ANIMATE_IMPERSONAL, Effect.DETRIMENTAL)
    "çn", "nç" -> if (final || r == "çn") listOf<Precision>(Referent.INANIMATE_IMPERSONAL, Effect.NEUTRAL) else null
    "çm", "mç" -> if (final || r == "çm") listOf<Precision>(Referent.INANIMATE_IMPERSONAL, Effect.BENEFICIAL) else null
    "çň", "ňç" -> if (final || r == "çň") listOf<Precision>(Referent.INANIMATE_IMPERSONAL, Effect.DETRIMENTAL) else null
    "çl", "lç" -> if (final || r == "çl") listOf<Precision>(Referent.NOMIC_REFERENT, Effect.NEUTRAL) else null
    "çr", "rç" -> if (final || r == "çr") listOf<Precision>(Referent.NOMIC_REFERENT, Effect.BENEFICIAL) else null
    "çř", "řç" -> if (final || r == "çř") listOf<Precision>(Referent.NOMIC_REFERENT, Effect.DETRIMENTAL) else null
    "rr" -> listOf(Referent.ABSTRACT_REFERENT, Effect.NEUTRAL)
    "č" -> listOf(Referent.ABSTRACT_REFERENT, Effect.BENEFICIAL)
    "j" -> listOf(Referent.ABSTRACT_REFERENT, Effect.DETRIMENTAL)
    else -> null
}

fun String.isGlottalCa(): Boolean {
    if (startsWith("'")) {
        return true
    } else if (length >= 3) {
        val pre = take(2)
        if (pre[0] == pre[1]) {
            if (pre[0] == 'r' ||
                    pre[0] == 'l' ||
                    pre[0].toString() in NASALS ||
                    pre[0].toString() in FRICATIVES ||
                    pre[0].toString() in AFFRICATES) {
                return true
            } else if (pre[0].toString() in STOPS && this[2].toString() in listOf("l", "r", "ř", "w", "y")) {
                return true
            }
        }
    }
    return false
}

fun parseCa(s: String) : List<Precision>? {
    val elements = arrayListOf<Precision>()
    var original = s.defaultForm()
    if (original.isEmpty())
        return null
    when (original) {
        "l" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.MONADIC, Essence.NORMAL)
        "ř" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.MONADIC, Essence.REPRESENTATIVE)
        "r" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.POLYADIC, Essence.NORMAL)
        "tļ" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.POLYADIC, Essence.REPRESENTATIVE)
        "v" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.NOMIC, Essence.NORMAL)
        "lm" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.NOMIC, Essence.REPRESENTATIVE)
        "ẓ" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.ABSTRACT, Essence.NORMAL)
        "ln" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.ABSTRACT, Essence.REPRESENTATIVE)
        "d" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.ASSOCIATIVE, Perspective.MONADIC, Essence.NORMAL)
        "g" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.COALESCENT, Perspective.MONADIC, Essence.NORMAL)
        "b" -> return listOf(Uniplex.SPECIFIC, Extension.DELIMITIVE, Affiliation.VARIATIVE, Perspective.MONADIC, Essence.NORMAL)
        "lţ" -> return listOf(Uniplex.POTENTIAL, Extension.DELIMITIVE, Affiliation.CONSOLIDATIVE, Perspective.MONADIC, Essence.NORMAL)
    }
    original = original.replace("mz", "mm")
        .replace("nd", "nn")
        .replace("sf", "pp")
        .replace("šf", "kk")
        .replace("sţ", "tt")
        .replace("j", "čy")
        .replace("nž", "çy")
        .replace("nz", "ňy")
        .replace("v(?=.)".toRegex(), "nf")
        .replace("fš(?=.)".toRegex(), "kf")
        .replace("fs(?=.)".toRegex(), "tf")
        .replace("ng", "ňk")
        .replace("mb", "np")
        .replace("ḑ", "tţ")
        .replace("č", "tš")
        .replace("c", "ts")
        .replace("rç", "ţç")
        .replace("rţ", "ţţ")
        .replace("rf", "ţf")
        .replace("ž", "ţš")
        .replace("z", "ţs")
    val a = when {
        original.endsWith("ř") -> {
            elements.add(Perspective.MONADIC)
            elements.add(Essence.REPRESENTATIVE)
            1
        }
        original.endsWith("r") || original.endsWith("v") -> {
            if (original.endsWith("v") && !(original.startsWith("ř") || original.getOrNull(original.length - 2) in listOf('b', 'd', 'g')))
                return null
            elements.add(Perspective.POLYADIC)
            elements.add(Essence.NORMAL)
            1
        }
        original.endsWith("ļ") -> {
            if (original.length == 1)
                return null
            elements.add(Perspective.POLYADIC)
            elements.add(Essence.REPRESENTATIVE)
            1
        }
        original.endsWith("w") -> {
            elements.add(Perspective.NOMIC)
            elements.add(Essence.NORMAL)
            1
        }
        original.endsWith("m") || original.endsWith("h") -> {
            if (original.endsWith("h") &&
                    (original.length < 3
                            || original[original.lastIndex - 1].toString() !in STOPS
                            || original[original.lastIndex - 2].toString() !in FRICATIVES)) {
                return null
            } else if (original.length == 1) {
                elements.add(Perspective.MONADIC)
                elements.add(Essence.NORMAL)
                0
            } else {
                elements.add(Perspective.NOMIC)
                elements.add(Essence.REPRESENTATIVE)
                1
            }
        }
        original.endsWith("y") -> {
            if (original.length == 1)
                return null
            elements.add(Perspective.ABSTRACT)
            elements.add(Essence.NORMAL)
            1
        }
        original.endsWith("n") || original.endsWith("ç") -> {
            if (original.endsWith("ç") &&
                    (original.length < 3
                            || original[original.lastIndex - 1].toString() !in STOPS
                            || original[original.lastIndex - 2].toString() !in FRICATIVES)) {
                return null
            } else if (original.length == 1) {
                elements.add(Perspective.MONADIC)
                elements.add(Essence.NORMAL)
                0
            } else {
                elements.add(Perspective.ABSTRACT)
                elements.add(Essence.REPRESENTATIVE)
                1
            }
        }
        else -> {
            elements.add(Perspective.MONADIC)
            elements.add(Essence.NORMAL)
            0
        }
    }
    original = original.dropLast(a)
    when (original) {
        "d" -> {
            elements.add(0, Affiliation.ASSOCIATIVE)
            return elements
        }
        "g" -> {
            elements.add(0, Affiliation.COALESCENT)
            return elements
        }
        "b" -> {
            elements.add(0, Affiliation.VARIATIVE)
            return elements
        }
    }
    val b = when (original.lastOrNull()) { // Dirty hack exploiting the fact that in Kotlin, 'void' functions return a singleton object called Unit
        't' -> if (original.length == 1 || original matches "[rř][ptk]".toRegex()) {
            elements.add(0, Affiliation.CONSOLIDATIVE)
            null
        } else {
            elements.add(0, Affiliation.ASSOCIATIVE)
        }
        'k' -> if (original.length == 1 || original matches "[rř][ptk]".toRegex()) {
            elements.add(0, Affiliation.CONSOLIDATIVE)
            null
        } else {
            elements.add(0, Affiliation.COALESCENT)
        }
        'p' -> if (original.length == 1 || original matches "[rř][ptk]".toRegex()) {
            elements.add(0, Affiliation.CONSOLIDATIVE)
            null
        } else {
            elements.add(0, Affiliation.VARIATIVE)
        }
        else -> {
            elements.add(0, Affiliation.CONSOLIDATIVE)
            null
        }
    }
    if (b == Unit)
        original = original.dropLast(1)
    val c = when (original.lastOrNull()) {
        's' -> elements.add(0, Extension.PROXIMAL)
        'š' -> elements.add(0, Extension.INCIPIENT)
        'f' -> elements.add(0, Extension.ATTENUATIVE)
        'ţ' -> elements.add(0, Extension.GRADUATIVE)
        'ç' -> elements.add(0, Extension.DEPLETIVE)
        else -> {
            elements.add(0, Extension.DELIMITIVE)
            null
        }
    }
    if (c == Unit)
        original = original.dropLast(1)
    when (original) {
        "ţ" -> elements.add(0, Uniplex.POTENTIAL)
        "rt" -> {
            elements.add(0, Connectedness.SEPARATE)
            elements.add(0, Similarity.DUPLEX_SIMILAR)
        }
        "rk" -> {
            elements.add(0, Connectedness.CONNECTED)
            elements.add(0, Similarity.DUPLEX_SIMILAR)
        }
        "rp" -> {
            elements.add(0, Connectedness.FUSED)
            elements.add(0, Similarity.DUPLEX_SIMILAR)
        }
        "rn" -> {
            elements.add(0, Connectedness.SEPARATE)
            elements.add(0, Similarity.DUPLEX_DISSIMILAR)
        }
        "rň" -> {
            elements.add(0, Connectedness.CONNECTED)
            elements.add(0, Similarity.DUPLEX_DISSIMILAR)
        }
        "rm" -> {
            elements.add(0, Connectedness.FUSED)
            elements.add(0, Similarity.DUPLEX_DISSIMILAR)
        }
        "řt" -> {
            elements.add(0, Connectedness.SEPARATE)
            elements.add(0, Similarity.DUPLEX_FUZZY)
        }
        "řk" -> {
            elements.add(0, Connectedness.CONNECTED)
            elements.add(0, Similarity.DUPLEX_FUZZY)
        }
        "řp" -> {
            elements.add(0, Connectedness.FUSED)
            elements.add(0, Similarity.DUPLEX_FUZZY)
        }
        "t" -> {
            elements.add(0, Connectedness.SEPARATE)
            elements.add(0, Similarity.MULTIPLEX_SIMILAR)
        }
        "k" -> {
            elements.add(0, Connectedness.CONNECTED)
            elements.add(0, Similarity.MULTIPLEX_SIMILAR)
        }
        "p" -> {
            elements.add(0, Connectedness.FUSED)
            elements.add(0, Similarity.MULTIPLEX_SIMILAR)
        }
        "n" -> {
            elements.add(0, Connectedness.SEPARATE)
            elements.add(0, Similarity.MULTIPLEX_DISSIMILAR)
        }
        "ň" -> {
            elements.add(0, Connectedness.CONNECTED)
            elements.add(0, Similarity.MULTIPLEX_DISSIMILAR)
        }
        "m" -> {
            elements.add(0, Connectedness.FUSED)
            elements.add(0, Similarity.MULTIPLEX_DISSIMILAR)
        }
        "lt" -> {
            elements.add(0, Connectedness.SEPARATE)
            elements.add(0, Similarity.MULTIPLEX_FUZZY)
        }
        "lk" -> {
            elements.add(0, Connectedness.CONNECTED)
            elements.add(0, Similarity.MULTIPLEX_FUZZY)
        }
        "lp" -> {
            elements.add(0, Connectedness.FUSED)
            elements.add(0, Similarity.MULTIPLEX_FUZZY)
        }
        "" -> elements.add(0, Uniplex.SPECIFIC)
        else -> return null
    }
    return elements
}

internal fun perspectiveIndexFromCa(ca: List<Precision>) = Perspective.values().indexOf(ca[ca.lastIndex - 1] as Perspective)

fun scopeToString(letter: String, ignoreDefault: Boolean): String? {
    val i = SCOPING_VALUES.indexOf(letter.defaultForm())
    return when {
        i == -1 -> null
        i % 6 == 0 && ignoreDefault -> ""
        else -> scopes[i % 6]
    }
}
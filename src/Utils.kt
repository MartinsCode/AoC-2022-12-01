import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * Cut string into array of Strings, each of equal length len.
 */
fun cutString(text: String, len: Int): MutableList<String> {
    val list = mutableListOf<String>()
    for (i in text.indices step len) {
        list.add(text.substring(i, minOf( i + len, text.length)))
    }
    return list
}

/**
 * Removes spaces around string and converts to Int.
 */
fun intify(text: String): Int = text.trim().toInt()
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

/**
 * Permutate a list or ordered collection
 */
fun permutate(list_: List<String>): MutableList<MutableList<String>> {
    var sublist: MutableList<String> = mutableListOf()
    var list = list_.toMutableList()
    val permutations = mutableListOf<MutableList<String>>()
    if (list_.size > 1) {
        for (i in list) {
            list = mutableListOf()
            list.addAll(list_)
     //       println(sublist)
            list.remove(i)
            val subpermutations = permutate(list).toMutableList()
            subpermutations.forEach {
                val sublist = mutableListOf(i)
                sublist.addAll(it)
                permutations.add(sublist)
            }
        }
    } else {
        permutations.add(list)
    }
    return permutations
}

fun permutateMaxLength(list_: List<String>, maxLength: Int): MutableList<MutableList<String>> {
    println("Max permutation length: $maxLength")
    var sublist: MutableList<String> = mutableListOf()
    var list = list_.toMutableList()
    val permutations = mutableListOf<MutableList<String>>()
    if (list_.size > 1 ) {
        if (maxLength > 1) {
            for (i in list) {
                list = mutableListOf()
                list.addAll(list_)
                //       println(sublist)
                list.remove(i)
                val subpermutations = permutateMaxLength(list, maxLength - 1).toMutableList()
                subpermutations.forEach {
                    val sublist = mutableListOf(i)
                    sublist.addAll(it)
                    permutations.add(sublist)
                }
            }
        } else {
            list_.forEach{
                permutations.add(mutableListOf(it))
            }
        }
    } else {
        permutations.add(list)
    }
    return permutations
}
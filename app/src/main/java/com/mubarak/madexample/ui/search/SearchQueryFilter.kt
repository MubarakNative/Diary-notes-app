package com.mubarak.madexample.ui.search

/**This [SearchQueryFilter] object helps to handle unwanted special character's from user
 * input query ex: if user enter -1 program crashes (-) represent NOT unary operator in sqlite
 * we need to skip some special character this object [SearchQueryFilter] help us to do that*/

object SearchQueryFilter {

    fun filterQuery(query: String): String {

        // return true if the query doesn't contain any special character false otherwise
        val isBareWord = query.all { character: Char ->
            character.isLetterOrDigit()
                    || character in setOf(
                '_',
                '\u001a'
            ) // isLetterOrDigit skip the _ because it treats as special character But _ is considered
            // as BareWord in sqlite and unicode code point 26 is '\u001a' that represent escape sequence or invalid
        }

        return if (isBareWord) {
            "*$query*"
        } else {
            //  wrap the special character into double-quote to avoid runtime exception
            // Refer this docs for more details [https://sqlite.org/fts5.html#fts5_strings]
            "\"${query.replace(oldValue = "\"", newValue = "\"\"")}\""
        }

    }

}
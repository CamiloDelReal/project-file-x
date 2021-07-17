package org.xapps.apps.filex.core.utils


sealed class Failure {

    data class Exception(val description: String?): Failure()

    object Database: Failure()

}
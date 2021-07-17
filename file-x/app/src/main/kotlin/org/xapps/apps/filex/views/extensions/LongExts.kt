package org.xapps.apps.filex.views.extensions


fun Long.toStorageSize(): String {
    var size = this.toDouble()
    var aux = size / 1024
    var unit = ""

    if(aux < 1) {
        unit = "B"
    } else {
        size = aux
        aux /= 1024
        if(aux < 1) {
            unit = "KB"
        } else {
            size = aux
            aux /= 1024
            if(aux < 1) {
                unit = "MB"
            } else {
                size = aux
                aux /= 1024
                if(aux < 1) {
                    unit = "GB"
                } else {
                    size = aux
                    aux /= 1024
                    if(aux < 1) {
                        unit = "TB"
                    } else {
                        size = aux
                        aux /= 1024
                        if(aux < 1) {
                            unit = "PB"
                        }
                    }
                }
            }
        }
    }

//    size = Math.rint(size * 100) / 100

    return String.format("%.2f %s", size, unit)
}
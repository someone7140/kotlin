fun main(args: Array<String>) {
    // firstNotNullOfは非NULLが入る前提
    val strArray = arrayOf("A", "1", "B", "2")
    val firstNotNullOfSample = strArray.firstNotNullOf {
        it.toIntOrNull()
    }
    println(firstNotNullOfSample)

    val strArray2 = arrayOf("A", "B")
    // このケースだと「No element of the array was transformed to a non-null value」のexceptionが発生
    /*
    val firstNotNullOfSample2 = strArray2.firstNotNullOf {
        it.toIntOrNull()
    }
    println(firstNotNullOfSample2)
    */
    //
    // firstNotNullOfOrNullはNULL値を許容するので、excepitonが発生しない
    val firstNotNullOfSample3 = strArray2.firstNotNullOfOrNull {
        it.toIntOrNull()
    }
    println(firstNotNullOfSample3)
}

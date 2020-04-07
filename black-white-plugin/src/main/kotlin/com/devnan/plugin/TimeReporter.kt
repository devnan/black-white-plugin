import java.util.concurrent.TimeUnit

 class TimeReporter {

    private var start: Long = -1L
    private var lastSplit: Long = -1L
    private lateinit var label: String

    fun start(label: String) {
        if (start != -1L) {
            throw IllegalStateException("already started")
        }
        this.label = label
        start = System.nanoTime()
        lastSplit = start
    }

    fun splitTime(label: String, reportDiffFromLastSplit: Boolean = true) {
        val split = System.nanoTime()
        val diff = if (reportDiffFromLastSplit) { split - lastSplit } else { split - start }
        lastSplit = split
        println("$label: ${TimeUnit.NANOSECONDS.toMillis(diff)} ms.")
    }

    fun stop() {
        val stop = System.nanoTime()
        val diff = stop - start
        println("$label: ${TimeUnit.NANOSECONDS.toMillis(diff)} ms.")
    }
}
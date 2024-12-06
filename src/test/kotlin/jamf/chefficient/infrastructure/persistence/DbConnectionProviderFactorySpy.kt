package jamf.chefficient.infrastructure.persistence

import java.io.InputStream

class DbConnectionProviderFactorySpy : DbConnectionProviderFactory() {
    private var isFileClosed = false

    override fun releaseSystemResources(inputStream: InputStream) {
        isFileClosed = true
    }

    fun isFileClosed(): Boolean {
        return isFileClosed
    }
}
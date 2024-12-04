package jamf.chefficient.infrastructure.configuration

interface BootstrappingService {
    fun startUp()
    fun javalinPort(): Int
}
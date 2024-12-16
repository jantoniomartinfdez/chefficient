package jamf.chefficient.infrastructure.configuration

object ProductionBootstrappingService : BaseBootstrappingService(), BootstrappingService {
    override fun javalinPort(): Int {
        return 7070
    }
}
package jamf.chefficient.infrastructure.configuration

object ServiceLocator {
    private val services: MutableMap<String, Any> = mutableMapOf()

    fun registerService(fullyQualifiedName: String, any: Any) {
        services[fullyQualifiedName] = any
    }

    fun getService(fullyQualifiedName: String): Any {
        if (services.containsKey(fullyQualifiedName)) {
            return services[fullyQualifiedName]!!
        }

        throw ClassNotFoundException("Service $fullyQualifiedName not found!")
    }
}
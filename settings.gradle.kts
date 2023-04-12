
rootProject.name = "JEXT-Reborn"

toolchainManagement {
    jvm { 
        javaRepositories {
            repository("foojay") { 
                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
            }
        }
    }
}


rootProject.name = "JEXT-Reborn"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

toolchainManagement {
    jvm { 
        javaRepositories {
            repository("foojay") { 
                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
            }
        }
    }
}

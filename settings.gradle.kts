rootProject.name = "commerce"

// api
include("modules:api:commerce")

// core
include("modules:core:core-common")
include("modules:core:core-order")

// infra
include("modules:infra:infra-common")
include("modules:infra:infra-order")

// architecture test
include("modules:architecture-test")
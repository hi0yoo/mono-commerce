rootProject.name = "commerce"

include("modules:app")

include("modules:common")
include("modules:common:snowflake")
include("modules:common:auth")

include("modules:product")
include("modules:product:product-api")
include("modules:product:product-core")
include("modules:order")
include("modules:order:order-api")

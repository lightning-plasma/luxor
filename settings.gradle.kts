rootProject.name = "luxor"

include(
    ":web",
    ":application",
    ":domain",
    ":infra:persistence",
    ":infra:s3",
    ":infra:api",
)
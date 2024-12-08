package loadtest

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class LoadSimulation : Simulation() {
    private val httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val scn: ScenarioBuilder = scenario("Simple load test")
        .exec(
            http("GET /")
                .get("/")
                .check(status().shouldBe(200))
        )

    init {
        setUp(
            scn.injectOpen(
                atOnceUsers(10)
            )
        ).protocols(httpProtocol)
    }
}

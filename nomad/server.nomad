job "octopode-cash" {
    datacenters = ["dc1"]
    type = "service"

    group "application" {
        count = 1

        network {
            mode = "bridge"

            port "http" {
                static = 8081
                to = 8080
            }
        }


        service {
            name = "octopode-cash-server"
            port = "8081"


            tags = [
                "traefik.enable=true",
                "traefik.http.routers.octopode.rule=Host(`octopode.app.ex0ns.me`)",
                "traefik.http.routers.octopode.entrypoints=https",
                "traefik.http.routers.octopode.tls=true",
            ]


        }


        task "backend" {
            driver = "docker"

            config {
               image = "registry.ex0ns.me/ex0ns/octopode-cash/octopode-cash:latest"
               force_pull = true
            }

            resources {
               cpu    = 500
               memory = 512
            }
        }
    }
}



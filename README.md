# spring-demo

A small Spring Boot 4 (Java 17) demo application, served behind an nginx reverse proxy, with a full CI/CD pipeline that builds, scans, and deploys it to Kubernetes via Helm.

## Endpoints

| Path                          | Description                                  |
|-------------------------------|-----------------------------------------------|
| `/`                            | Welcome message                              |
| `/hello`                       | Sample greeting                              |
| `/health`                      | Simple text health message                   |
| `/version`                     | App version string                           |
| `/time`                        | Current server time                          |
| `/actuator/health`             | Spring Boot Actuator health (aggregate)      |
| `/actuator/health/liveness`    | Liveness probe (used by Docker/Kubernetes)   |
| `/actuator/health/readiness`   | Readiness probe (used by Docker/Kubernetes)  |

## Running locally

**With Maven:**

```bash
./mvnw spring-boot:run
```

The app listens on port `8080`.

**With Docker Compose** (app + nginx reverse proxy, matching production topology):

```bash
docker compose up -d --build
```

nginx listens on port `80` and proxies to the app on `8080`, adding security headers (CSP, X-Frame-Options, etc.) and gzip compression. See [nginx/nginx.conf](nginx/nginx.conf).

## CI/CD

- **CI** ([.github/workflows/ci.yml](.github/workflows/ci.yml)): runs on every push/PR to `main` — unit tests, secret scanning (Gitleaks), SAST (CodeQL), dependency/license scanning (Trivy), then builds and scans the Docker image before pushing `ajitesh70/spring-demo:<sha>` and `:latest` to Docker Hub (push only on `main`).
- **CD** ([.github/workflows/cd.yml](.github/workflows/cd.yml)): triggered after a successful CI run on `main`. Authenticates to AWS via GitHub OIDC (no long-lived credentials stored in GitHub), points `kubectl`/`helm` at the target EKS cluster, and runs `helm upgrade --install --atomic` — which automatically rolls back to the last successful release if the new one doesn't become healthy in time.

## Deploying with Helm

The Helm chart lives in [helm/spring-demo/](helm/spring-demo/) and deploys two components:

- **app** — the Spring Boot Deployment/Service, with liveness/readiness probes wired to the actuator endpoints above and a non-root security context.
- **nginx** — a Deployment/Service running the same reverse-proxy config as `docker-compose.yml`, loaded from a ConfigMap.

```bash
helm upgrade --install spring-demo ./helm/spring-demo --namespace default --create-namespace
```

Key values in [helm/spring-demo/values.yaml](helm/spring-demo/values.yaml):

- `app.image.tag` — image tag to deploy (CI/CD sets this to the commit SHA)
- `nginx.enabled` — set to `false` to deploy the app only, without the nginx proxy
- `nginx.service.type` — `ClusterIP` by default (internal only); set to `LoadBalancer` for a public endpoint on a cloud provider

## Project structure

```
src/                    Application source and tests
Dockerfile              Multi-stage build for the app image
docker-compose.yml      Local app + nginx stack
nginx/                  nginx image and reverse-proxy config
helm/spring-demo/       Helm chart for Kubernetes/EKS deployment
.github/workflows/      CI and CD pipelines
```

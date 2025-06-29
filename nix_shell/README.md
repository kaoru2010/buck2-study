# Nix Shell Environment

This directory contains a Nix shell environment setup using Docker and [Lix](https://lix.systems/) with Nix Flakes support.

## Usage

First, create the Docker volume for the Nix store:

```bash
docker volume create nix-store
```

### Using Nix Flakes (Recommended)

Start the development environment with flakes:

```bash
# Interactive shell
docker compose run --rm nix-shell nix develop --command bash

# Run specific commands
docker compose run --rm nix-shell nix develop --command "コマンド名"
```

### Using Legacy nix-shell

```bash
docker compose run --rm nix-shell nix-shell --command "コマンド名"
```

## Running Tests

The environment includes [bats-core](https://github.com/bats-core/bats-core) for testing bash scripts. Run tests using:

```bash
# With flakes
docker compose run --rm nix-shell nix develop --command "bats test/bash_version.bats"

# With legacy nix-shell
docker compose run --rm nix-shell nix-shell --run "bats test/bash_version.bats"
```

## Files

- `flake.nix` - Nix flakes configuration with bash and bats
- `shell.nix` - Legacy Nix shell configuration (maintained for compatibility)
- `nix.conf` - Nix configuration to enable experimental features
- `docker-compose.yml` - Docker Compose configuration with pinned image hash
- `test/bash_version.bats` - Test to verify bash v4+ availability
- `flake.lock` - Flakes lock file (auto-generated)

## Requirements

- Docker
- Docker Compose
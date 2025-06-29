# Nix Shell Environment

This directory contains a Nix shell environment setup using Docker and [Lix](https://lix.systems/).

## Usage

First, create the Docker volume for the Nix store:

```bash
docker volume create nix-store
```

Then start the nix-shell environment:

```bash
docker compose run --rm nix-shell
```

## Running Tests

The environment includes [bats-core](https://github.com/bats-core/bats-core) for testing bash scripts. Run tests using:

```bash
docker compose run --rm nix-shell nix-shell --run "bats test/bash_version.bats"
```

## Files

- `shell.nix` - Nix shell configuration with bash and bats
- `docker-compose.yml` - Docker Compose configuration with pinned image hash
- `test/bash_version.bats` - Test to verify bash v4+ availability

## Requirements

- Docker
- Docker Compose
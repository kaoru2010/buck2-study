{
  description = "Buck2 study environment with bash and bats";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            bash
            bats
          ];

          shellHook = ''
            echo "Bash version: $(bash --version | head -n1)"
            if [[ $(bash --version | head -n1 | grep -o '[0-9]\+\.[0-9]\+' | head -n1 | cut -d. -f1) -ge 4 ]]; then
              echo "✓ Bash 4.x+ is available"
            else
              echo "⚠ Warning: Bash version may be older than 4.x"
            fi
          '';
        };
      }
    );
}
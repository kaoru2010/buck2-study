#!/usr/bin/env bats

@test "requires bash version 4 or higher" {
    run bash -c 'echo $BASH_VERSION | cut -d. -f1'
    [ "$status" -eq 0 ]
    [ "$output" -ge 4 ]
}
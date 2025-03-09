# A list of available rules and their signatures can be found here: https://buck2.build/docs/prelude/globals/

genrule(
    name = "hello_world",
    out = "out.txt",
    cmd = "echo BUILT BY BUCK2> $OUT",
)

sh_binary(
    name = 'git_clean_merged_branches',
    main = 'git/git-clean-merged-branches.sh',
)

sh_test(
    name = 'bats_sample',
    test = 'toolchains//:bats_bin',
    args = [
        'bats/sample.bats',
    ],
)

test_suite(
  name = "all_tests",
  tests = [
    ':bats_sample',
    '//spin:all_tests',
  ],
)

load("@toolchains//:android_rule.bzl", "android_rule")
android_rule(
    name = "android_sample",
    srcs = glob(["android/**/*"]),
    settings_gradle = "android/settings.gradle.kts",
    args = [
        ":app:assembleDebug",
    ],
    out = "the_out",
    cmd = "cp -a android/app/build/outputs ${OUT}",
    sub_targets = {
        'app-debug.apk': ['apk/debug/app-debug.apk'],
    },
)

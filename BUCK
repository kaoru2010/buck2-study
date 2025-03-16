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
    test = 'buck2_study_toolchains//:bats_bin',
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

load("@buck2_study_toolchains//:android_rule.bzl", "android_rule")
android_rule(
    name = "android_sample",
    srcs = glob(["android/**/*"]),
    settings_gradle = "android/settings.gradle.kts",
    patch_files = [
        "release_build.patch",
    ],
    patch_command = "patch -p1",
    args = [
        ":app:assembleRelease",
    ],
    out = "the_out",
    cmd = "cp -a android/app/build/outputs ${OUT}",
    sub_targets = {
        'app-release.apk': ['apk/release/app-release.apk'],
    },
)

git_fetch(
    name = "local_git",
    repo = ".",
    rev = "8d4aca0b6ffc2b0a978400546c25e9e7305f728a",
)

android_rule(
    name = "android_sample2",
    srcs = [':local_git'],
    settings_gradle = "local_git/android/settings.gradle.kts",
    args = [
        ":app:assembleDebug",
    ],
    out = "the_out",
    cmd = "cp -a local_git/android/app/build/outputs ${OUT}",
    sub_targets = {
        'app-debug.apk': [ 'apk/debug/app-debug.apk' ],
        'output-metadata.json': [ 'apk/debug/output-metadata.json' ],
    }
)

load("@buck2_study_toolchains//:android_install.bzl", "android_install")
android_install(
    name = "android_install",
    apk_path = ":android_sample2[app-debug.apk]",
    metadata_path = ":android_sample2[output-metadata.json]",
)

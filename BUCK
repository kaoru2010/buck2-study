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
    ':pict_tests',
  ],
)

test_suite(
  name = 'pict_tests',
  tests = [
    ':pict_test',
    ':pict_cucumber_test',
    ':pict_negative_testing_test',
  ],
)

sh_test(
    name = 'pict_test',
    test = 'buck2_study_toolchains//:pict_bin',
    args = [
        'pict/PICT_OS_Browser_InputConstraintDemo.pict',
        '/s',
    ],
)

sh_test(
    name = 'pict_cucumber_test',
    test = ':pict_cucumber',
)

sh_test(
    name = 'pict_negative_testing_test',
    test = ':pict_negative_testing',
)

load("@buck2_study_toolchains//:pict_cucumber_table.bzl", "pict_cucumber_table")
pict_cucumber_table(
    name = "pict_cucumber",
    src = "pict/PICT_OS_Browser_InputConstraintDemo.pict",
)

pict_cucumber_table(
    name = "pict_negative_testing",
    src = "pict/negative_testing.pict",
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

load("@buck2_study_toolchains//:gradle_collect_module_info.bzl", "gradle_collect_module_info")
gradle_collect_module_info(
    name = "gradle_collect_module_info",
    srcs = glob(["android/**/*"]),
    strip_prefix = "android/",
)
